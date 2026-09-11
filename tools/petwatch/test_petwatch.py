#!/usr/bin/env python3
"""
Tests for the parsing and comparison logic behind petwatch.

No network and no dependencies:

    python -m unittest discover -s tools/petwatch

The wikitext samples are trimmed from real pages, so a wiki change that breaks
an assumption should break a test here rather than quietly under-report.
"""

import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parent))

import notify  # noqa: E402
import petwatch as pw  # noqa: E402
import review  # noqa: E402


class TestTemplateExtraction(unittest.TestCase):
    def test_brace_matching_handles_nested_templates(self):
        text = "{{Infobox NPC|name=X|examine={{Nested|a|b}}|id=1}}trailing"
        bodies = pw.extract_template(text, "Infobox NPC")
        self.assertEqual(len(bodies), 1)
        self.assertTrue(bodies[0].endswith("}}"))
        self.assertNotIn("trailing", bodies[0])

    def test_does_not_match_a_longer_template_name(self):
        # "Infobox Item2" must not be picked up when asking for "Infobox Item"
        self.assertEqual(pw.extract_template("{{Infobox Item2|id=5}}", "Infobox Item"), [])

    def test_finds_every_occurrence(self):
        text = "{{Infobox NPC|id=1}} words {{Infobox NPC|id=2}}"
        self.assertEqual(len(pw.extract_template(text, "Infobox NPC")), 2)

    def test_unbalanced_braces_terminate(self):
        # a truncated page must not hang or raise
        self.assertIsInstance(pw.extract_template("{{Infobox NPC|id=1", "Infobox NPC"), list)


class TestInfoboxParams(unittest.TestCase):
    def test_top_level_params_only(self):
        body = "{{Infobox NPC\n|name = Vorki\n|id = 8025,8029\n|examine = [[a|b]]\n}}"
        p = pw.infobox_params(body)
        self.assertEqual(p["name"], "Vorki")
        self.assertEqual(p["id"], "8025,8029")

    def test_pipes_inside_links_and_templates_are_not_separators(self):
        body = "{{Infobox NPC\n|source = {{efn|a|b}} and [[Page|label]]\n|id = 7\n}}"
        p = pw.infobox_params(body)
        self.assertEqual(p["id"], "7")
        self.assertIn("label", p["source"])

    def test_comments_are_stripped(self):
        p = pw.infobox_params("{{Infobox NPC|id = 12<!-- 999 was wrong -->}}")
        self.assertEqual(pw.parse_ids(p["id"]), [12])


class TestParseVariants(unittest.TestCase):
    def test_single_variant(self):
        text = "{{Infobox NPC\n|name = Vorki\n|id = 8025,8029\n}}"
        got = pw.parse_variants("Vorki", text)
        self.assertEqual(got, [{"variant": "Vorki", "ids": [8025, 8029]}])

    def test_numbered_variants_use_version_labels(self):
        text = (
            "{{Infobox NPC\n|version1 = Nightmare\n|version2 = Parasite\n"
            "|id1 = 9398,9399\n|id2 = 8183,8541\n}}"
        )
        got = pw.parse_variants("Little Nightmare", text)
        self.assertEqual([v["variant"] for v in got], ["Nightmare", "Parasite"])
        self.assertEqual(got[0]["ids"], [9398, 9399])

    def test_variant_without_ids_is_dropped(self):
        self.assertEqual(pw.parse_variants("X", "{{Infobox NPC|name = X|id = }}"), [])

    def test_duplicate_infoboxes_are_deduplicated(self):
        one = "{{Infobox NPC|name = X|id = 5}}"
        self.assertEqual(len(pw.parse_variants("X", one + "\n" + one)), 1)

    def test_page_with_no_npc_infobox_yields_nothing(self):
        # the "... (item)" pages: an item infobox must not be read for NPC ids
        self.assertEqual(pw.parse_variants("Gary (item)", "{{Infobox Item|id = 1234}}"), [])

    # the dog pages: every colour appears once as a follower and once in the POH
    DOG = (
        "{{Multi Infobox\n|text1 = Follower\n|item1 =\n"
        "{{Infobox NPC\n|version1 = Chocolate\n|version2 = Merle\n|id1 = 16385\n|id2 = 16386\n}}\n"
        "|text2 = POH\n|item2 =\n"
        "{{Infobox NPC\n|version1 = Chocolate\n|version2 = Merle\n|id1 = 16564\n|id2 = 16565\n}}\n"
        "|text3 = Item\n|item3 =\n{{Infobox Item\n|version1 = Chocolate\n|id1 = 34479\n}}\n}}"
    )

    def test_repeated_labels_are_qualified_by_multi_infobox_tab(self):
        got = {v["variant"]: v["ids"] for v in pw.parse_variants("Bernese Mountain Dog", self.DOG)}
        self.assertEqual(got, {
            "Chocolate (Follower)": [16385], "Merle (Follower)": [16386],
            "Chocolate (POH)": [16564], "Merle (POH)": [16565],
        })

    def test_labels_that_do_not_collide_are_left_alone(self):
        # Beef has a Multi Infobox too, but only one NPC infobox in it
        text = "{{Multi Infobox\n|text1 = Follower\n|item1 =\n{{Infobox NPC\n|name = Beef\n|id = 15631\n}}\n}}"
        self.assertEqual([v["variant"] for v in pw.parse_variants("Beef", text)], ["Beef"])

    def test_repeated_labels_without_tabs_fall_back_to_ids(self):
        text = "{{Infobox NPC|name = X|id = 5}}\n{{Infobox NPC|name = X|id = 6}}"
        labels = [v["variant"] for v in pw.parse_variants("X", text)]
        self.assertEqual(len(set(labels)), 2)


