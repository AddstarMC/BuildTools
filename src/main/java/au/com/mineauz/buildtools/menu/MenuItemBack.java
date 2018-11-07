package au.com.mineauz.buildtools.menu;

import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;

public class MenuItemBack extends MenuItem{
	private MenuSession previous;
	
	public MenuItemBack(MenuSession previous) {
		super("Back", Material.REDSTONE_TORCH);
		this.previous = previous;
	}
	
	@Override
	public void onClick(BTPlayer player) {
		previous.current.displaySession(player, previous);
	}
}
