package au.com.mineauz.buildtools.types;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public interface BuildType {
	
	public String getName();
	public int getRequiredPointCount();
	public List<Location> execute(BTPlayer player, List<Location> points, BuildPattern pattern, String[] settings);
//	public void fill(List<Location> toFill, BTPlayer player, BuildPattern pattern, boolean breaking, BTUndo undo);
}
