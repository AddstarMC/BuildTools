package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

public class MenuCommand implements ICommand {

	@Override
	public String getName() {
		return "menu";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"m"};
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
		return "Displays an interactive menu to change BuildTools options.";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

	@Override
	public String getPermission() {
		return "buildtools.command.menu";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer player = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
		player.openMenu();
		return true;
	}

}
