package net.smudgecraft.nations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.smudgecraft.nations.skills.PassiveSkill;
import net.smudgecraft.nations.skills.Skill;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.PluginClassLoader;

public class NationManager 
{
	private static List<Nation> nations = new ArrayList<Nation>();
	private static List<NationPlayer> nationPlayers = new ArrayList<NationPlayer>();
	public static double EXP_MODIFIER;
	private static List<Skill> loadedSkills = new ArrayList<Skill>();
	private static URLClassLoader classLoader;
	
	public NationManager()
	{
		initClassLoader();
		loadSkills();
		loadNations();
	}
	
	public static void initClassLoader()
	{
		File folder = new File(Nations.getPlugin().getDataFolder() + "/skills/");
		
		if(!folder.exists())
		{
			folder.mkdirs();
			return;
		}
		
		PluginClassLoader pluginClassLoader = (PluginClassLoader) Nations.getPlugin().getClass().getClassLoader();
				
		if(pluginClassLoader.getClass().getConstructors().length > 1)
		{
			return;
		}
				
		for(File f : folder.listFiles())
		{
			if(f.getName().endsWith(".jar"))
			{
				try
				{
					pluginClassLoader.addURL(f.toURI().toURL());
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		classLoader = new URLClassLoader(pluginClassLoader.getURLs(), pluginClassLoader);
	}
	
	public static void loadSkills()
	{	
		File folder = new File(Nations.getPlugin().getDataFolder() + "/skills/");
		
		if(!folder.exists())
		{
			folder.mkdirs();
			return;
		}
		
		for(File f : folder.listFiles())
		{
			if(f.getName().endsWith(".jar"))
			{
				try 
				{					
					JarFile jarFile = new JarFile(f);
					
					Enumeration<JarEntry> entries = jarFile.entries();
					
					String main = null;
					
					while(entries.hasMoreElements())
					{
						JarEntry jarEntry = (JarEntry) entries.nextElement();
						
						if(jarEntry.getName().equals("main.skill"))
						{
							BufferedReader in = new BufferedReader(new InputStreamReader(jarFile.getInputStream(jarEntry)));
							
							main = in.readLine().substring(12);
							break;
						}
					}
					
					if(main!=null)
					{
						Class<?> clazz = Class.forName(main, true, classLoader);
						
						Class<? extends Skill> skillClass = clazz.asSubclass(Skill.class);
						
						Constructor<? extends Skill> constructor = skillClass.getConstructor(new Class[] { Nations.getPlugin().getClass() });
					
						Skill skill = (Skill) constructor.newInstance(new Object[] { Nations.getPlugin() });
						
						loadedSkills.add(skill);
					}
					
					jarFile.close();
					
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
				
			}
		}
	}
	
	public static List<Skill> getLoadedSkills()
	{
		return loadedSkills;
	}
	
	public static void loadNations()
	{
		File folder = Nations.getPlugin().getDataFolder();
		
		boolean firstTime = false;
		boolean firstTimeConfig = false;
		
		if(!folder.exists())
		{
			folder.mkdir();
			firstTime=true;
		}
		
		File configFile = new File(folder + "/config.yml");
		
		File file = new File(folder + "/nations.yml");
		
		File experienceFile = new File(folder + "/experiences.yml");
		
		if(!file.exists())
		{
			try
			{
				file.createNewFile();
				firstTime=true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(!configFile.exists())
		{
			try
			{
				file.createNewFile();
				firstTimeConfig=true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(!experienceFile.exists())
		{
			try
			{
				experienceFile.createNewFile();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(Nations.getPlugin().getClass().getResourceAsStream("/net/smudgecraft/nations/files/experiences.yml")));
				
				BufferedWriter out = new BufferedWriter(new FileWriter(experienceFile));
				
				String str;
				
				while((str=in.readLine())!=null && str!="")
				{
					out.write(str + System.getProperty("line.separator"));
				}
				
				in.close();
				out.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		FileConfiguration nationConfig = YamlConfiguration.loadConfiguration(file);
		
		if(firstTimeConfig)
		{
			config.set("exp-modifier", 1.5);
			config.set("databasesync.hours", 0);
			config.set("databasesync.minutes", 10);
			config.set("databasesync.seconds", 0);
			
			try
			{
				config.save(configFile);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
		if(firstTime)
		{
			nationConfig.set("Alzeria.name", "Alzeria");
			nationConfig.createSection("Alzeria.skills");
			nationConfig.set("Farhelm.name", "Farhelm");
			nationConfig.createSection("Farhelm.skills");
			nationConfig.set("Drakmar.name", "Drakmar");
			nationConfig.createSection("Drakmar.skills");
			nationConfig.set("Voronia.name", "Voronia");
			nationConfig.createSection("Voronia.skills");
			
			try
			{
				nationConfig.save(file);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			for(String s : nationConfig.getKeys(true))
			{
				if(!s.contains(".") && !s.equalsIgnoreCase("exp-modifier"))
				{
					ConfigurationSection n = nationConfig.getConfigurationSection(s);
					
					Nation nation = new Nation(n.getString("name"));
					
					ConfigurationSection skills = n.getConfigurationSection("skills");
					
					for(String str : skills.getKeys(true))
					{
						if(!str.contains("."))
						{
							ConfigurationSection node = skills.getConfigurationSection(str);
							
							String name = str;
														
							for(Skill skill : loadedSkills)
							{
								if(skill.getName().equalsIgnoreCase(name) && skill instanceof PassiveSkill)
								{
									PassiveSkill tempSkill = (PassiveSkill) skill;
									tempSkill.setConfiguration(node);
									tempSkill.setLevelRequirement(tempSkill.getConfigOption("level", 1));
									nation.addSkill(tempSkill);
								}
							}
						}
					}
					
					nations.add(nation);
				}
			}
			
			Nation defaultNation = new Nation("None");
			
			nations.add(defaultNation);
			
			EXP_MODIFIER = 1.0D + config.getDouble("exp-modifier");
		}
	}
	
	public static void removePlayer(NationPlayer nationPlayer)
	{
		nationPlayers.remove(nationPlayer);
	}
	
	public static void removePlayer(Player player)
	{
		NationPlayer toRemove = null;
		
		for(NationPlayer nationPlayer : nationPlayers)
		{
			if(nationPlayer.getPlayer().equals(player))
				toRemove=nationPlayer;
		}
		
		if(toRemove!=null)
		{
			nationPlayers.remove(toRemove);
		}
	}
	
	public static List<Nation> getNations()
	{
		return nations;
	}
	
	public static void addNation(Nation nation)
	{
		nations.add(nation);
	}
	
	public static void removeNation(Nation nation)
	{
		nations.remove(nation);
	}
	
	public static Nation getNation(String name)
	{
		for(Nation n : nations)
		{
			if(n.getName().equalsIgnoreCase(name))
				return n;
		}
		
		return null;
	}
	
	public static boolean containsNation(Nation nation)
	{
		return nations.contains(nation);
	}
	
	public static boolean containsNationByName(String name)
	{
		for(Nation n : nations)
		{
			if(n.getName().equalsIgnoreCase(name))
				return true;
		}
		
		return false;
	}
	
	public static List<NationPlayer> getAllFromNation(Nation nation)
	{
		List<NationPlayer> nplayers = new ArrayList<NationPlayer>();
		
		if(nations.contains(nation))
		{
			for(NationPlayer nationPlayer : nationPlayers)
			{
				if(nationPlayer.getNation().equals(nation))
				{
					nplayers.add(nationPlayer);
				}
			}
		}
		return nplayers;
	}
	
	public static NationPlayer getNationPlayer(Player player)
	{
		for(NationPlayer nationPlayer : nationPlayers)
		{
			if(nationPlayer.getPlayer().equals(player))
				return nationPlayer;
		}
		
		return null;
	}
	
	public static void addNationPlayer(NationPlayer nationPlayer)
	{
		nationPlayers.add(nationPlayer);
	}
	
	public static boolean contains(NationPlayer nationPlayer)
	{
		return nationPlayers.contains(nationPlayer);
	}
	
	public static boolean contains(Player player)
	{
		for(NationPlayer nationPlayer : nationPlayers)
		{
			if(nationPlayer.getPlayer().equals(player))
				return true;
		}
		
		return false;
	}
}
