package net.smudgecraft.nations;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class Nation 
{
	private final String name;
	private List<NationSkill> skills;
	private List<NationPlayer> nationPlayers;
	
	public Nation(String name)
	{
		this.name = name;
		this.skills = new ArrayList<NationSkill>();
		this.nationPlayers = new ArrayList<NationPlayer>();
	}
	
	public void addNationPlayer(NationPlayer nationPlayer)
	{
		this.nationPlayers.add(nationPlayer);
	}
	
	public NationPlayer getNationPlayer(Player player)
	{
		for(NationPlayer nplayer : nationPlayers)
		{
			if(nplayer.getPlayer().equals(player))
				return nplayer;
		}
		return null;
	}
	
	public void removeNationPlayer(NationPlayer nationPlayer)
	{
		this.nationPlayers.remove(nationPlayer);
	}
	
	public void removeNationPlayer(Player player)
	{
		if(getNationPlayer(player)!=null)
		{
			this.nationPlayers.remove(getNationPlayer(player));
		}
	}
	
	public boolean contains(Player player)
	{
		for(NationPlayer nplayer : nationPlayers)
		{
			if(nplayer.getPlayer().equals(player))
				return true;
		}
		return false;
	}
	
	public boolean contains(NationPlayer nplayer)
	{
		return nationPlayers.contains(nplayer);
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
	
	public List<NationPlayer> getNationPlayers()
	{
		return this.nationPlayers;
	}
}
