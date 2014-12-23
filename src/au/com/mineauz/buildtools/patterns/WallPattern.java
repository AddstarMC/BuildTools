package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTUtils;

public class WallPattern implements BuildPattern {

	@Override
	public String getName() {
		return "WALL";
	}

	@Override
	public List<String> compatibleSelections() {
		List<String> c = new ArrayList<>();
		c.add("CUBOID");
		c.add("OVERLAY");
		return c;
	}

	@Override
	public boolean useMaterialMatch() {
		return true;
	}

	@Override
	public boolean fitsPattern(Location block, List<Location> points) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		if(block.getBlockX() == mmt[0].getBlockX() || 
				block.getBlockX() == mmt[1].getBlockX() ||
				block.getBlockZ() == mmt[0].getBlockZ() || 
				block.getBlockZ() == mmt[1].getBlockZ()){
			return true;
		}
		return false;
	}

}