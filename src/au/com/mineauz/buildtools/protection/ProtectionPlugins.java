package au.com.mineauz.buildtools.protection;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.exceptions.DuplicateProtectionPluginException;
import au.com.mineauz.buildtools.exceptions.UnknownProtectionPluginException;

public class ProtectionPlugins {
	
	public Map<String, ProtectionPlugin> pp = new HashMap<String, ProtectionPlugin>();
	
	public void addProtectionPlugin(ProtectionPlugin plugin) throws DuplicateProtectionPluginException{
		if(!hasProtectionPlugin(plugin.getName()))
			pp.put(plugin.getName().toLowerCase(), plugin);
		else
			throw new DuplicateProtectionPluginException(plugin.getName());
	}
	
	public boolean hasProtectionPlugin(String name){
		return pp.containsKey(name.toLowerCase());
	}
	
	public ProtectionPlugin getProtectionPlugin(String name) throws UnknownProtectionPluginException{
		if(pp.containsKey(name.toLowerCase()))
			return pp.get(name.toLowerCase());
		else
			throw new UnknownProtectionPluginException(name);
	}
	
	public void removeProtectionPlugin(String name) throws UnknownProtectionPluginException{
		if(pp.containsKey(name.toLowerCase()))
			pp.remove(name.toLowerCase());
		else
			throw new UnknownProtectionPluginException(name);
	}
	
	public boolean canBuild(BTPlayer player, Location location){
		boolean canBuild = true;
		if(player.hasProtectionOverride()) return true;
		
		for(ProtectionPlugin plugin : pp.values()){
			if(!plugin.canBuild(player.getPlayer(), location)){
				canBuild = false;
				break;
			}
		}
		return canBuild;
	}
}
