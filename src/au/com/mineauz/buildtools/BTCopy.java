package au.com.mineauz.buildtools;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.material.MaterialData;

public class BTCopy {
	private Map<String, MaterialData> materials = new HashMap<>();
	private boolean replacing = false;
	
	public void addState(IVector vec, MaterialData data){
		if(!materials.containsKey(vec.toString())){
			materials.put(vec.toString(), data.clone());
		}
	}
	
	public Map<IVector, MaterialData> getMaterials(){
		Map<IVector, MaterialData> mats = new HashMap<>();
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
	
}
