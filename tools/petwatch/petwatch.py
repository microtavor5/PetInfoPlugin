#!/usr/bin/env python3
"""
petwatch - detect pets and pet variants that exist on the OSRS Wiki but are not
yet registered in PetInfoPlugin's PetJsonCreator.java.

How it works
------------
1. Works out which RuneLite API your build compiles against (build.gradle asks
   for 'latest.release'), and loads the NpcID constants for that version.
2. Builds the set of NPC ids the plugin already covers, by parsing every
   `NpcID.SOME_CONSTANT` reference out of PetJsonCreator.java and resolving each
   constant against that API.
3. Builds the set of pet pages on the wiki: members of Category:Pets, unioned
   with every {{plinkt|...}} entry in the tables on the "Pet" article.
4. Pulls the raw wikitext of each pet page and extracts each variant's NPC ids
   from its {{Infobox NPC}} (`|id =` / `|id1 =` ...).
5. Reports any wiki variant whose ids are entirely absent from the plugin, split
   by whether you can act on it yet (see STATUS_HEADINGS).

Cheap repeat checks
-------------------
Steps 3-5 are preceded by a probe that only asks for revision ids, the category
listing and the RuneLite release number - roughly 46KB against ~1.8MB for the
full check. If no pet page has been edited, the category is unchanged, the
RuneLite release is unchanged and PetJsonCreator.java is unchanged, the run
stops there. This makes running daily about as cheap as running weekly, so a
wiki edit that lands late is picked up the next day instead of the next week.

Two state files, with different jobs:
  acknowledged.json - what you have already been told about. Durable, small,
                      meant to be committed.
  cache.json        - revision ids and parsed variants from the last run. Purely
                      an optimisation; deleting it only costs one full check.

Exit codes: 0 = nothing to do, 10 = findings, 1 = error.
"""

from __future__ import annotations

import argparse
import hashlib
import json
import os
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request
from pathlib import Path

WIKI_API = "https://oldschool.runescape.wiki/api.php"
NPCID_URL = (
    "https://raw.githubusercontent.com/runelite/runelite/{ref}/"
    "runelite-api/src/main/java/net/runelite/api/gameval/NpcID.java"
)
# build.gradle asks for 'latest.release', which resolves against this metadata.
RUNELITE_METADATA = "https://repo.runelite.net/net/runelite/client/maven-metadata.xml"
RELEASE_TAG = "runelite-parent-{version}"

DEFAULT_UA = os.environ.get(
    "PETWATCH_UA",
    "PetInfoPlugin-petwatch/1.0 (https://github.com/microtavor5/PetInfoPlugin)",
)

# Pages in Category:Pets that are not themselves a pet.
PAGE_BLOCKLIST = {
    "Pet",
    "Pets",
    "Metamorphosis",
    "Non-pet followers",
    "Karamthulhu (unused pet)",
}


# --------------------------------------------------------------------------
# http
# --------------------------------------------------------------------------
MAX_RESPONSE = 32 * 1024 * 1024  # nothing we fetch is remotely this big


def _request(url: str, headers: dict, retries: int = 3):
    """GET with retries. Returns (body, response headers); body is None on 304.

    4xx answers are not retried: they mean the request was wrong, so repeating
    it only adds load.
    """
    last = None
    for attempt in range(retries):
        req = urllib.request.Request(url, headers=dict(headers, **{"User-Agent": DEFAULT_UA}))
        try:
            with urllib.request.urlopen(req, timeout=60) as resp:
                body = resp.read(MAX_RESPONSE + 1)
                if len(body) > MAX_RESPONSE:
                    raise RuntimeError("response from " + url + " exceeds " + str(MAX_RESPONSE) + " bytes")
                return body, resp.headers
        except urllib.error.HTTPError as exc:
            if exc.code == 304:
                return None, exc.headers
            if 400 <= exc.code < 500:
                raise RuntimeError("GET " + url + " failed: " + str(exc)) from exc
            last = exc
        except (urllib.error.URLError, TimeoutError) as exc:
            last = exc
        time.sleep(2 * (attempt + 1))
    raise RuntimeError("GET " + url + " failed after " + str(retries) + " attempts: " + str(last))


def http_get(url: str, retries: int = 3) -> bytes:
    return _request(url, {}, retries)[0]


