package com.micro.petinfo.dataretrieval;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

@Slf4j
public class PetDataFetcher
{
	private final OkHttpClient okHttpClient;
	private Gson gson;
	private static final String URL = "https://raw.githubusercontent.com/microtavor5/PetInfoPlugin/http-xml-petinfo-retreval/pets.json";

	public PetDataFetcher(OkHttpClient ok, Gson gs)
	{
		okHttpClient = ok;
		gson = gs;
	}

	public Map<Integer, Pet> getMap(boolean useRemote) throws IOException
	{
		Reader reader;
		if(useRemote)
		{
			try
			{
				reader = getRemoteJson();
			}
			catch (Exception e)
			{
				log.error("Error with remote Json fetch: ", e);
				log.error("Attempting local Json retrieval");
				reader = new FileReader("pets.json");
			}
		}
		else {
			reader = new FileReader("pets.json");
		}

		// CHECKSTYLE:OFF
		Map<Integer, Pet> map = gson.fromJson(reader, new TypeToken<Map<Integer, Pet>>(){}.getType());
		// CHECKSTYLE:ON
		return map;
	}

	/* All network code goes in here. Only use hard coded URL's to approved domains */
	private Reader getRemoteJson() throws IOException
	{
		Request request = new Request.Builder().url(URL).build();
		Response response = okHttpClient.newCall(request).execute();
		if(!response.isSuccessful())
		{
			String errText = "An error occurred fetching remote json from " + URL + " HTTP response: " + response.code();
			response.close();
			throw new IOException(errText);
		}

//			if (response.networkResponse().code() == 304)
//				System.out.println("Was Cached");
		log.debug("Fetched json from remote source successfully");
		return response.body().charStream();
	}
}
