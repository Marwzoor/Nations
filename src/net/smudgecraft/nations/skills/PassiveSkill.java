package net.smudgecraft.nations.skills;

import net.smudgecraft.nations.Nations;

public class PassiveSkill extends Skill
{	
	private int levelRequirement;
	
	public PassiveSkill(Nations plugin, String name)
	{
		super(plugin, name);
	}
	
	public int getLevelRequirement()
	{
		return this.levelRequirement;
	}
	
	public void setLevelRequirement(int level)
	{
		this.levelRequirement=level;
	}
}
