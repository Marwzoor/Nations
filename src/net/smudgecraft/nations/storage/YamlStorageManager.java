package net.smudgecraft.nations.storage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.smudgecraft.nations.Nation;
import net.smudgecraft.nations.NationManager;
import net.smudgecraft.nations.NationPlayer;
import net.smudgecraft.nations.Nations;
import net.smudgecraft.nations.OfflineNationPlayer;

public class YamlStorageManager 
{
	private Nations plugin;
	
	public YamlStorageManager(Nations plugin)
	{
		this.plugin=plugin;
	}
	
	public boolean saveNationPlayers(List<NationPlayer> players)
	{
		boolean success = true;
		for(NationPlayer np : players)
		{
			success = savePlayer(np);
		}
		
		return success;
	}
	
	public boolean savePlayers(List<Player> players)
	{
		boolean success = true;
		for(Player p : players)
		{
			success = savePlayer(p);
		}
		
		return success;
	}
	
	public File getConfigFile(Player player)
	{
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(player.getName().charAt(0)) + "/" + player.getName().toLowerCase());
		
		return new File(folder + "/" + player.getName().toLowerCase() + ".yml");
	}
	
	public File getConfigFile(String name)
	{
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(name.charAt(0)) + "/" + name.toLowerCase());
		
		return new File(folder + "/" + name.toLowerCase() + ".yml");
	}
	
	public FileConfiguration getConfig(Player player)
	{
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(player.getName().charAt(0)) + "/" + player.getName());
		
		File file = new File(folder + "/" + player.getName() + ".yml");
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public FileConfiguration getConfig(String name)
	{
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(name.charAt(0)) + "/" + name.toLowerCase());
		
		File file = new File(folder + "/" + name.toLowerCase() + ".yml");
		
		return YamlConfiguration.loadConfiguration(file);
	}
	
	public List<FileConfiguration> getAllConfigs()
	{
		List<FileConfiguration> configs = new ArrayList<FileConfiguration>();
		
		File mainFolder = new File(plugin.getDataFolder() + "/players");
		
		for(File letterFolder : mainFolder.listFiles())
		{
			if(!letterFolder.getName().contains("."))
			{
				for(File playerFolder : letterFolder.listFiles())
				{
					if(!playerFolder.getName().contains("."))
					{
						for(File playerFile : playerFolder.listFiles())
						{
							if(playerFile.getName().contains(playerFolder.getName()) && playerFile.getName().endsWith(".yml"))
							{
								if(YamlConfiguration.loadConfiguration(playerFile)!=null)
								{
									configs.add(YamlConfiguration.loadConfiguration(playerFile));
								}
							}
						}
					}
				}
			}
		}
		
		return configs;
	}
	
	public NationPlayer loadPlayer(Player player)
	{
		boolean newPlayer = false;
		
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(player.getName().charAt(0)));
		
		if(!folder.exists())
			folder.mkdirs();
		
		File playerFolder = new File(folder + "/" + player.getName().toLowerCase());
		
		if(!playerFolder.exists())
			playerFolder.mkdir();
		
		File file = new File(playerFolder + "/" + player.getName().toLowerCase() + ".yml");
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
				newPlayer = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if(!newPlayer)
		{

			NationPlayer nationPlayer = new NationPlayer(player);
			
			if(config.contains("nation.name") && config.contains("nation.level") && NationManager.containsNationByName(config.getString("nation.name")) && config.contains("nation.experience"))
			{
				nationPlayer.setLevel(config.getInt("nation.level"));
				nationPlayer.setExperience(config.getInt("nation.experience"));
			}
			
			return nationPlayer;
		}
		
		return new NationPlayer(player);
	}
	
	public String getNationName(Player player)
	{
		boolean newPlayer = false;
		
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(player.getName().charAt(0)));
		
		if(!folder.exists())
			folder.mkdir();
		
		File playerFolder = new File(folder + "/" + player.getName().toLowerCase());
		
		if(!playerFolder.exists())
			playerFolder.mkdir();
		
		File file = new File(playerFolder + "/" + player.getName().toLowerCase() + ".yml");
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
				newPlayer = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if(!newPlayer)
		{
			if(config.contains("nation.name"))
			{
				return config.getString("nation.name");
			}
		}
		
		return null;
	}
	
	public boolean saveOfflinePlayer(OfflineNationPlayer nationPlayer)
	{
		if(nationPlayer==null)
			return false;
		
		FileConfiguration config = getConfig(nationPlayer.getPlayer().getName());
		
		if(config==null)
			return false;
		
		if(nationPlayer.getNationName() == null)
			return false;
		
		config.set("nation.name", nationPlayer.getNationName());
		
		config.set("nation.level", nationPlayer.getLevel());
		
		config.set("nation.experience", nationPlayer.getExperience());
		
		try
		{
			config.save(getConfigFile(nationPlayer.getPlayer().getName()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public OfflineNationPlayer loadPlayer(OfflinePlayer player)
	{
		boolean newPlayer = false;
		
		File folder = new File(plugin.getDataFolder() + "/players/" + Character.toLowerCase(player.getName().charAt(0)));
		
		if(!folder.exists())
			folder.mkdirs();
		
		File playerFolder = new File(folder + "/" + player.getName().toLowerCase());
		
		if(!playerFolder.exists())
			playerFolder.mkdir();
		
		File file = new File(playerFolder + "/" + player.getName().toLowerCase() + ".yml");
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
				newPlayer = true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if(!newPlayer)
		{

			OfflineNationPlayer nationPlayer = new OfflineNationPlayer(player);
			
			if(config.contains("nation.name") && config.contains("nation.level") && NationManager.containsNationByName(config.getString("nation.name")) && config.contains("nation.experience"))
			{
				nationPlayer.setLevel(config.getInt("nation.level"));
				nationPlayer.setExperience(config.getInt("nation.experience"));
			}
			
			return nationPlayer;
		}
		
		return new OfflineNationPlayer(player);
	}
	
	public boolean savePlayer(Player player)
	{
		NationPlayer nplayer = NationManager.getNationPlayer(player);
		
		if(nplayer==null)
			return false;
		
		FileConfiguration config = getConfig(player);
		
		if(config==null)
			return false;
		
		if(nplayer.getNation()==null)
			return false;
		
		Nation nation = nplayer.getNation();
		
		config.set("nation.name", nation.getName());
		
		config.set("nation.level", nplayer.getLevel());
		
		config.set("nation.experience", nplayer.getExperience());
		
		try
		{
			config.save(getConfigFile(player));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public boolean savePlayer(NationPlayer nplayer)
	{
		if(nplayer==null)
			return false;
		
		FileConfiguration config = getConfig(nplayer.getPlayer());
		
		if(config==null)
			return false;
		
		if(nplayer.getNation()==null)
			return false;
		
		Nation nation = nplayer.getNation();
		
		config.set("nation.name", nation.getName());
		
		config.set("nation.level", nplayer.getLevel());
		
		config.set("nation.experience", nplayer.getExperience());
		
		try
		{
			config.save(getConfigFile(nplayer.getPlayer()));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
}
