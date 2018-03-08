package au.com.mineauz.buildtools;

public class IVector {
	private int x;
	private int y;
	private int z;
	
	public IVector(int x, int y, int z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX(){
		return x;
	}
	
	public int getY(){
		return y;
	}
	
	public int getZ(){
		return z;
	}
	
	public static IVector fromString(String vector){
		String[] points = vector.split(";");
		int x;
		int y;
		int z;
		if(points[0].matches("-?[0-9]+")){
			x = Integer.valueOf(points[0]);
		}
		else
			throw new NumberFormatException();
		if(points[1].matches("-?[0-9]+")){
			y = Integer.valueOf(points[1]);
		}
		else
			throw new NumberFormatException();
		if(points[2].matches("-?[0-9]+")){
			z = Integer.valueOf(points[2]);
		}
		else
			throw new NumberFormatException();
		return new IVector(x, y, z);
	}
	
	@Override
	public String toString(){
		return x + ";" + y + ";" + z;
	}
}
