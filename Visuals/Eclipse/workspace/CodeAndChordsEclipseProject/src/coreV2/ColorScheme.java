package coreV2;

import java.awt.Color;
import java.util.ArrayList;

/*
 * TODO: Change this to be ColorScheme instead of ColorHandler and make a singleton library for color scheme presets
 * 
 * 
 */

public class ColorScheme 
{
	private ModuleDriver 					driver;
	
	private int[]							canvasColor;

	private int [][]						pitchColors;

	//  int[preset number][note number][RGB]
	private int[][][]						colorPresetsLibrary;

	public final int 						RAINBOW_CHROMATIC 	= 0;
	public final int 						RAINBOW_MAJOR 		= 1;
	public final int 						RAINBOW_MINOR		= 2;


	public ColorScheme(ModuleDriver driver)
	{
		this.driver = driver;
		
		this.initializeColorLibrary();

		this.pitchColors = new int [12][3];
		this.setPitchColors(this.RAINBOW_CHROMATIC);

		this.canvasColor = new int[] {0, 0, 0};

	}

	public void setPitchColors(int colorSchemePreset)
	{
		for(int i = 0; i < this.pitchColors.length; i++)
		{
			this.pitchColors[i] = this.colorPresetsLibrary[colorSchemePreset][i].clone();
		}
	}
	
	public int[] getPitchColor(int midiNote)
	{
		int[] result; 
		int[] scale = this.driver.getInputHandler().getScale();
		
		if(this.driver.getColorMenu().contains(midiNote%12, scale) == false)
		{
			//not in scale
			result=  new int[] {this.canvasColor[0], this.canvasColor[1], this.canvasColor[2], 0};
			//System.out.println("alpha = 0");
		}
		else
		{
			//if in scale
			result=  new int[] {this.pitchColors[midiNote%12][0], this.pitchColors[midiNote%12][1], this.pitchColors[midiNote%12][2], 255};
			//System.out.println("alpha = 255");
		}
		return result;
	}
	
	
	public int[] getCanvasColor()
	{
		return this.canvasColor;
	}
	
	public void setColor(int colorNumber, int r, int g, int b)
	{
		this.pitchColors[colorNumber][0] = r;
		this.pitchColors[colorNumber][1] = g;
		this.pitchColors[colorNumber][2] = b;
	}
	
	public void setColor(int colorNumber, int RGB)
	{
		Color newColor = new Color(RGB);
		
		this.setColor(colorNumber, newColor.getRed(), newColor.getGreen(), newColor.getBlue());
	}


	private void initializeColorLibrary()
	{
		this.colorPresetsLibrary = new int[3][12][3];
	//  int[preset number][note number][RGB] colorPresetsLibrary[][][]
		this.colorPresetsLibrary[0][0] = new int[] { 255, 0, 0 };
		this.colorPresetsLibrary[0][1] = new int[] { 255, 127, 0 };
		this.colorPresetsLibrary[0][2] = new int[] { 255, 255, 0 };
		this.colorPresetsLibrary[0][3] = new int[] { 127, 255, 0 };
		this.colorPresetsLibrary[0][4] = new int[] { 0, 255, 0 };
		this.colorPresetsLibrary[0][5] = new int[] { 0, 255, 127 };
		this.colorPresetsLibrary[0][6] = new int[] { 0, 255, 255 };
		this.colorPresetsLibrary[0][7] = new int[] { 0, 127, 255 };
		this.colorPresetsLibrary[0][8] = new int[] { 0, 0, 255 };
		this.colorPresetsLibrary[0][9] = new int[] { 127, 0, 255 };
		this.colorPresetsLibrary[0][10] = new int[] { 255, 0, 255 };
		this.colorPresetsLibrary[0][11] = new int[] { 255, 0, 127 };



		/*

		PitchColorScheme rainbowChromatic = new PitchColorScheme();

		rainbowChromatic.addColor(0, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(1, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(2, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(3, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(4, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(5, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(6, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(7, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(8, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(9, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(10, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(11, new ColorWrapper(new int[] { 255, 0, 0 }));

		this.colorPresetsLibrary.add(this.RAINBOW_CHROMATIC, rainbowChromatic);


		PitchColorScheme rainbowChromatic = new PitchColorScheme();

		rainbowChromatic.addColor(0, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(1, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(2, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(3, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(4, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(5, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(6, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(7, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(8, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(9, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(10, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(11, new ColorWrapper(new int[] { 255, 0, 0 }));

		this.colorPresetsLibrary.add(this.RAINBOW_CHROMATIC, rainbowChromatic);

		 */


	}
	


}
