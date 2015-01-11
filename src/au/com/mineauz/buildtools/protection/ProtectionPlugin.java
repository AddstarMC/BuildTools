package au.com.mineauz.buildtools.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface ProtectionPlugin {
	
	public String getName();
	public boolean canBuild(Player player, Location location);
}
