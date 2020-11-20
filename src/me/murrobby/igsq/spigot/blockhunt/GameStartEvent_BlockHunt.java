package me.murrobby.igsq.spigot.blockhunt;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import me.murrobby.igsq.spigot.Common;
import me.murrobby.igsq.spigot.Yaml;

public class GameStartEvent_BlockHunt implements Listener
{
	private static Random random = new Random();
	
	public GameStartEvent_BlockHunt()
	{
		Bukkit.getPluginManager().registerEvents(this, Common.spigot);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void GameStart_BlockHunt(me.murrobby.igsq.spigot.event.GameStartEvent event) 
	{
		if(!event.isCancelled()) 
		{
			event.getGame().setStage(Stage.PRE_SEEKER);
			Common_BlockHunt.setupTeams();
			shufflePlayers(100,event.getGame());
			allocatePlayers(event.getGame());
			event.getGame().setTimer(Yaml.getFieldInt("hidetime", "blockhunt"));
		}
		else 
		{
			event.getGame().setTimer( Yaml.getFieldInt("lobbytime", "blockhunt"));
		}
	}
	
	
	
	
	
	
	
	private void shufflePlayers(int shuffles,Game_BlockHunt gameInstance)
	{
		for(int i = 0; i < shuffles;i++) 
		{
			int randomNumber = random.nextInt(Common_BlockHunt.getSeekerCount(gameInstance.getPlayerCount()));
			Player_BlockHunt player = gameInstance.getPlayers()[randomNumber];
			gameInstance.removePlayer(player);
			gameInstance.addPlayer(player);
			
		}
	}
	private void allocatePlayers(Game_BlockHunt gameInstance) 
	{
		Player_BlockHunt[] allocation = gameInstance.getPlayers();
		for(int i = 0; i < Common_BlockHunt.getSeekerCount(allocation) ;i++) //seeker allocation
		{
			int randomNumber = random.nextInt(Common_BlockHunt.getSeekerCount(gameInstance.getPlayerCount()));
			gameInstance.addSeeker(allocation[randomNumber]);
			allocation = Common_BlockHunt.depend(allocation, allocation[randomNumber]);
		}
		for(Player_BlockHunt player : allocation) //hider allocation
		{
			gameInstance.addHider(player);
		}
	}
	
}
