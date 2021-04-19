package com.micro.petinfo.dataretrieval;

public class Pet
{
	public Pet(PetGroup petGroup, int npcId, String info) {
		this.petGroup = petGroup;
		this.npcId = npcId;
		this.info = info;
	}

	public final PetGroup petGroup;
	public final int npcId;
	public final String info;
}
