package au.com.mineauz.buildtools.menu;

public class MenuSession {
	public final Menu current;
	public int page;
	public final MenuSession previous;
	
	public MenuItem[] controlBar;
	
	public MenuSession(Menu current, MenuSession previous) {
		this.current = current;
		this.previous = previous;
		this.page = 0;
	}
}
