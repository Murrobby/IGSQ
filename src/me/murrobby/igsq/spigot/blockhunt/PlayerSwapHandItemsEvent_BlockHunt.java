package me.murrobby.igsq.spigot.blockhunt;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.Common;

public class PlayerSwapHandItemsEvent_BlockHunt implements Listener
{
	public PlayerSwapHandItemsEvent_BlockHunt()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.spigot);
	}
	
	@EventHandler
	public void PlayerSwapHandItems_BlockHunt(org.bukkit.event.player.PlayerSwapHandItemsEvent event) 
	{
		if(!event.isCancelled()) 
		{
			if(Common_BlockHunt.blockhuntCheck()) 
			{
				if(Common_BlockHunt.isPlayer(event.getPlayer())) 
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
}