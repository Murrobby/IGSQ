package me.murrobby.igsq.spigot.blockhunt;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.murrobby.igsq.spigot.Common;
import me.murrobby.igsq.spigot.Yaml;
import me.murrobby.igsq.spigot.event.BeginSeekEvent;
import me.murrobby.igsq.spigot.event.GameEndEvent;
import me.murrobby.igsq.spigot.event.GameStartEvent;
import me.murrobby.igsq.spigot.event.LobbyCreateEvent;
import me.murrobby.igsq.spigot.event.PlayerJoinLobbyEvent;

public class Game_BlockHunt 
{
	private Seeker_BlockHunt[] seekers = {};
	private Hider_BlockHunt[] hiders = {};
	private Player_BlockHunt[] players = {};
	
	private Stage stage = Stage.NO_GAME;
	private Map_BlockHunt map;
    private int timer;
    private String name;
    
    private static Game_BlockHunt[] games = {};
    private Random random = new Random();
	
	public Game_BlockHunt(String name) 
	{
		map = new Map_BlockHunt();
		this.name = name;
		timer = Yaml.getFieldInt("lobbytime", "blockhunt");
		games = Common_BlockHunt.append(games, this);
		createLobby();
	}
	
	public Game_BlockHunt() 
	{
		map = new Map_BlockHunt();
		this.name = String.valueOf(System.currentTimeMillis());
		timer = Yaml.getFieldInt("lobbytime", "blockhunt");
		games = Common_BlockHunt.append(games, this);
		createLobby();
	}
	
	
	
