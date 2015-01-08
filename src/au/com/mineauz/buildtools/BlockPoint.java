package au.com.mineauz.buildtools;

import org.bukkit.Location;
import org.bukkit.Material;

public class BlockPoint {
	private Location point;
	private Material material;
	private BuildMode mode;
	
	public BlockPoint(Location point, Material material, BuildMode mode){
		this.point = point;
		this.material = material;
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
}
