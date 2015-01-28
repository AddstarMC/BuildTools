package au.com.mineauz.buildtools.types;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.mineauz.buildtools.exceptions.DuplicateTypeException;
import au.com.mineauz.buildtools.exceptions.UnknownTypeException;

public class BuildTypes {
	
	private Map<String, BuildType> selections = new HashMap<String, BuildType>();
	
	public BuildTypes(){
		addType(new CuboidType());
		addType(new TerrainType());
		addType(new OverlayType());
		addType(new TerrainBlobType());
		addType(new SphereType());
		addType(new CopyType());
		addType(new PasteType());
		addType(new CylinderType());
		addType(new CaveType());
	}
	
	public List<String> getAllTypes(){
		return new ArrayList<>(selections.keySet());
	}
	
	public BuildType getType(String name) throws UnknownTypeException{
		if(hasType(name))
			return selections.get(name.toUpperCase());
		else
			throw new UnknownTypeException(name);
	}
	
	public void addType(BuildType type) throws DuplicateTypeException{
		if(!hasType(type.getName()))
			selections.put(type.getName().toUpperCase().replace(" ", "_"), type);
		else
			throw new DuplicateTypeException(type.getName());
	}
	
	public boolean hasType(String name){
		return selections.containsKey(name.toUpperCase());
	}

}
