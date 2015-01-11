package au.com.mineauz.buildtools.exceptions;

public class DuplicateTypeException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	
	public DuplicateTypeException(String name){
		super("By the name: " + name);
	}

}
