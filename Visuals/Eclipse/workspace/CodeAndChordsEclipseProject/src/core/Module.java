package core;

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
	protected	Input			input;
	
	protected	Shape			shape;
	
	/**	For Modules with a Shape, this ShapeEditor provides Shape customization Controllers	*/
	protected 	ShapeEditor		shapeEditor;
	
	/**	"Sidebar" Menu, where most basic Controllers will be - global HSB and RGB modulation, etc.	*/
	protected	ModuleMenu		menu;
	
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
	public void legend(int goalHuePos)
	{
		this.textSize(24);		

		String[]	legendText	= this.getLegendText();
		
		float	sideWidth1   = (this.width - this.menu.getLeftEdgeX()) / legendText.length;
		float	sideHeight  = this.width / 12;	// pretty arbitrary
		float	addToLastRect	= (this.width - this.menu.getLeftEdgeX()) - (sideWidth1 * legendText.length);
		float	sideWidth2	= sideWidth1;

		this.noStroke();
		
		int	scaleDegree;
		int	colorPos;

//		for (int i = 0; i < this.thresholds.length; i++)
		for (int i = 0; i < legendText.length; i++)
		{
			if(i == legendText.length - 1)
			{
				sideWidth2	= sideWidth1 + addToLastRect;
			}
			
			// colors is filled all the way and only picked at the desired notes:
						scaleDegree	= this.scaleDegrees[this.menu.getMajMinChrom()][i];
						colorPos	= scaleDegree;
			this.fill(this.menu.getColor(colorPos)[0], this.menu.getColor(colorPos)[1], this.menu.getColor(colorPos)[2]);


			if (i == goalHuePos) {
				this.rect(this.menu.getLeftEdgeX() + (sideWidth1 * i), (float)(this.height - (sideHeight * 1.5)), sideWidth2, (float) (sideHeight * 1.5));
			} else {
				this.rect(this.menu.getLeftEdgeX() + (sideWidth1 * i), this.height - sideHeight, sideWidth2, sideHeight);
			}

			this.fill(0);
			this.text(legendText[i], (float) (this.menu.getLeftEdgeX() + (sideWidth1 * i) + (sideWidth1 * 0.3)), this.height - 20);
		} // for

	} // legend
	
	/**
	 * @return	String[] of text to display in each position of the legend
	 */
	public abstract String[] getLegendText();

} // Module
