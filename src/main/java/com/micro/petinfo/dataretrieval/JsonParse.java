package com.micro.petinfo.dataretrieval;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.runelite.http.api.RuneLiteAPI;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class JsonParse
{
	private static final String URL = "https://raw.githubusercontent.com/microtavor5/PetInfoPlugin/http-xml-petinfo-retreval/pets.json";

	@Inject
	public JsonParse(OkHttpClient okHttpClient, Gson gson){

	}

	public Map<Integer, Pet> getMap() throws IOException
	{
		FileReader fr = new FileReader("pets.json");
		// CHECKSTYLE:OFF
		Map<Integer, Pet> map = RuneLiteAPI.GSON.fromJson(fr, new TypeToken<Map<Integer, Pet>>(){}.getType());
		// CHECKSTYLE:ON
		return map;
	}
}
