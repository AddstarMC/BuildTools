package au.com.mineauz.buildtools.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;

public class MenuItemHelp extends MenuItem {

	public MenuItemHelp() {
		super("Help Book", Material.BOOK);
	}
	
	@Override
	public void onClick(BTPlayer player){
		player.getPlayer().getInventory().addItem(BTUtils.getHelpBook());
		player.sendMessage("You have been given the help book.", ChatColor.AQUA);
	}

}
