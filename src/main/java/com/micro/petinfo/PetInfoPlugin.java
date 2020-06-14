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

import javax.activity.ActivityRequiredException;
import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;


@PluginDescriptor(
		name = "Pet Highlight",
		description = "Highlights other players pets,shows the pets name and gives info about the pets."
)
public class PetInfoPlugin extends Plugin
{
	@Getter(AccessLevel.PACKAGE)
	private final List<NPC> pets = new ArrayList<>();

	@Getter(AccessLevel.PACKAGE)
	private final List<String> owners = new ArrayList<>();

	private final String EXAMINE = "Info";
	private final String OWNER = "Owner";

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
	public void onNpcChanged(NpcChanged npcCompositionChanged)
	{
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

	@Subscribe
	public void onClientTick(ClientTick clientTick)
	{
		if (!config.showMenu() || client.getGameState() != GameState.LOGGED_IN || client.isMenuOpen())
		{
			return;
		}

		addMenus();
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getMenuAction() == MenuAction.RUNELITE && event.getMenuOption().equals(EXAMINE))
		{
			event.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "The " + event.getMenuTarget() + " " + Pet.getInfo(event.getId()), "");
		}
		else if (event.getMenuAction() == MenuAction.RUNELITE && event.getMenuOption().equals(OWNER))
		{
			for (String name: owners)
			{
				System.out.println(name);
			}
			event.consume();
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "The owner of this " + event.getMenuTarget() + " is " + owners.get(event.getActionParam()), "");
		}
	}

	PetsConfig.PetMode showNpc(NPC npc)
	{
		Pet pet = Pet.findPet(npc.getId());
		if (pet == null)
		{
			return PetsConfig.PetMode.OFF;
		}

		return showPetGroup(pet.getPetGroup());
	}

	PetsConfig.PetMode showPetGroup(PetGroup petGroup)
	{
		switch (petGroup)
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

	private List<NPC> getPetsUnderCursor()
	{
		ArrayList<NPC> underCursor = new ArrayList<>();
		for (NPC npc : pets)
		{
			Shape npcHull = npc.getConvexHull();

			if (npcHull != null)
			{
				Point mouseCanvasPosition = client.getMouseCanvasPosition();
				if (npcHull.contains(mouseCanvasPosition.getX(), mouseCanvasPosition.getY()) && Pet.findPet(npc.getId()) != null)
				{
					underCursor.add(npc);
				}
			}
		}

		return underCursor;
	}

	private void addMenus()
	{
		List<NPC> petsUnderCursor = getPetsUnderCursor();
		if (!petsUnderCursor.isEmpty())
		{
			owners.clear();
			for (NPC pet : petsUnderCursor)
			{
				addOwnerMenuEntry(pet);
				addPetMenuEntry(pet);
			}
		}
	}

	private void addPetMenuEntry(NPC pet)
	{
		ChatMessageBuilder petNameColored = new ChatMessageBuilder().append(npcToColor(pet), pet.getName());

		final MenuEntry examine = new MenuEntry();
		examine.setOption(EXAMINE);
		examine.setTarget(petNameColored.build());
		examine.setType(MenuAction.RUNELITE.getId());
		examine.setIdentifier(pet.getId());

		MenuEntry[] newMenu = ObjectArrays.concat(client.getMenuEntries(), examine);
		client.setMenuEntries(newMenu);
	}

	private void addOwnerMenuEntry(NPC pet)
	{
		ChatMessageBuilder petNameColored = new ChatMessageBuilder().append(npcToColor(pet), pet.getName());

		Actor owner = pet.getInteracting();

		if(owner == null)
		{
			return;
		}
		owners.add(owner.getName());

		final MenuEntry examine = new MenuEntry();
		examine.setOption(OWNER);
		examine.setTarget(petNameColored.build());
		examine.setType(MenuAction.RUNELITE.getId());
		examine.setIdentifier(pet.getId());
		examine.setParam0(owners.indexOf(owner.getName()));

		MenuEntry[] newMenu = ObjectArrays.concat(client.getMenuEntries(), examine);
		client.setMenuEntries(newMenu);
	}
}
