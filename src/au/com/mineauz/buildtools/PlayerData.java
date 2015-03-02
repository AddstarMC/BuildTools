package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import au.com.mineauz.buildtools.exceptions.DuplicateLimitException;
import au.com.mineauz.buildtools.exceptions.UnknownBTPlayerException;
import au.com.mineauz.buildtools.exceptions.UnknownLimitException;

public class PlayerData {
	private Map<UUID, BTPlayer> players = new HashMap<UUID, BTPlayer>();
	private List<Material> tools = new ArrayList<Material>();
	private Map<String, Integer[]> heightLimits = new HashMap<String, Integer[]>();
	private Map<String, Integer> volumeLimits = new HashMap<String, Integer>();
	private Map<String, List<Material>> blockLimits = new HashMap<String, List<Material>>();
	
	public PlayerData(){
		tools.add(Material.WOOD_AXE);
		tools.add(Material.WOOD_SPADE);
		tools.add(Material.WOOD_PICKAXE);
		tools.add(Material.STONE_AXE);
		tools.add(Material.STONE_SPADE);
		tools.add(Material.STONE_PICKAXE);
		tools.add(Material.IRON_AXE);
		tools.add(Material.IRON_SPADE);
		tools.add(Material.IRON_PICKAXE);
		tools.add(Material.GOLD_AXE);
		tools.add(Material.GOLD_SPADE);
		tools.add(Material.GOLD_PICKAXE);
		tools.add(Material.DIAMOND_AXE);
		tools.add(Material.DIAMOND_SPADE);
		tools.add(Material.DIAMOND_PICKAXE);
		
		FileConfiguration conf = BTPlugin.plugin.getConfig();
		Set<String> vols = conf.getConfigurationSection("maxVolume").getKeys(false);;
		Set<String> heis = conf.getConfigurationSection("heightLimits").getKeys(false);;
		Set<String> blks = conf.getConfigurationSection("disabledBlocks").getKeys(false);
		for(String vol : vols){
			addVolumeLimit(vol, conf.getInt("maxVolume." + vol));
		}
		for(String h : heis){
			List<Integer> ints = conf.getIntegerList("heightLimits." + h);
			int min = 0;
			int max = 255;
			if(ints.size() >= 2){
				min = ints.get(0);
				max = ints.get(1);
			}
			addHeightLimits(h, min, max);
		}
		for(String b : blks){
			List<String> l = conf.getStringList("disabledBlocks." + b);
			List<Material> mats = new ArrayList<Material>();
			for(String m : l){
				if(Material.matchMaterial(m) != null)
					mats.add(Material.matchMaterial(m));
			}
			addBlockLimits(b, mats);
		}
		if(BTPlugin.plugin.isDebugging()){
			Logger log = BTPlugin.plugin.getLogger();
			log.info("Loaded " + heightLimits.size() + " height limit settings.");
			log.info("Loaded " + volumeLimits.size() + " volume limit settings.");
			log.info("Loaded " + blockLimits.size() + " disabled blocks settings.");
		}
	}
	
	public void addBTPlayer(Player player){
		players.put(player.getUniqueId(), new BTPlayer(player));
	}
	
	public void removeBTPlayer(Player player){
		players.remove(player.getUniqueId());
	}
	
	public BTPlayer getBTPlayer(UUID uuid) throws UnknownBTPlayerException{
		if(players.containsKey(uuid))
			return players.get(uuid);
		throw new UnknownBTPlayerException(uuid.toString());
	}
	
	public BTPlayer getBTPlayer(Player player) throws UnknownBTPlayerException{
		if(players.containsKey(player.getUniqueId()))
			return players.get(player.getUniqueId());
		throw new UnknownBTPlayerException(player.getName() + ", " + player.getUniqueId().toString());
	}
	
	public void clearAllBTPlayers(){
		players.clear();
	}
	
	public boolean hasTool(Material tool){
		return tools.contains(tool);
	}
	
	public void addHeightLimits(String name, int min, int max) throws DuplicateLimitException{
		Integer[] lim = new Integer[]{min, max};
		name = name.toLowerCase();
		if(!heightLimits.containsKey(name))
			heightLimits.put(name, lim);
		else
			throw new DuplicateLimitException(name);
	}
	
	public void modifyHeightLimits(String name, int min, int max) throws UnknownLimitException{
		Integer[] lim = new Integer[]{min, max};
		name = name.toLowerCase();
		if(heightLimits.containsKey(name))
			heightLimits.put(name, lim);
		else
			throw new UnknownLimitException(name);
	}
	
	public Integer[] getHeightLimits(String name) throws UnknownLimitException{
		name = name.toLowerCase();
		if(heightLimits.containsKey(name))
			return heightLimits.get(name);
		else if(heightLimits.containsKey("default"))
			return heightLimits.get("default");
		throw new UnknownLimitException(name);
	}
	