def http_get_conditional(url: str, validators: dict):
    """GET with If-None-Match/If-Modified-Since.

    Returns (body, validators). body is None when the server answers 304, which
    costs a few hundred bytes instead of the whole document.
    """
    headers = {}
    if validators.get("etag"):
        headers["If-None-Match"] = validators["etag"]
    if validators.get("last_modified"):
        headers["If-Modified-Since"] = validators["last_modified"]
    body, resp_headers = _request(url, headers)
    if body is None:
        return None, validators
    return body, {
        "etag": resp_headers.get("ETag"),
        "last_modified": resp_headers.get("Last-Modified"),
    }


def api(**params) -> dict:
    params.setdefault("format", "json")
    params.setdefault("formatversion", "2")
    url = WIKI_API + "?" + urllib.parse.urlencode(params)
    return json.loads(http_get(url).decode("utf-8"))


def load_json(path: Path) -> dict:
    if path.exists():
        try:
            return json.loads(path.read_text(encoding="utf-8"))
        except ValueError:
            return {}
    return {}


# --------------------------------------------------------------------------
# plugin side
# --------------------------------------------------------------------------
NPCID_DECL = re.compile(r"public static final int ([A-Z0-9_]+)\s*=\s*(\d+)\s*;")


# A version string is interpolated straight into a URL, so keep it to something
# that cannot escape the path even if the metadata is malformed or hostile.
VERSION_RE = re.compile(r"^[0-9][0-9A-Za-z._-]*$")


def check_version(version: str) -> str:
    if not VERSION_RE.match(version):
        raise RuntimeError("refusing to use implausible RuneLite version " + repr(version))
    return version


def parse_release(xml: str) -> str:
    m = re.search(r"<release>([^<]+)</release>", xml)
    if not m:
        raise RuntimeError("no <release> element in " + RUNELITE_METADATA)
    return check_version(m.group(1).strip())


def released_runelite_version() -> str:
    """The version Gradle's 'latest.release' resolves to (snapshots excluded)."""
    return parse_release(http_get(RUNELITE_METADATA).decode("utf-8"))


def load_npcid_map(state_dir: Path, ref: str, max_age_hours: float = 24.0) -> dict:
    """name -> numeric id, from gameval NpcID.java at a git ref (cached on disk).

    A release tag is immutable, so its cached copy never expires. Only `master`
    is re-fetched once it goes stale.
    """
    safe = re.sub(r"[^A-Za-z0-9._-]", "_", ref)
    cache = state_dir / ("NpcID-" + safe + ".java")
    immutable = ref != "master"
    fresh = (
        cache.exists()
        and cache.stat().st_size > 100_000
        and (immutable or (time.time() - cache.stat().st_mtime) < max_age_hours * 3600)
    )
    if not fresh:
        cache.parent.mkdir(parents=True, exist_ok=True)
        cache.write_bytes(http_get(NPCID_URL.format(ref=ref)))
    text = cache.read_text(encoding="utf-8", errors="replace")
    mapping = {m.group(1): int(m.group(2)) for m in NPCID_DECL.finditer(text)}
    if len(mapping) < 1000:
        raise RuntimeError(
            "NpcID.java at " + ref + " parsed to only " + str(len(mapping)) + " constants; format changed?"
        )
    return mapping


def index_by_number(mapping: dict) -> dict:
    by_number = {}
    for name, num in mapping.items():
        by_number.setdefault(num, []).append(name)
    return by_number


# Only constants actually passed to a Pet entry count as registered. A bare
# NpcID reference in a comment or helper must not make a pet look covered.
PET_ENTRY = re.compile(r"new\s+Pet\s*\([^()]*?NpcID\.([A-Z0-9_]+)", re.S)
ANY_NPCID = re.compile(r"NpcID\.([A-Z0-9_]+)")


def plugin_constant_names(java: Path):
    """Constants registered as pets, and any others merely mentioned in the file."""
    text = java.read_text(encoding="utf-8", errors="replace")
    registered = sorted(set(PET_ENTRY.findall(text)))
    mentioned = sorted(set(ANY_NPCID.findall(text)) - set(registered))
    if not registered and mentioned:
        # the file's shape changed; reporting every pet as missing would be worse
        raise RuntimeError(
            "found NpcID references in " + java.name + " but none inside a `new Pet(...)` call; "
            "PET_ENTRY needs updating for the new style"
        )
    return registered, mentioned


