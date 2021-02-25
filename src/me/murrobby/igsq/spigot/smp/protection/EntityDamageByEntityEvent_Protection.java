package me.murrobby.igsq.spigot.smp.protection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.murrobby.igsq.spigot.YamlWrapper;
import me.murrobby.igsq.spigot.Common;

public class EntityDamageByEntityEvent_Protection implements Listener
{
	public EntityDamageByEntityEvent_Protection()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.spigot);
	}
	
	@EventHandler
	public void EntityDamageByEntity_Protection(org.bukkit.event.entity.EntityDamageByEntityEvent event) 
	{
		if(!event.isCancelled() && YamlWrapper.isSMP()) 
		{
			if(event.getDamager() instanceof Player) 
			{
				if(Common_Protection.isProtected((Player)event.getDamager(),event.getEntity())) event.setCancelled(true);
			}
			if(event.getDamager() instanceof Player) 
			{
				if(Common_Protection.isProtected((Player)event.getDamager(),event.getEntity())) event.setCancelled(true);
			}
		}
	}
	
}