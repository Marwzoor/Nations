package net.smudgecraft.nations;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class NationManager 
{
	private List<Nation> nations;
	
	public NationManager()
	{
		this.nations=new ArrayList<Nation>();
	}
	
	public List<Nation> getNations()
	{
		return this.nations;
	}
	
	public void addNation(Nation nation)
	{
		this.nations.add(nation);
	}
	
	public void removeNation(Nation nation)
	{
		this.nations.remove(nation);
	}
	
	public Nation getNation(String name)
	{
		for(Nation n : nations)
		{
			if(n.getName().equalsIgnoreCase(name))
				return n;
		}
		
		return null;
	}
	
	public boolean contains(Nation nation)
	{
		return nations.contains(nation);
	}
	
	public boolean containsNationByName(String name)
	{
		for(Nation n : nations)
		{
			if(n.getName().equalsIgnoreCase(name))
				return true;
		}
		
		return false;
	}
	
	public List<NationPlayer> getAllFromNation(Nation nation)
	{
		List<NationPlayer> nplayers = new ArrayList<NationPlayer>();
		
		if(nations.contains(nation))
		{
			for(Nation n : nations)
			{
				nplayers = n.getNationPlayers();
			}
		}
		return nplayers;
	}
	
	public Nation getNation(NationPlayer nplayer)
	{
		for(Nation n : nations)
		{
			if(n.contains(nplayer))
				return n;
		}
		
		return null;
	}
	
	public Nation getNation(Player player)
	{
		for(Nation n : nations)
		{
			if(n.contains(player))
				return n;
		}
		
		return null;
	}
	
	public NationPlayer getNationPlayer(Player player)
	{
		for(Nation n : nations)
		{
			if(n.contains(player))
				return n.getNationPlayer(player);
		}
		
		return null;
	}
}
