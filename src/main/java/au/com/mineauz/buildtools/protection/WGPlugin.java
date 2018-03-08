package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class WGPlugin implements ProtectionPlugin {
	
	private WorldGuardPlugin plugin;
	
	public WGPlugin(WorldGuardPlugin plugin){
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "WorldGuard";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		return plugin.canBuild(player, location);
	}

}
