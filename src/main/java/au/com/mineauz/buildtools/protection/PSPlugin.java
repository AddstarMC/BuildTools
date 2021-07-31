package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PSPlugin implements ProtectionPlugin {
	@Override
	public String getName() {
		return "PlotSquared";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		com.plotsquared.core.location.Location l = com.plotsquared.core.location.Location.at(
				location.getWorld().getName(),
				location.getBlockX(),
				location.getBlockY(),
				location.getBlockZ()
		);

		if (l.getPlotAbs() != null) {
			return l.getPlotAbs().isAdded(player.getUniqueId());
		} else {
			return false;
		}
	}

}
