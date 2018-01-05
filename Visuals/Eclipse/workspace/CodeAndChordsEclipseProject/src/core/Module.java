package core;

import core.input.RealTimeInput;
import processing.core.PApplet;

/**
 * Aug. 16, 2017
 * 
 * Template for Modules
 * 
 * @author Dan Mahota, Emily Meuer
 */
public abstract class Module extends PApplet {
	
	/**	Input, because we are assuming that the whole point of a Module is to interact with an Input	*/
	protected	RealTimeInput			input;
	
	/**	This is the total number of possible inputs; *must* be initialized by child classes!	*/
	protected	int		totalNumInputs = 1;
	
	/**	This is the number of inputs currently displaying in the Module	*/
	protected	int		curNumInputs;

	protected	int[]	xVals;
	protected	int[]	yVals;
	protected	int[]	rectWidths;
	protected	int[]	rectHeights;
	
	protected	boolean	debugLegendColors	= false;
	
	protected	Shape			shape;
	protected	Shape[]			shapes;
	
	/**	For Modules with a Shape, this ShapeEditor provides Shape customization Controllers	*/
	protected 	ShapeEditor		shapeEditor;
	
	/**	"Sidebar" Menu, where most basic Controllers will be - global HSB and RGB modulation, etc.	*/
	protected	ModuleMenu		menu;
	
	protected	int		currentMenu;
	
	protected 	boolean		verticalBarsDemo = false;
	protected 	float[]		amplitude;
	
