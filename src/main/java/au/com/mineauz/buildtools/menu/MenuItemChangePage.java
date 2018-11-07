package au.com.mineauz.buildtools.menu;

import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;

public class MenuItemChangePage extends MenuItem {
	private MenuSession session;
	private int page;
	
	public MenuItemChangePage(String name, MenuSession session, int page) {
		super(name, Material.REDSTONE_TORCH);
		this.session = session;
		this.page = page;
	}
	
	@Override
	public void onClick(BTPlayer player) {
		session.page = page;
		session.current.displaySession(player, session);
	}

}
