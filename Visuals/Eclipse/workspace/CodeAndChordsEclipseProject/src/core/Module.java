package core;

import processing.core.PApplet;

public abstract class Module extends PApplet {
	
//	private		DisposeHandler	disposeHandler;
	protected	Input			input;
	protected	Shape			shape;
	protected 	ShapeEditor		shapeEditor;
	protected	ModuleMenu		menu;
	
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

	public void settings()
	{
		size(925, 520);
	}
	
	public void setup()
	{
//		this.disposeHandler	= new DisposeHandler(this, this);
		
//		System.out.println("Module: hi! disposeHandler.module.input = " + this.disposeHandler.module.input);
	} // setup
	

	public void setShapeEditorRunning(boolean isRunning)
	{
		this.shapeEditor.setIsRunning(isRunning);
	} // setShapeEditorRunning
	
	public Shape getShape() {
		return this.shape;
	} // getShape
	

	/**
	 * ** ModuleTemplate02's legend!!!
	 * 
	 * Draws the thresholds legend at the bottom of the screen.
	 * 
	 * @param goalHuePos	current position in the threshold list; used to show the user their general amplitude level
	 */
	public void legend(int goalHuePos)
	{
		this.textSize(24);		

		String[]	legendText	= this.getLegendText();

		System.out.println("this.width = " + this.width + "; this.menu.getLeftEdgeX() = " + this.menu.getLeftEdgeX());
		
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
	
	public abstract String[] getLegendText();

} // Module
