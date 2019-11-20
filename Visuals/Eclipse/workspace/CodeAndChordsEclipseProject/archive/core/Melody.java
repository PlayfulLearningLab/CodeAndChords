package core;

import java.util.*;

import core.input.Input;
import core.input.MicrophoneInput;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import processing.core.PApplet;

/**
 * 06/12/2017
 * 
 * Class to create a melody of Notes and play it by calling Instrument's 
 * play(Note) method with them at the appropriate time.
 * 
 * @author Dan Mahota, Emily Meuer
 *
 */
public class Melody implements Runnable {

	private static HashMap<String, float[][]>	melodyLib  = Melody.initializeLib();
	private	static String[]						keys	= new String[] { 
			" C ", " C# / Db ", " D "," D# / Eb ", " E ", " F ", " F# / Gb "," G "," G# / Ab ", " A "," A# / Bb "," B " 
	};
	private	static String[]						allNotes	= new String[] {
			"A", "A#", "Bb", "B", "B#", "C", "C#", "Db", "D", "D#", "Eb", "E", "E#", "F", "F#", "Gb", "G", "G#", "Ab"
	}; // allNotes

	// Input is not required; if initialized in the constructor, it will be used, but if not, ignored.
	private	Input								input	= null;
	// TODO: make private:
	public Instrument							instrument;
	private	PApplet								parent;

	private String                              key;
	private int                                 bpm;
	// TODO: make private:
	public String                              scale;
	private int                                 rangeOctave;

	private	String[]							rangeList;

	private boolean                             melodyThreadRunning;
	private boolean                             paused;

	private AudioContext                        ac;
	private Glide                               volumeControl;
	private Gain                                volume;
	private Bead                                bead;


	/**
	 * Constructor for using Melody independently of an Input
	 * 
	 * @param parent
	 */
	public Melody(PApplet parent)
	{
		// Simply sends null to initialize the Input:
		this(parent, null);
	} // constructor(PApplet)


	/**
	 * Constructor for use with an Input that allows the user to provide their Instrument of choice
	 * 
	 * @param parent PApplet for this sketch
	 * @param input2	Input that will use the frequencies generated by playMelody()
	 */
	public Melody(PApplet parent, Input input2)
	{
		if(parent == null)
		{
			throw new IllegalArgumentException("Melody constructor(PApplet, Input, Instrument): PApplet parameter is null.");
		}

		if(input2 == null)
		{
			System.out.println("Melody constructor(PApplet, Input, Instrument): Input parameter is null, so there will be no Input associated with this Melody.");
		}

		this.parent		= parent;
		this.input		= input2;

		// Defaults, to be safe; will be given values in playMelody:
		this.key			= " A ";
		this.bpm			= 120;
		this.scale			= "major";
		this.rangeOctave	= 4;
		this.rangeList		= new String[3];
	} // constructor(PApplet, Input)

	/**
	 * Calls the other playMelody with the Instrument instance variable, instead of requiring one from the user.
	 * 
	 * @param key	String denoting the current key; do not include major or minor designation here
	 * @param bpm	tempo in beats per minute
	 * @param scale		either the String "major", "minor", or "chromatic"
	 * @param rangeOctave	int giving the range octave
	 */
	public Instrument playMelody(String key, float bpm, String scale, int rangeOctave)
	{
		this.instrument = new Instrument(parent, this.input.getAudioContext());
		this.playMelody(key, bpm, scale, rangeOctave, this.instrument);
		return this.instrument;
	}

	/**
	 * Sets the instance variables with the same names to the values of the parameters,
	 * then starts the thread that will send them to the playMelodyThread method.
	 * 
	 * @param key	String denoting the current key; do not include major or minor designation here
	 * @param bpm	tempo in beats per minute
	 * @param scale	either the String "major", "minor", or "chromatic"
	 * @param rangeOctave	int giving the range octave; must be between 3 and 6
	 * @param instrument	Instrument that will play the Melody
	 */
	public void playMelody(String key, float bpm, String scale, int rangeOctave, Instrument instrument)
	{

		if(instrument == null)
		{
			throw new IllegalArgumentException("Melody.playMelody: Instrument parameter is null.");
		}

		this.key = key;
		this.bpm = (int) bpm;
		this.scale = scale;
		this.rangeOctave = (int) rangeOctave;
		this.instrument = instrument;

		new Thread(this).start();

	} // playMelody

