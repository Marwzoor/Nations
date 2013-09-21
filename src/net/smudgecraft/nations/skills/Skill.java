package net.smudgecraft.nations.skills;

import org.bukkit.configuration.ConfigurationSection;

import net.smudgecraft.nations.Nations;

public class Skill 
{
	private final String name;
	public Nations plugin;
	private String description;
	private ConfigurationSection node;
	
	public Skill(Nations instance, String name)
	{
		this.name=name;
		this.plugin=instance;
	}
	
	public ConfigurationSection getConfiguration()
	{
		return this.node;
	}
	
	public void setConfiguration(ConfigurationSection node)
	{
		this.node=node;
	}
	
	public void setDescription(String description)
	{
		this.description=description;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
}
