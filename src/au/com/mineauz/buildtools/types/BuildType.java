package au.com.mineauz.buildtools.types;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public interface BuildType {
	
	public String getName();
	public int getRequiredPointCount();
	public List<Location> execute(BTPlayer player, BuildMode mode, List<BlockPoint> points, 
			BuildPattern pattern, String[] tSettings, String[] pSettings);
}
