package au.com.mineauz.buildtools.types;

import java.util.HashMap;
import java.util.Map;

public class BuildTypes {
	
	private static Map<String, BuildType> selections = new HashMap<String, BuildType>();
	
	static{
		addType(new CuboidType());
		addType(new TerrainType());
		addType(new OverlayType());
	}
	
	public static BuildType getType(String name){
		return selections.get(name.toUpperCase());
	}
	
	public static void addType(BuildType selection){
		selections.put(selection.getName().toUpperCase().replace(" ", "_"), selection);
	}
	
	public static boolean hasType(String name){
		return selections.containsKey(name.toUpperCase());
	}

}
