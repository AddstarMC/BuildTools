package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.selections.BuildSelections;

public class SelectionCommand implements ICommand {

	@Override
	public String getName() {
		return "selection";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"select", "sel"};
	}

	@Override
	public boolean canBeConsole() {
		return false;
	}

	@Override
	public boolean canBeCommandBlock() {
		return false;
	}

	@Override
	public String[] getUsage() {
		return new String[] {"selection <Type>"};
	}
	
	@Override
	public String getPermission(){
		return "buildtools.command.selection";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null){
			BTPlayer pl = Main.plugin.getPlayerData().getBTPlayer((Player)sender);
			if(BuildSelections.hasSelection(args[0])){
				pl.setSelection(args[0].toUpperCase());
				pl.sendMessage("Set selection type to " + pl.getSelection().getName().toLowerCase(), ChatColor.AQUA);
				if(args.length > 1){
					String[] s = new String[args.length - 1];
					for(int i = 1; i < args.length; i++){
						s[i - 1] = args[i];
					}
					pl.setSSettings(s);
				}
				else{
					pl.setSSettings(new String[0]);
				}
			}
			else
				pl.sendMessage(ChatColor.RED + "No selection type by the name '" + args[0] + "'", ChatColor.RED);
			return true;
		}
		return false;
	}

}