package coreV2;

import java.util.ArrayList;

/*
 * TODO: 
 * - Make pitchColors an int[][].  Full functionality of ColorWrapper is not needed.
 * - Make ColorWrapper an inner class
 * - Make piano threshold and incorporate alpha values
 * - Add forte threshold
 * - Add legend
 * - Implement sensitivity menu
 * - Implement color menu
 * - Add more to the colorPresetsLibrary
 */

public class ColorHandler2 
{

	private ModuleDriver 					driver;
	private int								inputNum;

	private ColorWrapper					currentColor;

	private ColorWrapper[]					pitchColors;



	private ArrayList<PitchColorScheme> 	colorPresetsLibrary;

	public final int 						RAINBOW_CHROMATIC 	= 0;
	public final int 						RAINBOW_MAJOR 		= 1;
	public final int 						RAINBOW_MINOR		= 2;



	public ColorHandler2(ModuleDriver driver, int inputNum)
	{
		this.driver = driver;
		this.initializeColorLibrary();
		
		int curNumInputs = this.driver.getInputHandler().getCurNumRealTimeInputs();

		this.pitchColors = new ColorWrapper[12];
		this.setPitchColors(this.RAINBOW_CHROMATIC);
		
		this.currentColor = new ColorWrapper(0,0,0);

	}
	
	public void setPitchColors(int colorSchemePreset)
	{
		for(int i = 0; i < this.pitchColors.length; i++)
		{
			this.pitchColors[i] = this.colorPresetsLibrary.get(colorSchemePreset).getColor(i);
		}
	}
	
	public void fadeColor()
	{
		int midiNote = (int) this.driver.getInputHandler().getMidiNote(this.inputNum);
		midiNote = midiNote % 12;
		
		this.currentColor.fadeColor(this.pitchColors[midiNote].getColor(), 5);
	}
	
	public int[] getCurrentColor()
	{
		return this.currentColor.getColor();
	}

	private class PitchColorScheme
	{
		private ColorWrapper[] pitchColors;

		private PitchColorScheme()
		{
			this.pitchColors = new ColorWrapper[12];
		}

		private void addColor(int scaleDegree, ColorWrapper color)
		{
			this.pitchColors[scaleDegree] = color;
		}
		
		private ColorWrapper getColor(int scaleDegree)
		{
			return this.pitchColors[scaleDegree];
		}
	}

	private void initializeColorLibrary()
	{
		this.colorPresetsLibrary = new ArrayList<PitchColorScheme>(1);
		
		
		PitchColorScheme rainbowChromatic = new PitchColorScheme();

		rainbowChromatic.addColor(0, new ColorWrapper(new int[] { 255, 0, 0 }));
		rainbowChromatic.addColor(1, new ColorWrapper(new int[] { 255, 127, 0 }));
		rainbowChromatic.addColor(2, new ColorWrapper(new int[] { 255, 255, 0 }));
		rainbowChromatic.addColor(3, new ColorWrapper(new int[] { 127, 255, 0 }));
		rainbowChromatic.addColor(4, new ColorWrapper(new int[] { 0, 255, 0 }));
		rainbowChromatic.addColor(5, new ColorWrapper(new int[] { 0, 255, 127 }));
		rainbowChromatic.addColor(6, new ColorWrapper(new int[] { 0, 255, 255 }));
		rainbowChromatic.addColor(7, new ColorWrapper(new int[] { 0, 127, 255 }));
		rainbowChromatic.addColor(8, new ColorWrapper(new int[] { 0, 0, 255 }));
		rainbowChromatic.addColor(9, new ColorWrapper(new int[] { 127, 0, 255 }));
		rainbowChromatic.addColor(10, new ColorWrapper(new int[] { 255, 0, 255 }));
		rainbowChromatic.addColor(11, new ColorWrapper(new int[] { 255, 0, 127 }));

		this.colorPresetsLibrary.add(this.RAINBOW_CHROMATIC, rainbowChromatic);
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
