package net.smudgecraft.nations;

import org.bukkit.entity.Player;

public class NationPlayer
{
	private Player player;
	private int level;
	private int experience;
	
	public NationPlayer(Player player)
	{
		this.player=player;
	}
	
	public void setExperience(int exp)
	{
		this.experience=exp;
	}
	
	public void addExperience(int exp)
	{
		this.experience+=exp;
		
		int remaining = getRemainingExperience();
		
		while(remaining<=0)
		{
			++this.level;
			
			this.experience=remaining*-1;
			
			remaining = getRemainingExperience();
		}
	}
	
	public void removeExperience(int exp)
	{
		this.experience-=exp;
		
		if(experience<0)
		{
			experience=0;
		}
	}
	
	public void setLevel(int level)
	{
		this.level=level;
	}
	
	public int getExperience()
	{
		return this.experience;
	}
	
	public Player getPlayer()
	{
		return this.player;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public int getRemainingExperience()
	{
		int exp = (int) (80 * Math.pow(Nations.getExpModifier(), this.level)) - experience;
		
		return exp;
	}
	
	public int getNeededExperience(int level)
	{
		int exp = (int) (80 * Math.pow(Nations.getExpModifier(), this.level));
		
		return exp;
	}
}