	/**
	 * Uses the given scale and key to create an array of Notes,
	 * then loops through that array and plays them with the given Instrument.
	 * 
	 * @param key	String denoting the current key; do not include major or minor designation here
	 * @param bpm	tempo in beats per minute
	 * @param scale	either the String "major", "minor", or "chromatic"
	 * @param rangeOctave	int giving the range octave; must be between 3 and 6
	 * @param instrument	Instrument that will play the Melody
	 * @throws InterruptedException
	 */
	private void playMelodyThread( String key, float bpm, String scale, int rangeOctave, Instrument instrument ) throws InterruptedException
	{
		float[][]	curMelody	= melodyLib.get(scale.trim().toLowerCase());
		//		int			scaleLength	= curMelody.length;
		//		int[]		midiNotes	= new int[scaleLength];
		//		float[]		durations	= new float[scaleLength];
		Note[]		notes		= new Note[curMelody.length];
		float		defaultAmp	= 1;

		// Find position of notes in this key:
		int		keyPos		= -1;
		for(int i = 0; i < Melody.keys.length; i++)
		{
			if(Melody.keys[i].toUpperCase().contains(" " + key.trim().toUpperCase() + " "))
			{
				keyPos	= i;
				/*
					As of 6/15, octaves will start at C and so the following is N/A:
				//because midi numbers start at C, the last three notes in the octave (A, A#, B)
				//need to be dropped down an octave so that they make intuitive sense in the 
				//drop down box, which starts at A.  Without this, the notes at the top of the 
				//drop down list (A, A#, B) would be higher than the proceeding notes ( C - G# )
				if(keyPos >= 9) { keyPos = keyPos - 12; }
				 */
			}
		} // for - find key position
		if(keyPos == -1)
		{
			throw new IllegalArgumentException("Melody.playMelody: key " + key + " is not a valid key.");
		} // error checking

		this.setRangeList();

		// Calculate milli's per Note, given the tempo:
		float quarterNoteTime = (float) (Math.pow(bpm, -1) * 60 * 1000);

		int		scalePos;
		int		midiNote;
		float	duration;
		float	amplitude;
		boolean	isRest;

		// Calculate midi notes, given the key and range:
		for(int i = 0; i < curMelody.length; i++)
		{
			// If there is indeed a note at this part of the melody, add it to the midiNotes array:
			if(curMelody[i].length == 2)
			{
				scalePos	= (int) ((curMelody[i][0]) + keyPos);
				midiNote	= (12 * (rangeOctave + 1)) + scalePos;

				duration	= curMelody[i][1] * quarterNoteTime;
				amplitude	= defaultAmp;
				isRest		= false;
			} else if(curMelody[i].length == 1){
				// but if not, use -1 as a placeholder:
				midiNote	= -1;
				duration	= curMelody[i][0] * quarterNoteTime;
				amplitude	= 0;
				isRest		= true;
			} else {
				throw new IllegalArgumentException("Melody.playMelodyThread: curMelody[" + i + "] (" + curMelody[i] + 
						" is not a valid length; if indicating a rest, it must have length 1, and for all other notes, length 2.");
			}

			// create an array of Notes:
			notes[i]	= new Note(midiNote, duration, amplitude, isRest);

		} // for - calculate midi notes

		// This while loop is what keeps the melody repeating:
		
		int multiplyer = 700;
		
		while(this.melodyThreadRunning)
		{

			float	nextNoteStartTime	= parent.millis();
			
			if(this.input != null)
			{
				this.input.setAmplitudeArray(new float[] { this.instrument.getGain() * multiplyer });
			}

			// use the Note's duration to call instrument.play() at the appropriate time
			for( int i = 0 ; i < notes.length - 1 && this.melodyThreadRunning; i++)
			{
				instrument.playNote(notes[i]);

				if(this.input != null)
				{
					this.input.setFundamentalArray(new float[] { Pitch.mtof(notes[i].getMidiNum()) });
//					this.input.setAdjustedFundArray(new float[] { Pitch.mtof(notes[i].getMidiNum()) });
					this.input.setAmplitudeArray(new float[] { this.instrument.getGain() * multiplyer });
				}

				nextNoteStartTime	= parent.millis() + notes[i].getDuration();
				//				System.out.println("nextNoteStartTime = " + nextNoteStartTime);


				while(parent.millis() < nextNoteStartTime && this.isRunning())	
				{
					if(this.input != null)
					{
						this.input.setAmplitudeArray(new float[] { this.instrument.getGain() * multiplyer });
					}
					
					if(this.paused) { nextNoteStartTime = nextNoteStartTime + 12; }
					Thread.sleep(10);
				}

			} // for - play Notes
		} // while

	}//playMelodyThread

	/**
	 * Stops the Instrument instance variable by calling stopNote() on it
	 * and sets the melodyThreadRunning boolean instance var to false.
	 */
	public void stop()
	{
		this.melodyThreadRunning = false;
		this.instrument.stopNote();
	}

