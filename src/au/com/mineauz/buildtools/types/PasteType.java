package au.com.mineauz.buildtools.types;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUndo;
import au.com.mineauz.buildtools.Generator;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class PasteType implements BuildType {

	@Override
	public String getName() {
		return "PASTE";
	}

	@Override
	public int getRequiredPointCount() {
		return 1;
	}

	@Override
	public List<Location> execute(BTPlayer player, boolean isBreaking,
			List<Location> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		BTUndo undo = new BTUndo(player, true);
		player.addUndo(undo);
		if(isBreaking)
			player.getCopy().setReplacing(true);
		else
			player.getCopy().setReplacing(false);
		new Generator(player.getCopy(), points.get(0), undo);
		return null;
	}

}
