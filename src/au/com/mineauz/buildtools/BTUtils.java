package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;

import au.com.mineauz.buildtools.patterns.BuildPattern;
import au.com.mineauz.buildtools.types.BuildType;

public class BTUtils {
	
	public static String arrayToString(String[] arr){
		String st = ChatColor.GRAY + "";
		boolean alt = false;
		for(String s : arr){
			st += s;
			if(!arr[arr.length - 1 ].equals(s)){
				st += ", ";
				if(alt){
					st += ChatColor.LIGHT_PURPLE;
					alt = false;
				}
				else{
					st += ChatColor.DARK_PURPLE;
					alt = true;
				}
			}
		}
		return st;
	}
	
	public static String capitalize(String input){
		return Character.toUpperCase(input.charAt(0)) + input.substring(1).toLowerCase();
	}
	
	public static Location[] createMinMaxTable(BlockPoint pos1, BlockPoint pos2){
		return(createMinMaxTable(pos1.getPoint(), pos2.getPoint()));
	}
	
	public static Location[] createMinMaxTable(Location pos1, Location pos2){
		Location[] locArr = new Location[2];
		int minx;
		int maxx;
		int miny;
		int maxy;
		int minz;
		int maxz;
		
		if(pos1.getBlockX() > pos2.getBlockX()){
			minx = pos2.getBlockX();
			maxx = pos1.getBlockX();
		}
		else{
			minx = pos1.getBlockX();
			maxx = pos2.getBlockX();
		}
		if(pos1.getBlockY() > pos2.getBlockY()){
			miny = pos2.getBlockY();
			maxy = pos1.getBlockY();
		}
		else{
			miny = pos1.getBlockY();
			maxy = pos2.getBlockY();
		}
		if(pos1.getBlockZ() > pos2.getBlockZ()){
			minz = pos2.getBlockZ();
			maxz = pos1.getBlockZ();
		}
		else{
			minz = pos1.getBlockZ();
			maxz = pos2.getBlockZ();
		}
		locArr[0] = new Location(pos1.getWorld(), minx, miny, minz);
		locArr[1] = new Location(pos1.getWorld(), maxx, maxy, maxz);
		return locArr;
	}
	
	public static void generateBlocks(BTPlayer player, BuildType selection, BuildPattern pattern, List<BlockPoint> points, BuildMode mode){
		if(player != null && !player.canBuild()){
			player.sendMessage("You are currently generating something else, please wait...", ChatColor.AQUA);
			return;
		}
		if(player.getPoint(0).getMode() == BuildMode.BREAK && 
				player.getPoint(player.getPointCount() - 1).getMode() == BuildMode.PLACE){
			mode = BuildMode.REPLACE;
		}
		
		List<Location> locs = selection.execute(player, mode, points, pattern, player.getTSettings(), player.getPSettings());

		if(locs != null){
			BTUndo undo = new BTUndo(player);
			if(player != null && (mode == BuildMode.BREAK || mode == BuildMode.PLACE) && 
					pattern.useMaterialMatch() && !player.pointMaterialsMatch()){
				player.sendMessage("Selection blocks aren't the same material!", ChatColor.RED);
			}
			else{
				player.setCanBuild(false);
				new Generator(locs, points.get(points.size() - 1).getPoint().getBlock(), player, mode, undo);
			}
			
			if(player != null)
				player.addUndo(undo);
		}
	}
	
	public static boolean placeBlock(BTPlayer player, Location loc, MaterialData data, BuildMode mode, BTUndo undo){
		if(mode == BuildMode.PLACE && (loc.getBlock().getType() == Material.AIR || loc.getBlock().isLiquid())){
			undo.addBlock(loc.getBlock().getState());
			BlockState state = loc.getBlock().getState();
			state.setType(data.getItemType());
			state.setData(data);
			state.update(true);
			return true;
		}
		else if(mode == BuildMode.BREAK && loc.getBlock().getType() != Material.AIR && !loc.getBlock().isLiquid()){
			undo.addBlock(loc.getBlock().getState());
			loc.getBlock().setType(Material.AIR);
			return true;
		}
		else if(mode == BuildMode.REPLACE && loc.getBlock().getType() != Material.AIR){
			undo.addBlock(loc.getBlock().getState());
			BlockState state = loc.getBlock().getState();
			state.setType(data.getItemType());
			state.setData(data);
			state.update(true);
			return true;
		}
		return true;
	}
	
	public static int getVolume(Location point1, Location point2){
		Location[] mmt = createMinMaxTable(point1, point2);
		int xl = Math.abs(mmt[1].getBlockX() - mmt[0].getBlockX()) + 1;
		int yl = Math.abs(mmt[1].getBlockY() - mmt[0].getBlockY()) + 1;
		int zl = Math.abs(mmt[1].getBlockZ() - mmt[0].getBlockZ()) + 1;
		return xl * yl * zl;
	}
	
