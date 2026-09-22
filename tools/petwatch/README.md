# petwatch

Reports pets and pet variants on the OSRS Wiki that `PetJsonCreator.java` does
not register yet.

## How it decides

It compares both sides by **NPC id**, rather than watching pages for edits
(which fires on every typo and misses new variants entirely):

- **Plugin** - every `NpcID.*` constant in `PetJsonCreator.java`, resolved
  against the RuneLite API the plugin builds against.
- **Wiki** - every pet page (`Category:Pets`, plus the `{{plinkt|...}}` entries
  in the `Pet` article tables), read for the `|id =` / `|id1 =` parameters of
  its `{{Infobox NPC}}`. Those hold one entry per variant.

A variant counts as covered if any of its ids are already in the plugin. What is
left is the work list, reported with the `NpcID` constant names to paste in.

The comparison is stateless, so it can find pre-existing gaps, not just changes
since the last run.

## Cheap repeat checks

The full check costs about 1.8MB, most of it `NpcID.java` from GitHub plus the
wikitext of every pet page. Running that daily to catch the occasional late wiki
edit would be wasteful, so each run starts with a probe that asks only for
revision ids (of the pet pages and of the pages their drop sources come from),
the category listing and the RuneLite release number.

If none of those pages has been edited, the Pet article and the category are
unchanged, the RuneLite release is unchanged and the plugin is unchanged, the run
stops there. Measured in September 2026, with 124 pet pages:

| | requests | downloaded |
| --- | --- | --- |
| probe only, nothing changed | 6 | ~26KB |
| full check | - | ~1.8MB |

When something has changed, only the pages whose revision id moved are
re-fetched; the rest are reused from the cache. Drop sources are re-read (about
30KB) only when the Pet article, the set of rate-tracked pets or one of the
source pages changed. The RuneLite metadata is requested with `If-None-Match`, so
an unchanged release answers `304`.

That makes a daily schedule cost about the same per week as a single weekly full
check, which is why the workflow runs daily.

## RuneLite API version

