package karaoke_demo;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import controlP5.ControlP5;
import controlP5.Toggle;
import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import processing.core.PApplet;
import processing.core.PConstants;

public class Karaoke_Demo extends Module {
	
	private	String[]	lionSleepsTonight = new String[] {
		"Wimoweh, wimoweh, wimoweh, wimoweh",
		"In the jungle, the mighty jungle, the lion sleeps tonight.",
		"In the jungle, the quiet jungle, the lion sleeps tonight.",
		"Wimoweh, wimoweh, wimoweh, wimoweh",
		"Near the village, the peaceful village, the lion sleeps tonight.",
		"Near the village, the quiet village, the lion sleeps tonight.",
	};
	private	String[]	dontStopBelieving;
	private	String[]	proudMary;
	// Ain't No Mountain High Enough
	// Sound of Silence? :)
	// ** California Dreamin'
	// T Swift: Shake It Off? Stay Stay Stay;
	// Lean on Me
	// Elvis?
	// Beatles?
	// Someday Maybe
	// Ring of Fire; Walk the Line?
	// Say Something?
	// Unforgettable
	// True Colors :)
	// Mammas Don't Let your Babies Grow Up to be Cowboys
	// Start of Something New/Breaking Free
	// First Time in Forever!
	// City of Stars/alia La La Land

	// Initial demos:
	private	static final int	SCENE_OPEN			= 57;	// 9
	private	static final int	SCENE_CLAP			= 48;	// 0
	private	static final int	SCENE_SOLOIST		= 49;	// 1
	private	static final int	SCENE_LULLABY		= 50;	// 2
	private	static final int	SCENE_RAINBOW_ROUND	= 51;	// 3
	private	static final int	SCENE_DUET			= 52;	// 4
	//	private	static final int	SCENE_DRUM_VOCAL	= 112;	// p

	// Shahzore:
	private	static final int	SCENE_SHAH_GIFTS		= 122;	// z
	private	static final int	SCENE_SHAH_HAL			= 120;	// x

	// Shahzore, Taylor, and Betsie:
	private	static final int	SCENE_TRIO				= 99;	// c

	// Taylor and Betsie:
	private	static final int	SCENE_RIVER				= 97;	// a
	private	static final int	SCENE_STRAIGHTEN		= 115;	// s
	private	static final int	SCENE_CAT				= 100;	// d
	private	static final int	SCENE_PRAYER			= 102;	// f	// 

	// Cadenza:
	private	static final int	SCENE_KALEIDESCOPE		= 113;	// q
	private	static final int	SCENE_KALEIDESCOPE_SPIN	= 119;	// w
	private	static final int	SCENE_MAN				= 101;	// e
	private	static final int	SCENE_KILLING			= 114;	// r
	private	static final int	SCENE_KILLING_DYNAMIC	= 116;	// t
	private	static final int	SCENE_WINGS_NO_BB		= 121;	// y
	private	static final int	SCENE_WINGS_BB_CENTER	= 117;	// u
	private	static final int	SCENE_WINGS_BB_BOTTOM	= 105;	// i
	private	static final int	SCENE_WINGS_LARGESHAPES	= 111;	// o
	private	static final int	SCENE_WINGS_RAINBOW		= 112;	// p
	private	static final int	SCENE_WINGS_DYNAMIC		= 91;	// [

	private	int	curScene							= Karaoke_Demo.SCENE_OPEN;

	private	int	clapInput0	= 0;
	//	private	int	clapShape0	= 0;
	
	private	int	ampDemoInput	= 6;
	private	int	soloistInput	= 1;
	private	int	lullabyInput	= 1;

	private	int	trioInput0	= 0;	// Shahzore
	private	int	trioInput1	= 1;	// Taylor
	private	int	trioInput2	= 2;	// Betsie

	private	int	shahInput0	= 0;
	private	int	shahInput1	= 1;

	private	int	betsieInput	= 0;
	private	int	taylorInput	= 1;

	private	int	rainbowRoundInput0	= 0;
	//	private	int	rainbowRoundShape0	= 0;
	private	int	rainbowRoundInput1	= 1;
	//	private	int	rainbowRoundShape1	= 1;
	private	int	rainbowRoundInput2	= this.betsieInput;
	//	private	int	rainbowRoundShape2	= 2;

	private	int	beatBoxInput	= 0;

	private	ShapeEditor	shapeEditor;

	private int	scaleDegree	= 0;
	private int	curX;
	private int	curY;
	private	int[]	curHue;

	// These are for the dynamic bars:
	private float[]	barVel;
	private float[] barPos;

	private	int[]	ampHist;
	private	int[]	ampHist1;
	private	int		ampWidth;
	private	static final int NUM_AMPS	= 350;
	
	private	int	curLyricsLine	= 0;


	public static void main(String[] args) {
		PApplet.main("karaoke_demo.Karaoke_Demo");
	}

	public void settings()
	{
		this.fullScreen(2);
	} // settings

