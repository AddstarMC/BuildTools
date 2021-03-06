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
import au.com.mineauz.buildtools.BTPlugin;

public class CommandDispatcher implements CommandExecutor, TabCompleter{
	private static Map<String, ICommand> commands = new HashMap<>();
	
	static{
		addCommand(new TypeCommand());
		addCommand(new PatternCommand());
		addCommand(new UndoCommand());
		addCommand(new RedoCommand());
		addCommand(new DebugCommand());
		addCommand(new CopyRotateCommand());
		addCommand(new HelpCommand());
		addCommand(new VolumeLimitCommand());
		addCommand(new HeightLimitCommand());
		addCommand(new BlockLimitCommand());
		addCommand(new MenuCommand());
		addCommand(new OnCommand());
		addCommand(new OffCommand());
		addCommand(new CancelCommand());
		addCommand(new ProtectionOverrideCommand());
		addCommand(new InvertSneakCommand());
	}
	
	public static void addCommand(ICommand command){
		commands.put(command.getName().toLowerCase(), command);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command,
			String cmd, String[] args) {
		if(args.length > 0){
			if(args.length == 1)
				return BTUtils.tabComplete(args[0], new ArrayList<>(commands.keySet()));
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
						System.arraycopy(args, 1, nargs, 0, args.length - 1);
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
						System.arraycopy(args, 1, nargs, 0, args.length - 1);
					}
					
					if((isConsole && !icmd.canBeConsole()) ||
							(isCommandBlock && !icmd.canBeCommandBlock())){
						sender.sendMessage(ChatColor.RED + "Sorry, this command can't be used under your conditions!");
						return true;
					}
					
					boolean ret = icmd.onCommand(sender, nargs);
					if(!ret){
						sender.sendMessage(ChatColor.GREEN + "------------Command Help------------");
						if(icmd.getInfo() != null)
							sender.sendMessage(ChatColor.AQUA + "Info: " + ChatColor.GRAY + icmd.getInfo());
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
			BTPlayer pl = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
			if(cmd.equals("btm")){
				commands.get("menu").onCommand(sender, args);
			}
			else{
				if(!pl.isBuildModeActive()){
					if(pl.isInCreative()){
						pl.setBuildModeActive(true);
						pl.sendMessage("Build mode active!", ChatColor.AQUA);
						pl.sendMessage(ChatColor.AQUA + "Mode: " + ChatColor.WHITE + BTUtils.capitalize(pl.getType().getName()));
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
			}
			return true;
		}
		return false;
	}
	
	public static List<ICommand> getAllCommands(){
		return new ArrayList<>(commands.values());
	}

}