def resolve_plugin_ids(names: list, npcids: dict):
    """Numeric NPC ids the plugin registers, plus constants that do not resolve."""
    ids, unresolved = set(), []
    for name in names:
        if name in npcids:
            ids.add(npcids[name])
        else:
            unresolved.append(name)
    return ids, unresolved


# --------------------------------------------------------------------------
# wiki side
# --------------------------------------------------------------------------
def category_members(category: str) -> list:
    titles, cont = [], {}
    while True:
        data = api(
            action="query",
            list="categorymembers",
            cmtitle=category,
            cmlimit="500",
            cmnamespace="0",
            **cont,
        )
        titles += [m["title"] for m in data["query"]["categorymembers"]]
        if "continue" not in data:
            return titles
        cont = data["continue"]


def _query_pages(titles: list, **extra) -> list:
    """Return (asked-for title, page) for many pages, 50 at a time.

    MediaWiki answers under the *canonical* title, having first normalised what
    we asked for ("Rock_golem" -> "Rock golem") and then followed any redirect.
    Both hops have to be walked forward or results come back under a title the
    caller never asked about, and the page is silently treated as empty.
    """
    out = []
    for i in range(0, len(titles), 50):
        chunk = titles[i : i + 50]
        data = api(action="query", redirects="1", titles="|".join(chunk), **extra)
        query = data.get("query", {})

        forward = {}
        for step in ("normalized", "redirects"):
            for entry in query.get(step, []):
                forward[entry["from"]] = entry["to"]

        def canonical(title):
            seen = set()
            while title in forward and title not in seen:
                seen.add(title)
                title = forward[title]
            return title

        # several asked-for titles can collapse onto one page; keep them all
        by_canonical = {}
        for asked in chunk:
            by_canonical.setdefault(canonical(asked), []).append(asked)

        for page in query.get("pages", []):
            if page.get("missing"):
                continue
            for asked in by_canonical.get(page["title"], [page["title"]]):
                out.append((asked, page))
    return out


def page_meta(titles: list) -> dict:
    """Latest revision id and page id per title. Metadata only - a fraction of
    the content, and the page id is what identifies aliases of the same page."""
    out = {}
    for title, page in _query_pages(titles, prop="revisions", rvprop="ids"):
        revs = page.get("revisions")
        if revs:
            out[title] = {"revid": revs[0]["revid"], "pageid": page.get("pageid")}
    return out


def page_wikitext(titles: list) -> dict:
    """Raw wikitext for many pages."""
    out = {}
    for title, page in _query_pages(titles, prop="revisions", rvprop="content", rvslots="main"):
        revs = page.get("revisions")
        if revs:
            out[title] = revs[0]["slots"]["main"]["content"]
    return out


PLINKT = re.compile(r"\{\{plinkt\|([^|}]+)")


def pets_from_pet_article(text: str) -> dict:
    """{{plinkt|Name}} entries inside the 'List of pets' tables -> section name."""
    found = {}
    section = ""
    in_list = False
    for line in text.splitlines():
        heading = re.match(r"^(={2,3})\s*(.+?)\s*\1\s*$", line)
        if heading:
            level, title = len(heading.group(1)), heading.group(2)
            if level == 2:
                in_list = title == "List of pets"
                section = title if in_list else ""
            elif in_list:
                section = title
            continue
        if not in_list:
            continue
        for m in PLINKT.finditer(line):
            found.setdefault(m.group(1).strip(), section)
    return found


def extract_template(text: str, name: str) -> list:
    """Bodies of every {{<name> ...}} template, brace-matched."""
    bodies, start = [], 0
    needle = "{{" + name
    lower = text.lower()
    needle_l = needle.lower()
    while True:
        i = lower.find(needle_l, start)
        if i == -1:
            return bodies
        # must be followed by a delimiter, not more letters (Infobox Item vs Infobox Item2)
        after = text[i + len(needle) : i + len(needle) + 1]
        if after not in ("", "|", "\n", " ", "}"):
            start = i + 2
            continue
        depth, j = 0, i
        while j < len(text) - 1:
            pair = text[j : j + 2]
            if pair == "{{":
                depth += 1
                j += 2
            elif pair == "}}":
                depth -= 1
                j += 2
                if depth == 0:
                    break
            else:
                j += 1
        bodies.append(text[i:j])
        start = j