	public void setup()
	{	
		this.totalNumInputs	= 16;
		this.curNumInputs	= 1;

		this.input			= new RealTimeInput(this.totalNumInputs, true, this);
		//		this.input			= new RealTimeInput(1, new AudioContext(), this);

		this.menu	= new ModuleMenu(this, this, this.input, 12);
		this.menu.addSensitivityMenu(true);
		this.menu.addLyricMenu();

		this.menu.addColorMenu();
		this.menu.hideColorMenu();

		// I want to define all values, even on the first calls to di- and trichrom:
		this.menu.setDichromFlag(true);
		this.menu.setTrichromFlag(true);

		//		this.menu.addColorMenu(new String[] { "white" }, 1, null, false, null, 0, 0, "OneColor");


		// Have to add this last, else it overwrites Controllers and causes an infinite loop:
		this.menu.addShapeMenu(8);
		this.shapeEditor	= this.menu.getShapeEditor();	// for convenience/speed
		/*
		// Starts on clapping scene:
		this.menu.setColor(0, new int[] { 255,  255, 255 }, true);
		this.shapeEditor.getShape().setCurrentShape("square");
		this.shapeEditor.getShape().setShapeScale(3);
		 */

		// Starts on Betsie's scene:
		this.curNumInputs	= 1;
		//this.setSquareValues();

		Shape	curShape	= this.shapeEditor.getShapes()[this.betsieInput];
		curShape.setCurrentShape("circle");
		curShape.setShapeScale(2);

		this.menu.getControlP5().getController("trichrom").update();

		this.menu.setColor(0, new int[] { 255, 0, 0 }, true);
		this.menu.setColor(4, new int[] { 255, 255, 0 }, true);
		this.menu.setColor(8, new int[] { 0, 0, 255 }, true);

		this.menu.getControlP5().getController("trichrom").update();

//		this.menu.getOutsideButtonsCP5().getController("hamburger").hide();
		this.menu.getOutsideButtonsCP5().getController("play").hide();	

		//

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

		this.menu.getControlP5().addToggle("dynamicBars")
		.setSize(100, 40)
		.setPosition(700, 20)
		.setState(false)
		.setId(99999)
		.moveTo("sensitivity")
		.setLabel("Dynamic Bar Height")
		.getCaptionLabel()
		.align(ControlP5.CENTER, ControlP5.CENTER);

		this.menu.setUseRecInput(true);

		this.ampHist	= new int[Karaoke_Demo.NUM_AMPS];
		this.ampHist1	= new int[Karaoke_Demo.NUM_AMPS];
		//		this.ampWidth	= (this.width / 3 * 2) / SMM_Demo.NUM_AMPS;
		this.ampWidth	= (this.width) / Karaoke_Demo.NUM_AMPS;

		this.noStroke();
	} // setup

	public void draw()
	{
		this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		this.drawScene();
		this.menu.runMenu();

		if(this.menu.getShowLyrics() && (this.menu.getCurLyrics().size() > 0))
		{
			this.stroke(0);
			this.fill(255);
			this.textSize(32);
			this.text(this.menu.getCurLyrics().get(curLyricsLine), this.menu.mapCurrentXPos(100), this.menu.mapCurrentYPos(100));
		}
	} // draw

