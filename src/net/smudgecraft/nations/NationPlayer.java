package net.smudgecraft.nations;

import net.smudgecraft.nations.events.ChangeLevelEvent;
import net.smudgecraft.nations.skills.PassiveSkill;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NationPlayer
{
	private Player player;
	private int level;
	private int experience;
	private Nation nation;
	
	public NationPlayer(Player player)
	{
		this.player=player;
	}
	
	public boolean hasAccessToSkill(PassiveSkill skill)
	{
		if(nation == null || nation.getSkill(skill.getName())==null)
			return false;
		
		int levelRequirement = skill.getLevelRequirement();
		
		if(level<levelRequirement)
			return false;
		
		return true;
	}
	
	public void setExperience(int exp)
	{
		this.experience=exp;
	}
	
	public void addExperience(int exp)
	{
		int futureExperience = this.experience + exp;
		
		int futureLevel = this.level;
		
		int remaining = getRemainingExperience(futureLevel, futureExperience);
		
		while(remaining<=0)
		{
			++futureLevel;
			
			futureExperience=remaining*-1;
			
			remaining = getRemainingExperience(futureLevel, futureExperience);
		}
		
		if(this.level!=futureLevel)
		{
			ChangeLevelEvent clEvent = new ChangeLevelEvent(this, this.nation.getName(), this.level, futureLevel, this.experience, futureExperience);
		
			Bukkit.getPluginManager().callEvent(clEvent);
		
			if(clEvent.isCancelled())
			{
				return;
			}
			
			this.level=clEvent.getTo();
			this.experience=clEvent.getToExperience();
			
			return;
		}
		
		this.experience+=exp;
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
		int exp = (int) (20 * Math.pow(NationManager.EXP_MODIFIER, this.level)) - experience;
		
		return exp;
	}
	
	public int getRemainingExperience(int level, int experience)
	{
		int exp = (int) (20 * Math.pow(NationManager.EXP_MODIFIER, level)) - experience;
		
		return exp;
	}
	
	public int getNeededExperience(int level)
	{
		int exp = (int) (20 * Math.pow(Nations.getExpModifier(), this.level));
		
		return exp;
	}
	
	public void setNation(Nation nation)
	{
		this.nation = nation;
	}
	
	public Nation getNation()
	{
		return nation;
	}
	
}