COMMENT = re.compile(r"<!--.*?-->", re.S)


def infobox_params(body: str) -> dict:
    """Top-level |key = value pairs of a template body."""
    body = COMMENT.sub("", body)
    params = {}
    depth = 0
    key = None
    buf = []

    def flush():
        if key is not None:
            params[key.strip().lower()] = "".join(buf).strip()

    inner = body[2:-2] if body.endswith("}}") else body[2:]
    i = 0
    while i < len(inner):
        pair = inner[i : i + 2]
        if pair in ("{{", "[["):
            depth += 1
            buf.append(pair)
            i += 2
            continue
        if pair in ("}}", "]]"):
            depth -= 1
            buf.append(pair)
            i += 2
            continue
        ch = inner[i]
        if ch == "|" and depth == 0:
            flush()
            key, buf = None, []
            eq = inner.find("=", i + 1)
            nxt = inner.find("|", i + 1)
            if eq != -1 and (nxt == -1 or eq < nxt):
                key = inner[i + 1 : eq]
                i = eq + 1
            else:
                i += 1
            continue
        buf.append(ch)
        i += 1
    flush()
    return params


def parse_ids(raw: str) -> list:
    return [int(n) for n in re.findall(r"\d+", raw)]


def parse_variants(title: str, text: str) -> list:
    """Each visual variant of a pet with its NPC ids."""
    variants = []
    for body in extract_template(text, "Infobox NPC"):
        p = infobox_params(body)
        indices = sorted(
            {int(m.group(1)) for k in p for m in [re.fullmatch(r"id(\d+)", k)] if m}
        )
        if indices:
            for n in indices:
                label = p.get("version" + str(n)) or p.get("name" + str(n)) or (title + " #" + str(n))
                variants.append({"variant": label.strip(), "ids": parse_ids(p.get("id" + str(n), ""))})
        elif "id" in p:
            label = p.get("name") or title
            variants.append({"variant": label.strip(), "ids": parse_ids(p["id"])})
    # de-duplicate variants that repeat across multiple infoboxes on one page
    seen, unique = set(), []
    for v in variants:
        key = (v["variant"], tuple(sorted(v["ids"])))
        if v["ids"] and key not in seen:
            seen.add(key)
            unique.append(v)
    return unique


# --------------------------------------------------------------------------
# report
# --------------------------------------------------------------------------
def wiki_url(title: str) -> str:
    return "https://oldschool.runescape.wiki/w/" + urllib.parse.quote(title.replace(" ", "_"))


# Whitespace is collapsed first, so the text can never reach the start of a line;
# that leaves only inline syntax (links, images, code, emphasis, HTML, tables) to
# neutralise, and escaping `#`/`-`/`+` as well would just add visible noise.
MD_SPECIAL = re.compile(r"([\\`*_\[\]()<>|~])")


def md(text, limit: int = 120) -> str:
    """Neutralise wiki-supplied text before it goes into a GitHub issue body.

    Variant and section names are free text that any wiki editor controls, and
    the report is posted verbatim as Markdown, so escape the syntax and cap the
    length rather than trusting it.
    """
    flat = re.sub(r"\s+", " ", str(text)).strip()
    if len(flat) > limit:
        flat = flat[: limit - 1] + "…"
    return MD_SPECIAL.sub(r"\\\1", flat)


STATUS_HEADINGS = {
    "ready": "ready to add",
    "pending-release": "waiting on a RuneLite release",
    "no-constant": "waiting on RuneLite to add the NPC ids",
}

STATUS_NOTES = {
    "ready": (
        "The constants exist in the RuneLite API your build resolves to. Add them to\n"
        "`src/test/java/com/micro/petinfo/PetJsonCreator.java`, re-run `PetJsonCreator.main`\n"
        "to regenerate `pets.json`, and bump `VERSION`.\n"
        "\n"
        "If your local build cannot see a constant, Gradle is holding a cached\n"
        "`latest.release`: run `./gradlew --refresh-dependencies build`."
    ),
    "pending-release": (
        "RuneLite has these on `master` but has not shipped them in a release yet, and\n"
        "`build.gradle` pins `latest.release`. Referencing them now would break the build\n"
        "and the Plugin Hub. Wait for the next RuneLite release, then re-run petwatch."
    ),
    "no-constant": (
        "RuneLite has no constant for these ids on `master` yet. Nothing to do until it\n"
        "lands, unless you want to hard-code the raw ids."
    ),
}