`build.gradle` asks for `latest.release`, so a constant is only usable once it
is in a *released* RuneLite - being on `master` is not enough. petwatch reads
`<release>` from
[repo.runelite.net](https://repo.runelite.net/net/runelite/client/maven-metadata.xml)
(the metadata Gradle resolves against) and takes `NpcID.java` from the matching
`runelite-parent-<version>` tag.

Findings are split by what can be acted on:

| Status | Meaning |
| --- | --- |
| ready to add | The constant is in the released API. |
| waiting on a RuneLite release | On `master` only; using it now breaks the build and the Plugin Hub. |
| waiting on RuneLite | No constant anywhere yet. |

Separating those last two is the only thing `master` is needed for, so petwatch
downloads it only when the released API leaves an id unexplained. Most runs
fetch one copy of `NpcID.java`, not two.

Status is part of the key recorded in `acknowledged.json`, so a pending pet is
reported again once a release makes it buildable.

`--runelite-release VERSION` checks against a specific version instead of
whatever `latest.release` currently resolves to.

### Constants the released API does not have

If `PetJsonCreator.java` references an `NpcID` constant that is missing from the
released API, the report lists it under its own heading instead of mixing it in
with the wiki findings. This is not a pet to add - it would mean the plugin does
not currently compile against the version it resolves to, normally because a
constant was added while it was still only on `master`.

Gradle caches `latest.release` for 24h, so petwatch can report a constant as
available before a local build sees it; `./gradlew --refresh-dependencies build`
clears that. petwatch reads the metadata over HTTP and is not affected by the
Gradle cache.

## Drop rates

petwatch also reads the drop rate column of the Pet article's tables, reports
when a rate changes, and compares it against the rate quoted in `pets.json`.

Only pets the article gives a concrete rate for are considered, which excludes
the noisy ones without having to name them: skilling pets link to a formula
(`See here`), the generic pets say `NA`, and rates given as a span
(`1/800 to 1/4,000`) or described as varying are skipped. In practice that
tracks the boss and collection-log pets and nothing else.

For those pets it also reads the **Item sources** table on the pet's page. That
table is not in the page's wikitext: it is assembled from the drop tables of
each monster and chest, which the wiki publishes through its
[Bucket API](https://oldschool.runescape.wiki/w/RuneScape:Bucket). One query
covers every pet. This is where a second route to a pet shows up when the Pet
article gives only the main one, such as Beef's `~1/400` from Demonic Brutus.

Two kinds of finding:

- **changed on the wiki** - a rate in the Pet article or the Item sources table
  moved since the last check. Always reported.
- **plugin disagrees** - `pets.json` quotes a rate the wiki does not give, or
  leaves out one in the Pet article's column. A rate from a footnote or the Item
  sources table counts as given, because the plugin may describe a different
  route to the pet: Abyssal orphan is quoted per unsired and the table per Sire
  kill. Reported once, then recorded. Both sides are part of what is recorded, so
  a disagreement that was left alone is raised again if either side changes.

Rates are read from `pets.json` rather than `PetJsonCreator.java` because that is
what actually ships; regenerating it re-runs the check.

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

`state/` holds two kinds of thing, with different jobs. All of it is gitignored
on this branch:

- **`acknowledged.json`** - what has already been reported, so a finding that was
  looked at and left alone stops being raised. Durable: in CI it is stored on a
  separate `petwatch-state` branch (see below). Deleting it means everything
  outstanding is reported again.
- **`cache.json`** and **`NpcID-*.java`** - revision ids, parsed variants, rates
  and the downloaded API. Purely an optimisation; deleting them costs one full
  check.

## Automation

`.github/workflows/pet-watch.yml` runs daily at 17:17 UTC and opens an issue on
findings. GitHub emails the repository owner about issues opened on their own
repository, so the email path needs no setup. The workflow can also be run by
hand from the Actions tab, with **full** ticked for `--all`.

`acknowledged.json` is written to a dedicated **`petwatch-state`** branch rather
than the default branch, which keeps bot commits out of the plugin's history and
works where the default branch requires a pull request. The branch is created on
first use, holds that one file, and is written with git plumbing - no second
checkout, no worktree, and the default branch is never pushed to. Deleting the
branch resets what has been reported; nothing else depends on it.

Wednesday is the run that matters - the game update lands around 11:30 and takes
about half an hour, so 17:17 leaves the wiki roughly five hours. The other six
days are the safety net for when the wiki is slower than that, and cost ~26KB
each thanks to the probe. The probe cache is carried between runs by
`actions/cache`; quiet days produce no commit.

A `PETWATCH_WEBHOOK` repository secret, set to a URL accepting a JSON `POST`
(Twilio, CallMeBot, ntfy), adds a notification through an external service such
as a text message. The step is skipped when the secret is unset. It runs
`notify.py`, which summarises the findings JSON into one line and posts it under
several common keys so it suits more services:

```sh
python tools/petwatch/notify.py --findings findings.json --dry-run
python tools/petwatch/notify.py --findings findings.json --url "$WEBHOOK"
```

Exit codes: `0` sent, `2` nothing worth sending, `1` failure. A failed webhook
does not invalidate the run itself, which has already opened the issue.

### Commit check

`.github/workflows/pet-review.yml` runs on pushes and pull requests to `master`
that change `PetJsonCreator.java` or `pets.json`. It runs petwatch against the
plugin as it was before and after the change, with the same wiki data for both,
and comments on each pet the change touched - in the pull request's
conversation, or on the commit when a push has no pull request:

| | |
| --- | --- |
| ✅ | adds variants petwatch listed as missing, brings a drop rate into line with the wiki, or hard-codes ids RuneLite has no released constant for |
| ⚠️ | leaves some of the pet's variants missing, or changes `PetJsonCreator.java` without regenerating `pets.json` |
| ❌ | quotes a rate the wiki does not give, drops a variant the wiki lists, hard-codes ids a released constant exists for, uses a constant the released API lacks, or leaves the ids in `pets.json` out of step with `PetJsonCreator.java` |
| ℹ️ | context, such as a disagreement the change left as it was, or ids on no wiki pet page (expected for pet-like NPCs) |

The headline takes its icon from the worst finding, but words the two kinds
apart: findings about the plugin's own files, such as a `pets.json` left to
regenerate, are named as that rather than as a disagreement with the wiki.

A pull request is judged as a whole, from where it branched off `master`. Re-runs
replace the earlier comment instead of adding another. It opens no issue and
records nothing, so it does not affect the scheduled check.

The same report is written to the run's job summary, so it is also on the
**Pet commit check** entry in the pull request's checks panel, behind *Details*.
Pull requests from forks run with a read-only token, which cannot post a comment;
there the summary is the only copy.

```sh
python tools/petwatch/review.py --base HEAD~1 --head HEAD    # print, do not post
```

Hard-coded ids (`new Pet(PetGroup.OTHER, 16385, ...)`) are recognised but do not
count as covered, so a variant added that way stays in the scheduled report,
marked as hard-coded, and is reported again when RuneLite releases its constant.

To run it locally on a schedule instead:

```powershell
$repo = 'C:\path\to\PetInfoPlugin'
$action = New-ScheduledTaskAction -Execute 'python' `
    -Argument "$repo\tools\petwatch\petwatch.py" -WorkingDirectory $repo
$trigger = New-ScheduledTaskTrigger -Weekly -DaysOfWeek Monday -At 9am
Register-ScheduledTask -TaskName 'OSRS pet watch' -Action $action -Trigger $trigger
```

## Wiki etiquette

The wiki's [stated position](https://oldschool.runescape.wiki/w/Forum:API_and_terms_of_use)
is that a custom User-Agent is preferred and that request volume should "be
reasonable". petwatch:

- goes through `api.php` only - it never scrapes article HTML, `Special:` pages
  or `?action=raw`, and it does not touch the disallowed `Bucket:` namespace.
  Drop sources come from `action=bucket`, which the wiki
  [invites external users](https://oldschool.runescape.wiki/w/RuneScape:Bucket)
  to query instead of scraping pages;
- sends an identifying User-Agent naming the tool and this repository, so the
  operators can see what the traffic is and make contact. The `PETWATCH_UA`
  environment variable overrides it;
- makes requests serially - 6 on a quiet day, more only when something changed,
  once per day;
- sends `maxlag=5`, so the servers turn it away while they are lagging rather
  than being asked to absorb load, and honours `Retry-After` when they do.

`robots.txt` disallows `/*api.php`. That directive is aimed at indexing
crawlers, and the same operators separately document and encourage API use for
tools like this one; Wikipedia's `robots.txt` does the same thing for the same
reason. A low-volume, clearly identified API client is what they have asked for,
while HTML scraping is not - which is why the tool does none.

## When it refuses to run

A monitor that reports "all clear" after checking nothing is worse than one that
fails, so petwatch exits non-zero rather than continue when:

- `Category:Pets` or the Pet article's tables come back empty, or the pet page
  list falls below 50, or it shrinks by more than a fifth against the cached
  list. A genuine large drop is accepted by deleting `state/cache.json`.
- `PetJsonCreator.java` has no `new Pet(...)` entries. An empty, truncated or
  restyled file would otherwise make every pet on the wiki look missing.
- A page could not be read. It keeps whatever was known about that page, retries
  it next run, and lists it under "Pages that could not be read".

Corrupt state files are not an error: `cache.json` and `acknowledged.json` are
discarded and rebuilt, as is a cache written by an older version. A missing or
malformed `pets.json` only disables the drop rate comparison.

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
