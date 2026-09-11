#!/usr/bin/env python3
"""
petwatch commit check - does a change to the pet data match the wiki?

Runs petwatch against the plugin as it was before and after a change, with the
same wiki data for both, and reports on each pet whose entry the change touched:
variants it adds that petwatch listed as missing, drop rates it brings into line,
and anything that does not match - a rate the wiki does not give, a constant the
released RuneLite API lacks, ids hard-coded where a constant exists, a pets.json
left out of step with PetJsonCreator.java.

    python tools/petwatch/review.py --base HEAD~1 --head HEAD
    python tools/petwatch/review.py --base "$BEFORE" --head "$SHA" --post --pr 12

--post comments through the GitHub API, using GITHUB_TOKEN and GITHUB_REPOSITORY
from the environment: on the pull request given by --pr, or on the head commit
without one. It replaces its own earlier comment there rather than adding
another. Nothing is recorded and no issue is opened.

Exit codes: 0 = done (including when the change touches no pet data), 1 = error.
"""

from __future__ import annotations

import argparse
import json
import os
import re
import shutil
import subprocess
import sys
import tempfile
import urllib.error
import urllib.request
from pathlib import Path

import petwatch as pw

HERE = Path(__file__).resolve().parent
CREATOR = "src/test/java/com/micro/petinfo/PetJsonCreator.java"
PETS_JSON = "pets.json"

# Identifies this tool's own comment so that a re-run edits it rather than
# leaving a second one behind. HTML comments do not render.
MARKER = "<!-- petwatch-commit-check -->"
# GitHub rejects a comment body longer than 65,536 characters
MAX_COMMENT = 60_000
# a change touching dozens of variants would otherwise bury the comment
MAX_LISTED = 12

# Worst first: a pet's, and then the whole change's, verdict is the worst level
# anything in it reached.
SEVERITY = ("bad", "warn", "ok", "info")
ICON = {"bad": "❌", "warn": "⚠️", "ok": "✅", "info": "ℹ️"}
VERDICT = {
    "bad": "does not match the wiki",
    "warn": "partly matches the wiki",
    "ok": "matches the wiki",
    "info": "touches pet data petwatch cannot check against the wiki",
}


# --------------------------------------------------------------------------
# reading the two sides out of git
# --------------------------------------------------------------------------
def git(repo: Path, *args: str) -> str:
    """Run git in `repo` and return its output, raising if it fails."""
    return subprocess.run(
        ["git", "-C", str(repo), *args], check=True, capture_output=True, text=True
    ).stdout.strip()


def commit_of(repo: Path, rev: str):
    """The commit `rev` names, or None if there is no such commit."""
    try:
        return git(repo, "rev-parse", "--verify", "--quiet", rev + "^{commit}")
    except subprocess.CalledProcessError:
        return None


def resolve_range(repo: Path, base: str, head: str):
    """The (base, head) commits to compare, where base is where head's own work starts.

    A pull request passes the tip of the branch it targets, which has usually
    moved on since; the merge base is where its changes actually begin. A push
    passes the commit it moved the branch from, and the merge base of that with
    the new tip is the same commit unless it was a force push.
    """
    head_commit = commit_of(repo, head)
    if not head_commit:
        raise RuntimeError("unknown commit " + repr(head))
    # a push that creates a branch reports all-zeros as the previous tip
    base_commit = commit_of(repo, base) if base and set(base) != {"0"} else None
    if base_commit:
        return git(repo, "merge-base", base_commit, head_commit), head_commit
    # a new branch, or a force push whose old tip is gone: judge the last commit
    parent = commit_of(repo, head_commit + "^")
    if not parent:
        raise RuntimeError("no base to compare " + head_commit[:7] + " against")
    return parent, head_commit


def checkout_pet_files(repo: Path, rev: str, dest: Path) -> None:
    """Write the plugin's pet files as of `rev` into `dest`, laid out like the repo.

    petwatch only reads these two files, so this stands in for a whole checkout.
    """
    for path in (CREATOR, PETS_JSON):
        proc = subprocess.run(["git", "-C", str(repo), "show", rev + ":" + path], capture_output=True)
        if proc.returncode != 0:
            if path == PETS_JSON:
                continue  # petwatch reads a missing pets.json as "no rates quoted"
            raise RuntimeError(path + " does not exist at " + rev[:7])
        target = dest / path
        target.parent.mkdir(parents=True, exist_ok=True)
        target.write_bytes(proc.stdout)


