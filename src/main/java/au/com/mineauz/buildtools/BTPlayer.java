package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import au.com.mineauz.buildtools.menu.Callback;
import au.com.mineauz.buildtools.menu.Menu;
import au.com.mineauz.buildtools.menu.MenuItem;
import au.com.mineauz.buildtools.menu.MenuItemBoolean;
import au.com.mineauz.buildtools.menu.MenuItemBuildType;
import au.com.mineauz.buildtools.menu.MenuItemHelp;
import au.com.mineauz.buildtools.menu.MenuItemPatterns;
import au.com.mineauz.buildtools.menu.MenuItemRotate;
import au.com.mineauz.buildtools.menu.MenuItemSubMenu;
import au.com.mineauz.buildtools.menu.MenuItemSettings;
import au.com.mineauz.buildtools.menu.MenuItemUndo;
import au.com.mineauz.buildtools.menu.MenuSession;
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
	private boolean protectionOverride = false;
	private boolean sneakInverted = false;
	
	private Generator currentGeneration = null;
	
	private MenuSession menu = null;
	private boolean noClose = false;
	private MenuItem manualEntry = null;
	private BukkitTask manualEntryTimer = null;
	
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
		return player.getGameMode() == GameMode.CREATIVE;
	}
	
	public boolean isBuildModeActive(){
		return buildModeActive;
	}
	
	public void setBuildModeActive(boolean active){
		buildModeActive = active;
		clearPoints();
		if(BTPlugin.plugin.isDebugging()){
			if(buildModeActive)
				BTPlugin.plugin.getLogger().info(getName() + " enabled Build Mode.");
			else
				BTPlugin.plugin.getLogger().info(getName() + " disabled Build Mode.");
		}
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
		Iterator<BlockPoint> it = points.iterator();
		while(it.hasNext()){
			BlockPoint point = it.next();
			if(loc.getBlockX() == point.getPoint().getBlockX() &&
					loc.getBlockY() == point.getPoint().getBlockY() &&
					loc.getBlockZ() == point.getPoint().getBlockZ() &&
					loc.getWorld() == point.getPoint().getWorld()){
				it.remove();
			}
		}
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
		return BTPlugin.plugin.getBuildTypes().getType(type);
	}
	
	public void setType(String name){
		type = name;
		pattern = "NONE";
		clearPoints();
		if(BTPlugin.plugin.isDebugging())
			BTPlugin.plugin.getLogger().info(getName() + " changed type: " + type);
	}
	
	public BuildPattern getPattern(){
		return BTPlugin.plugin.getBuildPatterns().getPattern(pattern);
	}
	
	public boolean setPattern(String name){
		if(BTPlugin.plugin.getBuildPatterns().getPattern(name).compatibleSelections() == null ||
				BTPlugin.plugin.getBuildPatterns().getPattern(name).compatibleSelections().contains(type)){
			pattern = name;
			if(BTPlugin.plugin.isDebugging())
				BTPlugin.plugin.getLogger().info(getName() + " changed pattern: " + pattern);
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
			if(BTPlugin.plugin.isDebugging())
				BTPlugin.plugin.getLogger().info(getName() + " made an undo");
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
			if(BTPlugin.plugin.isDebugging())
				BTPlugin.plugin.getLogger().info(getName() + " made an redo");
		}
		else
			sendMessage("Nothing to redo!", ChatColor.RED);
	}
	
	public boolean hasItem(ItemStack item){
		if(item == null) return false;
		for(ItemStack i : player.getInventory().getContents()){
			if(i != null && i.getType() == item.getType()){
				return true;
			}
		}
		return false;
	}
	
	public void removeItem(ItemStack item){
		if(item == null) return;
		ItemStack toClear = null;
		for(ItemStack i : player.getInventory().getContents()){
			if(i != null && i.getType() == item.getType()){
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
	
	public String[] getTSettings(){
		return tSettings;
	}
	
	public Callback<String> getTSettingsCallback(){
		return new Callback<String>() {

			@Override
			public void setValue(String value) {
				if(value != null){
					String[] spl = value.split(" ");
					tSettings = spl;
				}
			}

			@Override
			public String getValue() {
				String set = null;
				if(tSettings.length > 0)
					set = "";
				for(String s : tSettings)
					set += s + " ";
				return set;
			}
		};
	}
	
	public void setTSettings(String[] settings){
		tSettings = settings;
	}
	
	public String[] getPSettings(){
		return pSettings;
	}
	
	public Callback<String> getPSettingsCallback(){
		return new Callback<String>() {

			@Override
			public void setValue(String value) {
				if(value != null){
					String[] spl = value.split(" ");
					pSettings = spl;
				}
			}

			@Override
			public String getValue() {
				String set = null;
				if(pSettings.length > 0)
					set = "";
				for(String s : pSettings)
					set += s + " ";
				return set;
			}
		};
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
	
	public void setGenerator(Generator gen){
		currentGeneration = gen;
	}
	
	public Generator getGenerator(){
		return currentGeneration;
	}
	
	public MenuSession getMenuSession() {
		return menu;
	}
	
	public void setMenuSession(MenuSession menu){
		this.menu = menu;
	}
	
	public boolean isInMenu(){
		return menu != null;
	}
	
	public void showPreviousMenu() {
		if (menu != null) {
			MenuSession session = menu.previous;
			if (session != null) {
				session.current.displaySession(this, session);
			}
		}
	}
	
	public void showPreviousMenu(int backCount) {
		if (menu != null) {
			MenuSession session = menu;
			while(session != null && backCount > 0) {
				session = session.previous;
				--backCount;
			}
			
			if (session != null) {
				session.current.displaySession(this, session);
			}
		}
	}
	
	public void setNoClose(boolean value){
		noClose = value;
	}
	
	public boolean getNoClose(){
		return noClose;
	}
	
	public void startManualEntry(MenuItem item, int time) {
		manualEntry = item;
		manualEntryTimer = Bukkit.getScheduler().runTaskLater(BTPlugin.plugin, () -> {
            noClose = false;
            manualEntry = null;
            manualEntryTimer = null;
            if (menu != null) {
                menu.current.displaySession(BTPlayer.this, menu);
            }
        }, (long)(time * 20));
	}
	
	public void cancelMenuReopen() {
		if (manualEntryTimer != null) {
			manualEntryTimer.cancel();
		}
		manualEntry = null;
	}
	
	public MenuItem getManualEntry(){
		return manualEntry;
	}
	
	@SuppressWarnings("deprecation")
	public void updateInventory(){
		getPlayer().updateInventory();
	}
	
	public void openMenu(){
		Menu m = new Menu(6, "BuildTools Menu");
		Menu types = new Menu(6, "Build Types");
		BTPlugin plugin = BTPlugin.plugin;
		
		MenuItemSubMenu typeSub = new MenuItemSubMenu("Build Types", Material.CHEST, types);
		MenuItemPatterns patSub = new MenuItemPatterns(this);
		typeSub.setDescription("Current Type: " + BTUtils.capitalize(getType().getName()));
		
		List<String> typeList = plugin.getBuildTypes().getAllTypes();
		Collections.sort(typeList);
		List<String> patternList = plugin.getBuildPatterns().getAllPatterns();
		Collections.sort(patternList);
		
		MenuItemBuildType mt;
		for(String t : typeList){
			if(hasPermission("buildtools.type." + t.toLowerCase())){
				mt = new MenuItemBuildType(Material.ENDER_PEARL, t, typeSub, patSub);
				if(plugin.getBuildTypes().getType(t).getHelpInfo() != null)
					mt.setDescription(BTUtils.wordWrap(plugin.getBuildTypes().getType(t).getHelpInfo()));
				types.addItem(mt);
			}
		}
		
		m.insertItem(new MenuItemBoolean("Enable BuildTools", Material.DIAMOND_PICKAXE, new Callback<Boolean>() {
			
			@Override
			public void setValue(Boolean value) {
				setBuildModeActive(value);
			}
			
			@Override
			public Boolean getValue() {
				return isBuildModeActive();
			}
		}), 4, 0);
		
		m.insertItem(typeSub, 9+3, 0);
		m.insertItem(patSub, 9+5, 0);
		m.insertItem(new MenuItemSettings(getTSettingsCallback(), MenuItemSettings.SettingType.TYPE), 9*2+3, 0);
		m.insertItem(new MenuItemSettings(getPSettingsCallback(), MenuItemSettings.SettingType.PATTERN), 9*2+5, 0);

		m.insertItem(new MenuItemUndo(MenuItemUndo.Type.REDO), 9*4-4, 0);
		m.insertItem(new MenuItemUndo(MenuItemUndo.Type.UNDO), 9*4-6, 0);
		m.insertItem(new MenuItemRotate(180), 9*5-3, 0);
		m.insertItem(new MenuItemRotate(90), 9*5-4, 0);
		m.insertItem(new MenuItemRotate(-90), 9*5-6, 0);
		m.insertItem(new MenuItemRotate(-180), 9*5-7, 0);
		m.setControlItem(new MenuItemHelp(), 4);
		MenuItemBoolean snki = new MenuItemBoolean("Sneak Inversion", Material.ENDER_EYE, getSneakInversionCallback());
		snki.setDescription(BTUtils.wordWrap("Inverts sneaking mode on BuildTools. By default, "
				+ "sneaking will stop BuildTools from working, if this "
				+ "is toggled, it will only work while sneaking."));
		m.setControlItem(snki, 3);
		
		
		m.displayMenu(this);
	}
	
	public boolean hasProtectionOverride(){
		return protectionOverride;
	}
	
	public boolean toggleProtectionOverride(){
		protectionOverride = !protectionOverride;
		return protectionOverride;
	}
	
	public boolean hasSneakInverted(){
		return sneakInverted;
	}
	
	public boolean toggleSneakInversion(){
		sneakInverted = !sneakInverted;
		return sneakInverted;
	}
	
	public Callback<Boolean> getSneakInversionCallback(){
		return new Callback<Boolean>() {

			@Override
			public void setValue(Boolean value) {
				sneakInverted = value;
			}

			@Override
			public Boolean getValue() {
				return sneakInverted;
			}
		};
	}
	
	public boolean canPlacePoint(){
		return (hasSneakInverted() || !player.isSneaking()) &&
				(!hasSneakInverted() || player.isSneaking());
	}
}
