package au.com.mineauz.buildtools.exceptions;

public class DuplicateLimitException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DuplicateLimitException(String name){
		super("Duplicate limit: " + name);
	}
}
