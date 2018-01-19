package smm_jan_2018;

import controlP5.ControlP5;
import controlP5.Toggle;
import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;
import processing.core.PConstants;

public class SMM_Demo extends Module {
	// TODO: multi-input guide tones for this? :D

	// Initial demos:
	private	static final int	SCENE_OPEN			= 57;	// 9
	private	static final int	SCENE_CLAP			= 48;	// 0
	private	static final int	SCENE_SOLOIST		= 49;	// 1
	private	static final int	SCENE_DUET			= 50;	// 2
	private	static final int	SCENE_RAINBOW_ROUND	= 51;	// 3
	private	static final int	SCENE_QUARTET		= 52;	// 4
//	private	static final int	SCENE_DRUM_VOCAL	= 112;	// p	
	private	static final int	SCENE_TRIO			= 56;	// 8

	// Shahzore:
	private	static final int	SCENE_SHAH_HAL			= 122;	// z
	private	static final int	SCENE_SHAH_GIFTS		= 120;	// x
	private	static final int	SCENE_SHAH_2			= 99;	// c
	private	static final int	SCENE_SHAH_3			= 118;	// v

	// Taylor and Betsie:
	private	static final int	SCENE_TAYLOR			= 97;	// a
	private	static final int	SCENE_BETSIE			= 115;	// s
	private	static final int	SCENE_TBDUET_SHAPES		= 100;	// d
	private	static final int	SCENE_TBDUET_RECTS		= 102;	// f
	private	static final int	SCENE_TBDUET_DYNBARS	= 103;	// g

	// Cadenza:
	private	static final int	SCENE_KALEIDESCOPE		= 113;	// q
	private	static final int	SCENE_KALEIDESCOPE_SPIN	= 119;	// w
	private	static final int	SCENE_MAN				= 101;	// e
	private	static final int	SCENE_KILLING			= 114;	// r
	private	static final int	SCENE_WINGS_NO_BB		= 116;	// t
	private	static final int	SCENE_WINGS_BB_CENTER	= 121;	// y
	private	static final int	SCENE_WINGS_BB_BOTTOM	= 117;	// u
	private	static final int	SCENE_WINGS_LARGESHAPES	= 105;	// i
	private	static final int	SCENE_WINGS_RAINBOW		= 111;	// o
	private	static final int	SCENE_WINGS_DYNAMIC		= 112;	// p

	private	int	curScene							= SMM_Demo.SCENE_OPEN;

	private	int	shahInput0	= 0;
	private	int	shahInput1	= 1; // 7
	
	private	int	betsieInput	= 0;
	private	int	taylorInput	= 0;
	
	private	int	clapInput0	= 0;
	private	int	clapShape0	= 0;
	private	int	clapInput1	= 1;
	private	int	clapShape1	= 1;

	private	int	rainbowRoundInput0	= 0;
	private	int	rainbowRoundShape0	= 0;
	private	int	rainbowRoundInput1	= 1;
	private	int	rainbowRoundShape1	= 1;
	private	int	rainbowRoundInput2	= 2;
	private	int	rainbowRoundShape2	= 2;

	private	int	soloist	= 0;

	private	int	drumVocalDrumInput	= 0;
	private	int	drumVocalDrumShape	= 0;
	private	int	drumVocalVocalInput	= 1;
	private	int	drumVocalVocalShape	= 1;

	private	int	duetShape0	= 0;
	private	int	duetInput0	= 0;
	private	int	duetShape1	= 1;
	private	int	duetInput1	= 1;

	private	int	trioShape0	= 0;
	private	int	trioShape1	= 1;
	private	int	trioShape2	= 2;

	private	int	quartet0	= 0;
	private	int	quartet1	= 1;
	private	int	quartet2	= 2;
	private	int	quartet3	= 3;

	private	ShapeEditor	shapeEditor;

	private int	scaleDegree	= 0;
	private int	curX;
	private int	curY;

	// These are for the dynamic bars:
	private float[]	barVel;
	private float[] barPos;

