package module_02_03;
/*
 * Rachel Farah
 * Vintage Stereo Lines Controlled by Amplitude
 * */

//import core.FullScreenDisplay;
//import core.Input;
import core.Module;
import core.ModuleMenu;
import processing.core.PApplet;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_03_AmplitudeBars extends Module /*implements ShapeEditorInterface */{
	int xspacing;   // How far apart should each horizontal location be spaced
	int w;              // Width of entire wave
	double q = 10;

	double theta = 0.0;  // Start angle at 0
	double amplitude = 75.0;  // Height of wave
	double period = 500.0;  // How many pixels before the wave repeats
	double dx;  // Value for incrementing X, a function of period and xspacing
	float[] yvalues;  // Using an array to store height values for the wave

	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

	public static void main(String[] args) 
	{
		PApplet.main("module_02_03.Module_02_03_AmplitudeBars");
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
		xspacing = width/70;
		dx = (TWO_PI / period) * xspacing;
		yvalues = new float[w/xspacing];

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

	void renderWave() {
		noStroke();
		fill(255);
		// A simple way to draw the line with an ellipse at each location
		for (int x = 0; x < yvalues.length; x++) 
		{
			//checks current amplitude to determine each amplitude step size for each new line 
			double amp;
			amp = this.input.getAmplitude();
			System.out.println("Curent Amplitute: "+amp);

			fill(255,0,0);
			ellipse(x*xspacing, height+yvalues[x], 16, 16);//bottom line
			if( this.input.getAmplitude()/q>10)
			{
				fill(255,0,0);
				ellipse(x*xspacing, 15*(height)/16+yvalues[x], 16, 16);
			}

			if( this.menu.getAmplitudeFollower(0)/q>50)
			{
				fill(255,127,0);
				ellipse(x*xspacing, 7*(height)/8+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>100)
			{
				fill(255,127,0);
				ellipse(x*xspacing, 13*(height)/16+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>125)
			{
				fill(255,255,0);
				ellipse(x*xspacing, 6*(height)/8+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>150)
			{
				fill(255,255,0);
				ellipse(x*xspacing, 11*(height)/16+yvalues[x], 16, 16);
			}
			if(this.menu.getAmplitudeFollower(0)/q>160)
			{
				fill(255,255,0);
				ellipse(x*xspacing, 47*(height)/48+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>175)
			{
				fill(0,255,0);
				ellipse(x*xspacing, 5*(height)/8+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>200)
			{
				fill(0,255,0);
				ellipse(x*xspacing, 9*(height)/16+yvalues[x], 16, 16);
			}			  

			if(this.menu.getAmplitudeFollower(0)/q>225)
			{
				fill(0,255,0);
				ellipse(x*xspacing, height/2+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>250)
			{
				fill(0,0,255);
				ellipse(x*xspacing, 7*(height)/16+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>275)
			{
				fill(0,0,255);
				ellipse(x*xspacing, 3*(height)/8+yvalues[x], 16, 16);
			}			  

			if(this.menu.getAmplitudeFollower(0)/q>300)
			{
				fill(0,0,255);
				ellipse(x*xspacing, 5*(height)/16+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>325)
			{
				fill(75,0,130);
				ellipse(x*xspacing, (height)/4+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>350)
			{
				fill(75,0,130);
				ellipse(x*xspacing, 3*(height)/16+yvalues[x], 16, 16);
			}
			if(this.menu.getAmplitudeFollower(0)/q>375)
			{
				fill(148,0,211);
				ellipse(x*xspacing, (height)/8+yvalues[x], 16, 16);
			}

			if(this.menu.getAmplitudeFollower(0)/q>400)
			{
				fill(148,0,211);
				ellipse(x*xspacing, (height)/16+yvalues[x], 16, 16);
			}
		}
	}

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