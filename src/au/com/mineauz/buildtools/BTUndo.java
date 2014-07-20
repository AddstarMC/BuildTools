package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockState;

public class BTUndo {
	private List<BlockState> blocks = new ArrayList<BlockState>();
	private boolean creativeUndo = false;
	
	public BTUndo(boolean creativeUndo){
		this.creativeUndo = creativeUndo;
	}
	
	public void addBlock(BlockState state){
		blocks.add(state);
	}
	
	public BTUndo restoreBlocks(){
		BTUndo undo = new BTUndo(creativeUndo);
		for(BlockState block : blocks){
			if(creativeUndo){
				undo.addBlock(block.getBlock().getState());
				block.update(true);
			}
			else{
				//TODO: Survival Elements
				break;
			}
		}
		return undo;
	}
}
