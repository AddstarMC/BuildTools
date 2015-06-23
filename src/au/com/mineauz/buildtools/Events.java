package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import au.com.mineauz.buildtools.exceptions.UnknownBTPlayerException;
import au.com.mineauz.buildtools.patterns.BuildPattern;
import au.com.mineauz.buildtools.types.BuildType;

public class Events implements Listener{
	
	private PlayerData pdata = BTPlugin.plugin.getPlayerData();
	private BTPlugin plugin = BTPlugin.plugin;
	
	@EventHandler(priority = EventPriority.LOWEST)
	private void playerLogin(PlayerJoinEvent event){
		pdata.addBTPlayer(event.getPlayer());
	}
	
	@EventHandler
	private void playerLogout(PlayerQuitEvent event){
		pdata.removeBTPlayer(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void placeBlock(BlockPlaceEvent event){
		BTPlayer pl;
		try{
			pl = pdata.getBTPlayer(event.getPlayer());
		}
		catch (UnknownBTPlayerException e){
			return;
		}
		
		if(pl.isBuildModeActive() && pl.hasPoint(event.getBlock().getLocation())){
			pl.removePoint(event.getBlock().getLocation());
			pl.sendMessage("Removed point from selection.", ChatColor.RED);
		}
		else if(pl.isBuildModeActive() && pl.canPlacePoint()){
			if(!pl.canBuild() && !pl.hasPoint(event.getBlock().getLocation())){
				pl.sendMessage("Still generating, please wait...", ChatColor.AQUA);
				event.setCancelled(true);
				return;
			}
			else if(!pl.canBuild() && pl.hasPoint(event.getBlock().getLocation())){
				pl.sendMessage("Cancelling block generation...", ChatColor.RED);
				pl.getGenerator().cancelGeneration();
				return;
			}
			
			if(!pdata.getPlayerBlockLimits(pl).contains(event.getBlockPlaced().getType()) ||
					pl.hasPermission("buildtools.bypassdisabledblocks")){
				Integer[] hl = pdata.getPlayerHightLimits(pl);
				if((event.getBlock().getLocation().getBlockY() <= hl[1] && 
						event.getBlock().getLocation().getBlockY() >= hl[0]) ||
						pl.hasPermission("buildtools.bypassheightlimit")){
					BlockPoint point = new BlockPoint(event.getBlockPlaced().getLocation(), event.getBlockPlaced().getType(), 
							event.getBlockReplacedState(), BuildMode.PLACE);
					pl.addPoint(point);
					if(pl.getPointCount() >= pl.getType().getRequiredPointCount()){
						int volLimit = pdata.getPlayerVolumeLimit(pl);
						int vol = -1;
						if(pl.getPointCount() > 1)
							vol = BTUtils.getVolume(pl.getPoint(0).getPoint(), pl.getPoint(1).getPoint());
						if(vol == -1 || vol <= volLimit || pl.hasPermission("buildtools.bypassvolumelimit")){
							final BTPlayer fpl = pl;
							final List<BlockPoint> fpoints = new ArrayList<BlockPoint>(pl.getPoints());
							final BuildPattern fpattern = pl.getPattern();
							final BuildType ftype = pl.getType();
							Bukkit.getScheduler().scheduleSyncDelayedTask(BTPlugin.plugin, new Runnable() {
								
								@Override
								public void run() {
									BTUtils.generateBlocks(fpl, ftype, fpattern, fpoints, BuildMode.PLACE);
									fpl.clearPoints();
								}
							});
							pl.sendMessage("Generating selected points...", ChatColor.AQUA);
							if(plugin.isDebugging()){
								plugin.getLogger().info("Generating volume of " + vol + " for " + pl.getName());
							}
						}
						else{
							pl.sendMessage("Volume limit exceeded.\n"
									+ "Selected size: " + vol + " blocks.\n"
									+ "Your limit: " + volLimit + " blocks.", ChatColor.RED);
							pl.getPoints().remove(pl.getPointCount() - 1);
							if(plugin.isDebugging()){
								plugin.getLogger().info("Volume exceeded for " + pl.getName() 
										+ ", Volume: " + vol + ", Max Volume: " + volLimit);
							}
							event.setCancelled(true);
						}
					}
					else{
						pl.sendMessage("Added point to selection.", ChatColor.AQUA);
					}
				}
				else{
					pl.sendMessage("Notice: Block not within height limits.\n"
							+ "Limit: Y" + hl[0] + " - Y" + hl[1] + "\n"
							+ "Your position: Y" + event.getBlock().getLocation().getBlockY(), ChatColor.GOLD);
					event.setCancelled(true);
					if(plugin.isDebugging()){
						plugin.getLogger().info("Height limit exceeded for " + pl.getName() + ", players height: " 
								+ event.getBlock().getLocation().getBlockY() + ", Limit: " + hl[0] + "-" + hl[1]);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void breakBlock(BlockBreakEvent event){
		BTPlayer pl;
		try{
			pl = pdata.getBTPlayer(event.getPlayer());
		}
		catch (UnknownBTPlayerException e){
			return;
		}
		
		if(pl.isBuildModeActive() && pl.hasPoint(event.getBlock().getLocation())){
			pl.removePoint(event.getBlock().getLocation());
			pl.sendMessage("Removed point from selection.", ChatColor.RED);
		}
		else if(pl.isBuildModeActive() && pl.canPlacePoint()){
			if(!pl.canBuild() && !pl.hasPoint(event.getBlock().getLocation())){
				pl.sendMessage("Still generating, please wait...", ChatColor.AQUA);
				event.setCancelled(true);
				return;
			}
			else if(!pl.canBuild() && pl.hasPoint(event.getBlock().getLocation())){
				pl.sendMessage("Cancelling block generation...", ChatColor.RED);
				pl.getGenerator().cancelGeneration();
				return;
			}
			
			if(!pdata.getPlayerBlockLimits(pl).contains(Material.AIR) || 
					pl.hasPermission("buildtools.bypassdisabledblocks")){
				Integer[] hl = pdata.getPlayerHightLimits(pl);
				if((event.getBlock().getLocation().getBlockY() <= hl[1] && 
						event.getBlock().getLocation().getBlockY() >= hl[0]) ||
						pl.hasPermission("buildtools.bypassheightlimit")){
					BlockPoint point = new BlockPoint(event.getBlock().getLocation(), Material.AIR, 
							event.getBlock().getState(), BuildMode.BREAK);
					pl.addPoint(point);
					if(pl.getPointCount() >= pl.getType().getRequiredPointCount()){
						int volLimit = pdata.getPlayerVolumeLimit(pl);
						int vol = -1;
						if(pl.getPointCount() > 1)
							vol = BTUtils.getVolume(pl.getPoint(0).getPoint(), pl.getPoint(1).getPoint());
						if(vol == -1 || vol <= volLimit || pl.hasPermission("buildtools.bypassvolumelimit")){
							final BTPlayer fpl = pl;
							final List<BlockPoint> fpoints = new ArrayList<BlockPoint>(pl.getPoints());
							final BuildPattern fpattern = pl.getPattern();
							final BuildType ftype = pl.getType();
							Bukkit.getScheduler().scheduleSyncDelayedTask(BTPlugin.plugin, new Runnable() {
								
								@Override
								public void run() {
									BTUtils.generateBlocks(fpl, ftype, fpattern, fpoints, BuildMode.BREAK);
									fpl.clearPoints();
								}
							});
							pl.sendMessage("Generating selected points...", ChatColor.AQUA);
							if(plugin.isDebugging()){
								plugin.getLogger().info("Generating volume of " + vol + " for " + pl.getName());
							}
						}
						else{
							pl.sendMessage("Volume limit exceeded.\n"
									+ "Selected size: " + vol + " blocks.\n"
									+ "Your limit: " + volLimit + " blocks.", ChatColor.RED);
							pl.getPoints().remove(pl.getPointCount() - 1);
							event.setCancelled(true);
							if(plugin.isDebugging()){
								plugin.getLogger().info("Volume exceeded for " + pl.getName() 
										+ ", Volume: " + vol + ", Max Volume: " + volLimit);
							}
						}
					}
					else{
						pl.sendMessage("Added point to selection.", ChatColor.AQUA);
					}
				}
				else{
					pl.sendMessage("Notice: Block not within height limits.\n"
							+ "Limit: Y" + hl[0] + " - Y" + hl[1] + "\n"
							+ "Your position: Y" + event.getBlock().getLocation().getBlockY(), ChatColor.GOLD);
					pl.clearPoints();
					if(plugin.isDebugging()){
						plugin.getLogger().info("Height limit exceeded for " + pl.getName() + ", players height: " 
								+ event.getBlock().getLocation().getBlockY() + ", Limit: " + hl[0] + "-" + hl[1]);
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void gamemodeChange(PlayerGameModeChangeEvent event){
		BTPlayer ply;
		try{
			ply = pdata.getBTPlayer(event.getPlayer());
		}
		catch (UnknownBTPlayerException e){
			return;
		}
		
		if(event.getNewGameMode() != GameMode.CREATIVE && ply.isBuildModeActive()){
			ply.setBuildModeActive(false);
			ply.sendMessage("Build mode deactivated.", ChatColor.RED);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private void worldChange(PlayerChangedWorldEvent event){
		BTPlayer ply;
		try{
			ply = pdata.getBTPlayer(event.getPlayer());
		}
		catch (UnknownBTPlayerException e){
			return;
		}
		
		if(ply.isBuildModeActive()){
			ply.setBuildModeActive(false);
			ply.sendMessage("Build mode deactivated.", ChatColor.RED);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void itemDrop(ItemSpawnEvent event){
		if(plugin.getGeneratingChunks().hasChunk(event.getLocation().getChunk())){
			event.setCancelled(true);
		}
	}
}
