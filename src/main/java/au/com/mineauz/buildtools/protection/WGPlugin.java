package au.com.mineauz.buildtools.protection;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
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
		LocalPlayer lp = plugin.wrapPlayer(player);
		RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		RegionQuery query = container.createQuery();
		StateFlag flag = Flags.BUILD;
		return (query.testState(BukkitAdapter.adapt(location), lp,flag));
	}

}