def run_petwatch(tree: Path, state: Path, out: Path, verbose: bool) -> dict:
    """Run a full petwatch check against one side of the change and read its findings."""
    cmd = [sys.executable, str(HERE / "petwatch.py"), "--repo", str(tree), "--state", str(state),
           "--force", "--json", str(out)]
    if verbose:
        cmd.append("--verbose")
    # stdout is the issue report, which is not wanted here; errors go to stderr
    proc = subprocess.run(cmd, stdout=subprocess.DEVNULL)
    # 10 means petwatch has findings, which is the normal case here
    if proc.returncode not in (0, 10):
        raise RuntimeError("petwatch failed on " + tree.name + " (exit " + str(proc.returncode) + ")")
    return json.loads(out.read_text(encoding="utf-8"))


# --------------------------------------------------------------------------
# comparison
# --------------------------------------------------------------------------
class Notes:
    """The findings about one pet, ready to render as markdown bullets.

    Findings of one kind are merged: `add` is called once per variant with the
    same message, and those variants are then listed in a single bullet. The
    message carries `{}` where that list belongs.
    """

    def __init__(self):
        # (level, message) -> the items to list in place of "{}"
        self._notes = {}

    def add(self, level: str, message: str, item: str = "") -> None:
        """Record a finding. Repeated calls with one message share a bullet."""
        items = self._notes.setdefault((level, message), [])
        if item:
            items.append(item)

    def levels(self) -> set:
        """The severity levels present, which decide the overall verdict."""
        return {level for level, _ in self._notes}

    def bullets(self) -> list:
        """The findings as markdown list items, worst first."""
        out = []
        for (level, message), items in sorted(self._notes.items(),
                                              key=lambda note: SEVERITY.index(note[0][0])):
            listed = ", ".join(items[:MAX_LISTED])
            if len(items) > MAX_LISTED:
                listed += " and " + str(len(items) - MAX_LISTED) + " more"
            out.append("- " + ICON[level] + " " + message.replace("{}", listed))
        return out

    def __bool__(self):
        return bool(self._notes)


def variant_text(entry: dict) -> str:
    """A missing variant as `*Chocolate (POH)* \\`16564\\``.

    The label comes from the wiki, so it is escaped; variants named after the
    page itself are shown as ids alone, since repeating the name reads oddly.
    """
    ids = "`" + ", ".join(str(i) for i in entry["ids"]) + "`"
    if entry["variant"] == entry["page"]:
        return ids
    return "*" + pw.md(entry["variant"]) + "* " + ids


def rate_text(rates: list) -> str:
    """Drop rates as code, or "no rate" when there are none."""
    return "`" + ", ".join(rates) + "`" if rates else "no rate"


def wiki_rate_text(stated: list, sources: list) -> str:
    """What the wiki gives for a pet: the Pet article's column, then Item sources."""
    text = rate_text(stated) + " in the Pet article"
    if sources:
        text += ", Item sources " + pw.md(", ".join(sources), 300)
    return text


def coverage_notes(before: dict, after: dict, notes: Notes) -> None:
    """Judge what the change did to the variants petwatch listed as missing.

    `before` and `after` map a variant to its missing entry on each side of the
    change, so a variant that disappears from the list is one the change added.
    """
    for key in sorted(set(before) | set(after)):
        was, now = before.get(key), after.get(key)
        item = variant_text(was or now)
        if was and not now:
            notes.add("ok", "adds {}, which petwatch listed as missing", item)
        elif now and not was:
            notes.add("bad", "drops {}, which the wiki still lists", item)
        elif now["hardcoded"] and not was["hardcoded"]:
            # the change registered the raw ids rather than a constant, which is
            # right only while RuneLite has no released constant to use
            if now["status"] == "ready":
                constants = ", ".join("`NpcID." + c + "`" for c in now["constants"])
                notes.add("bad", "hard-codes {}, but the released RuneLite API has constants for them;"
                                 " use those instead", item + " -> " + constants)
            elif now["status"] == "pending-release":
                notes.add("ok", "hard-codes {}; RuneLite has constants for them on master only,"
                                " so switch once they are released", item)
            else:
                notes.add("ok", "hard-codes {}; RuneLite has no constants for them yet, and petwatch"
                                " will report when it does", item)
        elif was["hardcoded"] and not now["hardcoded"]:
            notes.add("bad", "drops {}, which the wiki still lists", item)
        elif not now["hardcoded"]:
            # the change touched this pet but left this variant unregistered
            notes.add("warn", "leaves {} missing", item + " (" + pw.STATUS_HEADINGS[now["status"]] + ")")


