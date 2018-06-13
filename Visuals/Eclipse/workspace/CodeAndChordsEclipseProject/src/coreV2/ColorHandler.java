package coreV2;

import controlP5.Button;
import core.ModuleMenuToSplitUp;

public class ColorHandler {
	

	/**	Holds the rgb values for all the colors, which will be updated by the ColorWheels of the current input num	*/
	protected	int[][][]	colors;
	
	/**	Current hue (as opposed to the goal hue, which may not have been reached)	 */
	private	int[][]			curHue;

	/**	Hue that corresponds to the current sound, but to which curHue may not yet have faded	*/
	private	int[][]			goalHue;
	
	/**	The color when sound is below the piano (lowest) threshold	*/
	protected	int[]		canvasColor;

	/**	The amount that must be added every 50 or so milliseconds to fade to the goal color	*/
	private	int[][]			colorAdd;

	/**	The difference between the R, G, and B values of 2 colors that are being faded between	*/
	private	int[][]			colorRange;

	/**	The current colors which hsb sliders are altering.	*/
	protected int[][][]   hsbColors;

	/**	Flags whether the curHue R, G, and B values have come within an acceptable range of the goalHue	*/
	private boolean[][]	colorReachedArray;

	/**	True if all values in the colorReachedArray are true; used to determine fade speed (whether this is attack, release, or transition)	*/
	private	boolean[]		colorReached;
	
	/**
	 * Colors roughly ROYGBIV; used for the rainbow() method.
	 */
	private final int[][][] rainbowColors	= new int[][][] { 
		new int[][] {
			{ 255, 0, 0 }, 
			{ 255, 0, 0 },
			{ 255, 127, 0 }, 
			{ 255, 255, 0 }, 
			{ 255, 255, 0 }, 
			{ 127, 255, 0 },
			{ 0, 255, 255 },
			{ 0, 255, 255 },
			{ 0, 255, 255 },  
			{ 0, 0, 255 },
			{ 127, 0, 255 },
			{ 127, 0, 255 }
		}, // major
		new int[][] {
			{ 255, 0, 0 }, 
			{ 255, 0, 0 },
			{ 255, 127, 0 }, 
			{ 255, 255, 0 }, 
			{ 255, 255, 0 }, 
			{ 127, 255, 0 },
			{ 0, 255, 255 },
			{ 0, 255, 255 },
			{ 0, 0, 255 },
			{ 0, 0, 255 },
			{ 127, 0, 255 },
			{ 127, 0, 255 }
		}, // minor
		new int[][] {
			{ 255, 0, 0 }, 
			{ 255, 127, 0 }, 
			{ 255, 255, 0 }, 
			{ 127, 255, 0 }, 
			{ 0, 255, 0 }, 
			{ 0, 255, 127 }, 
			{ 0, 255, 255 }, 
			{ 0, 127, 255 }, 
			{ 0, 0, 255 }, 
			{ 127, 0, 255 }, 
			{ 255, 0, 255 }, 
			{ 255, 0, 127 }
		} // chromatic
	}; // rainbowColors

