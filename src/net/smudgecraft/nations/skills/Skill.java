package net.smudgecraft.nations.skills;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;

import net.smudgecraft.nations.Nations;

public abstract class Skill 
{
	private final String name;
	public Nations plugin;
	private String description;
	private ConfigurationSection node;
	
	public Skill(Nations instance, String name)
	{
		this.description="";
		this.name=name;
		this.plugin=instance;
		node=null;
	}
	
	public List<? extends Object> getConfigList(String option, List<? extends Object> def)
	{
		if(node == null || !node.contains(option))
			return def;
		else
		{
			return node.getList(option);
		}
	}
	
	public Object getConfigOption(String option, Object def)
	{		
		if(node == null || !node.contains(option))
			return def;
		else
		{
			return node.get(option);
		}
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
