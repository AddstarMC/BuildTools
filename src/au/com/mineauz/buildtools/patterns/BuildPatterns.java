package au.com.mineauz.buildtools.patterns;

import java.util.HashMap;
import java.util.Map;

public class BuildPatterns {
	
	private static Map<String, BuildPattern> patterns = new HashMap<String, BuildPattern>();
	
	static{
		addPattern(new NonePattern());
		addPattern(new HollowPattern());
		addPattern(new FramePattern());
		addPattern(new WallPattern());
		addPattern(new RandomPattern());
	}
	
	public static void addPattern(BuildPattern pattern){
		patterns.put(pattern.getName().toUpperCase().replace(" ", "_"), pattern);
	}
	
	public static BuildPattern getPattern(String name){
		return patterns.get(name.toUpperCase());
	}
	
	public static boolean hasPattern(String name){
		return patterns.containsKey(name.toUpperCase());
	}
}
