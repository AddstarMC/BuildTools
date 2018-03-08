package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.noise.SimplexNoiseGenerator;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.IVector;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class CaveType implements BuildType{

	@Override
	public String getName() {
		return "CAVE";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public String getHelpInfo() {
		return "Generates a standard cave. Currently starts generating from the middle of the selection (Will be changed).";
	}

	@Override
	public String[] getParameters() {
		return new String[] {
				ChatColor.GOLD + "<length> " + ChatColor.GRAY + "The length of the cave. (defaults to 200, max 500)"
		};
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		Map<String, IVector> cpoints = new HashMap<>();
		List<Location> locs = new ArrayList<>();
		int sm = new Random().nextInt(25 - 15) + 15;
		long seed = System.currentTimeMillis();
		int length = 200;
		
		if(tSettings.length != 0){
			if(tSettings.length >= 1 && tSettings[0].matches("[1-9]([0-9]+)?")){
				length = Integer.valueOf(tSettings[0]);
			}
		}
		if(length > 500)
			length = 500;
		
		SimplexNoiseGenerator gen = new SimplexNoiseGenerator(seed);
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location tmp = mmt[0].clone();
		Double l = (double) getDistance(mmt[0].getBlockX(), mmt[1].getBlockX());
		Double w = (double) getDistance(mmt[0].getBlockZ(), mmt[1].getBlockZ());
		Double h = (double) getDistance(mmt[0].getBlockY(), mmt[1].getBlockY());
		
		Random rand = new Random(seed);
		
		tmp.setX(mmt[0].getX() + (mmt[1].getBlockX() - mmt[0].getBlockX()) / 2);
		tmp.setY(mmt[0].getY() + (mmt[1].getBlockY() - mmt[0].getBlockY()) / 2);
		tmp.setZ(mmt[0].getZ() + (mmt[1].getBlockZ() - mmt[0].getBlockZ()) / 2);
		tmp.setPitch(rand.nextFloat() * 90 - 45);
		tmp.setYaw(rand.nextFloat() * 720 - 360);
		
		IVector vec = new IVector(tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ());
		cpoints.put(vec.toString(), vec);
		
		Double n1;
		Double n2;
		
		int rep = 0;
		
		for(int i = 0; i < length; i++){
			rep++;
			if(rep == 1){
				n1 = gen.noise(tmp.getX()/sm, tmp.getY()/sm);
				n2 = gen.noise(tmp.getX()/sm, tmp.getZ()/sm);
				
				float angle = n1.floatValue() * 45f;
				float angle2 = n2.floatValue() * 10f;
				tmp.setYaw(addAngle(tmp.getYaw(), angle));
				tmp.setPitch(addAngle(tmp.getPitch(), angle2));
			}
			else if(rep >= 4){
				rep = 0;
			}
			//y
			if(getDistance(tmp.getBlockY(), mmt[0].getBlockY()) <= h/6){
				if(tmp.getPitch() <= 90 && tmp.getPitch() > 0)
					tmp.setPitch(addAngle(tmp.getPitch(), -40));
				else if(tmp.getPitch() >= 90 && tmp.getPitch() < 180)
					tmp.setPitch(addAngle(tmp.getPitch(), 40));
			}
			if(getDistance(tmp.getBlockY(), mmt[1].getBlockY()) <= h/6){
				if(tmp.getPitch() >= -90 && tmp.getPitch() < 0)
					tmp.setPitch(addAngle(tmp.getPitch(), 40));
				else if(tmp.getPitch() <= -90 && tmp.getPitch() > -180)
					tmp.setPitch(addAngle(tmp.getPitch(), -40));
			}
			//x
			if(getDistance(tmp.getBlockX(), mmt[0].getBlockX()) <= l/6){
				if(tmp.getYaw() <= 90 && tmp.getYaw() > 0)
					tmp.setYaw(addAngle(tmp.getYaw(), -40));
				else if(tmp.getYaw() > 90 && tmp.getYaw() < 180)
					tmp.setYaw(addAngle(tmp.getYaw(), 40));
			}
			if(getDistance(tmp.getBlockX(), mmt[1].getBlockX()) <= l/6){
				if(tmp.getYaw() >= -90 && tmp.getYaw() < 0)
					tmp.setYaw(addAngle(tmp.getYaw(), 40));
				else if(tmp.getYaw() <= -90 && tmp.getYaw() > -180)
					tmp.setYaw(addAngle(tmp.getYaw(), -40));
			}
			//z
			if(getDistance(tmp.getBlockZ(), mmt[0].getBlockZ()) <= w/6){
				if(tmp.getYaw() >= -90 && tmp.getYaw() < -180)
					tmp.setYaw(addAngle(tmp.getYaw(), 40));
				else if(tmp.getYaw() >= 90 && tmp.getYaw() < 180)
					tmp.setYaw(addAngle(tmp.getYaw(), -40));
			}
			if(getDistance(tmp.getBlockZ(), mmt[1].getBlockZ()) <= w/6){
				if(tmp.getYaw() >= 0 && tmp.getYaw() < 90)
					tmp.setYaw(addAngle(tmp.getYaw(), 40));
				else if(tmp.getYaw() <= 0 && tmp.getYaw() > -90)
					tmp.setYaw(addAngle(tmp.getYaw(), -40));
			}
				
			tmp.add(tmp.getDirection());
			if(isBetween(tmp.getBlockX(), mmt[0].getBlockX(), mmt[0].getBlockX() + l.intValue()) && 
					isBetween(tmp.getBlockY(), mmt[0].getBlockY(), mmt[0].getBlockY() + h.intValue()) && 
					isBetween(tmp.getBlockZ(), mmt[0].getBlockZ(), mmt[0].getBlockZ() + w.intValue())){
				vec = new IVector(tmp.getBlockX(), tmp.getBlockY(), tmp.getBlockZ());
				if(!cpoints.containsKey(vec.toString())){
					cpoints.put(vec.toString(), vec);
					
					int size = Double.valueOf(Math.abs(gen.noise(vec.getX()/sm, vec.getY()/sm, vec.getZ()/sm) * 5)).intValue();
					if(size < 2)
						size = 2;
					
					createFakeSphere(size, cpoints, tmp, mmt, l, w, h);
				}
			}
		}
		
		tmp.setX(mmt[0].getX());
		tmp.setY(mmt[0].getY());
		tmp.setZ(mmt[0].getZ());
		
		for(IVector v : cpoints.values()){
			locs.add(new Location(tmp.getWorld(), v.getX(), v.getY(), v.getZ()));
		}
		
		if(player != null){
			player.sendMessage(ChatColor.GRAY + "Length: " + length);
		}
		else if(BTPlugin.plugin.isDebugging()){
			BTPlugin.plugin.getLogger().info("Length: " + length);
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
	
	private boolean isBetween(int pos, int min, int max){
		return pos >= min && pos <= max;
	}
	
	private float addAngle(float orig, float add){
		orig = orig + add;
		if(orig > 180f){
			orig = orig - 360f;
		}
		if(orig < -180f){
			orig = orig + 360f;
		}
		return orig;
	}
	
	private void createFakeSphere(int size, Map<String, IVector> cpoints, Location tmp, 
									Location[] mmt, Double l, Double w, Double h){
		Location tmp2 = tmp.clone();
		IVector vec;
		for(int cy = tmp.getBlockY() - (size - 1); cy <= tmp.getBlockY() + (size - 1); cy++){
			tmp2.setY(cy);
			for(int cx = tmp.getBlockX() - (size - 1); cx <= tmp.getBlockX() + (size - 1); cx++){
				tmp2.setX(cx);
				for(int cz = tmp.getBlockZ() - (size - 1); cz <= tmp.getBlockZ() + (size - 1); cz++){
					tmp2.setZ(cz);
					vec = new IVector(tmp2.getBlockX(), tmp2.getBlockY(), tmp2.getBlockZ());
					if(!cpoints.containsKey(vec.toString()) && 
							isBetween(tmp2.getBlockX(), mmt[0].getBlockX(), mmt[0].getBlockX() + l.intValue()) && 
							isBetween(tmp2.getBlockY(), mmt[0].getBlockY(), mmt[0].getBlockY() + h.intValue()) && 
							isBetween(tmp2.getBlockZ(), mmt[0].getBlockZ(), mmt[0].getBlockZ() + w.intValue())){
						cpoints.put(vec.toString(), vec);
					}
				}
			}
		}
		for(int cy = tmp.getBlockY() - size; cy <= tmp.getBlockY() + size; cy++){
			tmp2.setY(cy);
			for(int cx = tmp.getBlockX() - (size - size/2); cx <= tmp.getBlockX() + (size - size/2); cx++){
				tmp2.setX(cx);
				for(int cz = tmp.getBlockZ() - (size - size/2); cz <= tmp.getBlockZ() + (size - size/2); cz++){
					tmp2.setZ(cz);
					vec = new IVector(tmp2.getBlockX(), tmp2.getBlockY(), tmp2.getBlockZ());
					if(!cpoints.containsKey(vec.toString()) && 
							isBetween(tmp2.getBlockX(), mmt[0].getBlockX(), mmt[0].getBlockX() + l.intValue()) && 
							isBetween(tmp2.getBlockY(), mmt[0].getBlockY(), mmt[0].getBlockY() + h.intValue()) && 
							isBetween(tmp2.getBlockZ(), mmt[0].getBlockZ(), mmt[0].getBlockZ() + w.intValue())){
						cpoints.put(vec.toString(), vec);
					}
				}
			}
		}
		for(int cy = tmp.getBlockY() - (size - size/2); cy <= tmp.getBlockY() + (size - size/2); cy++){
			tmp2.setY(cy);
			for(int cx = tmp.getBlockX() - size; cx <= tmp.getBlockX() + size; cx++){
				tmp2.setX(cx);
				for(int cz = tmp.getBlockZ() - (size - size/2); cz <= tmp.getBlockZ() + (size - size/2); cz++){
					tmp2.setZ(cz);
					vec = new IVector(tmp2.getBlockX(), tmp2.getBlockY(), tmp2.getBlockZ());
					if(!cpoints.containsKey(vec.toString()) && 
							isBetween(tmp2.getBlockX(), mmt[0].getBlockX(), mmt[0].getBlockX() + l.intValue()) && 
							isBetween(tmp2.getBlockY(), mmt[0].getBlockY(), mmt[0].getBlockY() + h.intValue()) && 
							isBetween(tmp2.getBlockZ(), mmt[0].getBlockZ(), mmt[0].getBlockZ() + w.intValue())){
						cpoints.put(vec.toString(), vec);
					}
				}
			}
		}
		for(int cy = tmp.getBlockY() - (size - size/2); cy <= tmp.getBlockY() + (size - size/2); cy++){
			tmp2.setY(cy);
			for(int cx = tmp.getBlockX() - (size - size/2); cx <= tmp.getBlockX() + (size - size/2); cx++){
				tmp2.setX(cx);
				for(int cz = tmp.getBlockZ() - size; cz <= tmp.getBlockZ() + size; cz++){
					tmp2.setZ(cz);
					vec = new IVector(tmp2.getBlockX(), tmp2.getBlockY(), tmp2.getBlockZ());
					if(!cpoints.containsKey(vec.toString()) && 
							isBetween(tmp2.getBlockX(), mmt[0].getBlockX(), mmt[0].getBlockX() + l.intValue()) && 
							isBetween(tmp2.getBlockY(), mmt[0].getBlockY(), mmt[0].getBlockY() + h.intValue()) && 
							isBetween(tmp2.getBlockZ(), mmt[0].getBlockZ(), mmt[0].getBlockZ() + w.intValue())){
						cpoints.put(vec.toString(), vec);
					}
				}
			}
		}
	}
}
