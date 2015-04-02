package au.com.mineauz.buildtools.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;

public class MenuItemBuildType extends MenuItem{
	
	private String type;
	private MenuItem typeItem;
	private MenuItem patItem;

	public MenuItemBuildType(Material displayItem, String type, MenuItem typeItem, MenuItem patItem) {
		super(BTUtils.capitalize(type), displayItem);
		this.type = type;
		this.typeItem = typeItem;
		this.patItem = patItem;
	}
	
	@Override
	public void onClick(BTPlayer player){
		player.setType(type.toUpperCase());
		player.setPattern("NONE");
		
		typeItem.setDescription("Current Type: " + BTUtils.capitalize(type));
		patItem.setDescription("Current Pattern: None");
		
		player.setTSettings(new String[0]);
		player.setPSettings(new String[0]);
		
		player.showPreviousMenu();
		
		player.sendMessage("Set type to " + BTUtils.capitalize(player.getType().getName()) + "\n"
				+ "Pattern reset to None.", ChatColor.AQUA);
	}

}
