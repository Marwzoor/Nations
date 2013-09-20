package net.smudgecraft.nations;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.Biome;

public class Nation 
{
	private final String name;
	private List<NationSkill> skills;
	private List<Biome> origionBiomes;
	private double reducedPercent;
	
	public Nation(String name)
	{
		this.name = name;
		this.skills = new ArrayList<NationSkill>();
		this.origionBiomes=new ArrayList<Biome>();
		this.reducedPercent=1;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setBiomeExtraDamage(double extrapercent)
	{
		this.reducedPercent=extrapercent;
	}
	
	public boolean isOriginBiome(Biome biome)
	{
		return origionBiomes.contains(biome);
	}
	
	public double getOriginReducedDamage()
	{
		return this.reducedPercent;
	}
	
	public void addOriginBiome(Biome biome)
	{
		this.origionBiomes.add(biome);
	}
	
	public void removeOriginBiome(Biome biome)
	{
		this.origionBiomes.remove(biome);
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
