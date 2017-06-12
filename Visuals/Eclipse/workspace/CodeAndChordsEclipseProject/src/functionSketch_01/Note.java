package functionSketch_01;

public class Note {
	
	/**
	 * 06/12/2017
	 * Emily Meuer
	 * 
	 * Class to create Notes that will be used to make a Melody.
	 */
	
	private	int		midiNum;
	private	float	duration;
	private	float	amplitude;
	
	/**
	 * Creates a new Note with the given midiNum, duration, and amplitude.
	 * 
	 * @param midiNum	int denoting the midi number of the Note
	 * @param duration	float denoting the length of the Note in milliseconds
	 * @param amplitude float denoting the amplitude of the Note (in unknown measurement - TODO?)
	 */
	public Note(int midiNum, float duration, float amplitude)
	{
		this.midiNum	= midiNum;
		this.duration	= duration;
		this.amplitude	= amplitude;
	}
	
	public int getMidiNum()		{	return this.midiNum;	}
	
	public float getDuration()	{	return this.duration;	}
	
	public float getAmplitude()	{	return this.amplitude;	}

}
