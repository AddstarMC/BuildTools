package au.com.mineauz.buildtools.menu;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;

public class MenuItemString extends MenuItemValue<String> {
	private boolean allowNull = false;
	
	public MenuItemString(String name, String description, Material displayItem, Callback<String> callback) {
		super(name, description, displayItem, callback);
	}
	public MenuItemString(String name, Material displayItem, Callback<String> callback) {
		super(name, null, displayItem, callback);
	}
	
	public void setAllowNull(boolean allow){
		allowNull = allow;
	}
	
	@Override
	protected List<String> getValueDescription(String value) {
		if (value == null) {
			return Collections.singletonList(ChatColor.RED + "Not Set");
		} else {
			return Collections.singletonList(ChatColor.GREEN + StringUtils.abbreviate(value, 20));
		}
	}
	
	@Override
	protected String increaseValue(String current, boolean shift) {
		return current;
	}
	
	@Override
	protected String decreaseValue(String current, boolean shift) {
		return current;
	}
	
	@Override
	protected boolean isManualEntryAllowed() {
		return true;
	}
	
	@Override
	protected String getManualEntryText() {
		return "Enter string value into chat for " + getName();
	}
	
	@Override
	protected int getManualEntryTime() {
		return 40;
	}
	
	@Override
	protected void onManualEntryStart(BTPlayer player) {
		if(allowNull){
			player.sendMessage("Enter \"null\" to remove the string value");
		}
	}
	
	@Override
	protected String onManualEntryComplete(BTPlayer player, String raw) throws IllegalArgumentException {
		if (allowNull && raw.equalsIgnoreCase("null")) {
			return null;
		} else {
			return raw;
		}
		
	}
}
