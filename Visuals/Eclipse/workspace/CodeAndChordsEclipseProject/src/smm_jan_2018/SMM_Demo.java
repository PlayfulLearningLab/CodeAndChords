package smm_jan_2018;

import core.Module;
import core.ModuleMenu;
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
	private	int	drumVocalVocalInput	= 0;
	private	int	drumVocalVocalShape	= 1;
	
	private	int	duet0	= 0;
	private	int	duet1	= 1;
	
	private	int	quartet0	= 0;
	private	int	quartet1	= 1;
	private	int	quartet2	= 2;
	private	int	quartet3	= 3;
	
	private	ShapeEditor	shapeEditor;

	private int	scaleDegree	= 0;
	private int	curX;
	private int	curY;


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
//		this.input			= new RealTimeInput(this.totalNumInputs, true, this);
		this.input			= new RealTimeInput(1, new AudioContext(), this);

		this.menu	= new ModuleMenu(this, this, this.input, 12);
		this.menu.addSensitivityMenu(false);
		
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
	} // setup
	
	public void draw()
	{
		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

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
				this.menu.fade(1, this.drumVocalVocalInput);
				
				if(!this.menu.getShapeEditor().getIsRunning())
				{
					this.menu.getShapeEditor().drawShape(this.drumVocalDrumShape);
					this.menu.getShapeEditor().drawShape(this.drumVocalVocalShape);
				}
				
				break;
			case SMM_Demo.SCENE_DUET :
				System.out.println("SCENE_DUET");
				break;
			case SMM_Demo.SCENE_QUARTET :
				System.out.println("SCENE_QUARTET");
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
			this.menu.hideColorMenu();
//			this.menu.showColorMenu("OneColor");
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);			
		}
		else if(this.curScene == SMM_Demo.SCENE_RAINBOW_ROUND)
		{
//			this.menu.hideColorMenu("OneColor");
			this.menu.showShapeMenu();
			this.menu.showColorMenu();
			this.menu.setColorStyle(0, 0);
	
			// TODO: give it three Inputs when we have that capacity:
//			this.curNumInputs	= 3;

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
			this.menu.hideShapeMenu();
			this.menu.showColorMenu();
			
			this.curNumInputs	= 1;
			
			// TODO - whatever colors we decide here...
		}
		else if(this.curScene == SMM_Demo.SCENE_DRUM_VOCAL)
		{
			// Set the size of the drum shape (fullscreen?)
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setCurrentShape("square");
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setXStretch(this.width / 2);
			this.shapeEditor.getShapes()[this.drumVocalDrumShape].setYStretch(this.height / 2);
			
			// Set the drum input color (white)
			this.menu.setGlobal(false);
			this.menu.setCurrentInput(this.drumVocalDrumInput);
			this.menu.setColor(0, new int[] { 255,  255, 255 }, true);
			
			// Set the size of the input shape (prob. relative to the drum shape)
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setCurrentShape("square");
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setXStretch(5);
			this.shapeEditor.getShapes()[this.drumVocalVocalShape].setYStretch(5);
			
			// TODO - pick up here: got ArrayIndexOutOfBoundsException from Canvas Color...
			// (Button id = 213 - but exception was from ColorWheel)
			
			// Set the input colors
			this.menu.setCurrentInput(this.drumVocalDrumInput);
			this.menu.setColor(1, new int[] { 0, 0, 0 }, true);
			
		}else {
			this.menu.showColorMenu();
		}
	}
	
	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