	/**
	 * Each int signifies the position in dichromColors/trichromColors/rainbowColors that is used to fill
	 * this.colors at the corresponding position in scaleDegreeColors[this.majMinChrom]:
	 */
	private final	int[][]	scaleDegreeColors	= new int[][] {
		// major:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 4, 5, 6, 6 },
		// minor:
		new int[] { 0, 0, 1, 2, 2, 3, 4, 4, 5, 5, 6, 6 },
		// chromatic:
		new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 }
	}; // scaleDegreeColors

	/**
	 * This is the same idea as scaleDegreeColors, but each length 2 array indicates the starting and ending
	 * positions of the colors that are affected by a particular ColorWheel in a major or minor key
	 */
	protected	final	int[][][] colorPosStartEnd	=  new int [][][] {
		// major:
		new int[][] { 
			new int[] { 0, 1 }, 
			new int[] { 0, 1 }, 
			new int[] { 2, 2 },
			new int[] { 3, 4 },
			new int[] { 3, 4 },
			new int[] { 5, 5 },
			new int[] { 6, 8 },
			new int[] { 6, 8 },
			new int[] { 6, 8 },
			new int[] { 9, 9 },
			new int[] { 10, 11 },
			new int[] { 10, 11 },
		},
		// minor:
		new int[][] { 
			new int[] { 0, 1 }, 
			new int[] { 0, 1 }, 
			new int[] { 2, 2 },
			new int[] { 3, 4 },
			new int[] { 3, 4 },
			new int[] { 5, 5 },
			new int[] { 6, 7 },
			new int[] { 6, 7 },
			new int[] { 8, 9 },
			new int[] { 8, 9 },
			new int[] { 10, 11 },
			new int[] { 10, 11 },
		},
		// chromatic:
		new int[][] { 
			new int[] { 0, 0 }, 
			new int[] { 1, 1 }, 
			new int[] { 2, 2 },
			new int[] { 3, 3 },
			new int[] { 4, 4 },
			new int[] { 5, 5 },
			new int[] { 6, 6 },
			new int[] { 7, 7 },
			new int[] { 8, 8 },
			new int[] { 9, 9 },
			new int[] { 10, 10 },
			new int[] { 11, 11 },
		}
	}; // colorPosStartEnd

	/**
	 * Color Styles: Rainbow, Dichromatic (fade from one color to another throughout the legend),
	 * Trichromatic (fade from color1 to  color2 and then from color2 to color3 throughout the legend),
	 * or Custom, which may be discontinued.
	 */
	public	static	int	CS_RAINBOW	= 0;
	public	static	int	CS_DICHROM	= 1;
	public	static	int	CS_TRICHROM	= 2;
	public	static	int	CS_CUSTOM	= 3;
	protected	int[]	curColorStyle;
	
	public ColorHandler(int numInputs, int totalNumColorItems) 
	{
		this.colors				= new int[numInputs][totalNumColorItems][3];
		this.hsbColors			= new int[numInputs][totalNumColorItems][3];
		this.curHue				= new int[numInputs][3];
		this.goalHue			= new int[numInputs][3];
		this.canvasColor		= new int[] { 1, 0, 0 };	// If this is set to rgb(0, 0, 0), the CW gets stuck in grayscale
		this.curColorStyle		= new int[numInputs];
		this.rainbow();
	} // constructor
	
	/**
	 * Populates colors with rainbow colors (ROYGBIV - with a few more for chromatic scales).
	 */
	protected void rainbow()
	{
		for(int i = this.moduleDriver.getStartHere(); i < this.moduleDriver.getEndBeforeThis(); i++)
		{
			for(int j = 0; j < this.colors[i].length; j++)
			{
				this.colors[i][j][0]	= this.rainbowColors[this.majMinChrom][j][0];
				this.colors[i][j][1]	= this.rainbowColors[this.majMinChrom][j][1];
				this.colors[i][j][2]	= this.rainbowColors[this.majMinChrom][j][2];
			} // for - i (going through colors)
		}

		this.fillHSBColors();
	} // rainbow
	

	/**
	 * 
	 * @param enharmonicKeyPos
	 */
	public void updateCustomColorButtonLabels(int enharmonicKeyPos)
	{
		for(int i = 0; i < this.totalRangeSegments; i++)
		{
			this.newNoteNames[i]	= this.noteNames[(enharmonicKeyPos + i) % this.newNoteNames.length];
		} // for - fill newNoteNames

		for(int i = 0; i < this.newNoteNames.length; i++)
		{
			((Button)this.controlP5.getController("button" + (this.firstColorSelectCWId - 100 + i))).setLabel(this.newNoteNames[i]);
		} // for
	} // updateCustomColorButtonLabels

	/**
	 * Sets the ColorStyle to the given ColorStyle
	 * and locks or unlocks the appropriate Buttons (e.g., in Rainbow, Tonic, 2ndColor and 3rdColor
	 * Buttons should all be unlocked, but for Dichromatic, only 3rdColor should be locked).
	 * 
	 * @param newColorStyle	int between 1 and 4 indicating the new ColorStyle
	 * @param inputNum	the input for which to change the colorStyle
	 */
	public void setColorStyle(int newColorStyle, int inputNum)
	{
		if(inputNum >= this.curColorStyle.length || inputNum < 0)
		{
			throw new IllegalArgumentException("ModuleMenu.setColorStyle: inputNum " + inputNum + 
					" is outside the acceptable range; must be between 0 and " + this.curColorStyle.length + ".");
		}
		this.curColorStyle[inputNum]	= newColorStyle;

		// Rainbow:
		if(this.curColorStyle[inputNum] == ModuleMenuToSplitUp.CS_RAINBOW)
		{
			//	if avoids errors during instantiation:
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	
			{	
				this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).lock();	
			}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)		
			{	
				this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).lock();	
			}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)		
			{	
				this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	
			}

			this.rainbow();
		} // if - rainbow

		// Dichromatic:
		if(this.curColorStyle[inputNum] == ModuleMenuToSplitUp.CS_DICHROM)
		{
			//			this.specialColorsPos[inputNum][0]	= 0;

			// Determine the correct position for 2nd Color (minor scale will use a whole step before the tonic,
			// but major and chromatic use the leading tone):
			int	colorPos2;

			colorPos2	= this.colors[inputNum].length - 1;

			// For minor keys, choose the 2nd to last note; else choose the last note:
			/*			if(this.majMinChrom == 1)	{	colorPos2	= this.colors[inputNum].length - 2;	}
				else						{	colorPos2	= this.colors[inputNum].length - 1;	}
			 */
			// Lock/unlock the appropriate specialColors Buttons:
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).lock();	}

			// First time to dichromatic, dichromFlag will be false, 
			// and the two colors will be set to contrast.			
			if(!this.dichromFlag)
			{
				this.dichromatic_OneRGB(this.colors[inputNum][0], inputNum);					

				this.dichromFlag	= true;
			} // first time
			// After the fjrst tjme, use current color values
			// (allows selection of 2nd color):
			else
			{
				// 1/12: Not doing this so that setting a color and then calling Dichrom will go from the newly set color:
				// Put the previous "2nd Color" into our new "2nd Color" position in colors, so that we can get the color directly from colors:
				//					this.colors[inputNum][colorPos2]	= this.colors[inputNum][this.specialColorsPos[inputNum][1]];

				// Update specialColors positions for this scale:
				this.specialColorsPos[inputNum][0]	= 0;
				this.specialColorsPos[inputNum][1]	= colorPos2;

				this.dichromatic_TwoRGB(this.colors[inputNum][0], this.colors[inputNum][this.specialColorsPos[inputNum][1]], inputNum);
			}

		} // Dichromatic

		// Trichromatic:
		if(this.curColorStyle[inputNum] == ModuleMenuToSplitUp.CS_TRICHROM)
		{

			int	colorPos2	= 4;	// initializing for the first call
			int	colorPos3	= 8;

			// Turned off the "first time/remaining times" because it's still pretty interesting
			// and, I think, more intuitive, coming off of another color.  Dichromatic is boring coming off rainbow.
			// first time trichromatic has been called:
			if(!this.trichromFlag)
			{
				this.trichromatic_OneRGB(this.colors[inputNum][0], inputNum);
				this.trichromFlag	= true;
			}
			// every other time:
			else
			{
				if(this.majMinChrom == 2)
				{
					colorPos2	= 4;
					colorPos3	= 8;
				} else {
					// Positions have to be 5 and 7, not 3 and 4, since colors is filled all the way and we just ignore
					// non-diatonic tones, so 5 and 7 actually corresponds to the mediant and dominant scale degrees.

					colorPos2	= 5;
					colorPos3	= 7;
				} // else - colorPos for different scales

			} // else - all but the first time

			//				System.out.println("trichrom: setting colors[" + i + "][" + colorPos2 + "] to the color at position " + this.specialColorsPos[i][1] + 
			//						": rgb(" + this.colors[i][this.specialColorsPos[i][1]][0] + ", " + this.colors[i][this.specialColorsPos[i][1]][1] + ", " + this.colors[i][this.specialColorsPos[i][1]][2] + ")");
			//				System.out.println("trichrom: setting colors[" + i + "][" + colorPos3 + "] to the color at position " + this.specialColorsPos[i][2] + 
			//						": rgb(" + this.colors[i][this.specialColorsPos[i][2]][0] + ", " + this.colors[i][this.specialColorsPos[i][2]][1] + ", " + this.colors[i][this.specialColorsPos[i][2]][2] + ")");

			// Have to do this so that changing between keys doesn't change the specialColors:
			this.colors[inputNum][colorPos2]	= this.colors[inputNum][this.specialColorsPos[inputNum][1]];
			this.colors[inputNum][colorPos3]	= this.colors[inputNum][this.specialColorsPos[inputNum][2]];

			this.specialColorsPos[inputNum][0]	= 0;
			this.specialColorsPos[inputNum][1]	= colorPos2;
			this.specialColorsPos[inputNum][2]	= colorPos3;

			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 100)).unlock();	}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 99)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId  - 99)).unlock();	}
			if(this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)) != null)	{	this.controlP5.getController("button" + (this.firstSpecialColorsCWId - 98)).unlock();	}

			this.trichromatic_ThreeRGB(this.colors[inputNum][0], this.colors[inputNum][colorPos2], this.colors[inputNum][colorPos3], inputNum);
		} // Trichromatic

	} // setColorStyle
	
	public int getColorStyle(int inputNum) {
		this.inputNumErrorCheck(inputNum);
		
		return this.curColorStyle[inputNum];
	} // getColorStyle

} // ColorHandler
>>>>>>> library
