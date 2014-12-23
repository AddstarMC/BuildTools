package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class CuboidType implements BuildType {

	@Override
	public String getName() {
		return "CUBOID";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public List<Location> execute(BTPlayer player, List<Location> points, BuildPattern pattern, String[] settings) {
		List<Location> loc = new ArrayList<Location>();
		Location[] mmtab = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location temp = mmtab[0].clone();
		for(int y = mmtab[0].getBlockY(); y <= mmtab[1].getBlockY(); y++){
			temp.setY(y);
			for(int x = mmtab[0].getBlockX(); x <= mmtab[1].getBlockX(); x++){
				temp.setX(x);
				for(int z = mmtab[0].getBlockZ(); z <= mmtab[1].getBlockZ(); z++){
					temp.setZ(z);
					if(pattern.fitsPattern(temp, points))
						loc.add(temp.clone());
				}
			}
		}
		return loc;
	}

}
