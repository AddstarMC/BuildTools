package au.com.mineauz.buildtools.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;

public class MenuItemPatternType extends MenuItem {
	
	private String pattern;
	private MenuItem toChange;

	public MenuItemPatternType(Material displayItem, String pattern, MenuItem changeDesc) {
		super(BTUtils.capitalize(pattern), displayItem);
		this.pattern = pattern;
		toChange = changeDesc;
	}
	
	@Override
	public void onClick(BTPlayer player){
		player.setPattern(pattern.toUpperCase());
		toChange.setDescription("Current Pattern: " + BTUtils.capitalize(pattern));
		
		player.setPSettings(new String[0]);
		
		player.showPreviousMenu();
		
		player.sendMessage("Your pattern has been set to " + BTUtils.capitalize(pattern), ChatColor.AQUA);
	}
}
