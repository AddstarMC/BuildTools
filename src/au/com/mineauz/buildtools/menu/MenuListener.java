package au.com.mineauz.buildtools.menu;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import au.com.mineauz.buildtools.BTPlayer;
import au.com.mineauz.buildtools.BTPlugin;

public class MenuListener implements Listener{

	private BTPlugin plugin = BTPlugin.plugin;
	
	@EventHandler(ignoreCancelled = true)
	private void clickMenu(InventoryClickEvent event){
		BTPlayer ply = plugin.getPlayerData().getBTPlayer((Player)event.getWhoClicked());
		MenuSession session = ply.getMenuSession();
		if (session == null) {
			return;
		}
		
		Menu menu = session.current;
		
		MenuPage page = menu.getPage(session.page);
		if(event.getRawSlot() >= 0 && event.getRawSlot() < menu.getSize() + 9) {
			MenuItem clickedItem = menu.getClickItem(session, event.getRawSlot());
			if (clickedItem != null) {
				event.setCancelled(true);
				ItemStack display = null;
				switch(event.getClick()) {
				case LEFT:
					if(event.getCursor().getType() != Material.AIR)
						display = clickedItem.handleClickWithItem(ply, event.getCursor());
					else
						display = clickedItem.handleClick(ply);
					break;
				case RIGHT:
					display = clickedItem.handleRightClick(ply);
					break;
				case SHIFT_LEFT:
					display = clickedItem.handleShiftClick(ply);
					break;
				case SHIFT_RIGHT:
					display = clickedItem.handleShiftRightClick(ply);
					break;
				case DOUBLE_CLICK:
					display = clickedItem.handleDoubleClick(ply);
					break;
				default:
					break;
				}
				if (display != null) {
					event.setCurrentItem(display);
				}
			} else if (page instanceof MenuPageInventory) {
				if (!menu.getAllowModify()) {
					event.setCancelled(true);
				} else {
					final Inventory inv = event.getView().getTopInventory();
					final MenuPageInventory invPage = (MenuPageInventory)page;
					Bukkit.getScheduler().runTask(BTPlugin.plugin, new Runnable() {
						@Override
						public void run() {
							invPage.updateFrom(inv);
						}
					});
				}
			} else {
				event.setCancelled(true);
			}
		} else if (event.getClick() == ClickType.SHIFT_LEFT || event.getClick() == ClickType.SHIFT_RIGHT) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void dragMenu(InventoryDragEvent event){
		BTPlayer ply = plugin.getPlayerData().getBTPlayer((Player)event.getWhoClicked());
		
		MenuSession session = ply.getMenuSession();
		if (session == null) {
			return;
		}
		
		Menu menu = session.current;
		
		MenuPage page = menu.getPage(session.page);
		if (page instanceof MenuPageInventory) {
			Iterator<Integer> it = event.getRawSlots().iterator();
			while(it.hasNext()) {
				int slot = it.next();
				if (slot < menu.getSize() + 9) {
					if (menu.getAllowModify()) {
						event.setCancelled(true);
						break;
					}
					
					MenuItem item = menu.getClickItem(session, slot);
					if (item != null) {
						it.remove();
						break;
					}
				}
			} 
		} else {
			for (int slot : event.getRawSlots()) {
				if (slot < menu.getSize() + 9) {
					event.setCancelled(true);
					break;
				}
			}
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	private void closeMenu(InventoryCloseEvent event) {
		if (!(event.getPlayer() instanceof Player))
			return;
		
		BTPlayer ply = plugin.getPlayerData().getBTPlayer((Player)event.getPlayer());
		if(ply == null) return;
		
		MenuSession session = ply.getMenuSession();
		if (session == null) {
			return;
		}
		
		session.current.onCloseMenu(ply);
		
		if (!ply.getNoClose()) {
			ply.setMenuSession(null);
		}
	}
	
	@EventHandler(priority = EventPriority.LOW)
	private void onDisconnect(PlayerQuitEvent event) {
		BTPlayer ply = plugin.getPlayerData().getBTPlayer((Player)event.getPlayer());
		
		MenuSession session = ply.getMenuSession();
		if (session == null) {
			return;
		}
		
		session.current.onCloseMenu(ply);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	private void onManualItemEntry(AsyncPlayerChatEvent event){
		BTPlayer ply = plugin.getPlayerData().getBTPlayer(event.getPlayer());
		
		MenuSession session = ply.getMenuSession();
		MenuItem manualEntry = ply.getManualEntry();
		if (session != null && manualEntry != null) {
			event.setCancelled(true);
			manualEntry.completeManualEntry(ply, event.getMessage());
		}
	}
}