	public void start()
	{
		if(stage.equals(Stage.IN_LOBBY)) 
		{
			GameStartEvent event = new GameStartEvent(this);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	public void end(EndReason reason) 
	{
		if(!stage.equals(Stage.NO_GAME)) 
		{
			GameEndEvent event = new GameEndEvent(this,reason);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	public void createLobby()
	{
		if(stage.equals(Stage.NO_GAME)) 
		{
			LobbyCreateEvent event = new LobbyCreateEvent(this);
			Bukkit.getPluginManager().callEvent(event);
		}
		
	}
	public void joinLobby(Player player)
	{
		if(stage.equals(Stage.NO_GAME)) createLobby();
		if(stage.equals(Stage.IN_LOBBY)) 
		{
			PlayerJoinLobbyEvent event = new PlayerJoinLobbyEvent(this,player);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	public void startSeek()
	{
		if(stage.equals(Stage.PRE_SEEKER)) 
		{
			BeginSeekEvent event = new BeginSeekEvent(this);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	
    public Boolean isSeeker(Player player) 
    {
    	for(Seeker_BlockHunt selectedPlayer :seekers) 
    	{
    		if(selectedPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) return true;
    	}
    	return false;
    }
    public Boolean isHider(Player player) 
    {
    	for(Hider_BlockHunt selectedPlayer :hiders) 
    	{
    		if(selectedPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) return true;
    	}
    	return false;
    }
    public Boolean isPlayer(Player player) 
    {
    	for(Player_BlockHunt selectedPlayer : getPlayers()) 
    	{
    		if(selectedPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) return true;
    	}
    	return false;
    }
    
    public Map_BlockHunt getMap()
    {
    	return map;
    }
    
    public Hider_BlockHunt getHiderCloaked(Location location) 
	{
		int x = location.getBlockX();
		int y = location.getBlockY();
		int z = location.getBlockZ();
    	for(Hider_BlockHunt hider : hiders) 
    	{
    		if(hider.getGeneric().isCloaked()) 
    		{
                if(hider.getPlayer().getLocation().getBlockX() == x && hider.getPlayer().getLocation().getBlockY() == y && hider.getPlayer().getLocation().getBlockZ() == z) 
                {
                	return hider;
                }
    		}
    	}
		return null;
	}
    
    public Boolean isBlockPlayable(Material material) 
    {
    	for (Material allowedMaterial : map.getBlocks()) 
    	{
    		if(allowedMaterial.equals(material)) return true;
    	}
    	return false;
    }
    
	public void hidePlayer(Player player) 
	{
		for(Player_BlockHunt selectedPlayer : getPlayers()) 
		{
			if(!selectedPlayer.getPlayer().getUniqueId().equals(player.getUniqueId()))  
			{
				selectedPlayer.getPlayer().hidePlayer(Common.spigot, player);
			}
		}
	}
	public void showPlayer(Player player) 
	{
		for(Player_BlockHunt selectedPlayer : getPlayers()) 
		{
			if(!selectedPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) 
			{
				selectedPlayer.getPlayer().showPlayer(Common.spigot, player);
			}
		}
	}
    //Timers
    public void decrementTimer() 
    {
    	timer--;
    }
    
    public int getTimer() 
    {
    	return timer;
    }
    
    public void setTimer(int timer) 
    {
    	this.timer = timer;
    }
    //Stage
    public Stage getStage() 
    {
    	return stage;
    }
    public void setStage(Stage stage) 
    {
    	this.stage = stage;
    }
    public boolean isStage(Stage stage) 
    {
    	return this.stage.equals(stage);
    }
    //Name
    public String getName() 
    {
    	return name;
    }
    //Seekers
    public Seeker_BlockHunt[] getSeekers() 
    {
    	return seekers;
    }
    public void setSeekers(Seeker_BlockHunt[] seekers) 
    {
    	this.seekers = seekers;
    }
    public void addSeeker(Player player) 
    {
    	removePlayer(player);
    	removeHider(player);
    	if(!isSeeker(player)) setSeekers(Common_BlockHunt.append(getSeekers(),new Seeker_BlockHunt(player,this)));
    }
    public void addSeeker(Player_BlockHunt player) 
    {
    	removePlayer(player);
    	removeHider(player);
    	if(!player.isSeeker()) setSeekers(Common_BlockHunt.append(getSeekers(),new Seeker_BlockHunt(player)));
    }
    public void addSeeker(Seeker_BlockHunt player) 
    {
    	removePlayer(player);
    	removeHider(player);
    	if(!player.isSeeker()) setSeekers(Common_BlockHunt.append(getSeekers(),player));
    }
    public void addPlayer(Player player) 
    {
    	removeSeeker(player);
    	removeHider(player);
    	if(!isPlayer(player)) setPlayers(Common_BlockHunt.append(getPlayers(),new Player_BlockHunt(player,this)));
    }
    public void addPlayer(Player_BlockHunt player) 
    {
    	removeSeeker(player);
    	removeHider(player);
    	if(!player.isPlayer()) setPlayers(Common_BlockHunt.append(getPlayers(),player));
    }
    public void removeSeeker(Player player) 
    {
    	Seeker_BlockHunt seeker = getSeeker(player);
    	if(seeker != null) 
    	{
    		setSeekers(Common_BlockHunt.depend(getSeekers(),seeker));
    	}
    }
    public void removeSeeker(Player_BlockHunt player) 
    {
    	Seeker_BlockHunt seeker = getSeeker(player.getPlayer());
    	if(seeker != null) 
    	{
    		setSeekers(Common_BlockHunt.depend(getSeekers(),seeker));
    	}
    }
	public int getSeekerCount() 
	{
		return seekers.length;
	}
    //Hiders
    public Hider_BlockHunt[] getHiders() 
    {
    	return hiders;
    }
    public void setHiders(Hider_BlockHunt[] hiders) 
    {
    	this.hiders = hiders;
    }
    public void setPlayers(Player_BlockHunt[] players) 
    {
    	this.players = players;
    }
	public Seeker_BlockHunt getSeeker(Player player) 
	{
		for(Seeker_BlockHunt seeker : seekers) 
		{
			if(seeker.getPlayer().getUniqueId().equals(player.getUniqueId())) return seeker;
		}
		return null;
	}
    public void addHider(Player player) 
    {
    	removePlayer(player);
    	removeSeeker(player);
    	if(!isHider(player)) setHiders(Common_BlockHunt.append(getHiders(),new Hider_BlockHunt(player,this)));
    }
    public void addHider(Player_BlockHunt player) 
    {
    	removePlayer(player);
    	removeSeeker(player);
    	if(!player.isHider()) setHiders(Common_BlockHunt.append(getHiders(),new Hider_BlockHunt(player)));
    }
    public void addHider(Hider_BlockHunt player) 
    {
    	removePlayer(player);
    	removeSeeker(player);
    	if(!player.isHider()) setHiders(Common_BlockHunt.append(getHiders(),player));
    }
    public void removeHider(Player player) 
    {
    	Hider_BlockHunt hider = getHider(player);
    	if(hider != null) 
    	{
    		setHiders(Common_BlockHunt.depend(getHiders(),hider));
    	}
    }
    public void removeHider(Player_BlockHunt player) 
    {
    	Hider_BlockHunt hider = getHider(player.getPlayer());
    	if(hider != null) 
    	{
    		setHiders(Common_BlockHunt.depend(getHiders(),hider));
    	}
    }
	public Hider_BlockHunt getHider(Player player) 
	{
		for(Hider_BlockHunt hider : hiders) 
		{
			if(hider.getPlayer().getUniqueId().equals(player.getUniqueId())) return hider;
		}
		return null;
	}
	public int getHiderCount() 
	{
		return hiders.length;
	}
    //Players
    public Player_BlockHunt[] getPlayers() 
    {
    	Player_BlockHunt[] players = this.players;
    	for(Player_BlockHunt hider : getHiders()) players = Common_BlockHunt.append(players, hider);
    	for(Player_BlockHunt seeker : getSeekers()) players = Common_BlockHunt.append(players, seeker);
    	return players;
    }
    public void removePlayer(Player player) 
    {
    	Hider_BlockHunt hider = getHider(player);
    	if(hider != null) 
    	{
    		setHiders(Common_BlockHunt.depend(getHiders(),hider));
    	}
    }
    public void removePlayer(Player_BlockHunt player) 
    {
    	Player_BlockHunt selectedPlayer = getPlayer(player.getPlayer());
    	if(selectedPlayer != null) 
    	{
    		setPlayers(Common_BlockHunt.depend(getPlayers(),selectedPlayer));
    	}
    }
	public int getPlayerCount() 
	{
		return getPlayers().length;
	}
	public Player_BlockHunt getPlayer(Player player) 
	{
		for(Player_BlockHunt selectedPlayer : getPlayers()) 
		{
			if(selectedPlayer.getPlayer().getUniqueId().equals(player.getUniqueId())) return selectedPlayer;
		}
		return null;
	}
	public void delete() 
	{
		for(Player_BlockHunt player : getPlayers()) 
		{
			player.cleanup();
			player.getPlayer().teleport(Map_BlockHunt.getHubLocation());
		}
		games = Common_BlockHunt.depend(games,this);
	}
	public Random getRandom() 
	{
		return random;
	}
	
	
    public static Game_BlockHunt getPlayersGame(Player player)
    {
		for(Game_BlockHunt game : games) 
		{
			if(game.isPlayer(player)) return game;
		}
		return null;
    }
    public static Game_BlockHunt[] getGameInstances() 
    {
    	return games;
    }
    
    public static Game_BlockHunt getInstanceByName(String name) 
    {
    	for(Game_BlockHunt game : games) 
    	{
    		if(game.getName().equals(name)) return game;
    	}
		return null;
    	
    }
    public static void removePlayerFromGames(Player player) 
    {
    	for(Game_BlockHunt game : games) game.removePlayer(player);
    	
    }
    public static boolean instanceofGenericHider(Player player) 
    {
    	for(Game_BlockHunt game : games) 
    	{
    		if(game.isHider(player)) return true;
    	}
    	return false;
    }

}
