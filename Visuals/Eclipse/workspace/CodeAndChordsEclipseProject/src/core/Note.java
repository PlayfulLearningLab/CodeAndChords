package core;

public class Note {
	
	/**
	 * 06/12/2017
	 * Emily Meuer
	 * 
	 * Class to create Notes that will be used to make a Melody.
	 */
	
	private	int		midiNum;	// the midi number of the note
	private	float	duration;	// the length of the note in milliseconds
	private	float	amplitude;	// the amplitude of the note in undefined measure; not yet implemented
	private	boolean	isRest;		// true if the note is a rest
	
	/**
	 * Creates a new Note with the given midiNum, duration, and amplitude and sets isRest to false.
	 * 
	 * @param midiNum	int denoting the midi number of the Note
	 * @param duration	float denoting the length of the Note in milliseconds
	 * @param amplitude float denoting the amplitude of the Note (in unknown measurement - TODO?)
	 */
	public Note(int midiNum, float duration, float amplitude)
	{
		this(midiNum, duration, amplitude, false);
	}
	
	/**
	 * Creates a new Note with the given midiNum, duration, amplitude, and isRest.
	 * 
	 * @param midiNum	int denoting the midi number of the Note
	 * @param duration	float denoting the length of the Note in milliseconds
	 * @param amplitude float denoting the amplitude of the Note (in unknown measurement - TODO?)
	 * @param isRest	boolean that is true when the Note is a rest and false otherwise
	 */
	public Note(int midiNum, float duration, float amplitude, boolean isRest)
	{
		this.midiNum	= midiNum;
		this.duration	= duration;
		this.amplitude	= amplitude;
		this.isRest		= isRest;
	}
	
	public int getMidiNum()		{	return this.midiNum;	}
	
	public float getDuration()	{	return this.duration;	}
	
	public float getAmplitude()	{	return this.amplitude;	}
	
	public boolean	isRest()	{	return this.isRest;		}

}
