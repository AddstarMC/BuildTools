package au.com.mineauz.buildtools;

import org.bukkit.Chunk;

public class BTChunk {
	private String world;
	private int x;
	private int z;
	private int timesRegistered = 1;
	
	public BTChunk(Chunk chunk){
		world = chunk.getWorld().getName();
		x = chunk.getX();
		z = chunk.getZ();
	}
	
	public int getTimesRegistered(){
		return timesRegistered;
	}
	
	public void addTimeRegistered(){
		timesRegistered++;
	}
	
	public void removeTimeRegistered(){
		timesRegistered--;
	}
	
	public Chunk getChunk(){
		return BTPlugin.plugin.getServer().getWorld(world).getChunkAt(x, z);
	}
}
