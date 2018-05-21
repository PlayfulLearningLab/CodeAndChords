package talentShow;

import controlP5.ControlP5;
import controlP5.Toggle;
import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import processing.core.PApplet;

public class TalentShow extends Module {

	private	static final int	SCENE_DEMO				= 0;
	//private	static final int	SCENE_SANDMAN			= 1;
	private	static final int	SCENE_LOLLIPOP_DYNAMIC	= 1;

	private	int	curScene							= TalentShow.SCENE_DEMO;

	private int	scaleDegree	= 0;

	// These are for the dynamic bars:
	private float[]	barVel;
	private float[] barPos;
	
	private	int[]	inputNums	= new int[] {
			0, // 16,
			1, // 17,
			2,
			3
	};
	private	int	demoInput	= 0; // 17


	public static void main(String[] args) {
		PApplet.main("talentShow.TalentShow");
	}

	public void settings()
	{
		this.fullScreen();
	} // settings

	public void setup()
	{
		this.totalNumInputs	= 24;
		this.input			= new RealTimeInput(this.totalNumInputs, false, this);
		// Setting this to 1 so that we have a nicely sized legend for the demo; no one else will use it.
		this.curNumInputs	= 1;

		this.menu	= new ModuleMenu(this, this, this.input, 12);
		this.menu.addSensitivityMenu(true);

		this.menu.addColorMenu();

		// I want to define all values, even on the first calls to di- and trichrom:
		this.menu.setDichromFlag(true);
		this.menu.setTrichromFlag(true);

		this.menu.setAlphaSlider(255);

		// Starts on "If I Were A Boy":
		this.menu.showColorMenu();

		//			this.menu.setGlobal(true);
		((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
		this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

		((Toggle)this.menu.getControlP5().getController("rainbow")).setValue(true);

		this.verticalBarsDemo = true;
		this.amplitude = new float[this.totalNumInputs];
		this.barPos = new float[this.totalNumInputs];
		this.barVel = new float[this.totalNumInputs];

		for(int i = 0; i < this.amplitude.length; i++)
		{
			this.amplitude[i] = 0;
			this.barPos[i] = 0;
			this.barVel[i] = 0;
		}

		this.setSquareValues();

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

	} // draw

	private void drawScene()
	{
		if(this.curScene == TalentShow.SCENE_DEMO)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.demoInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						

			this.menu.fadeColor(scaleDegree, this.demoInput);

			this.fill(this.menu.getCurHue()[this.demoInput][0], this.menu.getCurHue()[this.demoInput][1], this.menu.getCurHue()[this.demoInput][2], this.menu.getAlphaVal());

			this.rect(0, 0, this.width, this.height);

			this.legend(this.scaleDegree, this.demoInput);
		}

		else if(this.curScene == TalentShow.SCENE_LOLLIPOP_DYNAMIC)
		{
			for(int i = 0; i < this.inputNums.length; i++)
			{					
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(inputNums[i])) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						

				this.menu.fadeColor(scaleDegree, inputNums[i]);

				this.fill(this.menu.getCurHue()[inputNums[i]][0], this.menu.getCurHue()[inputNums[i]][1], this.menu.getCurHue()[inputNums[i]][2], this.menu.getAlphaVal());

				int	curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);

				//Value from 0 to 1 to act as a percent of the screen that should be covered
				float amp = (float) Math.min(1, this.input.getAmplitude(inputNums[i]) / 100/*max amp*/);
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
			} // for - curNumInputs
		} // SCENE_LOLLIPOP_DYNAMIC

	} // drawScene


	public void keyPressed()
	{
		this.curScene	= (this.curScene + 1) % 2;
		System.out.println("curScene = " + this.curScene);			

		if(this.curScene == TalentShow.SCENE_DEMO)
		{
			((Toggle)this.menu.getControlP5().getController("rainbow")).setValue(true);
			this.curNumInputs	= 1;
			this.setSquareValues();
		}
		else if (this.curScene == TalentShow.SCENE_LOLLIPOP_DYNAMIC)
		{

			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.totalNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 5, 0, 234 }, i, true);
				this.menu.setColor(4, new int[] { 250, 130, 25 }, i, true);
				this.menu.setColor(8, new int[] { 98, 153, 247 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			this.menu.setDynamicBars(true);
			this.curNumInputs	= 4;
			this.setSquareValues();
		} // SCENE_BOY_DYNAMIC

	} // keyPressed

	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
