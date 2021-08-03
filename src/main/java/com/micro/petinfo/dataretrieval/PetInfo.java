package com.micro.petinfo.dataretrieval;

import com.google.common.collect.ImmutableMap;
import okhttp3.Call;
import okhttp3.Request;

import javax.inject.Inject;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PetInfo
{
	private Map<Integer, Pet> PETS;

	private XMLParse xmlParse;

	public PetInfo() {
		xmlParse = new XMLParse();
		this.PETS = xmlParse.getMap();
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
