package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class SphereType implements BuildType {

	@Override
	public String getName() {
		return "SPHERE";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}
	
	@Override
	public String getHelpInfo(){
		return "Creates a sphere, the first block you place/break will be the centre, the second will be the radius from the "
				+ "centre.";
	}
	
	@Override
	public String[] getParameters(){
		return null;
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		List<Location> locs = new ArrayList<>();
		Location mid = points.get(0).getPoint();
		Location tmp = mid.clone();
		
		double dist = mid.distance(points.get(1).getPoint()) + 1;
		tmp.setY(mid.getY() - dist);
		tmp.setX(mid.getX() - dist);
		tmp.setZ(mid.getZ() - dist);
		int mx = tmp.getBlockX();
		int my = tmp.getBlockY();
		int mz = tmp.getBlockZ();
		for(int x = mx; x <= mid.getBlockX() + dist; x++){
			tmp.setX(x);
			for(int z = mz; z <= mid.getBlockZ() + dist; z++){
				tmp.setZ(z);
				for(int y = my; y <= mid.getBlockY() + dist; y++){
					tmp.setY(y);
					double m = Math.pow(tmp.getX() - mid.getX(), 2) + Math.pow(tmp.getY() - mid.getY(), 2) + Math.pow(tmp.getZ() - mid.getZ(), 2);
					double r2 = Math.pow(dist, 2);
					if(m < r2){
						if(pattern.fitsPattern(tmp, points, pSettings))
							locs.add(tmp.clone());
					}
				}
			}
		}
		
		return locs;
	}

}
