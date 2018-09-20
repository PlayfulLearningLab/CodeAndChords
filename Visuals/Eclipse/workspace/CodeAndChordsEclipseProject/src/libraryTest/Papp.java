package libraryTest;

import processing.core.PApplet;

public class Papp extends PApplet 
{

	public static void main(String[] args)
	{
		PApplet.main("libraryTest.Papp");
	}
	
	public void settings()
	{
		System.out.println("Starting Lib");
		new Lib(this, "Lib 1");
		new Lib(this, "Lib 2");
	}
	
	public void settup()
	{
		
	}
	
	public void draw()
	{
		System.out.println("draw");
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
