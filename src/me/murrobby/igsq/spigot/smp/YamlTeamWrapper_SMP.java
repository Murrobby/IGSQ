package me.murrobby.igsq.spigot.smp;

import org.bukkit.configuration.file.FileConfiguration;

import me.murrobby.igsq.spigot.Yaml;

public class YamlTeamWrapper_SMP 
{
	private String uid;
	public YamlTeamWrapper_SMP(Team_SMP team)
	{
		uid = team.getUID().toString();
	}
	public String getName() 
	{ 
		return Yaml.getFieldString(uid + ".name", "teams");
	}
	public void setName(String name) 
	{ 
		Yaml.updateField(uid + ".name", "teams",name);
	}
	
	public String getOwner() 
	{ 
		return Yaml.getFieldString(uid + ".owner", "teams");
	}
	public void setOwner(String owner) 
	{ 
		Yaml.updateField(uid + ".owner", "teams",owner);
	}
	
	public String getMembers() 
	{ 
		return Yaml.getFieldString(uid + ".members", "teams");
	}
	public void setMembers(String name)
	{ 
		Yaml.updateField(uid + ".members", "teams",name);
	}
	
	public String getRanks() 
	{ 
		return Yaml.getFieldString(uid + ".ranks", "teams");
	}
	public void setRanks(String ranks)
	{
		Yaml.updateField(uid + ".ranks", "teams",ranks);
	}
	public static String getTeams() 
	{ 
		return Yaml.getFieldString("teams", "teams");
	}
	public static void setTeams(String teams) 
	{
		Yaml.updateField("teams", "teams",teams);
	}
	public String getAlly() 
	{ 
		return Yaml.getFieldString(uid + ".ally", "teams");
	}
	public void setAlly(String ally) 
	{ 
		Yaml.updateField(uid + ".ally", "teams", ally);
	}
	public String getAllyPending() 
	{ 
		return Yaml.getFieldString(uid + ".allypending", "teams");
	}
	public void setAllyPending(String allypending) 
	{ 
		Yaml.updateField(uid + ".allypending", "teams", allypending);
	}
	public String getEnemy() 
	{ 
		return Yaml.getFieldString(uid + ".enemy", "teams");
	}
	public void setEnemy(String enemy) 
	{ 
		Yaml.updateField(uid + ".enemy", "teams", enemy);
	} 
	public String getLeavePending() 
	{ 
		return Yaml.getFieldString(uid + ".leavepending", "teams");
	}
	public void setLeavePending(String leavepending) 
	{ 
		Yaml.updateField(uid + ".leavepending", "teams", leavepending);
	}
	public String getBanned() 
	{ 
		return Yaml.getFieldString(uid + ".leavepending", "teams");
	}
	public void setBanned(String banned) 
	{ 
		Yaml.updateField(uid + ".leavepending", "teams", banned);
	}
	public void delete() 
	{ 
		Yaml.deleteField(uid, "teams");
	}
	public void applyDefault() 
	{
		Yaml.addFieldDefault(uid + ".ranks", "teams", "");
		Yaml.addFieldDefault(uid + ".members", "teams", "");
		Yaml.addFieldDefault(uid + ".name", "teams", "");
		Yaml.addFieldDefault(uid + ".owner", "teams", "");
		Yaml.addFieldDefault(uid + ".ally", "teams", "");
		Yaml.addFieldDefault(uid + ".allypending", "teams", "");
		Yaml.addFieldDefault(uid + ".enemy", "teams", "");
		Yaml.addFieldDefault(uid + ".leavepending", "teams", "");
		Yaml.addFieldDefault(uid + ".banned", "teams", "");
		
		for(FileConfiguration configuration : Yaml.getConfigurations()) configuration.options().copyDefaults(true);
	}
}
