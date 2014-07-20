package au.com.mineauz.buildtools;

import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import au.com.mineauz.buildtools.commands.CommandDispatcher;

public class Main extends JavaPlugin{
	
	public static Main plugin;
	
	@Override
	public void onEnable(){
		plugin = this;
		for(Player pl : getServer().getOnlinePlayers())
			PlayerData.addBTPlayer(pl);
		
		CommandDispatcher comd = new CommandDispatcher();
		getCommand("buildtools").setExecutor(comd);
		getCommand("buildtools").setTabCompleter(comd);
		
		getServer().getPluginManager().registerEvents(new Events(), this);
		
		getLogger().info(" successfully enabled!");
	}
	
	@Override
	public void onDisable(){
		PlayerData.clearAllBTPlayers();
		
		getLogger().info(" successfully disabled!");
	}

}
