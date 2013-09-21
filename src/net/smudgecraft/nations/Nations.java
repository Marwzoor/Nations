package net.smudgecraft.nations;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import net.smudgecraft.nations.commands.NationCommand;
import net.smudgecraft.nations.database.DBSyncer;
import net.smudgecraft.nations.listeners.PlayerListener;
import net.smudgecraft.nations.storage.YamlStorageManager;

import org.bukkit.Bukkit;
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
		
		NationManager.initClassLoader();
		NationManager.loadSkills();
		NationManager.loadNations();
		
		this.yamlStorageManager = new YamlStorageManager(this);
		dbsyncer = new DBSyncer(this);
		setupSyncing();
		loadExperiences();
		
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
		return NationManager.EXP_MODIFIER;
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
		File file = new File(this.getDataFolder() + "/config.yml");
		
		boolean firstTime = false;
		
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
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		
		int hours = 0;
		int minutes = 10;
		int seconds = 0;
		
		if(firstTime)
		{			
			config.set("exp-modifier", 1.5);
			config.set("databasesync.hours", 0);
			config.set("databasesync.minutes", 10);
			config.set("databasesync.seconds", 0);
			
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
			hours = config.getInt("databasesync.hours");
			minutes = config.getInt("databasesync.minutes");
			seconds = config.getInt("databasesync.seconds");
		}
		
		this.syncScheduleId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
		{
			public void run()
			{
				dbsyncer.start();
			}
		}, (20L*seconds) + (20L*60*minutes) + (20L*60*60*hours), (20L*seconds) + (20L*60*minutes) + (20L*60*60*hours));
	}
	
	public Heroes getHeroes()
	{
		return this.heroes;
	}
}
