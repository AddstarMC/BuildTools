package au.com.mineauz.buildtools.patterns;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BlockPoint;

public class NonePattern implements BuildPattern {

	@Override
	public String getName() {
		return "NONE";
	}

	@Override
	public List<String> compatibleSelections() {
		return null;
	}

	@Override
	public boolean useMaterialMatch() {
		return true;
	}
	
	@Override
	public String[] getParameters(){
		return null;
	}

	@Override
	public boolean fitsPattern(Location block,
			List<BlockPoint> points, String[] settings) {
		return true;
	}

}
