package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtectionPlugin {
	
	String getName();
	boolean canBuild(Player player, Location location);
}
