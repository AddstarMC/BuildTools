package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.noise.PerlinNoiseGenerator;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
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
	public List<Location> execute(BTPlayer player, boolean isBreaking, List<Location> points, BuildPattern pattern, String[] settings, String[] pSettings) {
		List<Location> locs = new ArrayList<>();
		int sm = new Random().nextInt(25 - 15) + 15;
		long seed = System.currentTimeMillis();
		if(settings.length != 0){
			if(settings.length >= 2 && settings[1].matches("-?[0-9]+")){
				seed = Long.valueOf(settings[1]);
			}
			if(settings.length >= 1 && settings[0].matches("[1-9]([0-9]+)?")){
				sm = Integer.valueOf(settings[0]);
			}
		}
		PerlinNoiseGenerator gen = new PerlinNoiseGenerator(seed);
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location tmp = mmt[0].clone();
		double h = mmt[1].getBlockY() - mmt[0].getBlockY();
		double hh = h/2;
		for(double x = mmt[0].getX(); x <= mmt[1].getX(); x++){
			tmp.setX(x);
			for(double z = mmt[0].getZ(); z <= mmt[1].getZ(); z++){
				tmp.setZ(z);
				tmp.setY(mmt[0].getY() + hh + (hh * gen.noise(x/sm, z/sm)));
				if(tmp.getY() < mmt[0].getY())
					tmp.setY(mmt[0].getY());
				else if(tmp.getY() > mmt[1].getY())
					tmp.setY(mmt[1].getY());
				locs.add(tmp.clone());
				while(tmp.getBlockY() > mmt[0].getBlockY()){
					tmp.setY(tmp.getY() - 1);
					locs.add(tmp.clone());
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

}
