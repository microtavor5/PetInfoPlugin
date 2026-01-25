/* Copyright (c) 2020 by micro
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Portions of the code are based off of the "Implings" RuneLite plugin.
 * The "Implings" is:
 * Copyright (c) 2017, Robin <robin.weymans@gmail.com>
 * All rights reserved.
 *
 * Portions of the code are based off of the "Menu Entry Swapper" RuneLite plugin.
 * The "Menu Entry Swapper" is:
 * Copyright (c) 2018, Adam <Adam@sigterm.info>
 * Copyright (c) 2018, Kamiel
 * Copyright (c) 2019, Rami <https://github.com/Rami-J>
 * All rights reserved.
 */

package com.micro.petinfo;

import com.google.gson.Gson;
import com.google.inject.Provides;
import com.micro.petinfo.dataretrieval.PetDataFetcher;
import com.micro.petinfo.dataretrieval.Pet;
import com.micro.petinfo.dataretrieval.PetGroup;
import com.micro.petinfo.dataretrieval.PetInfo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Menu;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.events.*;
import net.runelite.api.geometry.SimplePolygon;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.outline.ModelOutlineRenderer;
import okhttp3.OkHttpClient;

import javax.inject.Inject;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@PluginDescriptor(
		name = "Pet Info",
		description = "Highlights other players pets,shows the pets name and gives info about the pets.",
		tags = {"pet"}
)
public class PetInfoPlugin extends Plugin
{
	// Use these two arrays instead of cachedNPC index to guard against despawn race-condition
	@Getter(AccessLevel.PACKAGE)
	private final List<NPC> pets = new ArrayList<>();	// Used to keep track of the current pets in the game

	@Getter(AccessLevel.PACKAGE)
	private final Color defaultWhite = new Color(0xffffff);

	@Getter(AccessLevel.PACKAGE)
	private final Color defaultYellow = new Color(0xffff00);

	@Inject
	private ModelOutlineRenderer modelOutlineRenderer;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PetsOverlay overlay;

	@Inject
	private PetsConfig config;

	@Inject
	private OkHttpClient okHttpClient;

	@Inject
	private Gson gson;

	private PetInfo petInfo;

	private final String MENU_OPTION_INFO = "Info";
	private final String MENU_OPTION_EXAMINE = "Examine";

