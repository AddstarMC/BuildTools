package au.com.mineauz.buildtools.types;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class MeasureType implements BuildType {

	@Override
	public String getName() {
		return "MEASURE";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public String getHelpInfo() {
		return "Measures the area you select with block placing and/or breaking, "
				+ "then prints out the details in chat.";
	}

	@Override
	public String[] getParameters() {
		return null;
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		int xl = BTUtils.getDistance(mmt[0].getBlockX(), mmt[1].getBlockX()) + 1;
		int zl = BTUtils.getDistance(mmt[0].getBlockZ(), mmt[1].getBlockZ()) + 1;
		int yl = BTUtils.getDistance(mmt[0].getBlockY(), mmt[1].getBlockY()) + 1;
		int vol = BTUtils.getVolume(mmt[0], mmt[1]);
		player.sendMessage(ChatColor.GREEN + "---------- Measurements ----------");
		player.sendMessage(ChatColor.AQUA + "Volume: " + ChatColor.WHITE + vol + " blocks");
		player.sendMessage(ChatColor.AQUA + "X Length: " + ChatColor.WHITE + xl + " blocks");
		player.sendMessage(ChatColor.AQUA + "Y Length: " + ChatColor.WHITE + yl + " blocks");
		player.sendMessage(ChatColor.AQUA + "Z Length: " + ChatColor.WHITE + zl + " blocks");
		player.sendMessage(ChatColor.GREEN + "----------------------------------");
		points.get(0).getPreviousState().update(true);
		points.get(1).getPreviousState().update(true);
		return null;
	}

}
