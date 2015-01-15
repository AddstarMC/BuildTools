package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.PlayerData;

public class VolumeLimitCommand implements ICommand{

	@Override
	public String getName() {
		return "volume";
	}

	@Override
	public String[] getAliases() {
		return new String[] {
				"vol"
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
		return "Adds, modifies and removes volume limits.";
	}

	@Override
	public String[] getUsage() {
		return new String[] {
				"add <name> <volume>",
				"remove <name>",
				"set <name> <volume>",
				"list"
		};
	}

	@Override
	public String getPermission() {
		return "buildtools.command.volumelimit";
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
			if(args[0].equalsIgnoreCase("add") && args.length >= 3 && args[2].matches("[0-9]+")){
				String vln = args[1];
				int vol = Integer.valueOf(args[2]);
				if(!pd.hasVolumeLimit(vln)){
					pd.addVolumeLimit(vln, vol);
					pd.saveVolumeLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Added volume limit '" + vln + "' with a limit of " + vol + " blocks.");
					else
						ply.sendMessage("Added volume limit '" + vln + "' with a limit of " + vol + " blocks.", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "A volume limit already exists by the name '" + vln + "'.");
					else
						ply.sendMessage("A volume limit already exists by the name '" + vln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("remove") && args.length >= 2){
				String vln = args[1];
				if(pd.hasVolumeLimit(vln)){
					pd.removeVolumeLimit(vln);
					pd.saveVolumeLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Removed volume limit '" + vln + "'.");
					else
						ply.sendMessage("Removed volume limit '" + vln + "'.", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No volume limit exists by the name '" + vln + "'.");
					else
						ply.sendMessage("No volume limit exists by the name '" + vln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("set") && args.length >= 3 && args[2].matches("[0-9]+")){
				String vln = args[1];
				int vol = Integer.valueOf(args[2]);
				if(pd.hasVolumeLimit(vln)){
					pd.modifyVolumeLimit(vln, vol);
					pd.saveVolumeLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Modified volume limit '" + vln + "' with a limit of " + vol + " blocks.");
					else
						ply.sendMessage("Modified volume limit '" + vln + "' with a limit of " + vol + " blocks.", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No volume limit exists by the name '" + vln + "'.");
					else
						ply.sendMessage("No volume limit exists by the name '" + vln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")){
				sender.sendMessage(ChatColor.GREEN + "------------Volume Limits------------");
				for(String limit : pd.getVolumeLimits()){
					sender.sendMessage(ChatColor.AQUA + limit + ": " + ChatColor.GRAY + pd.getVolumeLimit(limit) + " blocks");
				}
				return true;
			}
		}
		return false;
	}

}