	private void drawScene()
	{
		if(this.curScene == Karaoke_Demo.SCENE_OPEN)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.betsieInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.betsieInput);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.betsieInput);
			}
		} // SCENE_OPEN

		else if(this.curScene == Karaoke_Demo.SCENE_CLAP)
		{
			this.menu.fade(0, this.clapInput0);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(clapInput0);
			}
		} // SCENE_CLAP


		if(this.curScene == Karaoke_Demo.SCENE_RAINBOW_ROUND)
		{

			// First:
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, rainbowRoundInput0);

			//			this.fill(this.menu.getCurHue()[rainbowRoundInput0][0], this.menu.getCurHue()[rainbowRoundInput0][1], this.menu.getCurHue()[rainbowRoundInput0][2]);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(rainbowRoundInput0);
			}

			// Second:
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, rainbowRoundInput1);

			//			this.fill(this.menu.getCurHue()[rainbowRoundInput1][0], this.menu.getCurHue()[rainbowRoundInput1][1], this.menu.getCurHue()[rainbowRoundInput1][2]);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(rainbowRoundInput1);
			}

			// Third:
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput2)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, rainbowRoundInput2);

			//			this.fill(this.menu.getCurHue()[rainbowRoundInput2][0], this.menu.getCurHue()[rainbowRoundInput2][1], this.menu.getCurHue()[rainbowRoundInput2][2]);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(rainbowRoundInput2);
			}

			// Draw the square over the bottom to make these a rainbow:
			this.menu.fade(this.scaleDegree, 4);
			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(4);
			}

			if(this.menu.isShowScale() && !this.shapeEditor.getIsRunning())
			{
				// draws the legend along the bottom of the screen:
				this.legend(scaleDegree, 0);
			} // if showScale
		} // SCENE_RAINBOW_ROUND

		if(this.curScene == Karaoke_Demo.SCENE_SOLOIST)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloistInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(this.scaleDegree, this.soloistInput);
			this.curHue	= this.menu.getCurHue()[this.soloistInput];
			this.fill(this.curHue[0], this.curHue[1], this.curHue[2]);

			curX	= (int)this.menu.mapCurrentXPos(0);
			curY	= (int)this.menu.mapCurrentYPos(0);
			this.rect(curX, curY, (int)this.menu.mapCurrentXPos(this.width), (int)this.menu.mapCurrentYPos(this.height));
			if(this.menu.isShowScale())
			{
				// draws the legend along the bottom of the screen:
//				this.legend(scaleDegree, this.soloistInput);
				this.legend(scaleDegree, 0);
			} // if showScale
		} // SCENE_SOLOIST et alia


		else if(this.curScene == Karaoke_Demo.SCENE_LULLABY)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.lullabyInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(this.scaleDegree, this.lullabyInput);
			this.curHue	= this.menu.getCurHue()[this.lullabyInput];
			this.fill(this.curHue[0], this.curHue[1], this.curHue[2]);

			curX	= (int)this.menu.mapCurrentXPos(0);
			curY	= (int)this.menu.mapCurrentYPos(0);
			this.rect(curX, curY, (int)this.menu.mapCurrentXPos(this.width), (int)this.menu.mapCurrentYPos(this.height));
			if(this.menu.isShowScale())
			{
				// draws the legend along the bottom of the screen:
//				this.legend(scaleDegree, this.lullabyInput);
				this.legend(scaleDegree, 0);
			} // if showScale
		} // SCENE_LULLABY

		if(this.curScene == Karaoke_Demo.SCENE_TRIO )
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.trioInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.trioInput0);

			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.trioInput1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.trioInput1);

			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.trioInput2)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.trioInput2);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.trioInput0);
				this.shapeEditor.drawShape(this.trioInput1);
				this.shapeEditor.drawShape(this.trioInput2);
			}
		} // SCENE_TRIO

		//////////////////////// Shahzore ///////////////////////////		
		else if(this.curScene == Karaoke_Demo.SCENE_SHAH_HAL)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.shahInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.shahInput0);

			//				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.shahInput0);
			}


			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.shahInput1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.shahInput1);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.shahInput1);
			}

		} // SCENE_SHAH_HAL

		else if(this.curScene == Karaoke_Demo.SCENE_SHAH_GIFTS)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.shahInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.shahInput0);

			this.curHue	= this.menu.getCurHue()[this.shahInput0];
			this.fill(this.curHue[0], this.curHue[1], this.curHue[2]);
			this.rect(this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(0),this.width, this.height);

			this.menu.fade(this.scaleDegree, 4);
			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(4);
			}

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.shahInput0);
			}

			
			// TODO - did this actually set the attack?
			// (Should the next one un-set it?)
			this.menu.setAttRelTranVal(0, this.shahInput0, 100);

		} // SCENE_SHAH_GIFTS

		////////////////// Taylor & Betsie ///////////////////////
		else if(this.curScene == Karaoke_Demo.SCENE_STRAIGHTEN)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.taylorInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(this.scaleDegree, this.taylorInput);

			//				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.taylorInput);
			}

		} // SCENE_STRAIGHTEN

		else if(this.curScene == Karaoke_Demo.SCENE_CAT)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.taylorInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.taylorInput);

			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.betsieInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.betsieInput);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.taylorInput);
				this.shapeEditor.drawShape(this.betsieInput);
			}

			if(this.menu.isShowScale())
			{
				this.legend(this.scaleDegree, this.taylorInput);
				this.legend(this.scaleDegree, this.betsieInput);
			}

			this.shapeEditor.getShapes()[this.taylorInput].setShapeScale(this.input.getAmplitude(this.taylorInput) / 10000);
			//			this.shapeEditor.getShapes()[1].setShapeScale(this.input.getAmplitude(1) / 10000);
		} // SCENE_CAT

		else if(this.curScene == Karaoke_Demo.SCENE_RIVER)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.taylorInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.taylorInput);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.taylorInput);
			}
		} // SCENE_RIVER

		else if(this.curScene == Karaoke_Demo.SCENE_PRAYER)
		{
			// Taylor:
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.taylorInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				this.menu.fade(scaleDegree, this.taylorInput);

				this.curHue	= this.menu.getCurHue()[this.taylorInput];
				this.fill(this.curHue[0], this.curHue[1], this.curHue[2], this.menu.getAlphaVal());

				int	curX	= (int)this.menu.mapCurrentXPos(this.xVals[0]);
				int	curY	= (int)this.menu.mapCurrentYPos(this.yVals[0]);

					//Value from 0 to 1 to act as a percent of the screen that should be covered
					float amp = (float) Math.min(1, this.input.getAmplitude(this.taylorInput) / 100/*max amp*/);
					//amp = (float) Math.max(amp, .1);

					if(amp > this.barPos[0])
					{
						this.barVel[0] = (float) Math.min(this.barVel[0] + .02, .2);

						if(this.barVel[0] < 0)
						{
							this.barVel[0] = 0;
						}
					}
					else
					{
						this.barVel[0] = (float) Math.max(this.barVel[0] - .02, -.2);

						if(this.barVel[0] > 0)
						{
							this.barVel[0] = 0;
						}
					}

					this.barPos[0] = this.barPos[0] + (amp - this.barPos[0])/10;

					this.rect(curX,this.menu.mapCurrentYPos((this.height/2)- this.barPos[0]*(this.height/2)), this.rectWidths[0], this.height*this.barPos[0]);
	
					// Betsie:
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.betsieInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
					this.menu.fade(scaleDegree, this.betsieInput);

					this.curHue	= this.menu.getCurHue()[this.betsieInput];
					this.fill(this.curHue[0], this.curHue[1], this.curHue[2], this.menu.getAlphaVal());

					curX	= (int)this.menu.mapCurrentXPos(this.xVals[1]);
					curY	= (int)this.menu.mapCurrentYPos(this.yVals[1]);

						//Value from 0 to 1 to act as a percent of the screen that should be covered
						amp = (float) Math.min(1, this.input.getAmplitude(this.betsieInput) / 100/*max amp*/);
						//amp = (float) Math.max(amp, .1);

						if(amp > this.barPos[1])
						{
							this.barVel[1] = (float) Math.min(this.barVel[1] + .02, .2);

							if(this.barVel[1] < 0)
							{
								this.barVel[1] = 0;
							}
						}
						else
						{
							this.barVel[1] = (float) Math.max(this.barVel[1] - .02, -.2);

							if(this.barVel[1] > 0)
							{
								this.barVel[1] = 0;
							}
						}

						this.barPos[1] = this.barPos[1] + (amp - this.barPos[1])/10;

						this.rect(curX,this.menu.mapCurrentYPos((this.height/2)- this.barPos[1]*(this.height/2)), this.rectWidths[1], this.height*this.barPos[1]);

		} // SCENE_PRAYER


		//////////////////////////// Cadenza: ////////////////////////////////
		else if(this.curScene == Karaoke_Demo.SCENE_KALEIDESCOPE)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				//					this.menu.fade(this.scaleDegree, i);
				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}

			} // for
		} // SCENE_KALEIDESCOPE

		else if(this.curScene == Karaoke_Demo.SCENE_KALEIDESCOPE_SPIN)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				//					this.menu.fade(this.scaleDegree, i);
				this.menu.fade(this.scaleDegree, i);

				this.shapeEditor.getShapes()[i].setRotation(this.shapeEditor.getShapes()[i].getRotation() + (this.input.getAmplitude(i) / 100000));

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}

			} // for
		} // SCENE_KALEIDESCOPE_SPIN

		else if(this.curScene == Karaoke_Demo.SCENE_MAN)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				//					this.menu.fade(this.scaleDegree, i);
				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}

			} // for
		} // SCENE_MAN

		else if(this.curScene == Karaoke_Demo.SCENE_KILLING)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{					
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(scaleDegree, i);

				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());

				int	curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);
				int	curY	= (int)this.menu.mapCurrentYPos(this.yVals[i]);

				this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);

			} // for - curNumInputs
		} // SCENE_KILLING

		else if(this.curScene == Karaoke_Demo.SCENE_KILLING_DYNAMIC)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{					
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(scaleDegree, i);

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

			} // for - curNumInputs
		} // SCENE_KILLING_DYNAMIC

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_NO_BB)
		{
			// Draw the shapes:
			for(int i = 1; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}
			} // for

		} // SCENE_WINGS_NO_BB

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_BB_CENTER)
		{
			// Draw the shapes:
			for(int i = 1; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}
			} // for

			// Amplitude bars:
			this.fill(255, this.menu.getAlphaVal());

			for(int i = 0; i < (this.ampHist.length - 1); i++)
			{
				ampHist[i]	= ampHist[i+1];

				this.rect((i * (this.ampWidth + 1)), (this.height / 2), this.ampWidth, this.ampHist[i] / 3);
				this.rect((i * (this.ampWidth + 1)), (this.height / 2), this.ampWidth, (this.ampHist[i] / 3) * (-1));
			}

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(this.beatBoxInput);

		} // SCENE_WINGS_BB_CENTER

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_BB_BOTTOM)
		{
			// Draw the shapes:
			for(int i = 1; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}
			} // for

			// Amplitude bars:
			this.fill(255, this.menu.getAlphaVal());

			for(int i = 0; i < (this.ampHist.length - 1); i++)
			{
				ampHist[i]	= ampHist[i+1];

				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min(this.ampHist[i] / 3, 150));
				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min((this.ampHist[i] / 3), 150) * (-1));

			}

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(this.beatBoxInput);
		} // SCENE_WINGS_BB_BOTTOM

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_LARGESHAPES)
		{
			// Draw the shapes:
			for(int i = 1; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}
			} // for

			// Amplitude bars:
			this.fill(255, this.menu.getAlphaVal());

			for(int i = 0; i < (this.ampHist.length - 1); i++)
			{
				ampHist[i]	= ampHist[i+1];

				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min(this.ampHist[i] / 3, 150));
				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min((this.ampHist[i] / 3), 150) * (-1));

			}

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(this.beatBoxInput);
		} // SCENE_WINGS_LARGESHAPES

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_RAINBOW)
		{
			// Draw the shapes:
			for(int i = 1; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(this.scaleDegree, i);

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}
			} // for

			// Amplitude bars:
			this.fill(255, this.menu.getAlphaVal());

			for(int i = 0; i < (this.ampHist.length - 1); i++)
			{
				ampHist[i]	= ampHist[i+1];

				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min(this.ampHist[i] / 3, 150));
				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min((this.ampHist[i] / 3), 150) * (-1));

			}

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(this.beatBoxInput);
		} // SCENE_WINGS_RAINBOW

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_DYNAMIC)
		{
			// Draw the shapes:
			for(int i = 1; i < this.curNumInputs; i++)
			{
				if(!this.menu.getRecInputPlaying())
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;						
				} else {
					this.scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;												
				}

				this.menu.fade(this.scaleDegree, i);

				this.shapeEditor.getShapes()[i].setShapeScale(5 * ( Math.min((this.input.getAmplitude(i) / 1000), 1) ) );

				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}
			} // for

			// Amplitude bars:
			this.fill(255, this.menu.getAlphaVal());

			for(int i = 0; i < (this.ampHist.length - 1); i++)
			{
				ampHist[i]	= ampHist[i+1];

				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min(this.ampHist[i] / 3, 150));
				this.rect((i * (this.ampWidth + 1)), (this.height - 150), this.ampWidth, Math.min((this.ampHist[i] / 3), 150) * (-1));
			}

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(this.beatBoxInput);
		} // SCENE_WINGS_DYNAMIC

	} // drawScene

	
	public void keyPressed()
	{
		// Don't automatically change the scene in case we just want arrow keys:
		
		int	key	= this.curScene;

		this.menu.getOutsideButtonsCP5().getController("hamburger").hide();
		this.menu.setCanvasColor(new int[] { 0, 0, 0 });

		this.menu.setAttRelTranVal(0, this.shahInput0, 400);
		this.menu.setAttRelTranVal(1, this.shahInput0, 400);
		
		this.menu.setAttRelTranVal(0, this.clapInput0, 400);
		this.menu.setAttRelTranVal(1, this.clapInput0, 400);

		if(this.key == PConstants.CODED)
		{
			if(this.keyCode == PConstants.RIGHT)
			{
				this.curLyricsLine	= (this.curLyricsLine + 1) % this.menu.getCurLyrics().size();
			} else if(this.keyCode == PConstants.LEFT)
			{
				this.curLyricsLine	= (this.curLyricsLine - 1 + this.menu.getCurLyrics().size()) % this.menu.getCurLyrics().size();
			}
		} else {
			key	= (int)this.key;
		}
		
		if(key == 107)
		{
			//			this.menu.setDynamicBars(!this.menu.getDynamicBars());
			this.menu.setDynamicBars(true);
		} else {
			this.curScene	= key;
			System.out.println("curScene = " + this.curScene);			
		}

		if(this.curScene == Karaoke_Demo.SCENE_OPEN)
		{
			this.curNumInputs	= 1;
			//this.setSquareValues();

			Shape	curShape	= this.shapeEditor.getShapes()[this.betsieInput];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(2);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setColor(0, new int[] { 255, 0, 0 }, true);
			this.menu.setColor(4, new int[] { 255, 255, 0 }, true);
			this.menu.setColor(8, new int[] { 0, 0, 255 }, true);

			this.menu.getControlP5().getController("trichrom").update();
			this.menu.getOutsideButtonsCP5().getController("hamburger").hide();	
			this.menu.getOutsideButtonsCP5().getController("play").hide();	
		} // SCENE_OPEN
		else if(this.curScene == Karaoke_Demo.SCENE_CLAP)
		{
			this.menu.getOutsideButtonsCP5().getController("hamburger").show();

			this.curNumInputs	= 1;
			//this.setSquareValues();

			Shape	curShape	= this.shapeEditor.getShapes()[this.clapInput0];
			curShape.setCurrentShape("square");
			curShape.setShapeScale(2);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			this.menu.hideColorMenu();
			//			this.menu.showColorMenu("OneColor");
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);	
			
			this.menu.setAttRelTranVal(0, this.clapInput0, 50);
			this.menu.setAttRelTranVal(1, this.clapInput0, 50);
		}

		else if(this.curScene == Karaoke_Demo.SCENE_RAINBOW_ROUND)
		{
			this.curNumInputs	= 3;
			//this.setSquareValues();

			this.menu.showShapeMenu();
			this.menu.showColorMenu();

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);

			// Set "rainbow" colors:
			int[][]	singARainbowColors	= new int[][] {
				new int[] { 255, 0, 0 }, // RED
				new int[] { 255, 255, 0 }, // YELLOW
				new int[] { 0, 0, 255 }, // BLUE
				new int[] { 255, 125, 0 }, // orange
				new int[] { 0, 255, 0 }, // GREEN
				new int[] { 136, 0, 170 }, // PURPLE
				new int[] { 80, 0, 170 } // (indigo)
			};

			for(int i = 0; i < this.menu.getColors()[0].length; i++)
			{
				this.menu.setColor(i, singARainbowColors[this.menu.getScaleDegreeColors()[0][i]], false);

				this.menu.setColorSelectCW(i, singARainbowColors[this.menu.getScaleDegreeColors()[0][i]]);
			} // for - i

			this.menu.setCurKey("C", 0);

			// First input goes furthest back:
			this.shapeEditor.getShapes()[this.rainbowRoundInput0].setShapeScale(4);
			this.shapeEditor.getShapes()[this.rainbowRoundInput1].setShapeScale(2.5f);
			this.shapeEditor.getShapes()[this.rainbowRoundInput2].setShapeScale(1);

			this.shapeEditor.getShapes()[this.rainbowRoundInput0].setCurrentShape("circle");
			this.shapeEditor.getShapes()[this.rainbowRoundInput1].setCurrentShape("circle");
			this.shapeEditor.getShapes()[this.rainbowRoundInput2].setCurrentShape("circle");

			this.shapeEditor.getShapes()[this.rainbowRoundInput0].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundInput0].setYPos(this.height - 200);
			this.shapeEditor.getShapes()[this.rainbowRoundInput1].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundInput1].setYPos(this.height - 200);
			this.shapeEditor.getShapes()[this.rainbowRoundInput2].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundInput2].setYPos(this.height - 200);

			Shape	square	= this.shapeEditor.getShapes()[4];
			square.setCurrentShape("square");
			square.setShapeScale(10);
			square.setXPos(this.width / 2);
			square.setYPos(this.height + 500);
		} // SCENE_RAINBOW_ROUND		

		else if(this.curScene == Karaoke_Demo.SCENE_SOLOIST)
		{
			this.menu.getOutsideButtonsCP5().getController("hamburger").show();

			this.curNumInputs	= 1;
			this.setSquareValues();

			this.menu.hideShapeMenu();
			this.menu.showColorMenu();

			this.menu.setCurKey("C", 2);
			this.menu.getControlP5().getController("rainbow").update();
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(false);
		}

		else if(this.curScene == Karaoke_Demo.SCENE_LULLABY)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();

			this.menu.hideShapeMenu();
			this.menu.showColorMenu();

			this.menu.setCurKey("C", 2);

			this.menu.setColor(0, new int[] { 0, 100, 255 }, true);
			this.menu.setColor(11, new int[] { 255, 255, 255 }, true);
			this.menu.getControlP5().getController("dichrom").update();
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(false);
		}

		else if(this.curScene == Karaoke_Demo.SCENE_TRIO)
		{
			this.curNumInputs	= 3;
			//this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	shape0	= this.shapeEditor.getShapes()[this.trioInput0];
			Shape	shape1	= this.shapeEditor.getShapes()[this.trioInput1];
			Shape	shape2	= this.shapeEditor.getShapes()[this.trioInput2];

			shape0.setCurrentShape("circle");
			shape1.setCurrentShape("circle");
			shape2.setCurrentShape("circle");

			shape0.setShapeScale(3);
			shape1.setShapeScale(3);
			shape2.setShapeScale(3);

			shape0.setXPos(this.width / 4);
			shape1.setXPos((this.width / 4) * 2);
			shape2.setXPos((this.width / 4) * 3);

			shape0.setYPos(this.height / 2);
			shape1.setYPos(this.height / 2);
			shape2.setYPos(this.height / 2);

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);
			this.menu.setCurKey("C", 2);	// set to Chromatic

			// Calling trichrom.update sets ensures that specialColorsPos will be correct for this key,
			// since the second call to update will set trichrom with whatever colors are at the previous specialColorsPos:
			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 150, 0, 150 }, i, true);
				this.menu.setColor(4, new int[] { 92, 16, 118 }, i, true);
				this.menu.setColor(8, new int[] { 0, 163, 255 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();

		} // SCENE_TRIO

		else if(this.curScene == Karaoke_Demo.SCENE_DUET)
		{
			this.menu.setCurKey("C", 2);

			this.curNumInputs	= 2;
			//this.setSquareValues();
			this.menu.setDynamicBars(true);

			this.menu.showColorMenu();
			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.getControlP5().getController("rainbow").update();
		} // SCENE_DUET

		///////////////////// Shahzore://///////////////////
		else if(this.curScene == Karaoke_Demo.SCENE_SHAH_HAL)
		{
			this.curNumInputs	= 2;
			//this.setSquareValues();

			Shape	curShape	= this.shapeEditor.getShapes()[this.shahInput0];
			curShape.setCurrentShape("square");
			curShape.setShapeScale(6);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			curShape	= this.shapeEditor.getShapes()[this.shahInput1];
			curShape.setCurrentShape("square");
			curShape.setShapeScale(4);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			// Set the color for all inputs:
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);

			this.menu.getControlP5().getController("trichrom").update();
			this.menu.setColor(0, new int[] { 255, 0, 0 }, 0, true);
			this.menu.setColor(4, new int[] { 255, 255, 0 }, 0, true);
			this.menu.setColor(8, new int[] { 143, 130, 5 }, 0, true);
			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setAttRelTranVal(0, this.shahInput0, 800);
			this.menu.setAttRelTranVal(1, this.shahInput0, 1000);

			this.menu.setCurKey("F", 2);
		} // SCENE_SHAH_HAL
		
		else if(this.curScene == Karaoke_Demo.SCENE_SHAH_GIFTS)
		{
			this.curNumInputs	= 1;
			//this.setSquareValues();
			this.menu.setCurrentInput(0);

			Shape	curShape	= this.shapeEditor.getShapes()[4];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(5);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			curShape	= this.shapeEditor.getShapes()[this.shahInput0];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(3);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			this.menu.setCurKey("C", 2);

			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setColor(0, new int[] { 204, 102, 0 }, 0, true);
			this.menu.setColor(4, new int[] { 0, 153, 0 }, 0, true);
			this.menu.setColor(8, new int[] { 255, 255, 153 }, 0, true);

			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setCanvasColor(new int[] { 242, 216, 115 });

			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);
		} // SCENE_SHAH_GIFTS


		///////////////////// Taylor & Betsie://///////////////////
		else if(this.curScene == Karaoke_Demo.SCENE_STRAIGHTEN)
		{
			this.curNumInputs	= 1;
			//this.setSquareValues();

			Shape	curShape	= this.shapeEditor.getShapes()[this.taylorInput];
			curShape.setCurrentShape("square");
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);
			curShape.setShapeScale(4);

			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setColor(0, new int[] { 204, 0, 153 }, true);
			this.menu.setColor(4, new int[] { 153, 255, 51 }, true);
			this.menu.setColor(8, new int[] { 255, 153, 102 }, true);

			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setCurKey("Ab", 2);

			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);

		} // SCENE_STRAIGHTEN

		else if(this.curScene == Karaoke_Demo.SCENE_CAT)
		{
			this.curNumInputs	= 2;
			//			//this.setSquareValues();
			this.menu.setCurKey(this.menu.getCurKey(), 2);
			this.menu.showColorMenu();

			Shape	shape0	= this.shapeEditor.getShapes()[this.taylorInput];
			Shape	shape1	= this.shapeEditor.getShapes()[this.betsieInput];

			shape0.setCurrentShape("splat");
			shape1.setCurrentShape("circle");

			shape0.setShapeScale(3.5f);
			shape1.setShapeScale(1.5f);

			shape0.setXPos(this.width / 2 + 10);
			shape1.setXPos(this.width / 2 - 10);
			shape0.setYPos(this.height / 2);
			shape1.setYPos(this.height / 2);

			this.menu.setAlphaSlider(255);

			//			this.menu.setGlobal(false);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(false);
			this.menu.setCurrentInput(this.taylorInput);

			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();

			this.menu.setCurrentInput(this.betsieInput);

			// Green to Blue:
			this.menu.setColor(0, new int[] { 0, 155, 0 }, true);
			this.menu.setColor(11, new int[] { 0, 85, 125 }, true);
			this.menu.getControlP5().getController("dichrom").update();

			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);

			this.menu.setCurKey("A", 2);
			this.menu.setAttRelTranVal(0, this.betsieInput, 100);
			this.menu.setAttRelTranVal(1, this.betsieInput, 100);
			this.menu.setAttRelTranVal(2, this.betsieInput, 100);
		} // SCENE_CAT

		else if(this.curScene == Karaoke_Demo.SCENE_RIVER)
		{
			this.curNumInputs	= 1;
			//			this.setSquareValues();

			Shape	curShape	= this.shapeEditor.getShapes()[this.taylorInput];
			curShape.setCurrentShape("circle");
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);
			curShape.setShapeScale(4);

			this.menu.setColor(0, new int[] { 0, 85, 200 }, true);
			this.menu.setColor(11, new int[] { 0, 155, 0 }, true);

			this.menu.getControlP5().getController("dichrom").update();

			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);

			this.menu.setCurKey("A", 2);

		} // SCENE_RIVER

		else if(this.curScene == Karaoke_Demo.SCENE_PRAYER)
		{
			this.menu.setAttRelTranVal(0, this.betsieInput, 400);
			this.menu.setAttRelTranVal(1, this.betsieInput, 400);
			this.menu.setAttRelTranVal(2, this.betsieInput, 400);
			
			this.curNumInputs	= 2;
			this.setSquareValues();
			this.menu.setCurKey(this.menu.getCurKey(), 2);

			this.menu.setDynamicBars(true);

			this.menu.setAlphaSlider(255);

			//			this.menu.setGlobal(false);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);

			this.menu.getControlP5().getController("trichrom").update();
			this.menu.setColor(0, new int[] { 255, 102, 153 }, true);
			this.menu.setColor(4, new int[] { 153, 204, 255 }, true);
			this.menu.setColor(8, new int[] { 255, 255, 153 }, true);
			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setCurKey("A", 2);
		} // SCENE_PRAYER


		//////////////////////////// Cadenza: ////////////////////////////
		else if(this.curScene == Karaoke_Demo.SCENE_KILLING)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.hideShapeMenu();

			this.menu.setAlphaSlider(255);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 0, 0 }, i, true);
				this.menu.setColor(4, new int[] { 243, 130, 5 }, i, true);
				this.menu.setColor(8, new int[] { 244, 226, 31 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // SCENE_KILLING

		else if (this.curScene == Karaoke_Demo.SCENE_KILLING_DYNAMIC)
		{
			this.curNumInputs	= 7;
			this.menu.setDynamicBars(true);
			this.setSquareValues();


			this.menu.showColorMenu();
			this.menu.hideShapeMenu();

			this.menu.setAlphaSlider(255);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 0, 0 }, i, true);
				this.menu.setColor(11, new int[] { 243, 130, 5 }, i, true);	// so that it works coming from dichromatic
				this.menu.setColor(4, new int[] { 243, 130, 5 }, i, true);
				this.menu.setColor(8, new int[] { 244, 226, 31 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);

		} // SCENE_KILLING_DYNAMIC

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_NO_BB || this.curScene == Karaoke_Demo.SCENE_WINGS_BB_CENTER)
		{
			this.curNumInputs	= 7;
			//			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(1);
				//curShape.setRotation( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) );

				curShape.setRotation(0.3f + (-0.6f * (i%2)));
				//curShape.setRotation( (PConstants.HALF_PI / 4) + (PConstants.HALF_PI * 4 * (i % 2)) );
				System.out.println(i + ": rotation = " + (PConstants.HALF_PI / 4));

				//System.out.println("i = " + i + "; rotation = " + ( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) ));

				curShape.setXPos((this.width / 7) * i);
				curShape.setYPos((this.height / 3) * ((i % 2) + 1)); // (i % 2) + 1 : want either 1 or 2
			} // for

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 155, 50 }, i, true);
				this.menu.setColor(11, new int[] { 150, 0, 150 }, i, true);			
			}

			this.menu.getControlP5().getController("dichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // WINGS_NO_BB || WINGS_BB_CENTER

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_BB_BOTTOM)
		{
			this.curNumInputs	= 7;
			//			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(1);
				//curShape.setRotation( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) );

				curShape.setRotation(0.3f + (-0.6f * (i%2)));
				//curShape.setRotation( (PConstants.HALF_PI / 4) + (PConstants.HALF_PI * 4 * (i % 2)) );
				System.out.println(i + ": rotation = " + (PConstants.HALF_PI / 4));

				//System.out.println("i = " + i + "; rotation = " + ( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) ));

				curShape.setXPos((this.width / 7) * i);
				curShape.setYPos((this.height / 3) * ((i % 2) + 1)); // (i % 2) + 1 : want either 1 or 2
			} // for

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 155, 50 }, i, true);
				this.menu.setColor(11, new int[] { 150, 0, 150 }, i, true);			
			}

			this.menu.getControlP5().getController("dichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // WINGS_BB_BOTTOM

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_LARGESHAPES)
		{
			this.curNumInputs	= 7;
			//			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(2);
				//curShape.setRotation( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) );

				curShape.setRotation(0.3f + (-0.6f * (i%2)));
				//curShape.setRotation( (PConstants.HALF_PI / 4) + (PConstants.HALF_PI * 4 * (i % 2)) );
				System.out.println(i + ": rotation = " + (PConstants.HALF_PI / 4));

				//System.out.println("i = " + i + "; rotation = " + ( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) ));

				curShape.setXPos((this.width / 7) * i);
				curShape.setYPos((this.height / 3) * ((i % 2) + 1)); // (i % 2) + 1 : want either 1 or 2
			} // for

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 155, 50 }, i, true);
				this.menu.setColor(11, new int[] { 150, 0, 150 }, i, true);			
			}

			this.menu.getControlP5().getController("dichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // WINGS_LARGESHAPES

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_RAINBOW)
		{
			this.curNumInputs	= 7;
			//			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(2);
				//curShape.setRotation( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) );

				curShape.setRotation(0.3f + (-0.6f * (i%2)));
				//curShape.setRotation( (PConstants.HALF_PI / 4) + (PConstants.HALF_PI * 4 * (i % 2)) );
				System.out.println(i + ": rotation = " + (PConstants.HALF_PI / 4));

				//System.out.println("i = " + i + "; rotation = " + ( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) ));

				curShape.setXPos((this.width / 7) * i);
				curShape.setYPos((this.height / 3) * ((i % 2) + 1)); // (i % 2) + 1 : want either 1 or 2
			} // for

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			this.menu.getControlP5().getController("rainbow").update();

			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // WINGS_RAINBOW

		else if(this.curScene == Karaoke_Demo.SCENE_WINGS_DYNAMIC)
		{
			this.curNumInputs	= 7;
			//			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(2);
				//curShape.setRotation( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) );

				curShape.setRotation(0.3f + (-0.6f * (i%2)));
				//curShape.setRotation( (PConstants.HALF_PI / 4) + (PConstants.HALF_PI * 4 * (i % 2)) );
				System.out.println(i + ": rotation = " + (PConstants.HALF_PI / 4));

				//System.out.println("i = " + i + "; rotation = " + ( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) ));

				curShape.setXPos((this.width / 7) * i);
				curShape.setYPos((this.height / 3) * ((i % 2) + 1)); // (i % 2) + 1 : want either 1 or 2
			} // for

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			this.menu.getControlP5().getController("rainbow").update();

			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // WINGS_DYNAMIC


		else if(this.curScene == Karaoke_Demo.SCENE_MAN)
		{
			this.curNumInputs	= 7;
			//			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 0; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("circle");
				curShape.setShapeScale(3);
				System.out.println("i = " + i + "; xPos = " + (this.width / 4) * ((i  % 3) + 1));

				// Three circle on the top row, four on the bottom:
				if(i < 3)
				{
					curShape.setXPos((this.width / 4) * (i + 1));	// i.e., width/4, (width/4) * 2, or (width/4) * 3
					curShape.setYPos(this.height / 3);
				} else {
					curShape.setXPos((this.width / 5) * (i - 2));	// i.e., width/5, (width/5) * 2, (width/5) * 3, or (width/5) * 4
					curShape.setYPos((this.height / 3) * 2);
				}			

			}

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 0, 10, 150 }, i, true);
				this.menu.setColor(11, new int[] { 210, 210, 210 }, i, true);			
			}

			this.menu.getControlP5().getController("dichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		}
		else if(this.curScene == Karaoke_Demo.SCENE_KALEIDESCOPE)
		{
			this.menu.showShapeMenu();
			this.menu.showColorMenu();

			this.curNumInputs	= 7;
			//this.setSquareValues();

			Shape	curShape;
			int		radius	= this.width / 4;	// width / 8 is best for shape."x"
			int[]	xVals	= new int[] {
					(this.width / 2),
					(this.width / 2) + (radius / 2),
					(this.width / 2) + radius,
					(this.width / 2) + (radius / 2),
					(this.width / 2) - (radius / 2),
					(this.width / 2) - radius,
					(this.width / 2) - (radius / 2)
			};
			int[]	yVals	= new int[] {
					(this.height / 2),
					(this.height / 2) - radius,
					(this.height / 2),
					(this.height / 2) + radius,
					(this.height / 2) + radius,
					(this.height / 2),
					(this.height / 2) - radius
			};

			for(int i = 0; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				//				curShape.setCurrentShape("x");
				//				curShape.setCurrentShape("pentagon");
				curShape.setCurrentShape("hexagon");
				//				curShape.setRotation(300);
				curShape.setShapeScale(2.5f);

				curShape.setXPos(xVals[i]);
				curShape.setYPos(yVals[i]);
			}

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 150, 0, 150 }, i, true);
				this.menu.setColor(4, new int[] { 255, 255, 0 }, i, true);
				this.menu.setColor(8, new int[] { 9, 187, 193 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // SCENE_KALEIDESCOPE

		else if(this.curScene == Karaoke_Demo.SCENE_KALEIDESCOPE_SPIN)
		{
			this.menu.showShapeMenu();
			this.menu.showColorMenu();

			this.curNumInputs	= 7;
			//this.setSquareValues();

			Shape	curShape;
			int		radius	= this.width / 4;	// width / 8 is best for shape."x"
			int[]	xVals	= new int[] {
					(this.width / 2),
					(this.width / 2) + (radius / 2),
					(this.width / 2) + radius,
					(this.width / 2) + (radius / 2),
					(this.width / 2) - (radius / 2),
					(this.width / 2) - radius,
					(this.width / 2) - (radius / 2)
			};
			int[]	yVals	= new int[] {
					(this.height / 2),
					(this.height / 2) - radius,
					(this.height / 2),
					(this.height / 2) + radius,
					(this.height / 2) + radius,
					(this.height / 2),
					(this.height / 2) - radius
			};

			for(int i = 0; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				//				curShape.setCurrentShape("x");
				//				curShape.setCurrentShape("pentagon");
				curShape.setCurrentShape("hexagon");
				//				curShape.setRotation(300);
				curShape.setShapeScale(2.51f);

				curShape.setXPos(xVals[i]);
				curShape.setYPos(yVals[i]);
			}

			this.menu.setAlphaSlider(150);

			//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

			this.menu.getControlP5().getController("trichrom").update();

			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 150, 0, 150 }, i, true);
				this.menu.setColor(4, new int[] { 255, 255, 0 }, i, true);
				this.menu.setColor(8, new int[] { 9, 187, 193 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		} // SCENE_KALEIDESCOPE_SPIN

	} // keyPressed

	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
