package net.smudgecraft.nations.listeners;

import net.smudgecraft.nations.Nation;
import net.smudgecraft.nations.NationManager;
import net.smudgecraft.nations.NationPlayer;
import net.smudgecraft.nations.NationSkill;
import net.smudgecraft.nations.Nations;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.util.Messaging;

public class PlayerListener implements Listener
{
	private Nations plugin;
	
	public PlayerListener(Nations plugin)
	{
		this.plugin=plugin;
	}
	
	@EventHandler
	public void onEntityDeathEvent(EntityDeathEvent event)
	{
		if(event.getEntity().getKiller()==null)
			return;
		
		Player player = event.getEntity().getKiller();
		
		EntityType entityType = event.getEntityType();
				
		NationPlayer nationPlayer = NationManager.getNationPlayer(player);
		
		if(nationPlayer == null || nationPlayer.getNation() == null || nationPlayer.getNation().getName().equals("None"))
			return;
		
		int experience = Nations.getExperience(entityType.getName());
		
		if(experience!=0)
		{
			int previousLevel = nationPlayer.getLevel();
			
			nationPlayer.addExperience(experience);
		
			int newLevel = nationPlayer.getLevel();
			
			player.sendMessage(nationPlayer.getNation().getName() + ": " + ChatColor.GRAY + "Gained " + ChatColor.WHITE + experience + ChatColor.GRAY + " Exp");
		
			if(previousLevel<newLevel)
			{
				player.sendMessage(ChatColor.GRAY + "You gained a level! (Lvl " + ChatColor.WHITE + newLevel + ChatColor.GRAY + " " + nationPlayer.getNation().getName() + ")");
			}
		}
	}
	
	@EventHandler
	public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event)
	{
		if(event.getMessage().equalsIgnoreCase("/lvl") || event.getMessage().equalsIgnoreCase("/hero level") || event.getMessage().equalsIgnoreCase("/level"))
		{
			Player player = event.getPlayer();
			Hero hero = plugin.getHeroes().getCharacterManager().getHero(player);

			player.sendMessage(ChatColor.RED + "-----[ " + ChatColor.WHITE + "Character Levels" + ChatColor.RED + " ]-----");
			player.sendMessage(ChatColor.GREEN + " Class: " + ChatColor.WHITE + hero.getHeroClass().getName() + " | Level: " + hero.getLevel(hero.getHeroClass()) + ChatColor.GREEN + "/" + ChatColor.WHITE + hero.getHeroClass().getMaxLevel());
			player.sendMessage(ChatColor.DARK_GREEN + "   EXP: " + Messaging.createExperienceBar(hero, hero.getHeroClass()));
			
			NationPlayer nationPlayer = NationManager.getNationPlayer(player);
			
			if(nationPlayer!=null && nationPlayer.getNation()!=null && !nationPlayer.getNation().getName().equals("None"))
			{
				Nation nation = nationPlayer.getNation();
				
				double exp = nationPlayer.getExperience();
				double neededExp = nationPlayer.getNeededExperience(nationPlayer.getLevel());
				
				player.sendMessage(ChatColor.GREEN + " Nation: " + ChatColor.WHITE + nation.getName() + " | Level: " + nationPlayer.getLevel());
				player.sendMessage(ChatColor.DARK_GREEN + "   EXP: " + createExperienceBar(exp, neededExp));
			}
			
			if(hero.getSecondClass()!=null)
			{
				player.sendMessage(ChatColor.GREEN + " Discipline: " + ChatColor.WHITE + hero.getSecondClass().getName() + " | Level: " + hero.getLevel(hero.getSecondClass()) + ChatColor.GREEN + "/" + ChatColor.WHITE + hero.getSecondClass().getMaxLevel());
				player.sendMessage(ChatColor.DARK_GREEN + "   EXP: " + Messaging.createExperienceBar(hero, hero.getSecondClass()));
			}
			
			player.sendMessage(ChatColor.RED + "----------------------------");
			
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		
		NationPlayer nationPlayer = plugin.getYamlStorageManager().loadPlayer(player);
		
		if(plugin.getYamlStorageManager().getNationName(player)!=null)
		{
			String nationName = plugin.getYamlStorageManager().getNationName(player);
			
			if(NationManager.containsNationByName(nationName))
			{
				nationPlayer.setNation(NationManager.getNation(nationName));
				
				NationManager.addNationPlayer(nationPlayer);
				
				for(NationSkill nSkill : NationManager.getNation(nationName).getNationSkills())
				{
					final Hero hero = plugin.getHeroes().getCharacterManager().getHero(player);
					
					if(nSkill.getNode().contains("nationlevel") && nSkill.getNode().getInt("nationlevel")<=nationPlayer.getLevel())
					{
						hero.addSkill(nSkill.getName(), nSkill.getNode());
					}
				}
			}
		}
		else
		{
			nationPlayer.setNation(NationManager.getNation("None"));
			NationManager.addNationPlayer(nationPlayer);
		}
	}
	
	@EventHandler
	public void onPlayerQuitEvent(PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		
		NationPlayer nationPlayer = NationManager.getNationPlayer(player);
		
		if(nationPlayer==null)
			return;
		
		plugin.getYamlStorageManager().savePlayer(nationPlayer);
		
		if(nationPlayer.getNation()==null)
			return;
		
		NationManager.removePlayer(nationPlayer);
	}
	
	 public static String createExperienceBar(double experience, double neededexperience) {
		    StringBuilder expBar = new StringBuilder(new StringBuilder().append(ChatColor.RED).append("[").append(ChatColor.DARK_GREEN).toString());
		    int progress = (int)(experience / neededexperience * 50.0D);
		    for (int i = 0; i < progress; i++) {
		      expBar.append('|');
		    }
		    expBar.append(ChatColor.DARK_RED);
		    for (int i = 0; i < 50 - progress; i++) {
		      expBar.append('|');
		    }
		    expBar.append(new StringBuilder().append(ChatColor.RED).append("]").toString());
		    expBar.append(new StringBuilder().append(" - ").append(ChatColor.DARK_GREEN).append(progress * 2).append("%  ").toString());
		    expBar.append(new StringBuilder().append("").append(ChatColor.DARK_GREEN).append((int)experience).append(ChatColor.RED).append("/").append(ChatColor.DARK_GREEN).append((int) neededexperience).toString());
		    return expBar.toString();
}
}
