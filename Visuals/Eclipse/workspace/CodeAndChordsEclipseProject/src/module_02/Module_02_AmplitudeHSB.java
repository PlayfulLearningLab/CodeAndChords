package module_02;

//import core.FullScreenDisplay;
//import core.Input;
import core.Module;
import core.ModuleMenu;
import processing.core.PApplet;
import core.Shape;
import core.ShapeEditor;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;

public class Module_02_AmplitudeHSB extends Module /*implements ShapeEditorInterface */{

	/**	holds the y values for all Controllers	*/
	private	int[]	yVals;

	public static void main(String[] args) 
	{
		PApplet.main("module_02.Module_02_AmplitudeHSB");
	} // main

	public void settings()
	{
		//fullScreen();
		size(925, 520);
	} // settings

	public void setup()
	{
		// Not specifying an AudioContext will use the PortAudioAudioIO:
		//		this.input	= new Input(this);
		this.input    = new RealTimeInput(1, new AudioContext(), this);
		this.totalNumEvents	= 1;
		this.curNumEvents	= 1;

		this.menu	= new ModuleMenu(this, this, this.input, 6);

				this.yVals		= new int[18];
				// Seemed like a good starting position, related to the text - but pretty arbitrary:
				this.yVals[0]	= 50;
				int	distance	= (this.height - this.yVals[0]) / this.yVals.length;
				for(int i = 1; i < this.yVals.length; i++)
				{
					this.yVals[i]	= this.yVals[i - 1] + distance;
				} // fill yVals

				// Have to addColorSelect() first so that everything else can access the colors:
				String[]	buttonLabels	= new String[] {
						"Canvas", "1", "2", "3", "4", "5", "6"
				};

				this.menu.addColorMenu(buttonLabels, 1, true, null, false, false, "Dynamic\nSegments", 6, 6, "color");
				this.menu.addSensitivityMenu(false);

				this.menu.addShapeSizeSlider(0, this.yVals[15]);

				// Have to do this last so that the manually added Slider (above) doesn't get an id already used by the ShapeEditor:
				this.menu.addShapeMenu(1);
			
				this.menu.getInstrument().setADSR(1000, 500, 0, 0);
				this.menu.setBPM(30);

//				this.textSize(32);

	} // setup

	public void draw()
	{
//		System.out.println("this.input.getAmplitude() = " + this.input.getAmplitude());

		// The following line is necessary so that key press shows the menu button
		if (keyPressed == true && !this.menu.getIsRunning() && !this.menu.getShapeEditor().getIsRunning()) 
		{
			this.menu.setMenuVal();
		}

		background(this.menu.getCanvasColor()[0], this.menu.getCanvasColor()[1], this.menu.getCanvasColor()[2]);

		// pick the appropriate color by checking amplitude threshold
		float	curAmp		= this.input.getAmplitude();
		int		goalHuePos	= 0;

		for(int i = 0; i < this.menu.getCurRangeSegments(); i++)
		{
			if(curAmp > this.menu.getThresholds()[0][i]) {
				goalHuePos	= i;
			} // if
		} // for

		//		System.out.println("curAmp " + curAmp + " was over thresholds[" + goalHuePos + "]: " + this.moduleTemplate.getThresholds()[goalHuePos]);

		this.menu.fade(goalHuePos, 0);

		/*
		 * need:
		 * 	drawShape()  		- draws the main shape
		 * 	runSE()     		- should be run every cycle, draws shape editor
		 * 	legend(goalHuePos)	- draws legend
		 * 
		 * 	if(this.moduleTemplate.isShowScale()
		 */

		this.menu.getShapeEditor().getShape().setShapeScale(this.menu.getShapeSize());

		if(!this.menu.getShapeEditor().getIsRunning())
		{
			this.menu.getShapeEditor().drawShape(0);
		}

		if(this.menu.isShowScale() && !this.menu.getShapeEditor().getIsRunning())
		{
			// draws the legend along the bottom of the screen:
			this.legend(goalHuePos, 0);

		} // if showScale

		// ShapeEditor.runMenu now included in menu.runMenu:
//		this.menu.shapeEditor.runMenu();
		this.menu.runMenu();

	} // draw

	@Override
	public String[] getLegendText()
	{
		String[]	result	= new String[this.menu.getCurRangeSegments()];
		for(int i = 0; i < result.length; i++)
		{
			result[i]	= this.menu.getThresholds()[0][i] + "";
		} // for

		return result;
	} // fillLegendText

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
		
		//TODO: Is the hamburger button in a ControlP5 object not in this if statement? -- yes, it's in the menu.getOutsideButtonsCP5().
		if(!this.menu.getShapeEditor().getControlP5().isMouseOver() && !this.menu.getControlP5().isMouseOver() && !this.menu.getOutsideButtonsCP5().isMouseOver())
		{
			ShapeEditor	shapeEditor	= this.menu.getShapeEditor();
			Shape	shape	= shapeEditor.getShapes()[0];
			
			// Map if running:
			if(this.menu.getShapeEditor().getIsRunning() || this.menu.getIsRunning())
			{
				shape.setXPos( shapeEditor.mapFullAppletXPos( Math.max( shapeEditor.mapAdjustedMenuXPos(0), Math.min(this.mouseX, shapeEditor.mapFullAppletXPos(this.width) ) ) ) );
				shape.setYPos( shapeEditor.mapFullAppletYPos( Math.max( shapeEditor.mapAdjustedMenuYPos(0), Math.min(this.mouseY, shapeEditor.mapFullAppletYPos(this.height) ) ) ) );
				shapeEditor.updateSliders();
			}
			else
			{
				// If neither are running, just keep w/in bounds:
				shape.setXPos( Math.max( 0, Math.min(this.mouseX, this.width) ) );
				shape.setYPos( Math.max( 0, Math.min(this.mouseY, this.height) ) );
				shapeEditor.updateSliders();
			}
		} // if - not over either ControlP5
	} // mouseClicked
} // Module_03_AmplitudeHSB
