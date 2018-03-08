package inputClassEclipse_Renamed;
import processing.core.PApplet;

/*
 * Driver for InputClassEclipse_Renamed
 */

public class Driver extends PApplet {
	Input	input;
	
	public static void main(String[] args)
	{
		PApplet.main("inputClassEclipse_Renamed.Driver");
	} // main
	
	public void settings()
	{
		size(400,600);
	} // settings
	
	public void setup()
	{
		input	= new Input();
//		input	= new Input(new String[] {"Horse and Rider 1.wav" }); //, "Horse and Rider 2.wav", "Horse and Rider 3.wav"});
	} // setup
	
	public void draw()
	{
		background(204, 150, 204);
		println("input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz());
		
		ellipse(width / 2, height - input.getAdjustedFund(), 50, 50);
	} // draw
}
