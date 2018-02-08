package module_01_02;

import processing.core.*;
import core.InputMatrix;
import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_01_02_PitchHue_MultipleInputs extends Module
{
	/**
	 * 
	 * 
 1/4/2016
 Emily

	 * 08/01/2016
	 * Emily Meuer

	 *
	 * Background changes hue based on pitch.
	 *
	 * (Adapted from Examples => Color => Hue.)
	 */

	public static void main(String[] args)
	{
		PApplet.main("module_01_02.Module_01_02_PitchHue_MultipleInputs");
	} // main
	
	public void setup() 
	{
		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		//this.input          = RealTimeInput() Elena give this the audiocontext in branch
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 2;
		
		this.menu	= new ModuleMenu(this, this, this.input, 12);

		this.setSquareValues();

		// call add methods:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();
	} // setup()


	public void draw()
	{
		int	scaleDegree;

		// The following line is necessary so that key press shows the menu button
		/*		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed
		 */

			this.menu.fade(scaleDegree, i);
			
			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2]);
			
			int	curX;
			int	curY;
			
			curX	= (int)this.menu.mapCurrentXPos(this.xVals[i]);
			curY	= (int)this.menu.mapCurrentYPos(this.yVals[i]);
			this.rect(curX, curY, this.rectWidths[i], this.rectHeights[i]);
			
			if(this.inputMatrix.isVisible())
			{
				this.inputMatrix.drawMatrix();
			}
	
		} // for
		
		this.menu.runMenu();

	} // draw()

	public void mousePressed()
	{
		if(this.inputMatrix.isVisible())
		{
			//			this.inputMatrix.mousePressed();
			this.inputMatrix.assignInputFromLoc(this.mouseX, this.mouseY);
		}
	}


	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

} // class