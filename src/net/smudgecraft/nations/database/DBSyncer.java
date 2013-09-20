package net.smudgecraft.nations.database;

import java.sql.Connection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.herocraftonline.heroes.characters.Hero;

import net.smudgecraft.nations.NationPlayer;
import net.smudgecraft.nations.Nations;

public class DBSyncer extends Thread
{
	private Nations plugin;
	
	public DBSyncer(Nations plugin)
	{
		this.plugin=plugin;
	}
	
	public void start()
	{
		try
		{
		Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Started thread safe database syncing...");	
		
		MySQL mysql = new MySQL(plugin, "smudgecraft.net", "3306", "userdata", "root", "M1829cmc@");
		
		Connection c = mysql.openConnection();
		
		c.createStatement().execute("CREATE TABLE IF NOT EXISTS users(username CHAR(30) NOT NULL," +
				"PRIMARY KEY(username)," +
				"maxhealth INT," +
				"nation Char(20)," +
				"nationlevel INT," +
				"primaryclass CHAR(20)," +
				"primaryclasslevel INT," +
				"secondaryclass CHAR(20)," +
				"secondaryclasslevel INT)");
		
		for(Player p : Bukkit.getOnlinePlayers())
		{							
			Hero hero = plugin.getHeroes().getCharacterManager().getHero(p);
			
			NationPlayer nationPlayer = plugin.getNationManager().getNationPlayer(p);
			
			if(nationPlayer==null)
				return;
			
			if(plugin.getNationManager().getNation(nationPlayer)==null)
				return;
			
			String primaryclass = hero.getHeroClass().getName();
			
			int primaryclasslevel = hero.getLevel(hero.getHeroClass());
			
			String secondaryclass = "none";
			int secondaryclasslevel = 0;
			
			if(hero.getSecondClass()!=null && hero.getSecondClass().getName()!=null)
			{
				secondaryclass = hero.getSecondClass().getName();
				secondaryclasslevel = hero.getLevel(hero.getSecondClass());
			}
			
			c.createStatement().execute("INSERT INTO users (username, maxhealth, nation, nationlevel, primaryclass, primaryclasslevel, secondaryclass, secondaryclasslevel) VALUES (" + "'" + p.getName() + "'" + ", " + p.getMaxHealth() + ", '" + plugin.getNationManager().getNation(nationPlayer).getName() + "', " + nationPlayer.getLevel() + ", '" + primaryclass + "', " + primaryclasslevel + ", " + "'" + secondaryclass + "', " + secondaryclasslevel + ") on duplicate key update username=VALUES(username), maxhealth=VALUES(maxhealth), nation=VALUES(nation), nationlevel=VALUES(nationlevel), primaryclass=VALUES(primaryclass), primaryclasslevel=VALUES(primaryclasslevel), secondaryclass=VALUES(secondaryclass), secondaryclasslevel=VALUES(secondaryclasslevel)");
		}
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
			this.kill();
		}
		
		Bukkit.getConsoleSender().sendMessage(ChatColor.LIGHT_PURPLE + "Database synced!");	
		
		this.kill();
	}
	
	public void kill()
	{
		this.interrupt();
	}
	
	public void sync()
	{
		start();
	}
	
	public void createTable()
	{
		try
		{
		MySQL mysql = new MySQL(plugin, "smudgecraft.net", "3306", "userdata", "root", "M1829cmc@");
		
		Connection c = mysql.openConnection();
		
		c.createStatement().execute("CREATE TABLE IF NOT EXISTS users(PID INT NOT NULL AUTO_INCREMENT," +
				"PRIMARY KEY(PID)," +
				"username CHAR(30))");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
