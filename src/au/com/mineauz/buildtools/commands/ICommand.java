package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface ICommand {
	
	public String getName();
	public String[] getAliases();
	public boolean canBeConsole();
	public boolean canBeCommandBlock();
	public String getInfo();
	public String[] getUsage();
	public String getPermission();
	
	public List<String> onTabComplete(CommandSender sender, String[] args);
	
	public boolean onCommand(CommandSender sender, String[] args);

}