def build_report(findings: dict) -> str:
    missing = findings["missing"]
    new_pages = findings["new_pages"]
    api_info = findings["runelite"]

    if not missing and not new_pages and not findings.get("fetch_failures"):
        return (
            "No new pets or variants. The plugin covers every NPC id on the wiki pet "
            "pages, checked against RuneLite " + api_info["release"] + "."
        )

    lines = []
    for status in ("ready", "pending-release", "no-constant"):
        group = [m for m in missing if m["status"] == status]
        if not group:
            continue
        lines.append("### " + str(len(group)) + " pet variant(s) " + STATUS_HEADINGS[status])
        lines.append("")
        by_page = {}
        for m in group:
            by_page.setdefault(m["page"], []).append(m)
        for page in sorted(by_page):
            entries = by_page[page]
            note = " - " + md(entries[0]["section"]) if entries[0].get("section") else ""
            lines.append("- **[" + md(page) + "](" + wiki_url(page) + ")**" + note)
            for e in entries:
                names = ", ".join("`NpcID." + c + "`" for c in e["constants"]) or "no matching NpcID constant"
                ids = ", ".join(str(i) for i in e["ids"])
                label = "" if e["variant"] == page else "*" + md(e["variant"]) + "* - "
                lines.append("  - " + label + "ids `" + ids + "` -> " + names)
        lines.append("")
        lines.append(STATUS_NOTES[status])
        lines.append("")

    if new_pages:
        lines.append("### " + str(len(new_pages)) + " new pet page(s) since the last check")
        lines.append("")
        for p in sorted(new_pages):
            lines.append("- [" + md(p) + "](" + wiki_url(p) + ")")
        lines.append("")

    if findings.get("fetch_failures"):
        lines.append("### Pages that could not be read")
        lines.append("")
        for p in sorted(findings["fetch_failures"]):
            lines.append("- [" + md(p) + "](" + wiki_url(p) + ") - not checked this run")
        lines.append("")

    if findings["unresolved_constants"]:
        lines.append("### Constants in the plugin that the released API does not have")
        lines.append("")
        for name, where in sorted(findings["unresolved_constants"].items()):
            lines.append("- `NpcID." + name + "` - " + where)
        lines.append("")

    lines.append(
        "Checked against RuneLite `" + api_info["release"] + "` (tag `" + api_info["release_tag"] + "`)"
        + (", with `master` consulted for pending ids." if api_info["master_consulted"] else ".")
    )
    return "\n".join(lines)


