package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitTask;

public class Generator implements Runnable{
	private Iterator<Location> locs;
	private Iterator<BlockState> states;
	private BukkitTask task;
	private BlockState block;
	private BTPlayer player;
	private BuildMode mode;
	private BTUndo undo;
	private BTUndo nundo;
	private BTCopy copy;
	private Chunk curChunk = null;
	
	public Generator(List<Location> locs, BlockState block, BTPlayer player, BuildMode mode, BTUndo undo){
		this.block = block;
		this.player = player;
		this.mode = mode;
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
		
		task = Bukkit.getScheduler().runTaskTimer(BTPlugin.plugin, this, 1, 1);
	}
	
	public Generator(BTUndo undo, BTUndo nundo){
		this.undo = undo;
		this.nundo = nundo;
		Collections.reverse(undo.getBlockStates());
		states = undo.getBlockStates().iterator();
		player = undo.getPlayer();
		
		task = Bukkit.getScheduler().runTaskTimer(BTPlugin.plugin, this, 1, 1);
	}
	
	public Generator(BTCopy copy, Location reference, BTUndo undo){
		this.copy = copy;
		this.undo = undo;
		player = undo.getPlayer();
		
		Location temp = reference.clone();
		Map<IVector, MaterialData> data = copy.getMaterials();
		List<BlockState> states = new ArrayList<BlockState>();
		for(IVector vec : data.keySet()){
			temp.setX(reference.getX() + vec.getX());
			temp.setY(reference.getY() + vec.getY());
			temp.setZ(reference.getZ() + vec.getZ());
			BlockState state = temp.getBlock().getState();
			state.setType(data.get(vec).getItemType());
			state.setData(data.get(vec));
			states.add(state);
		}
		Collections.sort(states, new Comparator<BlockState>() {

			@Override
			public int compare(BlockState o1, BlockState o2) {
				int comp = Integer.valueOf(o1.getChunk().getX()).compareTo(o2.getChunk().getX());
				if(comp != 0)
					return comp;
				comp = Integer.valueOf(o1.getChunk().getZ()).compareTo(o2.getChunk().getZ());
				if(comp != 0)
					return comp;
				return Integer.valueOf(o1.getLocation().getBlockY()).compareTo(o2.getLocation().getBlockY());
			}
		});
		this.states = states.iterator();
		
		task = Bukkit.getScheduler().runTaskTimer(BTPlugin.plugin, this, 1, 1);
	}
	
	@Override
	public void run() {
		long time = System.nanoTime();
		if(locs != null){
			MaterialData data = block.getData();
			while(locs.hasNext()){
				Location loc = locs.next();
				
				if(curChunk == null || loc.getChunk() != curChunk){
					if(curChunk != null)
						BTPlugin.plugin.getGeneratingChunks().removeGeneratingChunk(curChunk);
					curChunk = loc.getChunk();
					BTPlugin.plugin.getGeneratingChunks().addGeneratingChunk(curChunk);
				}
				
				BTUtils.placeBlock(player, loc, data, mode, undo);
				if(System.nanoTime() - time > BTPlugin.plugin.getMaxGeneratorDelay() * 1000000)
					return;
			}
		}
		else if(copy != null){
			while(states.hasNext()){
				BlockState state = states.next();
				
				if(curChunk == null || state.getChunk() != curChunk){
					if(curChunk != null)
						BTPlugin.plugin.getGeneratingChunks().removeGeneratingChunk(curChunk);
					curChunk = state.getChunk();
					BTPlugin.plugin.getGeneratingChunks().addGeneratingChunk(curChunk);
				}
				
				if(copy.isReplacing() || state.getBlock().getState().getType() == Material.AIR){
					Integer[] lim = BTPlugin.plugin.getPlayerData().getPlayerHightLimits(player);
					if(state.getLocation().getBlockY() >= lim[0] && state.getLocation().getY() <= lim[1]){
						BTUtils.placeBlock(player, state.getLocation(), state.getData(), BuildMode.OVERWRITE, undo);
					}
				}
				if(System.nanoTime() - time > BTPlugin.plugin.getMaxGeneratorDelay() * 1000000)
					return;
			}
		}
		else{
			while(states.hasNext()){
				BlockState state = states.next();
				
				if(curChunk == null || state.getChunk() != curChunk){
					if(curChunk != null)
						BTPlugin.plugin.getGeneratingChunks().removeGeneratingChunk(curChunk);
					curChunk = state.getChunk();
					BTPlugin.plugin.getGeneratingChunks().addGeneratingChunk(curChunk);
				}
				
				if(player != null){
					BTUtils.placeBlock(player, state.getLocation(), state.getData(), BuildMode.OVERWRITE, nundo);

					if(System.nanoTime() - time > BTPlugin.plugin.getMaxGeneratorDelay() * 1000000)
						return;
				}
				else{
					break;
				}
			}
		}
		if(player != null)
			player.setCanBuild(true);
		if(curChunk != null)
			BTPlugin.plugin.getGeneratingChunks().removeGeneratingChunk(curChunk);
		task.cancel();
	}
}
