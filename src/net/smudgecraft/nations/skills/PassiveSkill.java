package net.smudgecraft.nations.skills;

import net.smudgecraft.nations.NationPlayer;
import net.smudgecraft.nations.Nations;

public abstract class PassiveSkill extends Skill
{	
	private int levelRequirement;
	
	public PassiveSkill(Nations plugin, String name)
	{
		super(plugin, name);
	}
	
	public abstract String getDescription(NationPlayer nationPlayer);
	
	public int getLevelRequirement()
	{
		return this.levelRequirement;
	}
	
	public int getLevel(NationPlayer nationPlayer)
	{
		int nlevel = nationPlayer.getLevel();
		
		if(nlevel<=levelRequirement)
			return 0;
		else
			return (nlevel-levelRequirement);
	}
		
	public void setLevelRequirement(int level)
	{
		this.levelRequirement=level;
	}
}
