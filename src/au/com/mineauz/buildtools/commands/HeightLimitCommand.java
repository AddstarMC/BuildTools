package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.PlayerData;

public class HeightLimitCommand implements ICommand{
	
	@Override
	public String getName() {
		return "height";
	}

	@Override
	public String[] getAliases() {
		return new String[] {
				"ht"
		};
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
		return "Adds, modifies and removes height limits.";
	}

	@Override
	public String[] getUsage() {
		return new String[] {
				"add <name> <min> <max>",
				"remove <name>",
				"set <name> <min> <max>",
				"list"
		};
	}

	@Override
	public String getPermission() {
		return "buildtools.command.heightlimit";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		PlayerData pd = BTPlugin.plugin.getPlayerData();
		BTPlayer ply = null;
		if(sender instanceof Player)
			ply = pd.getBTPlayer((Player)sender);
		if(args != null){
			if(args[0].equalsIgnoreCase("add") && args.length >= 4 && args[2].matches("[0-9]+") && args[3].matches("[0-9]+")){
				String hln = args[1];
				int min = Integer.valueOf(args[2]);
				int max = Integer.valueOf(args[3]);
				if(max < min){
					int tmp = min;
					min = max;
					max = tmp;
				}
				if(!pd.hasHeightLimits(hln)){
					pd.addHeightLimits(hln, min, max);
					pd.saveHeightLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Added height limit '" + hln + "' "
								+ "with a limit between " + min + " and " + max + ".");
					else
						ply.sendMessage("Added height limit '" + hln + "' "
								+ "with a limit between " + min + " and " + max + ".", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "A height limit already exists by the name '" + hln + "'.");
					else
						ply.sendMessage("A height limit already exists by the name '" + hln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("remove") && args.length >= 2){
				String hln = args[1];
				if(pd.hasHeightLimits(hln)){
					pd.removeHeightLimits(hln);
					pd.saveHeightLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Removed height limit '" + hln + "'.");
					else
						ply.sendMessage("Removed height limit '" + hln + "'.", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No height limit exists by the name '" + hln + "'.");
					else
						ply.sendMessage("No height limit exists by the name '" + hln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("set") && args.length >= 4 && args[2].matches("[0-9]+") && args[3].matches("[0-9]+")){
				String hln = args[1];
				int min = Integer.valueOf(args[2]);
				int max = Integer.valueOf(args[3]);
				if(max < min){
					int tmp = min;
					min = max;
					max = tmp;
				}
				if(pd.hasHeightLimits(hln)){
					pd.modifyHeightLimits(hln, min, max);
					pd.saveHeightLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Modified height limit '" + hln + "' "
								+ "with a limit between " + min + " and " + max + ".");
					else
						ply.sendMessage("Modified height limit '" + hln + "' "
								+ "with a limit between " + min + " and " + max + ".", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No height limit exists by the name '" + hln + "'.");
					else
						ply.sendMessage("No height limit exists by the name '" + hln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")){
				sender.sendMessage(ChatColor.GREEN + "------------Height Limits------------");
				for(String limit : pd.getAllHeightLimits()){
					int min = pd.getHeightLimits(limit)[0];
					int max = pd.getHeightLimits(limit)[1];
					sender.sendMessage(ChatColor.AQUA + limit + ": "+ ChatColor.GRAY + min + "-" + max);
				}
				return true;
			}
		}
		return false;
	}
}
