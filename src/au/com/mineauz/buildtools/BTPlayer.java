package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.buildtools.patterns.BuildPattern;
import au.com.mineauz.buildtools.patterns.BuildPatterns;
import au.com.mineauz.buildtools.types.BuildTypes;
import au.com.mineauz.buildtools.types.BuildType;

public class BTPlayer {
	private Player player;
	private boolean buildModeActive = false;
	private List<Location> points = new ArrayList<Location>();
	private String selection = "CUBOID";
	private String pattern = "NONE";
	private List<BTUndo> undos = new ArrayList<BTUndo>();
	private List<BTUndo> redos = new ArrayList<BTUndo>();
	private String[] tSettings = new String[0];
	private String[] pSettings = new String[0];
	private boolean canBuild = true;
	
	public BTPlayer(Player player){
		this.player = player;
	}
	
	public Player getPlayer(){
		return player;
	}
	
	public String getName(){
		return player.getName();
	}
	
	public boolean hasPermission(String perm){
		return player.hasPermission(perm);
	}
	
	public UUID getUniqueId(){
		return player.getUniqueId();
	}
	
	public Location getLocation(){
		return player.getLocation();
	}
	
	public boolean isInCreative(){
		if(player.getGameMode() == GameMode.CREATIVE)
			return true;
		return false;
	}
	
	public boolean isBuildModeActive(){
		return buildModeActive;
	}
	
	public void setBuildModeActive(boolean active){
		buildModeActive = active;
		clearPoints();
	}
	
	public void addPoint(Location loc){
		points.add(loc);
	}
	
	public boolean hasPoint(Location loc){
		for(Location point : points){
			if(loc.getBlockX() == point.getBlockX() &&
					loc.getBlockY() == point.getBlockY() &&
					loc.getBlockZ() == point.getBlockZ() &&
					loc.getWorld() == point.getWorld()){
				return true;
			}
		}
		return false;
	}
	
	public void removePoint(Location loc){
		for(Location point : new ArrayList<Location>(points)){
			if(loc.getBlockX() == point.getBlockX() &&
					loc.getBlockY() == point.getBlockY() &&
					loc.getBlockZ() == point.getBlockZ() &&
					loc.getWorld() == point.getWorld()){
				points.remove(point);
			}
		}
	}
	
	public int getPointCount(){
		return points.size();
	}
	
	public void clearPoints(){
		points.clear();
	}
	
	public boolean pointMaterialsMatch(){
		Material mat = null;
		for(Location loc : points){
			if(mat == null)
				mat = loc.getBlock().getType();
			else
				if(mat != loc.getBlock().getType())
					return false;
		}
		return true;
	}
	
	public Location getPoint(int number){
		if(number < points.size())
			return points.get(number);
		return null;
	}
	
	public List<Location> getPoints(){
		return points;
	}
	
	public BuildType getSelection(){
		return BuildTypes.getType(selection);
	}
	
	public void setSelection(String name){
		selection = name;
		pattern = "NONE";
		clearPoints();
	}
	
	public BuildPattern getPattern(){
		return BuildPatterns.getPattern(pattern);
	}
	
	public boolean setPattern(String name){
		if(BuildPatterns.getPattern(name).compatibleSelections() == null ||
				BuildPatterns.getPattern(name).compatibleSelections().contains(selection)){
			pattern = name;
			return true;
		}
		return false;
	}
	
	public void sendMessage(String message, ChatColor prefixColor){
		String prefix = prefixColor + "[BuildTools] " + ChatColor.WHITE;
		player.sendMessage(prefix + message);
	}
	
	public void sendMessage(String message){
		player.sendMessage(message);
	}
	
	public void addUndo(BTUndo undo){
		undos.add(undo);
		if(undos.size() > 10) //TODO: Variable amount of undos (config)
			undos.remove(0);
	}
	
	public void addRedo(BTUndo redo){
		redos.add(redo);
		if(redos.size() > 10) //TODO: Variable amount of redos (config)
			redos.remove(0);
	}
	
	public void undo(){
		if(!undos.isEmpty()){
			BTUndo redo = undos.get(undos.size() - 1).restoreBlocks();
			undos.remove(undos.size() - 1);
			redos.add(redo);
			sendMessage("Last change undone", ChatColor.AQUA);
		}
		else
			sendMessage("Nothing to undo!", ChatColor.RED);
	}
	
	public void redo(){
		if(!redos.isEmpty()){
			BTUndo undo = redos.get(redos.size() - 1).restoreBlocks();
			redos.remove(redos.size() - 1);
			undos.add(undo);
			sendMessage("Last change redone", ChatColor.AQUA);
		}
		else
			sendMessage("Nothing to redo!", ChatColor.RED);
	}
	
	public boolean hasItem(ItemStack item){
		if(item == null) return false;
		for(ItemStack i : player.getInventory().getContents()){
			if(i != null && i.getType() == item.getType() &&
					i.getDurability() == item.getDurability()){
				return true;
			}
		}
		return false;
	}
	
	public void removeItem(ItemStack item){
		if(item == null) return;
		ItemStack toClear = null;
		for(ItemStack i : player.getInventory().getContents()){
			if(i != null && i.getType() == item.getType() &&
					i.getDurability() == item.getDurability()){
				if(i.getAmount() > 1)
					i.setAmount(i.getAmount() - 1);
				else
					toClear = i;
				break;
			}
		}
		if(toClear != null)
			player.getInventory().remove(toClear);
	}
	
	public void damageItem(ItemStack item){
		if(item == null) return;
		if(Main.plugin.getPlayerData().hasTool(item.getType())){
			item.setDurability((short) (item.getDurability() + 1));
			if(item.getDurability() >= item.getType().getMaxDurability()){
				player.playSound(getLocation(), Sound.ITEM_BREAK, 1, 1);
				removeItem(item);
			}
		}
	}
	
	public String[] getTSettings(){
		return tSettings;
	}
	
	public void setTSettings(String[] settings){
		tSettings = settings;
	}
	
	public String[] getPSettings(){
		return pSettings;
	}
	
	public void setPSettings(String[] settings){
		pSettings = settings;
	}
	
	public boolean canBuild(){
		return canBuild;
	}
	
	public void setCanBuild(boolean canBuild){
		this.canBuild = canBuild;
	}
}
