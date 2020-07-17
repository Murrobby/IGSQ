package me.murrobby.igsq.spigot.commands;

import java.util.Calendar;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.block.Block;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import me.murrobby.igsq.spigot.Common_Spigot;
import me.murrobby.igsq.spigot.Main_Spigot;
import me.murrobby.igsq.spigot.expert.Main_Expert;

public class Main_Command implements CommandExecutor{
	private Main_Spigot plugin;
	private Player player;
	private CommandSender sender;
	private String[] args = new String[0];
	
	private Player[] players = null;
	private Material material = Material.valueOf("STONE");
	private String display = "Yourself";
	
	private int realtimeTask = -1;
	
	public Main_Command(Main_Spigot plugin)
	{
		this.plugin = plugin;
		plugin.getCommand("igsq").setExecutor(this);
	}
	@Override 
	
	public boolean onCommand(CommandSender sender,Command command,String label,String[] args) 
	{
		this.sender = sender;
		if(args.length == 0) 
		{
			sender.sendMessage(Common_Spigot.ChatColour("&cPlease Specify a command!"));
			return false;
		}
		//Detect which arguments are used in the /igsq command
		this.args = Common_Spigot.Depend(args, 0);
    	switch(args[0].toLowerCase()) 
    	{
  	  		case "version":
  	  			Version_Command version = new Version_Command(plugin,this,sender,this.args);
  	  			return version.result;
  	  		case "nightvision":
  	  			NightVision_Command nightvision = new NightVision_Command(plugin,this,sender,this.args);
  	  			return nightvision.result;
  	  		case "nv":
  	  			NightVision_Command nv = new NightVision_Command(plugin,this,sender,this.args);
  	  			return nv.result;
  	  		case "block":
  	  			return BlockQuery();
  	  		case "entity":
  	  			return EntityQuery();
  	  		case "realtime":
  	  			return RealTimeQuery();
  	  		case "expert":
  	  			return ExpertDifficultyQuery();
  	  		default:
  	  			Help();
  	  			return false;
    	}
	}
	//Permission checking function
	public boolean RequirePermission(String permission) 
	{
		if(IsPlayer() && player.hasPermission(permission))
		{	
			return true;
		}
		else if(!IsPlayer())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public boolean IsPlayer() 
	{
		if(sender instanceof Player) 
		{
			player = (Player)sender;
			return true;
		}
		else 
		{
			return false;
		}
	}
	private boolean RealTimeQuery()
	{
		
		if(RequirePermission("igsq.realtime")) 
		{
			if(RealTime()) 
			{
				return true;
			}
			else 
			{
				sender.sendMessage(Common_Spigot.ChatColour("&cSomething Went Wrong When Executing this Command!"));
				return false;
			}
		}
		else 
		{
			sender.sendMessage(Common_Spigot.ChatColour("&cYou cannot Execute this command!\nThis may be due to not having the required permission"));
  			return false;
		}
	}
	private boolean RealTime() {
		World world = player.getWorld();
		if(Common_Spigot.getFieldBool("Modules.realtime", "internal").equals(false))
		{
			realtimeTask = plugin.scheduler.scheduleSyncRepeatingTask(plugin, new Runnable()
	    	{

				@Override
				public void run() 
				{
					Calendar calendar = Calendar.getInstance();
					int seconds = calendar.get(Calendar.SECOND);
					int minutes = calendar.get(Calendar.MINUTE);
					int hours = calendar.get(Calendar.HOUR_OF_DAY);
					long totalSeconds = (long) ((double)seconds + ((double)minutes*60) + ((double)hours*3600));
					long mctime = (long)((double)totalSeconds/72*20);
					world.setTime((mctime)-5000);
				} 		
	    	}, 0, 20);
			Common_Spigot.internalData.set("Modules.realtime", true);
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,false);
			sender.sendMessage(Common_Spigot.ChatColour("&bRealtime mode Turned On!"));
			return true;
		}
		else
		{
			Common_Spigot.internalData.set("Modules.realtime", false);
			world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE,true);
			plugin.scheduler.cancelTask(realtimeTask);
			sender.sendMessage(Common_Spigot.ChatColour("&3Realtime mode Turned Off!"));
			return true;
		}
		
	}

	private boolean BlockQuery() 
	{
			if(RequirePermission("igsq.block") && IsPlayer()) 
			{
				if(Block()) 
				{
					return true;
				}
				else 
				{
					sender.sendMessage(Common_Spigot.ChatColour("&9block [block_ID] [*fake*/real/trap] {@all/@others/username/*\"you\"*} {\"another user\"} ..."));
					return false;
				}
			}
			else
			{
				sender.sendMessage(Common_Spigot.ChatColour("&cYou cannot Execute this command!\nThis may be due to being the wrong type or not having the required permission"));
	  			return false;
			}
	}
	private boolean Block() {
		Location location = player.getLocation();
		players = new Player[1];
		players[0] = player;
		display = "Yourself";
		String[] playerArgs = new String[0];
		try 
		{
			material = Material.valueOf(args[0].toUpperCase());
			playerArgs = Common_Spigot.GetBetween(args, 2, -1);
			if(args.length >=3) 
			{
				if(args[2].equalsIgnoreCase("@all")) 
				{
					display = "Everyone";
					for(Player selectedPlayer : plugin.getServer().getOnlinePlayers()) 
					{
							players = Common_Spigot.Append(players,selectedPlayer);
					}
				}
				else if(args[2].equalsIgnoreCase("@others")) 
				{
					display = "Everyone Else";
					for(Player selectedPlayer : plugin.getServer().getOnlinePlayers()) 
					{
						if(!(selectedPlayer.equals(player))) 
						{
							players = Common_Spigot.Append(players,selectedPlayer);
						}
					}
				}
				else 
				{
					players = new Player[0];
					display = "";
					for (String selectedPlayer : playerArgs) 
					{ 
						players = Common_Spigot.Append(players,Bukkit.getPlayer(selectedPlayer));
						display += players[players.length-1].getName() + " ";
					}
				}
			}
		}
		catch(Exception exception) 
		{
			sender.sendMessage(Common_Spigot.ChatColour("&cThis Block, or a Player cound not be found!"));
			return false;
		}
		Block block = location.getBlock();
		if(args.length == 1 || args[1].equalsIgnoreCase("fake")) 
		{
			for (Player selectedPlayer : players) 
			{ 
				selectedPlayer.sendBlockChange(location, material.createBlockData());
			}
			sender.sendMessage(Common_Spigot.ChatColour("&bGave &4FAKE &a"+ args[0].toLowerCase() +" &bto " + display));
			return true;
		}
		else if(args[1].equalsIgnoreCase("real"))
		{
			block.setType(Material.valueOf(args[0].toUpperCase()));
			sender.sendMessage(Common_Spigot.ChatColour("&bGave &a"+ args[0].toLowerCase() +" &bto " + display));
			return true;
		}
		else if(args[1].equalsIgnoreCase("trap")) 
		{
			block.setBlockData(Bukkit.createBlockData("minecraft:tnt[unstable=true]"));
			Bukkit.getScheduler().runTaskLater(plugin, () -> {
				for (Player selectedPlayer : players) 
				{ 
					selectedPlayer.sendBlockChange(location, material.createBlockData());
				}
				sender.sendMessage(Common_Spigot.ChatColour("&bGave &eTRAP &a"+ args[0].toLowerCase() +" &bto " + display));
			}, 2);
			return true;
		}
		else 
		{
			return false;
		}
	}
	private Boolean Help() 
	{
		sender.sendMessage(Common_Spigot.ChatColour("&c+&e----&6IGSQ HELP&e----&c+"));
		sender.sendMessage(Common_Spigot.ChatColour("&eVersion &0- &6Returns current IGSQ plugin version."));
		sender.sendMessage(Common_Spigot.ChatColour("&eBlock &0- &6Allows you to create blocks bellow you."));
		sender.sendMessage(Common_Spigot.ChatColour("&eNightvision &0- &6Gives a player night vision."));
		sender.sendMessage(Common_Spigot.ChatColour("&eEntity &0- &6Allows you to create entities bellow you."));
		sender.sendMessage(Common_Spigot.ChatColour("&c+&e----&6COMMAND KEY HELP&e----&c+"));
		sender.sendMessage(Common_Spigot.ChatColour("&e* &0- &6Default"));
		sender.sendMessage(Common_Spigot.ChatColour("&e\" &0- &6Abreviation"));
		sender.sendMessage(Common_Spigot.ChatColour("&e[ &0- &6Required"));
		sender.sendMessage(Common_Spigot.ChatColour("&e{ &0- &6Optional"));
		sender.sendMessage(Common_Spigot.ChatColour("&e... &0- &6Follows previous block"));
		return true;
	}
	private boolean EntityQuery() 
    {
        if(IsPlayer() && RequirePermission("igsq.entity"))
        {
            if(Entity()) 
            {
                return true;
            }
            else 
            {
                sender.sendMessage(Common_Spigot.ChatColour("&9entity [entity_ID]"));
                return false;
            }
        }
        else 
        {
            sender.sendMessage(Common_Spigot.ChatColour("&cYou cannot Execute this command!\nThis may be due to being the wrong type or not having the required permission"));
            return false;
        }
}

    private boolean Entity() {
        EntityType entitytype;
        @SuppressWarnings("unused")
        LivingEntity entity;
        try
        {
            entitytype = EntityType.valueOf(args[0].toUpperCase());
        }
        catch(Exception exception)
        {
            sender.sendMessage(Common_Spigot.ChatColour("&cThis Entity could not be found!"));
            return false;
        }
        if(args.length == 1 || args[1].equalsIgnoreCase("fake")) 
		{
        	return true;
		}
        else if(args[1].equalsIgnoreCase("real")) 
        {
            entity = (LivingEntity) plugin.getServer().getPlayer(player.getUniqueId()).getWorld().spawnEntity(player.getLocation(), entitytype);
            sender.sendMessage(Common_Spigot.ChatColour("&bGave &a"+ args[0].toLowerCase() +" &bto " + display));
            return true;
        }
        else 
        {
        	
        	return false;
        }

    }
	private boolean ExpertDifficultyQuery() 
    {
        if(RequirePermission("igsq.difficulty"))
        {
            if(ExpertDifficulty()) 
            {
                return true;
            }
            else 
            {
                sender.sendMessage(Common_Spigot.ChatColour("&9expert [true/false]"));
                return false;
            }
        }
        else 
        {
            sender.sendMessage(Common_Spigot.ChatColour("&cYou cannot Execute this command!\nThis may be due to being the wrong type or not having the required permission"));
            return false;
        }
}

    private boolean ExpertDifficulty() //turn expert mode on or off
    {
    	 try
         {
         	boolean enabled = Boolean.parseBoolean(args[0]);
        	plugin.getConfig().set("GAMEPLAY.expert", enabled);
         	plugin.saveConfig();
         	if(enabled) 
         	{
         		player.sendMessage(Common_Spigot.ChatColour("&bExpert Mode &aEnabled&b!"));
         		Main_Expert.Start_Expert();
         	}
         	else 
         	{
         		player.sendMessage(Common_Spigot.ChatColour("&bExpert Mode &cDisabled&b!"));
         	}
         }
         catch(Exception exception)
         {
             sender.sendMessage(Common_Spigot.ChatColour("&cThis Boolean is not valid!"));
             return false;
         }
         return true;
    }
}