	public List<String> getAllHeightLimits(){
		return new ArrayList<String>(heightLimits.keySet());
	}
	
	public boolean hasHeightLimits(String name){
		name = name.toLowerCase();
		return heightLimits.containsKey(name);
	}
	
	public void removeHeightLimits(String name) throws UnknownLimitException{
		name = name.toLowerCase();
		if(heightLimits.containsKey(name))
			heightLimits.remove(name);
		else
			throw new UnknownLimitException(name);
	}
	
	public void saveHeightLimits(){
		BTPlugin plugin = BTPlugin.plugin;
		plugin.getConfig().set("heightLimits", null);
		for(String hln : heightLimits.keySet()){
			List<String> lm = new ArrayList<String>(2);
			for(int i = 0; i < 2; i++)
				lm.add(heightLimits.get(hln)[i].toString());
			plugin.getConfig().set("heightLimits." + hln, lm);
		}
		plugin.saveConfig();
	}
	
	public Integer[] getPlayerHightLimits(BTPlayer player){
		for(String l : heightLimits.keySet()){
			if(player.hasPermission("buildtools.heightlimit." + l)){
				return heightLimits.get(l);
			}
		}
		return getHeightLimits("default");
	}
	
	public void addVolumeLimit(String name, int limit) throws DuplicateLimitException{
		name = name.toLowerCase();
		if(!volumeLimits.containsKey(name))
			volumeLimits.put(name, limit);
		else
			throw new DuplicateLimitException(name);
	}
	
	public void modifyVolumeLimit(String name, int limit) throws UnknownLimitException{
		name = name.toLowerCase();
		if(volumeLimits.containsKey(name))
			volumeLimits.put(name, limit);
		else
			throw new UnknownLimitException(name);
	}
	
	public int getVolumeLimit(String name) throws UnknownLimitException{
		name = name.toLowerCase();
		if(volumeLimits.containsKey(name))
			return volumeLimits.get(name);
		else if(volumeLimits.containsKey("default"))
			return volumeLimits.get("default");
		throw new UnknownLimitException(name);
	}
	
	public List<String> getVolumeLimits(){
		return new ArrayList<String>(volumeLimits.keySet());
	}
	
	public boolean hasVolumeLimit(String name){
		name = name.toLowerCase();
		return volumeLimits.containsKey(name);
	}
	
	public void removeVolumeLimit(String name) throws UnknownLimitException{
		name = name.toLowerCase();
		if(volumeLimits.containsKey(name))
			volumeLimits.remove(name);
		else
			throw new UnknownLimitException(name);
	}
	
	public void saveVolumeLimits(){
		BTPlugin plugin = BTPlugin.plugin;
		plugin.getConfig().set("maxVolume", null);
		for(String vln : volumeLimits.keySet()){
			plugin.getConfig().set("maxVolume." + vln, volumeLimits.get(vln));
		}
		plugin.saveConfig();
	}
	
	public int getPlayerVolumeLimit(BTPlayer player){
		for(String l : volumeLimits.keySet()){
			if(player.hasPermission("buildtools.volumelimit." + l))
				return volumeLimits.get(l);
		}
		return getVolumeLimit("default");
	}
	
	public void addBlockLimits(String name, List<Material> blocks) throws DuplicateLimitException{
		name = name.toLowerCase();
		if(!blockLimits.containsKey(name))
			blockLimits.put(name, blocks);
		else
			throw new DuplicateLimitException(name);
	}
	
	public List<Material> getBlockLimits(String name) throws UnknownLimitException{
		name = name.toLowerCase();
		if(blockLimits.containsKey(name))
			return blockLimits.get(name);
		else if(blockLimits.containsKey("default"))
			return blockLimits.get("default");
		throw new UnknownLimitException(name);
	}
	
	public boolean hasBlockLimits(String name){
		name = name.toLowerCase();
		return blockLimits.containsKey(name);
	}
	
	public void saveBlockLimits(){
		BTPlugin plugin = BTPlugin.plugin;
		plugin.getConfig().set("disabledBlocks", null);
		for(String bln : blockLimits.keySet()){
			List<String> mts = new ArrayList<String>();
			for(Material mat : blockLimits.get(bln)){
				mts.add(mat.toString());
			}
			plugin.getConfig().set("disabledBlocks." + bln, mts);
		}
		plugin.saveConfig();
	}
	
	public void removeBlockLimits(String name) throws UnknownLimitException{
		name = name.toLowerCase();
		if(blockLimits.containsKey(name))
			blockLimits.remove(name);
		else
			throw new UnknownLimitException(name);
	}
	
	public List<String> getAllBlockLimits(){
		return new ArrayList<String>(blockLimits.keySet());
	}
	
	public List<Material> getPlayerBlockLimits(BTPlayer player){
		for(String l : blockLimits.keySet()){
			if(player.hasPermission("buildtools.disabledblocks." + l))
				return blockLimits.get(l);
		}
		return getBlockLimits("default");
	}
	
}
