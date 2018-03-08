package au.com.mineauz.buildtools.exceptions;

public class DuplicateProtectionPluginException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public DuplicateProtectionPluginException(String name){
		super("By the name: " + name);
	}

}
