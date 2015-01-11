package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.api.ILocation;
import com.worldcretornica.plotme_core.bukkit.PlotMe_CorePlugin;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;

public class PMPlugin implements ProtectionPlugin {
	
	private PlotMe_CorePlugin plugin;
	
	public PMPlugin(PlotMe_CorePlugin plugin){
		this.plugin = plugin;
	}

	@Override
	public String getName() {
		return "PlotMe";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		ILocation loc = new BukkitLocation(location);
		if(!plugin.getAPI().getPlotMeCoreManager().isPlotWorld(loc)) return true;
		Plot plot = PlotMeCoreManager.getPlotById(PlotMeCoreManager.getPlotId(loc), 
				plugin.getAPI().getPlotMeCoreManager().getMap(loc));
		if(plot == null)
			return false;
		return plot.isAllowed(player.getUniqueId());
	}

}