class TestDropRates(unittest.TestCase):
    def test_plain_rate(self):
        info = pw.wiki_rate_info("1/3,000")
        self.assertEqual(info["stated"], ["1/3000"])
        self.assertFalse(info["ambiguous"])

    def test_footnote_rates_are_supporting_not_stated(self):
        cell = "1/2,560{{efn|1/2,560 per Abyssal Sire kill; 5/128 per unsired.}}"
        info = pw.wiki_rate_info(cell)
        self.assertEqual(info["stated"], ["1/2560"])
        self.assertIn("5/128", info["supporting"])

    def test_two_sources_both_captured(self):
        info = pw.wiki_rate_info("1/1,500 (Callisto)<br/>1/2,800 (Artio)")
        self.assertEqual(info["stated"], ["1/1500", "1/2800"])

    def test_span_is_ambiguous(self):
        self.assertTrue(pw.wiki_rate_info("1/800 to 1/4,000 (team size)")["ambiguous"])
        self.assertTrue(pw.wiki_rate_info("1/250-1/1000")["ambiguous"])

    def test_varies_is_ambiguous_and_stateless(self):
        info = pw.wiki_rate_info("Varies{{efn|depends on points}}")
        self.assertEqual(info["stated"], [])
        self.assertTrue(info["ambiguous"])

    def test_skilling_and_generic_pets_have_no_rate(self):
        # this is what keeps them out of the comparison, so it must keep holding
        self.assertEqual(pw.wiki_rate_info("[[Beaver#Drop rates|See here]]")["stated"], [])
        self.assertEqual(pw.wiki_rate_info("{{NA}}")["stated"], [])

    def test_decimal_rate(self):
        self.assertEqual(pw.wiki_rate_info("1/2,015.75")["stated"], ["1/2015.75"])

    def test_footnote_caveat_is_not_a_span(self):
        info = pw.wiki_rate_info("1/53{{efn|Only for the player that rolls unique loot.}}")
        self.assertEqual(info["stated"], ["1/53"])
        self.assertFalse(info["ambiguous"])


