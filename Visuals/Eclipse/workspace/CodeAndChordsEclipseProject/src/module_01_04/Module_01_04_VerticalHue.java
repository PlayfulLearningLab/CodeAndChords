package module_01_04;

import core.Module;
import core.ModuleMenu;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class Module_01_04_VerticalHue extends Module {

	int[][] move;
	int[] hold1;
	int[] hold2;
	int checkpoint;


	public static void main(String[] args)
	{
		PApplet.main("module_01_04.Module_01_04_VerticalHue");
	} // main

	public void setup() 
	{
		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		//		this.input	= new RealTimeInput(16, true, this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 3;

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

		move = new int [10][3];
		hold1 = new int [3];
		hold2 = new int [3];
		checkpoint = this.millis(); 
		//System.out.println(this.width + ";" + (this.width/185) + ";" + (this.height));



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

		
		if(checkpoint < this.millis())
		{
			for(int j = 0; j < this.curNumInputs; j++)
			{
			scaleDegree	= (round(input.getAdjustedFundAsMidiNote(j)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;

			this.menu.fade(scaleDegree, j);


		
			
			hold2[0] = this.menu.getCurHue()[j][0];
			hold2[1] = this.menu.getCurHue()[j][1];
			hold2[2] = this.menu.getCurHue()[j][2];
			
			for(int i = 0; i < move.length; i++)
			{	

				hold1[0] = move[i][0];
				hold1[1] = move[i][1];
				hold1[2] = move[i][2];


				move[i][0] = hold2[0];
				move[i][1] = hold2[1];
				move[i][2] = hold2[2];

			
				fill(move[i][0], move[i][1], move[i][2]);
				rect(((this.width/move.length)*(i)),(this.height/this.curNumInputs)*j,(this.width/move.length), this.height/this.curNumInputs);
			
				
				hold2[0] = hold1[0];
				hold2[1] = hold1[1];
				hold2[2] = hold1[2];
				System.out.println(((this.width/move.length)*(i)));
			}
			//checkpoint = this.millis() + 100;
			}
		}
		



		//rect(this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(0), width - this.menu.mapCurrentXPos(0), this.menu.mapCurrentYPos(height));

		//			this.fill(this.menu.getCurHue()[i][0], this.menu.getCurHue()[i][1], this.menu.getCurHue()[i][2], this.menu.getAlphaVal());




		//if(this.menu.isShowScale())
	//{
			//draws the legend along the bottom of the screen:
			//this.legend(scaleDegree, j);
		//}

		




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
