package au.com.mineauz.buildtools.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;

public class MenuItemSettings extends MenuItem {
	
	private Callback<String> callback;
	private SettingType type;

	public MenuItemSettings(Callback<String> callback, SettingType type) {
		super("Type Settings", Material.PAPER);
		this.callback = callback;
		this.type = type;
		if(type == SettingType.PATTERN)
			setName("Pattern Setting");
	}
	
	@Override
	public void onClick(BTPlayer player){
		if((type == SettingType.TYPE && player.getType().getParameters() != null) || 
				player.getPattern().getParameters() != null){
			Menu m = new Menu(6, BTUtils.capitalize(type.toString()) + " Settings");
			MenuItemString tset = new MenuItemString(type.toString() + " Settings", Material.PAPER, callback);
			tset.setDoubleClickHandler(new IMenuItemClick() {
				
				@Override
				public void onClick(MenuItem menuItem, BTPlayer player) {
					player.sendMessage("Settings to use:");
					if(type == SettingType.TYPE){
						for(String s : player.getType().getParameters()){
							player.sendMessage(s);
						}
					}
					else{
						for(String s : player.getPattern().getParameters()){
							player.sendMessage(s);
						}
					}
				}
			});
			
			List<String> desc;
			String[] pars;
			if(type == SettingType.TYPE){
				desc = new ArrayList<String>(player.getType().getParameters().length);
				pars = player.getType().getParameters();
			}
			else{
				desc = new ArrayList<String>(player.getPattern().getParameters().length);
				pars = player.getPattern().getParameters();
			}
			for(String par : pars){
				List<String> wrap = BTUtils.wordWrap(par);
				for(int i = 0; i < wrap.size(); i++){
					wrap.set(i, ChatColor.GRAY + wrap.get(i));
				}
				desc.addAll(wrap);
			}
			tset.setDescription(desc);
			
			m.addItem(tset);
			m.displayMenu(player);
		}
		else{
			player.sendMessage("No " + type.toString().toLowerCase() + " settings available.", ChatColor.RED);
		}
	}
	
	public enum SettingType {
		TYPE,
		PATTERN;
	}

}
