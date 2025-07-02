package com.micro.petinfo.dataretrieval;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.*;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class PetDataFetcher
{
	private final OkHttpClient okHttpClient;
	private final Gson gson;
	private static final String URL = "https://raw.githubusercontent.com/microtavor5/PetInfoPlugin/master/pets.json";

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
				reader = getRemoteJson().get();
			}
			catch (Exception e)
			{
				log.error("Error with remote Json fetch: ", e);
				log.error("Attempting local Json retrieval");
				reader = new FileReader(PetInfo.localJson);
			}
		}
		else {
			reader = new FileReader(PetInfo.localJson);
		}

		// CHECKSTYLE:OFF
		Map<Integer, Pet> map = gson.fromJson(reader, new TypeToken<Map<Integer, Pet>>(){}.getType());
		// CHECKSTYLE:ON
		return map;
	}

	/* All network code goes in here. Only use hard coded URL's to approved domains */
	private CompletableFuture<StringReader> getRemoteJson()
	{
		Request request = new Request.Builder().url(URL).build();

		CompletableFuture<StringReader> future = new CompletableFuture<>();
		okHttpClient.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call call, IOException e) {
				future.completeExceptionally(e);
			}

			@Override
			public void onResponse(Call call, Response response) throws IOException
			{
				try
				{
					ResponseBody responseBody = response.body();

					if (!response.isSuccessful() || responseBody == null)
					{
						String errText = "An error occurred fetching remote json from " + URL + " HTTP response: " + response.code();
						response.close();
						throw new IOException(errText);
					}

					String body = responseBody.string();

					int responseCode = getRelevantResponseCode(response);

					try
					{
						makeLocalBackup(body, responseCode == 304);
					} catch (IOException e) {
						log.error("[PetInfo]\tA local backup was needed but could not be created.");
					}

					response.close();
					future.complete(new StringReader(body));
				}
				catch (Exception e)
				{
					future.completeExceptionally(e);
				}
			}
		});

		return future;
	}

	private int getRelevantResponseCode(Response response)
	{
		if (response.networkResponse() == null)
		{
			return response.code();
		}

		return response.networkResponse().code();
	}

	private void makeLocalBackup(String body, boolean wasCashed) throws IOException
	{
		boolean isFirstLocalSave = makeLocalDir();
		boolean fileExists = new File(PetInfo.localJson).exists();
		if (isFirstLocalSave || !fileExists || !wasCashed)
		{
			FileWriter fr = new FileWriter(PetInfo.localJson);
			fr.write(body);
			fr.close();
		}
	}

	private boolean makeLocalDir()
	{
		File dir = new File(PetInfo.localDir);
		if(!dir.exists())
		{
			return dir.mkdir();
		}
		return false;
	}
}
