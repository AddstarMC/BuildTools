package au.com.mineauz.buildtools.exceptions;

public class DuplicatePatternException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DuplicatePatternException(String name){
		super("By the name: " + name);
	}
}
