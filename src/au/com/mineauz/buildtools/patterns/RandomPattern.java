package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BlockPoint;

public class RandomPattern implements BuildPattern {

	@Override
	public String getName() {
		return "RANDOM";
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
	public boolean fitsPattern(Location block, List<BlockPoint> points,
			String[] settings) {
		double r = Math.random() * 100;
		double chance = 50;
		if(settings.length >= 1){
			if(settings[0].matches("[0-9]+(.[0-9]+)?")){
				chance = Double.valueOf(settings[0]);
			}
		}
		if(r < chance)
			return true;
		return false;
	}

}
