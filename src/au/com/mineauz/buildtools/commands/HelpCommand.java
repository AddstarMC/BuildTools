package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.Main;

public class HelpCommand implements ICommand {

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String[] getAliases() {
		return null;
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
	public String getInfo() {
		return "Gives help?";
	}

	@Override
	public String[] getUsage() {
		return new String[] {
				"[Command]"
		};
	}

	@Override
	public String getPermission() {
		return "buildtools.command.help";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer ply = Main.plugin.getPlayerData().getBTPlayer((Player)sender);
		if(args == null){
			ply.sendMessage("For basic info on usage, type '/bt help basics'\n"
					+ "For info on a command, type '/bt help <Command>'\n"
					+ "To view commands, type '/bt help commands'", ChatColor.AQUA);
		}
		else{
			if(args[0].equalsIgnoreCase("basics")){
				ply.getPlayer().getInventory().addItem(BTUtils.getHelpBook());
				ply.sendMessage("You have been given the basic help book.", ChatColor.AQUA);
			}
			else if(args[0].equalsIgnoreCase("commands")){
				ply.sendMessage(ChatColor.AQUA + "/buildtools type");
				ply.sendMessage(ChatColor.GRAY + "Sets your building type.");
				ply.sendMessage(ChatColor.AQUA + "/buildtools pattern");
				ply.sendMessage(ChatColor.GRAY + "Sets your building pattern.");
				ply.sendMessage(ChatColor.AQUA + "/buildtools undo");
				ply.sendMessage(ChatColor.GRAY + "Undo your last build.");
				ply.sendMessage(ChatColor.AQUA + "/buildtools redo");
				ply.sendMessage(ChatColor.GRAY + "Redoes your last undo.");
				ply.sendMessage(ChatColor.AQUA + "/buildtools copyrotate");
				ply.sendMessage(ChatColor.GRAY + "Rotates the last copy on your clipboard.");
			}
			else{
				List<ICommand> commands = CommandDispatcher.getAllCommands();
				ICommand icmd = null;
				loop:		
				for(ICommand ic : commands){
					if(ic.getName().equalsIgnoreCase(args[0])){
						icmd = ic;
					}
					else if(ic.getAliases() != null){
						for(String al : ic.getAliases()){
							if(al.equalsIgnoreCase(args[0])){
								icmd = ic;
								break loop;
							}
						}
					}
				}
				
				if(icmd != null){
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
							sender.sendMessage(ChatColor.GRAY + "/buildtools" + " " + icmd.getName() + " " + use);
						}
					}
				}
			}
		}
		return true;
	}

}
