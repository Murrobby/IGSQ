package me.murrobby.igsq.spigot.blockhunt;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.Common;
import me.murrobby.igsq.spigot.YamlWrapper;

public class PlayerHarvestBlockEvent_BlockHunt implements Listener
{
	public PlayerHarvestBlockEvent_BlockHunt()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.spigot);
	}
	
	@EventHandler
	public void PlayerHarvestBlock_BlockHunt(org.bukkit.event.player.PlayerHarvestBlockEvent event) 
	{
		if(!event.isCancelled()) 
		{
			if(YamlWrapper.isBlockHunt()) 
			{
				if(Game_BlockHunt.getPlayersGame(event.getPlayer()) != null) 
				{
					event.setCancelled(true);
				}
			}
		}
	}
	
}