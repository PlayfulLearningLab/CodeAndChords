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
	private	static final int	SCENE_CLAP			= 48;	// 0
	private	static final int	SCENE_SOLOIST		= 49;	// 1
	private	static final int	SCENE_DUET			= 50;	// 2
	private	static final int	SCENE_RAINBOW_ROUND	= 51;	// 3
	private	static final int	SCENE_QUARTET		= 52;	// 4
	private	static final int	SCENE_DRUM_VOCAL	= 57;	// 9	
	private	static final int	SCENE_TRIO			= 56;	// 8
	
	// Shahzore:
	private	static final int	SCENE_SHAH_0			= 122;	// z
	private	static final int	SCENE_SHAH_1			= 120;	// x
	private	static final int	SCENE_SHAH_2			= 99;	// c
	private	static final int	SCENE_SHAH_3			= 118;	// v
	
	
	// Taylor and Betsie:
	private	static final int	SCENE_TAYLOR			= 116;	// t
	private	static final int	SCENE_BETSIE			= 98;	// b
	private	static final int	SCENE_TBDUET_SHAPES		= 100;	// d
	private	static final int	SCENE_TBDUET_RECTS		= 102;	// f
	private	static final int	SCENE_TBDUET_DYNBARS	= 103;	// g
	
	// Cadenza:
	private	static final int	SCENE_WINGS			= 113;	// q
	private	static final int	SCENE_KILLING		= 119;	// w
	private	static final int	SCENE_MAN			= 101;	// e
	private	static final int	SCENE_KALEIDESCOPE	= 114;	// r
	
	private	int	curScene							= SMM_Demo.SCENE_CLAP;
	
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
	private	int		ampWidth;
	private	static final int NUM_AMPS	= 200;


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

