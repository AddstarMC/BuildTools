package au.com.mineauz.buildtools;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.buildtools.commands.CommandDispatcher;

public class Main extends JavaPlugin{
	
	public static Main plugin;
	private PlayerData pdata;
	private boolean debug;
	
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
		
		pdata = new PlayerData();
		for(Player pl : getServer().getOnlinePlayers())
			pdata.addBTPlayer(pl);
		
		CommandDispatcher comd = new CommandDispatcher();
		getCommand("buildtools").setExecutor(comd);
		getCommand("buildtools").setTabCompleter(comd);
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		getLogger().info(" successfully enabled!");
	}
	
	@Override
	public void onDisable(){
		pdata.clearAllBTPlayers();
		pdata = null;
		
		getLogger().info(" successfully disabled!");
	}
	
	public PlayerData getPlayerData(){
		return pdata;
	}
	
	public boolean isDebugging(){
		return debug;
	}

}
