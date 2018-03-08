package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

public class ProtectionOverrideCommand implements ICommand{

	@Override
	public String getName() {
		return "protectionoverride";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"po", "protection", "protect"};
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
	public String getInfo() {
		return "Allows a player to bypass protection plugins, building anywhere.";
	}

	@Override
	public String[] getUsage() {
		return new String[] {"[player]"};
	}

	@Override
	public String getPermission() {
		return "buildtools.command.protectionoverride";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer splayer = null;
		if(sender instanceof Player){
			splayer = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
		}
		BTPlayer player = null;
		if(args != null && args.length >= 1){
			List<Player> plys = BTPlugin.plugin.getServer().matchPlayer(args[0]);
			if(!plys.isEmpty()){
				player = BTPlugin.plugin.getPlayerData().getBTPlayer(plys.get(0));
				sender.sendMessage(ChatColor.AQUA + "Toggled protection override to " + 
						player.toggleProtectionOverride() + " for " + player.getName());
			}
			else{
				sender.sendMessage(ChatColor.RED + "No player found by the name " + args[0]);
			}
		}
		else if(splayer != null){
			sender.sendMessage(ChatColor.AQUA + "Your protection override has been toggled to " + splayer.toggleProtectionOverride());
		}
		else{
			return false;
		}
		return true;
	}

}
