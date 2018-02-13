package edgeDemo;

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

public class EdgeDemo extends Module {

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

	private	int	curScene							= EdgeDemo.SCENE_KALEIDESCOPE;

	private	int	beatBoxInput	= 0;

	private	ShapeEditor	shapeEditor;

	private int	scaleDegree	= 0;

	// These are for the dynamic bars:
	private float[]	barVel;
	private float[] barPos;

	private	int[]	ampHist;
	private	int		ampWidth;
	private	static final int NUM_AMPS	= 350;


	public static void main(String[] args) {
		PApplet.main("edgeDemo.EdgeDemo");
	}

	public void settings()
	{
		this.fullScreen(2);
	} // settings

	public void setup()
	{
		this.totalNumInputs	= 16;
		this.curNumInputs	= 8;

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

		// Starts on Kaleidescope:
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

		this.ampHist	= new int[EdgeDemo.NUM_AMPS];
		//		this.ampWidth	= (this.width / 3 * 2) / EdgeDemo.NUM_AMPS;
		this.ampWidth	= (this.width) / EdgeDemo.NUM_AMPS;

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
		if(this.curScene == EdgeDemo.SCENE_KALEIDESCOPE)
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

		else if(this.curScene == EdgeDemo.SCENE_KALEIDESCOPE_SPIN)
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

		else if(this.curScene == EdgeDemo.SCENE_MAN)
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
		} // SCENE_MAN

		else if(this.curScene == EdgeDemo.SCENE_KILLING)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for - curNumInputs
		} // SCENE_KILLING

		else if(this.curScene == EdgeDemo.SCENE_KILLING_DYNAMIC)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for - curNumInputs
		} // SCENE_KILLING_DYNAMIC

		else if(this.curScene == EdgeDemo.SCENE_WINGS_NO_BB)
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
				
				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
				}
			} // for

		} // SCENE_WINGS_NO_BB

		else if(this.curScene == EdgeDemo.SCENE_WINGS_BB_CENTER)
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
				
				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_BB_BOTTOM)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_LARGESHAPES)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_RAINBOW)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_DYNAMIC)
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

				if(this.menu.isShowScale())
				{
					this.legend(this.scaleDegree, i);
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
		int	key	= (int)this.key;

		this.menu.getOutsideButtonsCP5().getController("hamburger").hide();
		this.menu.setCanvasColor(new int[] { 0, 0, 0 });

		if(key == 107)
		{
			//			this.menu.setDynamicBars(!this.menu.getDynamicBars());
			this.menu.setDynamicBars(true);
		} else {
			this.curScene	= key;
			System.out.println("curScene = " + this.curScene);			
		}

		if(this.curScene == EdgeDemo.SCENE_KALEIDESCOPE)
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

		else if(this.curScene == EdgeDemo.SCENE_KALEIDESCOPE_SPIN)
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
		
		else if(this.curScene == EdgeDemo.SCENE_KILLING)
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

		else if (this.curScene == EdgeDemo.SCENE_KILLING_DYNAMIC)
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_NO_BB || this.curScene == EdgeDemo.SCENE_WINGS_BB_CENTER)
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_BB_BOTTOM)
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_LARGESHAPES)
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_RAINBOW)
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

		else if(this.curScene == EdgeDemo.SCENE_WINGS_DYNAMIC)
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


		else if(this.curScene == EdgeDemo.SCENE_MAN)
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
		} // SCENE_MAN

	} // keyPressed

	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText


} // class
