package com.micro.petinfo.dataretrieval;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.RuneLite;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class PetInfo
{
	protected static final String localDir = RuneLite.RUNELITE_DIR + "/pet-info";
	protected static final String localJson = localDir + "/pets.json";

	private Map<Integer, Pet> PETS;

	public PetInfo(PetDataFetcher petDataFetcher, boolean useRemote) throws IOException {
		this.PETS = petDataFetcher.getMap(useRemote);
	}

	/**
	 * Returns the Pet enum if the passed NPCid is a pet, null if not
	 */
	public Pet findPet(int npcId)
	{
		return PETS.get(npcId);
	}

	/**
	 * Returns the info string for the passed NPCid
	 */
	public String getInfo(int npcId)
	{
		return PETS.get(npcId).info;
	}
}
