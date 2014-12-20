package au.com.mineauz.buildtools.selections;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUndo;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.patterns.BuildPattern;

public class CuboidSelection implements BuildSelection {

	@Override
	public String getName() {
		return "CUBOID";
	}

	@Override
	public int getRequiredPointCount() {
		return 2;
	}

	@Override
	public List<Location> execute(List<Location> points, BuildPattern pattern) {
		List<Location> loc = new ArrayList<Location>();
		Location[] mmtab = BTUtils.createMinMaxTable(points.get(0), points.get(1));
		Location temp = mmtab[0].clone();
		for(int y = mmtab[0].getBlockY(); y <= mmtab[1].getBlockY(); y++){
			temp.setY(y);
			for(int x = mmtab[0].getBlockX(); x <= mmtab[1].getBlockX(); x++){
				temp.setX(x);
				for(int z = mmtab[0].getBlockZ(); z <= mmtab[1].getBlockZ(); z++){
					temp.setZ(z);
					if(pattern.fitsPattern(temp, points))
						loc.add(temp.clone());
				}
			}
		}
		return loc;
	}

	@Override
	public void fill(List<Location> toFill, BTPlayer player, BuildPattern pattern, boolean breaking,
			BTUndo undo) {
		if(player != null && pattern.useMaterialMatch() && !player.pointMaterialsMatch()){
			player.sendMessage("Selection blocks aren't the same material!", ChatColor.RED);
		}
		else{
			boolean succeed = false;
			Block block = player.getPoints().get(player.getPointCount() - 1).getBlock();
			ItemStack item = BTUtils.getBlockDrop(block);
			MaterialData data = block.getState().getData();
			for(Location loc : toFill){
				succeed = BTUtils.placeBlock(player, loc, 
						data, item, breaking, undo);
				if(!succeed)
					break;
			}
		}
	}

}
