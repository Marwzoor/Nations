package net.smudgecraft.nations;

public class Experience 
{
	private int experience;
	private String name;
	
	public Experience(String name, int experience)
	{
		this.experience=experience;
		this.name=name;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public void setExperience(int experience)
	{
		this.experience=experience;
	}
	
	public int getExperience()
	{
		return this.experience;
	}
	
	public String getName()
	{
		return this.name;
	}
}
