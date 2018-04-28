package cadenza;
/*
 * Rachel Farah
 * Vintage Stereo Lines Controlled by Amplitude
 * */

//import core.FullScreenDisplay;
//import core.Input;
import core.Module;
import core.ModuleMenu;
import core.PortAudioAudioIO;
import processing.core.PApplet;
import core.input.RealTimeInput;
import filters.Follower;
import net.beadsproject.beads.core.AudioContext;

public class HoldMyHand extends Module /*implements ShapeEditorInterface */{

	private static final int SCENE1 = 1;
	private static final int SCENE2 = 2;

	private int sceneNum = 1;  //IMPORTANT: Scene numbers start at 1 not 0!!!

	int	soloistInput	= 0;
	int secondSoloistInput = 1;

	int[]	inputNums	= {
	//		1, 2, 3
			2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14
	};

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

	private Follower[] follower;


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

		//		 maxAmp=40;
		maxAmp=400;

		dx = (TWO_PI / period) * xspacing;

		yvalues = new float[h/yspacing];
		for(int i=0; i<yvalues.length;i++)
		{
			yvalues[i]=h-(i*yspacing);
		}

	//	this.input    = new RealTimeInput(9, new AudioContext(), this);
//		this.input	= new RealTimeInput(20, new AudioContext(new PortAudioAudioIO()) , this);
		this.input	= new RealTimeInput(25, false, this);
		this.totalNumInputs = this.input.getNumInputs();
		this.curNumInputs	= this.totalNumInputs;
		//		this.curNumInputs = 4;

		this.menu	= new ModuleMenu(this, this, this.input, 12);
	//	this.curNumInputs	= this.menu.getRecInput().getNumInputs();

		this.menu.setCurKey("G", 2);

		this.inputWidth = width/this.inputNums.length;
		this.numEllipses  = (inputWidth/xspacing) ;
		
		System.out.println("width = " + this.width + "; inputWidth = " + inputWidth + "; xSpacing = " + this.xspacing + "; numEllipses = " + this.numEllipses);

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

		//		this.menu.addColorMenu(buttonLabels, 1, true, null, false, false, "Dynamic\nSegments", 6, 6, "color");
		this.menu.addColorMenu();
		this.menu.addSensitivityMenu(false);

		this.menu.addShapeSizeSlider(0, this.yVals[15]);

		// Have to do this last so that the manually added Slider (above) doesn't get an id already used by the ShapeEditor:
		//				this.menu.addShapeMenu(1);

		this.menu.getInstrument().setADSR(1000, 500, 0, 0);
		this.menu.setBPM(30);

		//				this.textSize(32);

		this.menu.getControlP5().getController("trichrom").update();

		this.menu.setColor(0, new int[] { 254, 183, 78 }, true);
		this.menu.setColor(4, new int[] { 0, 0, 200 }, true);
		this.menu.setColor(8, new int[] { 150, 0, 150 }, true);

		this.menu.getControlP5().getController("trichrom").update();
		
		this.menu.getOutsideButtonsCP5().hide();

