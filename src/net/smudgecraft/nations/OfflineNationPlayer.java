package net.smudgecraft.nations;

import org.bukkit.OfflinePlayer;

public class OfflineNationPlayer 
{
	private OfflinePlayer player;
	private int level;
	private int experience;
	private String nationName;
	
	public OfflineNationPlayer(OfflinePlayer player)
	{
		this.player=player;
	}
	
	public NationPlayer getNationPlayer()
	{
		if(player.isOnline())
		{
			return NationManager.getNationPlayer(player.getPlayer());
		}
		
		return null;
	}
	
	public void setNationName(String name)
	{
		this.nationName=name;
	}
	
	public String getNationName()
	{
		return this.nationName;
	}
	
	public boolean isOnline()
	{
		return player.isOnline();
	}
	
	public boolean isWhitelisted()
	{
		return player.isWhitelisted();
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
	
	public OfflinePlayer getPlayer()
	{
		return this.player;
	}
	
	public int getLevel()
	{
		return this.level;
	}
	
	public int getRemainingExperience()
	{
		int exp = (int) (80 * Math.pow(Nations.getExpModifier(), this.level+1)) - experience;
		
		return exp;
	}
	
	public int getNeededExperience(int level)
	{
		int exp = (int) (80 * Math.pow(Nations.getExpModifier(), this.level));
		
		return exp;
	}
}
