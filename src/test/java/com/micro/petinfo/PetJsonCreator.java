package com.micro.petinfo;

import com.google.common.collect.ImmutableMap;
import com.micro.petinfo.dataretrieval.Pet;
import com.micro.petinfo.dataretrieval.PetGroup;
import net.runelite.api.NpcID;
import net.runelite.client.RuneLite;
import net.runelite.http.api.RuneLiteAPI;

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
	private static final String ABYSSAL_ORPHAN_EXAMINE = "Born in the death throes of an Abyssal Sire.";
	private static final String ABYSSAL_PROTECTOR_INFO = "is obtained from the Guardians of the Rift minigame.";
	private static final String ABYSSAL_PROTECTOR_EXAMINE = "Your own personal abyssal horror.";
	private static final String BABY_CHINCHOMPA_INFO = "is obtained while catching chinchompas.";
	private static final String BABY_CHINCHOMPA_EXAMINE = "Fluffy and cute, keep away from fire!";
	private static final String BABY_MOLE_INFO = "is obtained by killing the Giant Mole, at a rate of 1/3000.";
	private static final String BABY_MOLE_EXAMINE = "Keep Molin', molin' molin' molin'!";
	private static final String BARON = "is obtained by killing Duke Sucellus, at a rate of 1/2500.";
	private static final String BARON_EXAMINE = "Better keep an eye on this one.";
	private static final String BEAVER_INFO = "is obtained while training Woodcutting.";
	private static final String BEAVER_EXAMINE = "Looks like it's gotten through a lot of wood.";
	private static final String BLOODHOUND_INFO = "is obtained by completing Master Clue Scrolls, at a rate of 1/1000.";
	private static final String BLOODHOUND_EXAMINE = "Tracking down clues with logic and a pipe.";
	private static final String BUTCH = "is obtained by killing Vardorvis, at a rate of 1/3000";
	private static final String BUTCH_EXAMINE = "A tiny headless executioner.";
	private static final String CALLISTO_CUB_INFO = "is dropped by Callisto, at a rate of 1/2000.";
	private static final String RETRO_VARIANT = " This is the retro variant.";
	private static final String CALLISTO_CUB_EXAMINE = "Bear-ly smaller than his father.";
	private static final String CAT_INFO = "is obtained by letting a kitten grow for about 2 hours.";
	private static final String CAT_EXAMINE = "A fully grown feline.";
	private static final String CHAOS_ELEMENTAL_JR_INFO = "is dropped by the Chaos Elemental, at a rate of 1/300; or the Chaos Fanatic, at 1/1000.";
	private static final String CHAOS_ELEMENTAL_JR_EXAMINE = "D'aw look at the liddle...";
	private static final String CHOMPY_CHICK_INFO = "is dropped by Chompy birds, at a rate of 1/500, after completing the elite Western Provinces Diary.";
	private static final String CHOMPY_CHICK_EXAMINE = "A small boisterous bird, a delicacy for ogres.";
	private static final String CLOCKWORK_CAT_INFO = "can be crafted in a POH with 84 Crafting and a Crafting table 4.";
	private static final String CLOCKWORK_CAT_EXAMINE = "Nice bit of crafting!";
	private static final String CORPOREAL_CRITTER_INFO = "is obtained by causing a pet Dark Core to metamorphosize.";
	private static final String CORPOREAL_CRITTER_EXAMINE = "A critter from the spirit realm.";
	private static final String CORRUPTED_YOUNGLLEF_INFO = "is obtained by causing a Youngllef to metamorphosize, this can only be done after completing the Corrupted Gauntlet.";
	private static final String CORRUPTED_YOUNGLLEF_EXAMINE = "Looks like a bit of a nightmare.";
	private static final String DAGANNOTH_PRIME_JR_INFO = "is dropped by Dagannoth Prime, at a rate of 1/5000.";
	private static final String DAGANNOTH_PRIME_JR_EXAMINE = "Has the same temper as it's father.";
	private static final String DAGANNOTH_REX_JR_INFO = "is dropped by Dagannoth Rex, at a rate of 1/5000.";
	private static final String DAGANNOTH_REX_JR_EXAMINE = "They do say if you like it you should put a ring on it.";
	private static final String DAGANNOTH_SUPREME_JR_INFO = "is dropped by Dagannoth Supreme, at a rate of 1/5000.";
	private static final String DAGANNOTH_SUPREME_JR_EXAMINE = "Wouldn't want that sleeping at the end of my bed.";
	private static final String DARK_CORE_INFO = "is dropped by the Corporeal Beast, at a rate of 1/5000.";
	private static final String DARK_CORE_EXAMINE = "Isn't so annoying when in pet form.";
	private static final String EEK_INFO = "was obtained during the 2018 Halloween event.";
	private static final String EEK_EXAMINE = "She's a keen spinner of webs.";
	private static final String FISHBOWL_INFO = "can be caught in Harry's Fishing Shop";
	private static final String FISHBOWL_BLUEFISH_EXAMINE = "A fishbowl with a Tiny Bluefish in it.";
	private static final String FISHBOWL_GREENFISH_EXAMINE = "A fishbowl with a Tiny Greenfish in it.";
	private static final String FISHBOWL_SPINEFISH_EXAMINE = "A fishbowl with a Tiny Spinefish in it.";
	private static final String GENERAL_GRAARDOR_JR_INFO = "is dropped by General Graardor, at a rate of 1/5000";
	private static final String GENERAL_GRAARDOR_JR_EXAMINE = "Totally unintelligible.";
	private static final String GIANT_SQUIRREL_INFO = "is obtained by training Agility.";
	private static final String GIANT_SQUIRREL_EXAMINE = "A giant squirrel with beautiful markings.";
	private static final String HELLCAT_INFO = "is obtained by having a pet Cat hunt Hell-Rats.";
	private static final String HELLCAT_EXAMINE = "A hellish pet cat!";
	private static final String HELLKITTEN_INFO = "is obtained by having a pet Kitten hunt Hell-Rats.";
	private static final String HELLKITTEN_EXAMINE = "A hellish little pet.";
	private static final String HELLPUPPY_INFO = "is dropped by Cerberus, at a rate of 1/3000.";
	private static final String HELLPUPPY_EXAMINE = "A fiery little pup!";
	private static final String HERBI_INFO = "is obtained by harvesting Herbiboars, at a rate of 1/6500.";
	private static final String HERBI_EXAMINE = "A boar with an impressive mane of dried herbs.";
	private static final String HERON_INFO = "is obtained while training Fishing.";
	private static final String HERON_EXAMINE = "A long-legged bird that likes to fish.";
	private static final String IKKLE_HYDRA_INFO = "is dropped by Alchemical Hydra, at a rate of 1/3000.";
	private static final String IKKLE_HYDRA_EXAMINE = "How does it not fall over?";
	private static final String JALNIBREK_INFO = "is obtained by completing the inferno, at a rate of 1/100 (or 1/75 while on a TzKal-Zuk task).";
	private static final String JALNIBREK_EXAMINE = "It loves to nibble.";
	private static final String JALREKJAD_INFO = "is obtained by causing a Tzrek-jad to metamorphosize, this can only be done after completing all six of TzHaar-Ket-Rak's Challenges (this requires having also completed the Inferno).";
	private static final String JALREKJAD_EXAMINE = "Small, troublesome, cute.";
	private static final String KALPHITE_PRINCESS_INFO = "is dropped by Kalphite Queen, at a rate of 1/3000.";
	private static final String KALPHITE_PRINCESS_EXAMINE = "Suggests there's a king nearby.";
	private static final String KITTEN_INFO = "can be bought from Gertrude for 100gp, after completing the Gertrude's Cat quest.";
	private static final String KITTEN_EXAMINE = "A friendly little pet.";
	private static final String KRAKEN_INFO ="is dropped by Kraken, at a rate of 1/3000.";
	private static final String KRAKEN_EXAMINE = "How.... is it walking?";
	private static final String KREEARRA_JR_INFO = "is dropped by Kree'arra, at a rate of 1/5000.";
	private static final String KREEARRA_JR_EXAMINE = "Is it a bird, is it... no it's a bird.";
	private static final String KRIL_TSUTSAROTH_JR_INFO = "is dropped by K'ril Tsutsaroth, at a rate of 1/5000.";
	private static final String KRIL_TSUTSAROTH_JR_EXAMINE = "Where did he even come from?";
	private static final String LAZY_CAT_INFO = "is obtained by letting a Wily Cat grow for about an hour.";
	private static final String LAZY_CAT_EXAMINE = "A friendly not so little pet.";
	private static final String LAZY_HELLCAT_INFO = "is obtained by letting a Wily Hellcat at grow for about an hour.";
	private static final String LAZY_HELLCAT_EXAMINE = "A hellish little pet.";
	private static final String LIL_CREATOR_INFO = "is obtained by opening the Spoils of War, a reward from the Soul Wars minigame, at a rate of 1/400.";
	private static final String LIL_CREATOR_EXAMINE = "A pint-sized bringer of existence.";
	private static final String LIL_DESTRUCTOR_EXAMINE = "Harbinger of light surface scratches and minor dents.";
	private static final String LIL_VIATHAN = "is obtained by killing The Leviathan, at a rate of 1/2500.";
	private static final String LIL_VIATHAN_EXAMINE = "A small creature deformed by the Abyss.";
	private static final String LIL_ZIK_INFO = "is obtained by completing the Theater of Blood, at a rate of 1/650 (with optimal performance).";
	private static final String LIL_ZIK_EXAMINE = "What has eight legs and runs a Vampyric Theatre?";
	private static final String LIL_MAIDEN_EXAMINE = "Freed from her torturous constraints.";
	private static final String LIL_BLOAT_EXAMINE = "Smaller size, same smell.";
	private static final String LIL_NYLO_EXAMINE = "Eight legs of unparalleled loyalty.";
	private static final String LIL_SOT_EXAMINE = "Has a lot of anger for such a small monster.";
	private static final String LIL_XARP_EXAMINE = "The prince of Yarasa.";
	private static final String LITTLE_NIGHTMARE_INFO = "is dropped by The Nightmare, at varying rates from 1/4000 to 1/800 (based on team size), or from the Phosani's Nightmare at a rate of 1/4000.";
	private static final String LITTLE_NIGHTMARE_EXAMINE = "Quite the little nightmare.";
	private static final String LITTLE_PARASITE_EXAMINE = "So cute!";
	private static final String MAZ_INFO = "isn't a true pet, but you can get in her good graces by giving her an acorn.";
	private static final String MAZ_EXAMINE = "Looking after trees as if they were home.";
	private static final String MIDNIGHT_INFO = "is a metamorphosized variant of Noon, which is dropped by the Grotesque Guardians, at a rate of 1/3000.";
	private static final String MIDNIGHT_EXAMINE = "The mini Guardian of Dusk!";
	private static final String MUPHIN_INFO = "is dropped by Phantom Muspah, at a rate of 1/2500.";
	private static final String MUPHIN_EXAMINE = "An oversized grub with arms.";
	private static final String NEXLING_INFO = "is dropped by Nex at a rate of 1/500.";
	private static final String NEXLING_EXAMINE = "The gods don't quite fear this one.";
	private static final String NOON_INFO = "is dropped by the Grotesque Guardians, at a rate of 1/3000.";
	private static final String NOON_EXAMINE = "The mini Guardian of Dawn!";
	private static final String OLMLET_INFO = "is dropped by the Great Olm, at a rate of 1/53 per received broadcasted unique item.";
	private static final String OLMLET_EXAMINE = "The most cuddly Spawn of the Guardian in the Deep.";
	private static final String PUPPADILE_EXAMINE = "A puppy of a mutated guardian of Xeric.";
	private static final String TEKTINY_EXAMINE = "Xeric's former artisan's former helper.";
	private static final String VANGUARD_EXAMINE = "A very small member of Xeric's elite tactical unit.";
	private static final String VASA_MINIRIO_EXAMINE = "The son of a former High Priest, fused with the rock and bound to the dark crystals.";
	private static final String VESPINA_EXAMINE = "Princess of the Abyssal Vespine.";
	private static final String OVERGROWN_CAT_INFO = "is obtained by letting a pet Cat grow for about 2-3 hours.";
	private static final String OVERGROWN_CAT_EXAMINE = "A friendly not-so little pet.";
	private static final String OVERGROWN_HELLCAT_INFO = "is obtained by letting a pet Hellcat grow for about 2-3 hours.";
	private static final String OVERGROWN_HELLCAT_EXAMINE = "A hellish little pet.";
	private static final String PET_ROCK_INFO = "can be received from Askeladden after The Fremennik Trials.";
	private static final String PET_ROCK_EXAMINE = "The lowest maintenance pet you will ever have.";
	private static final String PENANCE_PET_INFO = "is received from High-level gambles in Barbarian Assault, at a rate of 1/1000.";
	private static final String PENANCE_PET_EXAMINE = "Run away! Run aw... wait, it's tiny...";
	private static final String PHOENIX_INFO = "is obtained by opening Supply crates from the Wintertodt, at a rate of 1/5000.";
	private static final String PHOENIX_EXAMINE = "The essence of fire.";
	private static final String PRINCE_BLACK_DRAGON_INFO = "is dropped by the King Black Dragon, at a rate of 1/3000.";
	private static final String PRINCE_BLACK_DRAGON_EXAMINE = "Not quite the full royalty yet.";
	private static final String QUETZIN_INFO = "is obtained by opening hunters' loot sacks obtained from completing Hunters' Rumours in the Hunter Guild.";
	private static final String QUETZIN_EXAMINE = "Is this bird tailing me?";
	private static final String RIFT_GUARDIAN_INFO = "is obtained while training Runecraft.";
	private static final String RIFT_GUARDIAN_EXAMINE = "An abyssal rift guardian.";
	private static final String GREATISH_GUARDIAN_EXAMINE = "Not quite as great as the Great Guardian, but pretty close.";
	private static final String ROCK_GOLEM_INFO = "is obtained while training Mining.";
	private static final String ROCK_GOLEM_EXAMINE = "Found somewhere between a rock and a hard place.";
	private static final String ROCKY_INFO = "is obtained while training Thieving.";
	private static final String ROCKY_EXAMINE = "Raccoons, like pandas but worse.";
	private static final String ZIGGY_EXAMINE = "No relation to a regular raccoon.";
	private static final String RED_EXAMINE = "Red panda, like pandas but red.";
	private static final String SCORPIAS_OFFSPRING_INFO = "is dropped by Scorpia, at a rate of 1/2,015.75";
	private static final String SCORPIAS_OFFSPRING_EXAMINE = "A scuttling little scorpion with an incredibly vicious tail.";
	private static final String SCURRY_INFO = "is dropped by Scurrius, for the MVP of the kill, at a rate of 1/3,000.";
	private static final String SCURRY_EXAMINE = "I wonder if it sits.";
	private static final String SKOTOS_INFO = "is dropped by Skotizo, at a rate of 1/65.";
	private static final String SKOTOS_EXAMINE = "Spawn of Darkness.";
	private static final String SMOKE_DEVIL_INFO = "is dropped by the Thermonuclear smoke devil, at a rate of 1/3000.";
	private static final String SMOKE_DEVIL_EXAMINE = "*cough*";
	private static final String SMOL_HEREDIT_INFO = "is dropped by Sol Heredit (in the  Fortis Colosseum) at a rate of 1/200, or by giving Diana's quivers to Minimus, also at a rate of 1/200.";
	private static final String SMOL_HEREDIT_EXAMINE = "Still somewhat imposing, I suppose.";
	private static final String SMOLCANO_INFO = "is dropped by Zalcano, at a rate of 1/2250.";
	private static final String SMOLCANO_EXAMINE = "Not so threatening at this size.";
	private static final String SNAKELING_INFO = "is dropped by Zulrah, at a rate of 1/4000.";
	private static final String SNAKELING_EXAMINE = "Spawn of Zulrah.";
	private static final String SRARACHA_INFO = "is dropped by Sarachnis, at a rate of 1/3000.";
	private static final String SRARACHA_EXAMINE = "Good thing there's no such thing as Sarachnophobia.";
	private static final String TANGLEROOT_INFO = "is obtained while training Farming.";
	private static final String TANGLEROOT_EXAMINE = "Don't be hasty.";
	private static final String TINY_TEMPOR_INFO = "is obtained from the Tempoross minigame, at a rate of 1/8000.";
	private static final String TINY_TEMPOR_EXAMINE = "So smol, so anger.";
	private static final String TOY_DOLL_INFO = "can be crafted in a POH with 18 Crafting and a Crafting table 3.";
	private static final String TOY_DOLL_EXAMINE = "Nice bit of crafting!";
	private static final String TOY_MOUSE_INFO = "can be crafted in a POH with 33 Crafting and a Crafting table 4.";
	private static final String TOY_MOUSE_EXAMINE = "Nice bit of crafting!";
	private static final String TOY_SOLDIER_INFO = "can be crafted in a POH with 13 Crafting and a Crafting table 3.";
	private static final String TOY_SOLDIER_EXAMINE = "Nice bit of crafting!";
	private static final String TUMEKENS_GUARDIAN_INFO = "is obtained as a reward from completing the Tombs of Amascut.";
	private static final String TUMEKENS_GUARDIAN_EXAMINE = "A tiny automaton imbued with a trace of Tumeken's power.";
	private static final String ELIDINIS_GUARDIAN_EXAMINE = "A tiny automaton imbued with a trace of Elidinis' power.";
	private static final String AKKHITO_EXAMINE = "Small, but still strong.";
	private static final String BABI_EXAMINE = "Her mother's daughter. Fond of bananas.";
	private static final String KEPHRITI_EXAMINE = "The tiniest bug can still make worlds fall over.";
	private static final String ZEBO_EXAMINE = "Chomp.";
	private static final String TZREKJAD_INFO = "is dropped by TzTok-Jad, at a rate of 1/200 (or 1/100 if on a slayer task).";
	private static final String TZREKJAD_EXAMINE = "This is not going to hurt... but it might tickle.";
	private static final String VENENATIS_SPIDERLING_INFO = "is dropped by Venenatis at a rate of 1/2000.";
	private static final String VENENATIS_SPIDERLING_EXAMINE = "Vacuum proof.";
	private static final String VETION_JR_INFO = "is dropped by Vet'ion, at a rate of 1/2000.";
	private static final String VETION_JR_EXAMINE = "Somehow much smoother in smaller form.";
	private static final String VORKI_INFO = "is dropped by Vorkath, at a rate of 1/3000.";
	private static final String VORKI_EXAMINE = "Three legs on my dragon...";
	private static final String WILY_CAT_INFO = "is obtained by asking Felkrash to train an Overgrown Cat, after completing the Ratcatchers quest.";
	private static final String WILY_CAT_EXAMINE = "Wild.";
	private static final String WILY_HELLCAT_INFO = "is obtained by asking Felkrash to train an Overgrown Hellcat, after completing the Ratcatchers quest.";
	private static final String WILY_HELLCAT_EXAMINE = "Wild and hellish.";
	private static final String WISP = "is obtained by killing The Whisperer, at a rate of 1/2000";
	private static final String WISP_EXAMINE = "Born in the shadows.";
	private static final String YOUNGLLEF_INFO = "is obtained by completing The Gauntlet, at a rate of 1/2000 (or 1/800 for The Corrupted Gauntlet).";
	private static final String YOUNGLLEF_EXAMINE = "Looks like a bit of a nightmare.";
	private static final String ZILYANA_JR_INFO = "is dropped by Commander Zilyana, at a rate of 1/5000.";
	private static final String ZILYANA_JR_EXAMINE = "Somehow a junior even though she's named after her spawn mother!";
	/*
	 *	Variant text
	 */
	private static final String BABY_CHINCHOMPA_GOLD = " The gold variant is obtained by causing a Baby Chinchompa to metamorphosize, at a rate of 1/10000.";

	private static final String BABY_MOLE_RAT = " This is a variant of the Baby Mole, obtained by using a Mole Claw on the Baby Mole.";

	private static final String DARK_GIANT_SQUIRREL = " This is a variant of the Giant Squirrel which is unlocked by using a Dark Acorn on a Giant Squirrel.";

	private static final String GREAT_BLUE_HERON_INFO = " This is a variant of the Heron, obtained by feeding the Heron 3,000 Spirit Flakes.";
	private static final String GREATISH_GUARDIAN = " This is a variant of the Rift Guardian, that can be obtained by using the Guardian's Eye reward from the Guardians of the Rift minigame on a Rift Guardian.";

	private static final String LITTLE_PARASITE_INFO = " This is a variant of the the Little Nightmare, obtained by using a Parasitic Egg on her.";

	private static final String MUPHIN_MELEE = " This is the melee variant, obtained by using Charged Ice on Muphin.";
	private static final String MUPHIN_RANGED = " This is the ranged variant, obtained by using Charged Ice on Muphin.";
	private static final String MUPHIN_SHIELDED = " This is the shielded variant, obtained by using Charged Ice on Muphin.";

	private static final String ROCKY_RED_PANDA = " This is the Red Panda variant of Rocky, obtained by using Redberries on any variant of Rocky.";
	private static final String ROCKY_TANUKI = " This is the Tanuki variant of Rocky, obtained by using Poison Ivy Berries on any variant of Rocky.";

	private static final String TUMEKENS_DAMAGED_GUARDIAN = " This is a variant of the Tumeken's guardian unlocked with the Ancient remnant";
	private static final String ELIDINIS_GUARDIAN = " This is a variant of the Tumeken's guardian.";
	private static final String ELIDINIS_DAMAGED_GUARDIAN = " This is a variant of the Tumeken's guardian unlocked with the Ancient remnant";
	private static final String AKKHITO = " This is a variant of the Tumeken's guardian unlocked with the Remnant of Akkha.";
	private static final String BABI = " This is a variant of the Tumeken's guardian unlocked with the Remnant of Ba-Ba.";

	private static final String SRARACHA_ORANGE = " This is the Orange variant unlocked using an orange egg sack.";
	private static final String SRARACHA_BLUE = " This is the Blue variant unlocked using a blue egg sack.";

	private static final String BEAVER_LOGS = " This is the Logs variant.";
	private static final String BEAVER_OAK = " This is the Oak variant.";
	private static final String BEAVER_WILLOW = " This is the Willow variant.";
	private static final String BEAVER_TEAK = " This is the Teak variant.";
	private static final String BEAVER_MAPLE = " This is the Maple variant.";
	private static final String BEAVER_MAHOGANY = " This is the Mahogany variant.";
	private static final String BEAVER_ARCTIC_PINE = " This is the Arctic Pine variant.";
	private static final String BEAVER_YEW = " This is the Yew variant.";
	private static final String BEAVER_MAGIC = " This is the Magic variant.";
	private static final String BEAVER_REDWOOD = " This is the Redwood variant.";

	private static final String KEPHRITI = " This is a variant of the Tumeken's guardian unlocked with the Remnant of Kephri.";
	private static final String ZEBO = " This is a variant of the Tumeken's guardian unlocked with the Remnant of Zebak.";


	private static final String OLMLET_CM_VARIANTS = " This is a COX Challenge Mode variant of the Olmlet. Obtained by causing any variant of the Olmlet to metamorphosize, after having used metamorphic dust on the Olmlet.";

	private static Pet[] pets1 = {
			new Pet(PetGroup.BOSS, NpcID.ABYSSAL_ORPHAN, ABYSSAL_ORPHAN_INFO, ABYSSAL_ORPHAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ABYSSAL_ORPHAN_5884, ABYSSAL_ORPHAN_INFO, ABYSSAL_ORPHAN_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.ABYSSAL_PROTECTOR, ABYSSAL_PROTECTOR_INFO, ABYSSAL_PROTECTOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.ABYSSAL_PROTECTOR_11429, ABYSSAL_PROTECTOR_INFO, ABYSSAL_PROTECTOR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.AKKHITO, TUMEKENS_GUARDIAN_INFO + AKKHITO, AKKHITO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.AKKHITO_11846, TUMEKENS_GUARDIAN_INFO + AKKHITO, AKKHITO_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.BABI, TUMEKENS_GUARDIAN_INFO + BABI, BABI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.BABI_11847, TUMEKENS_GUARDIAN_INFO + BABI, BABI_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Red Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6719, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Grey Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6720, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Black Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6721, BABY_CHINCHOMPA_INFO + " " + BABY_CHINCHOMPA_GOLD, BABY_CHINCHOMPA_EXAMINE),	// Gold Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6756, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Red Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6757, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Grey Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6758, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Black Variant
			new Pet(PetGroup.SKILLING, NpcID.BABY_CHINCHOMPA_6759, BABY_CHINCHOMPA_INFO + " " + BABY_CHINCHOMPA_GOLD, BABY_CHINCHOMPA_EXAMINE),	// Gold Variant

			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),	// Pink/naked
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_5781, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),	// Pink slightly smaller?
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_5782, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),	// Pink even smaller
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_6635, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),	// Small Brown	// Seen
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLE_6651, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),	// Small Brown

			new Pet(PetGroup.BOSS, NpcID.BABY_MOLERAT, BABY_MOLE_INFO + BABY_MOLE_RAT, BABY_MOLE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.BABY_MOLERAT_10651, BABY_MOLE_INFO + BABY_MOLE_RAT, BABY_MOLE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.BARON, BARON, BARON_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.BARON_12159, BARON, BARON_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.BEAVER, BEAVER_INFO + BEAVER_LOGS, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12181, BEAVER_INFO + BEAVER_LOGS, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12170, BEAVER_INFO + BEAVER_OAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12182, BEAVER_INFO + BEAVER_OAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12171, BEAVER_INFO + BEAVER_WILLOW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12183, BEAVER_INFO + BEAVER_WILLOW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12176, BEAVER_INFO + BEAVER_TEAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12188, BEAVER_INFO + BEAVER_TEAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12172, BEAVER_INFO + BEAVER_MAPLE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12184, BEAVER_INFO + BEAVER_MAPLE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12177, BEAVER_INFO + BEAVER_MAHOGANY, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12189, BEAVER_INFO + BEAVER_MAHOGANY, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12178, BEAVER_INFO + BEAVER_ARCTIC_PINE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12190, BEAVER_INFO + BEAVER_ARCTIC_PINE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12173, BEAVER_INFO + BEAVER_YEW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12185, BEAVER_INFO + BEAVER_YEW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12174, BEAVER_INFO + BEAVER_MAGIC, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12186, BEAVER_INFO + BEAVER_MAGIC, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12175, BEAVER_INFO + BEAVER_REDWOOD, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.BEAVER_12187, BEAVER_INFO + BEAVER_REDWOOD, BEAVER_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.BLOODHOUND, BLOODHOUND_INFO, BLOODHOUND_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.BLOODHOUND_7232, BLOODHOUND_INFO, BLOODHOUND_EXAMINE),	// Seen in game, other players

			new Pet(PetGroup.BOSS, NpcID.BUTCH, BUTCH, BUTCH_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.BUTCH_12158, BUTCH, BUTCH_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.CALLISTO_CUB, CALLISTO_CUB_INFO, CALLISTO_CUB_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CALLISTO_CUB_5558, CALLISTO_CUB_INFO, CALLISTO_CUB_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CALLISTO_CUB_11982, CALLISTO_CUB_INFO + RETRO_VARIANT, CALLISTO_CUB_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CALLISTO_CUB_11986, CALLISTO_CUB_INFO + RETRO_VARIANT, CALLISTO_CUB_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.CAT, CAT_INFO, CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_1619, CAT_INFO, CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_1620, CAT_INFO, CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.CAT_1621, CAT_INFO, CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.CAT_1622, CAT_INFO, CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.CAT_1623, CAT_INFO, CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_1624, CAT_INFO, CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_3831, CAT_INFO, CAT_EXAMINE),	// Brown checkered, looks unkempt
			new Pet(PetGroup.OTHER, NpcID.CAT_3832, CAT_INFO, CAT_EXAMINE),	// Black checkered, looks unkempt
			new Pet(PetGroup.OTHER, NpcID.CAT_6662, CAT_INFO, CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_6663, CAT_INFO, CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.CAT_6664, CAT_INFO, CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.CAT_6665, CAT_INFO, CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.CAT_6666, CAT_INFO, CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_6667, CAT_INFO, CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.CAT_7380, CAT_INFO, CAT_EXAMINE),	// Black with white feet

			new Pet(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_JR, CHAOS_ELEMENTAL_JR_INFO, CHAOS_ELEMENTAL_JR_EXAMINE),	// Spotted in Game as other players
			new Pet(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_JR_5907, CHAOS_ELEMENTAL_JR_INFO, CHAOS_ELEMENTAL_JR_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.CHOMPY_CHICK, CHOMPY_CHICK_INFO, CHOMPY_CHICK_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.CHOMPY_CHICK_4002, CHOMPY_CHICK_INFO, CHOMPY_CHICK_EXAMINE),	// Slightly larger?

			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT_541, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT_2782, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.CLOCKWORK_CAT_6661, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.CORPOREAL_CRITTER, CORPOREAL_CRITTER_INFO, CORPOREAL_CRITTER_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CORPOREAL_CRITTER_8010, CORPOREAL_CRITTER_INFO, CORPOREAL_CRITTER_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.CORRUPTED_YOUNGLLEF, CORRUPTED_YOUNGLLEF_INFO, CORRUPTED_YOUNGLLEF_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CORRUPTED_YOUNGLLEF_8738, CORRUPTED_YOUNGLLEF_INFO, CORRUPTED_YOUNGLLEF_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_PRIME_JR, DAGANNOTH_PRIME_JR_INFO, DAGANNOTH_PRIME_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_PRIME_JR_6629, DAGANNOTH_PRIME_JR_INFO, DAGANNOTH_PRIME_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_REX_JR, DAGANNOTH_REX_JR_INFO, DAGANNOTH_REX_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_REX_JR_6641, DAGANNOTH_REX_JR_INFO, DAGANNOTH_REX_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_SUPREME_JR, DAGANNOTH_SUPREME_JR_INFO, DAGANNOTH_SUPREME_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DAGANNOTH_SUPREME_JR_6628, DAGANNOTH_SUPREME_JR_INFO, DAGANNOTH_SUPREME_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.DARK_CORE, DARK_CORE_INFO, DARK_CORE_EXAMINE),    // Not sure this is the pet dark core, but it looks right
			new Pet(PetGroup.BOSS, NpcID.DARK_CORE_388, DARK_CORE_INFO, DARK_CORE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.DARK_SQUIRREL, GIANT_SQUIRREL_INFO + DARK_GIANT_SQUIRREL, GIANT_SQUIRREL_EXAMINE),	// Thank you to Gamma91/Bram91 on gitHub for the info
			new Pet(PetGroup.BOSS, NpcID.DARK_SQUIRREL_9638, GIANT_SQUIRREL_INFO + DARK_GIANT_SQUIRREL, GIANT_SQUIRREL_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.EEK, EEK_INFO, EEK_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ELIDINIS_DAMAGED_GUARDIAN, TUMEKENS_GUARDIAN_INFO + ELIDINIS_DAMAGED_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ELIDINIS_DAMAGED_GUARDIAN_11851, TUMEKENS_GUARDIAN_INFO + ELIDINIS_DAMAGED_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ELIDINIS_GUARDIAN, TUMEKENS_GUARDIAN_INFO + ELIDINIS_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ELIDINIS_GUARDIAN_11813, TUMEKENS_GUARDIAN_INFO + ELIDINIS_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ENRAGED_TEKTINY, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ENRAGED_TEKTINY_9513, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.FISHBOWL, FISHBOWL_INFO, FISHBOWL_BLUEFISH_EXAMINE),	// Blue
			new Pet(PetGroup.OTHER, NpcID.FISHBOWL_6659, FISHBOWL_INFO, FISHBOWL_GREENFISH_EXAMINE),	// Green
			new Pet(PetGroup.OTHER, NpcID.FISHBOWL_6660, FISHBOWL_INFO, FISHBOWL_SPINEFISH_EXAMINE),	// Gold

			new Pet(PetGroup.BOSS, NpcID.FLYING_VESPINA, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.FLYING_VESPINA_9514, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.GENERAL_GRAARDOR_JR, GENERAL_GRAARDOR_JR_INFO, GENERAL_GRAARDOR_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.GENERAL_GRAARDOR_JR_6644, GENERAL_GRAARDOR_JR_INFO, GENERAL_GRAARDOR_JR_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL, GIANT_SQUIRREL_INFO, GIANT_SQUIRREL_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL_7351, GIANT_SQUIRREL_INFO, GIANT_SQUIRREL_EXAMINE),	// Seen in game as other players
			new Pet(PetGroup.SKILLING, NpcID.GIANT_SQUIRREL_9666, GIANT_SQUIRREL_INFO, GIANT_SQUIRREL_EXAMINE), // There's usually an even number, so I'm not sure what's up

			new Pet(PetGroup.SKILLING, NpcID.GREAT_BLUE_HERON, HERON_INFO + GREAT_BLUE_HERON_INFO, HERON_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.GREAT_BLUE_HERON_10636, HERON_INFO + GREAT_BLUE_HERON_INFO, HERON_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.GREATISH_GUARDIAN, RIFT_GUARDIAN_INFO + GREATISH_GUARDIAN, GREATISH_GUARDIAN_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.GREATISH_GUARDIAN_11428, RIFT_GUARDIAN_INFO + GREATISH_GUARDIAN, GREATISH_GUARDIAN_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.HELLCAT, HELLCAT_INFO, HELLCAT_EXAMINE),	// Spotted in Game as other players
			new Pet(PetGroup.OTHER, NpcID.HELLCAT_6668, HELLCAT_INFO, HELLCAT_EXAMINE),	// Spotted in Game as other players

			new Pet(PetGroup.OTHER, NpcID.HELLKITTEN, HELLKITTEN_INFO, HELLKITTEN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.HELLPUPPY, HELLPUPPY_INFO, HELLPUPPY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.HELLPUPPY_3099, HELLPUPPY_INFO, HELLPUPPY_EXAMINE),	// Seen in game as other players

			new Pet(PetGroup.SKILLING, NpcID.HERBI, HERBI_INFO, HERBI_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.HERBI_7760, HERBI_INFO, HERBI_EXAMINE),	// Spotted in game as other players, slightly larger?

			new Pet(PetGroup.SKILLING, NpcID.HERON, HERON_INFO, HERON_EXAMINE),	// Spotted in Game as other players
			new Pet(PetGroup.SKILLING, NpcID.HERON_6722, HERON_INFO, HERON_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Green Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8493, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Blue Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8494, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Red Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8495, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Grey Variant, other player
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8517, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Green
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8518, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Blue
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8519, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Red
			new Pet(PetGroup.BOSS, NpcID.IKKLE_HYDRA_8520, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Grey

			new Pet(PetGroup.BOSS, NpcID.JALNIBREK, JALNIBREK_INFO, JALNIBREK_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.JALNIBREK_7675, JALNIBREK_INFO, JALNIBREK_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.JALREKJAD, JALREKJAD_INFO, JALREKJAD_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.JALREKJAD_10625, JALREKJAD_INFO, JALREKJAD_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),	// Orange airborn
			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6638, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),	// Green grounded	// Seen in game as other players
			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6653, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),	// Green grounded
			new Pet(PetGroup.BOSS, NpcID.KALPHITE_PRINCESS_6654, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),	// Green grounded

			new Pet(PetGroup.BOSS, NpcID.KEPHRITI, TUMEKENS_GUARDIAN_INFO + KEPHRITI, KEPHRITI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.KEPHRITI_11848, TUMEKENS_GUARDIAN_INFO + KEPHRITI, KEPHRITI_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.KITTEN, KITTEN_INFO, KITTEN_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5591, KITTEN_INFO, KITTEN_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5592, KITTEN_INFO, KITTEN_EXAMINE),	// White	// Seen in game as other players, white kitten
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5593, KITTEN_INFO, KITTEN_EXAMINE),	// Checkered orange	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5594, KITTEN_INFO, KITTEN_EXAMINE),	// Black	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5595, KITTEN_INFO, KITTEN_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.KITTEN_5596, KITTEN_INFO, KITTEN_EXAMINE),	// Grey and blue checkered	// Seen in game as other players

			new Pet(PetGroup.BOSS, NpcID.KRAKEN_6640, KRAKEN_INFO, KRAKEN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.KRAKEN_6656, KRAKEN_INFO, KRAKEN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.KREEARRA_JR, KREEARRA_JR_INFO, KREEARRA_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.KREEARRA_JR_6643, KREEARRA_JR_INFO, KREEARRA_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.KRIL_TSUTSAROTH_JR, KRIL_TSUTSAROTH_JR_INFO, KRIL_TSUTSAROTH_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.KRIL_TSUTSAROTH_JR_6647, KRIL_TSUTSAROTH_JR_INFO, KRIL_TSUTSAROTH_JR_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1627, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1628, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1629, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1630, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_1631, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6683, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6684, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6685, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6686, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6687, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.LAZY_CAT_6688, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and blue checkered

			new Pet(PetGroup.OTHER, NpcID.LAZY_HELLCAT, LAZY_HELLCAT_INFO, LAZY_HELLCAT_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.LAZY_HELLCAT_6689, LAZY_HELLCAT_INFO, LAZY_HELLCAT_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.LIL_CREATOR, LIL_CREATOR_INFO, LIL_CREATOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.LIL_CREATOR_3566, LIL_CREATOR_INFO, LIL_CREATOR_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.LIL_DESTRUCTOR, LIL_CREATOR_INFO, LIL_DESTRUCTOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.LIL_DESTRUCTOR_5008, LIL_CREATOR_INFO, LIL_DESTRUCTOR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LIL_BLOAT, LIL_ZIK_INFO, LIL_BLOAT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LIL_BLOAT_10871, LIL_ZIK_INFO, LIL_BLOAT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LIL_MAIDEN, LIL_ZIK_INFO, LIL_MAIDEN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LIL_MAIDEN_10870, LIL_ZIK_INFO, LIL_MAIDEN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LIL_NYLO, LIL_ZIK_INFO, LIL_NYLO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LIL_NYLO_10872, LIL_ZIK_INFO, LIL_NYLO_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LITTLE_PARASITE, LITTLE_NIGHTMARE_INFO + LITTLE_PARASITE_INFO, LITTLE_PARASITE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LITTLE_PARASITE_8541, LITTLE_NIGHTMARE_INFO + LITTLE_PARASITE_INFO, LITTLE_PARASITE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LIL_SOT, LIL_ZIK_INFO, LIL_SOT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LIL_SOT_10873, LIL_ZIK_INFO, LIL_SOT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LILVIATHAN, LIL_VIATHAN, LIL_VIATHAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LILVIATHAN_12160, LIL_VIATHAN, LIL_VIATHAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LIL_XARP, LIL_ZIK_INFO, LIL_XARP_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LIL_XARP_10874, LIL_ZIK_INFO, LIL_XARP_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.LIL_ZIK, LIL_ZIK_INFO, LIL_ZIK_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LIL_ZIK_8337, LIL_ZIK_INFO, LIL_ZIK_EXAMINE),	// Seen in game other player

			new Pet(PetGroup.BOSS, NpcID.LITTLE_NIGHTMARE, LITTLE_NIGHTMARE_INFO, LITTLE_NIGHTMARE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LITTLE_NIGHTMARE_9399, LITTLE_NIGHTMARE_INFO, LITTLE_NIGHTMARE_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.MAZ, MAZ_INFO, MAZ_EXAMINE),	// Not a real pet, but close enough for me to want to add it

			new Pet(PetGroup.BOSS, NpcID.MIDNIGHT, MIDNIGHT_INFO, MIDNIGHT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MIDNIGHT_7893, MIDNIGHT_INFO, MIDNIGHT_EXAMINE),	// Seen in game other player, morphed to NOON_7892

			new Pet(PetGroup.BOSS, NpcID.MUPHIN, MUPHIN_INFO + MUPHIN_RANGED, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUPHIN_12006, MUPHIN_INFO + MUPHIN_MELEE, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUPHIN_12007, MUPHIN_INFO + MUPHIN_SHIELDED, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUPHIN_12014, MUPHIN_INFO + MUPHIN_RANGED, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUPHIN_12015, MUPHIN_INFO + MUPHIN_MELEE, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUPHIN_12016, MUPHIN_INFO + MUPHIN_SHIELDED, MUPHIN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.NEXLING, NEXLING_INFO, NEXLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.NEXLING_11277, NEXLING_INFO, NEXLING_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.NOON, NOON_INFO, NOON_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.NOON_7892, NOON_INFO, NOON_EXAMINE),	// Seen in game as other players, morphed to MIDNIGHT_7893

			new Pet(PetGroup.BOSS, NpcID.OLMLET, OLMLET_INFO, OLMLET_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.OLMLET_7520, OLMLET_INFO, OLMLET_EXAMINE),	// Seen in game other player

			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5599, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5600, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5601, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5602, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_5603, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and blue checkered	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6676, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6677, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6678, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6679, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6680, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_CAT_6681, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and blue checkered

			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_HELLCAT, OVERGROWN_HELLCAT_INFO, OVERGROWN_HELLCAT_EXAMINE),	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.OVERGROWN_HELLCAT_6682, OVERGROWN_HELLCAT_INFO, OVERGROWN_HELLCAT_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.PET_ROCK, PET_ROCK_INFO, PET_ROCK_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.PET_ROCK_6657, PET_ROCK_INFO, PET_ROCK_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.PENANCE_PET, PENANCE_PET_INFO, PENANCE_PET_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.PENANCE_PET_6674, PENANCE_PET_INFO, PENANCE_PET_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.PHOENIX, PHOENIX_INFO, PHOENIX_EXAMINE),	// Green
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3078, PHOENIX_INFO, PHOENIX_EXAMINE),	// Blue
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3079, PHOENIX_INFO, PHOENIX_EXAMINE),	// White
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3080, PHOENIX_INFO, PHOENIX_EXAMINE),	// Purple
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3081, PHOENIX_INFO, PHOENIX_EXAMINE),	// Green
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3082, PHOENIX_INFO, PHOENIX_EXAMINE),	// Blue
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3083, PHOENIX_INFO, PHOENIX_EXAMINE),	// White
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_3084, PHOENIX_INFO, PHOENIX_EXAMINE),	// Purple
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_7368, PHOENIX_INFO, PHOENIX_EXAMINE),	// Orange
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_7370, PHOENIX_INFO, PHOENIX_EXAMINE),	// Orange

			new Pet(PetGroup.BOSS, NpcID.PRINCE_BLACK_DRAGON, PRINCE_BLACK_DRAGON_INFO, PRINCE_BLACK_DRAGON_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.PRINCE_BLACK_DRAGON_6652, PRINCE_BLACK_DRAGON_INFO, PRINCE_BLACK_DRAGON_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.QUETZIN, QUETZIN_INFO, QUETZIN_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.QUETZIN_12858, QUETZIN_INFO, QUETZIN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.PUPPADILE, OLMLET_INFO + OLMLET_CM_VARIANTS, PUPPADILE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.PUPPADILE_8201, OLMLET_INFO + OLMLET_CM_VARIANTS, PUPPADILE_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.RED, ROCKY_INFO + " " + ROCKY_RED_PANDA, RED_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.RED_9852, ROCKY_INFO + " " + ROCKY_RED_PANDA, RED_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN, RIFT_GUARDIAN_INFO + " This is the Fire variant.", RIFT_GUARDIAN_EXAMINE),	// Fire
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7338, RIFT_GUARDIAN_INFO + " This is the Air variant.", RIFT_GUARDIAN_EXAMINE),	// Air
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7339, RIFT_GUARDIAN_INFO + " This is the Mind variant.", RIFT_GUARDIAN_EXAMINE),	// Mind
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7340, RIFT_GUARDIAN_INFO + " This is the Water variant.", RIFT_GUARDIAN_EXAMINE),	// Water
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7341, RIFT_GUARDIAN_INFO + " This is the Earth variant.", RIFT_GUARDIAN_EXAMINE),	// Earth
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7342, RIFT_GUARDIAN_INFO + " This is the Body variant.", RIFT_GUARDIAN_EXAMINE),	// Body
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7343, RIFT_GUARDIAN_INFO + " This is the Cosmic variant.", RIFT_GUARDIAN_EXAMINE),	// Cosmic
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7344, RIFT_GUARDIAN_INFO + " This is the Chaos variant.", RIFT_GUARDIAN_EXAMINE),	// Chaos
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7345, RIFT_GUARDIAN_INFO + " This is the Nature variant.", RIFT_GUARDIAN_EXAMINE),	// Nature
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7346, RIFT_GUARDIAN_INFO + " This is the Law variant.", RIFT_GUARDIAN_EXAMINE),	// Law
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7347, RIFT_GUARDIAN_INFO + " This is the Death variant.", RIFT_GUARDIAN_EXAMINE),	// Death
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7348, RIFT_GUARDIAN_INFO + " This is the Soul variant.", RIFT_GUARDIAN_EXAMINE),	// Soul
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7349, RIFT_GUARDIAN_INFO + " This is the Astral variant.", RIFT_GUARDIAN_EXAMINE),	// Astral
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7350, RIFT_GUARDIAN_INFO + " This is the Blood variant.", RIFT_GUARDIAN_EXAMINE),	// Blood
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7354, RIFT_GUARDIAN_INFO + " This is the Fire variant.", RIFT_GUARDIAN_EXAMINE),	// fire
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7355, RIFT_GUARDIAN_INFO + " This is the Air variant.", RIFT_GUARDIAN_EXAMINE),	// Air
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7356, RIFT_GUARDIAN_INFO + " This is the Mind variant.", RIFT_GUARDIAN_EXAMINE),	// Mind
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7357, RIFT_GUARDIAN_INFO + " This is the Water variant.", RIFT_GUARDIAN_EXAMINE),	// Water
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7358, RIFT_GUARDIAN_INFO + " This is the Earth variant.", RIFT_GUARDIAN_EXAMINE),	// Earth
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7359, RIFT_GUARDIAN_INFO + " This is the Body variant.", RIFT_GUARDIAN_EXAMINE),	// Body
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7360, RIFT_GUARDIAN_INFO + " This is the Cosmic variant.", RIFT_GUARDIAN_EXAMINE),	// Cosmic
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7361, RIFT_GUARDIAN_INFO + " This is the Chaos variant.", RIFT_GUARDIAN_EXAMINE),	// Chaos
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7362, RIFT_GUARDIAN_INFO + " This is the Nature variant.", RIFT_GUARDIAN_EXAMINE),	// Nature
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7363, RIFT_GUARDIAN_INFO + " This is the Law variant.", RIFT_GUARDIAN_EXAMINE),	// Law
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7364, RIFT_GUARDIAN_INFO + " This is the Death variant.", RIFT_GUARDIAN_EXAMINE),	// Death
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7365, RIFT_GUARDIAN_INFO + " This is the Soul variant.", RIFT_GUARDIAN_EXAMINE),	// Soul
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7366, RIFT_GUARDIAN_INFO + " This is the Astral variant.", RIFT_GUARDIAN_EXAMINE),	// Astral
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_7367, RIFT_GUARDIAN_INFO + " This is the Blood variant.", RIFT_GUARDIAN_EXAMINE),	// Blood
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_8024, RIFT_GUARDIAN_INFO + " This is the Wrath variant.", RIFT_GUARDIAN_EXAMINE),	// Wrath
			new Pet(PetGroup.SKILLING, NpcID.RIFT_GUARDIAN_8028, RIFT_GUARDIAN_INFO + " This is the Wrath variant.", RIFT_GUARDIAN_EXAMINE),	// Wrath

			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM, ROCK_GOLEM_INFO + " This is the Amethyst variant.", ROCK_GOLEM_EXAMINE),	// Amethyst
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7439, ROCK_GOLEM_INFO + " This is the Rock variant.", ROCK_GOLEM_EXAMINE),	// Rock
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7440, ROCK_GOLEM_INFO + " This is the Tin variant.", ROCK_GOLEM_EXAMINE),	// Tin
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7441, ROCK_GOLEM_INFO + " This is the Copper variant.", ROCK_GOLEM_EXAMINE),	// Copper
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7442, ROCK_GOLEM_INFO + " This is the Iron variant.", ROCK_GOLEM_EXAMINE),	// Iron
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7443, ROCK_GOLEM_INFO + " This is the Blurite variant.", ROCK_GOLEM_EXAMINE),	// Blurite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7444, ROCK_GOLEM_INFO + " This is the Silver variant.", ROCK_GOLEM_EXAMINE),	// Silver
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7445, ROCK_GOLEM_INFO + " This is the Coal variant.", ROCK_GOLEM_EXAMINE),	// Coal
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7446, ROCK_GOLEM_INFO + " This is the Gold variant.", ROCK_GOLEM_EXAMINE),	// Gold
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7447, ROCK_GOLEM_INFO + " This is the Mithril variant.", ROCK_GOLEM_EXAMINE),	// Mithril
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7448, ROCK_GOLEM_INFO + " This is the Granite variant.", ROCK_GOLEM_EXAMINE),	// Granite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7449, ROCK_GOLEM_INFO + " This is the Adamantite variant.", ROCK_GOLEM_EXAMINE),	// Adamantite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7450, ROCK_GOLEM_INFO + " This is the Runite variant.", ROCK_GOLEM_EXAMINE),	// Runite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7451, ROCK_GOLEM_INFO + " This is the Rock variant.", ROCK_GOLEM_EXAMINE),	// Rock
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7452, ROCK_GOLEM_INFO + " This is the Tin variant.", ROCK_GOLEM_EXAMINE),	// Tin
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7453, ROCK_GOLEM_INFO + " This is the Copper variant.", ROCK_GOLEM_EXAMINE),	// Copper
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7454, ROCK_GOLEM_INFO + " This is the Iron variant.", ROCK_GOLEM_EXAMINE),	// Iron
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7455, ROCK_GOLEM_INFO + " This is the Blurite variant.", ROCK_GOLEM_EXAMINE),	// Blurite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7642, ROCK_GOLEM_INFO + " This is the Silver variant.", ROCK_GOLEM_EXAMINE),	// Silver
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7643, ROCK_GOLEM_INFO + " This is the Coal variant.", ROCK_GOLEM_EXAMINE),	// Coal
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7644, ROCK_GOLEM_INFO + " This is the Gold variant.", ROCK_GOLEM_EXAMINE),	// Gold
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7645, ROCK_GOLEM_INFO + " This is the Mithril variant.", ROCK_GOLEM_EXAMINE),	// Mithril
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7646, ROCK_GOLEM_INFO + " This is the Granite variant.", ROCK_GOLEM_EXAMINE),	// Granite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7647, ROCK_GOLEM_INFO + " This is Adamantite fire variant.", ROCK_GOLEM_EXAMINE),	// Adamantite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7648, ROCK_GOLEM_INFO + " This is the Runite variant.", ROCK_GOLEM_EXAMINE),	// Runite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7711, ROCK_GOLEM_INFO + " This is the Amethyst variant.", ROCK_GOLEM_EXAMINE),	// Amethyst
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7736, ROCK_GOLEM_INFO + " This is the Lovakite variant.", ROCK_GOLEM_EXAMINE),	// Lovakite
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7737, ROCK_GOLEM_INFO + " This is the Elemental variant.", ROCK_GOLEM_EXAMINE),	// Elemental
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7738, ROCK_GOLEM_INFO + " This is the Daeyalt variant.", ROCK_GOLEM_EXAMINE),	// Daeyalt
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7739, ROCK_GOLEM_INFO + " This is the Lovakite variant.", ROCK_GOLEM_EXAMINE),	// Lovakite variant, otherplayer
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7740, ROCK_GOLEM_INFO + " This is the Elemental variant.", ROCK_GOLEM_EXAMINE),	// Elemental variant, other player
			new Pet(PetGroup.SKILLING, NpcID.ROCK_GOLEM_7741, ROCK_GOLEM_INFO + " This is the Daeyalt variant.", ROCK_GOLEM_EXAMINE),	// Daeyalt

			new Pet(PetGroup.SKILLING, NpcID.ROCKY, ROCKY_INFO, ROCKY_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.ROCKY_7353, ROCKY_INFO, ROCKY_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING, SCORPIAS_OFFSPRING_INFO, SCORPIAS_OFFSPRING_EXAMINE),		// Usually even, what's up with this?
			new Pet(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING_5561, SCORPIAS_OFFSPRING_INFO, SCORPIAS_OFFSPRING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SCORPIAS_OFFSPRING_6616, SCORPIAS_OFFSPRING_INFO, SCORPIAS_OFFSPRING_EXAMINE),	// This one is much smaller

			new Pet(PetGroup.BOSS, NpcID.SCURRY, SCURRY_INFO, SCURRY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SCURRY_7616, SCURRY_INFO, SCURRY_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.SKOTOS, SKOTOS_INFO, SKOTOS_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SKOTOS_7671, SKOTOS_INFO, SKOTOS_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_6639, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Yellow
			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_6655, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Yellow
			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_8482, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Blue
			new Pet(PetGroup.BOSS, NpcID.SMOKE_DEVIL_8483, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Blue

			new Pet(PetGroup.BOSS, NpcID.SMOL_HEREDIT, SMOL_HEREDIT_INFO, SMOL_HEREDIT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SMOL_HEREDIT_12857, SMOL_HEREDIT_INFO, SMOL_HEREDIT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.SMOLCANO, SMOLCANO_INFO, SMOLCANO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SMOLCANO_8739, SMOLCANO_INFO, SMOLCANO_EXAMINE),

			// Not sure if there are more pet ids, or if the boss fight minions will appear
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2127, SNAKELING_INFO, SNAKELING_EXAMINE),	// Green
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2130, SNAKELING_INFO, SNAKELING_EXAMINE),	// Green variant, other player
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2131, SNAKELING_INFO, SNAKELING_EXAMINE),	// Red variant, other player
			new Pet(PetGroup.BOSS, NpcID.SNAKELING_2132, SNAKELING_INFO, SNAKELING_EXAMINE),	// Blue/Purple variant, other player

			new Pet(PetGroup.BOSS, NpcID.SRARACHA, SRARACHA_INFO, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_2144, SRARACHA_INFO, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11157, SRARACHA_INFO + SRARACHA_ORANGE, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11159, SRARACHA_INFO + SRARACHA_ORANGE, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11158, SRARACHA_INFO + SRARACHA_BLUE, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SRARACHA_11160, SRARACHA_INFO + SRARACHA_BLUE, SRARACHA_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT, TANGLEROOT_INFO, TANGLEROOT_EXAMINE),	// Acorn
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_7352, TANGLEROOT_INFO + " This is the Acorn variant.", TANGLEROOT_EXAMINE),	// Acorn
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9492, TANGLEROOT_INFO + " This is the Crystal variant.", TANGLEROOT_EXAMINE),	// Crystal
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9493, TANGLEROOT_INFO + " This is the Dragon Fruit variant.", TANGLEROOT_EXAMINE),	// Dragon
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9494, TANGLEROOT_INFO + " This is the Guam variant.", TANGLEROOT_EXAMINE),	// Guam
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9495, TANGLEROOT_INFO + " This is the White Lily variant.", TANGLEROOT_EXAMINE),	// White Lily
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9496, TANGLEROOT_INFO + " This is the Redwood variant.", TANGLEROOT_EXAMINE),	// Redwood
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9497, TANGLEROOT_INFO + " This is the Crystal variant.", TANGLEROOT_EXAMINE),	// Crystal
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9498, TANGLEROOT_INFO + " This is the Dragon Fruit variant.", TANGLEROOT_EXAMINE),	// Dragon Fruit Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9499, TANGLEROOT_INFO + " This is the Guam variant.", TANGLEROOT_EXAMINE),	// Guam Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9500, TANGLEROOT_INFO + " This is the White Lily variant.", TANGLEROOT_EXAMINE),	// White Lily Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.TANGLEROOT_9501, TANGLEROOT_INFO + " This is the Redwood variant.", TANGLEROOT_EXAMINE),	// Redwood Variant, other player

			new Pet(PetGroup.BOSS, NpcID.TEKTINY, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TEKTINY_8202, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.TINY_TEMPOR, TINY_TEMPOR_INFO, TINY_TEMPOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.TINY_TEMPOR_10637, TINY_TEMPOR_INFO, TINY_TEMPOR_EXAMINE),

			new Pet(PetGroup.TOY, NpcID.TOY_DOLL, TOY_DOLL_INFO, TOY_DOLL_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.TOY_DOLL_9253, TOY_DOLL_INFO, TOY_DOLL_EXAMINE),

			new Pet(PetGroup.TOY, NpcID.TOY_MOUSE, TOY_MOUSE_INFO, TOY_MOUSE_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.TOY_MOUSE_9255, TOY_MOUSE_INFO, TOY_MOUSE_EXAMINE),

			new Pet(PetGroup.TOY, NpcID.TOY_SOLDIER, TOY_SOLDIER_INFO, TOY_SOLDIER_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.TOY_SOLDIER_9251, TOY_SOLDIER_INFO, TOY_SOLDIER_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.TUMEKENS_DAMAGED_GUARDIAN, TUMEKENS_GUARDIAN_INFO + TUMEKENS_DAMAGED_GUARDIAN, TUMEKENS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TUMEKENS_DAMAGED_GUARDIAN_11850, TUMEKENS_GUARDIAN_INFO + TUMEKENS_DAMAGED_GUARDIAN, TUMEKENS_GUARDIAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.TUMEKENS_GUARDIAN, TUMEKENS_GUARDIAN_INFO, TUMEKENS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TUMEKENS_GUARDIAN_11812, TUMEKENS_GUARDIAN_INFO, TUMEKENS_GUARDIAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.TZREKJAD, TZREKJAD_INFO, TZREKJAD_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TZREKJAD_5893, TZREKJAD_INFO, TZREKJAD_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.TZREKZUK, JALNIBREK_INFO, JALNIBREK_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TZREKZUK_8011, JALNIBREK_INFO, JALNIBREK_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.VANGUARD_8198, OLMLET_INFO + OLMLET_CM_VARIANTS, VANGUARD_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VANGUARD_8203, OLMLET_INFO + OLMLET_CM_VARIANTS, VANGUARD_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.VASA_MINIRIO, OLMLET_INFO + OLMLET_CM_VARIANTS, VASA_MINIRIO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VASA_MINIRIO_8204, OLMLET_INFO + OLMLET_CM_VARIANTS, VASA_MINIRIO_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING, VENENATIS_SPIDERLING_INFO, VENENATIS_SPIDERLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING_5557, VENENATIS_SPIDERLING_INFO, VENENATIS_SPIDERLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING_11981, VENENATIS_SPIDERLING_INFO + RETRO_VARIANT, VENENATIS_SPIDERLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VENENATIS_SPIDERLING_11985, VENENATIS_SPIDERLING_INFO + RETRO_VARIANT, VENENATIS_SPIDERLING_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.VESPINA, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VESPINA_8205, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.VETION_JR, VETION_JR_INFO, VETION_JR_EXAMINE),	// Purple
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_5537, VETION_JR_INFO, VETION_JR_EXAMINE),	// Orange
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_5559, VETION_JR_INFO, VETION_JR_EXAMINE),	// Purple
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_5560, VETION_JR_INFO, VETION_JR_EXAMINE),	// Orange
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_11983, VETION_JR_INFO, VETION_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_11984, VETION_JR_INFO, VETION_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_11987, VETION_JR_INFO, VETION_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VETION_JR_11988, VETION_JR_INFO, VETION_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.VORKI, VORKI_INFO, VORKI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VORKI_8029, VORKI_INFO, VORKI_EXAMINE),	// Seen in game, other player

			new Pet(PetGroup.OTHER, NpcID.WILY_CAT, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5585, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5586, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5587, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5588, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_5589, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6690, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6691, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6692, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6693, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6694, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILY_CAT_6695, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black and grey checkered

			new Pet(PetGroup.OTHER, NpcID.WILY_HELLCAT, WILY_HELLCAT_INFO, WILY_HELLCAT_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.WILY_HELLCAT_6696, WILY_HELLCAT_INFO, WILY_HELLCAT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.WISP, WISP, WISP_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WISP_12157, WISP, WISP_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.YOUNGLLEF, YOUNGLLEF_INFO, YOUNGLLEF_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.YOUNGLLEF_8737, YOUNGLLEF_INFO, YOUNGLLEF_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ZEBO, TUMEKENS_GUARDIAN_INFO + ZEBO, ZEBO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ZEBO_11849, TUMEKENS_GUARDIAN_INFO + ZEBO, ZEBO_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.ZIGGY, ROCKY_INFO + " " + ROCKY_TANUKI, ZIGGY_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.ZIGGY_9853, ROCKY_INFO + " " + ROCKY_TANUKI, ZIGGY_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ZILYANA_JR, ZILYANA_JR_INFO, ZILYANA_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ZILYANA_JR_6646, ZILYANA_JR_INFO, ZILYANA_JR_EXAMINE)

	};
}
