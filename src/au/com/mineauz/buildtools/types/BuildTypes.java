package au.com.mineauz.buildtools.types;

import java.util.HashMap;
import java.util.Map;

public class BuildTypes {
	
	private static Map<String, BuildType> selections = new HashMap<String, BuildType>();
	
	static{
		addSelection(new CuboidType());
		addSelection(new TerrainType());
	}
	
	public static BuildType getSelection(String name){
		return selections.get(name.toUpperCase());
	}
	
	public static void addSelection(BuildType selection){
		selections.put(selection.getName().toUpperCase().replace(" ", "_"), selection);
	}
	
	public static boolean hasSelection(String name){
		return selections.containsKey(name.toUpperCase());
	}

}
