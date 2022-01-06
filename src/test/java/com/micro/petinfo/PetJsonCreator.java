package com.micro.petinfo;

import com.google.common.collect.ImmutableMap;
import com.micro.petinfo.dataretrieval.Pet;
import com.micro.petinfo.dataretrieval.PetGroup;
import net.runelite.api.NpcID;
import net.runelite.client.RuneLite;
import net.runelite.http.api.RuneLiteAPI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetJsonCreator
{
	private static final int VERSION = 0;
	public static void main(String[] args) throws Exception
	{
		try {
			List<Pet> testPets = new ArrayList<>(getList());
			serializeToJson(testPets);
		}
		catch (IOException e) {
			System.out.println("welp " + e);
		}
		System.out.println("and done " + RuneLite.RUNELITE_DIR);
	}

	private static void serializeToJson(List<Pet> pets) throws IOException
	{
		ImmutableMap.Builder<Integer, Pet> builder = new ImmutableMap.Builder<>();
		for (Pet pet: pets)
		{
			builder.put(pet.npcId, pet);
		}
		String jsonStr = RuneLiteAPI.GSON.toJson(builder.build());

		FileWriter fr = new FileWriter("pets.json");
		fr.write(jsonStr);
		fr.close();
	}

	private static List<Pet> getList() {
		return new ArrayList<>(Arrays.asList(pets1));
	}

	/*
	 * Pet Info Strings
	 */
	private static final String ABYSSAL_ORPHAN_INFO = "is obtained by placing an unsired on the Font of Consumption, at a rate of 5/128.";
	private static final String BABY_CHINCHOMPA_INFO = "is obtained while catching chinchompas.";
	private static final String BABY_MOLE_INFO = "is obtained by killing the Giant Mole, at a rate of 1/3000.";
	private static final String BEAVER_INFO = "is obtained while training Woodcutting.";
	private static final String BLOODHOUND_INFO = "is obtained by completing Master Clue Scrolls, at a rate of 1/1000.";
	private static final String CALLISTO_CUB_INFO = "is dropped by Callisto, at a rate of 1/2000.";
	private static final String CAT_INFO = "is obtained by letting a kitten grow for about 2 hours.";
	private static final String CHAOS_ELEMENTAL_JR_INFO = "is dropped by the Chaos Elemental, at a rate of 1/300; or the Chaos Fanatic, at 1/1000.";
	private static final String CHOMPY_CHICK_INFO = "is dropped by Chompy birds, at a rate of 1/500, after completing the elite Western Provinces Diary.";
	private static final String CLOCKWORK_CAT_INFO = "can be crafted in a POH with 84 Crafting and a Crafting table 4.";
	private static final String CORPOREAL_CRITTER_INFO = "is obtained by causing a pet Dark Core to metamorphosize.";
	private static final String CORRUPTED_YOUNGLLEF_INFO = "is obtained by causing a Youngllef to metamorphosize, this can only be done after completing the Corrupted Gauntlet.";
	private static final String DAGANNOTH_PRIME_JR_INFO = "is dropped by Dagannoth Prime, at a rate of 1/5000.";
	private static final String DAGANNOTH_REX_JR_INFO = "is dropped by Dagannoth Rex, at a rate of 1/5000.";
	private static final String DAGANNOTH_SUPREME_JR_INFO = "is dropped by Dagannoth Supreme, at a rate of 1/5000.";
	private static final String DARK_CORE_INFO = "is dropped by the Corporeal Beast, at a rate of 1/5000.";
	private static final String EEK_INFO = "was obtained during the 2018 Halloween event.";
	private static final String FISHBOWL_INFO = "can be caught in Harry's Fishing Shop";
	private static final String GENERAL_GRAARDOR_JR_INFO = "is dropped by General Graardor, at a rate of 1/5000";
	private static final String GIANT_SQUIRREL_INFO = "is obtained by training Agility.";
	private static final String HELLCAT_INFO = "is obtained by having a pet Cat hunt Hell-Rats.";
	private static final String HELLKITTEN_INFO = "is obtained by having a pet Kitten hunt Hell-Rats.";
	private static final String HELLPUPPY_INFO = "is dropped by Cerberus, at a rate of 1/3000.";
	private static final String HERBI_INFO = "is obtained by harvesting Herbiboars, at a rate of 1/6500.";
	private static final String HERON_INFO = "is obtained while training Fishing.";
	private static final String IKKLE_HYDRA_INFO = "is dropped by Alchemical Hydra, at a rate of 1/3000.";
	private static final String JALNIBREK_INFO = "is obtained by completing the inferno, at a rate of 1/100 (or 1/75 while on a TzKal-Zuk task).";
	private static final String JALREKJAD_INFO = "is obtained by causing a Tzrek-jad to metamorphosize, this can only be done after completing all six of TzHaar-Ket-Rak's Challenges (this requires having also completed the Inferno).";
	private static final String KALPHITE_PRINCESS_INFO = "is dropped by Kalphite Queen, at a rate of 1/3000.";
	private static final String KITTEN_INFO = "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest.";
	private static final String KRAKEN_INFO ="is dropped by Kraken, at a rate of 1/3000.";
	private static final String KREEARRA_JR_INFO = "is dropped by Kree'arra, at a rate of 1/5000.";
	private static final String KRIL_TSUTSAROTH_JR_INFO = "is dropped by K'ril Tsutsaroth.";
	private static final String LAZY_CAT_INFO = "is obtained by letting a Wily Cat grow for about an hour.";
	private static final String LAZY_HELLCAT_INFO = "is obtained by letting a Wily Hellcat at grow for about an hour.";
	private static final String LIL_CREATOR_INFO = "is obtained by opening the Spoils of War, a reward from the Soul Wars minigame, at a rate of 1/400.";
	private static final String LIL_ZIK_INFO = "is obtained by completing the Theater of Blood, at a rate of 1/650 (with optimal performance).";
	private static final String LITTLE_NIGHTMARE_INFO = "is dropped by The Nightmare, at a rate of 1/4000 (or 1/3800 as MVP).";
	private static final String MAZ_INFO = "isn't a true pet, but you can get in her good graces by giving her an acorn.";
	private static final String MIDNIGHT_INFO = "is obtained by causing a pet Noon to metamorphosize.";
	private static final String NEXLING_INFO = "is dropped by Nex.";
	private static final String NOON_INFO = "is dropped by the Grotesque Guardians, at a rate of 1/3000.";
	private static final String OLMLET_INFO = "is dropped by the Great Olm, at a rate of 1/53 per received broadcasted unique item.";
	private static final String OVERGROWN_CAT_INFO = "is obtained by letting a pet Cat grow for about 2-3 hours.";
	private static final String OVERGROWN_HELLCAT_INFO = "is obtained by letting a pet Hellcat grow for about 2-3 hours.";
	private static final String PET_ROCK_INFO = "can be received from Askeladden after The Fremennik Trials.";
	private static final String PENANCE_PET_INFO = "is received from High-level gambles in Barbarian Assault, at a rate of 1/1000.";
	private static final String PHOENIX_INFO = "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000.";
	private static final String PRINCE_BLACK_DRAGON_INFO = "is dropped by the King Black Dragon, at a rate of 1/3000.";
	private static final String RIFT_GUARDIAN_INFO = "is obtained while training Runecraft.";
	private static final String ROCK_GOLEM_INFO = "is obtained while training Mining.";
	private static final String ROCKY_INFO = "is obtained while training Thieving.";
	private static final String SCORPIAS_OFFSPRING_INFO = "is dropped by Scorpia, at a rate of 1/2,015.75";
	private static final String SKOTOS_INFO = "is dropped by Skotizo, at a rate of 1/65.";
	private static final String SMOKE_DEVIL_INFO = "is dropped by the Thermonuclear smoke devil, at a rate of 1/3000.";
	private static final String SMOLCANO_INFO = "is dropped by Zalcano, at a rate of 1/2250.";
	private static final String SNAKELING_INFO = "is dropped by Zulrah, at a rate of 1/4000.";
	private static final String SRARACHA_INFO = "is dropped by Sarachnis, at a rate of 1/3000.";
	private static final String TANGLEROOT_INFO = "is obtained while training Farming.";
	private static final String TINY_TEMPOR_INFO = "is obtained from the Tempoross mini game, at a rate of 1/8000.";
	private static final String TOY_DOLL_INFO = "can be crafted in a POH with 18 Crafting and a Crafting table 3.";
	private static final String TOY_MOUSE_INFO = "can be crafted in a POH with 33 Crafting and a Crafting table 4.";
	private static final String TOY_SOLDIER_INFO = "can be crafted in a POH with 13 Crafting and a Crafting table 3.";
	private static final String TZREKJAD_INFO = "is dropped by TzTok-Jad, at a rate of 1/200 (or 1/100 if on a slayer task).";
	private static final String VENENATIS_SPIDERLING_INFO = "is dropped by Venenatis at a rate of 1/2000.";
	private static final String VETION_JR_INFO = "is dropped by Vet'ion, at a rate of 1/2000.";
	private static final String VORKI_INFO = "is dropped by Vorkath, at a rate of 1/3000.";
	private static final String WILY_CAT_INFO = "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest.";
	private static final String WILY_HELLCAT_INFO = "is obtained by asking Felkrash to train an Overgrown Hellcat, after completing the Ratcatchers quest.";
	private static final String YOUNGLLEF_INFO = "is obtained by completing The Gauntlet, at a rate of 1/2000 (or 1/800 for The Corrupted Gauntlet).";
	private static final String ZILYANA_JR_INFO = "is dropped by Commander Zilyana, at a rate of 1/5000.";

	/*
	 *	Variant text
	 */
	private static final String BABY_CHINCHOMPA_GOLD = " The gold variant is obtained by causing a Baby Chinchompa to metamorphosize, at a rate of 1/10000.";

	private static final String BABY_MOLE_RAT = " This is a variant of the Baby Mole, obtained by using a Mole Claw on the Baby Mole.";

	private static final String DARK_Giant_SQUIRREL = " This is a variant of the Giant Squirrel which is unlocked by using a Dark Acorn on a Giant Squirrel.";

	private static final String GREAT_BLUE_HERON_INFO = " This is a variant of the Heron, obtained by feeding the Heron 3,000 Spirit Flakes";

	private static final String ROCKY_RACCOON = " This is the Raccoon variant of Rocky, obtained by using Redberries on any variant of Rocky.";
	private static final String ROCKY_RED_PANDA = " This is the Red Panda variant of Rocky, obtained by using Redberries on any variant of Rocky.";
	private static final String ROCKY_TANUKI = " This is the Tanuki variant of Rocky, obtained by using Poison Ivy Berries on any variant of Rocky.";

	private static final String OLMLET_CM_VARIANTS = " This is a COX Challenge Mode variant of the Olmlet. Obtained by causing any variant of the Olmlet to metamorphosize, after having used metamorphic dust on the Olmlet.";

	private static Pet[] pets1 = {
			new Pet(PetGroup.BOSS, NpcID.ABYSSAL_ORPHAN, ABYSSAL_ORPHAN_INFO),
			new Pet(PetGroup.BOSS, NpcID.ABYSSAL_ORPHAN_5884, ABYSSAL_ORPHAN_INFO),

			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA, BABY_CHINCHOMPA_INFO),	// Red Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6719, BABY_CHINCHOMPA_INFO),	// Grey Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6720, BABY_CHINCHOMPA_INFO),	// Black Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6721, BABY_CHINCHOMPA_INFO + " " + BABY_CHINCHOMPA_GOLD),	// Gold Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6756, BABY_CHINCHOMPA_INFO),	// Red Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6757, BABY_CHINCHOMPA_INFO),	// Grey Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6758, BABY_CHINCHOMPA_INFO),	// Black Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6759, BABY_CHINCHOMPA_INFO + " " + BABY_CHINCHOMPA_GOLD),	// Gold Variant

			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE, BABY_MOLE_INFO),	// Pink/naked
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_5781, BABY_MOLE_INFO),	// Pink slightly smaller?
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_5782, BABY_MOLE_INFO),	// Pink even smaller
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_6635, BABY_MOLE_INFO),	// Small Brown	// Seen
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_6651, BABY_MOLE_INFO),	// Small Brown

			new Pet(PetGroup.BOSS, NpcID.BABY_MOLERAT, BABY_MOLE_INFO + BABY_MOLE_RAT),
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLERAT_10651, BABY_MOLE_INFO + BABY_MOLE_RAT),

			new Pet(PetGroup.SKILLING, NpcID.BEAVER, BEAVER_INFO),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_6724, BEAVER_INFO),

			new Pet(PetGroup.OTHER, NpcID.BLOODHOUND, BLOODHOUND_INFO),
			new Pet(PetGroup.OTHER, NpcID.BLOODHOUND_7232, BLOODHOUND_INFO),	// Seen in game, other players

			new Pet(PetGroup.BOSS, NpcID.CALLISTO_CUB, CALLISTO_CUB_INFO),
			new Pet(PetGroup.BOSS, NpcID.CALLISTO_CUB_5558, CALLISTO_CUB_INFO),

			new Pet(PetGroup.OTHER, NpcID.CAT, CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_1619, CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_1620, CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.CAT_1621, CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.CAT_1622, CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.CAT_1623, CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_1624, CAT_INFO),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_3831, CAT_INFO),	// Brown checkered, looks unkempt
			new Pet(PetGroup.OTHER, NpcID.CAT_3832, CAT_INFO),	// Black checkered, looks unkempt
			new Pet(PetGroup.OTHER, NpcID.CAT_6662, CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_6663, CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.CAT_6664, CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.CAT_6665, CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.CAT_6666, CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_6667, CAT_INFO),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_7380, CAT_INFO),	// Black with white feet

			new Pet(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_JR, CHAOS_ELEMENTAL_JR_INFO),	// Spotted in Game as other players
			new Pet(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_JR_5907, CHAOS_ELEMENTAL_JR_INFO),

			new Pet(PetGroup.OTHER, NpcID.CHOMPY_CHICK, CHOMPY_CHICK_INFO),
			new Pet(PetGroup.OTHER, NpcID.CHOMPY_CHICK_4002, CHOMPY_CHICK_INFO),	// Slightly larger?

			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT, CLOCKWORK_CAT_INFO),
			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT_541, CLOCKWORK_CAT_INFO),
			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT_2782, CLOCKWORK_CAT_INFO),
			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT_6661, CLOCKWORK_CAT_INFO),

			new Pet(PetGroup.BOSS, NpcID.CORPOREAL_CRITTER, CORPOREAL_CRITTER_INFO),
			new Pet(PetGroup.BOSS, NpcID.CORPOREAL_CRITTER_8010, CORPOREAL_CRITTER_INFO),

			new Pet(PetGroup.BOSS, NpcID.CORRUPTED_YOUNGLLEF, CORRUPTED_YOUNGLLEF_INFO),
			new Pet(PetGroup.BOSS, NpcID.CORRUPTED_YOUNGLLEF_8738, CORRUPTED_YOUNGLLEF_INFO),

			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_PRIME_JR, DAGANNOTH_PRIME_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_PRIME_JR_6629, DAGANNOTH_PRIME_JR_INFO),

			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_REX_JR, DAGANNOTH_REX_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_REX_JR_6641, DAGANNOTH_REX_JR_INFO),

			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_SUPREME_JR, DAGANNOTH_SUPREME_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_SUPREME_JR_6628, DAGANNOTH_SUPREME_JR_INFO),

			new Pet(PetGroup.BOSS, NpcID.DARK_CORE, DARK_CORE_INFO),    // Not sure this is the pet dark core, but it looks right
			new Pet(PetGroup.BOSS, NpcID.DARK_CORE_388, DARK_CORE_INFO),

			new Pet(PetGroup.BOSS, NpcID.DARK_SQUIRREL, GIANT_SQUIRREL_INFO + DARK_Giant_SQUIRREL),	// Thank you to Gamma91/Bram91 on gitHub for the info
			new Pet(PetGroup.BOSS, NpcID.DARK_SQUIRREL_9638, GIANT_SQUIRREL_INFO + DARK_Giant_SQUIRREL),

			new Pet(PetGroup.OTHER, NpcID.EEK, EEK_INFO),

			new Pet(PetGroup.OTHER, NpcID.ENRAGED_TEKTINY, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.OTHER, NpcID.ENRAGED_TEKTINY_9513, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.OTHER, NpcID.FISHBOWL, FISHBOWL_INFO),	// Blue
			new Pet(PetGroup.OTHER, NpcID.FISHBOWL_6659, FISHBOWL_INFO),	// Green
			new Pet(PetGroup.OTHER, NpcID.FISHBOWL_6660, FISHBOWL_INFO),	// Gold

			new Pet(PetGroup.OTHER, NpcID.FLYING_VESPINA, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.OTHER, NpcID.FLYING_VESPINA_9514, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.BOSS, NpcID.GENERAL_GRAARDOR_JR, GENERAL_GRAARDOR_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.GENERAL_GRAARDOR_JR_6644, GENERAL_GRAARDOR_JR_INFO),

			new Pet(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL, GIANT_SQUIRREL_INFO),
			new Pet(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL_7351, GIANT_SQUIRREL_INFO),	// Seen in game as other players
			new Pet(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL_9666, GIANT_SQUIRREL_INFO), // There's usually an even number, so I'm not sure what's up

			new Pet(PetGroup.SKILLING, NpcID.GREAT_BLUE_HERON, HERON_INFO + GREAT_BLUE_HERON_INFO),
			new Pet(PetGroup.SKILLING, NpcID.GREAT_BLUE_HERON_10636, HERON_INFO + GREAT_BLUE_HERON_INFO),

			new Pet(PetGroup.OTHER, NpcID.HELLCAT, HELLCAT_INFO),	// Spotted in Game as other players
			new Pet(PetGroup.OTHER, NpcID.HELLCAT_6668, HELLCAT_INFO),	// Spotted in Game as other players

			new Pet(PetGroup.OTHER, NpcID.HELLKITTEN, HELLKITTEN_INFO),

			new Pet(PetGroup.BOSS, NpcID.HELLPUPPY, HELLPUPPY_INFO),
			new Pet(PetGroup.BOSS, NpcID.HELLPUPPY_3099, HELLPUPPY_INFO),	// Seen in game as other players

			new Pet(PetGroup.SKILLING, NpcID.HERBI, HERBI_INFO),
			new Pet(PetGroup.SKILLING, NpcID.HERBI_7760, HERBI_INFO),	// Spotted in game as other players, slightly larger?

			new Pet(PetGroup.SKILLING, NpcID.HERON, HERON_INFO),	// Spotted in Game as other players
			new Pet(PetGroup.SKILLING, NpcID.HERON_6722, HERON_INFO),

			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA, IKKLE_HYDRA_INFO),	// Green Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8493, IKKLE_HYDRA_INFO),	// Blue Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8494, IKKLE_HYDRA_INFO),	// Red Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8495, IKKLE_HYDRA_INFO),	// Grey Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8517, IKKLE_HYDRA_INFO),	// Green
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8518, IKKLE_HYDRA_INFO),	// Blue
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8519, IKKLE_HYDRA_INFO),	// Red
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8520, IKKLE_HYDRA_INFO),	// Grey

			new Pet(PetGroup.BOSS, NpcID.JALNIBREK, JALNIBREK_INFO),
			new Pet(PetGroup.BOSS, NpcID.JALNIBREK_7675, JALNIBREK_INFO),

			new Pet(PetGroup.BOSS, NpcID.JALREKJAD, JALREKJAD_INFO),
			new Pet(PetGroup.BOSS, NpcID.JALREKJAD_10625, JALREKJAD_INFO),

			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS, KALPHITE_PRINCESS_INFO),	// Orange airborn
			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6638, KALPHITE_PRINCESS_INFO),	// Green grounded	// Seen in game as other players
			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6653, KALPHITE_PRINCESS_INFO),	// Green grounded
			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6654, KALPHITE_PRINCESS_INFO),	// Green grounded

			new Pet(PetGroup.OTHER, NpcID.KITTEN, KITTEN_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5591, KITTEN_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5592, KITTEN_INFO),	// White	// Seen in game as other players, white kitten
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5593, KITTEN_INFO),	// Checkered orange	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5594, KITTEN_INFO),	// Black	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5595, KITTEN_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5596, KITTEN_INFO),	// Grey and blue checkered	// Seen in game as other players

			new Pet(PetGroup.BOSS, NpcID.KRAKEN_6640, KRAKEN_INFO),
			new Pet(PetGroup.BOSS, NpcID.KRAKEN_6656, KRAKEN_INFO),

			new Pet(PetGroup.BOSS, NpcID.KREEARRA_JR, KREEARRA_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.KREEARRA_JR_6643, KREEARRA_JR_INFO),

			new Pet(PetGroup.BOSS, NpcID.KRIL_TSUTSAROTH_JR, KRIL_TSUTSAROTH_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.KRIL_TSUTSAROTH_JR_6647, KRIL_TSUTSAROTH_JR_INFO),

			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT, LAZY_CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1627, LAZY_CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1628, LAZY_CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1629, LAZY_CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1630, LAZY_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1631, LAZY_CAT_INFO),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6683, LAZY_CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6684, LAZY_CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6685, LAZY_CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6686, LAZY_CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6687, LAZY_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6688, LAZY_CAT_INFO),	// Grey and blue checkered

			new Pet(PetGroup.OTHER, NpcID.LAZY_HELLCAT, LAZY_HELLCAT_INFO),
			new Pet(PetGroup.OTHER, NpcID.LAZY_HELLCAT_6689, LAZY_HELLCAT_INFO),

			new Pet(PetGroup.OTHER, NpcID.LIL_CREATOR, LIL_CREATOR_INFO),
			new Pet(PetGroup.OTHER, NpcID.LIL_CREATOR_3566, LIL_CREATOR_INFO),

			new Pet(PetGroup.OTHER, NpcID.LIL_DESTRUCTOR, LIL_CREATOR_INFO),
			new Pet(PetGroup.OTHER, NpcID.LIL_DESTRUCTOR_5008, LIL_CREATOR_INFO),

			new Pet(PetGroup.BOSS, NpcID.LIL_BLOAT, LIL_ZIK_INFO),
			new Pet(PetGroup.BOSS, NpcID.LIL_BLOAT_10871, LIL_ZIK_INFO),

			new Pet(PetGroup.BOSS, NpcID.LIL_MAIDEN, LIL_ZIK_INFO),
			new Pet(PetGroup.BOSS, NpcID.LIL_MAIDEN_10870, LIL_ZIK_INFO),

			new Pet(PetGroup.BOSS, NpcID.LIL_NYLO, LIL_ZIK_INFO),
			new Pet(PetGroup.BOSS, NpcID.LIL_NYLO_10872, LIL_ZIK_INFO),

			new Pet(PetGroup.BOSS, NpcID.LIL_SOT, LIL_ZIK_INFO),
			new Pet(PetGroup.BOSS, NpcID.LIL_SOT_10873, LIL_ZIK_INFO),

			new Pet(PetGroup.BOSS, NpcID.LIL_XARP, LIL_ZIK_INFO),
			new Pet(PetGroup.BOSS, NpcID.LIL_XARP_10874, LIL_ZIK_INFO),

			new Pet(PetGroup.BOSS, NpcID.LIL_ZIK, LIL_ZIK_INFO),
			new Pet(PetGroup.BOSS, NpcID.LIL_ZIK_8337, LIL_ZIK_INFO),	// Seen in game other player

			new Pet(PetGroup.BOSS, NpcID.LITTLE_NIGHTMARE, LITTLE_NIGHTMARE_INFO),
			new Pet(PetGroup.BOSS, NpcID.LITTLE_NIGHTMARE_9399, LITTLE_NIGHTMARE_INFO),

			new Pet(PetGroup.OTHER, NpcID.MAZ, MAZ_INFO),	// Not a real pet, but close enough for me to want to add it

			new Pet(PetGroup.BOSS, NpcID.MIDNIGHT, MIDNIGHT_INFO),
			new Pet(PetGroup.BOSS, NpcID.MIDNIGHT_7893, MIDNIGHT_INFO),	// Seen in game other player, morfed to NOON_7892
		
			new Pet(PetGroup.BOSS, NpcID.NEXLING, NEXLING_INFO),	//TODO get updated droprate
			new Pet(PetGroup.BOSS, NpcID.NEXLING_11277, NEXLING_INFO),

			new Pet(PetGroup.BOSS, NpcID.NOON, NOON_INFO),
			new Pet(PetGroup.BOSS, NpcID.NOON_7892, NOON_INFO),	// Seen in game as other players, morfed to MIDNIGHT_7893

			new Pet(PetGroup.BOSS, NpcID.OLMLET, OLMLET_INFO),
			new Pet(PetGroup.BOSS, NpcID.OLMLET_7520, OLMLET_INFO),	// Seen in game other player

			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT, OVERGROWN_CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5599, OVERGROWN_CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5600, OVERGROWN_CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5601, OVERGROWN_CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5602, OVERGROWN_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5603, OVERGROWN_CAT_INFO),	// Grey and blue checkered	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6676, OVERGROWN_CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6677, OVERGROWN_CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6678, OVERGROWN_CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6679, OVERGROWN_CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6680, OVERGROWN_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6681, OVERGROWN_CAT_INFO),	// Grey and blue checkered

			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_HELLCAT, OVERGROWN_HELLCAT_INFO),	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_HELLCAT_6682, OVERGROWN_HELLCAT_INFO),

			new Pet(PetGroup.OTHER, NpcID.PET_ROCK, PET_ROCK_INFO),
			new Pet(PetGroup.OTHER, NpcID.PET_ROCK_6657, PET_ROCK_INFO),

			new Pet(PetGroup.OTHER, NpcID.PENANCE_PET, PENANCE_PET_INFO),
			new Pet(PetGroup.OTHER, NpcID.PENANCE_PET_6674, PENANCE_PET_INFO),

			new Pet(PetGroup.SKILLING, NpcID.PHOENIX, PHOENIX_INFO),	// Green
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3078, PHOENIX_INFO),	// Blue
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3079, PHOENIX_INFO),	// White
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3080, PHOENIX_INFO),	// Purple
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3081, PHOENIX_INFO),	// Green
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3082, PHOENIX_INFO),	// Blue
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3083, PHOENIX_INFO),	// White
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3084, PHOENIX_INFO),	// Purple
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_7368, PHOENIX_INFO),	// Orange
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_7370, PHOENIX_INFO),	// Orange

			new Pet(PetGroup.BOSS, NpcID.PRINCE_BLACK_DRAGON, PRINCE_BLACK_DRAGON_INFO),
			new Pet(PetGroup.BOSS, NpcID.PRINCE_BLACK_DRAGON_6652, PRINCE_BLACK_DRAGON_INFO),

			new Pet(PetGroup.BOSS, NpcID.PUPPADILE, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.BOSS, NpcID.PUPPADILE_8201, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.SKILLING, NpcID.RED, ROCKY_INFO + " " + ROCKY_RED_PANDA),
			new Pet(PetGroup.SKILLING, NpcID.RED_9852, ROCKY_INFO + " " + ROCKY_RED_PANDA),

			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN, RIFT_GUARDIAN_INFO + " This is the Fire variant."),	// Fire
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7338, RIFT_GUARDIAN_INFO + " This is the Air variant."),	// Air
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7339, RIFT_GUARDIAN_INFO + " This is the Mind variant."),	// Mind
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7340, RIFT_GUARDIAN_INFO + " This is the Water variant."),	// Water
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7341, RIFT_GUARDIAN_INFO + " This is the Earth variant."),	// Earth
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7342, RIFT_GUARDIAN_INFO + " This is the Body variant."),	// Body
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7343, RIFT_GUARDIAN_INFO + " This is the Cosmic variant."),	// Cosmic
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7344, RIFT_GUARDIAN_INFO + " This is the Chaos variant."),	// Chaos
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7345, RIFT_GUARDIAN_INFO + " This is the Nature variant."),	// Nature
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7346, RIFT_GUARDIAN_INFO + " This is the Law variant."),	// Law
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7347, RIFT_GUARDIAN_INFO + " This is the Death variant."),	// Death
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7348, RIFT_GUARDIAN_INFO + " This is the Soul variant."),	// Soul
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7349, RIFT_GUARDIAN_INFO + " This is the Astral variant."),	// Astral
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7350, RIFT_GUARDIAN_INFO + " This is the Blood variant."),	// Blood
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7354, RIFT_GUARDIAN_INFO + " This is the Fire variant."),	// fire
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7355, RIFT_GUARDIAN_INFO + " This is the Air variant."),	// Air
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7356, RIFT_GUARDIAN_INFO + " This is the Mind variant."),	// Mind
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7357, RIFT_GUARDIAN_INFO + " This is the Water variant."),	// Water
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7358, RIFT_GUARDIAN_INFO + " This is the Earth variant."),	// Earth
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7359, RIFT_GUARDIAN_INFO + " This is the Body variant."),	// Body
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7360, RIFT_GUARDIAN_INFO + " This is the Cosmic variant."),	// Cosmic
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7361, RIFT_GUARDIAN_INFO + " This is the Chaos variant."),	// Chaos
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7362, RIFT_GUARDIAN_INFO + " This is the Nature variant."),	// Nature
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7363, RIFT_GUARDIAN_INFO + " This is the Law variant."),	// Law
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7364, RIFT_GUARDIAN_INFO + " This is the Death variant."),	// Death
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7365, RIFT_GUARDIAN_INFO + " This is the Soul variant."),	// Soul
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7366, RIFT_GUARDIAN_INFO + " This is the Astral variant."),	// Astral
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7367, RIFT_GUARDIAN_INFO + " This is the Blood variant."),	// Blood
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_8024, RIFT_GUARDIAN_INFO + " This is the Wrath variant."),	// Wrath
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_8028, RIFT_GUARDIAN_INFO + " This is the Wrath variant."),	// Wrath

			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM, ROCK_GOLEM_INFO + " This is the Amethyst variant."),	// Amethyst
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7439, ROCK_GOLEM_INFO + " This is the Rock variant."),	// Rock
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7440, ROCK_GOLEM_INFO + " This is the Tin variant."),	// Tin
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7441, ROCK_GOLEM_INFO + " This is the Copper variant."),	// Copper
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7442, ROCK_GOLEM_INFO + " This is the Iron variant."),	// Iron
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7443, ROCK_GOLEM_INFO + " This is the Blurite variant."),	// Blurite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7444, ROCK_GOLEM_INFO + " This is the Silver variant."),	// Silver
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7445, ROCK_GOLEM_INFO + " This is the Coal variant."),	// Coal
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7446, ROCK_GOLEM_INFO + " This is the Gold variant."),	// Gold
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7447, ROCK_GOLEM_INFO + " This is the Mithril variant."),	// Mithril
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7448, ROCK_GOLEM_INFO + " This is the Granite variant."),	// Granite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7449, ROCK_GOLEM_INFO + " This is the Adamantite variant."),	// Adamantite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7450, ROCK_GOLEM_INFO + " This is the Runite variant."),	// Runite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7451, ROCK_GOLEM_INFO + " This is the Rock variant."),	// Rock
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7452, ROCK_GOLEM_INFO + " This is the Tin variant."),	// Tin
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7453, ROCK_GOLEM_INFO + " This is the Copper variant."),	// Copper
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7454, ROCK_GOLEM_INFO + " This is the Iron variant."),	// Iron
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7455, ROCK_GOLEM_INFO + " This is the Blurite variant."),	// Blurite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7642, ROCK_GOLEM_INFO + " This is the Silver variant."),	// Silver
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7643, ROCK_GOLEM_INFO + " This is the Coal variant."),	// Coal
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7644, ROCK_GOLEM_INFO + " This is the Gold variant."),	// Gold
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7645, ROCK_GOLEM_INFO + " This is the Mithril variant."),	// Mithril
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7646, ROCK_GOLEM_INFO + " This is the Granite variant."),	// Granite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7647, ROCK_GOLEM_INFO + " This is Adamantite fire variant."),	// Adamantite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7648, ROCK_GOLEM_INFO + " This is the Runite variant."),	// Runite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7711, ROCK_GOLEM_INFO + " This is the Amethyst variant."),	// Amethyst
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7736, ROCK_GOLEM_INFO + " This is the Lovakite variant."),	// Lovakite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7737, ROCK_GOLEM_INFO + " This is the Elemental variant."),	// Elemental
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7738, ROCK_GOLEM_INFO + " This is the Daeyalt variant."),	// Daeyalt
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7739, ROCK_GOLEM_INFO + " This is the Lovakite variant."),	// Lovakite variant, otherplayer
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7740, ROCK_GOLEM_INFO + " This is the Elemental variant."),	// Elemental variant, other player
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7741, ROCK_GOLEM_INFO + " This is the Daeyalt variant."),	// Daeyalt

			new Pet(PetGroup.SKILLING, NpcID.ROCKY, ROCKY_INFO + " " + ROCKY_RACCOON),
			new Pet(PetGroup.SKILLING, NpcID.ROCKY_7353, ROCKY_INFO + " " + ROCKY_RACCOON),

			new Pet(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING, SCORPIAS_OFFSPRING_INFO),		// Usually even, what's up with this?
			new Pet(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING_5561, SCORPIAS_OFFSPRING_INFO),
			new Pet(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING_6616, SCORPIAS_OFFSPRING_INFO),	// This one is much smaller

			new Pet(PetGroup.BOSS, NpcID.SKOTOS, SKOTOS_INFO),
			new Pet(PetGroup.BOSS, NpcID.SKOTOS_7671, SKOTOS_INFO),

			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_6639, SMOKE_DEVIL_INFO),	// Yellow
			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_6655, SMOKE_DEVIL_INFO),	// Yellow
			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_8482, SMOKE_DEVIL_INFO),	// Blue
			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_8483, SMOKE_DEVIL_INFO),	// Blue

			new Pet(PetGroup.BOSS, NpcID.SMOLCANO, SMOLCANO_INFO),
			new Pet(PetGroup.BOSS, NpcID.SMOLCANO_8739, SMOLCANO_INFO),

			// Not sure if there are more pet ids, or if the boss fight minions will appear
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2127, SNAKELING_INFO),	// Green
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2130, SNAKELING_INFO),	// Green variant, other player
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2131, SNAKELING_INFO),	// Red variant, other player
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2132, SNAKELING_INFO),	// Blue/Purple variant, other player

			new Pet(PetGroup.BOSS, NpcID.SRARACHA, SRARACHA_INFO),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_2144, SRARACHA_INFO),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11157, SRARACHA_INFO),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11158, SRARACHA_INFO),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11159, SRARACHA_INFO),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11160, SRARACHA_INFO),

			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT, TANGLEROOT_INFO),	// Acorn
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_7352, TANGLEROOT_INFO + " This is the Acorn variant."),	// Acorn
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9492, TANGLEROOT_INFO + " This is the Crystal variant."),	// Crystal
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9493, TANGLEROOT_INFO + " This is the Dragon Fruit variant."),	// Dragon
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9494, TANGLEROOT_INFO + " This is the Guam variant."),	// Guam
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9495, TANGLEROOT_INFO + " This is the White Lily variant."),	// White Lily
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9496, TANGLEROOT_INFO + " This is the Redwood variant."),	// Redwood
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9497, TANGLEROOT_INFO + " This is the Crystal variant."),	// Crystal
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9498, TANGLEROOT_INFO + " This is the Dragon Fruit variant."),	// Dragon Fruit Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9499, TANGLEROOT_INFO + " This is the Guam variant."),	// Guam Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9500, TANGLEROOT_INFO + " This is the White Lily variant."),	// White Lily Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9501, TANGLEROOT_INFO + " This is the Redwood variant."),	// Redwood Variant, other player

			new Pet(PetGroup.BOSS, NpcID.TEKTINY, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.BOSS, NpcID.TEKTINY_8202, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.OTHER, NpcID.TINY_TEMPOR, TINY_TEMPOR_INFO),
			new Pet(PetGroup.OTHER, NpcID.TINY_TEMPOR_10637, TINY_TEMPOR_INFO),

			new Pet(PetGroup.TOY, NpcID.TOY_DOLL, TOY_DOLL_INFO),
			new Pet(PetGroup.TOY, NpcID.TOY_DOLL_9253, TOY_DOLL_INFO),

			new Pet(PetGroup.TOY, NpcID.TOY_MOUSE, TOY_MOUSE_INFO),
			new Pet(PetGroup.TOY, NpcID.TOY_MOUSE_9255, TOY_MOUSE_INFO),

			new Pet(PetGroup.TOY, NpcID.TOY_SOLDIER, TOY_SOLDIER_INFO),
			new Pet(PetGroup.TOY, NpcID.TOY_SOLDIER_9251, TOY_SOLDIER_INFO),

			new Pet(PetGroup.BOSS, NpcID.TZREKJAD, TZREKJAD_INFO),
			new Pet(PetGroup.BOSS, NpcID.TZREKJAD_5893, TZREKJAD_INFO),

			new Pet(PetGroup.BOSS, NpcID.TZREKZUK, JALNIBREK_INFO),
			new Pet(PetGroup.BOSS, NpcID.TZREKZUK_8011, JALNIBREK_INFO),

			new Pet(PetGroup.BOSS, NpcID.VANGUARD_8198, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.BOSS, NpcID.VANGUARD_8203, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.BOSS, NpcID.VASA_MINIRIO, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.BOSS, NpcID.VASA_MINIRIO_8204, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING, VENENATIS_SPIDERLING_INFO),
			new Pet(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING_5557, VENENATIS_SPIDERLING_INFO),

			new Pet(PetGroup.BOSS, NpcID.VESPINA, OLMLET_INFO + OLMLET_CM_VARIANTS),
			new Pet(PetGroup.BOSS, NpcID.VESPINA_8205, OLMLET_INFO + OLMLET_CM_VARIANTS),

			new Pet(PetGroup.BOSS, NpcID.VETION_JR, VETION_JR_INFO),	// Purple
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_5537, VETION_JR_INFO),	// Orange
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_5559, VETION_JR_INFO),	// Purple
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_5560, VETION_JR_INFO),	// Orange

			new Pet(PetGroup.BOSS, NpcID.VORKI, VORKI_INFO),
			new Pet(PetGroup.BOSS, NpcID.VORKI_8029, VORKI_INFO),	// Seen in game, other player

			new Pet(PetGroup.OTHER, NpcID.WILY_CAT, WILY_CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5585, WILY_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5586, WILY_CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5587, WILY_CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5588, WILY_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5589, WILY_CAT_INFO),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6690, WILY_CAT_INFO),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6691, WILY_CAT_INFO),	// White
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6692, WILY_CAT_INFO),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6693, WILY_CAT_INFO),	// Black
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6694, WILY_CAT_INFO),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6695, WILY_CAT_INFO),	// Black and grey checkered

			new Pet(PetGroup.OTHER, NpcID.WILY_HELLCAT, WILY_HELLCAT_INFO),
			new Pet(PetGroup.OTHER, NpcID.WILY_HELLCAT_6696, WILY_HELLCAT_INFO),

			new Pet(PetGroup.BOSS, NpcID.YOUNGLLEF, YOUNGLLEF_INFO),
			new Pet(PetGroup.BOSS, NpcID.YOUNGLLEF_8737, YOUNGLLEF_INFO),

			new Pet(PetGroup.SKILLING, NpcID.ZIGGY, ROCKY_INFO + " " + ROCKY_TANUKI),
			new Pet(PetGroup.SKILLING, NpcID.ZIGGY_9853, ROCKY_INFO + " " + ROCKY_TANUKI),

			new Pet(PetGroup.BOSS, NpcID.ZILYANA_JR, ZILYANA_JR_INFO),
			new Pet(PetGroup.BOSS, NpcID.ZILYANA_JR_6646, ZILYANA_JR_INFO)

	};
}
