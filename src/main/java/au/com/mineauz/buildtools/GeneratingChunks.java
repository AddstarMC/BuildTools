package au.com.mineauz.buildtools;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Chunk;

public class GeneratingChunks {
	
	private Map<String, BTChunk> genChunks = new HashMap<>();
	
	public void addGeneratingChunk(Chunk chunk){
		String cid = getChunkID(chunk);
		if(!genChunks.containsKey(cid))
			genChunks.put(cid, new BTChunk(chunk));
		else
			genChunks.get(cid).addTimeRegistered();
	}
	
	public void removeGeneratingChunk(Chunk chunk){
		String cid = getChunkID(chunk);
		if(genChunks.containsKey(cid)){
			if(genChunks.get(cid).getTimesRegistered() > 1)
				genChunks.get(cid).removeTimeRegistered();
			else
				genChunks.remove(cid);
		}
	}
	
	public boolean hasChunk(Chunk chunk){
		return genChunks.containsKey(getChunkID(chunk));
	}
	
	private String getChunkID(Chunk chunk){
		return chunk.getWorld().getName() + ";" + chunk.getX() + ";" + chunk.getZ();
	}
}
