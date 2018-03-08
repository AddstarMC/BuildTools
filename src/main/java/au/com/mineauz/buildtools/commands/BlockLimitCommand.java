package au.com.mineauz.buildtools.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;
import au.com.mineauz.buildtools.BTUtils;
import au.com.mineauz.buildtools.PlayerData;

public class BlockLimitCommand implements ICommand {

	@Override
	public String getName() {
		return "blocks";
	}

	@Override
	public String[] getAliases() {
		return new String[] {
				"block"
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
		return "Adds, removes and modifies block limits.";
	}

	@Override
	public String[] getUsage() {
		return new String[] {
				"add <name>",
				"remove <name>",
				"addtype <name> <type>",
				"rmtype <name> <type>",
				"list"
		};
	}

	@Override
	public String getPermission() {
		return "buildtools.command.blocklimit";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		BTPlayer ply = null;
		PlayerData pd = BTPlugin.plugin.getPlayerData();
		if(sender instanceof Player)
			ply = pd.getBTPlayer((Player)sender);
		if(args != null){
			if(args[0].equalsIgnoreCase("add") && args.length >= 2){
				String bln = args[1];
				if(!pd.hasBlockLimits(bln)){
					pd.addBlockLimits(bln, new ArrayList<>());
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Added a block limit with the name '" + bln + "'.");
					else
						ply.sendMessage("Added a block limit with the name '" + bln + "'.", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "A block limit already exists by the name '" + bln + "'.");
					else
						ply.sendMessage("A block limit already exists by the name '" + bln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("remove") && args.length >= 2){
				String bln = args[1];
				if(pd.hasBlockLimits(bln)){
					pd.removeBlockLimits(bln);
					pd.saveBlockLimits();
					if(ply == null)
						sender.sendMessage(ChatColor.GOLD + "Removed a block limit with the name '" + bln + "'.");
					else
						ply.sendMessage("Removed a block limit with the name '" + bln + "'.", ChatColor.AQUA);
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No block limit exists by the name '" + bln + "'.");
					else
						ply.sendMessage("No block limit exists by the name '" + bln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("addtype") && args.length >= 3){
				String bln = args[1];
				Material mat = Material.matchMaterial(args[2].toUpperCase());
				if(pd.hasBlockLimits(bln)){
					if(mat != null){
						pd.getBlockLimits(bln).add(mat);
						pd.saveBlockLimits();
						if(ply == null)
							sender.sendMessage(ChatColor.GOLD + "Added '" + BTUtils.capitalize(mat.toString()) + "' to '" + bln + "'.");
						else
							ply.sendMessage("Added '" + BTUtils.capitalize(mat.toString()) + "' to '" + bln + "'.", ChatColor.AQUA);
					}
					else{
						if(ply == null)
							sender.sendMessage(ChatColor.RED + "No material exists by the name '" + args[2] + "'.");
						else
							ply.sendMessage("No material exists by the name '" + args[2] + "'.", ChatColor.RED);
					}
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No block limit exists by the name '" + bln + "'.");
					else
						ply.sendMessage("No block limit exists by the name '" + bln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("rmtype") && args.length >= 3){
				String bln = args[1];
				Material mat = Material.matchMaterial(args[2].toUpperCase());
				if(pd.hasBlockLimits(bln)){
					if(mat != null){
						if(pd.getBlockLimits(bln).contains(mat)){
							pd.getBlockLimits(bln).remove(mat);
							pd.saveBlockLimits();
							if(ply == null)
								sender.sendMessage(ChatColor.GOLD + "Removed '" + BTUtils.capitalize(mat.toString()) + "' from '" + bln + "'.");
							else
								ply.sendMessage("Removed '" + BTUtils.capitalize(mat.toString()) + "' from '" + bln + "'.", ChatColor.AQUA);
						}
						else{
							if(ply == null)
								sender.sendMessage(ChatColor.RED + "'" + bln + "' does not have the material '" + 
										BTUtils.capitalize(mat.toString()) + "'.");
							else
								ply.sendMessage("'" + bln + "' does not have the material '" + 
										BTUtils.capitalize(mat.toString()) + "'.", ChatColor.RED);
						}
					}
					else{
						if(ply == null)
							sender.sendMessage(ChatColor.RED + "No material exists by the name '" + args[2] + "'.");
						else
							ply.sendMessage("No material exists by the name '" + args[2] + "'.", ChatColor.RED);
					}
				}
				else{
					if(ply == null)
						sender.sendMessage(ChatColor.RED + "No block limit exists by the name '" + bln + "'.");
					else
						ply.sendMessage("No block limit exists by the name '" + bln + "'.", ChatColor.RED);
				}
				return true;
			}
			else if(args[0].equalsIgnoreCase("list")){
				sender.sendMessage(ChatColor.GREEN + "------------Block Limits------------");
				for(String limit : pd.getAllBlockLimits()){
					sender.sendMessage(ChatColor.AQUA + limit + ": " + ChatColor.GRAY + pd.getBlockLimits(limit).size() + " blocks");
				}
				return true;
			}
		}
		return false;
	}

}
