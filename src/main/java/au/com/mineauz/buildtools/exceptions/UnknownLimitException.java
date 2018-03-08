package au.com.mineauz.buildtools.exceptions;

public class UnknownLimitException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnknownLimitException(String name){
		super("Unknown Limit: " + name);
	}
}