def rate_notes(before: dict, after: dict, wiki: dict, conflicts: tuple, notes: Notes) -> None:
    """Judge the drop rate the plugin quotes for one pet after the change.

    `before` and `after` are what the plugin says about the pet on each side,
    `wiki` what the wiki gives, and `conflicts` petwatch's disagreement for the
    pet before and after, if any.
    """
    was_conflict, now_conflict = conflicts
    info_changed = before.get("info") != after.get("info")

    if now_conflict:
        detail = ("the plugin quotes " + rate_text(now_conflict["plugin"]) + ", the wiki gives "
                  + wiki_rate_text(now_conflict["wiki"], now_conflict.get("sources")))
        # a disagreement the change caused or touched is the change's doing; one
        # that was already there and untouched is only context
        if info_changed or not was_conflict:
            notes.add("bad", "drop rate does not match the wiki: " + detail)
        else:
            notes.add("info", "drop rate still does not match the wiki, as before this change: " + detail)
    elif was_conflict and after.get("rates"):
        notes.add("ok", "drop rate now matches the wiki: " + rate_text(after["rates"]))
    elif info_changed and wiki and not wiki.get("ambiguous"):
        if after.get("rates"):
            notes.add("ok", "drop rate matches the wiki: " + rate_text(after["rates"]))
        else:
            notes.add("info", "the info text quotes no drop rate; the wiki gives "
                      + wiki_rate_text(wiki["stated"], wiki.get("sources")))
    elif info_changed and after.get("rates"):
        # a span, or a rate that varies: petwatch does not compare those
        notes.add("info", "the drop rate was not compared: the wiki gives no single fixed rate for this pet")


def plugin_wide_notes(base: dict, head: dict, changed_files: list) -> Notes:
    """Findings about the change as a whole rather than about one pet."""
    notes = Notes()
    for name, where in sorted(head.get("unresolved_constants", {}).items()):
        if name not in base.get("unresolved_constants", {}):
            notes.add("bad", "`NpcID." + name + "` is " + where)

    mismatch = head.get("pets_json_mismatch") or {}
    if mismatch.get("not_in_pets_json") or mismatch.get("only_in_pets_json"):
        notes.add("bad", "pets.json does not match PetJsonCreator.java ("
                  + str(len(mismatch.get("not_in_pets_json", []))) + " id(s) missing from it, "
                  + str(len(mismatch.get("only_in_pets_json", []))) + " extra); regenerate it with"
                  " `PetJsonCreator.main`")
    elif CREATOR in changed_files and PETS_JSON not in changed_files:
        # the ids still line up, but an edited info string would not show there
        notes.add("warn", "PetJsonCreator.java changed but pets.json did not; if an info text changed,"
                  " regenerate pets.json, since the plugin ships that file")

    new_unmatched = sorted(set(head.get("unmatched_ids", [])) - set(base.get("unmatched_ids", [])))
    if new_unmatched:
        notes.add("info", "registers ids that are on no wiki pet page petwatch reads: {} - expected"
                  " for pet-like NPCs that are not pets",
                  ", ".join("`" + str(i) + "`" for i in new_unmatched))
    return notes


