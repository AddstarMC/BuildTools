package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

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
	public String getInfo(){
		return null;
	}

	@Override
	public String[] getUsage() {
		return new String[] {""};
	}
	
	@Override
	public String getPermission(){
		return "buildtools.command.undo";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer player = BTPlugin.plugin.getPlayerData().getBTPlayer((Player)sender);
		if(player.canBuild())
			player.undo();
		else
			player.sendMessage("Still generating, please wait...", ChatColor.AQUA);
		return true;
	}

}
