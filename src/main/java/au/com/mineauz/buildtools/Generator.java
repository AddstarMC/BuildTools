package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
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
	
	private boolean cancel = false;
	
	public Generator(List<Location> locs, BlockState block, BTPlayer player, BuildMode mode, BTUndo undo){
		this.block = block;
		this.player = player;
		this.mode = mode;
		this.undo = undo;
		
		locs.sort((o1, o2) -> {
            int comp = Integer.compare(o1.getChunk().getX(), o2.getChunk().getX());
            if (comp != 0)
                return comp;
            comp = Integer.compare(o1.getChunk().getZ(), o2.getChunk().getZ());
            if (comp != 0)
                return comp;
            return Integer.compare(o1.getBlockY(), o2.getBlockY());
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
		Map<IVector, BlockData> data = copy.getMaterials();
		List<BlockState> states = new ArrayList<>();
		for(IVector vec : data.keySet()){
			temp.setX(reference.getX() + vec.getX());
			temp.setY(reference.getY() + vec.getY());
			temp.setZ(reference.getZ() + vec.getZ());
			BlockState state = temp.getBlock().getState();
			state.setBlockData(data.get(vec));
			states.add(state);
		}
		states.sort((o1, o2) -> {
            int comp = Integer.compare(o1.getChunk().getX(), o2.getChunk().getX());
            if (comp != 0)
                return comp;
            comp = Integer.compare(o1.getChunk().getZ(), o2.getChunk().getZ());
            if (comp != 0)
                return comp;
            return Integer.compare(o1.getLocation().getBlockY(), o2.getLocation().getBlockY());
        });
		this.states = states.iterator();
		
		task = Bukkit.getScheduler().runTaskTimer(BTPlugin.plugin, this, 1, 1);
	}
	
	@Override
	public void run() {
		long time = System.nanoTime();
		if(!cancel){
			if(locs != null){
				BlockData data = block.getBlockData();
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
                Integer[] lim = BTPlugin.plugin.getPlayerData().getPlayerHeightLimits(player);
						if(state.getLocation().getBlockY() >= lim[0] && state.getLocation().getY() <= lim[1]){
							BTUtils.placeBlock(player, state.getLocation(), state.getBlockData(), BuildMode.OVERWRITE, undo);
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
						BTUtils.placeBlock(player, state.getLocation(), state.getBlockData(), BuildMode.OVERWRITE, nundo);
	
						if(System.nanoTime() - time > BTPlugin.plugin.getMaxGeneratorDelay() * 1000000)
							return;
					}
					else{
						break;
					}
				}
			}
		}
		if(player != null)
			player.setCanBuild(true);
		if(curChunk != null)
			BTPlugin.plugin.getGeneratingChunks().removeGeneratingChunk(curChunk);
		task.cancel();
		player.setGenerator(null);
	}
	
	public void cancelGeneration(){
		cancel = true;
	}
}
