package module_01_04;

import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class Module_01_04_PitchHue_Kristina extends Module {

	int[][] move;
	int[] hold1;
	int[] hold2;
	int checkpoint;


	public static void main(String[] args)
	{
		PApplet.main("module_01_04.Module_01_04_PitchHue_Kristina");
	} // main

	public void setup() 
	{
		this.input			= new RealTimeInput(3, false, this);
		//		this.input	= new RealTimeInput(16, true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= this.totalNumInputs;

		this.menu	= new ModuleMenu(this, this, this.input, 12);


		this.setSquareValues();

		// call add methods:
		this.menu.addSensitivityMenu(true);

		this.menu.addColorMenu();

		this.menu.getControlP5().getController("keyDropdown").bringToFront();
		background(255);
		int[] color;

		color = new int [] {255,255,255};
		this.menu.setCanvasColor(color);
		this.noStroke();

		move = new int [925][3];
		hold1 = new int [3];
		hold2 = new int [3];
		checkpoint = this.millis(); 
		System.out.println(this.width + ";" + (this.width/185) + ";" + (this.height));



		//		this.menu.shapeEditor = this.shapeEditor;

	} // setup()


	public void draw()
	{
		//		System.out.println("input.getAdjustedFund() = " + input.getAdjustedFund());


		int	scaleDegree;

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning()) 
		{
			this.menu.setMenuVal();
		} // if keyPressed

		//System.out.println("****** CurNumInputs = " + this.curNumInputs + "  *******");

		for(int i = 0; i < this.getCurNumInputs(); i++)
		{

			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;

			this.menu.fadeColor(scaleDegree, i);

			hold2[0] = this.menu.getCurHue()[0][0];
			hold2[1] = this.menu.getCurHue()[0][1];
			hold2[2] = this.menu.getCurHue()[0][2];

			if(checkpoint < this.millis())
			{
				for(int j = 0; j < move.length; j++)
				{	

					hold1[0] = move[j][0];
					hold1[1] = move[j][1];
					hold1[2] = move[j][2];


					move[j][0] = hold2[0];
					move[j][1] = hold2[1];
					move[j][2] = hold2[2];

					//System.out.println(this.width/move.length);
					fill(move[j][0], move[j][1], move[j][2]);
					rect(((this.width/move.length)*(j)), (this.height/3) * i, (this.width/move.length), this.height/3);

					hold2[0] = hold1[0];
					hold2[1] = hold1[1];
					hold2[2] = hold1[2];

				}
				//checkpoint = this.millis() + 100;
			}
		} // for - numInputs

		//rect(((this.width/move.length)*(i)),(this.height/3)*1 ,(this.width/move.length), this.height/3);
		//rect(((this.width/move.length)*(i)),(this.height/3)*2 ,(this.width/move.length), this.height/3);




		//rect(this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(0), width - this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(height));

		//			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());




		//if(this.menu.isShowScale())
		//{
		// draws the legend along the bottom of the screen:
		//this.legend(scaleDegree, 0);

		//} // if showScale





		//		this.shapeEditor.runMenu();
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



}
