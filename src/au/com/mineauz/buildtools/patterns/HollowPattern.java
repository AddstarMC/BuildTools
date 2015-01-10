package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;

public class HollowPattern implements BuildPattern {

	@Override
	public String getName() {
		return "HOLLOW";
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
		return "Creates a hollow shape.";
	}
	
	@Override
	public String[] getParameters(){
		return null;
	}

	@Override
	public boolean fitsPattern(Location block,
			List<BlockPoint> points, String[] settings) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		if(block.getBlockX() == mmt[0].getBlockX() || 
				block.getBlockX() == mmt[1].getBlockX() ||
				block.getBlockY() == mmt[0].getBlockY() ||
				block.getBlockY() == mmt[1].getBlockY() ||
				block.getBlockZ() == mmt[0].getBlockZ() || 
				block.getBlockZ() == mmt[1].getBlockZ()){
			return true;
		}
		return false;
	}

}
