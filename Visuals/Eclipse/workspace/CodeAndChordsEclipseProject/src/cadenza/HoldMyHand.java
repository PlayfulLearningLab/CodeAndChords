package cadenza;
/*
 * Rachel Farah
 * Vintage Stereo Lines Controlled by Amplitude
 * */

//import core.FullScreenDisplay;
//import core.Input;
import core.Module;
import core.ModuleMenu;
import processing.core.PApplet;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class HoldMyHand extends Module /*implements ShapeEditorInterface */{
	int xspacing; 
	int yspacing;
	int w; 
	int h;// Width of entire wave
	double q = 10;
	float amp;
	float ampLevel;
	float maxAmp;
	float inputWidth;
	float numEllipses; 

	double theta = 0.0;  // Start angle at 0
	double amplitude = 75.0;  // Height of wave
	double period = 500.0;  // How many pixels before the wave repeats
	double dx;  // Value for incrementing X, a function of period and xspacing
	float[] yvalues;  // Using an array to store height values for the wave

	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

	public static void main(String[] args) 
	{
		PApplet.main("cadenza.HoldMyHand");
	} // main


	public void settings()
	{
		fullScreen();
	} // settings

	public void setup()
	{
		// Not specifying an AudioContext will use the PortAudioAudioIO:
		//		this.input	= new Input(this);
		 w = width;
		 h = height;
		 xspacing = width/70;
		 yspacing = height/70;
		 maxAmp=40;
		 dx = (TWO_PI / period) * xspacing;
		 yvalues = new float[h/yspacing];
		 for(int i=0; i<yspacing;i++)
		 {
			 yvalues[i]=h-(i*yspacing);
		 }

		this.input    = new RealTimeInput(1, new AudioContext(), this);
		this.totalNumInputs = this.input.getAdjustedNumInputs();
		this.curNumInputs = 4;

		this.menu	= new ModuleMenu(this, this, this.input, 12);

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
		int	scaleDegree;

		// The following line is necessary so that key press shows the menu button
		System.out.print(keyPressed);

		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		}

		this.menu.runMenu();

		//		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		scaleDegree	= (round(input.getAdjustedFundAsMidiNote(0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.menu.fadeColor(scaleDegree, 0);
		background(this.menu.getCurHue()[0][0], this.menu.getCurHue()[0][1], this.menu.getCurHue()[0][2]);

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();

		renderWave();

		for(int i=0;i<curNumInputs;i++)
		{
			this.menu.updateAmplitudeFollower(i,1);
		}

	} // draw

	void renderWave() 
	{
		noStroke();
		fill(255);
		// A simple way to draw the line with an ellipse at each location
			for(int i = 0; i<curNumInputs; i++)
			{
				//checks current amplitude to determine each amplitude step size for each new line 
				amp = this.menu.getAmplitudeFollower(i);
				ampLevel = (Math.min(amp, maxAmp)/maxAmp)*yvalues.length;
				inputWidth = width/curNumInputs; 
				numEllipses  = inputWidth/xspacing; 
				System.out.println("ampLevel: "+ampLevel);
				System.out.println("Curent Amplitute: "+amp);
				for(int y = 0; y<ampLevel; y++)
				{
					for(int x = 0; x<(numEllipses); x++)
					{
						//fill(255,0,0);
						ellipse(((x*xspacing)+((inputWidth)*i)), (yvalues[y]), 12, 12);//bottom line
					}
				}
			}

		} // renderWave

	@Override
	public String[] getLegendText()
	{
		String[]	result	= new String[this.menu.getCurRangeSegments()];
		for(int i = 0; i < result.length; i++)
		{
			result[i]	= this.menu.getThresholds()[0][i] + "";
		} // for

		return result;
	} // fillLegendText

} // Module_03_AmplitudeHSB