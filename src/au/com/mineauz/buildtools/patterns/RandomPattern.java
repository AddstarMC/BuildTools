package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BlockPoint;

public class RandomPattern implements BuildPattern {

	@Override
	public String getName() {
		return "RANDOM";
	}

	@Override
	public List<String> compatibleSelections() {
		List<String> c = new ArrayList<String>();
		c.add("CUBOID");
		c.add("OVERLAY");
		c.add("SPHERE");
		c.add("CYLINDER");
		return c;
	}

	@Override
	public boolean useMaterialMatch() {
		return true;
	}
	
	@Override
	public String getHelpInfo(){
		return "Randomizes block placing.";
	}
	
	@Override
	public String[] getParameters(){
		return new String[] {
				ChatColor.GOLD + "<Chance> " + ChatColor.GRAY + "Chance of a block placing. (50 by default)"
		};
	}

	@Override
	public boolean fitsPattern(BTPlayer player, Location block,
			List<BlockPoint> points, String[] settings) {
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
