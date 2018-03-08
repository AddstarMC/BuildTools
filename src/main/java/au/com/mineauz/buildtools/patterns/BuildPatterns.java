package au.com.mineauz.buildtools.patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.mineauz.buildtools.exceptions.DuplicatePatternException;
import au.com.mineauz.buildtools.exceptions.UnknownPatternException;

public class BuildPatterns {
	
	private Map<String, BuildPattern> patterns = new HashMap<>();
	
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
	
	public void addPattern(BuildPattern pattern) throws DuplicatePatternException{
		if(!hasPattern(pattern.getName()))
			patterns.put(pattern.getName().toUpperCase().replace(" ", "_"), pattern);
		else
			throw new DuplicatePatternException(pattern.getName());
	}
	
	public BuildPattern getPattern(String name) throws UnknownPatternException{
		if(hasPattern(name))
			return patterns.get(name.toUpperCase());
		else
			throw new UnknownPatternException(name);
	}
	
	public boolean hasPattern(String name){
		return patterns.containsKey(name.toUpperCase());
	}
}
