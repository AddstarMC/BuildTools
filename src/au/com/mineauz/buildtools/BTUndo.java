package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

public class BTUndo {
	private BTPlayer player = null;
	private List<BlockState> blocks = new ArrayList<BlockState>();
	private List<ItemStack> items = new ArrayList<ItemStack>();
	private boolean creativeUndo = false;
	
	public BTUndo(BTPlayer player, boolean creativeUndo){
		this.player = player;
		this.creativeUndo = creativeUndo;
	}
	
	public BTUndo(){
		creativeUndo = true;
	}
	
	public BTUndo(BTPlayer player, List<BlockState> blocks, List<ItemStack> items, boolean creativeUndo){
		this.player = player;
		this.blocks = blocks;
		this.items = items;
		this.creativeUndo = creativeUndo;
	}
	
	public void addBlock(BlockState state){
		blocks.add(state);
	}
	
	public void addItem(ItemStack usedItem){
		items.add(usedItem);
	}
	
	public BTUndo restoreBlocks(){
		BTUndo undo;
		if(player == null)
			undo = new BTUndo();
		else
			undo = new BTUndo(player, creativeUndo);
//		for(BlockState block : blocks){
//			if(creativeUndo){
//				undo.addBlock(block.getBlock().getState());
//				block.update(true);
//			}
//			else{
//				if(block.getType() != Material.AIR && player.hasItem(block.getData().toItemStack())){
//					player.removeItem(block.getData().toItemStack());
//					undo.addBlock(block.getBlock().getState());
//					block.update(true);
//				}
//				else if(block.getType() == Material.AIR){
//					undo.addBlock(block.getBlock().getState());
//					block.update(true);
//				}
//			}
//		}
		player.setCanBuild(false);
		new Generator(this.clone(), undo);
		return undo;
	}
	
	public List<BlockState> getBlockStates(){
		return blocks;
	}
	
	public boolean isCreativeUndo(){
		return creativeUndo;
	}
	
	public BTPlayer getPlayer(){
		return player;
	}
	
	public List<ItemStack> getItems(){
		return items;
	}
	
	@Override
	public BTUndo clone(){
		return new BTUndo(player, new ArrayList<>(blocks), new ArrayList<>(items), creativeUndo);
	}
}
