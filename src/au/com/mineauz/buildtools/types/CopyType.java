package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTCopy;
import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.IVector;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class CopyType implements BuildType{

	@Override
	public String getName() {
		return "COPY";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public List<Location> execute(BTPlayer player, boolean isBreaking,
			List<Location> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location temp = mmt[0].clone();
		BTCopy cp = new BTCopy();
		List<Location> locs = new ArrayList<>();
		for(int y = mmt[0].getBlockY(); y <= mmt[1].getBlockY(); y++){
			temp.setY(y);
			for(int z = mmt[0].getBlockZ(); z <= mmt[1].getBlockZ(); z++){
				temp.setZ(z);
				for(int x = mmt[0].getBlockX(); x <= mmt[1].getBlockX(); x++){
					temp.setX(x);
					IVector vec = new IVector(getRelativeCoord(x, points.get(0).getBlockX()), 
							getRelativeCoord(y, points.get(0).getBlockY()), 
							getRelativeCoord(z, points.get(0).getBlockZ()));
					if(!Main.plugin.getPlayerData().getPlayerBlockLimits(player).contains(temp.getBlock().getType()))
						cp.addState(vec, temp.getBlock().getState().getData());
					if(isBreaking)
						locs.add(temp.clone());
				}
			}
		}
		player.setCopy(cp);
		if(locs.isEmpty())
			return null;
		return locs;
	}
	
	private int getRelativeCoord(int point, int origin){
		return point - origin;
	}
}