		this.follower = new Follower[this.curNumInputs];
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.follower[i] = new Follower();
			this.follower[i].setType("average");
			this.follower[i].setMaxVal(this.maxAmp);
			this.follower[i].setMinVal(0);
			this.follower[i].setUseLimits(true);
			this.follower[i].setScalar(this.yVals.length);
			this.follower[i].setIntOnly(true);
			this.follower[i].setMaxIncrament(3);
		}




	} // setup

	public void draw()
	{
		
		for(int i = 0; i < this.input.getNumInputs(); i++)
		{
			System.out.println(i + ": amp = " + this.input.getAmplitude(i));
		}
		
		switch(this.sceneNum)
		{
		case 1:

			this.inputWidth = this.width / (this.inputNums.length);

			this.drawScene1();
			break;

		case 2:

			this.inputWidth = this.width / (this.inputNums.length - 1);

			this.drawScene2();
			break;

		default: throw new IllegalArgumentException("not a valid scene number");
		}

		this.menu.runMenu();
	}

	private void drawScene1()
	{
		int	scaleDegree;
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.follower[i].update(this.input.getAmplitude(i));
		}
		

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		}

		//this.menu.runMenu();

		//		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		/*
		scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.menu.fadeColor(scaleDegree, this.soloistInput);
		 */
		if(this.menu.getRecInputPlaying())
		{
			scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
		} else {
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
		}

		this.menu.fadeColor(scaleDegree, this.soloistInput);		
		background(this.menu.getCurHue()[this.soloistInput][0], this.menu.getCurHue()[this.soloistInput][1], this.menu.getCurHue()[this.soloistInput][2]);

		renderWave();
	}

	private void drawScene2()
	{
		int	scaleDegree;
		int scaleDegree2;
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.follower[i].update(this.input.getAmplitude(i));
		}

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		}

		//this.menu.runMenu();

		//		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		/*
		scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.menu.fadeColor(scaleDegree, this.soloistInput);
		 */
		if(this.menu.getRecInputPlaying())
		{
			scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
		} else {
			System.out.println("input check");
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
			scaleDegree2	= (round(input.getAdjustedFundAsMidiNote(this.secondSoloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
		}

		this.menu.fadeColor(scaleDegree, this.soloistInput);	
		this.menu.fadeColor(scaleDegree, this.secondSoloistInput);
		//background(this.menu.getCurHue()[this.soloistInput][0], this.menu.getCurHue()[this.soloistInput][1], this.menu.getCurHue()[this.soloistInput][2]);

		this.background(0);
		
		
		
		

		this.fill(this.menu.getCurHue()[this.soloistInput][0], this.menu.getCurHue()[this.soloistInput][1], this.menu.getCurHue()[this.soloistInput][2]);
		this.rect(0, 0, this.width/2, this.height);

		this.fill(this.menu.getCurHue()[this.secondSoloistInput][0], this.menu.getCurHue()[this.secondSoloistInput][1], this.menu.getCurHue()[this.secondSoloistInput][2]);
		this.rect(this.width/2, 0, this.width/2, this.height);

		renderWave();
	}

	public void oldDraw()
	{
		int	scaleDegree;

		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.follower[i].update(this.input.getAmplitude(i));
		}

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		}

		this.menu.runMenu();

		//		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		/*
		scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		this.menu.fadeColor(scaleDegree, this.soloistInput);
		 */
		if(this.menu.getRecInputPlaying())
		{
			scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
		} else {
		//	System.out.println("input check");
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
		}

		this.menu.fadeColor(scaleDegree, this.soloistInput);		
		background(this.menu.getCurHue()[this.soloistInput][0], this.menu.getCurHue()[this.soloistInput][1], this.menu.getCurHue()[this.soloistInput][2]);

		renderWave();

	} // draw

	private void renderWave() 
	{
		noStroke();
		fill(255);

		int skipForSoloist = 0;

		// A simple way to draw the line with an ellipse at each location
		for(int i = 0; i<this.inputNums.length; i++)
		{
			/*
			if(i == this.soloistInput)
			{
				skipForSoloist++;
			}
			*/
			
			if(this.sceneNum == 2 && i == this.secondSoloistInput)
			{
				skipForSoloist++;
				
				if(i == this.soloistInput)
				{
					skipForSoloist++;
				}
			}
			
			
			//checks current amplitude to determine each amplitude step size for each new line 
			//amp = this.menu.getAmplitudeFollower(this.inputNums[i]);
			//ampLevel = (Math.min(amp, maxAmp)/maxAmp)*yvalues.length;

			ampLevel = this.follower[this.inputNums[i]].getScalarVal();
	//		System.out.println(i + ": ampLevel = " + ampLevel);

			//System.out.println("ampLevel: "+ampLevel);
			//System.out.println("Curent Amplitute: "+amp);

			for(int y = 0; y < ampLevel; y++)
			{
				System.out.println("y = " + y);
				for(int x = 0; x < this.numEllipses; x++)
				{
					//fill(255,0,0);
					//fill(0);
					ellipse(((x*xspacing)+(this.inputWidth*i)), (yvalues[y]), 6, 6);//bottom line
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


	public void keyPressed()
	{
		if(this.key == '1')
		{
			this.sceneNum = 1;
		}
		else if(this.key == '2')
		{
			this.sceneNum = 2;
		}
	}

} // Module_03_AmplitudeHSB