package au.com.mineauz.buildtools.patterns;

import java.util.List;

import org.bukkit.Location;

public interface BuildPattern {
	
	public String getName();
	public List<String> compatibleSelections();
	public boolean useMaterialMatch();
	public boolean fitsPattern(Location block, List<Location> points);

}
