package module_01_03;

import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class Module_01_03_PitchHue_MultipleShapes extends Module {


	public static void main(String[] args)
	{
		PApplet.main("module_01_03.Module_01_03_PitchHue_MultipleShapes");
	} // main

	float[][] superShapes = new float[][] {
		new float[] { 1, 1, 0, 0, 1, 1, 1 },
		new float[] { 1, 1, 5, 5, 1, 1, 1 },
		new float[] { 2, 2, 3, 3, 1, 1, 1 },
		new float[] { .7f, .7f, 8, 8, 1, 1, 1},
		new float[] { 1.4f, 1.4f, 4, 4, .3f, .5f, .7f }
	};



	public void setup() 
	{
		this.input	= new RealTimeInput(2, new AudioContext(), this);
//		this.input	= new RealTimeInput(16, true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 2;

		this.menu	= new ModuleMenu(this, this, this.input, 12);

		this.setSquareValues();

		int[]	textYVals  		= new int[18];
		int[]	modulateYVals	= new int[3];
		int[]	modulateHSBVals	= new int[3];
		int[]	controllerXVals	= new int[3];
		//		int					colorSelectY;

		// calculate y's
		// set y vals for first set of scrollbar labels:
		textYVals[0]	=	26;
		// Given our height = 250 and "hide" (textYVals[0]) starts at [40] - now 26 (1/17),
		// We want a difference of 27.  This gets that:
		int	yValDif = (int)((this.height - textYVals[0]) / 18);//(textYVals.length + noteYVals.length + modulateYVals.length));
		// ... but no smaller than 25:
		if(yValDif < 25) {
			yValDif	= 25;
		}

		yValDif = 26;

		for(int i = 1; i < textYVals.length; i++)
		{
			textYVals[i]	= textYVals[i - 1] + yValDif;
		} // for

		// Add extra space before "Pitch Color Codes":
		textYVals[textYVals.length - 3]	= textYVals[textYVals.length - 4] + (int)(yValDif * 1.5);
		textYVals[textYVals.length - 2]	= textYVals[textYVals.length - 3] + (int)(yValDif * 1);
		textYVals[textYVals.length - 1]	= textYVals[textYVals.length - 2] + (int)(yValDif * 1);

		controllerXVals	= new int[] {	
				0, 
				(this.width / 3) - 20, 
				((this.width / 3) * 2) - 40	
		};

		// call add methods:

		this.menu.addHideButtons(controllerXVals[0], textYVals[1]);

		//		this.menu.addSliders(textYVals[1], textYVals[2], textYVals[3], textYVals[4]);
		this.menu.addPianoThresholdSlider(controllerXVals[0], textYVals[2]);

		// Adding inputNumSelect first so that inputSelect can be in front:
		this.menu.addInputNumSelect(controllerXVals[0], textYVals[5]);
		this.menu.addInputSelect(controllerXVals[0], textYVals[4]);

		this.menu.addARTSliders(controllerXVals[1], textYVals[1], textYVals[2], textYVals[3]);

		this.menu.addGuideTonePopout(controllerXVals[2], textYVals[2]);

		this.menu.addKeySelector(controllerXVals[2], textYVals[2]);
		this.menu.setCurKey("A", 2);

		modulateHSBVals[0] = textYVals[6];
		modulateHSBVals[1] = textYVals[7];
		modulateHSBVals[2] = textYVals[8];

		modulateYVals[0]	= textYVals[10];
		modulateYVals[1]	= textYVals[11];
		modulateYVals[2]	= textYVals[12];

		// Adding ColorSelect first since everything to do with colors depends on that:
		String[] noteNames = new String[] {
				"A", "A#/Bb", "B", "C", "C#/Db", "D", "D#/Db", "E", "F", "F#/Gb", "G", "G#/Ab"
		}; // noteNames

		this.menu.addColorSelect(controllerXVals[0], new int[] { textYVals[15], textYVals[16], textYVals[17] }, noteNames, "Custom Pitch\nColor Select", false);


		// ColorSelect and ColorStyle added out of order so that the 2nd Color
		// and 3rd Color select buttons will exist for the Rainbow ColorStyle
		// to lock them.
		//		this.addColorSelectButtons(textYVals[14]);
		String[] buttonLabels	= new String[] {
				"Canvas", "Tonic", "2nd Color", "3rd Color"
		}; // buttonLabels
		this.menu.addSpecialColors(controllerXVals[0], textYVals[14], buttonLabels, "Color Select", true);

		// addColorStyleButtons will set the colorStyle to rainbow() first:
		this.menu.addColorStyleButtons(controllerXVals[2], textYVals[3]);

		this.menu.addHSBSliders(controllerXVals[0], modulateHSBVals);

		this.menu.addModulateSliders(controllerXVals[0], modulateYVals);
		
		this.menu.addAlphaSlider(controllerXVals[2], textYVals[1]);

		//		this.menu.setColorStyle(ModuleTemplate01.CS_RAINBOW);

		this.menu.getControlP5().getController("keyDropdown").bringToFront();

		this.menu.setMenuList(new String[] {"Canvas", "Module Menu", "Shape Editor"});

		
		this.shapes = new Shape[16];

		for(int i = 0; i < this.shapes.length; i++)
		{
			this.shapes[i] = new Shape(this);

			for(int j = 0; j < 5; j++)
			{
				this.shapes[i].setShapeIndex(j);
				this.shapes[i].setCurrentShape("supershape", superShapes[j]);
			} // for - j
		} // for - i

		this.shapeEditor = new ShapeEditor(this, this.shapes, this, 925, 520);
		this.shapeEditor.setIsRunning(false);
		this.shapeEditor.getControlP5().getController("shapeSelect").setVisible(false);
		this.shapeEditor.updateSliders();

	} // setup()


	public void draw()
	{
//		System.out.println("input.getAdjustedFund() = " + input.getAdjustedFund());

		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		int	scaleDegree;

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed

		//System.out.println("****** CurNumInputs = " + this.curNumInputs + "  *******");

		for(int i = 0; i < this.curNumInputs; i++)
		{
			//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i) + ") = " + input.getAdjustedFundAsMidiNote(i) + 
			//					"; input.getAmplitude(" + (i) + ") = " + input.getAmplitude(1));

			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(scaleDegree, i);

//			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());


			if(!this.shapeEditor.getIsRunning())
			{
				this.drawShape(i);
			}
			

			if(this.menu.isShowScale() && !this.shapeEditor.getIsRunning())
			{
				// draws the legend along the bottom of the screen:
				this.okGoLegend(scaleDegree, i);

			} // if showScale

		} // for

		//this.shape.setShapeScale(this.menu.getShapeSize());

		if(this.currentMenu != this.menu.getCurrentMenu())
		{
			this.currentMenu = this.menu.getCurrentMenu();
			
			if(this.currentMenu == 0)
			{
				this.menu.setIsRunning(false);
				this.shapeEditor.setIsRunning(false);
			}
			else if(this.currentMenu == 1)
			{
				this.shapeEditor.setIsRunning(false);
				this.menu.setIsRunning(true);
			}
			else if(this.currentMenu == 2)
			{
				this.menu.setIsRunning(false);
				this.shapeEditor.setIsRunning(true);
			}
		}


		this.shapeEditor.runMenu();
		this.menu.runMenu();

		// TODO - trying to find the trichromatic major/minor customPitchColor bug:
		/*	if(this.menu.getCurColorStyle() == ModuleTemplate01.CS_TRICHROM)
				{
					for(int i = 0; i < menu.trichromColors.length; i++)
					{
						this.fill(menu.trichromColors[i][0], menu.trichromColors[i][1], menu.trichromColors[i][2]);
						this.ellipse(this.width / 2, i * 30 + 60, 30, 30);
					}
				} // if		
		 */

	} // draw()


	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText



}
