package module_01_02;

import processing.core.*;
import core.Module;
import core.ModuleMenu;
import core.archive.InputMatrix;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_01_02_PitchHue_MultipleInputs_Kristina extends Module
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
		PApplet.main("module_01_02.Module_01_02_PitchHue_MultipleInputs_Kristina");
	} // main
	
	public void setup() 
	{
		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		//this.input          = RealTimeInput() Elena give this the audiocontext in branch
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 1;
		
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
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
//			System.out.println("input.getAdjustedFundAsMidiNote(" + (i + 1) + ") = " + input.getAdjustedFundAsMidiNote(i + 1) + 
//					"; input.getAmplitude(" + (i + 1) + ") = " + input.getAmplitude(1 + 1));
			
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i + 1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fade(scaleDegree, i);
			
			this.fill( (255 - this.menu.getCurHue()[i][0]), (255 - this.menu.getCurHue()[i][1]), (255 - this.menu.getCurHue()[i][2]), this.menu.getAlphaVal() );
			
			this.ellipse(250, 250, 350, 350);

			
		
			this.fill( (this.menu.getCurHue()[i][0]), (this.menu.getCurHue()[i][1]), (this.menu.getCurHue()[i][2]) , this.menu.getAlphaVal());
			this.ellipse(160, 160, 350, 350);
			
			
			//if(this.menu.isShowScale())
			//{
				//this.legend(scaleDegree, i);
			//}
	
		} // for
		
		this.menu.runMenu();

	} // draw()



	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

} // class