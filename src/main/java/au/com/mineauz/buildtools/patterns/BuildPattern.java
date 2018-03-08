package au.com.mineauz.buildtools.patterns;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BlockPoint;

public interface BuildPattern {
	
	String getName();
	List<String> compatibleSelections();
	boolean useMaterialMatch();
	String getHelpInfo();
	String[] getParameters();
	boolean fitsPattern(BTPlayer player, Location block, List<BlockPoint> points, String[] settings);

}
