package com.micro.petinfo;

import com.google.common.collect.ImmutableMap;
import com.micro.petinfo.dataretrieval.Pet;
import com.micro.petinfo.dataretrieval.PetGroup;
import net.runelite.api.gameval.NpcID;
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
		catch (Exception e) {
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
	private static final String BRAN_INFO = "is dropped by the Royal Titans, Branda the Fire Queen and Eldric the Ice King, at rates from 1/1500 to 1/3000.";
	private static final String BRAN_EXAMINE = "Might have a short temper.";
	private static final String RIC_EXAMINE = "Looks like a chill guy.";
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
	private static final String BONE_SQUIRREL_EXAMINE = "A giant squirrel with a skeletal aesthetic.";
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
	private static final String HUBERTE_INFO = "is dropped by the Hueycoatl at a base rate of 1/400, scaled according to kill contribution.";
	private static final String HUBERTE_EXAMINE = "Looks very polite.";
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
	private static final String MOXI_INFO = "is dropped by Amoxliatl at a rate of 1/3000.";
	private static final String MOXI_EXAMINE = "A tiny frost nagua.";
	private static final String MUPHIN_INFO = "is dropped by Phantom Muspah, at a rate of 1/2500.";
	private static final String MUPHIN_EXAMINE = "An oversized grub with arms.";
	private static final String NEXLING_INFO = "is dropped by Nex at a rate of 1/500.";
	private static final String NEXLING_EXAMINE = "The gods don't quite fear this one.";
	private static final String NID_INFO = "is dropped by Araxxor at a base rate of 1/3000 when harvesting Araxxor's corpse, or 1/1500 when destroying the corpse instead of harvesting.";
	private static final String NID_EXAMINE = "Looks like a respectable gentleman.";
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
	private static final String QUETZIN_INFO = "is obtained by opening hunters' loot sacks obtained from completing Hunters' Rumours in the Hunter Guild, at a rate of 1/1000.";
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
	private static final String YAMI_INFO = "is obtained by killing Yama, at a rate of 1/2500, or 1/100 when fought with a contract.";
	private static final String YAMI_EXAMINE = "Small, but somehow still looks down on me.";
	private static final String YOUNGLLEF_INFO = "is obtained by completing The Gauntlet, at a rate of 1/2000 (or 1/800 for The Corrupted Gauntlet).";
	private static final String YOUNGLLEF_EXAMINE = "Looks like a bit of a nightmare.";
	private static final String ZILYANA_JR_INFO = "is dropped by Commander Zilyana, at a rate of 1/5000.";
	private static final String ZILYANA_JR_EXAMINE = "Somehow a junior even though she's named after her spawn mother!";
	/*
	 *	Variant text
	 */
	private static final String BABY_CHINCHOMPA_GOLD = " The gold variant is obtained by causing a Baby Chinchompa to metamorphosize, at a rate of 1/10000.";

	private static final String BABY_MOLE_RAT = " This is a variant of the Baby Mole, obtained by using a Mole Claw on the Baby Mole.";

	private static final String BRAN_VARIANT = " This is a miniature of Branda the Fire Queen, and can be turned into the Ric pet.";

	private static final String RIC_VARIANT = " This is a miniature of Eldric the Ice King, and can be turned into the Bran pet.";

	private static final String DARK_GIANT_SQUIRREL = " This is a variant of the Giant Squirrel which is unlocked by using a Dark Acorn on a Giant Squirrel.";

	private static final String BONE_SQUIRREL = " This is a variant of the Giant Squirrel which is unlocked by using a Calcified Acorn on a Giant Squirrel.";

	private static final String FOX = " This is a variant of the Beaver, unlocked by using a Fox Whistle (a drop from the Poachers forestry event) on a Beaver.";
	private static final String FOX_EXAMINE = "Foxy.";

	private static final String GREAT_BLUE_HERON_INFO = " This is a variant of the Heron, obtained by feeding the Heron 3,000 Spirit Flakes.";
	private static final String GREATISH_GUARDIAN = " This is a variant of the Rift Guardian, that can be obtained by using the Guardian's Eye reward from the Guardians of the Rift minigame on a Rift Guardian.";

	private static final String LITTLE_PARASITE_INFO = " This is a variant of the the Little Nightmare, obtained by using a Parasitic Egg on her.";

	private static final String MUPHIN_MELEE = " This is the melee variant, obtained by using Charged Ice on Muphin.";
	private static final String MUPHIN_RANGED = " This is the ranged variant, obtained by using Charged Ice on Muphin.";
	private static final String MUPHIN_SHIELDED = " This is the shielded variant, obtained by using Charged Ice on Muphin.";

	private static final String RAX_VARIANT = " This is a metamorph of Nid, which is obtained by using coagulated venom on Nid.";
	private static final String RAX_EXAMINE = "Really hope she doesn't crawl into my mouth while I sleep.";

	private static final String PHEASANT = " This is a variant of the Beaver, that can obtained by using a Golden Pheasant Egg (a very rare drop from the Pheasant Control forestry event) on a Beaver.";
	private static final String PHEASANT_EXAMINE = "A brightly coloured game bird.";

	private static final String ROCKY_RED_PANDA = " This is the Red Panda variant of Rocky, obtained by using Redberries on any variant of Rocky.";
	private static final String ROCKY_TANUKI = " This is the Tanuki variant of Rocky, obtained by using Poison Ivy Berries on any variant of Rocky.";

	private static final String TUMEKENS_DAMAGED_GUARDIAN = " This is a variant of the Tumeken's guardian unlocked with the Ancient remnant";
	private static final String ELIDINIS_GUARDIAN = " This is a variant of the Tumeken's guardian.";
	private static final String ELIDINIS_DAMAGED_GUARDIAN = " This is a variant of the Tumeken's guardian unlocked with the Ancient remnant";
	private static final String AKKHITO = " This is a variant of the Tumeken's guardian unlocked with the Remnant of Akkha.";
	private static final String BABI = " This is a variant of the Tumeken's guardian unlocked with the Remnant of Ba-Ba.";

	private static final String SRARACHA_ORANGE = " This is the Orange variant unlocked using an orange egg sack.";
	private static final String SRARACHA_BLUE = " This is the Blue variant unlocked using a blue egg sack.";

	private static final String BEAVER_LOGS = " This is the normal Logs variant.";
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
			new Pet(PetGroup.BOSS, NpcID.POH_ABYSSALSIRE_PET, ABYSSAL_ORPHAN_INFO, ABYSSAL_ORPHAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ABYSSALSIRE_PET, ABYSSAL_ORPHAN_INFO, ABYSSAL_ORPHAN_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_ABYSSAL_PET, ABYSSAL_PROTECTOR_INFO, ABYSSAL_PROTECTOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.ABYSSAL_PET, ABYSSAL_PROTECTOR_INFO, ABYSSAL_PROTECTOR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_AKKHA, TUMEKENS_GUARDIAN_INFO + AKKHITO, AKKHITO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_AKKHA, TUMEKENS_GUARDIAN_INFO + AKKHITO, AKKHITO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_BABA, TUMEKENS_GUARDIAN_INFO + BABI, BABI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_BABA, TUMEKENS_GUARDIAN_INFO + BABI, BABI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_ELIDINIS_DESTROYED, TUMEKENS_GUARDIAN_INFO + ELIDINIS_DAMAGED_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_ELIDINIS_DESTROYED, TUMEKENS_GUARDIAN_INFO + ELIDINIS_DAMAGED_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_ELIDINIS, TUMEKENS_GUARDIAN_INFO + ELIDINIS_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_ELIDINIS, TUMEKENS_GUARDIAN_INFO + ELIDINIS_GUARDIAN, ELIDINIS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_KEPHRI, TUMEKENS_GUARDIAN_INFO + KEPHRITI, KEPHRITI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_KEPHRI, TUMEKENS_GUARDIAN_INFO + KEPHRITI, KEPHRITI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_TUMEKEN_DESTROYED, TUMEKENS_GUARDIAN_INFO + TUMEKENS_DAMAGED_GUARDIAN, TUMEKENS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_TUMEKEN_DESTROYED, TUMEKENS_GUARDIAN_INFO + TUMEKENS_DAMAGED_GUARDIAN, TUMEKENS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_TUMEKEN, TUMEKENS_GUARDIAN_INFO, TUMEKENS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_TUMEKEN, TUMEKENS_GUARDIAN_INFO, TUMEKENS_GUARDIAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_WARDEN_PET_ZEBAK, TUMEKENS_GUARDIAN_INFO + ZEBO, ZEBO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WARDEN_PET_ZEBAK, TUMEKENS_GUARDIAN_INFO + ZEBO, ZEBO_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_HUNTER_RED, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Red Variant
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_HUNTER_GREY, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Grey Variant
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_HUNTER_BLACK, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Black Variant
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_HUNTER_GOLD, BABY_CHINCHOMPA_INFO + " " + BABY_CHINCHOMPA_GOLD, BABY_CHINCHOMPA_EXAMINE),	// Gold Variant
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_HUNTER_RED, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Red Variant
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_HUNTER_GREY, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Grey Variant
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_HUNTER_BLACK, BABY_CHINCHOMPA_INFO, BABY_CHINCHOMPA_EXAMINE),	// Black Variant
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_HUNTER_GOLD, BABY_CHINCHOMPA_INFO + " " + BABY_CHINCHOMPA_GOLD, BABY_CHINCHOMPA_EXAMINE),	// Gold Variant

			new Pet(PetGroup.BOSS, NpcID.POH_MOLE_PET, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MOLE_PET, BABY_MOLE_INFO, BABY_MOLE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_MOLE_PET_NAKED, BABY_MOLE_INFO + BABY_MOLE_RAT, BABY_MOLE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MOLE_PET_NAKED, BABY_MOLE_INFO + BABY_MOLE_RAT, BABY_MOLE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_RTBRANDA_PET, BRAN_INFO + BRAN_VARIANT, BRAN_VARIANT),
			new Pet(PetGroup.BOSS, NpcID.RTBRANDA_PET, BRAN_INFO + BRAN_VARIANT, BRAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_RTELDRIC_PET, BRAN_INFO + RIC_VARIANT, RIC_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.RTELDRIC_PET, BRAN_INFO + RIC_VARIANT, RIC_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_DUKE_SUCELLUS_PET, BARON, BARON_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DUKE_SUCELLUS_PET, BARON, BARON_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPETWC, BEAVER_INFO + BEAVER_LOGS, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPETWC, BEAVER_INFO + BEAVER_LOGS, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_OAK, BEAVER_INFO + BEAVER_OAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_OAK, BEAVER_INFO + BEAVER_OAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_WILLOW, BEAVER_INFO + BEAVER_WILLOW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_WILLOW, BEAVER_INFO + BEAVER_WILLOW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_TEAK, BEAVER_INFO + BEAVER_TEAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_TEAK, BEAVER_INFO + BEAVER_TEAK, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_MAPLE, BEAVER_INFO + BEAVER_MAPLE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_MAPLE, BEAVER_INFO + BEAVER_MAPLE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_MAHOGANY, BEAVER_INFO + BEAVER_MAHOGANY, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_MAHOGANY, BEAVER_INFO + BEAVER_MAHOGANY, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_ARCTIC, BEAVER_INFO + BEAVER_ARCTIC_PINE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_ARCTIC, BEAVER_INFO + BEAVER_ARCTIC_PINE, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_YEW, BEAVER_INFO + BEAVER_YEW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_YEW, BEAVER_INFO + BEAVER_YEW, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_MAGIC, BEAVER_INFO + BEAVER_MAGIC, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_MAGIC, BEAVER_INFO + BEAVER_MAGIC, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_REDWOOD, BEAVER_INFO + BEAVER_REDWOOD, BEAVER_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_REDWOOD, BEAVER_INFO + BEAVER_REDWOOD, BEAVER_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_BLOODHOUNDPET, BLOODHOUND_INFO, BLOODHOUND_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.BLOODHOUNDPET, BLOODHOUND_INFO, BLOODHOUND_EXAMINE),	// Seen in game, other players

			new Pet(PetGroup.BOSS, NpcID.POH_VARDORVIS_PET, BUTCH, BUTCH_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VARDORVIS_PET, BUTCH, BUTCH_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_CALLISTO_PET, CALLISTO_CUB_INFO, CALLISTO_CUB_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CALLISTOPET, CALLISTO_CUB_INFO, CALLISTO_CUB_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_CALLISTO_PET_LEGACY, CALLISTO_CUB_INFO + RETRO_VARIANT, CALLISTO_CUB_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CALLISTOPET_LEGACY, CALLISTO_CUB_INFO + RETRO_VARIANT, CALLISTO_CUB_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_DEFAULT, CAT_INFO, CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT, CAT_INFO, CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_LIGHT, CAT_INFO, CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_BROWN, CAT_INFO, CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_BLACK, CAT_INFO, CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_BROWNGREY, CAT_INFO, CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_BLUEGREY, CAT_INFO, CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT_LIGHT, CAT_INFO, CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT_BROWN, CAT_INFO, CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT_BLACK, CAT_INFO, CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT_BROWNGREY, CAT_INFO, CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT_BLUEGREY, CAT_INFO, CAT_EXAMINE),	// Grey and blue checkered

			new Pet(PetGroup.BOSS, NpcID.POH_CHAOS_ELEMENTAL_PET, CHAOS_ELEMENTAL_JR_INFO, CHAOS_ELEMENTAL_JR_EXAMINE),	// Spotted in Game as other players
			new Pet(PetGroup.BOSS, NpcID.CHAOS_ELEMENTAL_PET, CHAOS_ELEMENTAL_JR_INFO, CHAOS_ELEMENTAL_JR_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_CHOMPYBIRD_PET, CHOMPY_CHICK_INFO, CHOMPY_CHICK_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.CHOMPY_BIRD_PET, CHOMPY_CHICK_INFO, CHOMPY_CHICK_EXAMINE),	// Slightly larger?

			new Pet(PetGroup.TOY, NpcID.POH_TOY_CAT, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.BRAIN_TOY_CAT, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.POH_TOY_CAT_MENAGERIE, CLOCKWORK_CAT_INFO, CLOCKWORK_CAT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_CORPPET, CORPOREAL_CRITTER_INFO, CORPOREAL_CRITTER_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.CORP_PET, CORPOREAL_CRITTER_INFO, CORPOREAL_CRITTER_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_GAUNTLET_PET_CORRUPT, CORRUPTED_YOUNGLLEF_INFO, CORRUPTED_YOUNGLLEF_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.GAUNTLET_PET_CORRUPT, CORRUPTED_YOUNGLLEF_INFO, CORRUPTED_YOUNGLLEF_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_PRIME_PET, DAGANNOTH_PRIME_JR_INFO, DAGANNOTH_PRIME_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.PRIME_PET, DAGANNOTH_PRIME_JR_INFO, DAGANNOTH_PRIME_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_REX_PET, DAGANNOTH_REX_JR_INFO, DAGANNOTH_REX_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.REX_PET, DAGANNOTH_REX_JR_INFO, DAGANNOTH_REX_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_SUPREME_PET, DAGANNOTH_SUPREME_JR_INFO, DAGANNOTH_SUPREME_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SUPREME_PET, DAGANNOTH_SUPREME_JR_INFO, DAGANNOTH_SUPREME_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_COREPET, DARK_CORE_INFO, DARK_CORE_EXAMINE),    // Not sure this is the pet dark core, but it looks right
			new Pet(PetGroup.BOSS, NpcID.CORE_PET, DARK_CORE_INFO, DARK_CORE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_SKILLPET_AGILITY_DARK, GIANT_SQUIRREL_INFO + DARK_GIANT_SQUIRREL, GIANT_SQUIRREL_EXAMINE),	// Thank you to Gamma91/Bram91 on gitHub for the info
			new Pet(PetGroup.BOSS, NpcID.SKILLPET_AGILITY_DARK, GIANT_SQUIRREL_INFO + DARK_GIANT_SQUIRREL, GIANT_SQUIRREL_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_TEKTON_ENRAGED_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TEKTON_ENRAGED_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_FISHBOWL_BLUEFISH, FISHBOWL_INFO, FISHBOWL_BLUEFISH_EXAMINE),	// Blue
			new Pet(PetGroup.OTHER, NpcID.POH_FISHBOWL_GREENFISH, FISHBOWL_INFO, FISHBOWL_GREENFISH_EXAMINE),	// Green
			new Pet(PetGroup.OTHER, NpcID.POH_FISHBOWL_SPINEFISH, FISHBOWL_INFO, FISHBOWL_SPINEFISH_EXAMINE),	// Gold

			new Pet(PetGroup.BOSS, NpcID.POH_VESPULA_FLYING_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VESPULA_FLYING_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_FOX, BEAVER_INFO + FOX, FOX_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_FOX, BEAVER_INFO + FOX, FOX_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_BANDOS_PET, GENERAL_GRAARDOR_JR_INFO, GENERAL_GRAARDOR_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.BANDOS_PET, GENERAL_GRAARDOR_JR_INFO, GENERAL_GRAARDOR_JR_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_AGILITY, GIANT_SQUIRREL_INFO, GIANT_SQUIRREL_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_AGILITY, GIANT_SQUIRREL_INFO, GIANT_SQUIRREL_EXAMINE),	// Seen in game as other players
			new Pet(PetGroup.SKILLING, NpcID.HALLOWED_ADVENTURER_FLOOR3_TREASURE_PET_VISIBLE, GIANT_SQUIRREL_INFO, GIANT_SQUIRREL_EXAMINE), // There's usually an even number, so I'm not sure what's up

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_AGILITY_BONE, GIANT_SQUIRREL_INFO + BONE_SQUIRREL, BONE_SQUIRREL_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_AGILITY_BONE, GIANT_SQUIRREL_INFO + BONE_SQUIRREL, BONE_SQUIRREL_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FISH_TEMPOROSS, HERON_INFO + GREAT_BLUE_HERON_INFO, HERON_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FISH_TEMPOROSS, HERON_INFO + GREAT_BLUE_HERON_INFO, HERON_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_GOTR, RIFT_GUARDIAN_INFO + GREATISH_GUARDIAN, GREATISH_GUARDIAN_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_GOTR, RIFT_GUARDIAN_INFO + GREATISH_GUARDIAN, GREATISH_GUARDIAN_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_GROWNCAT_HELL, HELLCAT_INFO, HELLCAT_EXAMINE),	// Spotted in Game as other players
			new Pet(PetGroup.OTHER, NpcID.GROWNCAT_HELL, HELLCAT_INFO, HELLCAT_EXAMINE),	// Spotted in Game as other players

			new Pet(PetGroup.OTHER, NpcID.KITTENPET_HELL, HELLKITTEN_INFO, HELLKITTEN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_HELLPET, HELLPUPPY_INFO, HELLPUPPY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.HELLPET, HELLPUPPY_INFO, HELLPUPPY_EXAMINE),	// Seen in game as other players

			new Pet(PetGroup.SKILLING, NpcID.POH_HERBIBOAR_PET, HERBI_INFO, HERBI_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.HERBIBOAR_PET, HERBI_INFO, HERBI_EXAMINE),	// Spotted in game as other players, slightly larger?

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FISH, HERON_INFO, HERON_EXAMINE),	// Spotted in Game as other players
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FISH, HERON_INFO, HERON_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_HUEY_PET, HUBERTE_INFO, HUBERTE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.HUEY_PET, HUBERTE_INFO, HUBERTE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.HYDRA_PET, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Green Variant, other player
			new Pet(PetGroup.BOSS, NpcID.HYDRA_PET_ELECTRIC, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Blue Variant, other player
			new Pet(PetGroup.BOSS, NpcID.HYDRA_PET_FIRE, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Red Variant, other player
			new Pet(PetGroup.BOSS, NpcID.HYDRA_PET_EXTINGUISHED, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Grey Variant, other player
			new Pet(PetGroup.BOSS, NpcID.POH_HYDRA_PET, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Green
			new Pet(PetGroup.BOSS, NpcID.POH_HYDRA_PET_ELECTRIC, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Blue
			new Pet(PetGroup.BOSS, NpcID.POH_HYDRA_PET_FIRE, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Red
			new Pet(PetGroup.BOSS, NpcID.POH_HYDRA_PET_EXTINGUISHED, IKKLE_HYDRA_INFO, IKKLE_HYDRA_EXAMINE),	// Grey

			new Pet(PetGroup.BOSS, NpcID.POH_INFERNO_PET, JALNIBREK_INFO, JALNIBREK_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.INFERNO_PET, JALNIBREK_INFO, JALNIBREK_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_JADPET_INFERNO, JALREKJAD_INFO, JALREKJAD_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.JADPET_INFERNO, JALREKJAD_INFO, JALREKJAD_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.KQ_PET_FLYING, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.KQ_PET_WALKING, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_KQ_PET_FLYING, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_KQ_PET_WALKING, KALPHITE_PRINCESS_INFO, KALPHITE_PRINCESS_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.KITTENPET1, KITTEN_INFO, KITTEN_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.KITTENPET_LIGHT, KITTEN_INFO, KITTEN_EXAMINE),	// White	// Seen in game as other players, white kitten
			new Pet(PetGroup.OTHER, NpcID.KITTENPET_BROWN, KITTEN_INFO, KITTEN_EXAMINE),	// Checkered orange	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.KITTENPET_BLACK, KITTEN_INFO, KITTEN_EXAMINE),	// Black	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.KITTENPET_BROWNGREY, KITTEN_INFO, KITTEN_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.KITTENPET_BLUEGREY, KITTEN_INFO, KITTEN_EXAMINE),	// Grey and blue checkered	// Seen in game as other players

			new Pet(PetGroup.BOSS, NpcID.KRAKEN_PET, KRAKEN_INFO, KRAKEN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_KRAKEN_PET, KRAKEN_INFO, KRAKEN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ARMADYL_PET, KREEARRA_JR_INFO, KREEARRA_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_ARMADYL_PET, KREEARRA_JR_INFO, KREEARRA_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ZAMORAK_PET, KRIL_TSUTSAROTH_JR_INFO, KRIL_TSUTSAROTH_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_ZAMORAK_PET, KRIL_TSUTSAROTH_JR_INFO, KRIL_TSUTSAROTH_JR_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.LAZYCAT_LIGHT, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.LAZYCAT, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.LAZYCAT_BROWN, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.LAZYCAT_BLACK, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.LAZYCAT_BROWNGREY, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.LAZYCAT_BLUEGREY, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_DEFAULT, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_LIGHT, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_BROWN, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_BLACK, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_BROWNGREY, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_BLUEGREY, LAZY_CAT_INFO, LAZY_CAT_EXAMINE),	// Grey and blue checkered

			new Pet(PetGroup.OTHER, NpcID.LAZYCAT_HELL, LAZY_HELLCAT_INFO, LAZY_HELLCAT_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.POH_LAZYCAT_HELL, LAZY_HELLCAT_INFO, LAZY_HELLCAT_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_SOULWARS_PET_BLUE, LIL_CREATOR_INFO, LIL_CREATOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.SOULWARS_PET_BLUE, LIL_CREATOR_INFO, LIL_CREATOR_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_SOULWARS_PET_RED, LIL_CREATOR_INFO, LIL_DESTRUCTOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.SOULWARS_PET_RED, LIL_CREATOR_INFO, LIL_DESTRUCTOR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VERZIK_PET_BLOAT, LIL_ZIK_INFO, LIL_BLOAT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VERZIK_PET_BLOAT, LIL_ZIK_INFO, LIL_BLOAT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VERZIK_PET_MAIDEN, LIL_ZIK_INFO, LIL_MAIDEN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VERZIK_PET_MAIDEN, LIL_ZIK_INFO, LIL_MAIDEN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VERZIK_PET_NYLOCAS, LIL_ZIK_INFO, LIL_NYLO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VERZIK_PET_NYLOCAS, LIL_ZIK_INFO, LIL_NYLO_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_NIGHTMARE_PET_PARASITE, LITTLE_NIGHTMARE_INFO + LITTLE_PARASITE_INFO, LITTLE_PARASITE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.NIGHTMARE_PET_PARASITE, LITTLE_NIGHTMARE_INFO + LITTLE_PARASITE_INFO, LITTLE_PARASITE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VERZIK_PET_SOTETSEG, LIL_ZIK_INFO, LIL_SOT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VERZIK_PET_SOTETSEG, LIL_ZIK_INFO, LIL_SOT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_LEVIATHAN_PET, LIL_VIATHAN, LIL_VIATHAN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.LEVIATHAN_PET, LIL_VIATHAN, LIL_VIATHAN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VERZIK_PET_XARPUS, LIL_ZIK_INFO, LIL_XARP_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VERZIK_PET_XARPUS, LIL_ZIK_INFO, LIL_XARP_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VERZIK_PET, LIL_ZIK_INFO, LIL_ZIK_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VERZIK_PET, LIL_ZIK_INFO, LIL_ZIK_EXAMINE),	// Seen in game other player

			new Pet(PetGroup.BOSS, NpcID.POH_NIGHTMARE_PET, LITTLE_NIGHTMARE_INFO, LITTLE_NIGHTMARE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.NIGHTMARE_PET, LITTLE_NIGHTMARE_INFO, LITTLE_NIGHTMARE_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.MAZ, MAZ_INFO, MAZ_EXAMINE),	// Not a real pet, but close enough for me to want to add it

			new Pet(PetGroup.BOSS, NpcID.POH_DUSK_PET, MIDNIGHT_INFO, MIDNIGHT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DUSK_PET, MIDNIGHT_INFO, MIDNIGHT_EXAMINE),	// Seen in game other player, morphed to NOON_7892

			new Pet(PetGroup.BOSS, NpcID.POH_AMOXLIATL_PET, MOXI_INFO, MOXI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.AMOXLIATL_PET, MOXI_INFO, MOXI_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_MUSPAH_PET, MUPHIN_INFO + MUPHIN_RANGED, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_MUSPAH_PET_MELEE, MUPHIN_INFO + MUPHIN_MELEE, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_MUSPAH_PET_SHIELDED, MUPHIN_INFO + MUPHIN_SHIELDED, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUSPAH_PET, MUPHIN_INFO + MUPHIN_RANGED, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUSPAH_PET_MELEE, MUPHIN_INFO + MUPHIN_MELEE, MUPHIN_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.MUSPAH_PET_SHIELDED, MUPHIN_INFO + MUPHIN_SHIELDED, MUPHIN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_NEX_PET, NEXLING_INFO, NEXLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.NEX_PET, NEXLING_INFO, NEXLING_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_ARAXXOR_PET, NID_INFO, NID_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ARAXXOR_PET, NID_INFO, NID_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_DAWN_PET, NOON_INFO, NOON_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DAWN_PET, NOON_INFO, NOON_EXAMINE),	// Seen in game as other players, morphed to MIDNIGHT_7893

			new Pet(PetGroup.BOSS, NpcID.POH_OLM_PET, OLMLET_INFO, OLMLET_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.RAIDS_OLM_PET, OLMLET_INFO, OLMLET_EXAMINE),	// Seen in game other player

			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT_LIGHT, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT_BROWN, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT_BLACK, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT_BROWNGREY, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT_BLUEGREY, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and blue checkered	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_DEFAULT, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_LIGHT, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_BROWN, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_BLACK, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_BROWNGREY, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_BLUEGREY, OVERGROWN_CAT_INFO, OVERGROWN_CAT_EXAMINE),	// Grey and blue checkered

			new Pet(PetGroup.OTHER, NpcID.OVERGROWNCAT_HELL, OVERGROWN_HELLCAT_INFO, OVERGROWN_HELLCAT_EXAMINE),	// Seen in game as other players
			new Pet(PetGroup.OTHER, NpcID.POH_OVERGROWNCAT_HELL, OVERGROWN_HELLCAT_INFO, OVERGROWN_HELLCAT_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.DAGANNOTH_DUNGEON_PRESSURE_PET_ROCK, PET_ROCK_INFO, PET_ROCK_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.POH_ROCK, PET_ROCK_INFO, PET_ROCK_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_PENANCE_PET, PENANCE_PET_INFO, PENANCE_PET_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.PENANCE_PET, PENANCE_PET_INFO, PENANCE_PET_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_WC_PHEASANT, BEAVER_INFO + PHEASANT, PHEASANT_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_WC_PHEASANT, BEAVER_INFO + PHEASANT, PHEASANT_EXAMINE),


			new Pet(PetGroup.SKILLING, NpcID.POH_PHOENIX_PET_GREEN, PHOENIX_INFO, PHOENIX_EXAMINE),	// Green
			new Pet(PetGroup.SKILLING, NpcID.POH_PHOENIX_PET_BLUE, PHOENIX_INFO, PHOENIX_EXAMINE),	// Blue
			new Pet(PetGroup.SKILLING, NpcID.POH_PHOENIX_PET_WHITE , PHOENIX_INFO, PHOENIX_EXAMINE),	// White
			new Pet(PetGroup.SKILLING, NpcID.POH_PHOENIX_PET_PURPLE, PHOENIX_INFO, PHOENIX_EXAMINE),	// Purple
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_PET_GREEN, PHOENIX_INFO, PHOENIX_EXAMINE),	// Green
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_PET_BLUE, PHOENIX_INFO, PHOENIX_EXAMINE),	// Blue
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_PET_WHITE, PHOENIX_INFO, PHOENIX_EXAMINE),	// White
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_PET_PURPLE, PHOENIX_INFO, PHOENIX_EXAMINE),	// Purple
			new Pet(PetGroup.SKILLING, NpcID.POH_PHOENIX_PET, PHOENIX_INFO, PHOENIX_EXAMINE),	// Orange
			new Pet(PetGroup.SKILLING, NpcID.PHOENIX_PET, PHOENIX_INFO, PHOENIX_EXAMINE),	// Orange

			new Pet(PetGroup.BOSS, NpcID.POH_KBD_PET, PRINCE_BLACK_DRAGON_INFO, PRINCE_BLACK_DRAGON_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.KBD_PET, PRINCE_BLACK_DRAGON_INFO, PRINCE_BLACK_DRAGON_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_QUETZAL_PET, QUETZIN_INFO, QUETZIN_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.QUETZAL_PET, QUETZIN_INFO, QUETZIN_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_DOGADILE_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, PUPPADILE_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.DOGADILE_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, PUPPADILE_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.ARAXXOR_VAMPYRE_EASTER_EGG, NID_INFO + RAX_VARIANT,  RAX_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ARAXXOR_PET_CUTE, NID_INFO + RAX_VARIANT,  RAX_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_THIEVING_PANDA, ROCKY_INFO + " " + ROCKY_RED_PANDA, RED_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_THIEVING_PANDA, ROCKY_INFO + " " + ROCKY_RED_PANDA, RED_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_FIRE, RIFT_GUARDIAN_INFO + " This is the Fire variant.", RIFT_GUARDIAN_EXAMINE),	// Fire
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_AIR, RIFT_GUARDIAN_INFO + " This is the Air variant.", RIFT_GUARDIAN_EXAMINE),	// Air
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_MIND, RIFT_GUARDIAN_INFO + " This is the Mind variant.", RIFT_GUARDIAN_EXAMINE),	// Mind
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_WATER, RIFT_GUARDIAN_INFO + " This is the Water variant.", RIFT_GUARDIAN_EXAMINE),	// Water
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_EARTH, RIFT_GUARDIAN_INFO + " This is the Earth variant.", RIFT_GUARDIAN_EXAMINE),	// Earth
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_BODY, RIFT_GUARDIAN_INFO + " This is the Body variant.", RIFT_GUARDIAN_EXAMINE),	// Body
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_COSMIC, RIFT_GUARDIAN_INFO + " This is the Cosmic variant.", RIFT_GUARDIAN_EXAMINE),	// Cosmic
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_CHAOS, RIFT_GUARDIAN_INFO + " This is the Chaos variant.", RIFT_GUARDIAN_EXAMINE),	// Chaos
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_NATURE, RIFT_GUARDIAN_INFO + " This is the Nature variant.", RIFT_GUARDIAN_EXAMINE),	// Nature
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_LAW, RIFT_GUARDIAN_INFO + " This is the Law variant.", RIFT_GUARDIAN_EXAMINE),	// Law
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_DEATH, RIFT_GUARDIAN_INFO + " This is the Death variant.", RIFT_GUARDIAN_EXAMINE),	// Death
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_SOUL, RIFT_GUARDIAN_INFO + " This is the Soul variant.", RIFT_GUARDIAN_EXAMINE),	// Soul
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_ASTRAL, RIFT_GUARDIAN_INFO + " This is the Astral variant.", RIFT_GUARDIAN_EXAMINE),	// Astral
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_BLOOD, RIFT_GUARDIAN_INFO + " This is the Blood variant.", RIFT_GUARDIAN_EXAMINE),	// Blood
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_FIRE, RIFT_GUARDIAN_INFO + " This is the Fire variant.", RIFT_GUARDIAN_EXAMINE),	// fire
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_AIR, RIFT_GUARDIAN_INFO + " This is the Air variant.", RIFT_GUARDIAN_EXAMINE),	// Air
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_MIND, RIFT_GUARDIAN_INFO + " This is the Mind variant.", RIFT_GUARDIAN_EXAMINE),	// Mind
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_WATER, RIFT_GUARDIAN_INFO + " This is the Water variant.", RIFT_GUARDIAN_EXAMINE),	// Water
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_EARTH, RIFT_GUARDIAN_INFO + " This is the Earth variant.", RIFT_GUARDIAN_EXAMINE),	// Earth
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_BODY, RIFT_GUARDIAN_INFO + " This is the Body variant.", RIFT_GUARDIAN_EXAMINE),	// Body
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_COSMIC, RIFT_GUARDIAN_INFO + " This is the Cosmic variant.", RIFT_GUARDIAN_EXAMINE),	// Cosmic
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_CHAOS, RIFT_GUARDIAN_INFO + " This is the Chaos variant.", RIFT_GUARDIAN_EXAMINE),	// Chaos
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_NATURE, RIFT_GUARDIAN_INFO + " This is the Nature variant.", RIFT_GUARDIAN_EXAMINE),	// Nature
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_LAW, RIFT_GUARDIAN_INFO + " This is the Law variant.", RIFT_GUARDIAN_EXAMINE),	// Law
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_DEATH, RIFT_GUARDIAN_INFO + " This is the Death variant.", RIFT_GUARDIAN_EXAMINE),	// Death
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_SOUL, RIFT_GUARDIAN_INFO + " This is the Soul variant.", RIFT_GUARDIAN_EXAMINE),	// Soul
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_ASTRAL, RIFT_GUARDIAN_INFO + " This is the Astral variant.", RIFT_GUARDIAN_EXAMINE),	// Astral
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_BLOOD, RIFT_GUARDIAN_INFO + " This is the Blood variant.", RIFT_GUARDIAN_EXAMINE),	// Blood
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_RUNECRAFTING_WRATH, RIFT_GUARDIAN_INFO + " This is the Wrath variant.", RIFT_GUARDIAN_EXAMINE),	// Wrath
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_RUNECRAFTING_WRATH, RIFT_GUARDIAN_INFO + " This is the Wrath variant.", RIFT_GUARDIAN_EXAMINE),	// Wrath

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_AMETHYST, ROCK_GOLEM_INFO + " This is the Amethyst variant.", ROCK_GOLEM_EXAMINE),	// Amethyst
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_DEFAULT, ROCK_GOLEM_INFO + " This is the Rock variant.", ROCK_GOLEM_EXAMINE),	// Rock
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_TIN, ROCK_GOLEM_INFO + " This is the Tin variant.", ROCK_GOLEM_EXAMINE),	// Tin
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_COPPER, ROCK_GOLEM_INFO + " This is the Copper variant.", ROCK_GOLEM_EXAMINE),	// Copper
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_IRON, ROCK_GOLEM_INFO + " This is the Iron variant.", ROCK_GOLEM_EXAMINE),	// Iron
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_BLURITE, ROCK_GOLEM_INFO + " This is the Blurite variant.", ROCK_GOLEM_EXAMINE),	// Blurite
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_SILVER, ROCK_GOLEM_INFO + " This is the Silver variant.", ROCK_GOLEM_EXAMINE),	// Silver
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_COAL, ROCK_GOLEM_INFO + " This is the Coal variant.", ROCK_GOLEM_EXAMINE),	// Coal
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_GOLD, ROCK_GOLEM_INFO + " This is the Gold variant.", ROCK_GOLEM_EXAMINE),	// Gold
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_MITHRIL, ROCK_GOLEM_INFO + " This is the Mithril variant.", ROCK_GOLEM_EXAMINE),	// Mithril
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_GRANITE, ROCK_GOLEM_INFO + " This is the Granite variant.", ROCK_GOLEM_EXAMINE),	// Granite
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_ADAMANTITE, ROCK_GOLEM_INFO + " This is the Adamantite variant.", ROCK_GOLEM_EXAMINE),	// Adamantite
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_RUNITE, ROCK_GOLEM_INFO + " This is the Runite variant.", ROCK_GOLEM_EXAMINE),	// Runite
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_DEFAULT, ROCK_GOLEM_INFO + " This is the Rock variant.", ROCK_GOLEM_EXAMINE),	// Rock
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_TIN, ROCK_GOLEM_INFO + " This is the Tin variant.", ROCK_GOLEM_EXAMINE),	// Tin
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_COPPER, ROCK_GOLEM_INFO + " This is the Copper variant.", ROCK_GOLEM_EXAMINE),	// Copper
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_IRON, ROCK_GOLEM_INFO + " This is the Iron variant.", ROCK_GOLEM_EXAMINE),	// Iron
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_BLURITE, ROCK_GOLEM_INFO + " This is the Blurite variant.", ROCK_GOLEM_EXAMINE),	// Blurite
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_SILVER, ROCK_GOLEM_INFO + " This is the Silver variant.", ROCK_GOLEM_EXAMINE),	// Silver
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_COAL, ROCK_GOLEM_INFO + " This is the Coal variant.", ROCK_GOLEM_EXAMINE),	// Coal
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_GOLD, ROCK_GOLEM_INFO + " This is the Gold variant.", ROCK_GOLEM_EXAMINE),	// Gold
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_MITHRIL, ROCK_GOLEM_INFO + " This is the Mithril variant.", ROCK_GOLEM_EXAMINE),	// Mithril
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_GRANITE, ROCK_GOLEM_INFO + " This is the Granite variant.", ROCK_GOLEM_EXAMINE),	// Granite
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_ADAMANTITE, ROCK_GOLEM_INFO + " This is Adamantite fire variant.", ROCK_GOLEM_EXAMINE),	// Adamantite
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_RUNITE, ROCK_GOLEM_INFO + " This is the Runite variant.", ROCK_GOLEM_EXAMINE),	// Runite
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_AMETHYST, ROCK_GOLEM_INFO + " This is the Amethyst variant.", ROCK_GOLEM_EXAMINE),	// Amethyst
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_LOVAKITE, ROCK_GOLEM_INFO + " This is the Lovakite variant.", ROCK_GOLEM_EXAMINE),	// Lovakite
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_ELEMENTAL, ROCK_GOLEM_INFO + " This is the Elemental variant.", ROCK_GOLEM_EXAMINE),	// Elemental
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_MINING_DAEYALT, ROCK_GOLEM_INFO + " This is the Daeyalt variant.", ROCK_GOLEM_EXAMINE),	// Daeyalt
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_LOVAKITE, ROCK_GOLEM_INFO + " This is the Lovakite variant.", ROCK_GOLEM_EXAMINE),	// Lovakite variant, otherplayer
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_ELEMENTAL, ROCK_GOLEM_INFO + " This is the Elemental variant.", ROCK_GOLEM_EXAMINE),	// Elemental variant, other player
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_MINING_DAEYALT, ROCK_GOLEM_INFO + " This is the Daeyalt variant.", ROCK_GOLEM_EXAMINE),	// Daeyalt

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_THIEVING, ROCKY_INFO, ROCKY_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_THIEVING, ROCKY_INFO, ROCKY_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_SCORPIA_PET, SCORPIAS_OFFSPRING_INFO, SCORPIAS_OFFSPRING_EXAMINE),		// Usually even, what's up with this?
			new Pet(PetGroup.BOSS, NpcID.SCORPIAPET, SCORPIAS_OFFSPRING_INFO, SCORPIAS_OFFSPRING_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_SCURRIUS_PET, SCURRY_INFO, SCURRY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SCURRIUS_PET, SCURRY_INFO, SCURRY_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_SKOTIZO_PET, SKOTOS_INFO, SKOTOS_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SKOTIZO_PET, SKOTOS_INFO, SKOTOS_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.SMOKE_PET, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Yellow
			new Pet(PetGroup.BOSS, NpcID.POH_SMOKE_PET, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Yellow
			new Pet(PetGroup.BOSS, NpcID.POH_SMOKE_PET_OLD, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Blue
			new Pet(PetGroup.BOSS, NpcID.SMOKE_PET_OLD, SMOKE_DEVIL_INFO, SMOKE_DEVIL_EXAMINE),	// Blue

			new Pet(PetGroup.BOSS, NpcID.POH_SOLHEREDIT_PET, SMOL_HEREDIT_INFO, SMOL_HEREDIT_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SOLHEREDIT_PET, SMOL_HEREDIT_INFO, SMOL_HEREDIT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_ZALCANO_PET, SMOLCANO_INFO, SMOLCANO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ZALCANO_PET, SMOLCANO_INFO, SMOLCANO_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_SNAKE_PET_GREEN, SNAKELING_INFO, SNAKELING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_SNAKE_PET_ORANGE, SNAKELING_INFO, SNAKELING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_SNAKE_PET_BLUE, SNAKELING_INFO, SNAKELING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SNAKE_PET_GREEN, SNAKELING_INFO, SNAKELING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SNAKE_PET_ORANGE, SNAKELING_INFO, SNAKELING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SNAKE_PET_BLUE, SNAKELING_INFO, SNAKELING_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_MENAGERIE_SARACHNISPET, SRARACHA_INFO, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SARACHNISPET, SRARACHA_INFO, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_MENAGERIE_SARACHNISPET_ORANGE, SRARACHA_INFO + SRARACHA_ORANGE, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SARACHNISPET_ORANGE, SRARACHA_INFO + SRARACHA_ORANGE, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_MENAGERIE_SARACHNISPET_BLUE, SRARACHA_INFO + SRARACHA_BLUE, SRARACHA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.SARACHNISPET_BLUE, SRARACHA_INFO + SRARACHA_BLUE, SRARACHA_EXAMINE),

			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FARMING, TANGLEROOT_INFO, TANGLEROOT_EXAMINE),	// Acorn
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FARMING, TANGLEROOT_INFO + " This is the Acorn variant.", TANGLEROOT_EXAMINE),	// Acorn
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FARMING_CRYSTAL, TANGLEROOT_INFO + " This is the Crystal variant.", TANGLEROOT_EXAMINE),	// Crystal
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FARMING_DRAGON, TANGLEROOT_INFO + " This is the Dragon Fruit variant.", TANGLEROOT_EXAMINE),	// Dragon
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FARMING_HERB, TANGLEROOT_INFO + " This is the Guam variant.", TANGLEROOT_EXAMINE),	// Guam
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FARMING_LILY, TANGLEROOT_INFO + " This is the White Lily variant.", TANGLEROOT_EXAMINE),	// White Lily
			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_FARMING_REDWOOD, TANGLEROOT_INFO + " This is the Redwood variant.", TANGLEROOT_EXAMINE),	// Redwood
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FARMING_CRYSTAL, TANGLEROOT_INFO + " This is the Crystal variant.", TANGLEROOT_EXAMINE),	// Crystal
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FARMING_DRAGON, TANGLEROOT_INFO + " This is the Dragon Fruit variant.", TANGLEROOT_EXAMINE),	// Dragon Fruit Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FARMING_HERB, TANGLEROOT_INFO + " This is the Guam variant.", TANGLEROOT_EXAMINE),	// Guam Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FARMING_LILY, TANGLEROOT_INFO + " This is the White Lily variant.", TANGLEROOT_EXAMINE),	// White Lily Variant, other player
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_FARMING_REDWOOD, TANGLEROOT_INFO + " This is the Redwood variant.", TANGLEROOT_EXAMINE),	// Redwood Variant, other player

			new Pet(PetGroup.BOSS, NpcID.POH_TEKTON_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.TEKTON_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, TEKTINY_EXAMINE),

			new Pet(PetGroup.OTHER, NpcID.POH_TEMPOROSS_PET, TINY_TEMPOR_INFO, TINY_TEMPOR_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.TEMPOROSS_PET, TINY_TEMPOR_INFO, TINY_TEMPOR_EXAMINE),

			new Pet(PetGroup.TOY, NpcID.POH_TOY_DOLL_1OP, TOY_DOLL_INFO, TOY_DOLL_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.POH_TOY_DOLL_0OP, TOY_DOLL_INFO, TOY_DOLL_EXAMINE),

			new Pet(PetGroup.TOY, NpcID.POH_TOY_MOUSE_1OP, TOY_MOUSE_INFO, TOY_MOUSE_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.POH_TOY_MOUSE_0OP, TOY_MOUSE_INFO, TOY_MOUSE_EXAMINE),

			new Pet(PetGroup.TOY, NpcID.POH_TOY_SOLDIER_1OP, TOY_SOLDIER_INFO, TOY_SOLDIER_EXAMINE),
			new Pet(PetGroup.TOY, NpcID.POH_TOY_SOLDIER_0OP, TOY_SOLDIER_INFO, TOY_SOLDIER_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_JADPET, TZREKJAD_INFO, TZREKJAD_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.JADPET, TZREKJAD_INFO, TZREKJAD_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_ZUK_PET, JALNIBREK_INFO, JALNIBREK_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.ZUK_PET, JALNIBREK_INFO, JALNIBREK_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VANGUARD_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VANGUARD_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VANGUARD_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VANGUARD_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VASA_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VASA_MINIRIO_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VASA_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VASA_MINIRIO_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VENENATIS_PET, VENENATIS_SPIDERLING_INFO, VENENATIS_SPIDERLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VENENATISPET, VENENATIS_SPIDERLING_INFO, VENENATIS_SPIDERLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_VENENATIS_PET_LEGACY, VENENATIS_SPIDERLING_INFO + RETRO_VARIANT, VENENATIS_SPIDERLING_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VENENATISPET_LEGACY, VENENATIS_SPIDERLING_INFO + RETRO_VARIANT, VENENATIS_SPIDERLING_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VESPULA_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VESPULA_PET, OLMLET_INFO + OLMLET_CM_VARIANTS, VESPINA_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VETION_PET, VETION_JR_INFO, VETION_JR_EXAMINE),	// Purple
			new Pet(PetGroup.BOSS, NpcID.POH_VETION_PET_2, VETION_JR_INFO, VETION_JR_EXAMINE),	// Orange
			new Pet(PetGroup.BOSS, NpcID.VETIONPET, VETION_JR_INFO, VETION_JR_EXAMINE),	// Purple
			new Pet(PetGroup.BOSS, NpcID.VETIONPET_2, VETION_JR_INFO, VETION_JR_EXAMINE),	// Orange
			new Pet(PetGroup.BOSS, NpcID.POH_VETION_PET_LEGACY, VETION_JR_INFO, VETION_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_VETION_PET_2_LEGACY, VETION_JR_INFO, VETION_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VETIONPET_LEGACY, VETION_JR_INFO, VETION_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VETIONPET_2_LEGACY, VETION_JR_INFO, VETION_JR_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_VORKATH_PET, VORKI_INFO, VORKI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.VORKATH_PET, VORKI_INFO, VORKI_EXAMINE),	// Seen in game, other player

			new Pet(PetGroup.OTHER, NpcID.WILEYCAT_LIGHT, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.WILEYCAT, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILEYCAT_BROWN, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.WILEYCAT_BLACK, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.WILEYCAT_BROWNGREY, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.WILEYCAT_BLUEGREY, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and blue checkered
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_DEFAULT, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black and grey checkered
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_LIGHT, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// White
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_BROWN, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Checkered orange
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_BLACK, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_BROWNGREY, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Grey and Brown checkered
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_BLUEGREY, WILY_CAT_INFO, WILY_CAT_EXAMINE),	// Black and grey checkered

			new Pet(PetGroup.OTHER, NpcID.WILEYCAT_HELL, WILY_HELLCAT_INFO, WILY_HELLCAT_EXAMINE),
			new Pet(PetGroup.OTHER, NpcID.POH_WILEYCAT_HELL, WILY_HELLCAT_INFO, WILY_HELLCAT_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_WHISPERER_PET, WISP, WISP_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.WHISPERER_PET, WISP, WISP_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_YAMA_PET, YAMI_INFO, YAMI_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.YAMA_PET, YAMI_INFO, YAMI_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.POH_GAUNTLET_PET, YOUNGLLEF_INFO, YOUNGLLEF_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.GAUNTLET_PET, YOUNGLLEF_INFO, YOUNGLLEF_EXAMINE),


			new Pet(PetGroup.SKILLING, NpcID.POH_SKILLPET_THIEVING_TANUKI, ROCKY_INFO + " " + ROCKY_TANUKI, ZIGGY_EXAMINE),
			new Pet(PetGroup.SKILLING, NpcID.SKILLPET_THIEVING_TANUKI, ROCKY_INFO + " " + ROCKY_TANUKI, ZIGGY_EXAMINE),

			new Pet(PetGroup.BOSS, NpcID.SARADOMIN_PET, ZILYANA_JR_INFO, ZILYANA_JR_EXAMINE),
			new Pet(PetGroup.BOSS, NpcID.POH_SARADOMIN_PET, ZILYANA_JR_INFO, ZILYANA_JR_EXAMINE)

	};
}
