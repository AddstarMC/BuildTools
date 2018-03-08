package au.com.mineauz.buildtools.menu;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;

public class MenuItemRotate extends MenuItem {
	
	private int angle = 0;

	public MenuItemRotate(int angle) {
		super("Rotate " + angle + " degrees", Material.ARROW);
		this.angle = angle;
	}
	
	@Override
	public void onClick(BTPlayer player){
		if(player.hasCopy()){
			player.getCopy().rotate(angle);
			player.sendMessage("Your clipboard has been rotated " + angle + " degrees.", ChatColor.AQUA);
		}
		else{
			player.sendMessage("Nothing to rotate.", ChatColor.RED);
		}
	}

}
