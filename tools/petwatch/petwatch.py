#!/usr/bin/env python3
"""
petwatch - detect pets and pet variants that exist on the OSRS Wiki but are not
yet registered in PetInfoPlugin's PetJsonCreator.java.

How it works
------------
1. Works out which RuneLite API the plugin builds against (build.gradle asks for
   'latest.release'), and loads the NpcID constants for that version.
2. Builds the set of NPC ids the plugin already covers, by parsing every
   `NpcID.SOME_CONSTANT` passed to a `new Pet(...)` call in PetJsonCreator.java
   and resolving each constant against that API.
3. Builds the set of pet pages on the wiki: members of Category:Pets, unioned
   with every {{plinkt|...}} entry in the tables on the "Pet" article.
4. Pulls the raw wikitext of each pet page and extracts each variant's NPC ids
   from its {{Infobox NPC}} (`|id =` / `|id1 =` ...).
5. Reports any wiki variant whose ids are entirely absent from the plugin, split
   by whether it can be acted on yet (see STATUS_HEADINGS).
6. Reads the drop rate column of the Pet article's tables and, for the same pets,
   the Item sources table (from the wiki's Bucket API), reports rates that
   changed, and compares them against the rates quoted in pets.json. Only pets
   given a concrete rate are considered, so skilling and generic pets drop out.

Cheap repeat checks
-------------------
Steps 3-6 are preceded by a probe that only asks for revision ids, the category
listing and the RuneLite release number - about 26KB against ~1.8MB for the full
check. The run stops there unless a pet page or a drop source page was edited,
the Pet article or the category changed, the RuneLite release moved, or
PetJsonCreator.java or pets.json changed. That makes running daily about as cheap
as running weekly, so a wiki edit that lands late is picked up the next day
instead of the next week.

State, with different jobs:
  acknowledged.json - what has already been reported. Durable, small, meant to
                      be committed.
  cache.json        - revision ids, parsed variants and rates from the last run,
                      plus the NpcID-*.java downloads alongside it. Purely an
                      optimisation; deleting it only costs one full check.

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

# Bumped whenever cache.json's shape changes. A cache from another version is
# discarded rather than half-read, which would silently drop pets.
CACHE_SCHEMA = 4

# The article has listed on the order of 100 pets for years. Far below that means
# something went wrong upstream, and reporting "all clear" would be a lie.
MIN_EXPECTED_PAGES = 50
MAX_SHRINK = 0.8


def _request(url: str, headers: dict, retries: int = 3):
    """GET with retries. Returns (body, response headers); body is None on 304.

    4xx answers are not retried: they mean the request was wrong, so repeating
    it only adds load.
    """
    last = None
    for attempt in range(retries):
        delay = 2 * (attempt + 1)
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
            # a lagging or overloaded server (maxlag answers 503) says how long
            # to wait; honour that rather than our own guess
            retry_after = (exc.headers or {}).get("Retry-After", "") or ""
            if retry_after.strip().isdigit():
                delay = min(int(retry_after.strip()), 60)
        except (urllib.error.URLError, TimeoutError) as exc:
            last = exc
        time.sleep(delay)
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
    # Standard MediaWiki courtesy: if the database replicas are lagging by more
    # than this many seconds, have the server turn us away (503 + Retry-After)
    # instead of adding load while it is already struggling.
    params.setdefault("maxlag", "5")
    url = WIKI_API + "?" + urllib.parse.urlencode(params)
    data = json.loads(http_get(url).decode("utf-8"))
    if "error" in data:
        raise RuntimeError("wiki API error: " + json.dumps(data["error"])[:300])
    return data


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
    if not registered:
        # With no registered ids every pet on the wiki looks missing, so an
        # empty, truncated or restyled file would raise hundreds of false
        # findings. Refuse instead.
        raise RuntimeError(
            "no `new Pet(...)` entries found in " + java.name
            + (
                "; the file has NpcID references, so PET_ENTRY needs updating for a new style"
                if mentioned
                else "; the file looks empty or truncated"
            )
        )
    return registered, mentioned


# `new Pet(PetGroup.OTHER, 16385, INFO)`: an id written out because RuneLite has
# no constant for it yet.
PET_RAW_ID = re.compile(r"new\s+Pet\s*\(\s*[\w.]+\s*,\s*(\d+)\s*[,)]")


def plugin_raw_ids(java: Path) -> set:
    """NPC ids the plugin hard-codes instead of naming an NpcID constant.

    These deliberately do not count as covered: the variant stays on the list so
    that the RuneLite release adding its constant is still reported. They are
    marked as hard-coded in the report, and the commit check reads them to tell
    hard-coding from registering a constant.
    """
    return {int(n) for n in PET_RAW_ID.findall(java.read_text(encoding="utf-8", errors="replace"))}


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
CELL_ATTR = re.compile(r'^[a-zA-Z-]+\s*=\s*(?:"[^"]*"|\S*)\s*\|(?!\|)(.*)$', re.S)


def _split_cells(line: str) -> list:
    """Split a table line on ||, ignoring separators inside {{ }} or [[ ]]."""
    out, buf, depth, i = [], [], 0, 0
    while i < len(line):
        pair = line[i : i + 2]
        if pair in ("{{", "[["):
            depth += 1
            buf.append(pair)
            i += 2
        elif pair in ("}}", "]]"):
            depth -= 1
            buf.append(pair)
            i += 2
        elif pair == "||" and depth == 0:
            out.append("".join(buf))
            buf = []
            i += 2
        else:
            buf.append(line[i])
            i += 1
    out.append("".join(buf))
    return out


def _cell_text(cell: str) -> str:
    """Drop any leading cell attributes ('data-sort-value=2560 | 1/2,560')."""
    cell = cell.strip()
    m = CELL_ATTR.match(cell)
    return (m.group(1) if m else cell).strip()


def strip_footnotes(text: str) -> str:
    """Remove {{efn|...}}, which repeats and qualifies rates rather than stating them."""
    out, i = [], 0
    while i < len(text):
        if text[i:].startswith("{{efn"):
            depth = 0
            while i < len(text):
                if text[i : i + 2] == "{{":
                    depth += 1
                    i += 2
                elif text[i : i + 2] == "}}":
                    depth -= 1
                    i += 2
                    if depth == 0:
                        break
                else:
                    i += 1
            continue
        out.append(text[i])
        i += 1
    return "".join(out)


def parse_pet_rows(text: str) -> dict:
    """Rows of the 'List of pets' tables: page -> {section, rate cell}."""
    rows = {}
    section, in_list = "", False
    lines = text.splitlines()
    i = 0
    while i < len(lines):
        line = lines[i]
        heading = re.match(r"^(={2,3})\s*(.+?)\s*\1\s*$", line)
        if heading:
            level, title = len(heading.group(1)), heading.group(2)
            if level == 2:
                in_list = title == "List of pets"
                section = title if in_list else ""
            elif in_list:
                section = title
            i += 1
            continue
        if not (in_list and line.startswith("|-")):
            i += 1
            continue
        cells = []
        i += 1
        while i < len(lines) and not lines[i].startswith(("|-", "|}", "=")):
            if lines[i].startswith("|"):
                cells.extend(_split_cells(lines[i][1:]))
            elif cells:
                cells[-1] += "\n" + lines[i]
            i += 1
        if not cells:
            continue
        name = PLINKT.search(cells[0])
        if name:
            # columns: pet | source | drop rate | release date
            rate = _cell_text(cells[2]) if len(cells) >= 3 else ""
            rows.setdefault(name.group(1).strip(), {"section": section, "rate": rate})
    return rows


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


# --------------------------------------------------------------------------
# drop rates
# --------------------------------------------------------------------------
RATE_RE = re.compile(r"(\d[\d,]*(?:\.\d+)?)\s*/\s*(\d[\d,]*(?:\.\d+)?)")
# "1/800 to 1/4,000", "1/250-1/1000": a span rather than a fixed rate
RANGE_RE = re.compile(r"\d\s*(?:to|-|–|—)\s*~?\s*\d+\s*/")


def extract_rates(text: str) -> list:
    """Normalised 'a/b' rates, in order, deduplicated."""
    seen, out = set(), []
    for a, b in RATE_RE.findall(text or ""):
        rate = a.replace(",", "") + "/" + b.replace(",", "")
        if rate not in seen:
            seen.add(rate)
            out.append(rate)
    return out


def wiki_rate_info(cell: str) -> dict:
    """What the Pet article states for one pet.

    `stated` is the rate column proper; `supporting` also includes footnotes,
    which often give an alternative framing (per kill vs per unsired) that the
    plugin may legitimately use instead.
    """
    flat = re.sub(r"\s+", " ", cell or "")
    stated = extract_rates(strip_footnotes(flat))
    return {
        "stated": stated,
        "supporting": extract_rates(flat),
        # a span, or an explicit "Varies", is not a number worth diffing
        "ambiguous": bool(RANGE_RE.search(flat)) or "varies" in flat.lower(),
    }


def load_pets_json(pets_json: Path) -> dict:
    """npc id (as a string) -> entry. Empty when the file is absent or unreadable."""
    if not pets_json.exists():
        return {}
    try:
        entries = json.loads(pets_json.read_text(encoding="utf-8"))
    except ValueError:
        return {}
    if not isinstance(entries, dict):
        return {}
    return {str(k): e for k, e in entries.items() if isinstance(e, dict)}


def plugin_rates_by_page(pets_json: Path, page_variants: dict) -> dict:
    """page -> (rates the shipped plugin text states, one sample of that text)."""
    by_id = {k: e["info"] for k, e in load_pets_json(pets_json).items() if e.get("info")}
    out = {}
    for page, variants in page_variants.items():
        infos, rates = [], []
        for v in variants:
            for npc_id in v["ids"]:
                info = by_id.get(str(npc_id))
                if info and info not in infos:
                    infos.append(info)
        for info in infos:
            for rate in extract_rates(info):
                if rate not in rates:
                    rates.append(rate)
        if infos:
            out[page] = {"rates": rates, "info": infos[0]}
    return out


def rates_agree(info: dict, source_rates: list, plugin_rates: list) -> bool:
    """Whether the rates the plugin quotes are ones the wiki gives.

    Every rate in the Pet article's column must be quoted. Beyond those the
    plugin may quote any rate the wiki states elsewhere - a footnote's framing,
    or a source's own rate from the Item sources table - since that is often a
    different route to the same pet (Demonic Brutus for Beef).
    """
    supporting = set(info["supporting"]) | set(source_rates)
    return set(plugin_rates) <= supporting and set(info["stated"]) <= set(plugin_rates)


# --------------------------------------------------------------------------
# drop sources
# --------------------------------------------------------------------------
# A pet page's "Item sources" table is not in its wikitext: it is assembled from
# the {{DropsLine}} entries on each monster and chest page, which the wiki keeps
# in Bucket and publishes through api.php?action=bucket for external tools. One
# query covers every pet.
BUCKET_LIMIT = 500


def bucket_query(items: list) -> str:
    """A Bucket query for the drop lines of every one of `items`.

    Bucket queries are Lua, so each name is a quoted string and the conditions
    are OR-ed together to cover all the pets in one request.
    """
    conds = ",".join("{'item_name',\"" + item + "\"}" for item in items)
    return (
        "bucket('dropsline').select('page_name','item_name','drop_json')"
        ".where(bucket.Or(" + conds + ")).limit(" + str(BUCKET_LIMIT) + ").run()"
    )


def parse_drop_rows(rows: list) -> dict:
    """item -> [{source, rate, approx}], one entry per rate a source states."""
    out = {}
    for row in rows:
        try:
            drop = json.loads(row.get("drop_json") or "{}")
        except (TypeError, ValueError):
            continue
        item, source = row.get("item_name"), row.get("page_name")
        if not isinstance(item, str) or not isinstance(source, str):
            continue
        entries = out.setdefault(item, [])
        # "Alt Rarity" is the second rate a source gives, such as on a slayer task
        for field in ("Rarity", "Alt Rarity"):
            for rate in extract_rates(str(drop.get(field) or "")):
                entry = {"source": source, "rate": rate, "approx": drop.get("Approx") is True}
                if entry not in entries:
                    entries.append(entry)
    for entries in out.values():
        entries.sort(key=lambda e: (e["source"], e["rate"]))
    return out


def drop_sources(items: list) -> dict:
    """item -> its drop sources, for every item asked about (empty if none)."""
    # names are interpolated into a Lua string literal, so refuse anything that
    # could end it early rather than trying to escape it
    askable = sorted(i for i in set(items) if '"' not in i and "\\" not in i)
    out = {i: [] for i in askable}
    for i in range(0, len(askable), 50):
        rows = api(action="bucket", query=bucket_query(askable[i : i + 50])).get("bucket")
        if not isinstance(rows, list):
            raise RuntimeError("Bucket answered without a result list")
        if len(rows) >= BUCKET_LIMIT:
            raise RuntimeError("Bucket returned " + str(len(rows)) + " drop lines, the query limit; results would be cut off")
        for item, entries in parse_drop_rows(rows).items():
            if item in out:
                out[item] = entries
    return out


def source_label(entry: dict) -> str:
    """One drop source as it is shown in a report: "~1/400 (Demonic Brutus)"."""
    return ("~" if entry["approx"] else "") + entry["rate"] + " (" + entry["source"] + ")"


def infobox_tabs(text: str) -> dict:
    """Infobox NPC body -> the {{Multi Infobox}} tab it sits under.

    A page such as Bernese Mountain Dog lists each colour twice, once in a
    Follower infobox and once in a POH one, under `|text1 = Follower` and
    `|text2 = POH`. Those labels are the only thing distinguishing the two sets
    of ids, since the colour names are identical.
    """
    tabs = {}
    for multi in extract_template(text, "Multi Infobox"):
        params = infobox_params(multi)
        for key, value in params.items():
            numbered = re.fullmatch(r"item(\d+)", key)
            if not numbered:
                continue
            tab = re.sub(r"\s+", " ", params.get("text" + numbered.group(1), "")).strip()
            if not tab:
                continue
            for body in extract_template(value, "Infobox NPC"):
                tabs[body] = tab
    return tabs


def qualify_duplicate_labels(variants: list) -> list:
    """Make each variant's label unique within its page.

    The label is part of the key recorded in acknowledged.json, so two variants
    sharing one would be acknowledged as though they were the same pet, and the
    report could not tell them apart either. A repeated label is qualified by its
    Multi Infobox tab ("Chocolate (POH)"), or by its ids where there is no tab.
    """
    counts = {}
    for v in variants:
        counts[v["variant"]] = counts.get(v["variant"], 0) + 1
    taken = set()
    for v in variants:
        tab = v.pop("tab", "")
        if counts[v["variant"]] > 1 and tab:
            v["variant"] += " (" + tab + ")"
        if v["variant"] in taken:
            v["variant"] += " (" + ", ".join(str(i) for i in v["ids"]) + ")"
        taken.add(v["variant"])
    return variants


def parse_variants(title: str, text: str) -> list:
    """Each visual variant of a pet with its NPC ids."""
    tabs = infobox_tabs(text)
    variants = []
    for body in extract_template(text, "Infobox NPC"):
        p = infobox_params(body)
        # infobox_params strips comments out of the bodies it hands back, so a
        # tab may be recorded under either form of the same infobox
        tab = tabs.get(COMMENT.sub("", body).strip()) or tabs.get(body, "")
        indices = sorted(
            {int(m.group(1)) for k in p for m in [re.fullmatch(r"id(\d+)", k)] if m}
        )
        if indices:
            for n in indices:
                label = p.get("version" + str(n)) or p.get("name" + str(n)) or (title + " #" + str(n))
                variants.append({"variant": label.strip(), "ids": parse_ids(p.get("id" + str(n), "")), "tab": tab})
        elif "id" in p:
            label = p.get("name") or title
            variants.append({"variant": label.strip(), "ids": parse_ids(p["id"]), "tab": tab})
    # de-duplicate variants that repeat across multiple infoboxes on one page
    seen, unique = set(), []
    for v in variants:
        key = (v["variant"], tuple(sorted(v["ids"])))
        if v["ids"] and key not in seen:
            seen.add(key)
            unique.append(v)
    return qualify_duplicate_labels(unique)


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

    rate_changes = findings.get("rate_changes") or []
    rate_conflicts = findings.get("rate_conflicts") or []

    if not any((missing, new_pages, findings.get("fetch_failures"), rate_changes, rate_conflicts)):
        return (
            "No new pets, variants or drop rate changes. The plugin covers every NPC id "
            "on the wiki pet pages, checked against RuneLite " + api_info["release"] + "."
        )

    lines = []

    if rate_changes:
        lines.append("### " + str(len(rate_changes)) + " drop rate(s) changed on the wiki")
        lines.append("")
        for c in sorted(rate_changes, key=lambda x: (x["page"], x.get("where", ""))):
            note = " - " + md(c["section"]) if c.get("section") else ""
            head = "- **[" + md(c["page"]) + "](" + wiki_url(c["page"]) + ")**" + note
            if c.get("where") == "drop sources":
                # source names are wiki text, so they are escaped rather than quoted
                lines.append(
                    head + ", Item sources: " + md(", ".join(c["was"]) or "none", 300)
                    + " -> " + md(", ".join(c["now"]) or "none", 300)
                )
                continue
            now = ", ".join(c["now"]) if c["now"] else "no fixed rate"
            lines.append(head + ": `" + ", ".join(c["was"]) + "` -> `" + now + "`")
        lines.append("")
        lines.append("Update the matching info string in `PetJsonCreator.java` if it quotes a rate.")
        lines.append("")

    if rate_conflicts:
        lines.append("### " + str(len(rate_conflicts)) + " drop rate(s) where the plugin disagrees with the wiki")
        lines.append("")
        for c in sorted(rate_conflicts, key=lambda x: x["page"]):
            note = " - " + md(c["section"]) if c.get("section") else ""
            lines.append("- **[" + md(c["page"]) + "](" + wiki_url(c["page"]) + ")**" + note)
            lines.append("  - wiki: `" + ", ".join(c["wiki"]) + "`")
            if c.get("sources"):
                lines.append("  - wiki Item sources: " + md(", ".join(c["sources"]), 300))
            lines.append("  - plugin: `" + ", ".join(c["plugin"]) + "` - " + md(c["info"], 200))
        lines.append("")
        lines.append("Some of these are wording rather than errors: the plugin may describe a")
        lines.append("different route to the pet than the rate column does. Anything you decide")
        lines.append("to leave alone is recorded and will not be raised again.")
        lines.append("")
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
                hardcoded = " (hard-coded in the plugin)" if e.get("hardcoded") else ""
                lines.append("  - " + label + "ids `" + ids + "` -> " + names + hardcoded)
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
    # A cache from another version, or one missing anything we rely on, is
    # discarded whole. Half-reading it silently drops pets from the check.
    if cache.get("schema") != CACHE_SCHEMA or not all(
        k in cache
        for k in ("titles", "page_revids", "page_variants", "pet_rows", "page_rates",
                  "page_sources", "source_scope", "source_revids")
    ):
        cache = {}
    force = args.force or args.all

    def log(*a):
        if args.verbose:
            print(*a, file=sys.stderr)

    # ---- probe: cheap signals only -------------------------------------
    names, mentioned = plugin_constant_names(java)
    digest = hashlib.sha256("\n".join(names).encode())
    # pets.json carries the shipped rate text, so regenerating it must re-check
    pets_json = args.repo / "pets.json"
    if pets_json.exists():
        digest.update(pets_json.read_bytes())
    plugin_digest = digest.hexdigest()
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
    if not cat:
        # this category has never been empty; an empty answer is a failed query,
        # and continuing would quietly check only part of the list
        raise RuntimeError("Category:Pets came back empty; treating that as a failed query")
    known = cache.get("titles", [])
    # the monster and chest pages the drop sources were read from: an edit to
    # one of them may have moved a rate without touching any pet page
    known_sources = cache.get("source_revids", {})
    meta = page_meta(sorted(set(cat) | set(known) | {"Pet"} | set(known_sources)))
    sources_changed = sorted(s for s, rev in known_sources.items() if meta.get(s, {}).get("revid") != rev)

    pet_revid = meta.get("Pet", {}).get("revid")
    pet_article_changed = pet_revid != cache.get("pet_article_revid")
    if pet_article_changed or not known:
        log("Pet article changed, re-reading its tables")
        pet_rows = parse_pet_rows(page_wikitext(["Pet"]).get("Pet", ""))
        if not pet_rows:
            # the article always has the tables; empty means the fetch or the
            # parse failed, and drop rate tracking would silently go dead
            raise RuntimeError("no pet rows parsed from the Pet article; refusing to run on that")
    else:
        pet_rows = cache["pet_rows"]
    sections = {page: row.get("section", "") for page, row in pet_rows.items()}

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

    # Better to fail loudly than to check a truncated list and call it all clear.
    previous = len(cache.get("titles", []))
    if len(titles) < MIN_EXPECTED_PAGES:
        raise RuntimeError(
            "only " + str(len(titles)) + " pet pages found (expected at least "
            + str(MIN_EXPECTED_PAGES) + "); Category:Pets or the Pet article did not "
            "come back as expected, so this run would under-report"
        )
    if previous and len(titles) < previous * MAX_SHRINK:
        raise RuntimeError(
            "pet page list shrank from " + str(previous) + " to " + str(len(titles))
            + "; refusing to report against a partial list. If the drop is genuine, "
            "delete " + str(cache_file.name) + " to accept the new list."
        )

    revids = {t: m["revid"] for t, m in meta.items()}

    prev_revids = cache.get("page_revids", {})
    cached_variants = cache.get("page_variants") or {}
    changed = sorted(t for t in titles if revids.get(t) != prev_revids.get(t))
    dropped = sorted(set(cached_variants) - set(titles))

    settled = (
        not changed
        and not dropped
        # drop rates live in the Pet article, so an edit there matters even when
        # no individual pet page moved
        and not pet_article_changed
        and not sources_changed
        and cache.get("runelite_release") == release
        and cache.get("plugin_digest") == plugin_digest
        and cache.get("page_variants") is not None
        # never stay quiet if nothing has been reported yet: deleting
        # acknowledged.json is how you ask to be told everything again
        and ack.get("missing_keys") is not None
    )
    log("probe: " + str(len(changed)) + " page(s) edited, " + str(len(dropped)) + " removed, "
        + str(len(sources_changed)) + " drop source page(s) edited")

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
    raw_ids = plugin_raw_ids(java)
    log("plugin registers " + str(len(plugin_ids)) + " npc ids, plus " + str(len(raw_ids)) + " hard-coded")

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
                    "hardcoded": any(i in raw_ids for i in v["ids"]),
                    "section": sections.get(title, ""),
                }
            )

    # ---- drop rates ----------------------------------------------------
    # Only pets the article gives a concrete rate for. Skilling pets link to a
    # formula ("See here") and the generic pets say "NA", so both drop out here
    # without needing to be named.
    plugin_rates = plugin_rates_by_page(args.repo / "pets.json", page_variants)
    prev_rates = cache.get("page_rates", {})
    page_rates, rate_changes, rate_conflicts = {}, [], []
    rate_info = {page: wiki_rate_info(pet_rows.get(page, {}).get("rate", "")) for page in sorted(page_variants)}

    # Drop sources are read for the same pets, and only re-read when the article,
    # the set of pets or one of the source pages has changed.
    scope = sorted(page for page, info in rate_info.items() if info["stated"])
    prev_sources = cache.get("page_sources")
    if prev_sources is None or pet_article_changed or sources_changed or cache.get("source_scope") != scope:
        log("reading drop sources for " + str(len(scope)) + " pet(s)")
        page_sources = drop_sources(scope)
    else:
        page_sources = prev_sources
    source_pages = sorted({e["source"] for entries in page_sources.values() for e in entries})
    unknown_sources = [s for s in source_pages if s not in meta]
    if unknown_sources:
        meta.update(page_meta(unknown_sources))
    source_revids = {s: meta[s]["revid"] for s in source_pages if s in meta}

    for page in sorted(page_variants):
        info = rate_info[page]
        was = prev_rates.get(page)

        if not info["stated"]:
            # A pet that used to quote a fixed rate and no longer does has still
            # changed - the article switched it to a formula, a span or "Varies".
            # Report that rather than letting it fall out of tracking unnoticed.
            if was:
                rate_changes.append({"page": page, "was": was, "now": [],
                                     "section": sections.get(page, "")})
            continue

        page_rates[page] = info["stated"]
        if was is not None and was != info["stated"]:
            rate_changes.append({"page": page, "was": was, "now": info["stated"],
                                 "section": sections.get(page, "")})

        sources = page_sources.get(page, [])
        was_sources = (prev_sources or {}).get(page)
        if was_sources is not None and was_sources != sources:
            rate_changes.append({"page": page, "where": "drop sources",
                                 "was": [source_label(e) for e in was_sources],
                                 "now": [source_label(e) for e in sources],
                                 "section": sections.get(page, "")})

        if info["ambiguous"]:
            continue
        plugin = plugin_rates.get(page)
        if not plugin or not plugin["rates"]:
            continue
        source_rates = sorted({e["rate"] for e in sources})
        if rates_agree(info, source_rates, plugin["rates"]):
            continue
        rate_conflicts.append({
            "page": page,
            "wiki": info["stated"],
            "sources": [source_label(e) for e in sources],
            "source_rates": source_rates,
            "plugin": plugin["rates"],
            "info": plugin["info"],
            "section": sections.get(page, ""),
        })

    # ---- what the plugin says per pet, for the commit check -------------
    pets_entries = load_pets_json(pets_json)
    infos_by_id = {k: e["info"] for k, e in pets_entries.items() if e.get("info")}
    wiki_ids, plugin_pages = set(), {}
    for page, variants in page_variants.items():
        ids = {i for v in variants for i in v["ids"]}
        wiki_ids |= ids
        texts = sorted({infos_by_id[str(i)] for i in ids if str(i) in infos_by_id})
        view = {
            "ids": sorted(ids & plugin_ids),
            "raw_ids": sorted(ids & raw_ids),
            "info": texts,
            "rates": sorted({r for t in texts for r in extract_rates(t)}),
        }
        if view["ids"] or view["raw_ids"] or texts:
            plugin_pages[page] = view
    # pets.json is generated from PetJsonCreator.java; the two drifting apart
    # means it was not regenerated
    creator_ids = plugin_ids | raw_ids
    json_ids = {int(k) for k in pets_entries if k.isdigit()}
    pets_json_mismatch = {
        "not_in_pets_json": sorted(creator_ids - json_ids),
        "only_in_pets_json": sorted(json_ids - creator_ids),
    } if pets_entries else {}

    # status is part of the key, so a variant that becomes buildable when the next
    # RuneLite release lands is reported again rather than staying silent.
    def key_of(m):
        return m["page"] + "::" + m["variant"] + "::" + m["status"]

    # Both sides are in the key, so a disagreement left alone is raised again as
    # soon as either the wiki or the plugin moves.
    def rate_key(c):
        wiki = ",".join(c["wiki"]) + (";" + ",".join(c["source_rates"]) if c.get("source_rates") else "")
        return c["page"] + "::" + wiki + "::" + ",".join(c["plugin"])

    prev_pages = set(ack.get("pages", []))
    prev_missing = set(ack.get("missing_keys", []))
    prev_rate_keys = set(ack.get("rate_keys", []))
    new_pages = sorted(set(titles) - prev_pages) if prev_pages else []
    shown = missing if (args.all or not prev_missing) else [m for m in missing if key_of(m) not in prev_missing]
    shown_conflicts = (
        rate_conflicts
        if (args.all or not prev_rate_keys)
        else [c for c in rate_conflicts if rate_key(c) not in prev_rate_keys]
    )

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
        "rate_changes": rate_changes,
        "rate_conflicts": shown_conflicts,
        "rates_tracked": len(page_rates),
        "unresolved_constants": unresolved,
        # The rest is for the commit check (review.py), which diffs the findings
        # of two runs and so needs the unfiltered lists: the ones above are cut
        # down to what has not been reported before. The report ignores these.
        "all_missing": missing,
        "all_rate_conflicts": rate_conflicts,
        "plugin_pages": plugin_pages,
        "wiki_rates": {
            page: {"stated": rate_info[page]["stated"],
                   "ambiguous": rate_info[page]["ambiguous"],
                   "sources": [source_label(e) for e in page_sources.get(page, [])]}
            for page in scope
        },
        "unmatched_ids": sorted(creator_ids - wiki_ids),
        "pets_json_mismatch": pets_json_mismatch,
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
                    "rate_keys": sorted(rate_key(c) for c in rate_conflicts),
                },
                indent=2,
            )
            + "\n",
            encoding="utf-8",
        )
        cache_file.write_text(
            json.dumps(
                {
                    "schema": CACHE_SCHEMA,
                    "checked_at": findings["checked_at"],
                    "runelite_release": release,
                    "runelite_validators": validators,
                    "plugin_digest": plugin_digest,
                    "pet_article_revid": pet_revid,
                    "pet_rows": pet_rows,
                    "page_rates": page_rates,
                    "page_sources": page_sources,
                    "source_scope": scope,
                    "source_revids": source_revids,
                    "titles": sorted(titles),
                    "page_revids": {t: revids[t] for t in titles if t in revids},
                    "page_variants": page_variants,
                },
                indent=2,
            )
            + "\n",
            encoding="utf-8",
        )

    return 10 if (shown or new_pages or fetch_failures or rate_changes or shown_conflicts) else 0


if __name__ == "__main__":
    try:
        sys.exit(main())
    except Exception as exc:  # noqa: BLE001
        print("petwatch failed: " + str(exc), file=sys.stderr)
        sys.exit(1)
