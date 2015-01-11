package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildTypes {
	
	private Map<String, BuildType> selections = new HashMap<String, BuildType>();
	
	public BuildTypes(){
		addType(new CuboidType());
		addType(new TerrainType());
		addType(new OverlayType());
		addType(new CaveType());
		addType(new SphereType());
		addType(new CopyType());
		addType(new PasteType());
	}
	
	public List<String> getAllTypes(){
		return new ArrayList<>(selections.keySet());
	}
	
	public BuildType getType(String name){
		return selections.get(name.toUpperCase());
	}
	
	public void addType(BuildType selection){
		selections.put(selection.getName().toUpperCase().replace(" ", "_"), selection);
	}
	
	public boolean hasType(String name){
		return selections.containsKey(name.toUpperCase());
	}

}
