package net.smudgecraft.nations;

import java.util.ArrayList;
import java.util.List;

public class Nation 
{
	private final String name;
	private List<NationSkill> skills;
	
	public Nation(String name)
	{
		this.name = name;
		this.skills = new ArrayList<NationSkill>();
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public NationSkill getNationSkill(String skill)
	{
		for(NationSkill nSkill : skills)
		{
			if(nSkill.getName().equalsIgnoreCase(skill))
				return nSkill;
		}
		
		return null;
	}
	
	public void addNationSkill(NationSkill nationSkill)
	{
		skills.add(nationSkill);
	}
	
	public List<NationSkill> getNationSkills()
	{
		return this.skills;
	}
	
	public void removeNationSkill(NationSkill nationSkill)
	{
		skills.remove(nationSkill);
	}
	
	public void removeNationSkill(String skill)
	{
		if(getNationSkill(skill)!=null)
			skills.remove(getNationSkill(skill));
	}
}
