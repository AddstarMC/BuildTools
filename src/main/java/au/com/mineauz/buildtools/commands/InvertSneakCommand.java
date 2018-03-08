package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

public class InvertSneakCommand implements ICommand {

	@Override
	public String getName() {
		return "invertsneak";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"invsneak", "togglesneak", "togsneak"};
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
		return "Inverts sneaking mode on BuildTools. By default, "
				+ "sneaking will stop BuildTools from working, if this "
				+ "is toggled, it will only work while sneaking.";
	}

	@Override
	public String[] getUsage() {
		return null;
	}

	@Override
	public String getPermission() {
		return "buildtools.command.invertsneak";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer player = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
		boolean inv = player.toggleSneakInversion();
		if(inv)
			player.sendMessage(ChatColor.AQUA + "Sneak Inversion has been enabled.");
		else
			player.sendMessage(ChatColor.RED + "Sneak Inversion has been disabled.");
		return true;
	}

}
