package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;

public class HollowPattern implements BuildPattern {

	@Override
	public String getName() {
		return "HOLLOW";
	}

	@Override
	public List<String> compatibleSelections() {
		List<String> s = new ArrayList<>(1);
		s.add("CUBOID");
		s.add("SPHERE");
		s.add("CYLINDER");
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
	public boolean fitsPattern(BTPlayer player,
			Location block, List<BlockPoint> points, String[] settings) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		if(player.getType() == BTPlugin.plugin.getBuildTypes().getType("CUBOID")){
            return block.getBlockX() == mmt[0].getBlockX() ||
                    block.getBlockX() == mmt[1].getBlockX() ||
                    block.getBlockY() == mmt[0].getBlockY() ||
                    block.getBlockY() == mmt[1].getBlockY() ||
                    block.getBlockZ() == mmt[0].getBlockZ() ||
                    block.getBlockZ() == mmt[1].getBlockZ();
		}
		else if(player.getType() == BTPlugin.plugin.getBuildTypes().getType("SPHERE")){
			double rad = Double.valueOf(settings[settings.length - 1]);
			double rad2 = rad - 1;
			double m = Math.pow(block.getX() - points.get(0).getPoint().getX(), 2) + 
						Math.pow(block.getY() - points.get(0).getPoint().getY(), 2) + 
						Math.pow(block.getZ() - points.get(0).getPoint().getZ(), 2);
			double r = Math.pow(rad, 2);
			double r2 = Math.pow(rad2, 2);
            return (m < r && m > r2) || m == Math.ceil(r2);
		}
		else{
			String dir = settings[settings.length - 1];
			double rad = Double.valueOf(settings[settings.length - 2]);
			double rad2 = rad - 1;
			double m;
			switch (dir) {
				case "y":
					m = Math.pow(block.getX() - points.get(0).getPoint().getX(), 2) +
							Math.pow(block.getZ() - points.get(0).getPoint().getZ(), 2);
					break;
				case "z":
					m = Math.pow(block.getX() - points.get(0).getPoint().getX(), 2) +
							Math.pow(block.getY() - points.get(0).getPoint().getY(), 2);
					break;
				default:
					m = Math.pow(block.getY() - points.get(0).getPoint().getY(), 2) +
							Math.pow(block.getZ() - points.get(0).getPoint().getZ(), 2);
					break;
			}
			double r = Math.pow(rad, 2);
			double r2 = Math.pow(rad2, 2);
            return (m < r && m > r2) || m == Math.ceil(r2);
		}
    }

}
