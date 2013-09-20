package net.smudgecraft.nations;

import org.bukkit.configuration.ConfigurationSection;

public class NationSkill 
{
	private String name;
	private ConfigurationSection node;
	
	public NationSkill(String name, ConfigurationSection node)
	{
		this.name=name;
		this.node=node;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public ConfigurationSection getNode()
	{
		return this.node;
	}
}