	/**
	 * Used to pause or un-pause the Instrument; calls pauseNote(input) on the Instrument instance variable.

	 * @param input boolean indicating whether to pause or start the Instrument
	 */
	public void pause(boolean input)
	{
		this.paused = input ;
		this.instrument.pauseNote(input);
	}

	public boolean isRunning()
	{
		return this.melodyThreadRunning;
	}

	/**
	 * Sets the rangeList instance variable with the high and low note of the current melody
	 * in each available octave.
	 * 
	 * @param keyPos position of the current key in the key array - C is 0, C#/Bb are 1, etc.
	 */
	public void setRangeList()
	{

		int		lowRangeInt;
		int		highRangeInt;
		String	lowRangeString;
		String	highRangeString;

		// Find position of notes in this key:
		int		keyPos		= -1;
		for(int i = 0; i < Melody.keys.length; i++)
		{
			if(Melody.keys[i].toUpperCase().contains(" " + this.key.trim().toUpperCase() + " "))
			{
				keyPos	= i;
				/*
							As of 6/15, octaves will start at C and so the following is N/A:
						//because midi numbers start at C, the last three notes in the octave (A, A#, B)
						//need to be dropped down an octave so that they make intuitive sense in the 
						//drop down box, which starts at A.  Without this, the notes at the top of the 
						//drop down list (A, A#, B) would be higher than the proceeding notes ( C - G# )
						if(keyPos >= 9) { keyPos = keyPos - 12; }
				 */
			}
		} // for - find key position
		if(keyPos == -1)
		{
			throw new IllegalArgumentException("Melody.setRangeList: key '" + key + "' is not a valid key.");
		} // error checking


		if(this.scale.equals("chromatic"))
		{
			lowRangeInt	= keyPos;
			lowRangeString	= this.key;
		} else {
			lowRangeInt	= (keyPos + 7) % 12;

			// find the position of the key in all notes:
			int	allNotesPos	= -1;
			for(int i = 0; i < Melody.allNotes.length; i++)
			{
				if(this.key.trim().equals(Melody.allNotes[i]))
				{
					allNotesPos	= i;
				} // if
			} // for
			if(allNotesPos == -1)
			{
				throw new IllegalArgumentException("Melody.setRangeList: this.key '" + this.key + "' could not be found in Melody.allKeys.");
			}

			// find the note that is + 10 from that position:
			lowRangeString	= Melody.allNotes[((allNotesPos + 11) % Melody.allNotes.length)];
		}


		highRangeInt	= keyPos;
		highRangeString	= this.key;

		int			octave		= 3;
		// For keys from C to F#, the dominant scale degree (lowRange) is in a lower Midi octave.
		int	lowOctave;
		if(keyPos < 5 && this.scale != "chromatic") {
			lowOctave	= octave - 1;
		} else {
			lowOctave	= octave;
		}

		// the highest note will always be one octave above the tonic:
		int	highOctave	= octave + 1;

		int			lowRangeMidi;
		int			highRangeMidi;
		for(int i = 0; i < this.rangeList.length; i++)
		{
			// ** (lowOctave and highOctave are the *printed* octave; must be incremented by 1 to use for calculations.) **
			lowRangeMidi	= (12 * (lowOctave + 1)) + lowRangeInt;
			highRangeMidi	= (12 * (highOctave + 1)) + highRangeInt;
			/*			
			lowRanges[i]	= lowRangeString + (lowOctave) + " (" + Pitch.mtof(lowRangeMidi) + ")";
			highRanges[i]	= highRangeString + (highOctave) + " (" + Pitch.mtof(highRangeMidi) + ")";
			 */			
			this.rangeList[i]	= lowRangeString + (lowOctave) + " (" + Pitch.mtof(lowRangeMidi) + ") - " +
					highRangeString + (highOctave) + " (" + Pitch.mtof(highRangeMidi) + ")";


			lowOctave++;
			highOctave++;
		} // for
	} // setRangeList

	/**
	 * Returns the list of possible range octave options as a String[]
	 * 
	 * @return rangeList instance variable
	 */
	public String[] getRangeList()
	{
		return this.rangeList;
	}

	/**
	 * Setter for the key instance variable, which needs to be updated to give an accurate range.
	 * 
	 * @param newKey
	 */
	public void setKey(String newKey)
	{
		this.key	= newKey;
	} // setKey

