package au.com.mineauz.buildtools.selections;

import java.util.HashMap;
import java.util.Map;

public class BuildSelections {
	
	private static Map<String, BuildSelection> selections = new HashMap<String, BuildSelection>();
	
	static{
		addSelection(new CuboidSelection());
	}
	
	public static BuildSelection getSelection(String name){
		return selections.get(name);
	}
	
	public static void addSelection(BuildSelection selection){
		selections.put(selection.getName().toUpperCase().replace(" ", "_"), selection);
	}
	
	public static boolean hasSelection(String name){
		return selections.containsKey(name.toUpperCase());
	}

}
