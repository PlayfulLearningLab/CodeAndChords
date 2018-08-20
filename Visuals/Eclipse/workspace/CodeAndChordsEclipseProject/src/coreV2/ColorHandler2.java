package coreV2;

import java.util.ArrayList;

/*
 * TODO: Change this to be ColorScheme instead of ColorHandler and make a singleton library for color scheme presets
 * 
 * 
 */

public class ColorHandler2 
{
	private ModuleDriver 					driver;
	private int								inputNum;

	private ColorWrapper					currentColor;
	private int[]							canvasColor;

	private int [][]						pitchColors;

	private int[][][]						colorPresetsLibrary;

	public final int 						RAINBOW_CHROMATIC 	= 0;
	public final int 						RAINBOW_MAJOR 		= 1;
	public final int 						RAINBOW_MINOR		= 2;


	private int								pianoThreshold;
	private int								curMidiNote;


	public ColorHandler2(ModuleDriver driver, int inputIndexNumber)
	{
		this.driver = driver;
		this.initializeColorLibrary();

		this.pitchColors = new int [12][3];
		this.setPitchColors(this.RAINBOW_CHROMATIC);

		this.currentColor = new ColorWrapper(0, 0, 0, this.driver.getParent());

		this.pianoThreshold = 1;
		this.canvasColor = new int[] {0, 0, 0};

		this.inputNum = inputIndexNumber;


	}

	public void setPitchColors(int colorSchemePreset)
	{
		for(int i = 0; i < this.pitchColors.length; i++)
		{
			this.pitchColors[i] = this.colorPresetsLibrary[colorSchemePreset][i];
		}
	}

	public void setColorToMatchPitch()
	{
		int midiNote = (int) this.driver.getInputHandler().getMidiNote(this.inputNum);
		midiNote = midiNote % 12;

		if(midiNote != this.curMidiNote)
		{
			if(this.driver.getInputHandler().getAmplitude(0) > this.pianoThreshold)
			{
				this.currentColor.setTargetColor(this.pitchColors[midiNote]);
				this.curMidiNote = midiNote;
			}
			else
			{
				this.currentColor.setTargetColor(this.canvasColor);
				this.curMidiNote = midiNote;
			}
		}//if(is new midi note)


	}

	public int[] getCurrentColor()
	{
		return this.currentColor.getColor();
	}


	private void initializeColorLibrary()
	{
		this.colorPresetsLibrary = new int[3][12][3];

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