	/**
	 * Setter for the scale instance variable, which needs to be updated to make rangeList accurate
	 * when the Driver changes the scale.
	 * 
	 * @param newScale Either "major", "minor" or "chromatic"
	 */
	public void setScale(String newScale)
	{
		if(!(newScale.equalsIgnoreCase("major")) &&
				!(newScale.equalsIgnoreCase("minor")) &&
				!newScale.equalsIgnoreCase("chromatic"))
		{
			throw new IllegalArgumentException("Melody.setScale: String param '" + newScale + "' is not a valid scale; must be either 'major', 'minor', or 'chromatic'.");
		}

		this.scale	= newScale.toLowerCase();
	}
	
	public void setBPM(int bpm)
	{
		this.bpm = bpm;
	}


	/**
	 * Fills a HashMap with float[]'s of the scale degrees, durations, and rests
	 * that make up the chromatic, major and minor melodies.
	 * 
	 * @return the filled HashMap<String, float[][]>
	 */
	private static HashMap<String, float[][]> initializeLib()
	{
		HashMap<String, float[][]> hm = new HashMap<String, float[][]>();

		// Each Note is represented by a float array where the first element is the scale degree 
		// (of the chromatic scale; 0 is the tonic, 11 the leading tone; notes outside the octave either negative numbers
		// for below or 12 and above for above).
		
		// Rests are indicated by an array of length 1, where the sole element of the array indicates
		// the value of the rest in quarter notes (e.g., an eighth rest is would be "new float[] { 0.5 };").
		
		hm.put("chromatic", new float[][]
				{ 
			new float[] { 0,1 },  // 0:1
			new float[] { 1,1 },  // 0:2
			new float[] { 2,1 },  // 0:3
			new float[] { 3,1 },  // 0:4
			new float[] { 4,1 },  // 1:1
			new float[] { 5,1 },  // 1:2
			new float[] { 6,1 },  // 1:3
			new float[] { 7,1 },  // 1:4
			new float[] { 8,1 },  // 2:1
			new float[] { 9,1 },  // 2:2
			new float[] { 10,1 },  // 2:3
			new float[] { 11,1 },  // 2:4
			new float[] { 12,2 },  // 3:1
			new float[] { 1 },	// 3:2
			new float[] { 12, (float) 0.5 },	// 3:3
			new float[] { 11, (float) 0.5 },  
			new float[] { 10, (float) 0.5 },	// 3:4
			new float[] { 9, (float) 0.5 },  
			new float[] { 8, (float) 0.5 },  // 4:1
			new float[] { 7, (float) 0.5 },
			new float[] { 6, (float) 0.5 },  // 4:2
			new float[] { 5, (float) 0.5 },
			new float[] { 4, (float) 0.5 },  // 4:3
			new float[] { 3, (float) 0.5 },  
			new float[] { 2, (float) 0.5 },  // 4:4
			new float[] { 1, (float) 0.5 },  
			new float[] { 0, 1 },  			// 5:1
			new float[] { 1 },
				}  );

		hm.put("major", new float[][]
				{ 
			new float[] { 0,1 },  // 0:1
			new float[] { 2,1 },  // 0:2
			new float[] { 4,1 },  // 0:3
			new float[] { 5,1 },  // 0:4
			new float[] { 1 },  // 1:1
			new float[] { 7,1 },  // 1:2
			new float[] { 1 },  // 1:3
			new float[] { 9,1 },  // 1:4
			new float[] { 11,1 },  // 2:1
			new float[] { 12,1 },  // 2:2
			new float[] { 1 },  // 2:3
			new float[] { 12,1 },  // 2:4
			new float[] { 11,1 },  // 3:1
			new float[] { 1 },  // 3:2
			new float[] { 9, (float) 0.5 },  // 3:3
			new float[] { 7, (float) 0.5 },  
			new float[] { 5, (float) 0.5 },  // 3:4
			new float[] { 4, (float) 0.5 },  
			new float[] { 2, 1 },	// 4:1
			new float[] { 0,1 },	// 4:2
			new float[] { 2 },		// 4:3, 4:4
			new float[] { 0,1 },	// 5:1
			new float[] { -7, 1 },	// 5:2
			new float[] { 0,1 },	// 5:3 
			new float[] { -5,1 },	// 5:4
			new float[] { 0,1 },	// 6:1
			new float[] { 0, (float) 0.5 },	// 6:2
			new float[] { 2, (float) 0.5 },  
			new float[] { 4, (float) 0.5 },	// 6:3
			new float[] { 5, (float) 0.5 },  
			new float[] { 7, (float) 0.5 },	// 6:4
			new float[] { 9, (float) 0.5 },  
			new float[] { 11,(float) 0.5 },	// 7:1
			new float[] { 12, (float) 0.5 },
			new float[] { 11, (float) 0.5 },	// 7:2
			new float[] { 9, (float) 0.5 },  
			new float[] { 7, (float) 0.5 },  // 7:3
			new float[] { 5, (float) 0.5 },  
			new float[] { 4, (float) 0.5 },  // 7:4
			new float[] { 2, (float) 0.5 },  
			new float[] { 0, 1 },  // 8:1
			new float[] { 1 },
				} );

		hm.put("minor", new float[][]
				{ 
			new float[] { 0,1 },  // 0:1
			new float[] { 2,1 },  // 0:2
			new float[] { 3,1 },  // 0:3
			new float[] { 5,1 },  // 0:4
			new float[] { 1 },  // 1:1
			new float[] { 7,1 },  // 1:2
			new float[] { 1 },  // 1:3
			new float[] { 8,1 },  // 1:4
			new float[] { 10,1 },  // 2:1
			new float[] { 12,1 },  // 2:2
			new float[] { 1 },  // 2:3
			new float[] { 12,1 },  // 2:4
			new float[] { 10,1 },  // 3:1
			new float[] { 1 },  // 3:2
			new float[] { 8, (float) 0.5 },  // 3:3
			new float[] { 7, (float) 0.5 },  
			new float[] { 5, (float) 0.5 },  // 3:4
			new float[] { 3, (float) 0.5 },  
			new float[] { 2, 1 },	// 4:1
			new float[] { 0,1 },	// 4:2
			new float[] { 1 },		// 4:3
			new float[] { 0,1 },	// 
			new float[] { -7, 1 },	// 5:1
			new float[] { 0,1 },	// 5:2
			new float[] { -5,1 },	// 5:3 
			new float[] { 0,1 },	// 5:4

			new float[] { 0,(float) 0.5 },  // 6:1
			new float[] { 2,(float) 0.5 },  
			new float[] { 3,(float) 0.5 },  // 6:2
			new float[] { 5,(float) 0.5 },  
			new float[] { 7,(float) 0.5 },  // 6:3
			new float[] { 8,(float) 0.5 },  
			new float[] { 10,(float) 0.5 },  // 6:4
			new float[] { 12,(float) 0.5 },
			new float[] { 10,(float) 0.5 },  // 7:1
			new float[] { 8,(float) 0.5 },  
			new float[] { 7,(float) 0.5 },  // 7:2
			new float[] { 5,(float) 0.5 },  
			new float[] { 3,(float) 0.5 },  // 7:3
			new float[] { 2,(float) 0.5 },  
			new float[] { 0,(float) 1 },  // 7:4

			new float[] { 0,(float) 0.5 },  // 8:1
			new float[] { 2,(float) 0.5 },  
			new float[] { 3,(float) 0.5 },  // 8:2
			new float[] { 5,(float) 0.5 },  
			new float[] { 7,(float) 0.5 },  // 8:3
			new float[] { 8,(float) 0.5 },  
			new float[] { 11,(float) 0.5 },  // 8:4
			new float[] { 12,(float) 0.5 },
			new float[] { 11,(float) 0.5 },  // 9:1
			new float[] { 8,(float) 0.5 },  
			new float[] { 7,(float) 0.5 },  // 9:2
			new float[] { 5,(float) 0.5 },  
			new float[] { 3,(float) 0.5 },  // 9:3
			new float[] { 2,(float) 0.5 },  
			new float[] { 0,(float) 0.5 },  // 9:4
			new float[] { 1 },
			new float[] { 0, (float) 0.5 },  // 10:1
			new float[] { 2, (float) 0.5 },  
			new float[] { 3,(float) 0.5 },  // 10:2
			new float[] { 5,(float) 0.5 },  
			new float[] { 7,(float) 0.5 },  // 10:3
			new float[] { 9,(float) 0.5 },  
			new float[] { 11,(float) 0.5 },  // 10:4
			new float[] { 12,(float) 0.5 },
			new float[] { 10,(float) 0.5 },  // 11:1
			new float[] { 8,(float) 0.5 },  
			new float[] { 7,(float) 0.5 },  // 11:2
			new float[] { 5,(float) 0.5 },  
			new float[] { 3,(float) 0.5 },  // 11:3
			new float[] { 2,(float) 0.5 },  
			new float[] { 0,(float) 1 },// 11:4
			new float[] { 1 },
				} );

		return hm;
	} // initializeLib

	@Override
	public void run() {
		this.melodyThreadRunning = true;
		try {
			this.playMelodyThread(this.key, this.bpm, this.scale, this.rangeOctave, this.instrument);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub

	}


} // Melody