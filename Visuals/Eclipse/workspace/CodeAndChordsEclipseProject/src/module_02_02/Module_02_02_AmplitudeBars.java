package module_02_02;
/*
 * Rachel Farah
 * Sin Wave Controlled by Amplitude
 * */

//import core.FullScreenDisplay;
//import core.Input;
import core.Module;
import core.ModuleMenu;
import processing.core.PApplet;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

/**
 * Sine wave based on amplitude;
 * heavily based upon a Processing sine example.
 * 
 * @author Rachel Farah
 *
 */
public class Module_02_02_AmplitudeBars extends Module {

	int xspacing;   // How far apart should each horizontal location be spaced
	int w;              // Width of entire wave

	double theta = 0.0;  // Start angle at 0
	double amplitude = 75.0;  // Height of wave
	double period = 500.0;  // How many pixels before the wave repeats
	double dx;  // Value for incrementing X, a function of period and xspacing
	float[] yvalues;  // Using an array to store height values for the wave

	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

	public static void main(String[] args) 
	{
		PApplet.main("module_02_02.Module_02_02_AmplitudeBars");
	} // main


	public void settings()
	{
		fullScreen();
		//size(925, 520);
	} // settings

	public void setup()
	{
		// Not specifying an AudioContext will use the PortAudioAudioIO:
		//		this.input	= new Input(this);

		System.out.println("Width:" + width);
		w = width;
		xspacing = width/50;
		dx = (TWO_PI / period) * xspacing;
		yvalues = new float[w/xspacing];
		System.out.println(yvalues.length);

		this.input    = new RealTimeInput(1, new AudioContext(), this);

		this.menu	= new ModuleMenu(this, this, this.input, 6);

		this.yVals		= new int[18];
		// Seemed like a good starting position, related to the text - but pretty arbitrary:
		this.yVals[0]	= 50;
		int	distance	= (this.height - this.yVals[0]) / this.yVals.length;
		for(int i = 1; i < this.yVals.length; i++)
		{
			this.yVals[i]	= this.yVals[i - 1] + distance;
		} // fill yVals

		// Have to addColorSelect() first so that everything else can access the colors:
		String[]	buttonLabels	= new String[] {
				"Canvas", "1", "2", "3", "4", "5", "6"
		};

		this.menu.addColorMenu(buttonLabels, 1, true, null, false, false, "Dynamic\nSegments", 6, 6, "color");
		this.menu.addSensitivityMenu(false);

		this.menu.addShapeSizeSlider(0, this.yVals[15]);

		// Have to do this last so that the manually added Slider (above) doesn't get an id already used by the ShapeEditor:
		//				this.menu.addShapeMenu(1);

		this.menu.getInstrument().setADSR(1000, 500, 0, 0);
		this.menu.setBPM(30);

		//				this.textSize(32);

	} // setup

	public void draw()
	{
		// The following line is necessary so that key press shows the menu button
		System.out.print(keyPressed);

		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		}

		this.menu.runMenu();

		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();

		//rect(0,height/2,1000,2);
		calcWave();
		renderWave();

	} // draw

	void calcWave() {
		// Increment theta (try different values for 'angular velocity' here
		theta += 0.02;

		// For every x value, calculate a y value with sine function
		double x = theta;
		for (int i = 0; i < yvalues.length; i++)
		{
			yvalues[i] = (float) (sin((float) x)*(amplitude*(this.input.getAmplitude()/900)));
			x+=dx;
		}
	}

	void renderWave() {
		noStroke();
		fill(255);
		// A simple way to draw the wave with an ellipse at each location
		for (int x = 0; x < yvalues.length; x++) 
		{
			System.out.println(x*xspacing);
			System.out.println("xspacing equals: "+xspacing);
			//fill(color);
			ellipse(x*xspacing, height/2+yvalues[x], 16, 16);
			//ellipse(x*xspacing, height/2, 16, 16);
			if (key == CODED)
			{
				if(keyCode==UP) 
				{
					ellipse(x*xspacing, height/4+yvalues[x], 16, 16);
					//ellipse(x*xspacing, height/4, 16, 16);
				}
				else if (keyCode==DOWN)
				{
					ellipse(x*xspacing, 3*(height/4)+yvalues[x], 16, 16);
					//ellipse(x*xspacing, 3*(height/4), 16, 16);
				}
			}
		}
	} // renderWave

	/**
	 * Each Module instance has to define what to show as the legend (scale) along the bottom.
	 * 
	 * @return	String[] of the current amplitude thresholds
	 */
	@Override
	public String[] getLegendText()
	{
		String[]	result	= new String[this.menu.getCurRangeSegments()];
		for(int i = 0; i < result.length; i++)
		{
			result[i]	= this.menu.getThresholds()[0][i] + "";
		} // for

		return result;
	} // getLegendText

} // Module_03_AmplitudeHSB
