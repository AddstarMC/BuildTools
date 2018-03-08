package au.com.mineauz.buildtools.patterns;

import java.util.List;

import org.bukkit.Location;

import au.com.mineauz.buildtools.BTPlayer;
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
	public String getHelpInfo(){
		return "Default generation, nothing will change what the type looks like.";
	}
	
	@Override
	public String[] getParameters(){
		return null;
	}

	@Override
	public boolean fitsPattern(BTPlayer player,
			Location block, List<BlockPoint> points, String[] settings) {
		return true;
	}

}
