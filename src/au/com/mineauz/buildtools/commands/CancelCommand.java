package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

public class CancelCommand implements ICommand {

	@Override
	public String getName() {
		return "cancel";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"c", "can", "cnl"};
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
		return "Cancels what you're currently generating.";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

	@Override
	public String getPermission() {
		return "buildtools.command.cancel";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer player = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
		if(player.getGenerator() != null){
			player.getGenerator().cancelGeneration();
			player.sendMessage("Canceling generation of blocks...", ChatColor.AQUA);
		}
		else{
			player.sendMessage("Nothing to cancel.", ChatColor.RED);
		}
		return true;
	}

}
