package au.com.mineauz.buildtools.protection;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

import org.bukkit.Location;
import org.bukkit.Material;
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
		Claim claim = plugin.dataStore.getClaimAt(location, false, null);
		String msg = claim.allowBuild(player, Material.STONE);
		if(msg == null)
			return true;
		return false;
	}

}
