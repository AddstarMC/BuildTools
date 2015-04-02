package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class TerrainBlobType implements BuildType {

	@Override
	public String getName() {
		return "TERRAIN_BLOB";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}
	
	@Override
	public String getHelpInfo(){
		return "Creates cave-like blobs. These aren't quite the Minecraft styled vanilla caves, more for cutting "
				+ "into the terrain to create cliffs or pockets. Not all parameters are required but must be filled "
				+ "out in order (you can't have seed without smoothness and smooth edge).";
	}
	
	@Override
	public String[] getParameters(){
		return new String[] {
				ChatColor.GOLD + "<Smoothness> " + ChatColor.GRAY + 
					"The smoothness of the cave. (15 - 25 default)",
				ChatColor.GOLD + "<Smooth Edge> " + ChatColor.GRAY + 
					"Should the edge of the cave selection be smoothed out. (defaults to 'true')",
				ChatColor.GOLD + "<Invert>" + ChatColor.GRAY + 
					"Should the cave generation be inverted (defaults to false)",
				ChatColor.GOLD + "<Density> " + ChatColor.GRAY + 
					"How big the caves should be, values between -1 and 1, -1 being most dense (defaults to 0.3)",
				ChatColor.GOLD + "<Seed> " + ChatColor.GRAY + "The seed of the cave."
		};
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		List<Location> locs = new ArrayList<Location>();
		int sm = new Random().nextInt(25 - 15) + 15;
		long seed = System.currentTimeMillis();
		boolean soft = true;
		double dens = 0.3;
		boolean invert = false;
		if(tSettings.length != 0){
			if(tSettings.length >= 1 && tSettings[0].matches("[1-9]([0-9]+)?")){
				sm = Integer.valueOf(tSettings[0]);
			}
			if(tSettings.length >= 2 && tSettings[1].matches("true|false")){
				soft = Boolean.parseBoolean(tSettings[1]);
			}
			if(tSettings.length >= 3 && tSettings[2].matches("true|false")){
				invert = Boolean.parseBoolean(tSettings[2]);
			}
			if(tSettings.length >= 4 && tSettings[3].matches("-?[0-1]+(.[0-9]+)?")){
				dens = Double.valueOf(tSettings[3]);
				if(dens > 1)
					dens = 1;
				else if(dens < -1)
					dens = -1;
			}
			if(tSettings.length >= 5){
				seed = Long.valueOf(tSettings[4].hashCode());
			}
		}
		SimplexNoiseGenerator gen = new SimplexNoiseGenerator(seed);
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location tmp = mmt[0].clone();
		double l = getDistance(mmt[0].getBlockX(), mmt[1].getBlockX());
		double w = getDistance(mmt[0].getBlockZ(), mmt[1].getBlockZ());
		double h = getDistance(mmt[0].getBlockY(), mmt[1].getBlockY());
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
						if(ud < h/6d){
							n = n - (1d / (h/6d)) * (h/6d - ud);
						}
						if(dd < h/6d){
							n = n - (1d / (h/6d)) * (h/6d - dd);
						}
						if(xmd < l/6d){
							n = n - (1d / (l/6d)) * (l/6d - xmd);
						}
						if(xd < l/6d){
							n = n - (1d / (l/6d)) * (l/6d - xd);
						}
						if(zmd < w/6d){
							n = n - (1d / (w/6d)) * (w/6d - zmd);
						}
						if(zd < w/6d){
							n = n - (1d / (w/6d)) * (w/6d - zd);
						}
					}
					if(invert){
						if(n < dens){
							locs.add(tmp.clone());
						}
					}
					else{
						if(n > dens){
							locs.add(tmp.clone());
						}
					}
				}
			}
		}
		if(player != null){
			player.sendMessage(ChatColor.GRAY + "Smoothness: " + sm);
			player.sendMessage(ChatColor.GRAY + "Soft Edge: " + soft);
			player.sendMessage(ChatColor.GRAY + "Invert: " + invert);
			player.sendMessage(ChatColor.GRAY + "Density: " + dens);
			player.sendMessage(ChatColor.GRAY + "Generator Seed: " + seed);
		}
		else if(BTPlugin.plugin.isDebugging()){
			BTPlugin.plugin.getLogger().info("Smoothness: " + sm);
			BTPlugin.plugin.getLogger().info("Soft Edge: " + soft);
			BTPlugin.plugin.getLogger().info("Invert: " + invert);
			BTPlugin.plugin.getLogger().info("Density: " + dens);
			BTPlugin.plugin.getLogger().info("Generator Seed: " + seed);
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
