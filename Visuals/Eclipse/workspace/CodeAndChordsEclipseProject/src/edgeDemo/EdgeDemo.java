package edgeDemo;

import controlP5.ControlP5;
import controlP5.Toggle;
import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import processing.core.PApplet;

/**
 * Demo for Edgertones (Feb. 2017)
 * 
 * Same as Demo_01_VerticalBars.java, but with different colors and hidden Menu.
 * @author codeandchords
 *
 */
public class EdgeDemo extends Module {

	// Keycodes of each scene (49 = 1, 50 = 2) to enable scene change on key press:
	private	static final int	SCENE_BOY			= 49;	// 1
	private	static final int	SCENE_BOY_DYNAMIC	= 50;	// 2

	private	int	curScene							= EdgeDemo.SCENE_BOY;

	private int	scaleDegree	= 0;

	// These are for the dynamic bars:
	private float[]	barVel;
	private float[] barPos;


	public static void main(String[] args) {
		PApplet.main("edgeDemo.EdgeDemo");
	}

	public void settings()
	{
		this.fullScreen();
	} // settings

	public void setup()
	{
		this.totalNumInputs	= 4;

		this.input			= new RealTimeInput(this.totalNumInputs, false, this);
		this.curNumInputs	= this.input.getAdjustedNumInputs();
		//		this.input			= new RealTimeInput(1, new AudioContext(), this);

		this.menu	= new ModuleMenu(this, this, this.input, 12);
		this.menu.addSensitivityMenu(true);

		this.menu.addColorMenu();

		// I want to define all values, even on the first calls to di- and trichrom:
		this.menu.setDichromFlag(true);
		this.menu.setTrichromFlag(true);

		this.menu.setAlphaSlider(255);
		
		this.menu.showColorMenu();

		//			this.menu.setGlobal(true);
		((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
		this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

		this.menu.getControlP5().getController("trichrom").update();

		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.menu.setColor(0, new int[] { 5, 0, 234 }, i, true);
			this.menu.setColor(4, new int[] { 250, 130, 25 }, i, true);
			this.menu.setColor(8, new int[] { 98, 153, 247 }, i, true);
		}

		this.menu.getControlP5().getController("trichrom").update();


		this.verticalBarsDemo = true;
		this.amplitude = new float[16];
		this.barPos = new float[16];
		this.barVel = new float[16];

		for(int i = 0; i < this.amplitude.length; i++)
		{
			this.amplitude[i] = 0;
			this.barPos[i] = 0;
			this.barVel[i] = 0;
		}

		this.setSquareValues();

		// This Button allows the dynamicBar functionality to be turned on or off in the Menu:
		this.menu.getControlP5().addToggle("dynamicBars")
		.setSize(100, 40)
		.setPosition(700, 20)
		.setState(false)
		.setId(99999)
		.moveTo("sensitivity")
		.setLabel("Dynamic Bar Height")
		.getCaptionLabel()
		.align(ControlP5.CENTER, ControlP5.CENTER);

		this.menu.setUseRecInput(false);

		this.noStroke();
		
		// Hide all the buttons:
		((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		this.menu.getOutsideButtonsCP5().getController("hamburger").hide();
		this.menu.getOutsideButtonsCP5().getController("hamburger").setClickable(false);
		this.menu.getOutsideButtonsCP5().getController("play").hide();
	} // setup

	public void draw()
	{
		this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		this.drawScene();
		this.menu.runMenu();
		
		int	fadeThis	= 0;

		this.menu.universalFade(fadeThis);
		
		System.out.println("fadeThis = " + fadeThis);
	} // draw

	/*
	 * The body of each if() is the part of the program that would be in the draw() method 
	 * if that particular scene was an independent Module.
	 */
	private void drawScene()
	{
		if(this.curScene == EdgeDemo.SCENE_BOY)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{					

				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						

				this.menu.fadeColor(scaleDegree, i);

				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());

				int	curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);
				int	curY	= (int)this.menu.mapCurrentYPos(this.yVals[i]);

				this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for - curNumInputs
		} // SCENE_BOY

		else if(this.curScene == EdgeDemo.SCENE_BOY_DYNAMIC)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{					
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						

				this.menu.fadeColor(scaleDegree, i);

				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());

				int	curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);

				//Value from 0 to 1 to act as a percent of the screen that should be covered
				float amp = (float) Math.min(1, this.input.getAmplitude(i) / 100/*max amp*/);
				//amp = (float) Math.max(amp, .1);

				if(amp > this.barPos[i])
				{
					this.barVel[i] = (float) Math.min(this.barVel[i] + .02, .2);

					if(this.barVel[i] < 0)
					{
						this.barVel[i] = 0;
					}
				}
				else
				{
					this.barVel[i] = (float) Math.max(this.barVel[i] - .02, -.2);

					if(this.barVel[i] > 0)
					{
						this.barVel[i] = 0;
					}
				}

				this.barPos[i] = this.barPos[i] + (amp - this.barPos[i])/10;

				this.rect(curX,this.menu.mapCurrentYPos((this.height/2)- this.barPos[i]*(this.height/2)), this.rectWidths[i], this.height*this.barPos[i]);

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for - curNumInputs
		} // SCENE_BOY_DYNAMIC

	} // drawScene


	/*
	 * Scene changes happen on key press;
	 * each if() body is essentially the setup() for that particular scene.
	 */
	public void keyPressed()
	{
		int	key	= (int)this.key;

		this.menu.setCanvasColor(new int[] { 0, 0, 0 });

		this.curScene	= key;
		System.out.println("curScene = " + this.curScene);			

		if(this.curScene == EdgeDemo.SCENE_BOY)
		{
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.hideShapeMenu();

			this.menu.setAlphaSlider(255);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
/*
			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 0, 0 }, i, true);
				this.menu.setColor(4, new int[] { 243, 130, 5 }, i, true);
				this.menu.setColor(8, new int[] { 244, 226, 31 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			*/
		} // SCENE_BOY

		else if (this.curScene == EdgeDemo.SCENE_BOY_DYNAMIC)
		{
			this.menu.setDynamicBars(true);
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.hideShapeMenu();

			this.menu.setAlphaSlider(255);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
/*
			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 0, 0 }, i, true);
				this.menu.setColor(11, new int[] { 243, 130, 5 }, i, true);	// so that it works coming from dichromatic
				this.menu.setColor(4, new int[] { 243, 130, 5 }, i, true);
				this.menu.setColor(8, new int[] { 244, 226, 31 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
*/
		} // SCENE_BOY_DYNAMIC

	} // keyPressed

	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
