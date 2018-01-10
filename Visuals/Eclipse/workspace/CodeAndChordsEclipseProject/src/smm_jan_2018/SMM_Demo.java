package smm_jan_2018;

import controlP5.ControlP5;
import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class SMM_Demo extends Module {
	// TODO: multi-input guide tones for this? :D
	
	// These are the ASCII codes for 0-5:
	private	static final int	SCENE_CLAP			= 48;
	private	static final int	SCENE_RAINBOW_ROUND	= 49;
	private	static final int	SCENE_SOLOIST		= 50;
	private	static final int	SCENE_DRUM_VOCAL	= 51;
	private	static final int	SCENE_DUET			= 52;
	private	static final int	SCENE_QUARTET		= 53;
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
	} // setup
	
	public void draw()
	{
		this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		this.drawScene();
		this.menu.runMenu();
		
	} // draw
	
	private void drawScene()
	{
		switch(curScene)
		{
			case SMM_Demo.SCENE_CLAP :
				this.menu.fade(0, this.clapInput0);
				
				this.background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);
				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(clapShape0);
				}
				
				break;
			case SMM_Demo.SCENE_RAINBOW_ROUND :

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

				/*
				if(this.menu.isShowScale() && !this.menu.getShapeEditor().getIsRunning())
				{
					// draws the legend along the bottom of the screen:
					this.legend(scaleDegree, 0);
				} // if showScale
				*/
				break;
				
			case SMM_Demo.SCENE_SOLOIST :
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
				break;
				
			case SMM_Demo.SCENE_DRUM_VOCAL :
				this.menu.fade(0, this.drumVocalDrumInput);
				
				this.scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.drumVocalVocalInput)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				this.menu.fade(this.scaleDegree, this.drumVocalVocalInput);
				
				if(!this.menu.getShapeEditor().getIsRunning())
				{
					// TODO - drum shape so huge! Why is that?
					this.menu.getShapeEditor().drawShape(this.drumVocalDrumShape);
					this.menu.getShapeEditor().drawShape(this.drumVocalVocalShape);
				}
				
				this.legend(this.scaleDegree, this.drumVocalVocalInput);
				
				break;
			case SMM_Demo.SCENE_DUET :
				for(int i = 0; i < 2; i++)
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
						this.legend(this.scaleDegree, 0);
					}
				} // for
				
				break;
			case SMM_Demo.SCENE_QUARTET :				
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

				break;
		
		} // switch
	} // drawScene
	
	public void keyPressed()
	{
		int	key	= (int)this.key;
		
		if(key > 47 && key < 54)
		{
			this.curScene	= key;
			System.out.println("curScene = " + this.curScene);
		}
		
		if(this.curScene == SMM_Demo.SCENE_CLAP)
		{
			this.curNumInputs	= 1;
			this.menu.hideColorMenu();
//			this.menu.showColorMenu("OneColor");
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);			
		}
		else if(this.curScene == SMM_Demo.SCENE_RAINBOW_ROUND)
		{
			this.curNumInputs	= 3;
			
//			this.menu.hideColorMenu("OneColor");
			this.menu.showShapeMenu();
			this.menu.showColorMenu();
			this.menu.setColorStyle(0, 0);
	
			// First input goes furthest back:
			this.shapeEditor.getShapes()[this.rainbowRoundShape0].setShapeScale(3);
			this.shapeEditor.getShapes()[this.rainbowRoundShape1].setShapeScale(2);
			this.shapeEditor.getShapes()[this.rainbowRoundShape2].setShapeScale(1);
			
			for(int i = this.rainbowRoundShape0; i < 3; i++)
			{
				this.shapeEditor.getShapes()[i].setCurrentShape("circle");
			}
			
		}
		else if(this.curScene == SMM_Demo.SCENE_SOLOIST)
		{
			this.curNumInputs	= 1;
			this.menu.hideShapeMenu();
			this.menu.showColorMenu();
			
			// TODO - whatever colors we decide here...
		}
		else if(this.curScene == SMM_Demo.SCENE_DRUM_VOCAL)
		{
			this.curNumInputs	= 2;
			
			this.menu.showColorMenu();
		
			// Set the size of the drum shape (fullscreen?)
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setCurrentShape("square");
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setXStretch(4.5f);
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setYStretch(4);
			
			// Set the drum input color (white)
			this.menu.setGlobal(false);
			this.menu.setCurrentInput(this.drumVocalDrumInput);
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);
			
			// Set the size of the input shape (prob. relative to the drum shape)
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setCurrentShape("square");
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setXStretch(4);
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setYStretch(3.5f);
			
			// TODO - pick up here: got ArrayIndexOutOfBoundsException from Canvas Color...
			// (Button id = 213 - but exception was from ColorWheel)
			
			// Set the input colors
			this.menu.setCurrentInput(this.drumVocalVocalInput);
			
			// TODO - ummm.... Not the colors I entered! The white seems to be persisting. :/
			// Purple - rgb(150, 0, 150) - to Deep Purple - rgb(92, 16, 118) - to Light Blue - rgb(0, 163, 255)
			this.menu.setColor(0, new int[] { 150, 0, 150 }, true);
			this.menu.setColor(4, new int[] { 92, 16, 118 }, true);
			this.menu.setColor(8, new int[] { 0, 163, 255 }, true);
			this.menu.getControlP5().getController("trichrom").update();
//			this.menu.dichromatic_TwoRGB(new int[] { 255, 255, 0 }, new int[] { 255, 100, 255 } );
		} else if(this.curScene == SMM_Demo.SCENE_DUET)
		{
			this.curNumInputs	= 2;
			
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
			
			// Salmon - rgb(212, 68, 94) - to Light Orange - rgb(254, 183, 78)
			this.menu.setGlobal(true);
			this.menu.setCurKey(this.menu.getCurKey(), 2);	// set to Chromatic
			this.menu.setColor(0, new int[] { 179, 41, 65 }, true);
			this.menu.setColor(11, new int[] { 254, 183, 78 }, true);
			this.menu.getControlP5().getController("dichrom").update();
			
		} else if(this.curScene == SMM_Demo.SCENE_QUARTET)
		{
			this.curNumInputs	= 4;
			this.menu.showColorMenu();
			this.menu.setGlobal(true);
			this.menu.getControlP5().getController("rainbow").update();
			
			if(this.xVals == null)
			{
				this.setSquareValues();
			}
		}else {
			this.menu.showColorMenu();
		}
		
		if(key == 'f')
		{
			this.menu.saveColorState("hello");
		}
	} // keyPressed
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
