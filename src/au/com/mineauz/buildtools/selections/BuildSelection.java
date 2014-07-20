package au.com.mineauz.buildtools.selections;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUndo;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public interface BuildSelection {
	
	public String getName();
	public int getRequiredPointCount();
	public List<Location> execute(List<Location> points, BuildPattern pattern);
	public void fill(List<Location> toFill, BTPlayer player, BuildPattern pattern, boolean breaking, BTUndo undo);
}
