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

import com.google.common.collect.ObjectArrays;
import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.events.*;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import javax.inject.Inject;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


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
	private final List<String> owners = new ArrayList<>();	// Used to keep track of the owners of the pets under the menu,
															// the ActionParam of an OWNER event corresponds to an index in this array

	private final String INFO = "Info";
	private final String OWNER = "Owner";

	@Getter(AccessLevel.PACKAGE)
	private final Color defaultYellow = new Color(0xffff00);

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private PetsOverlay overlay;

	@Inject
	private PetsConfig config;

	@Provides
	PetsConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(PetsConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		owners.clear();
		pets.clear();
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned npcSpawned)
	{
		NPC npc = npcSpawned.getNpc();
		Pet pet = Pet.findPet(npc.getId());

		if (pet != null)
		{
			pets.add(npc);
		}
	}

	@Subscribe
	public void onNpcChanged(NpcChanged npcCompositionChanged)	// Do pet's compositions ever change? If they do we need to handle if they ever stop being pets,
	{															// if they don't we don't need this at all. I think this may have cause the highlight with no pet issue.
		NPC npc = npcCompositionChanged.getNpc();
		Pet pet = Pet.findPet(npc.getId());

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
			owners.clear();
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
		// Ensure we are going to be showing the menus, that a menu can be show at all, or an existing menu is not already open
		if (!config.showMenu() || client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
		{
			return;
		}

		addMenus();
	}

	/**
	 * Prints to chat the appropriate text for the given menu
	 */
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		// If the player clicks on an INFO menu entry
		if (event.getMenuAction() == MenuAction.RUNELITE && event.getMenuOption().equals(INFO))
		{
			event.consume();
			// We get the info text based off of the pet's NPCid
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "The " + event.getMenuTarget() + " " + Pet.getInfo(event.getId()), "");
			return;
		}

		// If the player clicks on an OWNER menu entry
		if (event.getMenuAction() == MenuAction.RUNELITE && event.getMenuOption().equals(OWNER))
		{
			event.consume();
			// We get the owner's name from the owners array. The events ActionParam is the index in the owners array for the owner in question.
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "The owner of this " + event.getMenuTarget() + " is " + owners.get(event.getActionParam()), "");
		}
	}

	/**
	 * Finds the {@link com.micro.petinfo.PetsConfig.PetMode} of a given NPC.
	 */
	PetsConfig.PetMode showNpc(NPC npc)
	{
		Pet pet = Pet.findPet(npc.getId());
		if (pet == null)
		{
			return PetsConfig.PetMode.OFF;
		}

		switch (pet.getPetGroup())
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
		Pet pet = Pet.findPet(npc.getId());
		if (pet == null)
		{
			return null;
		}

		switch (pet.getPetGroup())
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
	 * Finds which pets are under the users cursor.
	 * This consults the {@link #pets} array.
	 */
	private List<NPC> getPetsUnderCursor()
	{
		Point mouseCanvasPosition = client.getMouseCanvasPosition();
		if (!mouseIsBlocked())
		{
			List<NPC> list = new ArrayList<>();
			for (NPC p : pets)
			{
				if (isClickable(p, mouseCanvasPosition))
				{
					list.add(p);
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
		for (MenuEntry menuEntry : client.getMenuEntries())
		{
			if (menuEntry.getType() == MenuAction.WALK.getId())
			{
				list.add(menuEntry);
			}
		}
		return list.isEmpty();
	}

	private boolean isClickable(NPC npc, Point mouseCanvasPosition)
	{
		Shape npcHull = npc.getConvexHull();	// Gets a Shape representing an outline of the npc

		if (npcHull != null)
		{
			// Determine if the cursor is inside of the outline of the pet
			return npcHull.contains(mouseCanvasPosition.getX(), mouseCanvasPosition.getY());
		}

		return false;
	}

	/**
	 * Adds the menus for the the pets that are under the cursor at the time of the call.
	 */
	private void addMenus()
	{
		List<NPC> petsUnderCursor = getPetsUnderCursor();
		if (!petsUnderCursor.isEmpty())
		{
			owners.clear();    // Clear the owners array of old owners
			for (NPC pet : petsUnderCursor)
			{
				// Owner first because the menu options are FILO
				addPetOwnerMenu(pet);
				addPetInfoMenu(pet);
			}
		}
	}

	private void addPetInfoMenu(NPC pet)
	{
		ChatMessageBuilder petNameColored = new ChatMessageBuilder().append(npcToColor(pet), pet.getName());

		final MenuEntry info = buildPetInfoMenu(pet);

		MenuEntry[] newMenu = ObjectArrays.concat(client.getMenuEntries(), info);
		client.setMenuEntries(newMenu);
	}

	private void addPetOwnerMenu(NPC pet)
	{
		Actor owner = pet.getInteracting();	// Pets are always interacting with their owner. I think.

		if(owner != null && (owner != client.getLocalPlayer() || !config.showOwnerOnOwnPet()))
		{
			String ownerName = colorOwnerName(owner);
			owners.add(ownerName);	// Add the owners name to the array for later retrieval

			final MenuEntry examine = buildPetOwnerMenu(pet, owners.indexOf(ownerName));

			MenuEntry[] newMenu = ObjectArrays.concat(client.getMenuEntries(), examine);
			client.setMenuEntries(newMenu);
		}
	}

	/**
	 * Adds an info menu for the given pet.
	 * The {@link MenuEntry} is built as follows:
	 * 	* {@link MenuEntry#setOption(String)}: is {@link #INFO}
	 * 	* {@link MenuEntry#setTarget(String)}: is the pets name, colored to match {@link #npcToColor(NPC)}
	 * 	* {@link MenuEntry#setType(int)}: is {@link MenuAction#RUNELITE}
	 * 	* {@link MenuEntry#setIdentifier(int)}: is the pets NPCid
	 */
	private MenuEntry buildPetInfoMenu(NPC pet)
	{
		String petName = colorPetName(pet);

		final MenuEntry info = new MenuEntry();
		info.setOption(INFO);
		info.setTarget(petName);
		info.setType(MenuAction.RUNELITE.getId());
		info.setIdentifier(pet.getId());

		return  info;
	}

	/**
	 * Adds an owner menu for the given pet.
	 * The {@link MenuEntry} is built as follows:
	 * 	* {@link MenuEntry#setOption(String)}: is {@link #OWNER}
	 * 	* {@link MenuEntry#setTarget(String)}: is the pets name, colored to match {@link #npcToColor(NPC)}
	 * 	* {@link MenuEntry#setType(int)}: is {@link MenuAction#RUNELITE}
	 * 	* {@link MenuEntry#setIdentifier(int)}: is the pets NPCid
	 * 	* {@link MenuEntry#setParam0(int)}: is the index in {@link #owners} represented by the pet's owner's name
	 */
	private MenuEntry buildPetOwnerMenu(NPC pet, int ownerIndex)
	{
		String petName = colorPetName(pet);

		final MenuEntry ownerMenu = new MenuEntry();
		ownerMenu.setOption(OWNER);
		ownerMenu.setTarget(petName);
		ownerMenu.setType(MenuAction.RUNELITE.getId());
		ownerMenu.setIdentifier(pet.getId());
		ownerMenu.setParam0(ownerIndex);	// This becomes the MenuOptionClicked's ActionParam somehow.

		return ownerMenu;
	}

	private String colorOwnerName(Actor owner)
	{
		Actor player = client.getLocalPlayer();

		if (player == null) {
			return colorChatString(defaultYellow, owner.getName());
		}

		Color colorFromLevel = getNameColorFromCombatLevels(
				client.getLocalPlayer().getCombatLevel(),
				owner.getCombatLevel()
		);

		return colorChatString(colorFromLevel, owner.getName());
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
