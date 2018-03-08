package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface ICommand {
	
	String getName();
	String[] getAliases();
	boolean canBeConsole();
	boolean canBeCommandBlock();
	String getInfo();
	String[] getUsage();
	String getPermission();
	
	List<String> onTabComplete(CommandSender sender, String[] args);
	
	boolean onCommand(CommandSender sender, String[] args);

}