	@Provides
	PetsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PetsConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);

		try
		{
			petInfo = new PetInfo(new PetDataFetcher(okHttpClient, gson), config.getRemoteData());
		}
		catch (IOException e)
		{
			log.error("[Pet-Info]\tCould not load pet data... Shutting plugin down...");
			shutDown();
		}
	}

	@Override
	protected void shutDown()
	{
		pets.clear();
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		Pet pet = petInfo.findPet(npc.getId());

		if (pet != null)
		{
			pets.add(npc);
		}
	}

	@Subscribe
	public void onNpcChanged(NpcChanged npcCompositionChanged)	// Do pet's compositions ever change? If they do we need to handle if they ever stop being pets,
	{															// if they don't we don't need this at all. I think this may have cause the highlight with no pet issue.
		NPC npc = npcCompositionChanged.getNpc();
		Pet pet = petInfo.findPet(npc.getId());

		if (pet != null)
		{
			if (!pets.contains(npc))
			{
				pets.add(npc);
			}
		}
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGIN_SCREEN || event.getGameState() == GameState.HOPPING)
		{
			pets.clear();
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned npcDespawned)
	{
		if (pets.isEmpty())
		{
			return;
		}

		NPC npc = npcDespawned.getNpc();
		pets.remove(npc);
	}

	/**
	 * Add the menus for the pets, must be done each tick or else they would disappear on the next tick.
	 * This is not done in onMenuOpened because the entries are visibly added after the fact.
	 */
	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		// Ensure we are going to be showing the menus, that a menu can be shown at all, or an existing menu is not already open
		if (config.menu() == PetsConfig.MenuMode.OFF || client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
		{
			return;
		}
		addMenus();
	}

	/**
	 * Prints to chat the appropriate text for the given menu
	 */
	public void onMenuOptionClicked(MenuEntry event)
	{
		// If the player did not click on an INFO menu entry (hopefully should lever happen with new api)
		if (event.getType() != MenuAction.RUNELITE)
		{
			log.error("[Pet-Info]\tSomehow got an incorrect menu entry?" + event);
			return;
		}

		if (config.menu() == PetsConfig.MenuMode.INFO && !(event.getOption().startsWith("Info")))
		{
			log.error("[Pet-Info]\tSomehow got an incorrect menu entry?" + event);
			return;
		}

		if (config.menu() == PetsConfig.MenuMode.EXAMINE && !(event.getOption().startsWith("Examine")))
		{
			log.error("[Pet-Info]\tSomehow got an incorrect menu entry?" + event);
			return;
		}

		if (config.menu() == PetsConfig.MenuMode.BOTH &&
				!(event.getOption().startsWith("Info")) && !(event.getOption().startsWith("Examine")))
		{
			log.error("[Pet-Info]\tSomehow got an incorrect menu entry?" + event);
			return;
		}

		// We get the info text based off of the pet's NPCid
		if (config.menu() == PetsConfig.MenuMode.INFO)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "The " + event.getTarget()
					+ " " + petInfo.getInfo(event.getIdentifier()), "");
		}
		else if (config.menu() == PetsConfig.MenuMode.EXAMINE)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", petInfo.getExamine(event.getIdentifier()), "");
		}
		else if (config.menu() == PetsConfig.MenuMode.BOTH)
		{
			if (event.getOption().startsWith("Info"))
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "The " + event.getTarget()
						+ " " + petInfo.getInfo(event.getIdentifier()), "");
			}
			else
			{
				client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", petInfo.getExamine(event.getIdentifier()), "");
			}
		}
	}

	/**
	 * Finds the {@link com.micro.petinfo.PetsConfig.PetMode} of a given NPC.
	 */
	PetsConfig.PetMode showNpc(NPC npc)
	{
		Pet pet = petInfo.findPet(npc.getId());
		if (pet == null)
		{
			return PetsConfig.PetMode.OFF;
		}

		switch (pet.petGroup)
		{
			case BOSS:
				return config.showBoss();
			case SKILLING:
				return config.showSkilling();
			case TOY:
				return config.showToy();
			case OTHER:
				return config.showOther();
			default:
				return PetsConfig.PetMode.OFF;
		}
	}

	/**
	 * Finds the color a given NPCs {@link PetGroup} should be displayed in
	 * @param npc the npc to get the color of
	 * @return the color of the npc based on the pet group
	 */
	Color npcToColor(NPC npc)
	{
		Pet pet = petInfo.findPet(npc.getId());
		if (pet == null)
		{
			return null;
		}

		switch (pet.petGroup)
		{
			case BOSS:
				return config.getBossColor();
			case SKILLING:
				return config.getSkillingColor();
			case TOY:
				return config.getToyColor();
			case OTHER:
				return config.getOtherColor();
			default:
				return null;
		}
	}

	/**
	 * Finds which pets are under the user's cursor.
	 * This consults the {@link #pets} array.
	 */
	private List<NPC> getPetsUnderCursor()
	{
		Point mouseCanvasPosition = client.getMouseCanvasPosition();
		if (!mouseIsBlocked())
		{
			List<NPC> list = new ArrayList<>();

			WorldView wv = client.getTopLevelWorldView();
			if (wv == null) {
				return list;
			}

			for (NPC pet : pets)
			{
				if (isClickable(pet, mouseCanvasPosition, wv))
				{
					list.add(pet);
				}
			}
			return list;
		}

		return Collections.emptyList();
	}

	/**
	 * Hacky way to see if the mouse is blocked;
	 * check to see if you can walk to the mouse position.
	 * This is instead of building a tree of widgets to search
	 */
	private boolean mouseIsBlocked()
	{
		List<MenuEntry> list = new ArrayList<>();
		Menu menu = client.getMenu();
		for (MenuEntry menuEntry : menu.getMenuEntries())
		{
			if (menuEntry.getType() == MenuAction.WALK)
			{
				list.add(menuEntry);
			}
		}
		return list.isEmpty();
	}

	private boolean isClickable(NPC npc, Point mouseCanvasPosition, WorldView wv)
	{
		// First calculate the NPC's local X Y Z coordinates
		NPCComposition transformedComposition = npc.getTransformedComposition();
		if(transformedComposition == null)
		{
			return false;
		}

		int size = transformedComposition.getSize();


		LocalPoint lp = npc.getLocalLocation();
		if (lp == null)
		{
			return false;
		}


		// NPCs z position are calculated based on the tile height of the northeastern tile
		final int northEastX = lp.getX() + Perspective.LOCAL_TILE_SIZE * (size - 1) / 2;
		final int northEastY = lp.getY() + Perspective.LOCAL_TILE_SIZE * (size - 1) / 2;
		final LocalPoint northEastLp = new LocalPoint(northEastX, northEastY, wv);
		if (northEastLp == null)
		{
			return false;
		}

		int height = Perspective.getTileHeight(client, northEastLp, wv.getPlane());

		Model npc_model = npc.getModel();
		if (npc_model == null)
		{
			return false;
		}

        SimplePolygon aabb = RLUtils.calculateAABB(client, npc_model, npc.getCurrentOrientation(), lp.getX(), lp.getY(), height);
		if (aabb == null)
		{
			return false;
		}


		// Check the bounding box to see if the mouse is anywhere near the NPC before doing the expensive check
		if (aabb.contains(mouseCanvasPosition.getX(), mouseCanvasPosition.getY()))
		{
			// If we don't want to do the expensive check
			if (config.getSkipConvexHull())
			{
				return true;
			}
			// We want to do the more precise (but expensive) check
			else {
				Shape npcHull = npc.getConvexHull();    // Gets a Shape representing an outline of the npc
				if (npcHull != null) {
					// Determine if the cursor is inside the outline of the NPC
					return npcHull.contains(mouseCanvasPosition.getX(), mouseCanvasPosition.getY());
				}
			}
		}

		return false;
	}

	/**
	 * Adds the menus for the pets that are under the cursor at the time of the call.
	 */
	private void addMenus()
	{
		if (pets.isEmpty())
		{
			return;
		}
		List<NPC> petsUnderCursor = getPetsUnderCursor();
		for (NPC pet : petsUnderCursor)
		{
			// Add info menu
			if (config.menu().equals(PetsConfig.MenuMode.INFO) || config.menu().equals(PetsConfig.MenuMode.BOTH))
			{
				addPetMenu(pet, MENU_OPTION_INFO);
			}

			// Add examine menu
			if (config.menu().equals(PetsConfig.MenuMode.EXAMINE) || config.menu().equals(PetsConfig.MenuMode.BOTH))
			{
				if (pet.getInteracting() == client.getLocalPlayer()) {
					return;
				}
				else
				{
					addPetMenu(pet, MENU_OPTION_EXAMINE);
				}
			}
		}
	}

	private void addPetMenu(NPC pet, String option)
	{
		if(pet.getInteracting() != null && config.showPetOwner())
		{
			option += " " + colorOwnerName(pet.getInteracting()) + "'s";
		}

		Menu menu = client.getMenu();

		menu.createMenuEntry(0)
				.setOption(option)
				.setTarget(colorPetName(pet))
				.setType(MenuAction.RUNELITE)
				.setIdentifier(pet.getId())
				.onClick(this::onMenuOptionClicked);
	}

	private String colorOwnerName(Actor owner)
	{
		Color ownerColor;

		switch (config.petOwnerColor())
		{
			case WHITE:
				ownerColor = defaultWhite;
				break;

			case YELLOW:
				ownerColor = defaultYellow;
				break;

			case COMBAT:
				Actor player = client.getLocalPlayer();

				if (player == null) {
					ownerColor = defaultYellow;
					log.error("[Pet-Info]\tLocal player was null. Defaulting to yellow menu color.");
				}
				else {
					ownerColor = getNameColorFromCombatLevels(
						player.getCombatLevel(),
						owner.getCombatLevel()
					);
				}
				break;

			default:
				return  owner.getName();
		}

		return colorChatString(ownerColor, owner.getName());
	}

	private String colorPetName(NPC pet)
	{
		String petName = pet.getName();
		switch (config.petInfoColor())
		{
			case HIGHLIGHT:
			{
				return colorChatString(npcToColor(pet), petName);
			}
			case YELLOW:
			default:
			{
				return colorChatString(defaultYellow, petName);
			}
		}

	}

	private String colorChatString(Color color, String name)
	{
		ChatMessageBuilder petNameColored = new ChatMessageBuilder().append(color, name);
		return petNameColored.build();
	}

	private Color getNameColorFromCombatLevels(int localPlayerLevel, int otherPlayerLevel)
	{
		int delta = localPlayerLevel - otherPlayerLevel;

		if (delta < -9) {
			return new Color(0xff0000);
		}
		if (delta < -6) {
			return new Color(0xff3000);
		}
		if (delta < -3) {
			return new Color(0xff7000);
		}
		if (delta < 0) {
			return new Color(0xffb000);
		}
		if (delta > 9) {
			return new Color(0xff00);
		}
		if (delta > 6) {
			return new Color(0x40ff00);
		}
		if (delta > 3) {
			return new Color(0x80ff00);
		}
		if (delta > 0) {
			return new Color(0xc0ff00);
		}
		return new Color(0xffff00);
	}

}
