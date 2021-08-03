package com.micro.petinfo.dataretrieval;

import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Map;

@Slf4j
public class PetInfo
{
	private Map<Integer, Pet> PETS;


	public PetInfo(PetDataFetcher petDataFetcher, boolean useRemote) {
		try
		{
			this.PETS = petDataFetcher.getMap(useRemote);
		}
		catch (IOException e)
		{
			log.error("Error getting Pets: ", e);
			log.error("Shutting plugin down...");
		}
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
