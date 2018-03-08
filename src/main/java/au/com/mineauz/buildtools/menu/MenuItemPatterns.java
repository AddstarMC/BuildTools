package au.com.mineauz.buildtools.menu;

import java.util.Collections;
import java.util.List;

import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.BTUtils;

public class MenuItemPatterns extends MenuItem {
	
	public MenuItemPatterns(BTPlayer player){
		super("Pattern Types", Material.CHEST);
		setDescription("Current Pattern: " + BTUtils.capitalize(player.getPattern().getName()));
	}
	
	@Override
	public void onClick(BTPlayer player){
		Menu patterns = new Menu(6, "Pattern Types");
		BTPlugin plugin = BTPlugin.plugin;
		MenuItemPatternType pt;
		List<String> patternList = plugin.getBuildPatterns().getAllPatterns();
		Collections.sort(patternList);
		
		for(String t : patternList){
			if(player.hasPermission("buildtools.pattern." + t.toLowerCase())){
				if(plugin.getBuildPatterns().getPattern(t).compatibleSelections() == null ||
						plugin.getBuildPatterns().getPattern(t).compatibleSelections().contains(player.getType().getName())){
					pt = new MenuItemPatternType(Material.ENDER_PEARL, t, this);
					patterns.addItem(pt);
				}
			}
		}
		
		patterns.displayMenu(player);
	}

}
