package com.micro.petinfo.dataretrieval;

public class SerializablePet
{
	private String petGroup;
	private int npcId;
	private String info;

	public SerializablePet(){

	}

	public SerializablePet(String petGroup, int npcId, String info) {
		this.petGroup = petGroup;
		this.npcId = npcId;
		this.info = info;
	}

	public String getPetGroup()
	{
		return petGroup;
	}

	public void setPetGroup(String petGroup)
	{
		this.petGroup = petGroup;
	}

	public int getNpcId()
	{
		return npcId;
	}

	public void setNpcId(int npcId)
	{
		this.npcId = npcId;
	}

	public String getInfo()
	{
		return info;
	}

	public void setInfo(String info)
	{
		this.info = info;
	}
}
