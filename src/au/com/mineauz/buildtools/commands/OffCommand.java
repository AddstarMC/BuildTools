package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

public class OffCommand implements ICommand{

	@Override
	public String getName() {
		return "off";
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
		return "Turns off build mode.";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

	@Override
	public String getPermission() {
		return "buildtools.command";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer player = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
		player.setBuildModeActive(true);
		player.sendMessage("Build mode deactivated.", ChatColor.RED);
		return true;
	}

}
