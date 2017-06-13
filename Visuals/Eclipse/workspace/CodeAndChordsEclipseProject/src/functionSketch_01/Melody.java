package functionSketch_01;

import java.util.*;

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
public class Melody {

	private static HashMap<String, float[][]>	melodyLib  = Melody.initializeLib();
	private	static String[]						keys	= new String[] { 
			" A "," A# / Bb "," B ", " C ", " C# / Db ", " D ",
			" D# / Eb ", " E ", " F ", " F# / Gb "," G "," G# / Ab ", 
	};
	private LinkedList<Note>                    mel;
	private float                               highRange;
	private float                               lowRange;
	private	PApplet								parent;

	/**
	 * Constructor
	 * @param parent
	 */
	public Melody(PApplet parent)
	{
		this.parent	= parent;
	}

	/**
	 * Where most of the work of playing the Melody is done.
	 * 
	 * @param key	String denoting the current key; do not include major or minor designation here
	 * @param bpm	tempo in beats per minute
	 * @param scale	either the String "major", "minor", or "chromatic"
	 * @param rangeOctave	int giving the range octave; must be between _____ and ____ (TODO)
	 * @param instrument	Instrument that will play the Melody
	 */
	public void playMelody(String key, float bpm, String scale, int rangeOctave, Instrument instrument)
	{
		float[][]	curMelody	= melodyLib.get(scale.trim().toLowerCase());
		//		int			scaleLength	= curMelody.length;
		//		int[]		midiNotes	= new int[scaleLength];
		//		float[]		durations	= new float[scaleLength];
		Note[]		notes		= new Note[curMelody.length];
		float		defaultAmp	= 60;

		// Find position of notes in this key:
		int		keyPos		= -1;
		for(int i = 0; i < Melody.keys.length; i++)
		{
			if(Melody.keys[i].equals(" " + key.trim().toUpperCase() + " "))
			{
				keyPos	= 1;
			}
		} // for - find key position
		if(keyPos < 0)
		{
			throw new IllegalArgumentException("Melody.playMelody: key " + key + " is not a valid key.");
		} // error checking

		// Calculate milli's per Note, given the tempo:
		float quarterNoteTime = (float) (Math.pow(bpm, -1) * 60 * 1000);

		int		scalePos;
		int		midiNote;
		float	duration;
		float		amplitude;

		// Calculate midi notes, given the key and range:
		for(int i = 0; i < curMelody.length; i++)
		{
			// If there is indeed a note at this part of the melody, add it to the midiNotes array:
			if(curMelody[i].length > 1)
			{
				scalePos	= (int) ((curMelody[i][0]) + keyPos);
				midiNote	= (12 * rangeOctave) + scalePos;

				duration	= curMelody[i][1] * quarterNoteTime;
				amplitude	= defaultAmp;
			} else {
				// but if not, use -1 as a placeholder:
				midiNote	= -1;
				duration	= (float) (0.5 * quarterNoteTime);
				amplitude	= 0;
			}

			// create an array of Notes:
			notes[i]	= new Note(midiNote, duration, amplitude);

		} // for - calculate midi notes

		float	nextNoteStartTime	= parent.millis();
		// use the Note's duration to call instrument.play() at the appropriate time
		for(int i = 0; i < notes.length; i++)
		{
			while(parent.millis() < nextNoteStartTime)	{ }

			instrument.playNote(notes[i]);

			nextNoteStartTime	= parent.millis() + notes[i].getDuration();
		} // for - play Notes

	} // playMelody

	/*	
	 * TODO: get rid of these?
	public float getHighRange()
	{
		return this.highRange;
	}

	public float getLowRange()
	{
		return this.lowRange;
	}
	 */

	/**
	 * Fills a HashMap with float[]'s of the scale degrees, durations, and rests
	 * that make up the chromatic, major and minor melodies.
	 * 
	 * @return the filled HashMap<String, float[][]>
	 */
	private static HashMap<String, float[][]> initializeLib()
	{
		HashMap<String, float[][]> hm = new HashMap<String, float[][]>();

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
			new float[] {  },	// 3:2
			new float[] {  },
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
			new float[] {  },
				}  );

		hm.put("major", new float[][]
				{ 
			new float[] { 0,1 },  // 0:1
			new float[] { 2,1 },  // 0:2
			new float[] { 4,1 },  // 0:3
			new float[] { 5,1 },  // 0:4
			new float[] {  },  // 1:1
			new float[] {  },
			new float[] { 7,1 },  // 1:2
			new float[] {  },  // 1:3
			new float[] {  },
			new float[] { 9,1 },  // 1:4
			new float[] { 11,1 },  // 2:1
			new float[] { 12,1 },  // 2:2
			new float[] {  },  // 2:3
			new float[] {  },
			new float[] { 12,1 },  // 2:4
			new float[] { 11,1 },  // 3:1
			new float[] {  },  // 3:2
			new float[] {  },
			new float[] { 9, (float) 0.5 },  // 3:3
			new float[] { 7, (float) 0.5 },  
			new float[] { 5, (float) 0.5 },  // 3:4
			new float[] { 4, (float) 0.5 },  
			new float[] { 2, 1 },	// 4:1
			new float[] { 0,1 },	// 4:2
			new float[] {  },		// 4:3
			new float[] {  },		// 4:4
			new float[] {  },
			new float[] {  },
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
			new float[] {  },
				} );

		hm.put("minor", new float[][]
				{ 
			new float[] { 0,1 },  // 0:1
			new float[] { 2,1 },  // 0:2
			new float[] { 3,1 },  // 0:3
			new float[] { 5,1 },  // 0:4
			new float[] {  },  // 1:1
			new float[] {  },
			new float[] { 7,1 },  // 1:2
			new float[] {  },  // 1:3
			new float[] {  },
			new float[] { 8,1 },  // 1:4
			new float[] { 10,1 },  // 2:1
			new float[] { 12,1 },  // 2:2
			new float[] {  },  // 2:3
			new float[] {  },
			new float[] { 12,1 },  // 2:4
			new float[] { 10,1 },  // 3:1
			new float[] {  },  // 3:2
			new float[] {  },
			new float[] { 8, (float) 0.5 },  // 3:3
			new float[] { 7, (float) 0.5 },  
			new float[] { 5, (float) 0.5 },  // 3:4
			new float[] { 3, (float) 0.5 },  
			new float[] { 2, 1 },	// 4:1
			new float[] { 0,1 },	// 4:2
			new float[] {  },		// 4:3
			new float[] {  },
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
			new float[] {  },
			new float[] {  },
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
			new float[] {  },
				} );

		return hm;
	} // initializeLib
} // Melody
