package au.com.mineauz.buildtools.protection;

import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GPPlugin implements ProtectionPlugin {
	
	private GriefPrevention plugin;
	
	public GPPlugin(GriefPrevention plugin){
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "GriefPrevention";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
        return plugin.allowBuild(player, location) == null &&
                plugin.allowBreak(player, location.getBlock(), location) == null;
    }

}