def compare(base: dict, head: dict, changed_files: list) -> dict:
    """What the change did to each pet it touched, judged against the wiki.

    `base` and `head` are petwatch findings for the two sides of the change. A
    pet counts as touched when what the plugin says about it differs between
    them, so pets the change left alone are not reported on.
    """
    before_pages, after_pages = base.get("plugin_pages", {}), head.get("plugin_pages", {})
    touched = sorted(page for page in set(before_pages) | set(after_pages)
                     if before_pages.get(page) != after_pages.get(page))

    # a variant is identified by its page and label on both sides
    before_missing = {(m["page"], m["variant"]): m for m in base.get("all_missing", [])}
    after_missing = {(m["page"], m["variant"]): m for m in head.get("all_missing", [])}
    before_conflicts = {c["page"]: c for c in base.get("all_rate_conflicts", [])}
    after_conflicts = {c["page"]: c for c in head.get("all_rate_conflicts", [])}
    wiki_rates = head.get("wiki_rates", {})

    pages = []
    for page in touched:
        notes = Notes()
        coverage_notes(
            {key: m for key, m in before_missing.items() if key[0] == page},
            {key: m for key, m in after_missing.items() if key[0] == page},
            notes,
        )
        rate_notes(
            before_pages.get(page, {}),
            after_pages.get(page, {}),
            wiki_rates.get(page),
            (before_conflicts.get(page), after_conflicts.get(page)),
            notes,
        )
        if notes:
            pages.append((page, notes))

    general = plugin_wide_notes(base, head, changed_files)
    levels = set().union(general.levels(), *(notes.levels() for _, notes in pages))
    worst = next(level for level in SEVERITY if level in levels or level == SEVERITY[-1])
    return {"pages": pages, "general": general, "verdict": (worst, VERDICT[worst])}


def render_comment(result: dict, base: str, head: str, release: str) -> str:
    """The comment body: a verdict, then the findings for each pet touched."""
    level, verdict = result["verdict"]
    lines = [
        MARKER,
        "### " + ICON[level] + " petwatch: this change " + verdict,
        "",
        "Compared `" + base[:7] + "..." + head[:7] + "` with the OSRS Wiki and RuneLite `" + release
        + "`, for the pets the change touched.",
        "",
    ]
    for page, notes in result["pages"]:
        lines.append("**[" + pw.md(page) + "](" + pw.wiki_url(page) + ")**")
        lines.extend(notes.bullets())
        lines.append("")
    if result["general"]:
        lines.append("**Plugin-wide**")
        lines.extend(result["general"].bullets())
        lines.append("")
    footer = (
        "<sub>Posted by `tools/petwatch/review.py`. Data from the "
        "[Old School RuneScape Wiki](https://oldschool.runescape.wiki), "
        "[CC BY-NC-SA 3.0](https://creativecommons.org/licenses/by-nc-sa/3.0/).</sub>"
    )
    body = "\n".join(lines)
    if len(body) + len(footer) > MAX_COMMENT:
        # cut at a line boundary, leaving room for the footer and the notice
        keep = MAX_COMMENT - len(footer) - 100
        body = body[:keep].rsplit("\n", 1)[0] + "\n\n*Cut short: see the run's summary.*\n"
    return body + "\n" + footer


# --------------------------------------------------------------------------
# posting
# --------------------------------------------------------------------------
SHA_RE = re.compile(r"^[0-9a-f]{40}$")
REPO_RE = re.compile(r"^[\w.-]+/[\w.-]+$")
PR_RE = re.compile(r"^[0-9]{1,10}$")
# the identity GITHUB_TOKEN comments as, and so the author of the comment to edit
BOT_LOGIN = "github-actions[bot]"


def github(method: str, url: str, token: str, payload=None):
    """One GitHub REST call, returning the decoded response."""
    request = urllib.request.Request(
        url,
        data=json.dumps(payload).encode("utf-8") if payload is not None else None,
        method=method,
        headers={
            "Authorization": "Bearer " + token,
            "Accept": "application/vnd.github+json",
            "X-GitHub-Api-Version": "2022-11-28",
            "User-Agent": pw.DEFAULT_UA,
        },
    )
    with urllib.request.urlopen(request, timeout=30) as response:
        return json.loads(response.read().decode("utf-8") or "null")


def comment_urls(api: str, repo: str, sha: str, pr: str) -> tuple:
    """Where to list, create and edit comments, for a pull request or a commit.

    A pull request's conversation is where a comment is actually read, so it is
    preferred; a push to master has no pull request, and the comment goes on the
    commit instead. A pull request comment is an issue comment, and neither kind
    is edited at the address it was created at.
    """
    base = api.rstrip("/") + "/repos/" + repo
    if pr:
        return (base + "/issues/" + pr + "/comments?per_page=100",
                base + "/issues/" + pr + "/comments",
                base + "/issues/comments/")
    return (base + "/commits/" + sha + "/comments?per_page=100",
            base + "/commits/" + sha + "/comments",
            base + "/comments/")


