package net.smudgecraft.nations;

import java.util.ArrayList;
import java.util.List;

import net.smudgecraft.nations.skills.Skill;

public class Nation 
{
	private final String name;
	private List<Skill> skills;
	
	public Nation(String name)
	{
		this.name = name;
		this.skills=new ArrayList<Skill>();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public Skill getSkill(String skillName)
	{
		for(Skill skill : skills)
		{
			if(skill.getName().equalsIgnoreCase(skillName))
				return skill;
		}
		
		return null;
	}
	
	public void addSkill(Skill skill)
	{
		skills.add(skill);
	}
	
	public List<Skill> getSkills()
	{
		return this.skills;
	}
	
	public void removeNationSkill(Skill skill)
	{
		skills.remove(skill);
	}
	
	public void removeNationSkill(String skill)
	{
		if(getSkill(skill)!=null)
			skills.remove(getSkill(skill));
	}
}
