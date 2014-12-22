package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import au.com.mineauz.buildtools.patterns.BuildPattern;
import au.com.mineauz.buildtools.selections.BuildSelection;

public class BTUtils {
	
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
	
	public static boolean generateBlocks(BTPlayer player, BuildSelection selection, BuildPattern pattern, List<Location> points, boolean breaking){
		List<Location> locs = selection.execute(player.getPoints(), pattern);
		
		boolean crUnd = false;
		if(player == null || player.isInCreative())
			crUnd = true;
		BTUndo undo = new BTUndo(player, crUnd);
		
		selection.fill(locs, player, pattern, breaking, undo);
		
		if(player != null)
			player.addUndo(undo);
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean placeBlock(BTPlayer player, Location loc, MaterialData data, ItemStack usedItem, boolean breaking, BTUndo undo){
		if(!breaking && loc.getBlock().getType() == Material.AIR){
			if(player == null || player.isInCreative()){
				undo.addBlock(loc.getBlock().getState());
				BlockState state = loc.getBlock().getState();
				state.setType(data.getItemType());
				state.setData(data);
				state.update(true);
				return true;
			}
			else{
				BlockState state = loc.getBlock().getState();
				boolean takenItem = false;
				if(player.hasItem(usedItem)){
					player.removeItem(usedItem);
					undo.addItem(usedItem);
					takenItem = true;
				}
				else if(player.hasItem(data.toItemStack())){
					player.removeItem(data.toItemStack());
					undo.addItem(data.toItemStack());
					takenItem = true;
				}
				
				if(takenItem){
					undo.addBlock(state);
					state.setType(data.getItemType());
					state.setData(data);
					state.update(true);
					player.getPlayer().updateInventory();
					return true;
				}
				else{
					player.sendMessage("You do not have enough items to fill this selection!", ChatColor.RED);
					return false;
				}
			}
		}
		else if(breaking && loc.getBlock().getType() != Material.AIR){
			if(player == null || player.isInCreative()){
				undo.addBlock(loc.getBlock().getState());
				loc.getBlock().setType(Material.AIR);
				return true;
			}
			else{
				if(Main.plugin.getPlayerData().hasTool(player.getPlayer().getItemInHand().getType())){
					player.damageItem(player.getPlayer().getItemInHand());
					undo.addBlock(loc.getBlock().getState());

					Map<Integer, ItemStack> map;
					boolean droppedItems = false;
					for(ItemStack it : loc.getBlock().getDrops(player.getPlayer().getItemInHand())){
						 map = player.getPlayer().getInventory().addItem(it);
						 player.getPlayer().updateInventory();
						if(!map.isEmpty()){
							player.getLocation().getWorld().dropItemNaturally(player.getLocation(), it);
							droppedItems = true;
						}
					}
					
					loc.getBlock().setType(Material.AIR);
					
					if(droppedItems){
						player.sendMessage("Your inventory is full!", ChatColor.RED);
						return false;
					}
					return true;
				}
				else{
					player.sendMessage("Your tool broke or has been dropped.", ChatColor.RED);
					return false;
				}
			}
		}
		return true;
	}
	
	public static ItemStack getBlockDrop(Block block){
		if(!block.getDrops().isEmpty())
			return (ItemStack) block.getDrops().toArray()[0];
		return null;
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
}
