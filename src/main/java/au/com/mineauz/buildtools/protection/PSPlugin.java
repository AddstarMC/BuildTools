package au.com.mineauz.buildtools.protection;

import com.github.intellectualsites.plotsquared.api.PlotAPI;
import org.bukkit.Location;
import org.bukkit.entity.Player;


public class PSPlugin implements ProtectionPlugin {
	
	private PlotAPI api;
	
	public PSPlugin(){
		api = new PlotAPI();
	}

	@Override
	public String getName() {
		return "PlotSquared";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		com.github.intellectualsites.plotsquared.plot.object.Location l = new com.github.intellectualsites.plotsquared.plot.object.Location(location.getWorld().getName(),
				location.getBlockX(),location.getBlockY(),location.getBlockZ());
		if(l.getPlotAbs() != null) {
			return l.getPlotAbs().isAdded(player.getUniqueId());
		}else{
			return false;
		}
	}

}
