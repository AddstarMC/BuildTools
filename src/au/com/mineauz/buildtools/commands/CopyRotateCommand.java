package au.com.mineauz.buildtools.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.Main;

public class CopyRotateCommand implements ICommand {

	@Override
	public String getName() {
		return "copyrotate";
	}

	@Override
	public String[] getAliases() {
		return new String[] {"crotate", "crot", "cr"};
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
		return new String[] {"<angle>"};
	}

	@Override
	public String getPermission() {
		return "buildtools.command.copyrotate";
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, String[] args) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender sender, String[] args) {
		if(args != null){
			BTPlayer player = Main.plugin.getPlayerData().getBTPlayer((Player)sender);
			if(player.hasCopy()){
				int angle = 0;
				if(args[0].matches("-?[0-9]+")){
					angle = Integer.valueOf(args[0]);
					if(angle % 90 == 0 && angle >= -270 && angle <= 270){
						player.getCopy().rotate(angle);
						player.sendMessage("Rotated clipboard by " + angle + " degrees.", ChatColor.AQUA);
					}
					else
						player.sendMessage("Angle must be divisible by 90!", ChatColor.RED);
				}
			}
			else{
				player.sendMessage("You have nothing copied!", ChatColor.RED);
			}
			return true;
		}
		return false;
	}

}
