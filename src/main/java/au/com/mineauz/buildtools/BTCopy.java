package au.com.mineauz.buildtools;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.block.data.BlockData;
import org.bukkit.material.Directional;

public class BTCopy {
	private Map<String, BlockData> materials = new HashMap<>();
	private boolean replacing = false;
	
	public void addState(IVector vec, BlockData data){
		if(!materials.containsKey(vec.toString())){
			materials.put(vec.toString(), data);
		}
	}
	
	public Map<IVector, BlockData> getMaterials(){
		Map<IVector, BlockData> mats = new HashMap<>();
		for(String v : materials.keySet()){
			mats.put(IVector.fromString(v), materials.get(v));
		}
		return mats;
	}
	
	public boolean isReplacing(){
		return replacing;
	}
	
	public void setReplacing(boolean replacing){
		this.replacing = replacing;
	}
	
	public void rotate(int angle){
		if(angle == 90 || angle == -270 || angle == 180 || angle == -180 || angle == 270 || angle == -90){
			for(String s : materials.keySet()){
				if(materials.get(s) instanceof Directional){
					Directional d = (Directional)materials.get(s);
					if(materials.get(s).getMaterial().toString().contains("STAIRS"))
						d.setFacingDirection(BTUtils.rotate(d.getFacing(), angle * -1));
					else
						d.setFacingDirection(BTUtils.rotate(d.getFacing(), angle));
				}
			}
			
			if(angle == 90 || angle == -270){
				Map<String, BlockData> dat = new HashMap<>();
				for(String s :materials.keySet()){
					IVector vec = IVector.fromString(s);
					int x = vec.getX();
					int y = vec.getY();
					int z = vec.getZ();
					if(vec.getX() >= 0 && vec.getZ() >= 0){
						x = vec.getZ() * -1;
						z = vec.getX();
					}
					else if(vec.getX() >= 0 && vec.getZ() <= 0){
						x = vec.getZ() * -1;
						z = vec.getX();
					}
					else if(vec.getX() <= 0 && vec.getZ() <= 0){
						x = vec.getZ() * -1;
						z = vec.getX();
					}
					else if(vec.getX() <= 0 && vec.getZ() >= 0){
						x = vec.getZ() * -1;
						z = vec.getX();
					}
					IVector vec2 = new IVector(x, y, z);
					dat.put(vec2.toString(), materials.get(s));
				}
				materials = dat;
			}
			else if(angle == 180 || angle == -180){
				Map<String, BlockData> dat = new HashMap<>();
				for(String s :materials.keySet()){
					IVector vec = IVector.fromString(s);
					int x = vec.getX() * -1;
					int y = vec.getY();
					int z = vec.getZ() * -1;
					IVector vec2 = new IVector(x, y, z);
					dat.put(vec2.toString(), materials.get(s));
				}
				materials = dat;
			}
			else if(angle == 270 || angle == -90){
				Map<String, BlockData> dat = new HashMap<>();
				for(String s :materials.keySet()){
					IVector vec = IVector.fromString(s);
					int x = vec.getX();
					int y = vec.getY();
					int z = vec.getZ();
					if(vec.getX() >= 0 && vec.getZ() >= 0){
						x = vec.getZ();
						z = vec.getX() * -1;
					}
					else if(vec.getX() >= 0 && vec.getZ() <= 0){
						x = vec.getZ();
						z = vec.getX() * -1;
					}
					else if(vec.getX() <= 0 && vec.getZ() <= 0){
						x = vec.getZ();
						z = vec.getX() * -1;
					}
					else if(vec.getX() <= 0 && vec.getZ() >= 0){
						x = vec.getZ();
						z = vec.getX() * -1;
					}
					IVector vec2 = new IVector(x, y, z);
					dat.put(vec2.toString(), materials.get(s));
				}
				materials = dat;
			}
		}
	}
	
}
