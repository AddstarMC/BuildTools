package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;

public class FramePattern implements BuildPattern {

	@Override
	public String getName() {
		return "FRAME";
	}

	@Override
	public List<String> compatibleSelections() {
		List<String> s = new ArrayList<String>(1);
		s.add("CUBOID");
		return s;
	}

	@Override
	public boolean useMaterialMatch() {
		return true;
	}
	
	@Override
	public String getHelpInfo(){
		return "Creates a frame around the selected area.";
	}
	
	@Override
	public String[] getParameters(){
		return null;
	}

	@Override
	public boolean fitsPattern(Location block, List<BlockPoint> points, String[] settings) {
		Location[] locs = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		int minx = locs[0].getBlockX();
		int miny = locs[0].getBlockY();
		int minz = locs[0].getBlockZ();
		int maxx = locs[1].getBlockX();
		int maxy = locs[1].getBlockY();
		int maxz = locs[1].getBlockZ();
		int x = block.getBlockX();
		int y = block.getBlockY();
		int z = block.getBlockZ();
		
		if(((z == minz || z == maxz) && (x == minx || x == maxx) && (y == miny || y == maxy)) ||
				((x == minx || x == maxx) && (y == miny || y == maxy)) ||
				((z == minz || z == maxz) && (y == miny || y == maxy)) || 
				((z == minz || z == maxz) && (x == minx || x == maxx)))
			return true;
		return false;
	}

}
