package au.com.mineauz.buildtools.menu;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class MenuItemBoolean extends MenuItemValue<Boolean> {
	

	public MenuItemBoolean(String name, String description, Material displayItem, Callback<Boolean> callback) {
		super(name, description, displayItem, callback);
	}
	public MenuItemBoolean(String name, Material displayItem, Callback<Boolean> callback) {
		super(name, null, displayItem, callback);
	}
	
	@Override
	protected List<String> getValueDescription(Boolean value) {
		if(getValue()) {
			return Collections.singletonList(ChatColor.GREEN + "True");
		} else {
			return Collections.singletonList(ChatColor.GREEN + "False");
		}
	}
	
	@Override
	protected Boolean increaseValue(Boolean current, boolean shift) {
		return !current;
	}
	
	@Override
	protected Boolean decreaseValue(Boolean current, boolean shift) {
		return !current;
	}
}