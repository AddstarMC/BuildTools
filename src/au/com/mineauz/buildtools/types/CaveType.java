package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class CaveType implements BuildType {

	@Override
	public String getName() {
		return "CAVE";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}
	
	@Override
	public String[] getParameters(){
		return new String[] {
				"<Smoothness>",
				"<Smooth Edge>",
				"<Seed>"
		};
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		List<Location> locs = new ArrayList<>();
		int sm = new Random().nextInt(25 - 15) + 15;
		long seed = System.currentTimeMillis();
		boolean soft = true;
		if(tSettings.length != 0){
			if(tSettings.length >= 3 && tSettings[2].matches("-?[0-9]+")){
				seed = Long.valueOf(tSettings[2].hashCode());
			}
			if(tSettings.length >= 2 && tSettings[1].matches("true|false")){
				soft = Boolean.parseBoolean(tSettings[1]);
			}
			if(tSettings.length >= 1 && tSettings[0].matches("[1-9]([0-9]+)?")){
				sm = Integer.valueOf(tSettings[0]);
			}
		}
		PerlinNoiseGenerator gen = new PerlinNoiseGenerator(seed);
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location tmp = mmt[0].clone();
		for(double x = mmt[0].getX(); x <= mmt[1].getX(); x++){
			tmp.setX(x);
			for(double z = mmt[0].getZ(); z <= mmt[1].getZ(); z++){
				tmp.setZ(z);
				for(double y = mmt[0].getY(); y <= mmt[1].getY(); y++){
					tmp.setY(y);
					double n = gen.noise(x/sm, y/sm, z/sm);
					if(soft){
						int ud = getDistance(new Double(y).intValue(), mmt[1].getBlockY());
						int dd = getDistance(new Double(y).intValue(), mmt[0].getBlockY());
						int xmd = getDistance(new Double(x).intValue(), mmt[1].getBlockX());
						int xd = getDistance(new Double(x).intValue(), mmt[0].getBlockX());
						int zmd = getDistance(new Double(z).intValue(), mmt[1].getBlockZ());
						int zd = getDistance(new Double(z).intValue(), mmt[0].getBlockZ());
						if(ud < 6){
							n = n - 0.05 * (6 - ud);
						}
						if(dd < 6){
							n = n - 0.05 * (6 - dd);
						}
						if(xmd < 6){
							n = n - 0.05 * (6 - xmd);
						}
						if(xd < 6){
							n = n - 0.05 * (6 - xd);
						}
						if(zmd < 6){
							n = n - 0.05 * (6 - zmd);
						}
						if(zd < 6){
							n = n - 0.05 * (6 - zd);
						}
					}
					if(n > 0.3){
						locs.add(tmp.clone());
					}
				}
			}
		}
		if(player != null){
			player.sendMessage(ChatColor.GRAY + "Smoothness: " + sm);
			player.sendMessage(ChatColor.GRAY + "Soft Edge: " + soft);
			player.sendMessage(ChatColor.GRAY + "Generator Seed: " + seed);
		}
		else if(Main.plugin.isDebugging()){
			Main.plugin.getLogger().info("Smoothness: " + sm);
			Main.plugin.getLogger().info("Soft Edge: " + soft);
			Main.plugin.getLogger().info("Generator Seed: " + seed);
		}
		
		return locs;
	}
	
	private int getDistance(int pos1, int pos2){
		if(pos1 > pos2){
			return Math.abs(pos1 - pos2);
		}
		else{
			return Math.abs(pos2 - pos1);
		}
	}

}
