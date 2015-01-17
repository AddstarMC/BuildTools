package au.com.mineauz.buildtools.patterns;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BlockPoint;

public interface BuildPattern {
	
	public String getName();
	public List<String> compatibleSelections();
	public boolean useMaterialMatch();
	public String getHelpInfo();
	public String[] getParameters();
	public boolean fitsPattern(BTPlayer player, Location block, List<BlockPoint> points, String[] settings);

}
