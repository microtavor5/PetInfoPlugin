package com.micro.petinfo.dataretrieval;

import com.google.common.collect.ImmutableMap;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PetInfo
{
	private Map<Integer, Pet> PETS;

	public PetInfo() {
		this.PETS = getMap();
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

	/**
	 * Builds a map of the possible pets from an XML file
	 */
	private Map<Integer, Pet> getMap() {
		ImmutableMap.Builder<Integer, Pet> builder = new ImmutableMap.Builder<>();

		List<SerializablePet> serializablePets;
		try {
			 serializablePets = deserializeFromXML();
		} catch (IOException e) {
			System.out.println("PetInfo: An error occurred while attempting to deserialize the data: " + e.toString());
			return builder.build();
		}

		for(SerializablePet pet : serializablePets) {
			if (pet.getNpcId() == 0 || pet.getInfo().isEmpty() || !petGroupExists(pet.getPetGroup()))
			{
				System.out.println("PetInfo: A pet's info was deserialized incorrectly: " + pet.toString());
				continue;
			}

			builder.put(pet.getNpcId(), new Pet(PetGroup.valueOf(pet.getPetGroup()), pet.getNpcId(), pet.getInfo()));
		}

		return builder.build();
	}

	private boolean petGroupExists(String group) {
		try {
			PetGroup.valueOf(group);
		} catch (Exception e) {
			return false;
		}

		return true;
	}

	private static List<SerializablePet> deserializeFromXML() throws IOException
	{
		FileInputStream fis = new FileInputStream("pets.xml");
		XMLDecoder decoder = new XMLDecoder(fis);
		List<SerializablePet> pets = (List<SerializablePet>) decoder.readObject();
		decoder.close();
		fis.close();
		return pets;
	}
}