//		this.menu.addColorMenu(new String[] { "white" }, 1, null, false, null, 0, 0, "OneColor");
		

		// Have to add this last, else it overwrites Controllers and causes an infinite loop:
		this.menu.addShapeMenu(8);
		this.shapeEditor	= this.menu.getShapeEditor();	// for convenience/speed

		// Starts on clapping scene:
		this.menu.setColor(0, new int[] { 255,  255, 255 }, true);
		this.shapeEditor.getShape().setCurrentShape("square");
		this.shapeEditor.getShape().setShapeScale(3);
		
		// I want to define all values, even on the first calls to di- and trichrom:
		this.menu.setDichromFlag(true);
		this.menu.setTrichromFlag(true);
		
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
//		this.ampWidth	= (this.width / 2) / SMM_Demo.NUM_AMPS;
		this.ampWidth	= (this.width ) / SMM_Demo.NUM_AMPS;
	} // setup
	
	public void draw()
	{
		this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		this.drawScene();
		this.menu.runMenu();
		
	} // draw
	
	private void drawScene()
	{
		if(this.curScene == SMM_Demo.SCENE_CLAP)
		{
				this.menu.fade(0, this.clapInput0);
				
				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(clapShape0);
				}
			}
			if(this.curScene == SMM_Demo.SCENE_RAINBOW_ROUND)
			{

				// First:
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				this.menu.fade(this.scaleDegree, rainbowRoundInput0);

				this.fill(this.menu.getCurHue()[rainbowRoundInput0][0], this.menu.getCurHue()[rainbowRoundInput0][1], this.menu.getCurHue()[rainbowRoundInput0][2]);

				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(rainbowRoundShape0);
				}
				
				// Second:
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				this.menu.fade(this.scaleDegree, rainbowRoundInput1);

				this.fill(this.menu.getCurHue()[rainbowRoundInput1][0], this.menu.getCurHue()[rainbowRoundInput1][1], this.menu.getCurHue()[rainbowRoundInput1][2]);

				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(rainbowRoundShape1);
				}
				
				// Third:
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(rainbowRoundInput2)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				this.menu.fade(this.scaleDegree, rainbowRoundInput2);

				this.fill(this.menu.getCurHue()[rainbowRoundInput2][0], this.menu.getCurHue()[rainbowRoundInput2][1], this.menu.getCurHue()[rainbowRoundInput2][2]);

				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(rainbowRoundShape2);
				}

				
				if(this.menu.isShowScale() && !this.menu.getShapeEditor().getIsRunning())
				{
					// draws the legend along the bottom of the screen:
					this.legend(scaleDegree, 0);
				} // if showScale
			}
				
			if(this.curScene == SMM_Demo.SCENE_SOLOIST ||
					this.curScene == SMM_Demo.SCENE_SHAH_0 || 
					this.curScene == SMM_Demo.SCENE_SHAH_1 ||
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
			}
				
			if(this.curScene == SMM_Demo.SCENE_DRUM_VOCAL)
			{
				this.menu.fade(0, this.drumVocalDrumInput);
				
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.drumVocalVocalInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				this.menu.fade(this.scaleDegree, this.drumVocalVocalInput);
				
		//		System.out.println("curHue = rgb(" );
				
				if(!this.menu.getShapeEditor().getIsRunning())
				{
					// TODO - drum shape so huge! Why is that?
					this.menu.getShapeEditor().drawShape(this.drumVocalDrumInput);
				}
				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(this.drumVocalVocalInput);
				}
				
				this.legend(this.scaleDegree, this.drumVocalVocalInput);
			}
				
			else if(this.curScene == SMM_Demo.SCENE_DUET || this.curScene == SMM_Demo.SCENE_TBDUET_SHAPES)
			{
				for(int i = 0; i < this.curNumInputs; i++)
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

					this.menu.fade(this.scaleDegree, i);

//					this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


					if(!this.menu.getShapeEditor().getIsRunning())
					{
						this.menu.getShapeEditor().drawShape(i);
					}
					
					if(this.menu.isShowScale())
					{
						this.legend(this.scaleDegree, i);
					}
				} // for
			}
				
				
			if(this.curScene == SMM_Demo.SCENE_TRIO )
			{
				for(int i = 0; i < 3; i++)
				{
					this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

					this.menu.fade(this.scaleDegree, i);

//					this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


					if(!this.menu.getShapeEditor().getIsRunning())
					{
						this.menu.getShapeEditor().drawShape(i);
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
			
			else if(this.curScene == SMM_Demo.SCENE_TAYLOR || this.curScene == SMM_Demo.SCENE_BETSIE)
			{
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(0)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

				this.menu.fade(this.scaleDegree, 0);

//				this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(0);
				}
				
				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, 0);
				}
			}
			
			// Cadenza:
			else if(this.curScene == SMM_Demo.SCENE_WINGS)
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
					
					// TODO - fade some shapes behind them (makes a border) that are white and depend on their amplitude?
					this.menu.fade(this.scaleDegree, i);
					
					if(!this.menu.getShapeEditor().getIsRunning())
					{
						this.menu.getShapeEditor().drawShape(i);
					}
				} // for
				
				// Amplitude bars:
				this.fill(255);
				
				for(int i = 0; i < (this.ampHist.length - 1); i++)
				{
					ampHist[i]	= ampHist[i+1];
					
					this.rect((i * this.ampWidth), (this.height / 2), this.ampWidth, this.ampHist[i] / 3);
					this.rect((i * this.ampWidth), (this.height / 2), this.ampWidth, (this.ampHist[i] / 3) * (-1));
				
				}
				
				this.ampHist[this.ampHist.length - 1]	= (int)this.input.getAmplitude(0);
			
			} // SCENE_WINGS
			
			else if(this.curScene == SMM_Demo.SCENE_KILLING)
			{
				
			}
				
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

					if(!this.menu.getShapeEditor().getIsRunning())
					{
						this.menu.getShapeEditor().drawShape(i);
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

					if(!this.menu.getShapeEditor().getIsRunning())
					{
						this.menu.getShapeEditor().drawShape(i);
					}
					
					if(this.menu.isShowScale())
					{
						this.legend(this.scaleDegree, i);
					}
				} // for
			}
	} // drawScene
	
	public void keyPressed()
	{
		int	key	= (int)this.key;
		
		System.out.println("key = " + key);
		
//		if(key > 47 && key < 58)
//		{
			this.curScene	= key;
			System.out.println("curScene = " + this.curScene);
//		}
		
		if(this.curScene == SMM_Demo.SCENE_CLAP)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			
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
			
			this.menu.setGlobal(true);
			// TODO - third input has white instead of red...
			
			// Set "rainbow" colors:
			int[][]	singARainbowColors	= new int[][] {
				new int[] { 255, 0, 0 }, // RED
				new int[] { 0, 0, 255 }, // BLUE
				new int[] { 255, 255, 0 }, // YELLOW
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
			
			// TODO - whatever colors we decide here...
			this.menu.getControlP5().getController("rainbow").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_DRUM_VOCAL)
		{
			this.curNumInputs	= 2;
			this.setSquareValues();
			
			this.menu.showColorMenu();
			this.menu.setAlphaSlider(255);
		
			// Set the size of the drum shape (fullscreen?)
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setCurrentShape("square");
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setYPos(this.height / 2);
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setShapeScale(1);
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setXStretch(4.5f);
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setYStretch(4);
			
			// Set the drum input color (white)
			this.menu.setGlobal(false);
			this.menu.setCurrentInput(this.drumVocalDrumInput);
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);
			
			// Set the size of the input shape (prob. relative to the drum shape)
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setCurrentShape("square");
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setXPos(this.width / 2);
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setYPos(this.height / 2);
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setShapeScale(1);
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setXStretch(4);
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setYStretch(3.5f);
			
			// Set the input colors
			this.menu.setCurrentInput(this.drumVocalVocalInput);
			
			this.menu.setColor(0, new int[] { 150, 0, 150 }, true);
			this.menu.setColor(11, new int[] { 255, 255, 0 }, true);
			this.menu.getControlP5().getController("dichrom").update();
			
			((Toggle)this.menu.getControlP5().getController("legend")).setValue(true);
			
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
			
			this.menu.setGlobal(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
			
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
			
			this.menu.setGlobal(true);
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
			this.menu.setGlobal(true);
			this.menu.getControlP5().getController("rainbow").update();
		}
		
		// Shahzore:
		else if(this.curScene == SMM_Demo.SCENE_SHAH_0)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurrentInput(0);
			
			// TODO - add shapes just to be on the safe side;
			// then give them their own draw scenes
			
			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			this.menu.setColor(0, new int[] { 100, 0, 100 }, 0, true);
			this.menu.setColor(11, new int[] { 0, 0, 150 }, 0, true);
			this.menu.getControlP5().getController("dichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_SHAH_1)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurrentInput(0);
			
			this.menu.showColorMenu();
			this.menu.showShapeMenu();

			this.menu.setColor(0, new int[] { 255, 0, 0 }, 0, true);
			this.menu.setColor(11, new int[] { 255, 255, 0 }, 0, true);
			this.menu.getControlP5().getController("dichrom").update();
		}
		else if(this.curScene == SMM_Demo.SCENE_SHAH_2)
		{
			this.curNumInputs	= 1;
			this.setSquareValues();
			this.menu.setCurrentInput(0);
			
			this.menu.showColorMenu();
			this.menu.showShapeMenu();

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

			Shape	shape0	= this.shapeEditor.getShapes()[0];
			Shape	shape1	= this.shapeEditor.getShapes()[1];
			
			shape0.setCurrentShape("square");
			shape1.setCurrentShape("square");
			
			shape0.setShapeScale(5);
			shape1.setShapeScale(5);
			
			shape0.setXPos(this.width / 3);
			shape1.setXPos((this.width / 3) * 2);
			
			this.menu.setAlphaSlider(150);
			
			this.menu.setGlobal(false);
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
			
			this.menu.setGlobal(false);
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
			
			this.menu.setGlobal(false);
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
		
		// Cadenza:
		else if(this.curScene == SMM_Demo.SCENE_WINGS)
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
				curShape.setShapeScale(1.5f);
				//curShape.setRotation( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) );

				curShape.setRotation(0.3f + (-0.6f * (i%2)));
				//curShape.setRotation( (PConstants.HALF_PI / 4) + (PConstants.HALF_PI * 4 * (i % 2)) );
				System.out.println(i + ": rotation = " + (PConstants.HALF_PI / 4));

				//System.out.println("i = " + i + "; rotation = " + ( ( (PConstants.PI) / 4 ) + ( (PConstants.PI / 2) * (i % 2) ) ));
				
				curShape.setXPos((this.width / 7) * i);
				curShape.setYPos((this.height / 3) * ((i % 2) + 1)); // (i % 2) + 1 : want either 1 or 2
			} // for
			
			this.menu.setAlphaSlider(150);
			
			this.menu.setGlobal(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
			
			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 255, 155, 50 }, i, true);
				this.menu.setColor(11, new int[] { 150, 0, 150 }, i, true);			
			}
			
			this.menu.getControlP5().getController("dichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(true);
		}
		else if(this.curScene == SMM_Demo.SCENE_KILLING)
		{
			
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
			
			this.menu.setGlobal(true);
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
				curShape.setShapeScale(3);
				
				curShape.setXPos(xVals[i]);
				curShape.setYPos(yVals[i]);
			}
			
			this.menu.setAlphaSlider(150);
			
			this.menu.setGlobal(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
			
			for(int i = 0; i < this.curNumInputs; i++)
			{
				this.menu.setColor(0, new int[] { 150, 0, 150 }, i, true);
				this.menu.setColor(4, new int[] { 255, 255, 0 }, i, true);
				this.menu.setColor(8, new int[] { 9, 187, 193 }, i, true);
			}
			
			this.menu.getControlP5().getController("trichrom").update();
			((Toggle)(this.menu.getControlP5().getController("legend"))).setValue(false);
		}

	} // keyPressed
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
