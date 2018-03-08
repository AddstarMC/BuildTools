package au.com.mineauz.buildtools.exceptions;

public class UnknownBTPlayerException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnknownBTPlayerException(String name){
		super("Unknown BuildTools Player: " + name);
	}
}
