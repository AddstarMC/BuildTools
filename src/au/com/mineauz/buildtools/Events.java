package au.com.mineauz.buildtools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Events implements Listener{
	
	@EventHandler
	private void playerLogin(PlayerJoinEvent event){
		PlayerData.addBTPlayer(event.getPlayer());
	}
	
	@EventHandler
	private void playerLogout(PlayerQuitEvent event){
		PlayerData.removeBTPlayer(event.getPlayer());
	}
	
	@EventHandler
	private void placeBlock(BlockPlaceEvent event){
		BTPlayer pl = PlayerData.getBTPlayer(event.getPlayer());
		if(pl == null) return;
		if(pl.isBuildModeActive() && !pl.getPlayer().isSneaking()){
			pl.addPoint(event.getBlockPlaced().getLocation());
			if(pl.getPointCount() >= pl.getSelection().getRequiredPointCount()){
				final BTPlayer fpl = pl;
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					
					@Override
					public void run() {
						BTUtils.generateBlocks(fpl, fpl.getSelection(), fpl.getPattern(), fpl.getPoints(), false);
						fpl.clearPoints();
					}
				});
				pl.sendMessage("Generating selected points...", ChatColor.AQUA);
			}
			else{
				pl.sendMessage("Added point to selection.", ChatColor.AQUA);
			}
		}
		else if(pl.isBuildModeActive() && pl.hasPoint(event.getBlock().getLocation())){
			pl.removePoint(event.getBlock().getLocation());
			pl.sendMessage("Removed point from selection.", ChatColor.RED);
		}
	}
	
	@EventHandler
	private void breakBlock(BlockBreakEvent event){
		BTPlayer pl = PlayerData.getBTPlayer(event.getPlayer());
		if(pl == null) return;
		if(pl.isBuildModeActive() && pl.hasPoint(event.getBlock().getLocation())){
			pl.removePoint(event.getBlock().getLocation());
			pl.sendMessage("Removed point from selection.", ChatColor.RED);
		}
		else if(pl.isBuildModeActive() && 
				PlayerData.hasTool(pl.getPlayer().getItemInHand().getType()) && 
				!pl.getPlayer().isSneaking()){
			pl.addPoint(event.getBlock().getLocation());
			if(pl.getPointCount() >= pl.getSelection().getRequiredPointCount()){
				final BTPlayer fpl = pl;
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.plugin, new Runnable() {
					
					@Override
					public void run() {
						BTUtils.generateBlocks(fpl, fpl.getSelection(), fpl.getPattern(), fpl.getPoints(), true);
						fpl.clearPoints();
					}
				});
				pl.sendMessage("Generating selected points...", ChatColor.AQUA);
			}
			else{
				pl.sendMessage("Added point to selection.", ChatColor.AQUA);
			}
		}
	}

}
