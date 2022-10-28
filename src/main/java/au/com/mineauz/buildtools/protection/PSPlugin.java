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

		// Checks if world is not a Plot world, then ignore
		if (l.getPlotArea() == null) return true;

		// Checks if player is in a plot and added
		if (l.getPlotAbs() != null) {
			return l.getPlotAbs().isAdded(player.getUniqueId());
		} else {
			return false;
		}
	}

}
