package coreV2;

import java.awt.Color;

import core.Instrument;
import core.Melody;
import processing.core.PApplet;

public class SensitivityMenu extends MenuTemplate
{
	
	/**	Amplitude thresholds	*/
	protected	int[][]	thresholds;

	/**	The minimum value for threshold Sliders	*/
	protected	int	minThreshold;

	/**	The lowest threshold; sounds below this will be ignored	*/
	protected	int[]	pianoThreshold;

	/**	The highest amplitude threshold	*/
	protected	int[]	forteThreshold;

	/**	Holds the values of the saturation threshold and brightness threshold Sliders, respectively	*/
	protected	float[][] satBrightThresholdVals;

	/**	Holds the values of the saturation percent and brightness percent threshold Sliders, respectively	*/
	protected	float[][]	satBrightPercentVals;
	
	/**	Stores the values of the attack, release, and transition sliders	*/
	private	float[][]	attRelTranVals;
	
	/**	Attack, Release, or Transition - 0 = attack, 1 = release, 2 = transition	*/
	private	int[]		attRelTranPos;
	
	/**	Melody object that guide tones will use to play scales	*/
	protected Melody		melody;

	/**	Letter (and '#' or 'b', if necessary) designating the current key	*/
	protected String		curKey;

	/**	The current key is this many keys away from "A" (the first in allNotes)	*/
	protected int 		curKeyOffset;

	/**	The current key is this many letter names away from "A," with enharmonic keys (e.g., C# and Db) together counting for one position	*/
	protected int 		curKeyEnharmonicOffset;

	/**	Length of the current scale (chromatic is longer than major and minor)	*/
	protected	int			scaleLength;

	/**	Current beats per minute	*/
	protected	int			bpm;

	/**	Current scale quality: 0 = major, 1 = minor, 2 = chromatic	*/
	protected int 		majMinChrom;

	/**	The current octave of the guide tones	*/
	protected int			rangeOctave;

	/**	Instrument object used to play the guide tone scales	*/
	protected Instrument	instrument;



	public SensitivityMenu() 
	{
		super();
	}

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buttonEvent(int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawMenu() {
		// TODO Auto-generated method stub
		
	}
}
