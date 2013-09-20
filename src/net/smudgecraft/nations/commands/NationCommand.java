package net.smudgecraft.nations.commands;

import net.smudgecraft.nations.Nation;
import net.smudgecraft.nations.NationPlayer;
import net.smudgecraft.nations.Nations;
import net.smudgecraft.nations.OfflineNationPlayer;

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
		if(commandLabel.equalsIgnoreCase("nation"))
		{
			if(args.length>0)
			{
				if(args[0].equalsIgnoreCase("skills") && sender instanceof Player)
				{
					
				}
				else if(args[0].equalsIgnoreCase("admin") && sender.isOp())
				{
					if(args.length>1)
					{
						if(args[1].equalsIgnoreCase("nation"))
						{
							if(args.length>3)
							{
								OfflinePlayer player = Bukkit.getOfflinePlayer(args[2]);
								
								Nation newNation = plugin.getNationManager().getNation(args[3]);
								
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
								
								Nation previousNation = plugin.getNationManager().getNation(nationPlayer);
								
								String prevNationName = "None";
								
								if(previousNation!=null)
								{
									previousNation.removeNationPlayer(nationPlayer);
									prevNationName = previousNation.getName();
								}
								
								newNation.addNationPlayer(nationPlayer);
								
								nationPlayer.setExperience(0);
								nationPlayer.setLevel(1);
								
								sender.sendMessage(ChatColor.DARK_RED + player.getName() + "'s " + ChatColor.GRAY + " nation has been changed from " + ChatColor.GREEN + prevNationName + ChatColor.GRAY + " to " + ChatColor.GREEN + newNation.getName() + ChatColor.GRAY + "!");
								
								return true;
							}
							else
							{
								sender.sendMessage(ChatColor.RED + "Incorrect command usage!");
								sender.sendMessage(ChatColor.RED + "Command usage: " + ChatColor.BLUE + "/nation admin nation <player> <nation>");
								return true;
							}
						}
					}
				}
			}
		}
		
		return true;
	}
}