# --------------------------------------------------------------------------
# main
# --------------------------------------------------------------------------
def main() -> int:
    here = Path(__file__).resolve()
    default_repo = here.parents[2]

    ap = argparse.ArgumentParser(
        description="Detect OSRS Wiki pets/variants missing from PetInfoPlugin.",
    )
    ap.add_argument("--repo", type=Path, default=default_repo, help="PetInfoPlugin checkout (default: %(default)s)")
    ap.add_argument("--state", type=Path, default=here.parent / "state", help="state + cache directory")
    ap.add_argument("--json", type=Path, help="also write raw findings as JSON here")
    ap.add_argument("--report", type=Path, help="also write the markdown report here")
    ap.add_argument("--no-save", action="store_true", help="do not update the stored state")
    ap.add_argument("--all", action="store_true", help="report every missing variant, not just ones new since the last run")
    ap.add_argument("--force", action="store_true", help="do the full check even if the probe finds nothing changed")
    ap.add_argument(
        "--runelite-release",
        metavar="VERSION",
        help="check against this RuneLite version instead of whatever 'latest.release' resolves to (e.g. 1.12.38)",
    )
    ap.add_argument("-v", "--verbose", action="store_true")
    args = ap.parse_args()

    java = args.repo / "src/test/java/com/micro/petinfo/PetJsonCreator.java"
    if not java.exists():
        print("error: cannot find " + str(java), file=sys.stderr)
        return 1

    state_dir = args.state
    state_dir.mkdir(parents=True, exist_ok=True)
    ack_file = state_dir / "acknowledged.json"
    cache_file = state_dir / "cache.json"
    ack = load_json(ack_file)
    cache = load_json(cache_file)
    force = args.force or args.all

    def log(*a):
        if args.verbose:
            print(*a, file=sys.stderr)

    # ---- probe: cheap signals only -------------------------------------
    names, mentioned = plugin_constant_names(java)
    plugin_digest = hashlib.sha256("\n".join(names).encode()).hexdigest()
    log(str(len(names)) + " constants registered as pets"
        + (", " + str(len(mentioned)) + " mentioned but not registered" if mentioned else ""))

    validators = cache.get("runelite_validators", {})
    if args.runelite_release:
        release = args.runelite_release
    elif validators and cache.get("runelite_release"):
        body, validators = http_get_conditional(RUNELITE_METADATA, validators)
        release = cache["runelite_release"] if body is None else parse_release(body.decode("utf-8"))
    else:
        body, validators = http_get_conditional(RUNELITE_METADATA, {})
        release = parse_release(body.decode("utf-8"))
    release_tag = RELEASE_TAG.format(version=release)
    log("runelite 'latest.release' -> " + release)

    cat = category_members("Category:Pets")
    known = cache.get("titles", [])
    meta = page_meta(sorted(set(cat) | set(known) | {"Pet"}))

    if meta.get("Pet", {}).get("revid") != cache.get("pet_article_revid") or not known:
        log("Pet article changed, re-reading its tables")
        sections = pets_from_pet_article(page_wikitext(["Pet"]).get("Pet", ""))
    else:
        sections = cache.get("sections", {})

    titles = sorted((set(cat) | set(sections)) - PAGE_BLOCKLIST)
    absent = [t for t in titles if t not in meta]
    if absent:
        meta.update(page_meta(absent))

    # The category and the Pet article can name the same page differently (one
    # being a redirect). Collapse those so a pet is not reported twice.
    titles, seen_pageid = [], {}
    for t in sorted((set(cat) | set(sections)) - PAGE_BLOCKLIST):
        pageid = meta.get(t, {}).get("pageid")
        if pageid is not None and pageid in seen_pageid:
            log("skipping " + t + ": same page as " + seen_pageid[pageid])
            continue
        if pageid is not None:
            seen_pageid[pageid] = t
        titles.append(t)

    revids = {t: m["revid"] for t, m in meta.items()}

    prev_revids = cache.get("page_revids", {})
    cached_variants = cache.get("page_variants") or {}
    changed = sorted(t for t in titles if revids.get(t) != prev_revids.get(t))
    dropped = sorted(set(cached_variants) - set(titles))

    settled = (
        not changed
        and not dropped
        and cache.get("runelite_release") == release
        and cache.get("plugin_digest") == plugin_digest
        and cache.get("page_variants") is not None
        # never stay quiet if nothing has been reported yet: deleting
        # acknowledged.json is how you ask to be told everything again
        and ack.get("missing_keys") is not None
    )
    log("probe: " + str(len(changed)) + " page(s) edited, " + str(len(dropped)) + " removed")

    if settled and not force:
        print(
            "No change since the last check: "
            + str(len(titles))
            + " pet pages unedited, RuneLite still "
            + release
            + ", plugin unchanged."
        )
        if not args.no_save:
            cache.update({"checked_at": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
                          "runelite_validators": validators})
            cache_file.write_text(json.dumps(cache, indent=2) + "\n", encoding="utf-8")
        return 0

    # ---- full check ----------------------------------------------------
    released = load_npcid_map(state_dir, release_tag)
    released_by_number = index_by_number(released)
    log(str(len(released)) + " constants in the released API")

    npc_cache = {}

    def master():
        if "map" not in npc_cache:
            log("consulting RuneLite master for unreleased ids...")
            npc_cache["map"] = load_npcid_map(state_dir, "master")
            npc_cache["by_number"] = index_by_number(npc_cache["map"])
        return npc_cache["map"]

    def master_by_number():
        master()
        return npc_cache["by_number"]

    plugin_ids, unresolved_names = resolve_plugin_ids(names, released)
    log("plugin registers " + str(len(plugin_ids)) + " npc ids")

    unresolved = {}
    for name in unresolved_names:
        if name in master():
            unresolved[name] = (
                "on RuneLite master but not in " + release + "; the build will fail until the next release"
            )
        else:
            unresolved[name] = "not found in RuneLite master either"

    # only re-download pages that actually changed
    page_variants = {t: v for t, v in cached_variants.items() if t in titles}
    refetch = sorted(set(changed) | {t for t in titles if t not in page_variants})
    fetch_failures = []
    if refetch:
        log("fetching wikitext for " + str(len(refetch)) + " page(s)")
        fetched = page_wikitext(refetch)
        for t in refetch:
            if t in fetched:
                page_variants[t] = parse_variants(t, fetched[t])
            else:
                # never record "no pets here" for a page we failed to read, and
                # do not let the stale revid mark it as checked
                fetch_failures.append(t)
                revids.pop(t, None)
        if fetch_failures:
            log("warning: no wikitext returned for " + str(len(fetch_failures)) + " page(s): "
                + ", ".join(fetch_failures[:5]))

    missing = []
    for title in sorted(page_variants):
        for v in page_variants[title]:
            if any(i in plugin_ids for i in v["ids"]):
                continue
            constants = sorted({c for i in v["ids"] for c in released_by_number.get(i, [])})
            if constants:
                status = "ready"
            else:
                constants = sorted({c for i in v["ids"] for c in master_by_number().get(i, [])})
                status = "pending-release" if constants else "no-constant"
            missing.append(
                {
                    "page": title,
                    "variant": v["variant"],
                    "ids": v["ids"],
                    "constants": constants,
                    "status": status,
                    "section": sections.get(title, ""),
                }
            )

    # status is part of the key, so a variant that becomes buildable when the next
    # RuneLite release lands is reported again rather than staying silent.
    def key_of(m):
        return m["page"] + "::" + m["variant"] + "::" + m["status"]

    prev_pages = set(ack.get("pages", []))
    prev_missing = set(ack.get("missing_keys", []))
    new_pages = sorted(set(titles) - prev_pages) if prev_pages else []
    shown = missing if (args.all or not prev_missing) else [m for m in missing if key_of(m) not in prev_missing]

    findings = {
        "checked_at": time.strftime("%Y-%m-%dT%H:%M:%SZ", time.gmtime()),
        "pages_checked": len(page_variants),
        "pages_refetched": len(refetch),
        "variants_checked": sum(len(v) for v in page_variants.values()),
        "plugin_ids": len(plugin_ids),
        "missing": shown,
        "missing_total": len(missing),
        "new_pages": new_pages,
        "fetch_failures": fetch_failures,
        "unresolved_constants": unresolved,
        "runelite": {
            "release": release,
            "release_tag": release_tag,
            "master_consulted": "map" in npc_cache,
        },
    }

    report = build_report(findings)
    print(report)

    if args.report:
        args.report.write_text(report + "\n", encoding="utf-8")
    if args.json:
        args.json.write_text(json.dumps(findings, indent=2) + "\n", encoding="utf-8")

    if not args.no_save:
        ack_file.write_text(
            json.dumps(
                {
                    "checked_at": findings["checked_at"],
                    "runelite_release": release,
                    "pages": sorted(titles),
                    "missing_keys": sorted(key_of(m) for m in missing),
                },
                indent=2,
            )
            + "\n",
            encoding="utf-8",
        )
        cache_file.write_text(
            json.dumps(
                {
                    "checked_at": findings["checked_at"],
                    "runelite_release": release,
                    "runelite_validators": validators,
                    "plugin_digest": plugin_digest,
                    "pet_article_revid": meta.get("Pet", {}).get("revid"),
                    "sections": sections,
                    "titles": sorted(titles),
                    "page_revids": {t: revids[t] for t in titles if t in revids},
                    "page_variants": page_variants,
                },
                indent=2,
            )
            + "\n",
            encoding="utf-8",
        )

    return 10 if (shown or new_pages or fetch_failures) else 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except Exception as exc:  # noqa: BLE001
        print("petwatch failed: " + str(exc), file=sys.stderr)
        sys.exit(1)
