package au.com.mineauz.buildtools.exceptions;

public class UnknownTypeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public UnknownTypeException(String name){
		super("By the name: " + name);
	}

}
