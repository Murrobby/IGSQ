package me.murrobby.igsq.spigot.expert.protection;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.Common;
import me.murrobby.igsq.spigot.YamlWrapper;

public class BlockBreakEvent_Protection implements Listener
{
	public BlockBreakEvent_Protection()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.spigot);
	}
	
	@EventHandler
	public void BlockBreak_Expert(org.bukkit.event.block.BlockBreakEvent event) 
	{
		if(!event.isCancelled() && YamlWrapper.isExpert()) 
		{
			if(Common_Protection.isProtected(event.getPlayer(),event.getBlock().getLocation())) event.setCancelled(true);
		}
	}
	
}
