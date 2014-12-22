package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import au.com.mineauz.buildtools.Main;

public class DebugCommand implements ICommand{

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public String[] getAliases() {
		return null;
	}

	@Override
	public boolean canBeConsole() {
		return true;
	}

	@Override
	public boolean canBeCommandBlock() {
		return false;
	}

	@Override
	public String[] getUsage() {
		return null;
	}
	
	@Override
	public String getPermission(){
		return "buildtools.command.debug";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(Main.plugin.isDebugging()){
			Main.plugin.setDebugging(false);
			sender.sendMessage(ChatColor.RED + "Stopped Debugging");
		}
		else{
			Main.plugin.setDebugging(true);
			sender.sendMessage(ChatColor.GOLD + "Started Debugging.");
		}
		return true;
	}

}
