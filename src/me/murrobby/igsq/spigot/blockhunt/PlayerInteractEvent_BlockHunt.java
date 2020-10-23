package me.murrobby.igsq.spigot.blockhunt;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;

import me.murrobby.igsq.spigot.Common;
import me.murrobby.igsq.spigot.Yaml;
import me.murrobby.igsq.spigot.Messaging;

public class PlayerInteractEvent_BlockHunt implements Listener
{
	public PlayerInteractEvent_BlockHunt()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.spigot);
	}
	
	@EventHandler
	public void PlayerInteract_BlockHunt(org.bukkit.event.player.PlayerInteractEvent event) 
	{
		if(Common_BlockHunt.blockhuntCheck()) 
		{
			if(Common_BlockHunt.isPlayer(event.getPlayer())) 
			{
				if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock() != null && !Common_BlockHunt.isInteractWhitelisted(event.getClickedBlock().getType())) event.setCancelled(true); //only allows certain blocks to be right clicked
				else if(Common_BlockHunt.isHider(event.getPlayer()) && Common_BlockHunt.isCloaked(event.getPlayer())) event.setCancelled(true);
				//else if(event.getItem() != null && event.getItem().getType().isBlock()) event.setCancelled(true); //Stops interactions with blocks in inventory
				else if(event.getAction().equals(Action.LEFT_CLICK_BLOCK)) event.setCancelled(true); //Stops interactions with blocks
				else if(event.getItem() != null && event.getItem().getType() == Material.ENDER_EYE && Common_BlockHunt.isHider(event.getPlayer())) event.setCancelled(true); //stops use of ender eye
				else if(event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL && Common_BlockHunt.isHider(event.getPlayer())) 
				{
					if(Common_BlockHunt.isCloaked(event.getPlayer()) && event.getAction().equals(Action.RIGHT_CLICK_AIR))
					{
						event.setCancelled(true);
						event.getPlayer().sendMessage(Messaging.chatFormatter("&#FF0000You cannot change block while hiding!"));
					}
				}
				
				if(Common_BlockHunt.isHider(event.getPlayer())) 
				{
					if(event.getClickedBlock() != null && event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL && Common_BlockHunt.isBlockPlayable(event.getClickedBlock().getType())) 
					{
						Common_BlockHunt.hiderChangeDisguise(event.getPlayer(), event.getClickedBlock().getType());
					}
					
					
					if(event.getItem() != null && Common_BlockHunt.isBlockPlayable(event.getItem().getType()) && Common_BlockHunt.getCloakCooldown(event.getPlayer()) == 0 && (event.getClickedBlock() == null || !Common_BlockHunt.isInteractWhitelisted(event.getClickedBlock().getType())))
					{
						if(!Common_BlockHunt.isCloaked(event.getPlayer())) 
						{
							if(Common_BlockHunt.validateCloak(event.getPlayer())) 
							{
								event.getPlayer().sendMessage(Messaging.chatFormatter("&#00FF00You are now hidden."));
								Common_BlockHunt.addCloak(event.getPlayer());
							}
							else 
							{
								event.getPlayer().sendMessage(Messaging.chatFormatter("&#FFb900You cannot hide here!"));
								Common_BlockHunt.setCloakCooldown(event.getPlayer(), Yaml.getFieldInt("cloakcooldown", "blockhunt")/Yaml.getFieldInt("failcooldown", "blockhunt"));
							}
						}
					}
				}
				else if(Common_BlockHunt.isSeeker(event.getPlayer())) 
				{
					if(event.getItem() != null && event.getItem().getType() == Material.GOLDEN_SWORD && (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))
					{
						Player hider = Common_BlockHunt.raycastForCloak(event.getPlayer(), 6);
						if(hider != null) 
						{
		    				hider.sendMessage(Messaging.chatFormatter("&#FF0000You have been revealed by "+ event.getPlayer().getName() +"!"));
		    				event.getPlayer().sendMessage(Messaging.chatFormatter("&#00FF00Hider "+ hider.getName() +" located!" ));
		    				Common_BlockHunt.setCloakCooldown(hider, Yaml.getFieldInt("cloakcooldown", "blockhunt"));
		    				Common_BlockHunt.removeCloak(hider);
						}
					}
				}
			}
		}
	}
	
}