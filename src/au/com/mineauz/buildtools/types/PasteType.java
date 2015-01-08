package au.com.mineauz.buildtools.types;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUndo;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
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
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		if(player.hasCopy()){
			BTUndo undo = new BTUndo(player);
			player.addUndo(undo);
			if(mode == BuildMode.BREAK)
				player.getCopy().setReplacing(true);
			else
				player.getCopy().setReplacing(false);
			new Generator(player.getCopy(), points.get(0).getPoint(), undo);
			player.sendMessage("Pasting selection from clipboard.", ChatColor.AQUA);
		}
		else{
			player.sendMessage("You have not copied anything to your clipboard!", ChatColor.RED);
		}
		return null;
	}

}
