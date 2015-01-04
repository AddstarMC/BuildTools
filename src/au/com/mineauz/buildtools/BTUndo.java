package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

public class BTUndo {
	private BTPlayer player = null;
	private List<BlockState> blocks = new ArrayList<BlockState>();
	private List<ItemStack> items = new ArrayList<ItemStack>();
	
	public BTUndo(BTPlayer player){
		this.player = player;
	}
	
	public BTUndo(BTPlayer player, List<BlockState> blocks, List<ItemStack> items){
		this.player = player;
		this.blocks = blocks;
		this.items = items;
	}
	
	public void addBlock(BlockState state){
		blocks.add(state);
	}
	
	public void addItem(ItemStack usedItem){
		items.add(usedItem);
	}
	
	public BTUndo restoreBlocks(){
		BTUndo undo;
		undo = new BTUndo(player);
		player.setCanBuild(false);
		new Generator(this.clone(), undo);
		return undo;
	}
	
	public List<BlockState> getBlockStates(){
		return blocks;
	}
	
	public BTPlayer getPlayer(){
		return player;
	}
	
	public List<ItemStack> getItems(){
		return items;
	}
	
	@Override
	public BTUndo clone(){
		return new BTUndo(player, new ArrayList<>(blocks), new ArrayList<>(items));
	}
}
