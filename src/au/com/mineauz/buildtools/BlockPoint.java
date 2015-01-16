package au.com.mineauz.buildtools;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

public class BlockPoint {
	private Location point;
	private BlockState prev;
	private Material material;
	private BuildMode mode;
	
	public BlockPoint(Location point, Material material, BlockState prev, BuildMode mode){
		this.point = point;
		this.material = material;
		this.prev = prev;
		this.mode = mode;
	}
	
	public Location getPoint(){
		return point.clone();
	}
	
	public Material getType(){
		return material;
	}
	
	public BuildMode getMode(){
		return mode;
	}
	
	public BlockState getPreviousState(){
		return prev;
	}
}