class TestPetArticleTable(unittest.TestCase):
    ARTICLE = """
==List of pets==
===Boss pets===
{| class="wikitable"
! colspan="2" |Pet
!Source
! data-sort-value="" | Drop rate
!Release date
|-
|{{plinkt|Vorki}}||[[Vorkath]]|| data-sort-value=3000 | 1/3,000||[[4 January]] [[2018]]
|-
|{{plinkt|Beaver}}||Woodcutting||[[Beaver#Drop rates|See here]]||[[5 November]] [[2015]]
|}
==Trivia==
|{{plinkt|NotAPet}}||x||1/1||y
"""

    def test_rows_are_read_with_section_and_rate(self):
        rows = pw.parse_pet_rows(self.ARTICLE)
        self.assertEqual(rows["Vorki"]["section"], "Boss pets")
        self.assertEqual(pw.wiki_rate_info(rows["Vorki"]["rate"])["stated"], ["1/3000"])

    def test_attribute_prefix_is_stripped_from_the_cell(self):
        rows = pw.parse_pet_rows(self.ARTICLE)
        self.assertNotIn("data-sort-value", rows["Vorki"]["rate"])

    def test_rows_outside_the_list_section_are_ignored(self):
        self.assertNotIn("NotAPet", pw.parse_pet_rows(self.ARTICLE))


class TestPluginConstants(unittest.TestCase):
    def _java(self, body):
        d = Path(tempfile.mkdtemp())
        f = d / "PetJsonCreator.java"
        f.write_text(body, encoding="utf-8")
        return f

    def test_only_constants_inside_new_pet_count(self):
        java = self._java(
            "// NpcID.IN_A_COMMENT\n"
            "new Pet(PetGroup.BOSS, NpcID.REAL, INFO),\n"
            "int x = NpcID.HELPER;\n"
        )
        registered, mentioned = pw.plugin_constant_names(java)
        self.assertEqual(registered, ["REAL"])
        self.assertEqual(mentioned, ["HELPER", "IN_A_COMMENT"])

    def test_empty_file_raises_rather_than_reporting_every_pet(self):
        with self.assertRaises(RuntimeError):
            pw.plugin_constant_names(self._java(""))

    def test_restyled_file_raises(self):
        with self.assertRaises(RuntimeError):
            pw.plugin_constant_names(self._java("register(NpcID.SOMETHING);"))


class TestVersionValidation(unittest.TestCase):
    def test_accepts_a_real_version(self):
        self.assertEqual(pw.check_version("1.12.38"), "1.12.38")

    def test_rejects_path_traversal_and_junk(self):
        for bad in ("../../etc/passwd", "1.12.38/../evil", "", "master", "-rf"):
            with self.subTest(bad=bad), self.assertRaises(RuntimeError):
                pw.check_version(bad)

    def test_parses_release_from_metadata(self):
        xml = "<metadata><versioning><latest>9-SNAPSHOT</latest>" \
              "<release>1.12.38</release></versioning></metadata>"
        self.assertEqual(pw.parse_release(xml), "1.12.38")


class TestMarkdownEscaping(unittest.TestCase):
    def test_links_and_images_are_neutralised(self):
        out = pw.md("[click](http://evil) ![x](http://evil/b.png)")
        self.assertNotIn("](", out)

    def test_newlines_are_collapsed_so_line_syntax_is_unreachable(self):
        self.assertNotIn("\n", pw.md("a\n\n# heading"))

    def test_long_text_is_capped(self):
        self.assertLessEqual(len(pw.md("x" * 500, limit=50)), 50)

    def test_ordinary_text_survives_readably(self):
        self.assertEqual(pw.md("Tumeken's Guardian"), "Tumeken's Guardian")


class TestNotifySummary(unittest.TestCase):
    def test_counts_every_kind_of_finding(self):
        text = notify.summarise({
            "missing": [1, 2],
            "rate_changes": [1],
            "rate_conflicts": [1, 2, 3],
            "new_pages": [],
            "fetch_failures": [1],
        })
        self.assertIn("2 pet variant(s) to add", text)
        self.assertIn("1 drop rate(s) changed", text)
        self.assertIn("3 drop rate(s) disagreeing", text)
        self.assertIn("1 page(s) unreadable", text)
        self.assertNotIn("new pet page", text)

    def test_nothing_to_report_is_empty(self):
        self.assertEqual(notify.summarise({"missing": [], "new_pages": []}), "")

    def test_missing_keys_do_not_raise(self):
        self.assertEqual(notify.summarise({}), "")


