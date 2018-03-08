package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;

public class FramePattern implements BuildPattern {

	@Override
	public String getName() {
		return "FRAME";
	}

	@Override
	public List<String> compatibleSelections() {
		List<String> s = new ArrayList<>(1);
		s.add("CUBOID");
		s.add("SPHERE");
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
	public boolean fitsPattern(BTPlayer player, Location block, List<BlockPoint> points, String[] settings) {
		if(player.getType() == BTPlugin.plugin.getBuildTypes().getType("CUBOID")){
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
		}
		else if(player.getType() == BTPlugin.plugin.getBuildTypes().getType("SPHERE")){
			double rad = Double.valueOf(settings[settings.length - 1]);
			double rad2 = rad - 1;
			double m = Math.pow(block.getX() - points.get(0).getPoint().getX(), 2) + 
						Math.pow(block.getY() - points.get(0).getPoint().getY(), 2) + 
						Math.pow(block.getZ() - points.get(0).getPoint().getZ(), 2);
			double r = Math.pow(rad, 2);
			double r2 = Math.pow(rad2, 2);
			if(((m < r && m > r2) || m == Math.ceil(r2)) &&
					(block.getBlockX() == points.get(0).getPoint().getBlockX() || 
					block.getBlockY() == points.get(0).getPoint().getBlockY() ||
					block.getBlockZ() == points.get(0).getPoint().getBlockZ()))
				return true;
		}
		return false;
	}

}
