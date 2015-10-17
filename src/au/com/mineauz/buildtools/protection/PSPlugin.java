package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.intellectualcrafters.plot.api.PlotAPI;

public class PSPlugin implements ProtectionPlugin {
	
	private PlotAPI api;
	
	@SuppressWarnings("deprecation")
	public PSPlugin(){
		api = new PlotAPI();
	}

	@Override
	public String getName() {
		return "PlotSquared";
	}

	@Override
	public boolean canBuild(Player player, Location location) {
		if(!api.isPlotWorld(location.getWorld())) return true;
		if(api.getPlot(location) != null &&
				api.getPlot(location).isAdded(player.getUniqueId())){
			return true;
		}
		return false;
	}

}
