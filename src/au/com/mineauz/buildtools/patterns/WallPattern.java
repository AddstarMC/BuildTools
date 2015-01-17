package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;

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
	public String getHelpInfo(){
		return "Creates a wall around the selected area. If 'overlay' type is used, will act like fencing.";
	}
	
	@Override
	public String[] getParameters(){
		return null;
	}

	@Override
	public boolean fitsPattern(BTPlayer player, Location block, List<BlockPoint> points, String[] settings) {
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
