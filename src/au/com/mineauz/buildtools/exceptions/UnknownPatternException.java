package au.com.mineauz.buildtools.exceptions;

public class UnknownPatternException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnknownPatternException(String name){
		super("By the name: " + name);
	}

}
