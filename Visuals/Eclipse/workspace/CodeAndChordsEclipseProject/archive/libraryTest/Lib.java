package libraryTest;

import processing.core.PApplet;

public class Lib 
{
	private PApplet parent;
	private String	message;
	
	public Lib(PApplet parent, String message)
	{
		this.parent = parent;
		this.message = message;
		
		this.parent.registerMethod("pre", this);
	}
	
	public void pre()
	{
		System.out.println(this.message);
	}
	
	
}
