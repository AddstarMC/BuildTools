package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;

public class PMPlugin implements ProtectionPlugin {

	@Override
	public String getName() {
		return "PlotMe";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		ILocation loc = new BukkitLocation(location);
		PlotMeCoreManager manager = PlotMeCoreManager.getInstance();
		if (!manager.isPlotWorld(loc)) return true;
		Plot plot = manager.getPlotById(manager.getPlotId(loc), manager.getMap(loc));
		return plot != null && plot.isAllowed(player.getUniqueId());
	}

}
