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
	int[]		rectWidths;
	int[]		rectHeights;
	int			numInputs	= 12;
	int[]		assigned;
	boolean		assignAll	= true;

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
		//		this.textSize(10);

		// Always initialize for 12, even if we actually have fewer, just to be safe:
		this.input	= new Input((12 + 4), true, this);
		this.cp5	= new ControlP5(this);

		this.setSquareValues();
		
		this.addControls();

		// Add input number selection:
		String[] numInputItems	= new String[] {
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"
		}; // numInputItems
		this.cp5.addScrollableList("numInputsList")
		.setPosition(this.width / 2, this.height / 2)
		.setItems(numInputItems)
		.setVisible(false);
	} // setup

	public void draw()
	{		
		for(int i = 0; i < this.numInputs; i++)
		{
			int	lineAssigned	= this.assigned[i];
			float	lineAmp;

			fill(150);
			rect(xVals[i], yVals[i], this.rectWidths[i], this.rectHeights[i]);

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

			rect(xVals[i], yVals[i], this.rectWidths[i], this.rectHeights[i]);

			if(lineAssigned > -1)
			{
				fill(0);
				//				text(lineAssigned, (xVals[i] + 10), (yVals[i] + 15));
				text(lineAmp, (xVals[i] + (this.width / 8)), (yVals[i] + 15));
//				text(0, (xVals[i] + (this.width / 8)), (yVals[i] + 15));

			}

			//			shape(squares[i]);

		} // for
	} // draw

	public void setSquareValues()
	{
		// Rectangles are always the same height, so will be set in a loop every time:
		this.rectHeights	= new int[this.numInputs];

		// Setting xVals and yVals and width and height of rectangles:
		// Even number of inputs:
		if(this.numInputs % 2 == 0 && this.numInputs != 12)
		{
			this.rectWidths		= new int[this.numInputs];
			this.rectHeights	= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				this.rectWidths[i]	= this.width / (this.numInputs / 2);
				this.rectHeights[i]	= this.height / 2;
			} // for

			this.xVals	= new int[this.numInputs];
			this.yVals	= new int[this.numInputs];

			for(int i = 0; i < this.xVals.length; i++)
			{
				int xPos	= i % (this.numInputs / 2);
				int xVal	= xPos * (this.rectWidths[i]);
				xVals[i]	= xVal;
				System.out.println(i + ": xPos = " + xPos + "; xVal = " + xVal);
			} // for - xVals

			for(int i = 0; i < this.yVals.length; i++)
			{
				int	yPos	= i / (this.numInputs / 2);
				int	yVal	= yPos * this.rectHeights[i];
				yVals[i]	= yVal;
				System.out.println(i + ": yPos = " + yPos + "; yVal = " + yVal);
			} // for - yVals
		} // even number of inputs
		else if(this.numInputs == 1)
		{
			this.rectWidths		= new int[] {	this.width	};
			this.rectHeights	= new int[]	{	this.height	};

			this.xVals	= new int[] {	0	};
			this.yVals	= new int[] {	0	};
		} // 1
		else if(this.numInputs == 3)
		{
			this.rectWidths		= new int[] {	
					this.width,
					(this.width / 2), (this.width / 2)
			};
			for(int i = 0; i < this.rectHeights.length; i++)
			{
				this.rectHeights[i]	= this.height / 2;
			}

			this.xVals	= new int[] { 
					0,
					0,	(this.width / 2)
			};
			this.yVals	= new int[] {
					0,
					(this.height / 2), (this.height / 2)
			};
		} // 3
		else if(this.numInputs == 5)
		{
			this.rectWidths	= new int[] {
					(this.width / 2),	(this.width / 2),
					(this.width / 3), (this.width / 3), (this.width / 3)
			};
			for(int i = 0; i < this.rectHeights.length; i++)
			{
				this.rectHeights[i]	= this.height / 2;
			}

			this.xVals	= new int[] {
					0,				(this.width / 2),	
					0,	(this.width / 3), ((this.width / 3) * 2)
			};
			this.yVals	= new int[] {
					0,				0,
					(this.height / 2), (this.height / 2), (this.height / 2)
			};
		} // 5
		else if(this.numInputs == 7)
		{
			this.rectWidths	= new int[] {
					(this.width / 2),	(this.width / 2),
					(this.width / 2), (this.width / 3), (this.width / 3),
					(this.width / 2),	(this.width / 2)
			};
			for(int i = 0; i < this.rectHeights.length; i++)
			{
				this.rectHeights[i]	= this.height / 3;
			}

			this.xVals	= new int[] {
					0,				(this.width / 2),	
					0,	(this.width / 3), ((this.width / 3) * 2),
					0,				(this.width / 2)
			};
			this.yVals	= new int[] {
					0,				0,
					(this.height / 3), (this.height / 3), (this.height / 3),
					(this.height / 3) * 2, (this.height / 3) * 2, (this.height / 3) * 2
			};
		} // 7
		else if(this.numInputs == 9)
		{
			this.rectWidths		= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				this.rectWidths[i]	= (this.width / 3);
				this.rectHeights[i]	= (this.height / 3);
			} // for

			this.xVals	= new int[] {
					0, this.width/3, (this.width/3) * 2,
					0, this.width/3, (this.width/3) * 2,
					0, this.width/3, (this.width/3) * 2
			};
			this.yVals	= new int[] {
					0, 0, 0,
					this.height/3, this.height/3, this.height/3, 
					((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2)
			};
		} // 9
		else if(this.numInputs == 11)
		{
			this.rectWidths		= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				if(i < 4 || i > 6)
				{
					this.rectWidths[i]	= (this.width / 4);
				} else {
					// middle row has only 3:
					this.rectWidths[i]	= (this.width / 3);
				}

				this.rectHeights[i]	= (this.height / 3);
			} // for

			this.xVals	= new int[] {
					0, this.width/4, this.width/2, (this.width/4) * 3,
					0, this.width/3, (this.width/3) * 2,
					0, this.width/4, this.width/2, (this.width/4) * 3,
			};
			this.yVals	= new int[] {
					0, 0, 0, 0,
					this.height/3, this.height/3, this.height/3, 
					((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2), ((this.height / 3) * 2)
			};
		} // 11
		else if(this.numInputs == 12)
		{
			this.rectWidths		= new int[this.numInputs];
			for(int i = 0; i < this.rectWidths.length; i++)
			{
				this.rectWidths[i]	= (this.width / 4);
				this.rectHeights[i]	= (this.height / 3);
			} // for

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
		} // 12

		// Reset listItems and this.assigned:
		this.listItems			= new String[this.numInputs];
		this.assigned			= new int[this.numInputs];
		this.lowThresholds		= new float[this.numInputs];
		this.maxSatThresholds	= new float[this.numInputs];
		for(int i = 0; i < this.listItems.length; i++)
		{
			this.listItems[i]	= String.valueOf(i + 1);			

			if(this.assignAll)	{	this.assigned[i]	= (i + 1);	}
			else				{	this.assigned[i]	= -1;		}

			this.lowThresholds[i]		= 10;
			this.maxSatThresholds[i]	= 2000;
		}

	} // setSquareValues

	/**
	 * This method adds Controllers to this.cp5;
	 * since it is called every time a new number of inputs is selected,
	 * it will warn that it is overwriting controllers,
	 * but this is okay, because we need them to be new controllers with different sizes.
	 */
	private void addControls()
	{
		for(int i = 0; i < this.numInputs; i++)
		{
			this.cp5.addGroup("controls" + i)
			.setVisible(false);

			this.cp5.addButton("square" + i)
			.setPosition(this.xVals[i], this.yVals[i])
			.setSize(this.rectWidths[i], this.rectHeights[i])
			.setClickable(true)
			.setVisible(false)
			.setId(i);

			int	id	= (i + 12);
			this.cp5.addScrollableList("list" + (i + 12))
			.setPosition(this.xVals[i] + 3, this.yVals[i] + 3)
			.setSize(50, this.rectHeights[i])
			.setBarHeight(15)
			.setItemHeight((this.rectHeights[i] - 36) / 13)
			.setItems(this.listItems)
			.setLabel("Line:")
			.setGroup("controls" + i)
			.setId(id);

			// When the mics are pre-assigned, show the mic number in the lists:
			if(this.assignAll)
			{
				this.cp5.getController("list" + (i + 12)).setValue(i);
			}

// TODO - think we need this id thing...
			id	= (i + 24);
			this.cp5.addSlider("lowSlider" + (i + 24))
			.setPosition( ( this.xVals[i] + 3 ), ( this.yVals[i] + this.rectHeights[i] - 18 ) )
			.setWidth(this.rectWidths[i] - 6)
			.setHeight(15)
			.setId(id)
			.setGroup("controls" + i)
			.setRange(2, 1000)
			.setValue(this.lowThresholds[i])
			.setSliderMode(Slider.FLEXIBLE)
			.bringToFront()
			.getCaptionLabel().setVisible(false);
			this.cp5.getController("lowSlider" + (i + 24)).update();

			id	= (i + 36);
			this.cp5.addSlider("highSlider" + (i + 36))
			.setPosition( ( this.xVals[i] + 3 ), ( this.yVals[i] + this.rectHeights[i] - 36 ) )
			.setWidth(this.rectWidths[i] - 6)
			.setHeight(15)
			.setId(id)
			.setGroup("controls" + i)
			.setRange(100, 5000)
			.setValue(this.maxSatThresholds[i])
			.setSliderMode(Slider.FLEXIBLE)
			.bringToFront()
			.getCaptionLabel().setVisible(false);
			
//			while(this.cp5.getController("lowSlider" + (id + 24)) == null)	{ }
		} // for
	} // addcontrols

	public void controlEvent(ControlEvent theControlEvent)
	{
		//		System.out.println("Got controlEvent " + theControlEvent.getName() + " with id " + theControlEvent.getController().getId());

		int	id	= theControlEvent.getController().getId();

		if(mouseButton == RIGHT)
		{
			//			System.out.println("Hooray! A right press!");
			this.cp5.getController("numInputsList").setVisible(!this.cp5.getController("numInputsList").isVisible());
			this.cp5.getController("numInputsList").setPosition(mouseX, mouseY);
		} // if
		else
		{
			// Clicked in a square:
			if(id < 12 && id > -1)
			{
				//			this.cp5.getController("list" + (id + 12)).setVisible(!this.cp5.getController("list" + (id + 12)).isVisible());
				//			this.cp5.getController("list" + (id + 12)).setVisible(true);
				this.cp5.getGroup("controls" + id).setVisible(!this.cp5.getGroup("controls" + id).isVisible());
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

			if(theControlEvent.getName().equals("numInputsList"))
			{
				System.out.println("A left click from numInputsList");

				this.numInputs	= (int)theControlEvent.getValue() + 1;
				this.setSquareValues();
				this.addControls();
			} // numInputsList

		// Clicked in a square:
		if(id < 12 && id > -1)
		{
//			this.cp5.getController("list" + (id + 12)).setVisible(!this.cp5.getController("list" + (id + 12)).isVisible());
			//			this.cp5.getController("list" + (id + 12)).setVisible(true);
			System.out.println("this.cp5 = " + this.cp5 + "; this.cp5.getGroup(controls" + id + ") = " + this.cp5.getGroup("controls" + id));
			boolean	visible	= this.cp5.getGroup("controls" + id).isVisible();
			this.cp5.getGroup("controls" + id).setVisible(!visible);
		} // in square
			} // leftClick

	} // controlEvent

} // FunctionSketch_02
