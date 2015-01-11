package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.Main;
import au.com.mineauz.buildtools.types.BuildType;

public class TypeCommand implements ICommand {

	@Override
	public String getName() {
		return "type";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"t"};
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
		List<String> types = Main.plugin.getBuildTypes().getAllTypes();
		return "Sets your build type for when BuildTools is enabled. You can run '/bt type help <type>' for "
				+ "more information on that type.\n"
				+ ChatColor.AQUA + "Possible Types: "
				+ BTUtils.listToString(types);
	}

	@Override
	public String[] getUsage() {
		return new String[] {"<Type>", "help <type>"};
	}
	
	@Override
	public String getPermission(){
		return "buildtools.command.type";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null){
			BTPlayer pl = Main.plugin.getPlayerData().getBTPlayer((Player)sender);
			if(args[0].equalsIgnoreCase("help") && args.length >= 2){
				if(Main.plugin.getBuildTypes().hasType(args[1])){
					BuildType t = Main.plugin.getBuildTypes().getType(args[1]);
					pl.sendMessage(ChatColor.GREEN + "--------------Type Help--------------");
					pl.sendMessage(ChatColor.AQUA + "Name: " + ChatColor.GRAY + BTUtils.capitalize(t.getName()));
					if(t.getHelpInfo() != null){
						pl.sendMessage(ChatColor.AQUA + "Help Info: " + ChatColor.GRAY + t.getHelpInfo());
					}
					if(t.getParameters() != null){
						pl.sendMessage(ChatColor.AQUA + "Parameters: ");
						for(String p : t.getParameters()){
							pl.sendMessage(ChatColor.GOLD + p);
						}
					}
					pl.sendMessage(ChatColor.AQUA + "Required Points: " + ChatColor.GRAY + t.getRequiredPointCount());
				}
				else{
					pl.sendMessage("No type by the name '" + args[1] + "'", ChatColor.RED);
				}
			}
			else if(Main.plugin.getBuildTypes().hasType(args[0])){
				if(pl.hasPermission("buildtools.type." + args[0].toLowerCase())){
					pl.setType(args[0].toUpperCase());
					pl.sendMessage("Set type to " + BTUtils.capitalize(pl.getType().getName()) + "\n"
							+ "Pattern reset to None.", ChatColor.AQUA);
					if(args.length > 1){
						String[] s = new String[args.length - 1];
						for(int i = 1; i < args.length; i++){
							s[i - 1] = args[i];
						}
						pl.setTSettings(s);
					}
					else{
						pl.setTSettings(new String[0]);
					}
				}
				else{
					pl.sendMessage("You do not have permission to use this type!", ChatColor.RED);
				}
			}
			else
				pl.sendMessage("No type by the name '" + args[0] + "'", ChatColor.RED);
			return true;
		}
		return false;
	}

}
