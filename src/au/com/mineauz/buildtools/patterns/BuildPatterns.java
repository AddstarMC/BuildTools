package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuildPatterns {
	
	private Map<String, BuildPattern> patterns = new HashMap<String, BuildPattern>();
	
	public BuildPatterns(){
		addPattern(new NonePattern());
		addPattern(new HollowPattern());
		addPattern(new FramePattern());
		addPattern(new WallPattern());
		addPattern(new RandomPattern());
	}
	
	public List<String> getAllPatterns(){
		return new ArrayList<>(patterns.keySet());
	}
	
	public void addPattern(BuildPattern pattern){
		patterns.put(pattern.getName().toUpperCase().replace(" ", "_"), pattern);
	}
	
	public BuildPattern getPattern(String name){
		return patterns.get(name.toUpperCase());
	}
	
	public boolean hasPattern(String name){
		return patterns.containsKey(name.toUpperCase());
	}
}