	/**	Used by legend() to determine which colors to select for the legend along the bottom	*/
	private	final	int[][] scaleDegrees = new int[][] {
		// major:
		new int[]  { 0, 2, 4, 5, 7, 9, 11
		},
		// minor:
		new int[]  { 0, 2, 3, 5, 7, 8, 10
		},
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11
		}
	}; // scaleDegrees

	/**
	 * Sets the Module size.
	 */
	public void settings()
	{
		//fullscreen();
		size(925, 520);
	}
	

	/**
	 * Setter for the shapeEditor.isRunning variable
	 * 
	 * @param isRunning	indicates whether or not the ShapeEditor should be open (i.e., running)
	 */
	public void setShapeEditorRunning(boolean isRunning)
	{
		this.shapeEditor.setIsRunning(isRunning);
	} // setShapeEditorRunning

	/**
	 * Getter for Shape instance variable
	 * @return	this.shape
	 */
	public Shape getShape() {
		return this.shape;
	} // getShape
	

	/**
	 * Draws the legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position, be that note or threshold level, in this Module's menu.colorSelect
	 */
	public void legend(int goalHuePos, int inputNum)
	{
		if(this.rectWidths == null)
		{
			this.curNumInputs		= Math.max(this.curNumInputs, 1);
			this.setSquareValues();
		}
		
		this.textSize(Math.max(24 - (this.curNumInputs * 2), 8));

		String[]	legendText	= this.getLegendText();
		
//		float	scale	= 1;
//		if(this.menu.getIsRunning())	{	scale	= this.menu.getScale();	}
		float	scale	= this.menu.getCurrentScale();

		float	sideWidth1	=(this.rectWidths[inputNum] * scale) / legendText.length;
		float	sideHeight	= this.rectHeights[inputNum] / 10; //this.rectWidths[inputNum] / 12;	// pretty arbitrary
		float	addToLastRect	= (this.rectWidths[inputNum] * scale) - (sideWidth1 * legendText.length);
		float	sideWidth2	= sideWidth1;

		this.noStroke();
		
		int	scaleDegree;
		float	xVal	= this.menu.mapCurrentXPos(this.xVals[inputNum]);
		float	yVal	= this.menu.mapCurrentYPos(this.yVals[inputNum] + this.rectHeights[inputNum]);

		for (int i = 0; i < legendText.length; i++)
		{
			if(i == legendText.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}
			
			// colors is filled all the way and only picked at the desired notes:
			scaleDegree	= this.scaleDegrees[this.menu.getMajMinChrom()][i];
			this.fill(this.menu.colors[inputNum][scaleDegree][0], this.menu.colors[inputNum][scaleDegree][1], this.menu.colors[inputNum][scaleDegree][2]);

			if (i == goalHuePos) {
				this.rect(xVal + (sideWidth1 * i), yVal - (sideHeight * 1.5f), sideWidth2, (sideHeight * 1.5f));
			} else {
				this.rect(xVal + (sideWidth1 * i), yVal - sideHeight, sideWidth2, sideHeight);
			}

			this.fill(0);
			this.text(legendText[i], (float) (xVal + (sideWidth1 * i) + (sideWidth1 * 0.3)), yVal - (sideHeight * 0.3f));
		
			if(debugLegendColors)
			{
				this.textSize(12);
				this.text(("r: " + this.menu.colors[inputNum][scaleDegree][0]), (float) (xVal + (sideWidth1 * i)) + 10, yVal - (sideHeight * 0.4f) - 50);
				this.text(("g: " + this.menu.colors[inputNum][scaleDegree][1]), (float) (xVal + (sideWidth1 * i)) + 10, yVal - (sideHeight * 0.4f) - 40);
				this.text(("b: " + this.menu.colors[inputNum][scaleDegree][2]), (float) (xVal + (sideWidth1 * i)) + 10, yVal - (sideHeight * 0.4f) - 30);
			}
		} // for - i

	} // legend

	/**
	 * Draws the legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position, be that note or threshold level, in this Module's menu.colorSelect
	 */
	public void okGoLegend(int goalHuePos, int inputNum)
	{
		if(this.rectWidths == null)
		{
			this.curNumInputs		= Math.max(this.curNumInputs, 1);
			this.setSquareValues();
		}
		
		this.textSize(Math.max(24 - (this.curNumInputs * 2), 8));

		String[]	legendText	= this.getLegendText();
		
//		float	scale	= 1;
//		if(this.menu.getIsRunning())	{	scale	= this.menu.getScale();	}
		float	scale	= this.menu.getCurrentScale();

		float	sideWidth1	=(this.rectWidths[inputNum] * scale) / legendText.length;
		float	sideHeight	= this.rectHeights[inputNum] / 10; //this.rectWidths[inputNum] / 12;	// pretty arbitrary
		float	addToLastRect	= (this.rectWidths[inputNum] * scale) - (sideWidth1 * legendText.length);
		float	sideWidth2	= sideWidth1;

		this.noStroke();
		
		int	scaleDegree;
		float	xVal;
		float	yVal;
		if(inputNum < 2)
		{
			xVal	= this.menu.mapCurrentXPos(this.xVals[inputNum]);
			yVal	= this.menu.mapCurrentYPos(this.yVals[inputNum] + (sideHeight * this.menu.getCurrentScale() * 1.5f));
		} else {
			xVal	= this.menu.mapCurrentXPos(this.xVals[inputNum]);
			yVal	= this.menu.mapCurrentYPos(this.yVals[inputNum] + this.rectHeights[inputNum]);
		}

		for (int i = 0; i < legendText.length; i++)
		{
			if(i == legendText.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}
			
			// colors is filled all the way and only picked at the desired notes:
			scaleDegree	= this.scaleDegrees[this.menu.getMajMinChrom()][i];
			this.fill(this.menu.colors[inputNum][scaleDegree][0], this.menu.colors[inputNum][scaleDegree][1], this.menu.colors[inputNum][scaleDegree][2]);

			if (i == goalHuePos) {
				this.rect(xVal + (sideWidth1 * i), yVal - (sideHeight * this.menu.getCurrentScale() * 1.5f), sideWidth2, (sideHeight * this.menu.getCurrentScale() * 1.5f));
			} else {
				this.rect(xVal + (sideWidth1 * i), yVal - (sideHeight * this.menu.getCurrentScale()), sideWidth2, sideHeight * this.menu.getCurrentScale());
			}

			this.fill(0);
			this.text(legendText[i], (float) (xVal + (sideWidth1 * i) + (sideWidth1 * 0.3)), yVal - (sideHeight * 0.3f));
		} // for - i

	} // legend

	
	/**
	 * @return	String[] of text to display in each position of the legend
	 */
	public abstract String[] getLegendText();
	
	
	public void setCurNumInputs(int newCurNumInputs)
	{
		this.curNumInputs	= newCurNumInputs;
	}
	
	public int getCurNumInputs()
	{
		return this.curNumInputs;
	}
	
	public int getTotalNumInputs()
	{
		return this.totalNumInputs;
	}
	
	protected void drawShape(int shapeIndex)
	{
		shapes[shapeIndex].drawShape(this.menu, shapeIndex);
	}
	
	protected void drawShapes()
	{
		for(int i = 0; i < this.curNumInputs; i++)
		{
			shapes[i].drawShape(this.menu, i);
		}
	}
	
	/**
	 * Calculates the x and y values for the squares given the number of inputs.
	 */
	protected void setSquareValues()
	{
		if(this.verticalBarsDemo)
		{
			int barWidth = this.width/this.curNumInputs;
			int val = this.height/2;
						
			this.xVals = new int[this.curNumInputs];
			this.yVals = new int[this.curNumInputs];
			this.rectWidths = new int[this.curNumInputs];
			this.rectHeights = new int[this.curNumInputs];
			
			for(int i = 0; i < this.curNumInputs; i++)
			{
				//gives value between 0 and 1 to be used as a percent
				//System.out.println(this.amplitude[i]);
				float amp = (float) Math.min(1, this.amplitude[i] / 500/*max amp*/);
				amp = (float) Math.max(amp, .1);
				
				this.xVals[i] = i * barWidth;
				//this.yVals[i] = (int) (val - (amp*val));
				this.yVals[i] = 0;
				this.rectWidths[i] = barWidth;
				//this.rectHeights[i] = (int) (amp * this.height);
				this.rectHeights[i] = this.height;
			}
			
		}
		else
		{
			// Rectangles are always the same height, so will be set in a loop every time:
			this.rectHeights	= new int[this.curNumInputs];
 
			// Setting xVals and yVals and width and height of rectangles:
			// Even number of inputs:
			if(this.curNumInputs % 2 == 0 && this.curNumInputs != 12)
			{
				this.rectWidths		= new int[this.curNumInputs];
				this.rectHeights	= new int[this.curNumInputs];
				for(int i = 0; i < this.rectWidths.length; i++)
				{
					this.rectWidths[i]	= this.width / (this.curNumInputs / 2);
					this.rectHeights[i]	= this.height / 2;
				} // for

				this.xVals	= new int[this.curNumInputs];
				this.yVals	= new int[this.curNumInputs];

				for(int i = 0; i < this.xVals.length; i++)
				{
					int xPos	= i % (this.curNumInputs / 2);
					int xVal	= xPos * (this.rectWidths[i]);
					xVals[i]	= xVal;
					System.out.println(i + ": xPos = " + xPos + "; xVal = " + xVal);
				} // for - xVals

				for(int i = 0; i < this.yVals.length; i++)
				{
					int	yPos	= i / (this.curNumInputs / 2);
					int	yVal	= yPos * this.rectHeights[i];
					yVals[i]	= yVal;
					System.out.println(i + ": yPos = " + yPos + "; yVal = " + yVal);
				} // for - yVals
			} // even number of inputs
			else if(this.curNumInputs == 1)
			{
				this.rectWidths		= new int[] {	this.width	};
				this.rectHeights	= new int[]	{	this.height	};

				this.xVals	= new int[] {	0	};
				this.yVals	= new int[] {	0	};
			} // 1
			else if(this.curNumInputs == 3)
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
			else if(this.curNumInputs == 5)
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
			else if(this.curNumInputs == 7)
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
			else if(this.curNumInputs == 9)
			{
				this.rectWidths		= new int[this.curNumInputs];
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
			else if(this.curNumInputs == 11)
			{
				this.rectWidths		= new int[this.curNumInputs];
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
			else if(this.curNumInputs == 12)
			{
				this.rectWidths		= new int[this.curNumInputs];
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
		}
		
	} // set Square Vals

} // Module
