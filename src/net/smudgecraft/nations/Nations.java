package net.smudgecraft.nations;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.smudgecraft.nations.commands.NationCommand;
import net.smudgecraft.nations.database.DBSyncer;
import net.smudgecraft.nations.listeners.PlayerListener;
import net.smudgecraft.nations.storage.YamlStorageManager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.herocraftonline.heroes.Heroes;

public class Nations extends JavaPlugin
{
	private static List<Experience> experiences;
	public static double EXPMODIFIER;
	public DBSyncer dbsyncer;
	private int syncScheduleId;
	private Heroes heroes;
	private NationManager nationManager;
	private YamlStorageManager yamlStorageManager;
	private static Nations plugin;
	
	public void onEnable()
	{
		plugin = this;
		
		if(!Bukkit.getPluginManager().isPluginEnabled("Heroes"))
		{
			Bukkit.getLogger().log(Level.SEVERE, "Couldn't find dependency Heroes.jar, disabling Nations...");
			this.setEnabled(false);
			return;
		}
		
		this.heroes = (Heroes) Bukkit.getPluginManager().getPlugin("Heroes");
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		
		this.nationManager = new NationManager(this);
		this.yamlStorageManager = new YamlStorageManager(this);
		dbsyncer = new DBSyncer(this);
		setupSyncing();
		loadExperiences();
		loadNations();
		
		this.getCommand("nation").setExecutor(new NationCommand(this));
		
	}
	
	public void loadExperiences()
	{
		experiences = new ArrayList<Experience>();
		
		File file = new File(this.getDataFolder() + "/experiences.yml");
		
		if(!file.exists())
			return;
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		for(String s : config.getKeys(true))
		{
			if(!s.contains("."))
			{
				Experience exp = new Experience(s, config.getInt(s));
				experiences.add(exp);
			}
		}
	}
	
	public static Nations getPlugin()
	{
		return plugin;
	}
	
	public static List<Experience> getExperiences()
	{
		return experiences;
	}
	
	public static int getExperience(String name)
	{
		for(Experience e : experiences)
		{
			if(e.getName().equalsIgnoreCase(name))
				return e.getExperience();
		}
		
		return 0;
	}
	
	public static double getExpModifier()
	{
		return expModifier;
	}
	
	public NationManager getNationManager()
	{
		return this.nationManager;
	}
	
	public YamlStorageManager getYamlStorageManager()
	{
		return this.yamlStorageManager;
	}
	
	public void onDisable()
	{
		Bukkit.getScheduler().cancelTask(syncScheduleId);
		
		this.getYamlStorageManager().savePlayers(new ArrayList<Player>(Arrays.asList(Bukkit.getOnlinePlayers())));
	}
	
	public void setupSyncing()
	{
		this.syncScheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				dbsyncer.start();
			}
		}, 20*60*10L, 20*60*10L);
	}
	
	public Heroes getHeroes()
	{
		return this.heroes;
	}
	
	public void loadNations()
	{
		File folder = this.getDataFolder();
		
		boolean firstTime = false;
		
		if(!folder.exists())
		{
			folder.mkdir();
			firstTime=true;
		}
		
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
		
		if(!experienceFile.exists())
		{
			try
			{
				experienceFile.createNewFile();
				
				BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/net/smudgecraft/nations/files/experiences.yml")));
				
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
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		if(firstTime)
		{
			config.set("Alzeria.name", "Alzeria");
			config.createSection("Alzeria.skills");
			config.set("Farhelm.name", "Farhelm");
			config.createSection("Farhelm.skills");
			config.set("Drakmar.name", "Drakmar");
			config.createSection("Drakmar.skills");
			config.set("Voronia.name", "Voronia");
			config.createSection("Voronia.skills");
			
			try
			{
				config.save(file);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			for(String s : config.getKeys(true))
			{
				if(!s.contains(".") && !s.equalsIgnoreCase("exp-modifier"))
				{
					ConfigurationSection n = config.getConfigurationSection(s);
					
					Nation nation = new Nation(n.getString("name"));
					
					ConfigurationSection skills = n.getConfigurationSection("skills");
					
					for(String str : skills.getKeys(true))
					{
						if(!str.contains("."))
						{
							ConfigurationSection node = skills.getConfigurationSection(str);
							
							String name = str;
							
							NationSkill nationSkill = new NationSkill(name, node);
							
							nation.addNationSkill(nationSkill);
						}
					}
					
					this.getNationManager().addNation(nation);
				}
			}
			
			Nation defaultNation = new Nation("None");
			
			this.getNationManager().addNation(defaultNation);
			
			expModifier = config.getDouble("exp-modifier");
		}
	}
}
