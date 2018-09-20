package module_01_03;

import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

/**
	Same basic idea as Module_01_PitchHue, where each note of the scale maps to a particular color,
	but there are multiple inputs, each corresponding to a different shape.
*/
public class Module_01_03_PitchHue_MultipleShapes extends Module {


	public static void main(String[] args)
	{
		PApplet.main("module_01_03.Module_01_03_PitchHue_MultipleShapes");
	} // main

	public void setup() 
	{
		// 1 input for testing:
//		this.input	= new RealTimeInput(1, new AudioContext(), this);
		this.input	= new RealTimeInput(16, true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= this.totalNumInputs;

		this.menu	= new ModuleMenu(this, this, this.input, 12);


		this.setSquareValues();

		// call add methods:
		this.menu.addSensitivityMenu(true);
		
		this.menu.addColorMenu();
		
		this.menu.getControlP5().getController("keyDropdown").bringToFront();

		this.menu.addShapeMenu(16);

	} // setup()


	public void draw()
	{
		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		int	scaleDegree;

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed

		for(int i = 0; i < this.curNumInputs; i++)
		{
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fadeColor(scaleDegree, i);

			if(!this.menu.getShapeEditor().getIsRunning())
			{
				this.menu.getShapeEditor().drawShape(i);
			}
			

			if(this.menu.isShowScale() && !this.menu.getShapeEditor().getIsRunning())
			{
				// draws the legend along the bottom of the screen:
				this.legend(scaleDegree, i);

			} // if showScale

		} // for

		this.menu.runMenu();
	} // draw()


	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

	public void mouseDragged()
	{
		this.mousePressed();
	}

	public void mousePressed()
	{
		/*
		FullScreenDisplay fsm = new FullScreenDisplay();
		fsm.startDisplay();
		this.shapeEditor.setPApplet(fsm);
		this.menu.setPApplet(fsm);
		 */

		if(!this.menu.getShapeEditor().getControlP5().isMouseOver() && !this.menu.getControlP5().isMouseOver() && !this.menu.getOutsideButtonsCP5().isMouseOver())
		{
			ShapeEditor	shapeEditor	= this.menu.getShapeEditor();
			Shape[]		shapes		= shapeEditor.getShapes();
			// Map if running:
			if(shapeEditor.getIsRunning() || this.menu.getIsRunning())
			{	
				shapes[shapeEditor.getShapeIndex()].setXPos( shapeEditor.mapFullAppletXPos( Math.max( shapeEditor.mapAdjustedMenuXPos(0), Math.min(this.mouseX, shapeEditor.mapFullAppletXPos(this.width) ) ) ) );
				shapes[shapeEditor.getShapeIndex()].setYPos( shapeEditor.mapFullAppletYPos( Math.max( shapeEditor.mapAdjustedMenuYPos(0), Math.min(this.mouseY, shapeEditor.mapFullAppletYPos(this.height) ) ) ) );
				shapeEditor.updateSliders();
			}
			else
			{
				// If neither are running, just keep w/in bounds:
				shapes[shapeEditor.getShapeIndex()].setXPos( Math.max( 0, Math.min(this.mouseX, this.width) ) );
				shapes[shapeEditor.getShapeIndex()].setYPos( Math.max( 0, Math.min(this.mouseY, this.height) ) );
				shapeEditor.updateSliders();
			}
		} // if - not over either ControlP5
	} // mouseClicked

}