class TestPluginRateLookup(unittest.TestCase):
    def test_rates_are_joined_to_pages_through_npc_ids(self):
        d = Path(tempfile.mkdtemp())
        pets = d / "pets.json"
        pets.write_text(json.dumps({
            "8025": {"info": "is dropped by Vorkath, at a rate of 1/3000."},
        }), encoding="utf-8")
        got = pw.plugin_rates_by_page(pets, {"Vorki": [{"variant": "Vorki", "ids": [8025, 8029]}]})
        self.assertEqual(got["Vorki"]["rates"], ["1/3000"])

    def test_missing_pets_json_is_not_an_error(self):
        self.assertEqual(pw.plugin_rates_by_page(Path("nope.json"), {}), {})

    def test_malformed_pets_json_is_not_an_error(self):
        d = Path(tempfile.mkdtemp())
        bad = d / "pets.json"
        bad.write_text("{not json", encoding="utf-8")
        self.assertEqual(pw.plugin_rates_by_page(bad, {}), {})


class TestRateAgreement(unittest.TestCase):
    def test_a_rate_only_in_the_item_sources_table_is_accepted(self):
        # Beef: the Pet article gives Brutus only; Demonic Brutus is in Item sources
        info = pw.wiki_rate_info("1/1,000")
        self.assertFalse(pw.rates_agree(info, [], ["1/1000", "1/400"]))
        self.assertTrue(pw.rates_agree(info, ["1/1000", "1/400"], ["1/1000", "1/400"]))

    def test_the_article_rate_must_still_be_quoted(self):
        info = pw.wiki_rate_info("1/1,000")
        self.assertFalse(pw.rates_agree(info, ["1/1000", "1/400"], ["1/400"]))

    def test_a_rate_nowhere_on_the_wiki_disagrees(self):
        info = pw.wiki_rate_info("1/1,000")
        self.assertFalse(pw.rates_agree(info, ["1/1000", "1/400"], ["1/1000", "1/500"]))

    def test_footnote_framing_is_accepted(self):
        info = pw.wiki_rate_info("1/2,560{{efn|5/128 per unsired.}}")
        self.assertTrue(pw.rates_agree(info, [], ["1/2560", "5/128"]))


class TestDropSources(unittest.TestCase):
    ROWS = [
        {"item_name": "Beef", "page_name": "Brutus",
         "drop_json": json.dumps({"Rarity": "1/1,000", "Approx": False, "Alt Rarity": ""})},
        {"item_name": "Beef", "page_name": "Demonic Brutus",
         "drop_json": json.dumps({"Rarity": "1/400", "Approx": True, "Alt Rarity": ""})},
        {"item_name": "Nid", "page_name": "Araxxor",
         "drop_json": json.dumps({"Rarity": "1/3,000", "Approx": False, "Alt Rarity": "1/1,500"})},
        {"item_name": "Nid", "page_name": "Broken", "drop_json": "{not json"},
    ]

    def test_rows_become_rates_per_source(self):
        got = pw.parse_drop_rows(self.ROWS)
        self.assertEqual([pw.source_label(e) for e in got["Beef"]],
                         ["1/1000 (Brutus)", "~1/400 (Demonic Brutus)"])

    def test_alt_rarity_is_a_second_rate(self):
        got = pw.parse_drop_rows(self.ROWS)
        self.assertEqual(sorted(e["rate"] for e in got["Nid"]), ["1/1500", "1/3000"])

    def test_query_names_every_item(self):
        q = pw.bucket_query(["Beef", "Lil' Zik"])
        self.assertIn("{'item_name',\"Lil' Zik\"}", q)
        self.assertTrue(q.endswith(".run()"))

    def test_names_that_would_end_the_string_are_not_sent(self):
        # nothing askable is left, so no request is made
        self.assertEqual(pw.drop_sources(['Evil"}).run() --', "back\\slash"]), {})


class TestPluginRawIds(unittest.TestCase):
    def test_hard_coded_ids_are_found_and_constants_are_not(self):
        d = Path(tempfile.mkdtemp())
        java = d / "PetJsonCreator.java"
        java.write_text(
            "new Pet(PetGroup.OTHER, 16385, CHOCOLATE + DOG_INFO),\n"
            "new Pet(PetGroup.BOSS, NpcID.COWBOSS_PET, BEEF_INFO),\n"
            "new Pet(PetGroup.OTHER, 16386)\n",
            encoding="utf-8",
        )
        self.assertEqual(pw.plugin_raw_ids(java), {16385, 16386})


