package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockState;

public class BTUndo {
	private BTPlayer player = null;
	private List<BlockState> blocks = new ArrayList<>();
	
	public BTUndo(BTPlayer player){
		this.player = player;
	}
	
	public BTUndo(BTPlayer player, List<BlockState> blocks){
		this.player = player;
		this.blocks = blocks;
	}
	
	public void addBlock(BlockState state){
		blocks.add(state);
	}
	
	public BTUndo restoreBlocks(){
		BTUndo undo;
		undo = new BTUndo(player);
		player.setCanBuild(false);
		player.setGenerator(new Generator(this.clone(), undo));
		return undo;
	}
	
	public List<BlockState> getBlockStates(){
		return blocks;
	}
	
	public BTPlayer getPlayer(){
		return player;
	}
	
	@Override
	public BTUndo clone(){
		return new BTUndo(player, new ArrayList<>(blocks));
	}
}
