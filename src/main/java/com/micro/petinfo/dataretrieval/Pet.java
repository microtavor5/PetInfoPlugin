package com.micro.petinfo.dataretrieval;

public class Pet
{
	public Pet(PetGroup petGroup, int npcId, String info, String examine) {
		this.petGroup = petGroup;
		this.npcId = npcId;
		this.info = info;
		this.examine = examine;
	}

	public final PetGroup petGroup;
	public final int npcId;
	public final String info;
	public final String examine;
}
