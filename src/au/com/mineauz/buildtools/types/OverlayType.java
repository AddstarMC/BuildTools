package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.BlockPoint;
import au.com.mineauz.buildtools.BuildMode;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class OverlayType implements BuildType {

	@Override
	public String getName() {
		return "OVERLAY";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}
	
	@Override
	public String getHelpInfo(){
		return "Overlays blocks in the area of the two points. The overlay will not go above or below "
				+ "your selected area, therefore if BuildTools can't find a block at the bottom of the selection, "
				+ "no block will be placed. Breaking away the terrain will remove the top layer of blocks, placing "
				+ "will put the blocks on top of the already existing blocks.";
	}
	
	@Override
	public String[] getParameters(){
		return new String[] {
				"<Height/Depth> " + ChatColor.GRAY + "How high/deep to overlay blocks."
		};
	}

	@Override
	public List<Location> execute(BTPlayer player, BuildMode mode,
			List<BlockPoint> points, BuildPattern pattern, String[] settings, String[] pSettings) {
		Location[] mmt = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		List<Location> locs = new ArrayList<>();
		Location tmp = mmt[0].clone();
		tmp.setY(mmt[1].getY());
		int depth = 1;
		if(settings.length > 0){
			if(settings[0].matches("[0-9]+")){
				depth = Integer.valueOf(settings[0]);
			}
			if(depth == 0)
				depth = 1;
		}
		for(int x = mmt[0].getBlockX(); x <= mmt[1].getBlockX(); x++){
			tmp.setX(x);
			for(int z = mmt[0].getBlockZ(); z <= mmt[1].getBlockZ(); z++){
				tmp.setZ(z);
				if(tmp.getBlock().getType() == Material.AIR){
					while(tmp.getBlock().getType() == Material.AIR && tmp.getBlockY() >= mmt[0].getBlockY()){
						tmp.setY(tmp.getY() - 1);
					}
					if(tmp.getBlock().getType() != Material.AIR && tmp.getBlock().getType().isSolid()){
						if(mode == BuildMode.PLACE)
							tmp.setY(tmp.getY() + 1);
						for(int i = 0; i < depth; i++){
							if(pattern.fitsPattern(player, tmp, points, pSettings))
								locs.add(tmp.clone());
							if(mode == BuildMode.PLACE)
								tmp.setY(tmp.getY() + 1);
							else
								tmp.setY(tmp.getY() - 1);
							
							if(tmp.getY() > mmt[1].getY() || tmp.getY() < mmt[0].getY())
								break;
						}
					}
				}
				else if(mode != BuildMode.PLACE && pattern.fitsPattern(player, tmp, points, pSettings))
					locs.add(tmp);
				tmp.setY(mmt[1].getBlockY());
			}
		}
		return locs;
	}

}
