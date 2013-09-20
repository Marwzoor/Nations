package net.smudgecraft.nations.events;

import net.smudgecraft.nations.NationPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChangeLevelEvent extends Event implements Cancellable
{
	private final NationPlayer nationPlayer;
	private final String nation;
	private final int fromExperience;
	private int toExperience;
	private final int from;
	private int to;
	private boolean isCancelled;
	private static final HandlerList handlers = new HandlerList();
	
	public ChangeLevelEvent(NationPlayer nationPlayer, String nation, int from, int to, int fromExperience, int toExperience)
	{
		this.nationPlayer=nationPlayer;
		this.nation=nation;
		this.from=from;
		this.to=to;
		this.fromExperience=fromExperience;
		this.toExperience=toExperience;
	}
	
	public void setToExperience(int exp)
	{
		this.toExperience=exp;
	}
	
	public int getFromExperience()
	{
		return this.fromExperience;
	}
	
	public int getToExperience()
	{
		return this.toExperience;
	}
	
	public void setTo(int to)
	{
		this.to=to;
	}
	
	public int getTo()
	{
		return this.to;
	}
	
	public int getFrom()
	{
		return this.from;
	}
	
	public NationPlayer getNationPlayer()
	{
		return this.nationPlayer;
	}
	
	public String getNationName()
	{
		return this.nation;
	}
	
	public boolean isCancelled() 
	{
		return this.isCancelled;
	}

	public void setCancelled(boolean bool) 
	{
		this.isCancelled=bool;
	}

	public HandlerList getHandlers()
	{
		return handlers;
	}
	
	public static HandlerList getHandlerList()
	{
		return handlers;
	}

}
