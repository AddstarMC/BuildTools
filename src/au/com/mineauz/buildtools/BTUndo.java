package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
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
	
	public void addBlock(BlockState state){
		blocks.add(state);
	}
	
	public void addItem(ItemStack usedItem){
		items.add(usedItem);
	}
	
	@SuppressWarnings("deprecation")
	public BTUndo restoreBlocks(){
		BTUndo undo;
		if(player == null)
			undo = new BTUndo();
		else
			undo = new BTUndo(player, creativeUndo);
		for(BlockState block : blocks){
			if(creativeUndo){
				undo.addBlock(block.getBlock().getState());
				block.update(true);
			}
			else{
				if(block.getType() != Material.AIR && player.hasItem(block.getData().toItemStack())){
					player.removeItem(block.getData().toItemStack());
					undo.addBlock(block.getBlock().getState());
					block.update(true);
				}
				else if(block.getType() == Material.AIR){
					undo.addBlock(block.getBlock().getState());
					block.update(true);
				}
			}
		}
		for(ItemStack i : items){
			Map<Integer, ItemStack> its = player.getPlayer().getInventory().addItem(i);
			undo.addItem(i);
			if(!its.isEmpty()){
				for(ItemStack it : its.values())
					player.getLocation().getWorld().dropItemNaturally(player.getLocation(), it);
			}
		}
		if(player != null)
			player.getPlayer().updateInventory();
		return undo;
	}
}
