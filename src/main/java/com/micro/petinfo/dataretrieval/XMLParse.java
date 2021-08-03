package com.micro.petinfo.dataretrieval;

import com.google.common.collect.ImmutableMap;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import java.beans.XMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static jogamp.common.os.elf.SectionArmAttributes.Tag.File;

public class XMLParse
{
	private static final String URL = "https://raw.githubusercontent.com/microtavor5/PetInfoPlugin/http-xml-petinfo-retreval/pets.xml";
	@Inject
	private OkHttpClient okHttpClient;

	XMLParse() {

	}

	/**
	 * Builds a map of the possible pets from an XML file
	 */
	public Map<Integer, Pet> getMap() {
		ImmutableMap.Builder<Integer, Pet> builder = new ImmutableMap.Builder<>();

		List<SerializablePet> serializablePets;
		try {
			serializablePets = deserializeFromLocalXML();
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

	private List<SerializablePet> deserializeFromLocalXML() throws IOException
	{
		FileInputStream fis = new FileInputStream("pets.xml");
		XMLDecoder decoder = new XMLDecoder(fis);

		List<SerializablePet> pets = (List<SerializablePet>) decoder.readObject();
		decoder.close();
		fis.close();
		return pets;
	}

	private void getRemoteXML() throws IOException {
//		Request request = new Request.Builder().url(URL).cache().build();
//
//		Call call = okHttpClient.newCall(request);
//		Response response = call.execute();


	}
}
