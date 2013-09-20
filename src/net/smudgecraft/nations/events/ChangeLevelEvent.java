package net.smudgecraft.nations.events;

import net.smudgecraft.nations.NationPlayer;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ChangeLevelEvent extends Event implements Cancellable
{
	private final NationPlayer nationPlayer;
	private final String nation;
	private final int from;
	private int to;
	private boolean isCancelled;
	private static final HandlerList handlers = new HandlerList();
	
	public ChangeLevelEvent(NationPlayer nationPlayer, String nation, int from, int to)
	{
		this.nationPlayer=nationPlayer;
		this.nation=nation;
		this.from=from;
		this.to=to;
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
