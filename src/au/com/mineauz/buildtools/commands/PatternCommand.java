package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.patterns.BuildPatterns;

public class PatternCommand implements ICommand {

	@Override
	public String getName() {
		return "pattern";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"pat"};
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
		return new String[] {"<Pattern>"};
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null){
			BTPlayer pl = Main.plugin.getPlayerData().getBTPlayer((Player)sender);
			String pat = args[0].toUpperCase();
			if(BuildPatterns.hasPattern(pat)){
				boolean bool = pl.setPattern(pat);
				if(bool)
					pl.sendMessage("Your pattern has been set to " + pat, ChatColor.AQUA);
				else
					pl.sendMessage("The pattern " + pat + " is not compatible with the selection " + pl.getSelection().getName(), ChatColor.RED);
			}
			else{
				pl.sendMessage("No pattern found by the name " + pl, ChatColor.RED);
			}
			return true;
		}
		return false;
	}

}
