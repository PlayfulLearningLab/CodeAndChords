package functionSketch_02;

import java.awt.Color;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.Slider;
import core.Input;
import processing.core.PApplet;
import processing.core.PShape;

public class FunctionSketch_02_MultiInput extends PApplet 
{
	int[]		xVals;
	int[]		yVals;
	int			numInputs	= 12;
	int			side;
	int[]		assigned;

	Input		input;

	ControlP5	cp5;
	String[]	listItems;

	float[]		lowThresholds;
	float[]		maxSatThresholds;

	float[][] rainbowColors	= new float[][] {
		{ 255, 0, 0 }, 
		{ 255, (float) 127.5, 0 }, 
		{ 255, 255, 0 }, 
		{ (float) 127.5, 255, 0 }, 
		{ 0, 255, 0 }, 
		{ 0, 255, (float) 127.5 }, 
		{ 0, 255, 255 }, 
		{ 0, (float) 127.5, 255 }, 
		{ 0, 0, 255 }, 
		{ (float) 127.5, 0, 255 }, 
		{ 255, 0, 255 }, 
		{ 255, 0, (float) 127.5 }
	} ;

	public static void main( String[] args )
	{
		PApplet.main("functionSketch_02.FunctionSketch_02_MultiInput");
	}

	public void settings()
	{
		size(800, 600);
	} // setup()

	public void setup()
	{
		background(0);

		this.input	= new Input(this.numInputs);
		this.cp5	= new ControlP5(this);

		this.side	= 200;

		this.xVals	= new int[] {
				0, this.width/4, this.width/2, (this.width/4) * 3,
				0, this.width/4, this.width/2, (this.width/4) * 3,
				0, this.width/4, this.width/2, (this.width/4) * 3,
		};
		this.yVals	= new int[] {
				0, 0, 0, 0,
				this.height/3, this.height/3, this.height/3, this.height/3, 
				((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2)
		};

		this.listItems	= new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
		}; // listItems
		/*	
		this.squares	= new PShape[this.numInputs];
		for(int i = 0; i < this.squares.length; i++)
		{
			this.squares[i]	= createShape(RECT, this.xVals[i], this.yVals[i], 200, 200);
//			this.squares[i].setFill(true);
		};
		 */

		this.assigned			= new int[this.numInputs];
		this.lowThresholds		= new float[this.numInputs];
		this.maxSatThresholds	= new float[this.numInputs];
		for(int i = 0; i < this.numInputs; i++)
		{
			fill(150);
			rect(xVals[i], yVals[i], (this.width / 4), (this.height / 3));

			this.assigned[i]			= -1;
			this.lowThresholds[i]		= 10;
			this.maxSatThresholds[i]	= 2000;
			
			System.out.println("this.lowThresholds[" + i + "] = " + this.lowThresholds[i]);


			this.cp5.addGroup("controls" + i)
			.setVisible(false);

			this.cp5.addButton("square" + i)
			.setPosition(this.xVals[i], this.yVals[i])
			.setSize(this.side, this.side)
			.setClickable(true)
			.setVisible(false)
			.setId(i);

			int	id	= (i + 12);
			this.cp5.addScrollableList("list" + (i + 12))
			.setPosition(this.xVals[i] + 3, this.yVals[i] + 3)
			.setSize(this.side / 3, this.side)
			.setBarHeight(15)
			.setItemHeight(side / 13)
			.setItems(this.listItems)
			.setLabel("Line:")
			.setGroup("controls" + i)
			.setId(id);

			id	= (i + 24);
			this.cp5.addSlider("lowSlider" + (i + 24))
			.setPosition( ( this.xVals[i] + 3 ), ( this.yVals[i] + side - 18 ) )
			.setWidth(side - 6)
			.setHeight(15)
			.setId(id)
			.setGroup("controls" + i)
			.setRange(2, 1000)
//			.setDefaultValue(this.lowThresholds[i])
			.setSliderMode(Slider.FLEXIBLE)
			.bringToFront()
			.getCaptionLabel().setVisible(false);
			this.cp5.getController("lowSlider" + (i + 24)).update();

			id	= (i + 36);
			this.cp5.addSlider("highSlider" + (i + 36))
			.setPosition( ( this.xVals[i] + 3 ), ( this.yVals[i] + side - 36 ) )
			.setWidth(side - 6)
			.setHeight(15)
			.setId(id)
			.setGroup("controls" + i)
			.setRange(100, 5000)
//			.setDefaultValue(this.maxSatThresholds[i])
			.setSliderMode(Slider.FLEXIBLE)
			.bringToFront()
			.getCaptionLabel().setVisible(false);
			
//			while(this.cp5.getController("lowSlider" + (id + 24)) == null)	{ }
		} // for
	} // setup

	public void draw()
	{		
		for(int i = 0; i < this.numInputs; i++)
		{
			int	lineAssigned	= this.assigned[i];
			float	lineAmp;

			fill(150);
			rect(xVals[i], yVals[i], (this.width / 4), (this.height / 3));

			if( ( lineAssigned > -1 ) && ( this.input.getAmplitude(lineAssigned) > this.lowThresholds[i] ) )
			{

				// Uses the thresholds to put the amplitude into a place that makes sense for a color alpha:
				lineAmp		= map(this.input.getAmplitude(lineAssigned), this.lowThresholds[i], this.maxSatThresholds[i], 0, 255);

				fill(this.rainbowColors[i][0], this.rainbowColors[i][1], this.rainbowColors[i][2], lineAmp);

			} else {
				lineAmp	= 0;
				fill(150);
				//				this.squares[i].setFill(grayInt);
			}

			rect(xVals[i], yVals[i], (this.width / 4), (this.height / 3));

			if(lineAssigned > -1)
			{
				fill(0);
				text(lineAssigned, (xVals[i] + 10), (yVals[i] + 15));
				text(lineAmp, (xVals[i] + (this.width / 8)), (yVals[i] + 15));
			}

			//			shape(squares[i]);

		} // for
	} // draw

	public void controlEvent(ControlEvent theControlEvent)
	{
		System.out.println("Got controlEvent " + theControlEvent.getName() + "; id = " + theControlEvent.getController().getId());

		int	id	= theControlEvent.getController().getId();

		// Clicked in a square:
		if(id < 12 && id > -1)
		{
//			this.cp5.getController("list" + (id + 12)).setVisible(!this.cp5.getController("list" + (id + 12)).isVisible());
			//			this.cp5.getController("list" + (id + 12)).setVisible(true);
			System.out.println("this.cp5 = " + this.cp5 + "; this.cp5.getGroup(controls" + id + ") = " + this.cp5.getGroup("controls" + id));
			boolean	visible	= this.cp5.getGroup("controls" + id).isVisible();
			this.cp5.getGroup("controls" + id).setVisible(!visible);
		}

		// Clicked on a list:
		if(id >= 12 && id < 24)
		{
			this.assigned[(id - 12)]	= (int)theControlEvent.getValue() + 1;
			//			theControlEvent.getController().setVisible(!theControlEvent.getController().isVisible());
			//			System.out.println("Just set controller " + theControlEvent.getController() + " to !visible.");
		}
		
		// Clicked on a low-range slider:
		if(id >= 24 && id < 36)
		{
			this.lowThresholds[id - 24]	= theControlEvent.getValue();
		} // low-range sliders
		
		// Clicked on a high-range slider:
		if(id >= 36 && id < 48)
		{
			this.maxSatThresholds[id - 36]	= theControlEvent.getValue();
		} // high-range sliders
	} // controlEvent

} // FunctionSketch_02