	public static List<String> tabComplete(String match, List<String> options){
		List<String> fOpts = new ArrayList<>();
		if(match != null && !match.equals("")){
			for(String o : options){
				if(o.startsWith(match))
					fOpts.add(o);
			}
		}
		else{
			fOpts.addAll(options);
		}
		return fOpts;
	}
	
	public static BlockFace rotate(BlockFace origin, int angle){
		BlockFace f = BlockFace.valueOf(origin.toString());
		if(angle == 90 || angle == -270){
			switch(origin){
			case NORTH: f = BlockFace.EAST;
			break;
			case EAST: f = BlockFace.SOUTH;
			break;
			case SOUTH: f = BlockFace.WEST;
			break;
			case WEST: f = BlockFace.NORTH;
			break;
			case NORTH_EAST: f = BlockFace.SOUTH_EAST;
			break;
			case SOUTH_EAST: f = BlockFace.SOUTH_WEST;
			break;
			case SOUTH_WEST: f = BlockFace.NORTH_WEST;
			break;
			case NORTH_WEST: f = BlockFace.NORTH_EAST;
			break;
			case NORTH_NORTH_EAST: f = BlockFace.EAST_SOUTH_EAST;
			break;
			case SOUTH_SOUTH_EAST: f = BlockFace.WEST_SOUTH_WEST;
			break;
			case SOUTH_SOUTH_WEST: f = BlockFace.WEST_NORTH_WEST;
			break;
			case NORTH_NORTH_WEST: f = BlockFace.EAST_NORTH_EAST;
			break;
			case EAST_NORTH_EAST: f = BlockFace.SOUTH_SOUTH_EAST;
			break;
			case EAST_SOUTH_EAST: f = BlockFace.SOUTH_SOUTH_WEST;
			break;
			case WEST_SOUTH_WEST: f = BlockFace.NORTH_NORTH_WEST;
			break;
			case WEST_NORTH_WEST: f = BlockFace.NORTH_NORTH_EAST;
			break;
			default:
				break;
			}
		}
		else if(angle == 180 || angle == -180){
			switch(origin){
			case NORTH: f = BlockFace.SOUTH;
			break;
			case EAST: f = BlockFace.WEST;
			break;
			case SOUTH: f = BlockFace.NORTH;
			break;
			case WEST: f = BlockFace.EAST;
			break;
			case NORTH_EAST: f = BlockFace.SOUTH_WEST;
			break;
			case SOUTH_EAST: f = BlockFace.NORTH_WEST;
			break;
			case SOUTH_WEST: f = BlockFace.NORTH_EAST;
			break;
			case NORTH_WEST: f = BlockFace.SOUTH_EAST;
			break;
			case NORTH_NORTH_EAST: f = BlockFace.SOUTH_SOUTH_WEST;
			break;
			case SOUTH_SOUTH_EAST: f = BlockFace.NORTH_NORTH_WEST;
			break;
			case SOUTH_SOUTH_WEST: f = BlockFace.NORTH_NORTH_EAST;
			break;
			case NORTH_NORTH_WEST: f = BlockFace.SOUTH_SOUTH_EAST;
			break;
			case EAST_NORTH_EAST: f = BlockFace.WEST_SOUTH_WEST;
			break;
			case EAST_SOUTH_EAST: f = BlockFace.WEST_NORTH_WEST;
			break;
			case WEST_SOUTH_WEST: f = BlockFace.EAST_NORTH_EAST;
			break;
			case WEST_NORTH_WEST: f = BlockFace.EAST_SOUTH_EAST;
			break;
			default:
				break;
			}
		}
		else if(angle == 270 || angle == -90){
			switch(origin){
			case NORTH: f = BlockFace.WEST;
			break;
			case EAST: f = BlockFace.NORTH;
			break;
			case SOUTH: f = BlockFace.EAST;
			break;
			case WEST: f = BlockFace.SOUTH;
			break;
			case NORTH_EAST: f = BlockFace.NORTH_WEST;
			break;
			case SOUTH_EAST: f = BlockFace.NORTH_EAST;
			break;
			case SOUTH_WEST: f = BlockFace.SOUTH_EAST;
			break;
			case NORTH_WEST: f = BlockFace.SOUTH_WEST;
			break;
			case NORTH_NORTH_EAST: f = BlockFace.WEST_NORTH_WEST;
			break;
			case SOUTH_SOUTH_EAST: f = BlockFace.EAST_NORTH_EAST;
			break;
			case SOUTH_SOUTH_WEST: f = BlockFace.EAST_SOUTH_EAST;
			break;
			case NORTH_NORTH_WEST: f = BlockFace.WEST_SOUTH_WEST;
			break;
			case EAST_NORTH_EAST: f = BlockFace.NORTH_NORTH_WEST;
			break;
			case EAST_SOUTH_EAST: f = BlockFace.NORTH_NORTH_EAST;
			break;
			case WEST_SOUTH_WEST: f = BlockFace.SOUTH_SOUTH_EAST;
			break;
			case WEST_NORTH_WEST: f = BlockFace.SOUTH_SOUTH_WEST;
			break;
			default:
				break;
			}
		}
		return f;
	}
}
