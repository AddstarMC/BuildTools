package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.patterns.BuildPattern;
import au.com.mineauz.buildtools.patterns.BuildPatterns;

public class PatternCommand implements ICommand {

	@Override
	public String getName() {
		return "pattern";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"pat", "p"};
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
		List<String> patterns = BuildPatterns.getAllPatterns();
		return "Sets your build pattern for when BuildTools is enabled.\n"
				+ ChatColor.AQUA + "Possible Patterns: "
				+ BTUtils.listToString(patterns);
	}

	@Override
	public String[] getUsage() {
		return new String[] {"<Pattern>"};
	}
	
	@Override
	public String getPermission(){
		return "buildtools.command.pattern";
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
			if(args[0].equalsIgnoreCase("help") && args.length >= 2){
				if(BuildPatterns.hasPattern(args[1])){
					BuildPattern p = BuildPatterns.getPattern(args[1]);
					pl.sendMessage(ChatColor.GREEN + "--------------Pattern Help--------------");
					pl.sendMessage(ChatColor.AQUA + "Name: " + ChatColor.GRAY + BTUtils.capitalize(p.getName()));
					if(p.getHelpInfo() != null){
						pl.sendMessage(ChatColor.AQUA + "Help Info: " + ChatColor.GRAY + p.getHelpInfo());
					}
					if(p.getParameters() != null){
						pl.sendMessage(ChatColor.AQUA + "Parameters: ");
						for(String par : p.getParameters()){
							pl.sendMessage(ChatColor.GOLD + par);
						}
					}
					if(p.compatibleSelections() != null){
						pl.sendMessage(ChatColor.AQUA + "Compatible Types: " + BTUtils.listToString(p.compatibleSelections()));
					}
					pl.sendMessage(ChatColor.AQUA + "Match Materials: " + ChatColor.GRAY + p.useMaterialMatch());
				}
				else{
					pl.sendMessage("No type by the name '" + args[1] + "'", ChatColor.RED);
				}
			}
			else if(BuildPatterns.hasPattern(pat)){
				if(pl.hasPermission("buildtools.pattern." + args[0].toLowerCase())){
					boolean bool = pl.setPattern(pat);
					if(bool){
						pl.sendMessage("Your pattern has been set to " + BTUtils.capitalize(pat), ChatColor.AQUA);
						if(args.length > 1){
							String[] s = new String[args.length - 1];
							for(int i = 1; i < args.length; i++){
								s[i - 1] = args[i];
							}
							pl.setPSettings(s);
						}
						else{
							pl.setPSettings(new String[0]);
						}
					}
					else
						pl.sendMessage("The pattern " + pat + " is not compatible with the selection " + 
								BTUtils.capitalize(pl.getType().getName()), ChatColor.RED);
				}
				else
					pl.sendMessage("You do not have permission to use this pattern!", ChatColor.RED);
			}
			else{
				pl.sendMessage("No pattern found by the name " + pat, ChatColor.RED);
			}
			return true;
		}
		return false;
	}

}