def findings(missing=(), conflicts=(), pages=None, wiki_rates=None, **extra):
    """A minimal petwatch --json result, as the commit check reads it."""
    out = {
        "all_missing": list(missing),
        "all_rate_conflicts": list(conflicts),
        "plugin_pages": pages or {},
        "wiki_rates": wiki_rates or {},
        "unresolved_constants": {},
        "unmatched_ids": [],
        "pets_json_mismatch": {},
        "runelite": {"release": "1.12.38"},
    }
    out.update(extra)
    return out


def missing(page, variant, ids, status="no-constant", hardcoded=False, constants=()):
    return {"page": page, "variant": variant, "ids": list(ids), "status": status,
            "hardcoded": hardcoded, "constants": list(constants)}


def view(ids=(), raw=(), info=(), rates=()):
    return {"ids": list(ids), "raw_ids": list(raw), "info": list(info), "rates": list(rates)}


BOTH = [review.CREATOR, review.PETS_JSON]


class TestCommitCheck(unittest.TestCase):
    def test_fixing_a_rate_matches(self):
        conflict = {"page": "Beef", "wiki": ["1/1000"], "plugin": ["1/1000", "1/500"], "sources": []}
        base = findings(conflicts=[conflict], pages={"Beef": view([1], info=["a"], rates=["1/1000", "1/500"])})
        head = findings(pages={"Beef": view([1], info=["b"], rates=["1/1000", "1/400"])},
                        wiki_rates={"Beef": {"stated": ["1/1000"], "ambiguous": False, "sources": []}})
        result = review.compare(base, head, BOTH)
        self.assertEqual(result["verdict"][0], "ok")
        self.assertIn("now matches", "\n".join(result["pages"][0][1].bullets()))

    def test_a_wrong_rate_does_not_match(self):
        conflict = {"page": "Beef", "wiki": ["1/1000"], "plugin": ["1/1000", "1/500"],
                    "sources": ["~1/400 (Demonic Brutus)"]}
        base = findings(pages={"Beef": view([1], info=["a"], rates=["1/1000", "1/400"])})
        head = findings(conflicts=[conflict], pages={"Beef": view([1], info=["b"], rates=["1/1000", "1/500"])})
        result = review.compare(base, head, BOTH)
        self.assertEqual(result["verdict"][0], "bad")
        self.assertIn("Demonic Brutus", "\n".join(result["pages"][0][1].bullets()))

    def test_a_disagreement_the_change_did_not_touch_is_context_only(self):
        conflict = {"page": "Beef", "wiki": ["1/1000"], "plugin": ["1/500"], "sources": []}
        base = findings(conflicts=[conflict], pages={"Beef": view([1], info=["a"], rates=["1/500"])})
        head = findings(conflicts=[conflict], pages={"Beef": view([1, 2], info=["a"], rates=["1/500"])})
        self.assertEqual(review.compare(base, head, BOTH)["verdict"][0], "info")

    def test_adding_a_missing_variant_matches(self):
        base = findings(missing=[missing("Beaver", "Camphor", [16000], "ready")],
                        pages={"Beaver": view([1])})
        head = findings(pages={"Beaver": view([1, 16000])})
        self.assertEqual(review.compare(base, head, BOTH)["verdict"][0], "ok")

    def test_adding_one_variant_of_several_is_partial(self):
        base = findings(missing=[missing("Dog", "Chocolate (Follower)", [1], "ready"),
                                 missing("Dog", "Chocolate (POH)", [2], "ready")])
        head = findings(missing=[missing("Dog", "Chocolate (POH)", [2], "ready")],
                        pages={"Dog": view([1])})
        self.assertEqual(review.compare(base, head, BOTH)["verdict"][0], "warn")

    def test_hard_coding_without_a_constant_matches(self):
        base = findings(missing=[missing("Dog", "Merle", [16386])])
        head = findings(missing=[missing("Dog", "Merle", [16386], hardcoded=True)],
                        pages={"Dog": view(raw=[16386])})
        self.assertEqual(review.compare(base, head, BOTH)["verdict"][0], "ok")

    def test_hard_coding_when_a_constant_is_released_does_not_match(self):
        base = findings(missing=[missing("Dog", "Merle", [16386], "ready", constants=["DOG_MERLE"])])
        head = findings(missing=[missing("Dog", "Merle", [16386], "ready", True, ["DOG_MERLE"])],
                        pages={"Dog": view(raw=[16386])})
        result = review.compare(base, head, BOTH)
        self.assertEqual(result["verdict"][0], "bad")
        self.assertIn("NpcID.DOG_MERLE", "\n".join(result["pages"][0][1].bullets()))

    def test_removing_a_variant_does_not_match(self):
        base = findings(pages={"Vorki": view([8025])})
        head = findings(missing=[missing("Vorki", "Vorki", [8025], "ready")])
        self.assertEqual(review.compare(base, head, BOTH)["verdict"][0], "bad")

    def test_pets_untouched_by_the_change_are_left_out(self):
        base = findings(missing=[missing("Other", "Other", [9])], pages={"Vorki": view([8025])})
        head = findings(missing=[missing("Other", "Other", [9])], pages={"Vorki": view([8025])})
        self.assertEqual(review.compare(base, head, BOTH)["pages"], [])

    def test_stale_pets_json_does_not_match(self):
        head = findings(pets_json_mismatch={"not_in_pets_json": [5], "only_in_pets_json": []})
        self.assertEqual(review.compare(findings(), head, BOTH)["verdict"][0], "bad")

    def test_creator_changed_alone_is_a_warning(self):
        result = review.compare(findings(), findings(), [review.CREATOR])
        self.assertEqual(result["verdict"][0], "warn")

    def test_a_range_on_the_wiki_is_not_called_a_match(self):
        base = findings(pages={"Olmlet": view([1], info=["a"], rates=["1/53"])})
        head = findings(pages={"Olmlet": view([1], info=["b"], rates=["1/60"])},
                        wiki_rates={"Olmlet": {"stated": ["1/53"], "ambiguous": True, "sources": []}})
        self.assertEqual(review.compare(base, head, BOTH)["verdict"][0], "info")

    def test_a_pull_request_is_commented_on_as_a_conversation(self):
        listing, create, patch = review.comment_urls("https://api.github.com", "o/r", "a" * 40, "12")
        self.assertTrue(listing.startswith("https://api.github.com/repos/o/r/issues/12/comments"))
        self.assertEqual(create, "https://api.github.com/repos/o/r/issues/12/comments")
        # editing an issue comment is not the same endpoint as creating one
        self.assertEqual(patch, "https://api.github.com/repos/o/r/issues/comments/")

    def test_without_a_pull_request_the_commit_is_commented_on(self):
        listing, create, patch = review.comment_urls("https://api.github.com", "o/r", "a" * 40, "")
        self.assertIn("/commits/" + "a" * 40 + "/comments", create)
        self.assertEqual(patch, "https://api.github.com/repos/o/r/comments/")
        self.assertIn("/commits/", listing)

    def test_comment_is_marked_and_escapes_wiki_text(self):
        base = findings(missing=[missing("Pet", "[x](http://evil)", [1], "ready")])
        head = findings(pages={"Pet": view([1])})
        body = review.render_comment(review.compare(base, head, BOTH), "a" * 40, "b" * 40, "1.12.38")
        self.assertTrue(body.startswith(review.MARKER))
        self.assertNotIn("](http://evil)", body)

    def test_long_lists_are_capped(self):
        many = [missing("Dog", "V" + str(i), [i]) for i in range(40)]
        head = findings(missing=[dict(m, hardcoded=True) for m in many], pages={"Dog": view(raw=range(40))})
        lines = review.compare(findings(missing=many), head, BOTH)["pages"][0][1].bullets()
        self.assertEqual(len(lines), 1)
        self.assertIn("and 28 more", lines[0])


if __name__ == "__main__":
    unittest.main(verbosity=2)