def post_comment(sha: str, body: str, pr: str = "") -> str:
    """Comment on the pull request, or on the commit, replacing any earlier one."""
    token, repo = os.environ.get("GITHUB_TOKEN", ""), os.environ.get("GITHUB_REPOSITORY", "")
    api = os.environ.get("GITHUB_API_URL", "https://api.github.com")
    if not token or not REPO_RE.match(repo):
        raise RuntimeError("--post needs GITHUB_TOKEN and GITHUB_REPOSITORY (owner/name)")
    # both go into a URL, so neither is taken on trust
    if not SHA_RE.match(sha):
        raise RuntimeError("refusing to comment on implausible commit " + repr(sha))
    if pr and not PR_RE.match(pr):
        raise RuntimeError("refusing to comment on implausible pull request " + repr(pr))

    where = "pull request #" + pr if pr else "commit " + sha[:7]
    listing, create, edit = comment_urls(api, repo, sha, pr)
    existing = github("GET", listing, token) or []
    mine = [c for c in existing
            if MARKER in (c.get("body") or "") and (c.get("user") or {}).get("login") == BOT_LOGIN]
    if mine:
        github("PATCH", edit + str(mine[-1]["id"]), token, {"body": body})
        return "updated the comment on " + where
    github("POST", create, token, {"body": body})
    return "commented on " + where


# --------------------------------------------------------------------------
# main
# --------------------------------------------------------------------------
def report(message: str, path) -> int:
    """Say there was nothing to comment on, and record that for the run summary."""
    print(message)
    if path:
        path.write_text(message + "\n", encoding="utf-8")
    return 0


def main() -> int:
    ap = argparse.ArgumentParser(description="Check a change to the pet data against the OSRS Wiki.")
    ap.add_argument("--base", required=True, help="commit before the change (a pull request's base branch tip is fine)")
    ap.add_argument("--head", default="HEAD", help="commit after the change (default: %(default)s)")
    ap.add_argument("--repo", type=Path, default=HERE.parents[1], help="PetInfoPlugin checkout (default: %(default)s)")
    ap.add_argument("--state", type=Path, default=HERE / "state", help="petwatch cache to start from; never written")
    ap.add_argument("--report", type=Path, help="also write the markdown here")
    ap.add_argument("--post", action="store_true", help="comment on GitHub")
    ap.add_argument("--pr", default="", metavar="NUMBER",
                    help="comment on this pull request instead of on the head commit")
    ap.add_argument("-v", "--verbose", action="store_true")
    args = ap.parse_args()

    base, head = resolve_range(args.repo, args.base, args.head)
    changed = git(args.repo, "diff", "--name-only", base, head, "--", CREATOR, PETS_JSON).split()
    if not changed:
        return report("No pet data changed between " + base[:7] + " and " + head[:7] + ".", args.report)

    with tempfile.TemporaryDirectory() as tmp:
        work = Path(tmp)
        # A copy of the cache, so both runs share one set of downloads while
        # leaving the real cache, and what the scheduled check has recorded,
        # untouched. The first run fills it in for the second.
        state = work / "state"
        state.mkdir()
        for cached in [args.state / "cache.json", *args.state.glob("NpcID-*.java")]:
            if cached.exists():
                shutil.copy2(cached, state / cached.name)
        findings = {}
        for side, rev in (("base", base), ("head", head)):
            checkout_pet_files(args.repo, rev, work / side)
            findings[side] = run_petwatch(work / side, state, work / (side + ".json"), args.verbose)

    result = compare(findings["base"], findings["head"], changed)
    if not result["pages"] and not result["general"]:
        return report("The change between " + base[:7] + " and " + head[:7]
                      + " touches no pet petwatch reads.", args.report)

    body = render_comment(result, base, head, findings["head"]["runelite"]["release"])
    print(body)
    if args.report:
        args.report.write_text(body + "\n", encoding="utf-8")
    if args.post:
        try:
            print(post_comment(head, body, args.pr))
        except urllib.error.HTTPError as exc:
            if exc.code in (403, 404):
                # a pull request from a fork runs with a read-only token, so the
                # run summary is the only place the result can go
                print("::warning::could not comment on " + head[:7] + " (" + str(exc) + "); "
                      "the result is in the run summary")
                return 0
            raise
    return 0


if __name__ == "__main__":
    # the verdict icons are not in the Windows console's default code page
    sys.stdout.reconfigure(encoding="utf-8", errors="replace")
    try:
        sys.exit(main())
    except Exception as exc:  # noqa: BLE001
        print("review failed: " + str(exc), file=sys.stderr)
        sys.exit(1)
