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
	
	public List<String> getConfigStringList(String option, List<String> def)
	{
		if(node == null || !node.contains(option))
			return def;
		else
		{
			return node.getStringList(option);
		}
	}
	
	public List<Integer> getConfigIntegerList(String option, List<Integer> def)
	{
		if(node == null || !node.contains(option))
			return def;
		else
		{
			return node.getIntegerList(option);
		}
	}
	
	public List<Double> getonfigDoubleList(String option, List<Double> def)
	{
		if(node == null || !node.contains(option))
			return def;
		else
		{
			return node.getDoubleList(option);
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
	
	public int getConfigOption(String option, int def)
	{
		if(node==null || !node.contains(option))
			return def;
		else
			return node.getInt(option);
	}
	
	public String getConfigOption(String option, String def)
	{
		if(node==null || !node.contains(option))
			return def;
		else
			return node.getString(option);
	}
	
	public double getConfigOption(String option, double def)
	{
		if(node==null || !node.contains(option))
			return def;
		else
			return node.getDouble(option);
	}
	
	public List<?> getConfigOption(String option, List<?> def)
	{
		if(node==null || !node.contains(option))
			return def;
		else
			return node.getList(option);
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