	private	int[]	ampHist;
	private	int[]	ampHist1;
	private	int		ampWidth;
	private	static final int NUM_AMPS	= 350;


	public static void main(String[] args) {
		PApplet.main("smm_jan_2018.SMM_Demo");
	}

	public void settings()
	{
		this.fullScreen();
	} // settings

	public void setup()
	{
		this.totalNumInputs	= 16;
		this.curNumInputs	= 1;
		this.input			= new RealTimeInput(this.totalNumInputs, true, this);
		//		this.input			= new RealTimeInput(1, new AudioContext(), this);

		this.menu	= new ModuleMenu(this, this, this.input, 12);
		this.menu.addSensitivityMenu(true);

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
		this.setSquareValues();
		
		Shape	curShape	= this.shapeEditor.getShapes()[this.betsieInput];
		curShape.setCurrentShape("circle");
		curShape.setShapeScale(2);
		
		this.menu.getControlP5().getController("trichrom").update();

		this.menu.setColor(0, new int[] { 255, 0, 0 }, true);
		this.menu.setColor(4, new int[] { 255, 255, 0 }, true);
		this.menu.setColor(8, new int[] { 0, 0, 255 }, true);
		
		this.menu.getControlP5().getController("trichrom").update();
		
//		this.menu.getOutsideButtonsCP5().getController("hamburger").hide();	
//		this.menu.getOutsideButtonsCP5().getController("play").hide();	
		
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

		this.ampHist	= new int[SMM_Demo.NUM_AMPS];
		this.ampHist1	= new int[SMM_Demo.NUM_AMPS];
//		this.ampWidth	= (this.width / 3 * 2) / SMM_Demo.NUM_AMPS;
		this.ampWidth	= (this.width) / SMM_Demo.NUM_AMPS;
		
		this.noStroke();
	} // setup

	public void draw()
	{
		this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		this.drawScene();
		this.menu.runMenu();

	} // draw

	private void drawScene()
	{
		if(this.curScene == SMM_Demo.SCENE_OPEN)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.betsieInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.betsieInput);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.betsieInput);
			}
		} // SCENE_OPEN
		
		else if(this.curScene == SMM_Demo.SCENE_CLAP)
		{
			this.menu.fade(0, this.clapInput0);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(clapShape0);
			}
		} // SCENE_CLAP
		

		if(this.curScene == SMM_Demo.SCENE_RAINBOW_ROUND)
		{

			// First:
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, rainbowRoundInput0);

			this.fill(this.menu.getCurHue()[rainbowRoundInput0][0], this.menu.getCurHue()[rainbowRoundInput0][1], this.menu.getCurHue()[rainbowRoundInput0][2]);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(rainbowRoundShape0);
			}

			// Second:
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, rainbowRoundInput1);

			this.fill(this.menu.getCurHue()[rainbowRoundInput1][0], this.menu.getCurHue()[rainbowRoundInput1][1], this.menu.getCurHue()[rainbowRoundInput1][2]);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(rainbowRoundShape1);
			}

			// Third:
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput2)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, rainbowRoundInput2);

			this.fill(this.menu.getCurHue()[rainbowRoundInput2][0], this.menu.getCurHue()[rainbowRoundInput2][1], this.menu.getCurHue()[rainbowRoundInput2][2]);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(rainbowRoundShape2);
			}


			if(this.menu.isShowScale() && !this.shapeEditor.getIsRunning())
			{
				// draws the legend along the bottom of the screen:
				this.legend(scaleDegree, 0);
			} // if showScale
		} // SCENE_RAINBOW_ROUND

		if(this.curScene == SMM_Demo.SCENE_SOLOIST || 
//				this.curScene == SMM_Demo.SCENE_SHAH_1 ||
				this.curScene == SMM_Demo.SCENE_SHAH_2 ||
				this.curScene == SMM_Demo.SCENE_SHAH_3)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.soloist)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(this.scaleDegree, this.soloist);
			this.fill(this.menu.getCurHue()[this.soloist][0], this.menu.getCurHue()[this.soloist][1], this.menu.getCurHue()[this.soloist][2]);

			curX	= (int)this.menu.mapCurrentXPos(0);
			curY	= (int)this.menu.mapCurrentYPos(0);
			this.rect(curX, curY, (int)this.menu.mapCurrentXPos(this.width), (int)this.menu.mapCurrentYPos(this.height));
			if(this.menu.isShowScale())
			{
				// draws the legend along the bottom of the screen:
				this.legend(scaleDegree, this.soloist);
			} // if showScale
		} // SCENE_SOLOIST et alia


		else if(this.curScene == SMM_Demo.SCENE_DUET)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

				this.menu.fade(this.scaleDegree, i);

				//					this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for
		} // SCENE_DUET
		
		else if(this.curScene == SMM_Demo.SCENE_TBDUET_SHAPES)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

				this.menu.fade(this.scaleDegree, i);

				//					this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for
			
			this.shapeEditor.getShapes()[0].setShapeScale(this.input.getAmplitude(0) / 10000);
