package au.com.mineauz.buildtools;

import java.io.File;
import java.io.IOException;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import au.com.mineauz.buildtools.commands.CommandDispatcher;
import au.com.mineauz.buildtools.menu.MenuListener;
import au.com.mineauz.buildtools.patterns.BuildPatterns;
import au.com.mineauz.buildtools.protection.GPPlugin;
import au.com.mineauz.buildtools.protection.PSPlugin;
import au.com.mineauz.buildtools.protection.ProtectionPlugins;
import au.com.mineauz.buildtools.protection.WGPlugin;
import au.com.mineauz.buildtools.types.BuildTypes;

public class BTPlugin extends JavaPlugin{
	
	public static BTPlugin plugin;
	private PlayerData pdata;
	private boolean debug;
	
	private ProtectionPlugins pplugins;
	private BuildPatterns patterns;
	private BuildTypes types;
	private GeneratingChunks chunks;
	private int generatorDelay = 10;
	
	@Override
	public void onEnable(){
		plugin = this;
		
		File conf = new File(getDataFolder().getAbsolutePath() + "/config.yml");
		if(!conf.exists()){
			saveResource("config.yml", false);
		}
		try {
			getConfig().load(conf);
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
        getConfig().options().copyDefaults(true);
		saveConfig();
		if(getConfig().contains("debug"))
			if(getConfig().getBoolean("debug"))
				debug = true;
		generatorDelay = getConfig().getInt("generatorMaxDelay");
		
		pdata = new PlayerData();
		for(Player pl : getServer().getOnlinePlayers())
			pdata.addBTPlayer(pl);
		
		CommandDispatcher comd = new CommandDispatcher();
		getCommand("buildtools").setExecutor(comd);
		getCommand("buildtools").setTabCompleter(comd);
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		
		pplugins = new ProtectionPlugins();
		
		if(plugin.getServer().getPluginManager().isPluginEnabled("WorldGuard")){
			pplugins.addProtectionPlugin(new WGPlugin((WorldGuardPlugin) plugin.getServer().getPluginManager().getPlugin("WorldGuard")));
			if(isDebugging())
				getLogger().info("WorldGuard protection enabled.");
		}
		if(plugin.getServer().getPluginManager().isPluginEnabled("GriefPrevention")){
			pplugins.addProtectionPlugin(new GPPlugin((GriefPrevention) plugin.getServer().getPluginManager().getPlugin("GriefPrevention")));
			if(isDebugging())
				getLogger().info("GriefPrevention protection enabled.");
		}
		if(plugin.getServer().getPluginManager().isPluginEnabled("PlotSquared")){
			pplugins.addProtectionPlugin(new PSPlugin());
			if(isDebugging())
				getLogger().info("PlotSquared protection enabled.");
		}
		
		types = new BuildTypes();
		patterns = new BuildPatterns();
		chunks = new GeneratingChunks();
		
		getLogger().info("Successfully enabled!");
	}
	
	@Override
	public void onDisable(){
		pdata.clearAllBTPlayers();
		pdata = null;
		pplugins = null;
		types = null;
		patterns = null;
		
		getCommand("buildtools").setExecutor(null);
		getCommand("buildtools").setTabCompleter(null);
		
		HandlerList.unregisterAll(this);
		
		getLogger().info(" successfully disabled!");
	}
	
	public PlayerData getPlayerData(){
		return pdata;
	}
	
	public boolean isDebugging(){
		return debug;
	}
	
	public void setDebugging(boolean debug){
		this.debug = debug;
	}
	
	public ProtectionPlugins getProtectionPlugins(){
		return pplugins;
	}
	
	public BuildTypes getBuildTypes(){
		return types;
	}
	
	public BuildPatterns getBuildPatterns(){
		return patterns;
	}
	
	public GeneratingChunks getGeneratingChunks(){
		return chunks;
	}
	
	public int getMaxGeneratorDelay(){
		return generatorDelay;
	}

}
