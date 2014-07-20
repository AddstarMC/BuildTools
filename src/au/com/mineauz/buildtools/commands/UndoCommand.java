package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.PlayerData;

public class UndoCommand implements ICommand {

	@Override
	public String getName() {
		return "undo";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"u"};
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
	public String[] getUsage() {
		return new String[] {""};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer player = PlayerData.getBTPlayer((Player)sender);
		player.undo();
		return true;
	}

}
