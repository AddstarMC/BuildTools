package au.com.mineauz.buildtools.menu;

import org.bukkit.Material;

import au.com.mineauz.buildtools.BTPlayer;

public class MenuItemUndo extends MenuItem{
	
	private Type type;

	public MenuItemUndo(Type t) {
		super("Undo", Material.EYE_OF_ENDER);
		type = t;
		if(t == Type.REDO)
			setName("Redo");
	}
	
	@Override
	public void onClick(BTPlayer player){
		if(type == Type.UNDO){
			player.undo();
		}
		else{
			player.redo();
		}
	}
	
	public enum Type {
		UNDO,
		REDO
    }
}
