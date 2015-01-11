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
import au.com.mineauz.buildtools.types.BuildType;

public class BTPlayer {
	private Player player;
	private boolean buildModeActive = false;
	private List<BlockPoint> points = new ArrayList<>();
	private String type = "CUBOID";
	private String pattern = "NONE";
	private List<BTUndo> undos = new ArrayList<>();
	private List<BTUndo> redos = new ArrayList<>();
	private String[] tSettings = new String[0];
	private String[] pSettings = new String[0];
	private boolean canBuild = true;
	private BTCopy copy = null;
	
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
	
	public void addPoint(BlockPoint point){
		points.add(point);
	}
	
	public boolean hasPoint(Location loc){
		for(BlockPoint point : points){
			if(loc.getBlockX() == point.getPoint().getBlockX() &&
					loc.getBlockY() == point.getPoint().getBlockY() &&
					loc.getBlockZ() == point.getPoint().getBlockZ() &&
					loc.getWorld() == point.getPoint().getWorld()){
				return true;
			}
		}
		return false;
	}
	
	public void removePoint(Location loc){
		for(BlockPoint point : new ArrayList<BlockPoint>(points)){
			if(loc.getBlockX() == point.getPoint().getBlockX() &&
					loc.getBlockY() == point.getPoint().getBlockY() &&
					loc.getBlockZ() == point.getPoint().getBlockZ() &&
					loc.getWorld() == point.getPoint().getWorld()){
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
		for(BlockPoint point : points){
			if(mat == null)
				mat = point.getType();
			else
				if(mat != point.getType())
					return false;
		}
		return true;
	}
	
	public BlockPoint getPoint(int number){
		if(number < points.size())
			return points.get(number);
		return null;
	}
	
	public List<BlockPoint> getPoints(){
		return points;
	}
	
	public BuildType getType(){
		return Main.plugin.getBuildTypes().getType(type);
	}
	
	public void setType(String name){
		type = name;
		pattern = "NONE";
		clearPoints();
		if(Main.plugin.isDebugging())
			Main.plugin.getLogger().info(getName() + " changed type: " + type);
	}
	
	public BuildPattern getPattern(){
		return Main.plugin.getBuildPatterns().getPattern(pattern);
	}
	
	public boolean setPattern(String name){
		if(Main.plugin.getBuildPatterns().getPattern(name).compatibleSelections() == null ||
				Main.plugin.getBuildPatterns().getPattern(name).compatibleSelections().contains(type)){
			pattern = name;
			if(Main.plugin.isDebugging())
				Main.plugin.getLogger().info(getName() + " changed pattern: " + pattern);
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
			if(Main.plugin.isDebugging())
				Main.plugin.getLogger().info(getName() + " made an undo");
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
			if(Main.plugin.isDebugging())
				Main.plugin.getLogger().info(getName() + " made an redo");
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
	
	public void setCopy(BTCopy copy){
		this.copy = copy;
	}
	
	public boolean hasCopy(){
		return copy != null;
	}
	
	public BTCopy getCopy(){
		return copy;
	}
}
