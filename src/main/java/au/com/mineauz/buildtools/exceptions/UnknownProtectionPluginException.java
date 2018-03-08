package au.com.mineauz.buildtools.exceptions;

public class UnknownProtectionPluginException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnknownProtectionPluginException(String name){
		super("By the name: " + name);
	}
}
