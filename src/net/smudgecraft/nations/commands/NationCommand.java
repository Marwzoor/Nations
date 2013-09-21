package net.smudgecraft.nations.commands;

import net.smudgecraft.nations.Nation;
import net.smudgecraft.nations.NationManager;
import net.smudgecraft.nations.NationPlayer;
import net.smudgecraft.nations.Nations;
import net.smudgecraft.nations.OfflineNationPlayer;
import net.smudgecraft.nations.skills.PassiveSkill;
import net.smudgecraft.nations.skills.Skill;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NationCommand implements CommandExecutor
{
	private Nations plugin;
	
	public NationCommand(Nations plugin)
	{
		this.plugin=plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[])
	{
		if(commandLabel.equalsIgnoreCase("nation") && sender.isOp() && args.length>0 && args[0].equalsIgnoreCase("admin"))
		{
				if(args.length>1)
				{
					if(args[1].equalsIgnoreCase("nation"))
					{
						if(args.length>3)
						{
							OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
								
							Nation newNation = NationManager.getNation(args[3]);
							
							if(newNation==null)
							{
								sender.sendMessage(ChatColor.RED + "There is no nation by the name: " + ChatColor.BLUE + args[3] + ChatColor.RED + " configured!");
								return true;
							}
								
								OfflineNationPlayer onPlayer = plugin.getYamlStorageManager().loadPlayer(player);
								
								if(!onPlayer.isOnline())
								{
									onPlayer.setExperience(0);
									onPlayer.setLevel(1);
									onPlayer.setNationName(newNation.getName());
									
									plugin.getYamlStorageManager().saveOfflinePlayer(onPlayer);
									
									sender.sendMessage(ChatColor.GRAY + "The offline player " + ChatColor.DARK_RED +  player.getName() + "'s" + ChatColor.GRAY + " nation has been set to " + ChatColor.GREEN + newNation.getName() + ChatColor.GRAY + "!");
									
									return true;
								}
								
								NationPlayer nationPlayer = onPlayer.getNationPlayer();
								
								Nation previousNation = nationPlayer.getNation();
								
								String prevNationName = "None";
								
								if(previousNation!=null)
								{
									prevNationName = previousNation.getName();
								}
								
								nationPlayer.setNation(newNation);
								
								nationPlayer.setExperience(0);
								nationPlayer.setLevel(1);
								
								sender.sendMessage(ChatColor.DARK_RED + player.getName() + "'s " + ChatColor.GRAY + "nation has been changed from " + ChatColor.GREEN + prevNationName + ChatColor.GRAY + " to " + ChatColor.GREEN + newNation.getName() + ChatColor.GRAY + "!");
								
								return true;
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
								sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.BLUE + "/nation admin nation <player> <nation>");
								return true;
							}
						}
						else if(args[1].equalsIgnoreCase("level"))
						{
							if(args.length>3)
							{
								OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
								
								if(player.isOnline())
								{
									NationPlayer nationPlayer = NationManager.getNationPlayer(player.getPlayer());
									
									int level = nationPlayer.getLevel();
									
									int levelBefore = level;
									
									try
									{
										level = Integer.parseInt(args[3]);
									}
									catch(NumberFormatException e)
									{
										sender.sendMessage(ChatColor.BLUE + args[3] + ChatColor.RED + " is not a valid number!");
										sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.BLUE + "/nation admin level <player> <nation> <level>");
										return true;
									}
									
									nationPlayer.setLevel(level);
									nationPlayer.setExperience(0);
									
									sender.sendMessage(ChatColor.DARK_RED + nationPlayer.getPlayer().getName() + "'s " + ChatColor.GRAY + "level has been changed from " + ChatColor.GREEN + levelBefore + ChatColor.GRAY + " to " + ChatColor.GREEN + level + ChatColor.GRAY + "!");
								}
								else
								{
									OfflineNationPlayer onationPlayer = plugin.getYamlStorageManager().loadPlayer(player);
									
									int level = onationPlayer.getLevel();
									
									int levelBefore = level;
									
									try
									{
										level = Integer.parseInt(args[3]);
									}
									catch(NumberFormatException e)
									{
										sender.sendMessage(ChatColor.BLUE + args[3] + ChatColor.RED + " is not a valid number!");
										sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.BLUE + "/nation admin level <player> <nation> <level>");
										return true;
									}
									
									onationPlayer.setLevel(level);
									onationPlayer.setExperience(0);
									
									plugin.getYamlStorageManager().saveOfflinePlayer(onationPlayer);
									
									sender.sendMessage(ChatColor.DARK_RED + onationPlayer.getPlayer().getName() + "'s " + ChatColor.GRAY + "level has been changed from " + ChatColor.GREEN + levelBefore + ChatColor.GRAY + " to " + ChatColor.GREEN + level + ChatColor.GRAY + "!");
								}
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
								sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.BLUE + "/nation admin level <player> <nation> <level>");
								return true;
							}
				}
			}
		}
		else if(commandLabel.equalsIgnoreCase("nation") && sender instanceof Player)
		{
			Player player = (Player) sender;
			
			if(args.length>0)
			{
				if(args[0].equalsIgnoreCase(""))
				{
					
				}
			}
			else
			{
				NationPlayer nationPlayer = NationManager.getNationPlayer(player);
				
				if(nationPlayer==null)
				{
					player.sendMessage(ChatColor.RED + "You are worth null to me!");
					return true;
				}
				
				if(nationPlayer.getNation()==null)
				{
					player.sendMessage(ChatColor.RED + "You have no nation!");
					return true;
				}
				
				player.sendMessage(ChatColor.BLUE + "-----[ " + ChatColor.DARK_GREEN + "Nation Information " + ChatColor.BLUE + "]-----");
				player.sendMessage(ChatColor.GREEN + "Nation: " + ChatColor.BLUE + nationPlayer.getNation().getName());
				player.sendMessage(ChatColor.GREEN + "Level: " + ChatColor.BLUE + nationPlayer.getLevel());
				
				if(!nationPlayer.getNation().getSkills().isEmpty())
				{
				player.sendMessage(ChatColor.GREEN + "Skills: ");
				
				for(Skill skill : nationPlayer.getNation().getSkills())
				{
					if(skill instanceof PassiveSkill)
					{
						PassiveSkill pSkill = (PassiveSkill) skill;
						if(nationPlayer.hasAccessToSkill(pSkill))
						{
							player.sendMessage(ChatColor.GREEN + "Level " + pSkill.getLevelRequirement() + " - " + ChatColor.YELLOW + pSkill.getName() + ": " + ChatColor.AQUA + pSkill.getDescription(nationPlayer));
						}
						else
						{
							player.sendMessage(ChatColor.RED + "Level " + pSkill.getLevelRequirement() + " - " + ChatColor.YELLOW + pSkill.getName() + ": " + ChatColor.AQUA + pSkill.getDescription(nationPlayer));
						}
					}
				}
				
				player.sendMessage(ChatColor.BLUE + "--------------------------------");
				}
			}
		}
		return true;
	}
}
