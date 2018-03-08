package au.com.mineauz.buildtools.menu;

import java.util.Collections;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import au.com.mineauz.buildtools.BTPlayer;

public class MenuItemInteger extends MenuItemValue<Integer> {
	private int min;
	private int max;
	
	public MenuItemInteger(String name, MaterialData displayItem, Callback<Integer> callback, int min, int max) {
		super(name, displayItem, callback);
		this.min = min;
		this.max = max;
	}
	public MenuItemInteger(String name, String description, Material displayItem, Callback<Integer> callback, int min, int max) {
		super(name, description, displayItem, callback);
		this.min = min;
		this.max = max;
	}
	public MenuItemInteger(String name, Material displayItem, Callback<Integer> callback, int min, int max) {
		super(name, null, displayItem, callback);
		this.min = min;
		this.max = max;
	}
	public MenuItemInteger(String name, String description, MaterialData displayItem, Callback<Integer> callback, int min, int max) {
		super(name, description, displayItem, callback);
		this.min = min;
		this.max = max;
	}
	
	@Override
	protected List<String> getValueDescription(Integer value) {
		return Collections.singletonList(ChatColor.GREEN.toString() + value);
	}
	
	@Override
	protected Integer increaseValue(Integer current, boolean shift) {
		int value = current + (shift ? 10 : 1);
		if (value > max) {
			value = max;
		}
		
		return value;
	}
	
	@Override
	protected Integer decreaseValue(Integer current, boolean shift) {
		int value = current - (shift ? 10 : 1);
		if (value < min) {
			value = min;
		}
		
		return value;
	}
	
	@Override
	protected boolean isManualEntryAllowed() {
		return true;
	}
	
	@Override
	protected String getManualEntryText() {
		return "Enter number value into chat for " + getName();
	}
	
	@Override
	protected int getManualEntryTime() {
		return 10;
	}
	
	@Override
	protected void onManualEntryStart(BTPlayer player) {
		String min;
		String max = "N/A";
		if(this.min == Integer.MIN_VALUE) {
			min = "N/A";
		} else {
			min = String.valueOf(this.min);
		}
		
		if(this.max == Integer.MAX_VALUE) {
			max = "N/A";
		} else {
			max = String.valueOf(this.max);
		}
		
		player.sendMessage("Min: " + min + ", Max: " + max);
	}
	
	@Override
	protected Integer onManualEntryComplete(BTPlayer player, String raw) throws IllegalArgumentException {
		if(raw.matches("-?[0-9]+")) {
			int value = Integer.parseInt(raw);
			value = Math.min(Math.max(value, min), max);
			return value;
		}
		
		throw new IllegalArgumentException("Invalid value entry!");
	}
}
