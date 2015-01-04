package au.com.mineauz.buildtools.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.Main;

public class CommandDispatcher implements CommandExecutor, TabCompleter{
	private static Map<String, ICommand> commands = new HashMap<String, ICommand>();
	
	static{
		addCommand(new TypeCommand());
		addCommand(new PatternCommand());
		addCommand(new UndoCommand());
		addCommand(new RedoCommand());
		addCommand(new DebugCommand());
		addCommand(new CopyRotateCommand());
	}
	
	public static void addCommand(ICommand command){
		commands.put(command.getName().toLowerCase(), command);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String cmd, String[] args) {
		if(args.length > 0){
			if(args.length == 1)
				return BTUtils.tabComplete(args[0], new ArrayList<String>(commands.keySet()));
			else if(args.length > 1){
				String subcomd = args[0].toLowerCase();
				ICommand icmd = null;
				if(commands.containsKey(subcomd))
					icmd = commands.get(subcomd);
				else{
	loop:			for(ICommand ic : commands.values()){
						if(ic.getAliases() != null){
							for(String al : ic.getAliases()){
								if(al.equalsIgnoreCase(subcomd)){
									icmd = ic;
									break loop;
								}
							}
						}
					}
				}
				if(icmd != null){
					String[] nargs = null;
					if(args.length - 1 > 0){
						nargs = new String[args.length - 1];
						for(int i = 1; i < args.length; i++)
							nargs[i - 1] = args[i];
					}
					return icmd.onTabComplete(sender, nargs);
				}
			}
		}
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String cmd,
			String[] args) {
		boolean isConsole = false;
		boolean isCommandBlock = false;
		if(sender instanceof BlockCommandSender)
			isCommandBlock = true;
		else if(!(sender instanceof Player))
			isConsole = true;
		
		if(args.length > 0){
			String subcomd = args[0].toLowerCase();
			ICommand icmd = null;
			if(commands.containsKey(subcomd))
				icmd = commands.get(subcomd);
			else{
loop:			for(ICommand ic : commands.values()){
					if(ic.getAliases() != null){
						for(String al : ic.getAliases()){
							if(al.equalsIgnoreCase(subcomd)){
								icmd = ic;
								break loop;
							}
						}
					}
				}
			}
			
			if(icmd != null){
				if(icmd.getPermission() == null || sender.hasPermission(icmd.getPermission())){
					String[] nargs = null;
					if(args.length - 1 > 0){
						nargs = new String[args.length - 1];
						for(int i = 1; i < args.length; i++)
							nargs[i - 1] = args[i];
					}
					
					if((isConsole && !icmd.canBeConsole()) ||
							(isCommandBlock && !icmd.canBeCommandBlock())){
						sender.sendMessage(ChatColor.RED + "Sorry, this command can't be used under your conditions!");
						return true;
					}
					
					boolean ret = icmd.onCommand(sender, nargs);
					if(!ret){
						sender.sendMessage(ChatColor.GREEN + "------------Command Help------------");
						sender.sendMessage(ChatColor.AQUA + "Command: " + ChatColor.GRAY + icmd.getName());
						if(icmd.getAliases() != null){
							sender.sendMessage(ChatColor.AQUA + "Aliases: " + BTUtils.arrayToString(icmd.getAliases()));
						}
						if(icmd.getUsage() != null){
							sender.sendMessage(ChatColor.AQUA + "Usage:");
							for(String use : icmd.getUsage()){
								sender.sendMessage(ChatColor.GRAY + "/" + cmd + " " + icmd.getName() + " " + use);
							}
						}
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command!\n" + icmd.getPermission());
				}
				return true;
			}
		}
		else if(sender instanceof Player){
			BTPlayer pl = Main.plugin.getPlayerData().getBTPlayer((Player)sender);
			if(!pl.isBuildModeActive()){
				if(pl.isInCreative()){
					pl.setBuildModeActive(true);
					pl.sendMessage("Build mode active!", ChatColor.AQUA);
					pl.sendMessage(ChatColor.AQUA + "Mode: " + ChatColor.WHITE + BTUtils.capitalize(pl.getSelection().getName()));
					pl.sendMessage(ChatColor.AQUA + "Pattern: " + ChatColor.WHITE + BTUtils.capitalize(pl.getPattern().getName()));
				}
				else{
					pl.sendMessage("You must be in creative mode to use BuildTools!", ChatColor.RED);
				}
			}
			else{
				pl.setBuildModeActive(false);
				pl.sendMessage("Build mode deactivated.", ChatColor.RED);
			}
			return true;
		}
		return false;
	}

}
