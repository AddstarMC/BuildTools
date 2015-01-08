package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class OverlayType implements BuildType {

	@Override
	public String getName() {
		return "OVERLAY";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<Location> points, BuildPattern pattern, String[] settings, String[] pSettings) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		List<Location> locs = new ArrayList<>();
		Location tmp = mmt[0].clone();
		tmp.setY(mmt[1].getY());
		for(int x = mmt[0].getBlockX(); x <= mmt[1].getBlockX(); x++){
			tmp.setX(x);
			for(int z = mmt[0].getBlockZ(); z <= mmt[1].getBlockZ(); z++){
				tmp.setZ(z);
				if(tmp.getBlock().getType() == Material.AIR){
					while(tmp.getBlock().getType() == Material.AIR && tmp.getBlockY() >= mmt[0].getBlockY()){
						tmp.setY(tmp.getY() - 1);
					}
					if(tmp.getBlock().getType() != Material.AIR && tmp.getBlock().getType().isSolid()){
						if(mode == BuildMode.PLACE)
							tmp.setY(tmp.getY() + 1);
						if(pattern.fitsPattern(tmp, points, pSettings))
							locs.add(tmp.clone());
					}
				}
				else if(mode == BuildMode.BREAK && pattern.fitsPattern(tmp, points, pSettings))
					locs.add(tmp);
				tmp.setY(mmt[1].getBlockY());
			}
		}
		return locs;
	}

}
