package au.com.mineauz.buildtools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class PlayerData {
	private static Map<UUID, BTPlayer> players = new HashMap<UUID, BTPlayer>();
	private static List<Material> tools = new ArrayList<Material>();
	
	static{
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
	}
	
	public static void addBTPlayer(Player player){
		players.put(player.getUniqueId(), new BTPlayer(player));
	}
	
	public static void removeBTPlayer(Player player){
		players.remove(player.getUniqueId());
	}
	
	public static BTPlayer getBTPlayer(UUID uuid){
		return players.get(uuid);
	}
	
	public static BTPlayer getBTPlayer(Player player){
		return players.get(player.getUniqueId());
	}
	
	public static void clearAllBTPlayers(){
		players.clear();
	}
	
	public static boolean hasTool(Material tool){
		return tools.contains(tool);
	}
}
