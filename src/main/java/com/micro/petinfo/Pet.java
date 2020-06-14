/* Copyright (c) 2020 by micro
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Portions of the code are based off of the "Implings" RuneLite plugin.
 * The "Implings" is:
 * Copyright (c) 2017, Robin <robin.weymans@gmail.com>
 * All rights reserved.
 */

package com.micro.petinfo;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.NpcID;

import java.util.Map;

@AllArgsConstructor
@Getter
public enum Pet
{
	ABYSSAL_ORPHAN(PetGroup.BOSS, NpcID.ABYSSAL_ORPHAN, "is obtained by placing an unsired on the Font of Consumption, at a rate of 5/128."),
	ABYSSAL_ORPHAN_5884(PetGroup.BOSS, NpcID.ABYSSAL_ORPHAN_5884, "is obtained by placing an unsired on the Font of Consumption, at a rate of 5/128."),

	BABY_CHINCHOMPA(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6719(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6719, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6720(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6720, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6721(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6721, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6756(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6756, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6757(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6757, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6758(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6758, "is obtained while catching chinchompas."),
	BABY_CHINCHOMPA_6759(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6759, "is obtained while catching chinchompas."),

	BABY_MOLE(PetGroup.BOSS, NpcID.BABY_MOLE, "is obtained by killing the Giant Mole, at a rate of 1/3000."),
	BABY_MOLE_5781(PetGroup.BOSS, NpcID.BABY_MOLE_5781, "is dropped by the Giant Mole, at a rate of 1/3000."),
	BABY_MOLE_5782(PetGroup.BOSS, NpcID.BABY_MOLE_5782, "is dropped by the Giant Mole, at a rate of 1/3000."),
	BABY_MOLE_6635(PetGroup.BOSS, NpcID.BABY_MOLE_6635, "is dropped by the Giant Mole, at a rate of 1/3000."),
	BABY_MOLE_6651(PetGroup.BOSS, NpcID.BABY_MOLE_6651, "is dropped by the Giant Mole, at a rate of 1/3000."),

	BEAVER(PetGroup.SKILLING, NpcID.BEAVER, "is obtained while training Woodcutting."),
	BEAVER_6724(PetGroup.SKILLING, NpcID.BEAVER_6724, "is obtained while training Woodcutting."),

	BLOODHOUND(PetGroup.OTHER, NpcID.BLOODHOUND, "is obtained by completing Master Clue Scrolls, at a rate of 1/1000."),
	BLOODHOUND_7232(PetGroup.OTHER, NpcID.BLOODHOUND_7232, "is obtained by completing Master Clue Scrolls, at a rate of 1/1000."),

	CALLISTO_CUB(PetGroup.BOSS, NpcID.CALLISTO_CUB, "is dropped by Callisto, at a rate of 1/2000."),
	CALLISTO_CUB_5558(PetGroup.BOSS, NpcID.CALLISTO_CUB_5558, "is dropped by Callisto, at a rate of 1/2000."),

	CAT(PetGroup.OTHER, NpcID.CAT, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_1619(PetGroup.OTHER, NpcID.CAT_1619, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_1620(PetGroup.OTHER, NpcID.CAT_1620, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_1621(PetGroup.OTHER, NpcID.CAT_1621, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_1622(PetGroup.OTHER, NpcID.CAT_1622, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_1623(PetGroup.OTHER, NpcID.CAT_1623, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_1624(PetGroup.OTHER, NpcID.CAT_1624, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_3831(PetGroup.OTHER, NpcID.CAT_3831, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_3832(PetGroup.OTHER, NpcID.CAT_3832, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_6662(PetGroup.OTHER, NpcID.CAT_6662, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_6663(PetGroup.OTHER, NpcID.CAT_6663, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_6664(PetGroup.OTHER, NpcID.CAT_6664, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_6665(PetGroup.OTHER, NpcID.CAT_6665, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_6666(PetGroup.OTHER, NpcID.CAT_6666, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_6667(PetGroup.OTHER, NpcID.CAT_6667, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_7380(PetGroup.OTHER, NpcID.CAT_7380, "is obtained by letting a kitten grow for about 2 hours."),
	CAT_8594(PetGroup.OTHER, NpcID.CAT_8594, "is obtained by letting a kitten grow for about 2 hours."),

	CHAOS_ELEMENTAL_JR(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_JR, "is dropped by the Chaos Elemental, at a rate of 1/33; or the Chaos Fanatic, at 1/1000."),
	CHAOS_ELEMENTAL_JR_5907(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_JR_5907, "is dropped by the Chaos Elemental, at a rate of 1/33; or the Chaos Fanatic, at 1/1000."),

	CHOMPY_CHICK(PetGroup.OTHER, NpcID.CHOMPY_CHICK, "is dropped by Chompy birds after completing the elite Western Provinces Diary, at a rate of 1/500."),
	CHOMPY_CHICK_4002(PetGroup.OTHER, NpcID.CHOMPY_CHICK_4002, "is dropped by Chompy birds after completing the elite Western Provinces Diary, at a rate of 1/500."),

	CLOCKWORK_CAT(PetGroup.TOY, NpcID.CLOCKWORK_CAT, "can be crafted in a POH with 84 Crafting and a Crafting table 4."),
	CLOCKWORK_CAT_541(PetGroup.TOY, NpcID.CLOCKWORK_CAT_541, "can be crafted in a POH with 84 Crafting and a Crafting table 4."),
	CLOCKWORK_CAT_2782(PetGroup.TOY, NpcID.CLOCKWORK_CAT_2782, "can be crafted in a POH with 84 Crafting and a Crafting table 4."),
	CLOCKWORK_CAT_6616(PetGroup.TOY, NpcID.CLOCKWORK_CAT_6661, "can be crafted in a POH with 84 Crafting and a Crafting table 4."),

	CORPOREAL_CRITTER(PetGroup.BOSS, NpcID.CORPOREAL_CRITTER, "is obtained by causing a pet Dark Core to metamorphosize."),
	CORPOREAL_CRITTER_8010(PetGroup.BOSS, NpcID.CORPOREAL_CRITTER_8010, "is obtained by causing a pet Dark Core to metamorphosize."),

	CORRUPTED_YOUNGLLEF(PetGroup.BOSS, NpcID.CORRUPTED_YOUNGLLEF, "is obtained by completing the Corrupted Gauntlet then causing a Youngllef to metamorphosize."),
	CORRUPTED_YOUNGLLEF_8738(PetGroup.BOSS, NpcID.CORRUPTED_YOUNGLLEF_8738, "is obtained by completing the Corrupted Gauntlet then causing a Youngllef to metamorphosize."),

	DAGANNOTH_PRIME_JR(PetGroup.BOSS, NpcID.DAGANNOTH_PRIME_JR, "is dropped by Dagannoth Prime, at a rate of 1/5000."),
	DAGANNOTH_PRIME_JR_9929(PetGroup.BOSS, NpcID.DAGANNOTH_PRIME_JR_6629, "is dropped by Dagannoth Prime, at a rate of 1/5000."),

	DAGANNOTH_REX_JR(PetGroup.BOSS, NpcID.DAGANNOTH_REX_JR, "is dropped by Dagannoth Rex, at a rate of 1/5000."),
	DAGANNOTH_REX_JR_6641(PetGroup.BOSS, NpcID.DAGANNOTH_REX_JR_6641, "is dropped by Dagannoth Rex, at a rate of 1/5000."),

	DAGANNOTH_SUPREME_JR(PetGroup.BOSS, NpcID.DAGANNOTH_SUPREME_JR, "is dropped by Dagannoth Supreme, at a rate of 1/5000."),
	DAGANNOTH_SUPREME_JR_6628(PetGroup.BOSS, NpcID.DAGANNOTH_SUPREME_JR_6628, "is dropped by Dagannoth Supreme, at a rate of 1/5000."),

	DARK_CORE(PetGroup.BOSS, NpcID.DARK_CORE, "is dropped by the Corporeal Beast, at a rate of 1/5000."),    // Not sure this is the pet dark core, but it looks right
	DARK_CORE_388(PetGroup.BOSS, NpcID.DARK_CORE_388, "is dropped by the Corporeal Beast, at a rate of 1/5000."),

	EEK(PetGroup.OTHER, NpcID.EEK, "was obtained during the 2018 Halloween event."),

	FISHBOWL(PetGroup.OTHER, NpcID.FISHBOWL, "can be cought is in Harry's Fishing Shop"),
	FISHBOWL_6659(PetGroup.OTHER, NpcID.FISHBOWL_6659, "can be cought is in Harry's Fishing Shop"),
	FISHBOWL_6660(PetGroup.OTHER, NpcID.FISHBOWL_6660, "can be cought is in Harry's Fishing Shop"),

	GENERAL_GRAARDOR_JR(PetGroup.BOSS, NpcID.GENERAL_GRAARDOR_JR, "is dropped by General Graardor, at a rate of 1/5000"),
	GENERAL_GRAARDOR_JR_6644(PetGroup.BOSS, NpcID.GENERAL_GRAARDOR_JR_6644, "is dropped by General Graardor, at a rate of 1/5000"),

	GIANT_SQUIRREL(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL, "is obtained by training Agility."),
	GIANT_SQUIRREL_7351(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL_7351, "is obtained by training Agility."),
	GIANT_SQUIRREL_9666(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL_9666, "is obtained by training Agility."), // There's usualy an even number, so I'm not sure what's up

	HELLCAT(PetGroup.OTHER, NpcID.HELLCAT, "is obtained by having a pet Cat hunt Hell-Rats."),
	HELLCAT_6668(PetGroup.OTHER, NpcID.HELLCAT_6668, "is obtained by having a pet Cat hunt Hell-Rats."),

	HELLKITTEN(PetGroup.OTHER, NpcID.HELLKITTEN, "is obtained by having a pet Kitten hunt Hell-Rats."),

	HELLPUPPY(PetGroup.BOSS, NpcID.HELLPUPPY, "is dropped by Cerberus, at a rate of 1/3000."),
	HELLPUPPY_3099(PetGroup.BOSS, NpcID.HELLPUPPY_3099, "is dropped by Cerberus, at a rate of 1/3000."),

	HERBI(PetGroup.SKILLING, NpcID.HERBI, "is obtained by harvesting Herbiboars, at a rate of 1/6500."),
	HERBI_7760(PetGroup.SKILLING, NpcID.HERBI_7760, "is obtained by harvesting Herbiboars, at a rate of 1/6500."),

	HERON(PetGroup.SKILLING, NpcID.HERON, "is obtained while training Fishing."),
	HERON_6722(PetGroup.SKILLING, NpcID.HERON_6722, "is obtained while training Fishing."),

	IKKLE_HYDRA(PetGroup.BOSS, NpcID.IKKLE_HYDRA, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8493(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8493, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8494(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8494, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8495(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8495, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8517(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8517, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8518(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8518, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8519(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8519, "is dropped by Alchemical Hydra, at a rate of 1/300."),
	IKKLE_HYDRA_8520(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8520, "is dropped by Alchemical Hydra, at a rate of 1/300."),

	JALNIBREK(PetGroup.BOSS, NpcID.JALNIBREK, "is obtained by completing the inferno, at a rate of 1/100 (or 1/75 while on a TzKal-Zuk tast)."),
	JALNIBREK_7675(PetGroup.BOSS, NpcID.JALNIBREK_7675, "is obtained by completing the inferno, at a rate of 1/100 (or 1/75 while on a TzKal-Zuk tast)."),

	KALPHITE_PRINCESS(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS, "is dropped by Kalphite Queen, at a rate of 1/3000."),
	KALPHITE_PRINCESS_6638(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6638, "is dropped by Kalphite Queen, at a rate of 1/3000."),
	KALPHITE_PRINCESS_6653(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6653, "is dropped by Kalphite Queen, at a rate of 1/3000."),
	KALPHITE_PRINCESS_6654(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6654, "is dropped by Kalphite Queen, at a rate of 1/3000."),

	KITTEN(PetGroup.OTHER, NpcID.KITTEN, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),
	KITTEN_5591(PetGroup.OTHER, NpcID.KITTEN_5591, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),
	KITTEN_5592(PetGroup.OTHER, NpcID.KITTEN_5592, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),
	KITTEN_5593(PetGroup.OTHER, NpcID.KITTEN_5593, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),
	KITTEN_5594(PetGroup.OTHER, NpcID.KITTEN_5594, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),
	KITTEN_5595(PetGroup.OTHER, NpcID.KITTEN_5595, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),
	KITTEN_5596(PetGroup.OTHER, NpcID.KITTEN_5596, "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest."),

	KRAKEN_6640(PetGroup.BOSS, NpcID.KRAKEN_6640, "is dropped by Kraken, at a rate of 1/3000."),
	KRAKEN_6656(PetGroup.BOSS, NpcID.KRAKEN_6656, "is dropped by Kraken, at a rate of 1/3000."),

	KREEARRA_JR(PetGroup.BOSS, NpcID.KREEARRA_JR, "is dropped by Kree'arra, at a rate of 1/5000."),
	KREEARRA_JR_6643(PetGroup.BOSS, NpcID.KREEARRA_JR_6643, "is dropped by Kree'arra, at a rate of 1/5000."),

	KRIL_TSUTSAROTH_JR(PetGroup.BOSS, NpcID.KRIL_TSUTSAROTH_JR, "is dropped by K'ril Tsutsaroth."),
	KRIL_TSUTSAROTH_JR_6647(PetGroup.BOSS, NpcID.KRIL_TSUTSAROTH_JR_6647, "is dropped by K'ril Tsutsaroth."),

	LAZY_CAT(PetGroup.OTHER, NpcID.LAZY_CAT, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_1627(PetGroup.OTHER, NpcID.LAZY_CAT_1627, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_1628(PetGroup.OTHER, NpcID.LAZY_CAT_1628, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_1629(PetGroup.OTHER, NpcID.LAZY_CAT_1629, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_1630(PetGroup.OTHER, NpcID.LAZY_CAT_1630, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_1631(PetGroup.OTHER, NpcID.LAZY_CAT_1631, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_6683(PetGroup.OTHER, NpcID.LAZY_CAT_6683, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_6684(PetGroup.OTHER, NpcID.LAZY_CAT_6684, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_6685(PetGroup.OTHER, NpcID.LAZY_CAT_6685, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_6686(PetGroup.OTHER, NpcID.LAZY_CAT_6686, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_6687(PetGroup.OTHER, NpcID.LAZY_CAT_6687, "is obtained by letting a Wily Cat grow for about an hour."),
	LAZY_CAT_6688(PetGroup.OTHER, NpcID.LAZY_CAT_6688, "is obtained by letting a Wily Cat grow for about an hour."),

	LAZY_HELLCAT(PetGroup.OTHER, NpcID.LAZY_HELLCAT, "is obtained by letting a Wily Hellcat at grow for about an hour."),
	LAZY_HELLCAT_6689(PetGroup.OTHER, NpcID.LAZY_HELLCAT_6689, "is obtained by letting a Wily Hellcat grow for about an hour."),

	LIL_ZIK(PetGroup.BOSS, NpcID.LIL_ZIK, "is obtained by completing the theater of blood, at a rate of 1/650 (with optimal performance)."),
	LIL_ZIK_8377(PetGroup.BOSS, NpcID.LIL_ZIK_8337, "is obtained by completing the theater of blood, at a rate of 1/650 (with optimal performance)."),

	LITTLE_NIGHTMARE(PetGroup.BOSS, NpcID.LITTLE_NIGHTMARE, "is dropped by The Nightmare, at a rate of 1/4000 (or 1/3800 as MVP)."),
	LITTLE_NIGHTMARE_9399(PetGroup.BOSS, NpcID.LITTLE_NIGHTMARE_9399, "is dropped by The Nightmare, at a rate of 1/4000 (or 1/3800 as MVP)."),

	MIDNIGHT(PetGroup.BOSS, NpcID.MIDNIGHT, "is obtained by causing a pet Noon to metamorphosize."),
	MIDNIGHT_7893(PetGroup.BOSS, NpcID.MIDNIGHT_7893, "is obtained by causing a pet Noon to metamorphosize."),

	NOON(PetGroup.BOSS, NpcID.NOON, "is dropped by the Grotesque Guardians, at a rate of 1/3000."),
	NOON_7892(PetGroup.BOSS, NpcID.NOON_7892, "is dropped by the Grotesque Guardians, at a rate of 1/3000."),

	OLMLET(PetGroup.BOSS, NpcID.OLMLET, "is dropped by the Great Olm, at a rate of 1/53 per received broadcasted unique item."),
	OLMLET_7520(PetGroup.BOSS, NpcID.OLMLET_7520, "is dropped by the Great Olm, at a rate of 1/53 per received broadcasted unique item."),

	OVERGROWN_CAT(PetGroup.OTHER, NpcID.OVERGROWN_CAT, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_5599(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5599, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_5600(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5600, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_5601(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5601, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_5602(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5602, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_5603(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5603, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_6676(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6676, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_6677(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6677, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_6678(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6678, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_6679(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6679, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_6680(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6680, "is obtained by letting a pet Cat grow for about 2-3 hours."),
	OVERGROWN_CAT_6681(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6681, "is obtained by letting a pet Cat grow for about 2-3 hours."),

	OVERGROWN_HELLCAT(PetGroup.OTHER, NpcID.OVERGROWN_HELLCAT, "is obtained by letting a pet Hellcat grow for about 2-3 hours."),
	OVERGROWN_HELLCAT_6682(PetGroup.OTHER, NpcID.OVERGROWN_HELLCAT_6682, "is obtained by letting a pet Hellcat grow for about 2-3 hours."),

	PET_ROCK(PetGroup.OTHER, NpcID.PET_ROCK, "can be received from Askeladden after The Fremennik Trials."),
	PET_ROCK_6657(PetGroup.OTHER, NpcID.PET_ROCK_6657, "can be received from Askeladden after The Fremennik Trials."),

	PENANCE_PET(PetGroup.OTHER, NpcID.PENANCE_PET, "is received from High-level gambles in Barbarian Assault, at a rate of 1/1000."),
	PENANCE_PET_6674(PetGroup.OTHER, NpcID.PENANCE_PET_6674, "is received from High-level gambles in Barbarian Assault, at a rate of 1/1000."),

	PHOENIX(PetGroup.SKILLING, NpcID.PHOENIX, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3078(PetGroup.SKILLING, NpcID.PHOENIX_3078, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3079(PetGroup.SKILLING, NpcID.PHOENIX_3079, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3080(PetGroup.SKILLING, NpcID.PHOENIX_3080, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3081(PetGroup.SKILLING, NpcID.PHOENIX_3081, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3082(PetGroup.SKILLING, NpcID.PHOENIX_3082, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3083(PetGroup.SKILLING, NpcID.PHOENIX_3083, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_3084(PetGroup.SKILLING, NpcID.PHOENIX_3084, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_7368(PetGroup.SKILLING, NpcID.PHOENIX_7368, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),
	PHOENIX_7370(PetGroup.SKILLING, NpcID.PHOENIX_7370, "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000."),

	PRINCE_BLACK_DRAGON(PetGroup.BOSS, NpcID.PRINCE_BLACK_DRAGON, "is dropped by the King Black Dragon, at a rate of 1/3000."),
	PRINCE_BLACK_DRAGON_6652(PetGroup.BOSS, NpcID.PRINCE_BLACK_DRAGON_6652, "is dropped by the King Black Dragon, at a rate of 1/3000."),

	RIFT_GUARDIAN(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7338(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7338, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7339(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7339, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7340(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7340, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7341(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7341, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7342(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7342, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7343(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7343, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7344(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7344, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7345(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7345, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7346(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7346, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7347(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7347, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7348(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7348, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7349(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7349, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7350(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7350, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7354(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7354, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7355(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7355, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7356(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7356, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7357(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7357, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7358(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7358, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7359(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7359, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7360(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7360, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7361(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7361, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7362(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7362, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7363(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7363, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7364(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7364, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7365(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7365, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7366(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7366, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_7367(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7367, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_8024(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_8024, "is obtained while training Runecraft."),
	RIFT_GUARDIAN_8028(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_8028, "is obtained while training Runecraft."),

	ROCK_GOLEM(PetGroup.SKILLING, NpcID.ROCK_GOLEM, "is obtained while training Mining."),
	ROCK_GOLEM_6725(PetGroup.SKILLING, NpcID.ROCK_GOLEM_6725, "is obtained while training Mining."),
	ROCK_GOLEM_6726(PetGroup.SKILLING, NpcID.ROCK_GOLEM_6726, "is obtained while training Mining."),
	ROCK_GOLEM_6727(PetGroup.SKILLING, NpcID.ROCK_GOLEM_6727, "is obtained while training Mining."),
	ROCK_GOLEM_6728(PetGroup.SKILLING, NpcID.ROCK_GOLEM_6728, "is obtained while training Mining."),
	ROCK_GOLEM_6729(PetGroup.SKILLING, NpcID.ROCK_GOLEM_6729, "is obtained while training Mining."),
	ROCK_GOLEM_6730(PetGroup.SKILLING, NpcID.ROCK_GOLEM_6730, "is obtained while training Mining."),
	ROCK_GOLEM_7439(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7439, "is obtained while training Mining."),
	ROCK_GOLEM_7440(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7440, "is obtained while training Mining."),
	ROCK_GOLEM_7441(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7441, "is obtained while training Mining."),
	ROCK_GOLEM_7442(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7442, "is obtained while training Mining."),
	ROCK_GOLEM_7443(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7443, "is obtained while training Mining."),
	ROCK_GOLEM_7444(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7444, "is obtained while training Mining."),
	ROCK_GOLEM_7445(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7445, "is obtained while training Mining."),
	ROCK_GOLEM_7446(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7446, "is obtained while training Mining."),
	ROCK_GOLEM_7447(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7447, "is obtained while training Mining."),
	ROCK_GOLEM_7448(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7448, "is obtained while training Mining."),
	ROCK_GOLEM_7449(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7449, "is obtained while training Mining."),
	ROCK_GOLEM_7450(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7450, "is obtained while training Mining."),
	ROCK_GOLEM_7451(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7451, "is obtained while training Mining."),
	ROCK_GOLEM_7452(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7452, "is obtained while training Mining."),
	ROCK_GOLEM_7453(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7453, "is obtained while training Mining."),
	ROCK_GOLEM_7454(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7454, "is obtained while training Mining."),
	ROCK_GOLEM_7455(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7455, "is obtained while training Mining."),
	ROCK_GOLEM_7642(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7642, "is obtained while training Mining."),
	ROCK_GOLEM_7643(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7643, "is obtained while training Mining."),
	ROCK_GOLEM_7644(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7644, "is obtained while training Mining."),
	ROCK_GOLEM_7645(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7645, "is obtained while training Mining."),
	ROCK_GOLEM_7646(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7646, "is obtained while training Mining."),
	ROCK_GOLEM_7647(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7647, "is obtained while training Mining."),
	ROCK_GOLEM_7648(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7648, "is obtained while training Mining."),
	ROCK_GOLEM_7711(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7711, "is obtained while training Mining."),
	ROCK_GOLEM_7736(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7736, "is obtained while training Mining."),
	ROCK_GOLEM_7737(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7737, "is obtained while training Mining."),
	ROCK_GOLEM_7738(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7738, "is obtained while training Mining."),
	ROCK_GOLEM_7739(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7739, "is obtained while training Mining."),
	ROCK_GOLEM_7740(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7740, "is obtained while training Mining."),
	ROCK_GOLEM_7741(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7741, "is obtained while training Mining."),

	ROCKY(PetGroup.SKILLING, NpcID.ROCKY, "is obtained while training Thieving."),
	ROCKY_7353(PetGroup.SKILLING, NpcID.ROCKY_7353, "is obtained while training Thieving."),

	SCORPIAS_OFFSPRING(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING, "is dropped by Scorpia, at a rate of 1/2,015.75"),
	SCORPIAS_OFFSPRING_5561(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING_5561, "is dropped by Scorpia, at a rate of 1/2,015.75"),
	SCORPIAS_OFFSPRING_6616(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING_6616, "is dropped by Scorpia, at a rate of 1/2,015.75"),

	SKOTOS(PetGroup.BOSS, NpcID.SKOTOS, "is ropped by Skotizo, at a rate of 1/65."),
	SKOTOS_7671(PetGroup.BOSS, NpcID.SKOTOS_7671, "is ropped by Skotizo, at a rate of 1/65."),

	SMOKE_DEVIL_6639(PetGroup.BOSS, NpcID.SMOKE_DEVIL_6639, "is dropped by the Thermonuclear smoke devil, at a rate of 1/3000."),
	SMOKE_DEVIL_6655(PetGroup.BOSS, NpcID.SMOKE_DEVIL_6655, "is dropped by the Thermonuclear smoke devil, at a rate of 1/3000."),
	SMOKE_DEVIL_8482(PetGroup.BOSS, NpcID.SMOKE_DEVIL_8482, "is dropped by the Thermonuclear smoke devil, at a rate of 1/3000."),
	SMOKE_DEVIL_8483(PetGroup.BOSS, NpcID.SMOKE_DEVIL_8483, "is dropped by the Thermonuclear smoke devil, at a rate of 1/3000."),

	SMOLCANO(PetGroup.BOSS, NpcID.SMOLCANO, "is dropped by Zalcano, at a rate of 1/2250."),
	SMOLCANO_8739(PetGroup.BOSS, NpcID.SMOLCANO_8739, "is dropped by Zalcano, at a rate of 1/2250."),

	// Not sure if there are more pet ids, or if the boss fight minions will apear
	SNAKELING_2127(PetGroup.BOSS, NpcID.SNAKELING_2127, "is dropped by Zulrah, at a rate of 1/4000."),
	SNAKELING_2130(PetGroup.BOSS, NpcID.SNAKELING_2130, "is dropped by Zulrah, at a rate of 1/4000."),
	SNAKELING_2131(PetGroup.BOSS, NpcID.SNAKELING_2131, "is dropped by Zulrah, at a rate of 1/4000."),
	SNAKELING_2132(PetGroup.BOSS, NpcID.SNAKELING_2132, "is dropped by Zulrah, at a rate of 1/4000."),

	SRARACHA(PetGroup.BOSS, NpcID.SRARACHA, "is dropped by Sarachnis, at a rate of 1/3000/"),
	SRARACHA_2144(PetGroup.BOSS, NpcID.SRARACHA_2144, "is dropped by Sarachnis, at a rate of 1/3000/"),

	TANGLEROOT(PetGroup.SKILLING, NpcID.TANGLEROOT, "is obtained while training Farming."),
	TANGLEROOT_7352(PetGroup.SKILLING, NpcID.TANGLEROOT_7352, "is obtained while training Farming."),
	TANGLEROOT_9492(PetGroup.SKILLING, NpcID.TANGLEROOT_9492, "is obtained while training Farming."),
	TANGLEROOT_9493(PetGroup.SKILLING, NpcID.TANGLEROOT_9493, "is obtained while training Farming."),
	TANGLEROOT_9494(PetGroup.SKILLING, NpcID.TANGLEROOT_9494, "is obtained while training Farming."),
	TANGLEROOT_9495(PetGroup.SKILLING, NpcID.TANGLEROOT_9495, "is obtained while training Farming."),
	TANGLEROOT_9496(PetGroup.SKILLING, NpcID.TANGLEROOT_9496, "is obtained while training Farming."),
	TANGLEROOT_9497(PetGroup.SKILLING, NpcID.TANGLEROOT_9497, "is obtained while training Farming."),
	TANGLEROOT_9498(PetGroup.SKILLING, NpcID.TANGLEROOT_9498, "is obtained while training Farming."),	// Dragon Fruit Variant
	TANGLEROOT_9499(PetGroup.SKILLING, NpcID.TANGLEROOT_9499, "is obtained while training Farming."),
	TANGLEROOT_9500(PetGroup.SKILLING, NpcID.TANGLEROOT_9500, "is obtained while training Farming."),	// White Lily Variant
	TANGLEROOT_9501(PetGroup.SKILLING, NpcID.TANGLEROOT_9501, "is obtained while training Farming."),

	TOY_DOLL(PetGroup.TOY, NpcID.TOY_DOLL, "can be crafted in a POH with 18 Crafting and a Crafting table 3."),
	TOY_DOLL_9253(PetGroup.TOY, NpcID.TOY_DOLL_9253, "can be crafted in a POH with 18 Crafting and a Crafting table 3."),

	TOY_MOUSE(PetGroup.TOY, NpcID.TOY_MOUSE, "can be crafted in a POH with 33 Crafting and a Crafting table 4."),
	TOY_MOUSE_9255(PetGroup.TOY, NpcID.TOY_MOUSE_9255, "can be crafted in a POH with 33 Crafting and a Crafting table 4."),

	TOY_SOLDIER(PetGroup.TOY, NpcID.TOY_SOLDIER, "can be crafted in a POH with 13 Crafting and a Crafting table 3."),
	TOY_SOLDIER_9251(PetGroup.TOY, NpcID.TOY_SOLDIER_9251, "can be crafted in a POH with 13 Crafting and a Crafting table 3."),

	TZREKJAD(PetGroup.BOSS, NpcID.TZREKJAD, "is dropped by TzTok-Jad, at a rate of 1/200 (or 1/100 if on a slayer task)."),
	TZREKJAD_5893(PetGroup.BOSS, NpcID.TZREKJAD_5893, "is dropped by TzTok-Jad, at a rate of 1/200 (or 1/100 if on a slayer task)."),

	TZREKZUK(PetGroup.BOSS, NpcID.TZREKZUK, "is obtained by causing a pet Jal-nib-rek to metamorphosize."),
	TZREKZUK_8011(PetGroup.BOSS, NpcID.TZREKZUK_8011, "is obtained by causing a pet Jal-nib-rek to metamorphosize."),

	VENENATIS_SPIDERLING(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING, "is dropped by Venenatis at a rate of 1/2000."),
	VENENATIS_SPIDERLING_5557(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING_5557, "is dropped by Venenatis at a rate of 1/2000."),

	VETION_JR(PetGroup.BOSS, NpcID.VETION_JR, "is dropped by Vet'ion, at a rate of 1/2000."),
	VETION_JR_5537(PetGroup.BOSS, NpcID.VETION_JR_5537, "is dropped by Vet'ion, at a rate of 1/2000."),
	VETION_JR_5559(PetGroup.BOSS, NpcID.VETION_JR_5559, "is dropped by Vet'ion, at a rate of 1/2000."),
	VETION_JR_5560(PetGroup.BOSS, NpcID.VETION_JR_5560, "is dropped by Vet'ion, at a rate of 1/2000."),

	VORKI(PetGroup.BOSS, NpcID.VORKI, "is dropped by Vorkath, at a rate of 1/3000."),
	VORKI_8029(PetGroup.BOSS, NpcID.VORKI_8029, "is dropped by Vorkath, at a rate of 1/3000."),

	WILY_CAT(PetGroup.OTHER, NpcID.WILY_CAT, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_5585(PetGroup.OTHER, NpcID.WILY_CAT_5585, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_5586(PetGroup.OTHER, NpcID.WILY_CAT_5586, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_5587(PetGroup.OTHER, NpcID.WILY_CAT_5587, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_5588(PetGroup.OTHER, NpcID.WILY_CAT_5588, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_5589(PetGroup.OTHER, NpcID.WILY_CAT_5589, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_6690(PetGroup.OTHER, NpcID.WILY_CAT_6690, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_6691(PetGroup.OTHER, NpcID.WILY_CAT_6691, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_6692(PetGroup.OTHER, NpcID.WILY_CAT_6692, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_6693(PetGroup.OTHER, NpcID.WILY_CAT_6693, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_6694(PetGroup.OTHER, NpcID.WILY_CAT_6694, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),
	WILY_CAT_6695(PetGroup.OTHER, NpcID.WILY_CAT_6695, "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest."),

	WILY_HELLCAT(PetGroup.OTHER, NpcID.WILY_HELLCAT, "is obtained by asking Felkrash to train an Overgrown Hellcat, after completing the Ratcatchers quest."),
	WILY_HELLCAT_6696(PetGroup.OTHER, NpcID.WILY_HELLCAT_6696, "is obtained by asking Felkrash to train an Overgrown Hellcat, after completing the Ratcatchers quest."),

	YOUNGLLEF(PetGroup.BOSS, NpcID.YOUNGLLEF, "is obtained by completing The Gauntlet, at a rate of 1/2000 (or 1/800 for The Corrupted Gauntlet)."),
	YOUNGLLEF_8737(PetGroup.BOSS, NpcID.YOUNGLLEF_8737, "is obtained by completing The Gauntlet, at a rate of 1/2000 (or 1/800 for The Corrupted Gauntlet)."),

	ZILYANA_JR(PetGroup.BOSS, NpcID.ZILYANA_JR, "is dropped by Commander Zilyana, at a rate of 1/5000."),
	ZILYANA_JR_6646(PetGroup.BOSS, NpcID.ZILYANA_JR_6646, "is dropped by Commander Zilyana, at a rate of 1/5000.");

	private PetGroup petGroup;
	private final int npcId;
	private String info;

	private static final Map<Integer, Pet> PETS;

	static
	{
		ImmutableMap.Builder<Integer, Pet> builder = new ImmutableMap.Builder<>();

		for (Pet pet : values())
		{
			builder.put(pet.npcId, pet);
		}

		PETS = builder.build();
	}

	static Pet findPet(int npcId)
	{
		return PETS.get(npcId);
	}

	static String getInfo(int npcId)
	{
		return PETS.get(npcId).info;
	}
}
