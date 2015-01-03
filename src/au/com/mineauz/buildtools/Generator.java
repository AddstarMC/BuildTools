package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;

public class Generator implements Runnable{
	private Iterator<Location> locs;
	private Iterator<BlockState> states;
	private BukkitTask task;
	private Block block;
	private BTPlayer player;
	private boolean breaking;
	private BTUndo undo;
	private BTUndo nundo;
	private BTCopy copy;
	
	public Generator(List<Location> locs, Block block, BTPlayer player, boolean breaking, BTUndo undo){
		this.block = block;
		this.player = player;
		this.breaking = breaking;
		this.undo = undo;
		
		Collections.sort(locs, new Comparator<Location>() {

			@Override
			public int compare(Location o1, Location o2) {
				int comp = Integer.valueOf(o1.getChunk().getX()).compareTo(o2.getChunk().getX());
				if(comp != 0)
					return comp;
				comp = Integer.valueOf(o1.getChunk().getZ()).compareTo(o2.getChunk().getZ());
				if(comp != 0)
					return comp;
				return Integer.valueOf(o1.getBlockY()).compareTo(o2.getBlockY());
			}
		});
		this.locs = locs.iterator();
		
		task = Bukkit.getScheduler().runTaskTimer(Main.plugin, this, 1, 1);
	}
	
	public Generator(BTUndo undo, BTUndo nundo){
		this.undo = undo;
		this.nundo = nundo;
		states = undo.getBlockStates().iterator();
		player = undo.getPlayer();
		
		task = Bukkit.getScheduler().runTaskTimer(Main.plugin, this, 1, 1);
	}
	
	public Generator(BTCopy copy, Location reference, BTUndo undo){
		this.copy = copy;
		this.undo = undo;
		
		Location temp = reference.clone();
		Map<IVector, MaterialData> data = copy.getMaterials();
		List<BlockState> states = new ArrayList<>();
		for(IVector vec : data.keySet()){
			temp.setX(reference.getX() + vec.getX());
			temp.setY(reference.getY() + vec.getY());
			temp.setZ(reference.getZ() + vec.getZ());
			BlockState state = temp.getBlock().getState();
			state.setType(data.get(vec).getItemType());
			state.setData(data.get(vec));
			states.add(state);
		}
		this.states = states.iterator();
		
		task = Bukkit.getScheduler().runTaskTimer(Main.plugin, this, 1, 1);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long time = System.nanoTime();
		if(locs != null){
			boolean succeed = false;
			ItemStack item = BTUtils.getBlockDrop(block);
			MaterialData data = block.getState().getData();
			while(locs.hasNext()){
				Location loc = locs.next();
				succeed = BTUtils.placeBlock(player, loc, data, item, breaking, undo);
				if(!succeed)
					break;
				if(System.nanoTime() - time > 10000000)
					return;
			}
		}
		else if(copy != null){
			while(states.hasNext()){
				BlockState state = states.next();
				if(copy.isReplacing() || state.getBlock().getState().getType() == Material.AIR){
					Integer[] lim = Main.plugin.getPlayerData().getPlayerHightLimits(player);
					if(state.getLocation().getBlockY() >= lim[0] && state.getLocation().getY() <= lim[1]){
						undo.addBlock(state.getLocation().getBlock().getState());
						state.update(true);
					}
				}
				if(System.nanoTime() - time > 10000000)
					return;
			}
		}
		else{
			while(states.hasNext()){
				BlockState state = states.next();
				if(player != null){
					if(nundo.isCreativeUndo()){
						nundo.addBlock(state.getBlock().getState());
						state.update(true);
					}
					else{
						if(state.getType() != Material.AIR && player.hasItem(state.getData().toItemStack())){
							player.removeItem(state.getData().toItemStack());
							nundo.addBlock(state.getBlock().getState());
							state.update(true);
						}
						else if(state.getType() == Material.AIR){
							nundo.addBlock(state.getBlock().getState());
							state.update(true);
						}
					}

					if(System.nanoTime() - time > 10000000)
						return;
				}
				else{
					break;
				}
			}
			if(player != null){
				for(ItemStack i : undo.getItems()){
					Map<Integer, ItemStack> its = player.getPlayer().getInventory().addItem(i);
					nundo.addItem(i);
					if(!its.isEmpty()){
						for(ItemStack it : its.values())
							player.getLocation().getWorld().dropItemNaturally(player.getLocation(), it);
					}
				}
				player.getPlayer().updateInventory();
			}
		}
		if(player != null)
			player.setCanBuild(true);
		task.cancel();
	}
}
