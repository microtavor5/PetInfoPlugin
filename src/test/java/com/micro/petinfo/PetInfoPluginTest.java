package com.micro.petinfo;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class PetInfoPluginTest
{
	public static void main(String[] args) throws Exception
	{
		try
		{
			ExternalPluginManager.loadBuiltin(PetInfoPlugin.class);
			RuneLite.main(args);
		}
		catch (Exception e)
		{
			System.out.println("Exception occured: " + e);
		}
	}
}