//			this.shapeEditor.getShapes()[1].setShapeScale(this.input.getAmplitude(1) / 10000);
		} // SCENE_TBDUET_SHAPES

		if(this.curScene == SMM_Demo.SCENE_TRIO )
		{
			for(int i = 0; i < 3; i++)
			{
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

				this.menu.fade(this.scaleDegree, i);

				//					this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


				if(!this.shapeEditor.getIsRunning())
				{
					this.shapeEditor.drawShape(i);
				}

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for
		}

		if(this.curScene == SMM_Demo.SCENE_QUARTET || this.curScene == SMM_Demo.SCENE_TBDUET_DYNBARS || 
				this.curScene == SMM_Demo.SCENE_TBDUET_RECTS)
		{
			for(int i = 0; i < this.curNumInputs; i++)
			{					
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

				this.menu.fade(scaleDegree, i);

				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());

				int	curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);
				int	curY	= (int)this.menu.mapCurrentYPos(this.yVals[i]);


				if(this.menu.getDynamicBars())
				{
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


					//this.barPos[i] = Math.max(this.barPos[i] + this.barVel[i], 0);
					this.barPos[i] = this.barPos[i] + (amp - this.barPos[i])/10;


					//System.out.println("Input number " + i + " - Amplitude = " + this.amplitude[i]);
					//System.out.println("Input number " + i + " - amp = " + this.barPos[i]);

					this.rect(curX,this.menu.mapCurrentYPos((this.height/2)- this.barPos[i]*(this.height/2)), this.rectWidths[i], this.height*this.barPos[i]);
				}
				else
				{
					this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);
				}


				//this.stroke(255);
				//this.strokeWeight(5);
				//this.line(0, (this.height/2)- amp*(this.height/2), this.width, (this.height/2)- amp*(this.height/2));

				if(this.menu.isShowScale())
				{
					this.legend(scaleDegree, i);
				}
			} // for - curNumInputs
		}
		
		else if(this.curScene == SMM_Demo.SCENE_SHAH_HAL)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.shahInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.shahInput0);

			//				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.shahInput0);
			}

			if(this.menu.isShowScale())
			{
				this.legend(this.scaleDegree, this.shahInput0);
			}
			
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.shahInput1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.shahInput1);

			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(this.shahInput1);
			}

			if(this.menu.isShowScale())
			{
				this.legend(this.scaleDegree, this.shahInput1);
			}
		} // SCENE_SHAH_HAL
		
		else if(this.curScene == SMM_Demo.SCENE_SHAH_GIFTS)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.shahInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
			this.menu.fade(this.scaleDegree, this.shahInput0);

			this.fill(this.menu.getCurHue()[this.shahInput0][0], this.menu.getCurHue()[this.shahInput0][1], this.menu.getCurHue()[this.shahInput0][2]);
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

			if(this.menu.isShowScale())
			{
				this.legend(this.scaleDegree, this.shahInput0);
			}

		} // SCENE_SHAH_GIFTS

		else if(this.curScene == SMM_Demo.SCENE_TAYLOR || this.curScene == SMM_Demo.SCENE_BETSIE)
		{
			this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(this.scaleDegree, 0);

			//				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


			if(!this.shapeEditor.getIsRunning())
			{
				this.shapeEditor.drawShape(0);
			}

			if(this.menu.isShowScale())
			{
				this.legend(this.scaleDegree, 0);
			}
		}

		// Cadenza:
		else if(this.curScene == SMM_Demo.SCENE_WINGS_NO_BB)
		{
			System.out.println("curNumInputs = " + this.curNumInputs);
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

	/*		// Amplitude bars:
			this.fill(255);

			for(int i = 0; i < (this.ampHist.length - 1); i++)
			{
				ampHist[i]	= ampHist[i+1];

				this.rect((i * this.ampWidth), (this.height / 2), this.ampWidth, this.ampHist[i] / 3);
				this.rect((i * this.ampWidth), (this.height / 2), this.ampWidth, (this.ampHist[i] / 3) * (-1));

			}

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);
*/
		} // SCENE_WINGS_NO_BB

		else if(this.curScene == SMM_Demo.SCENE_WINGS_BB_CENTER)
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

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);

		} // SCENE_WINGS_BB_CENTER
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_BB_BOTTOM)
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

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);
		} // SCENE_WINGS_BB_BOTTOM
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_LARGESHAPES)
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

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);
		} // SCENE_WINGS_LARGESHAPES
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_RAINBOW)
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

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);
		} // SCENE_WINGS_RAINBOW
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_DYNAMIC)
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

			this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);
		} // SCENE_WINGS_DYNAMIC
		
		else if(this.curScene == SMM_Demo.SCENE_KILLING)
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


				if(this.menu.getDynamicBars())
				{
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


					//this.barPos[i] = Math.max(this.barPos[i] + this.barVel[i], 0);
					this.barPos[i] = this.barPos[i] + (amp - this.barPos[i])/10;


					//System.out.println("Input number " + i + " - Amplitude = " + this.amplitude[i]);
					//System.out.println("Input number " + i + " - amp = " + this.barPos[i]);

					this.rect(curX,this.menu.mapCurrentYPos((this.height/2)- this.barPos[i]*(this.height/2)), this.rectWidths[i], this.height*this.barPos[i]);
				}
				else
				{
					this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);
				}


				//this.stroke(255);
				//this.strokeWeight(5);
				//this.line(0, (this.height/2)- amp*(this.height/2), this.width, (this.height/2)- amp*(this.height/2));

				if(this.menu.isShowScale())
				{
					this.legend(scaleDegree, i);
				}
			} // for - curNumInputs
		} // SCENE_KILLING

		else if(this.curScene == SMM_Demo.SCENE_MAN)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for
		}

		if(this.curScene == SMM_Demo.SCENE_KALEIDESCOPE)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for
		} // SCENE_KALEIDESCOPE
		
		else if(this.curScene == SMM_Demo.SCENE_KALEIDESCOPE_SPIN)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for
		} // SCENE_KALEIDESCOPE_SPIN
	} // drawScene

	public void keyPressed()
	{
		int	key	= (int)this.key;

		System.out.println("key = " + key);

		if(key == 107)
		{
//			this.menu.setDynamicBars(!this.menu.getDynamicBars());
			this.menu.setDynamicBars(true);
		} else {
			this.curScene	= key;
			System.out.println("curScene = " + this.curScene);			
		}

		if(this.curScene == SMM_Demo.SCENE_OPEN)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			
			Shape	curShape	= this.shapeEditor.getShapes()[this.betsieInput];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(2);
			
			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setColor(0, new int[] { 255, 0, 0 }, true);
			this.menu.setColor(4, new int[] { 255, 255, 0 }, true);
			this.menu.setColor(8, new int[] { 0, 0, 255 }, true);
			
			this.menu.getControlP5().getController("trichrom").update();
			this.menu.getOutsideButtonsCP5().getController("hamburger").hide();	
			this.menu.getOutsideButtonsCP5().getController("play").hide();	
		} // SCENE_OPEN
		else if(this.curScene == SMM_Demo.SCENE_CLAP)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			
			Shape	curShape	= this.shapeEditor.getShapes()[this.clapShape0];
			curShape.setCurrentShape("square");
			curShape.setShapeScale(2);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);

			this.menu.hideColorMenu();
			//			this.menu.showColorMenu("OneColor");
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);			
		}

		else if(this.curScene == SMM_Demo.SCENE_RAINBOW_ROUND)
		{
			this.curNumInputs	= 3;
			this.setSquareValues();

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
			this.shapeEditor.getShapes()[this.rainbowRoundShape0].setShapeScale(3);
			this.shapeEditor.getShapes()[this.rainbowRoundShape1].setShapeScale(2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape2].setShapeScale(1);

			this.shapeEditor.getShapes()[this.rainbowRoundShape0].setCurrentShape("circle");
			this.shapeEditor.getShapes()[this.rainbowRoundShape1].setCurrentShape("circle");
			this.shapeEditor.getShapes()[this.rainbowRoundShape2].setCurrentShape("circle");

			this.shapeEditor.getShapes()[this.rainbowRoundShape0].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape0].setYPos(this.height / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape1].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape1].setYPos(this.height / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape2].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape2].setYPos(this.height / 2);
		}
		else if(this.curScene == SMM_Demo.SCENE_SOLOIST)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();

			this.menu.hideShapeMenu();
			this.menu.showColorMenu();

			this.menu.setCurKey("A", 2);
			this.menu.getControlP5().getController("rainbow").update();
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(false);
		}

		else if(this.curScene == SMM_Demo.SCENE_DUET)
		{
			this.curNumInputs	= 2;
			this.setSquareValues();

			this.menu.showColorMenu();

			Shape	shape0	= this.shapeEditor.getShapes()[this.duetShape0];
			Shape	shape1	= this.shapeEditor.getShapes()[this.duetShape1];

			shape0.setCurrentShape("circle");
			shape1.setCurrentShape("circle");

			shape0.setShapeScale(5);
			shape1.setShapeScale(5);

			shape0.setXPos(this.width / 3);
			shape1.setXPos((this.width / 3) * 2);

			this.menu.setAlphaSlider(150);

//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
			
			// Call trichrom once first so that the specialColorsPos are correct:
			this.menu.getControlP5().getController("trichrom").update();
			
			this.menu.setColor(0, new int[] { 150, 0, 150 }, true);
			this.menu.setColor(4, new int[] { 92, 16, 118 }, true);
			this.menu.setColor(8, new int[] { 0, 163, 255 }, true);

			this.menu.getControlP5().getController("trichrom").update();

			/*
			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();
			 */
		}

		else if(this.curScene == SMM_Demo.SCENE_TRIO)
		{
			this.curNumInputs	= 3;
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	shape0	= this.shapeEditor.getShapes()[this.trioShape0];
			Shape	shape1	= this.shapeEditor.getShapes()[this.trioShape1];
			Shape	shape2	= this.shapeEditor.getShapes()[this.trioShape2];

			shape0.setCurrentShape("circle");
			shape1.setCurrentShape("circle");
			shape2.setCurrentShape("circle");

			shape0.setShapeScale(4);
			shape1.setShapeScale(4);
			shape2.setShapeScale(4);

			shape0.setXPos(this.width / 4);
			shape1.setXPos((this.width / 4) * 2);
			shape2.setXPos((this.width / 4) * 3);

			this.menu.setAlphaSlider(150);

//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic

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

			/*
			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();
			 */
		}
		else if(this.curScene == SMM_Demo.SCENE_QUARTET)
		{
			this.curNumInputs	= 4;
			this.setSquareValues();

			this.menu.showColorMenu();
//			this.menu.setGlobal(true);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(true);
			this.menu.getControlP5().getController("rainbow").update();
		}

		// Shahzore:
		else if(this.curScene == SMM_Demo.SCENE_SHAH_HAL)
		{
			this.curNumInputs	= 2;
			this.setSquareValues();
			
			Shape	curShape	= this.shapeEditor.getShapes()[this.shahInput0];
			curShape.setCurrentShape("square");
			curShape.setShapeScale(7);
			curShape.setXPos(this.width / 2);
			curShape.setYPos(this.height / 2);
			
			curShape	= this.shapeEditor.getShapes()[this.shahInput1];
			curShape.setCurrentShape("square");
			curShape.setShapeScale(5);
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
			
//			this.menu.setCurrentInput(1);
			this.menu.setAttRelTranVal(0, this.shahInput0, 800);
			this.menu.setAttRelTranVal(1, this.shahInput0, 1000);
		}
		else if(this.curScene == SMM_Demo.SCENE_SHAH_GIFTS)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurrentInput(0);

			Shape	curShape	= this.shapeEditor.getShapes()[4];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(6);
			
			curShape	= this.shapeEditor.getShapes()[this.shahInput0];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(4);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			this.menu.getControlP5().getController("trichrom").update();
			
			this.menu.setColor(0, new int[] { 204, 102, 0 }, 0, true);
			this.menu.setColor(4, new int[] { 0, 153, 0 }, 0, true);
			this.menu.setColor(8, new int[] { 255, 255, 153 }, 0, true);

			this.menu.getControlP5().getController("trichrom").update();
			
			this.menu.setCanvasColor(new int[] { 242, 216, 115 });
		}
		else if(this.curScene == SMM_Demo.SCENE_SHAH_2)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurrentInput(0);

			Shape	curShape	= this.shapeEditor.getShapes()[0];
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(1);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			this.menu.getControlP5().getController("trichrom").update();
			
			this.menu.setColor(0, new int[] { 150, 50, 150 }, 0, true);
			this.menu.setColor(4, new int[] { 59, 174, 255 }, 0, true);
			this.menu.setColor(8, new int[] { 5, 0, 255 }, 0, true);
			
			this.menu.getControlP5().getController("trichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_SHAH_3)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurrentInput(0);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();
			

			Shape	curShape	= this.shapeEditor.getShapes()[0];
			
			curShape.setCurrentShape("circle");
			curShape.setShapeScale(1);

			this.menu.getControlP5().getController("trichrom").update();

			this.menu.setColor(0, new int[] { 255, 155, 50 }, 0, true);
			this.menu.setColor(4, new int[] { 84, 218, 255 }, 0, true);
			this.menu.setColor(8, new int[] { 5, 0, 255 }, 0, true);
			this.menu.getControlP5().getController("trichrom").update();
		}

		// Taylor & Betsie:
		else if(this.curScene == SMM_Demo.SCENE_TAYLOR)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();

			this.menu.setCurKey(this.menu.getCurKey(), 2);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_BETSIE)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurKey(this.menu.getCurKey(), 2);

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			// Green to Blue:
			this.menu.setColor(0, new int[] { 0, 155, 0 }, true);
			this.menu.setColor(11, new int[] { 0, 85, 125 }, true);
			this.menu.getControlP5().getController("dichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_TBDUET_SHAPES)
		{
			this.curNumInputs	= 2;
			this.setSquareValues();
			this.menu.setCurKey(this.menu.getCurKey(), 2);
			this.menu.showColorMenu();

			Shape	shape0	= this.shapeEditor.getShapes()[0];
			Shape	shape1	= this.shapeEditor.getShapes()[1];

			shape0.setCurrentShape("splat");
			shape1.setCurrentShape("circle");

			shape0.setShapeScale(3.5f);
			shape1.setShapeScale(1.5f);

/*
 * 			shape0.setXPos(this.width / 3);
			shape1.setXPos((this.width / 3) * 2);
*/
			shape0.setXPos(this.width / 2 + 10);
			shape1.setXPos(this.width / 2 - 10);
			shape0.setYPos(this.height / 2);
			shape1.setYPos(this.height / 2);

			this.menu.setAlphaSlider(255);

//			this.menu.setGlobal(false);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(false);
			this.menu.setCurrentInput(0);

			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();

			this.menu.setCurrentInput(1);

			// Green to Blue:
			this.menu.setColor(0, new int[] { 0, 155, 0 }, true);
			this.menu.setColor(11, new int[] { 0, 85, 125 }, true);
			this.menu.getControlP5().getController("dichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_TBDUET_RECTS)
		{
			this.curNumInputs	= 2;
			this.setSquareValues();
			this.menu.setCurKey(this.menu.getCurKey(), 2);

			this.menu.setDynamicBars(false);

			this.menu.setAlphaSlider(255);

//			this.menu.setGlobal(false);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(false);
			this.menu.setCurrentInput(0);

			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();

			this.menu.setCurrentInput(1);

			// Green to Blue:
			this.menu.setColor(0, new int[] { 0, 155, 0 }, true);
			this.menu.setColor(11, new int[] { 0, 85, 125 }, true);
			this.menu.getControlP5().getController("dichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_TBDUET_DYNBARS)
		{
			this.curNumInputs	= 2;
			this.setSquareValues();
			this.menu.setCurKey(this.menu.getCurKey(), 2);

			this.menu.setDynamicBars(true);

			this.menu.setAlphaSlider(255);

//			this.menu.setGlobal(false);
			((Toggle)this.menu.getControlP5().getController("global")).setValue(false);
			this.menu.setCurrentInput(0);

			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();

			this.menu.setCurrentInput(1);

			// Green to Blue:
			this.menu.setColor(0, new int[] { 0, 155, 0 }, true);
			this.menu.setColor(11, new int[] { 0, 85, 125 }, true);
			this.menu.getControlP5().getController("dichrom").update();
		}

		/*
		 * 
	private	static final int	SCENE_WINGS_NO_BB		= 116;	// t
	private	static final int	SCENE_WINGS_BB_CENTER	= 121;	// y
	private	static final int	SCENE_WINGS_BB_BOTTOM	= 117;	// u
	private	static final int	SCENE_WINGS_LARGESHAPES	= 105;	// i
	private	static final int	SCENE_WINGS_RAINBOW		= 111;	// o
	private	static final int	SCENE_WINGS_DYNAMIC		= 112;	// p
		 */
		// Cadenza:
		else if(this.curScene == SMM_Demo.SCENE_WINGS_NO_BB || this.curScene == SMM_Demo.SCENE_WINGS_BB_CENTER)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

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
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_BB_BOTTOM)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

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
		} // WINGS_BB_BOTTOM
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_LARGESHAPES)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(4);
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
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_RAINBOW)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(4);
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
		
		else if(this.curScene == SMM_Demo.SCENE_WINGS_DYNAMIC)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			Shape	curShape;

			for(int i = 1; i < this.curNumInputs; i++)
			{
				curShape	= this.shapeEditor.getShapes()[i];
				curShape.setCurrentShape("butterfly");
				curShape.setShapeScale(4);
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
		
		else if(this.curScene == SMM_Demo.SCENE_KILLING)
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
				this.menu.setColor(11, new int[] { 243, 130, 5 }, i, true);	// so that it works coming from dichromatic
				this.menu.setColor(4, new int[] { 243, 130, 5 }, i, true);
				this.menu.setColor(8, new int[] { 244, 226, 31 }, i, true);
			}

			this.menu.getControlP5().getController("trichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		}
		else if(this.curScene == SMM_Demo.SCENE_MAN)
		{
			this.curNumInputs	= 7;
			this.setSquareValues();

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
		else if(this.curScene == SMM_Demo.SCENE_KALEIDESCOPE)
		{
			this.menu.showShapeMenu();
			this.menu.showColorMenu();

			this.curNumInputs	= 7;
			this.setSquareValues();

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
				curShape.setShapeScale(4);

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
		
		else if(this.curScene == SMM_Demo.SCENE_KALEIDESCOPE_SPIN)
		{
			this.menu.showShapeMenu();
			this.menu.showColorMenu();

			this.curNumInputs	= 7;
			this.setSquareValues();

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
				curShape.setShapeScale(4);

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
