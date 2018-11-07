package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import au.com.mineauz.buildtools.patterns.BuildPattern;
import au.com.mineauz.buildtools.types.BuildType;

public class BTUtils {
	
	public static String listToString(List<String> list){
		String[] str = new String[list.size()];
		for(int i = 0; i < list.size(); i++){
			str[i] = list.get(i);
		}
		return arrayToString(str);
	}
	
	public static List<String> stringToList(String toList){
		String[] st = toList.split(";");
		List<String> list = new ArrayList<>(Arrays.asList(st));
		return list;
	}
	
	public static String arrayToString(String[] arr){
		String st = ChatColor.GRAY + "";
		boolean alt = false;
		for(String s : arr){
			st += s;
			if(!arr[arr.length - 1 ].equals(s)){
				st += ", ";
				if(alt){
					st += ChatColor.GRAY;
					alt = false;
				}
				else{
					st += ChatColor.WHITE;
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
		if(points.get(0).getMode() == BuildMode.BREAK && 
				points.get(points.size() - 1).getMode() == BuildMode.PLACE){
			mode = BuildMode.REPLACE;
			points.get(0).getPreviousState().update(true);
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
				player.setGenerator(new Generator(locs, points.get(points.size() - 1).getPoint().getBlock().getState(), player, mode, undo));
			}
			
			for(BlockPoint p : points){
				undo.addBlock(p.getPreviousState());
			}
			
			if(player != null)
				player.addUndo(undo);
		}
	}
	
	public static void placeBlock(BTPlayer player, Location loc, BlockData data, BuildMode mode, BTUndo undo){
		if(BTPlugin.plugin.getProtectionPlugins().canBuild(player, loc)){
			if(mode == BuildMode.PLACE && (loc.getBlock().getType() == Material.AIR || loc.getBlock().isLiquid())){
				if(!data.getMaterial().hasGravity() ||
						loc.clone().subtract(0, 1, 0).getBlock().getType().isSolid()){
					undo.addBlock(loc.getBlock().getState());
					BlockState state = loc.getBlock().getState();
					state.setBlockData(data);
					state.update(true);
				}
			}
			else if(mode == BuildMode.BREAK && loc.getBlock().getType() != Material.AIR && !loc.getBlock().isLiquid()){
				undo.addBlock(loc.getBlock().getState());
				loc.getBlock().setType(Material.AIR);
			}
			else if(mode == BuildMode.REPLACE && loc.getBlock().getType() != Material.AIR){
				undo.addBlock(loc.getBlock().getState());
				BlockState state = loc.getBlock().getState();
				state.setBlockData(data);
				state.update(true);
			}
			else if(mode == BuildMode.OVERWRITE){
				undo.addBlock(loc.getBlock().getState());
				BlockState state = loc.getBlock().getState();
				state.setBlockData(data);
				state.update(true);
			}
		}
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
	
	public static ItemStack getHelpBook(){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setAuthor("BuildTools");
		meta.setTitle("BuildTools Help");
		String intro = "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Basic Help:\n" + ChatColor.RESET
						+ "BuildTools is made around placing and breaking blocks rather than using commands to "
						+ "build structures. To enable BuildTools, simply type '/buildtools' or '/bt'. "
						+ "To disable it again, simply type this command again. You can also use '/bt on' and "
						+ "'/bt off' to turn BuildTools on and off respectively.\n"
						+ "Alternatively you can use the BuildTools Menu by typing '/btm' ('/bt m', '/buildtools menu'). "
						+ "Here you can change settings and enable or disable BuildTools.\n"
						+ "By default, you'll have the 'Cuboid' type and 'None' pattern selected, this will "
						+ "generate a basic cuboid, depending on your selection type, more on these on the next "
						+ "page. While having BuildTools enabled, you can sneak (shift + left/right click by default) "
						+ "to be able to break/place blocks without "
						+ "using the BuildTools functions. This is useful if you need to tower up to a point to fill "
						+ "your selection in.";
		String selections = "" + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Selection Types:\n" + ChatColor.RESET
							+ "Depending on what you doing, you may want to remove blocks, place blocks, or "
							+ "replace blocks in your selection area. This is where selection types come in.";
		String fill = ChatColor.LIGHT_PURPLE + "Fill Type:\n" + ChatColor.RESET
							+ "You can fill an area by simply placing blocks, for a cuboid, place a block at one "
							+ "corner of your area, and then again at the second. The block you placed at both points "
							+ "will then fill in the area. This does not remove any blocks already in the area.";
		String remove = ChatColor.LIGHT_PURPLE + "Remove Type:\n" + ChatColor.RESET
							+ "To remove blocks instead of place them, break the blocks at each corner of your selection. "
							+ "This will remove all blocks (excluding liquids like water and lava) from the area.";
		String replace = ChatColor.LIGHT_PURPLE + "Replace Type:\n" + ChatColor.RESET
							+ "You can replace the blocks in an area too (excluding air blocks). To do this, break a block "
							+ "in the first corner of the selection, then place a block in the second corner. This will replace "
							+ "all the blocks with the block you placed at the end.";
		String howItWorks = ChatColor.LIGHT_PURPLE + "How block generation works:\n" + ChatColor.RESET
							+ "Something that isn't easy to understand is how the blocks generate."
							+ "Think of it this way, when you select two points in the area you want to fill, BuildTools "
							+ "will go through the area and depending on the pattern and type you selected, it will select "
							+ "certain blocks in that area. So when it goes to the selection type, the blocks will change "
							+ "depending on that (break, place or replace). "
							+ "For example, the cave type will select blocks that resemble a cave "
							+ "formation, therefore if you use a fill type, it'll only place new blocks, and not cut out "
							+ "a cave. However if you use the remove selection type, you will get a cave.";
		String cancelling = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Canceling a Large Generation:\n" + ChatColor.RESET
							+ "You may want to cancel a generation if you find its taking too long or is too large. To do this "
							+ "use '/bt cancel' and the generation will stop as soon as it can.";
		String needMoreHelp = ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Need More Help?\n" + ChatColor.RESET
							+ "If you want more information on using commands, you can use '/bt help <command name>' and "
							+ "it will bring up some info on the command you entered. Remember you don't have to use these "
							+ "commands, the menu can do all the functions as well. Use '/bt help commands' to show a list "
							+ "of all commands so you know what to enter when using the command help.";
		for(String page : makePages(intro)){
			meta.addPage(page);
		}
		for(String page : makePages(selections)){
			meta.addPage(page);
		}
		for(String page : makePages(fill)){
			meta.addPage(page);
		}
		for(String page : makePages(remove)){
			meta.addPage(page);
		}
		for(String page : makePages(replace)){
			meta.addPage(page);
		}
		for(String page : makePages(howItWorks)){
			meta.addPage(page);
		}
		for(String page : makePages(cancelling)){
			meta.addPage(page);
		}
		for(String page : makePages(needMoreHelp)){
			meta.addPage(page);
		}
		book.setItemMeta(meta);
		return book;
	}
	
	public static List<String> makePages(String entry){
		int ind = 0;
		List<String> out = new ArrayList<>();
		
		while(ind <= entry.length()){
			int nind = ind + 245;
			if(nind < entry.length()){
				if(entry.charAt(nind) != ' '){
					while(entry.charAt(nind) != ' ' && nind != 0){
						nind--;
					}
					if(nind == 0){
						nind = ind + 245;
					}
				}
				out.add(entry.substring(ind, nind));
				ind = nind + 1;
			}
			else{
				out.add(entry.substring(ind));
				break;
			}
		}
		
		return out;
	}
	
	public static List<String> wordWrap(String entry){
		int ind = 0;
		List<String> out = new ArrayList<>();
		
		while(ind <= entry.length()){
			int nind = ind + 40;
			if(nind < entry.length()){
				if(entry.charAt(nind) != ' '){
					while(entry.charAt(nind) != ' ' && nind != 0){
						nind--;
					}
					if(nind == 0){
						nind = ind + 40;
					}
				}
				out.add(entry.substring(ind, nind));
				ind = nind + 1;
			}
			else{
				out.add(entry.substring(ind));
				break;
			}
		}
		
		return out;
	}
	
	public static int getDistance(int pos1, int pos2){
		if(pos1 > pos2){
			return Math.abs(pos1 - pos2);
		}
		else{
			return Math.abs(pos2 - pos1);
		}
	}
}
