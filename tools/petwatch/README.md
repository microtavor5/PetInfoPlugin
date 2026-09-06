# petwatch

Reports pets and pet variants on the OSRS Wiki that `PetJsonCreator.java` does
not register yet.

## How it decides

It compares both sides by **NPC id**, rather than watching pages for edits
(which fires on every typo and misses new variants entirely):

- **Plugin** - every `NpcID.*` constant in `PetJsonCreator.java`, resolved
  against the RuneLite API your build compiles against.
- **Wiki** - every pet page (`Category:Pets`, plus the `{{plinkt|...}}` entries
  in the `Pet` article tables), read for the `|id =` / `|id1 =` parameters of
  its `{{Infobox NPC}}`. Those hold one entry per variant.

A variant counts as covered if any of its ids are already in the plugin. What is
left is the work list, reported with the `NpcID` constant names to paste in.

The comparison is stateless, so it can find pre-existing gaps, not just changes
since the last run.

## Cheap repeat checks

The full check costs about 1.8MB, most of it `NpcID.java` from GitHub plus the
wikitext of ~98 pet pages. Running that daily to catch the occasional late wiki
edit would be wasteful, so each run starts with a probe that asks only for
revision ids, the category listing and the RuneLite release number.

If no pet page has been edited, the category is unchanged, the RuneLite release
is unchanged and `PetJsonCreator.java` is unchanged, the run stops there:

| | requests | downloaded |
| --- | --- | --- |
| probe only, nothing changed | 5 | ~15KB |
| full check | ~10 | ~1800KB |

When something has changed, only the pages whose revision id moved are
re-fetched; the rest are reused from the cache. The RuneLite metadata is
requested with `If-None-Match`, so an unchanged release answers `304`.

That makes a daily schedule cost about the same per week as a single weekly full
check, which is why the workflow runs daily.

## RuneLite API version

`build.gradle` asks for `latest.release`, so a constant is only usable once it
is in a *released* RuneLite - being on `master` is not enough. petwatch reads
`<release>` from
[repo.runelite.net](https://repo.runelite.net/net/runelite/client/maven-metadata.xml)
(the metadata Gradle resolves against) and takes `NpcID.java` from the matching
`runelite-parent-<version>` tag.

Findings are split by what you can act on:

| Status | Meaning |
| --- | --- |
| ready to add | The constant is in the released API. |
| waiting on a RuneLite release | On `master` only; using it now breaks the build and the Plugin Hub. |
| waiting on RuneLite | No constant anywhere yet. |

Separating those last two is the only thing `master` is needed for, so petwatch
downloads it only when the released API leaves an id unexplained. Most runs
fetch one copy of `NpcID.java`, not two.

Status is part of the snapshot key, so a pending pet is reported again once a
release makes it buildable.

Use `--runelite-release VERSION` to check against a specific version instead of
whatever `latest.release` currently resolves to.

### Constants the released API does not have

If `PetJsonCreator.java` references an `NpcID` constant that is missing from the
released API, the report lists it under its own heading instead of mixing it in
with the wiki findings. This is not a pet to add - it would mean the plugin does not
currently compile against the version it resolves to, normally because a
constant was added while it was still only on `master`.

Gradle caches `latest.release` for 24h, so petwatch can report a constant as
available before your local build sees it - fix with
`./gradlew --refresh-dependencies build`. petwatch reads the metadata over HTTP
and is not affected by that cache.

## Usage

```sh
python tools/petwatch/petwatch.py            # new since the last run
python tools/petwatch/petwatch.py --all      # every gap
python tools/petwatch/petwatch.py --force    # full check even if the probe sees nothing
python tools/petwatch/petwatch.py --no-save  # leave the state alone
```

Python 3.9+, no dependencies. Exit codes: `0` nothing to do, `10` findings,
`1` error. `--report FILE` and `--json FILE` also write the output. `--all`
implies `--force`.

Two state files in `state/`, with different jobs:

- **`acknowledged.json`** - what you have already been told about, so a gap you
  have chosen not to act on stops nagging. Small, changes only when the findings
  do, and is committed.
- **`cache.json`** and **`NpcID-*.java`** - revision ids, parsed variants and the
  downloaded API. Purely an optimisation, gitignored; deleting them costs one
  full check.

## Automation

`.github/workflows/pet-watch.yml` runs daily at 17:17 UTC, opens an issue on
findings, and commits `acknowledged.json`. GitHub emails you about issues on your
own repository, so the email path needs no setup. Run it by hand from the Actions
tab; tick **full** for `--all`.

Wednesday is the run that matters - the game update lands around 11:30 and takes
about half an hour, so 17:17 leaves the wiki roughly five hours. The other six
days are the safety net for when the wiki is slower than that, and cost ~15KB
each thanks to the probe. The probe cache is carried between runs by
`actions/cache`; quiet days produce no commit.

For a notification via an external service (like a text or WhatsApp), set a
`PETWATCH_WEBHOOK` repository secret to a URL accepting a JSON `POST` (Twilio,
CallMeBot, ntfy). The step is skipped when the secret is unset.

To run it locally on a schedule instead:

```powershell
$repo = 'C:\path\to\PetInfoPlugin'
$action = New-ScheduledTaskAction -Execute 'python' `
    -Argument "$repo\tools\petwatch\petwatch.py" -WorkingDirectory $repo
$trigger = New-ScheduledTaskTrigger -Weekly -DaysOfWeek Monday -At 9am
Register-ScheduledTask -TaskName 'OSRS pet watch' -Action $action -Trigger $trigger
```

## Limitations

- Ids come from the wiki, so a pet is invisible to the id comparison until an
  editor fills in the `{{Infobox NPC}}` id parameters. That is often immediate
  but not reliably so: of 11 recent pets sampled in September 2026, 7 had ids
  the same day the page appeared and 4 lagged behind by 1 to 20 days. The
  report's "new pet page" section is what covers that window.
- Pet *items* with no `{{Infobox NPC}}` (the `... (item)` pages) carry no NPC
  ids, so they are never coverage-checked. They surface only when the page
  itself is new, via the "new pet page" section.
- The release lookup assumes the `runelite-parent-<version>` tag scheme. If that
  changes the fetch 404s and petwatch exits non-zero rather than silently
  checking the wrong thing.
- The wiki's own watchlist emails are a weaker alternative: they only cover
  pages that already exist, and fire on every edit. Its `Special:` Atom feeds
  return 403 to scripts, so petwatch uses `api.php`.
