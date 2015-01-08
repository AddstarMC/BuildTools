package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class TerrainType implements BuildType{

	@Override
	public String getName() {
		return "TERRAIN";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode, List<Location> points, BuildPattern pattern, String[] tSettings, String[] pSettings) {
		List<Location> locs = new ArrayList<>();
		int sm = new Random().nextInt(25 - 15) + 15;
		long seed = System.currentTimeMillis();
		boolean soft = false;
		if(tSettings.length != 0){
			if(tSettings.length >= 3 && tSettings[2].matches("-?[0-9]+")){
				seed = Long.valueOf(tSettings[2]);
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
		double h = mmt[1].getBlockY() - mmt[0].getBlockY();
		double hh = h/2;
		double l = getDistance(mmt[0].getBlockX(), mmt[1].getBlockX());
		double w = getDistance(mmt[0].getBlockZ(), mmt[1].getBlockZ());
		for(double x = mmt[0].getX(); x <= mmt[1].getX(); x++){
			tmp.setX(x);
			for(double z = mmt[0].getZ(); z <= mmt[1].getZ(); z++){
				tmp.setZ(z);
				double n = gen.noise(x/sm, z/sm);
				if(soft){
					int xmd = getDistance(new Double(x).intValue(), mmt[1].getBlockX());
					int xd = getDistance(new Double(x).intValue(), mmt[0].getBlockX());
					int zmd = getDistance(new Double(z).intValue(), mmt[1].getBlockZ());
					int zd = getDistance(new Double(z).intValue(), mmt[0].getBlockZ());
					if(xmd < l/6d){
						n = n - (1d / (l/6d)) * (l/6d - xmd);
					}
					if(xd < l/6d){
						n = n - (1d / (l/6d)) * (l/6d - xd);
					}
					if(zmd < l/6d){
						n = n - (1d / (w/6d)) * (w/6d - zmd);
					}
					if(zd < l/6d){
						n = n - (1d / (w/6d)) * (w/6d - zd);
					}
				}
				tmp.setY(mmt[0].getY() + hh + (hh * n));
				if(tmp.getY() > mmt[1].getY())
					tmp.setY(mmt[1].getY());
				if(tmp.getY() > mmt[0].getY()){
					locs.add(tmp.clone());
					while(tmp.getBlockY() > mmt[0].getBlockY()){
						tmp.setY(tmp.getY() - 1);
						locs.add(tmp.clone());
					}
				}
			}
		}
		if(player != null){
			player.sendMessage(ChatColor.GRAY + "Generator Seed: " + seed);
			player.sendMessage(ChatColor.GRAY + "Smoothness: " + sm);
		}
		else if(Main.plugin.isDebugging()){
			Main.plugin.getLogger().info("Generator Seed: " + seed);
			Main.plugin.getLogger().info("Smoothness: " + sm);
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
