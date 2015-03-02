package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class CylinderType implements BuildType{

	@Override
	public String getName() {
		return "CYLINDER";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public String getHelpInfo() {
		return "Creates a cylinder with the first point being the centre of the cylinder and the second point being the "
				+ "radius and height/lenght.";
	}

	@Override
	public String[] getParameters() {
		return new String[] {
				"<Direction> " + ChatColor.GRAY + "What direction to face the cylinder (defaults to 'y', possible 'x' and 'z')"
		};
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] tSettings,
			String[] pSettings) {
		List<Location> locs = new ArrayList<Location>();
		Location mid = points.get(0).getPoint();
		String dir = "y";
		if(tSettings.length >= 1){
			if(tSettings[0].matches("y|x|z")){
				dir = tSettings[0].toLowerCase();
			}
		}
		
		Location tmp = points.get(0).getPoint();
		int length = 0;
		if(dir.equals("y")){
			tmp.setY(points.get(1).getPoint().getY());
			length = BTUtils.getDistance(points.get(0).getPoint().getBlockY(), tmp.getBlockY());
		}
		else if(dir.equals("x")){
			tmp.setX(points.get(1).getPoint().getX());
			length = BTUtils.getDistance(points.get(0).getPoint().getBlockX(), tmp.getBlockX());
		}
		else{
			tmp.setZ(points.get(1).getPoint().getZ());
			length = BTUtils.getDistance(points.get(0).getPoint().getBlockZ(), tmp.getBlockZ());
		}
		double dist = tmp.distance(points.get(1).getPoint()) + 1;
		
		int mix;
		int max;
		int miz;
		int maz;
		int miy;
		int may;
		int vol;
		int vollimit;
		
		String[] npSettings = new String[pSettings.length + 2];
		for(int i = 0; i < pSettings.length; i++)
			npSettings[i] = pSettings[i];
		npSettings[npSettings.length - 2] = String.valueOf(dist);
		npSettings[npSettings.length - 1] = dir;
		
		if(dir.equals("y")){
			mix = (int) (points.get(0).getPoint().getBlockX() - dist);
			max = (int) (points.get(0).getPoint().getBlockX() + dist);
			miz = (int) (points.get(0).getPoint().getBlockZ() - dist);
			maz = (int) (points.get(0).getPoint().getBlockZ() + dist);
			if(points.get(0).getPoint().getY() < points.get(1).getPoint().getY()){
				miy = points.get(0).getPoint().getBlockY();
				may = points.get(0).getPoint().getBlockY() + length;
			}
			else{
				miy = points.get(0).getPoint().getBlockY() - length;
				may = points.get(0).getPoint().getBlockY();
			}
		}
		else if(dir.equals("z")){
			mix = (int) (points.get(0).getPoint().getBlockX() - dist);
			max = (int) (points.get(0).getPoint().getBlockX() + dist);
			miy = (int) (points.get(0).getPoint().getBlockY() - dist);
			may = (int) (points.get(0).getPoint().getBlockY() + dist);
			if(points.get(0).getPoint().getZ() < points.get(1).getPoint().getZ()){
				miz = points.get(0).getPoint().getBlockZ();
				maz = points.get(0).getPoint().getBlockZ() + length;
			}
			else{
				miz = points.get(0).getPoint().getBlockZ() - length;
				maz = points.get(0).getPoint().getBlockZ();
			}
		}
		else{
			miy = (int) (points.get(0).getPoint().getBlockY() - dist);
			may = (int) (points.get(0).getPoint().getBlockY() + dist);
			miz = (int) (points.get(0).getPoint().getBlockZ() - dist);
			maz = (int) (points.get(0).getPoint().getBlockZ() + dist);
			if(points.get(0).getPoint().getX() < points.get(1).getPoint().getX()){
				mix = points.get(0).getPoint().getBlockX();
				max = points.get(0).getPoint().getBlockX() + length;
			}
			else{
				mix = points.get(0).getPoint().getBlockX() - length;
				max = points.get(0).getPoint().getBlockX();
			}
		}
		
		vol = BTUtils.getVolume(new Location(tmp.getWorld(), mix, miy, miz), new Location(tmp.getWorld(), max, may, maz));
		vollimit = BTPlugin.plugin.getPlayerData().getPlayerVolumeLimit(player);
		
		if(vol <= vollimit || player.hasPermission("buildtools.bypassvolumelimit")){
			for(int y = miy; y <= may; y++){
				tmp.setY(y);
				for(int x = mix; x <= max; x++){
					tmp.setX(x);
					for(int z = miz; z <= maz; z++){
						tmp.setZ(z);
						
						double m;
						double r2 = Math.pow(dist, 2);
						
						if(dir.equals("y"))
							m = Math.pow(tmp.getX() - mid.getX(), 2) + Math.pow(tmp.getZ() - mid.getZ(), 2);
						else if(dir.equals("z"))
							m = Math.pow(tmp.getX() - mid.getX(), 2) + Math.pow(tmp.getY() - mid.getY(), 2);
						else
							m = Math.pow(tmp.getY() - mid.getY(), 2) + Math.pow(tmp.getZ() - mid.getZ(), 2);
						
						if(m < r2){
							if(pattern.fitsPattern(player, tmp, points, npSettings))
								locs.add(tmp.clone());
						}
					}
				}
			}
		}
		else{
			player.sendMessage("Volume limit exceeded.\n"
					+ "Selected size: " + vol + " blocks.\n"
					+ "Your limit: " + vollimit + " blocks.", ChatColor.RED);
		}
		
		return locs;
	}

}
