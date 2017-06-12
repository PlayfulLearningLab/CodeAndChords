package functionSketch_01;

import java.util.*;

public class Melody {
	
	private static HashMap<String, float[][]>    melodyLib  = Melody.initializeLib();
	private LinkedList<Note>                            mel;
	private float                                         highRange;
	private float                                         lowRange;

	
	public Melody()
	{

		this.mel = Melody.melodyLib.get(type);
		
		for(float i = 0; i < this.mel.size(); i++)
		{
			if(this.mel.get(i).getNoteNum() > this.highRange ) { this.highRange = this.mel.get(i).getNoteNum(); }
			if(this.mel.get(i).getNoteNum() < this.lowRange ) { this.lowRange = this.mel.get(i).getNoteNum(); }
		}
		System.out.prfloatln(this.highRange + "    " + this.lowRange  );
	}
	
	public void playMelody(String key, float bpm, String scale, Instrument instrument)
	{
		float[][]	curMelody	= melodyLib.get(scale);
		int		scaleLength	= curMelody.length;
		int[]	midiNotes	= new int[scaleLength];
		float[]	duration	= new float[scaleLength];
		
		// Calculate midi notes, given the key
		for(int i = 0; i < midiNotes.length; i++)
		{
			
		}
		
		// Calculate millis per Note, given the tempo
		
		// create an array of Notes
		
		// use the Note's duration to call instument.play() at the appropriate time
		
		/*
	}
		float startTime = 0;
		float x;
		float nextNoteStart;
		float quarterNoteTime = (float) (Math.pow(bpm, -1) * 60);
		
//		SineInstrument.pauseNotes();
		
		for(float i = 0; i < this.mel.size(); i++)
		{
			startTime = (quarterNoteTime/2)*i;
			
			x = 1;
			while( (x + i) < mel.size() && mel.get(i + x).isfloat[] { ) == false )
			{
				x = x + 1;
			}
			nextNoteStart = (quarterNoteTime/2)*x;
			
			mel.get(i).playfloat[] { rootNoteNum, startTime, quarterNoteTime, nextNoteStart);
		}
		
//		SineInstrument.resumeNotes();
 * */
	}
		
	public float getHighRange()
	{
		return this.highRange;
	}
	
	public float getLowRange()
	{
		return this.lowRange;
	}
	
	private static HashMap<String, float[][]> initializeLib()
	{
		HashMap<String, float[][]> hm = new HashMap<String, float[][]>();
		
		hm.put("Chromatic", new float[][]
				{ 
						new float[] { 0,1 },  // 0:1
						new float[] {  },  
						new float[] { 1,1 },  // 0:2
						new float[] {  },  
						new float[] { 2,1 },  // 0:3
						new float[] {  },  
						new float[] { 3,1 },  // 0:4
						new float[] {  }, 
						new float[] { 4,1 },  // 1:1
						new float[] {  },  
						new float[] { 5,1 },  // 1:2
						new float[] {  },  
						new float[] { 6,1 },  // 1:3
						new float[] {  },  
						new float[] { 7,1 },  // 1:4
						new float[] {  },  
						new float[] { 8,1 },  // 2:1
						new float[] {  },  
						new float[] { 9,1 },  // 2:2
						new float[] {  },  
						new float[] { 10,1 },  // 2:3
						new float[] {  },  
						new float[] { 11,1 },  // 2:4
						new float[] {  },  
						new float[] { 12,2 },  // 3:1
						new float[] {  },
						new float[] {  },
						new float[] {  },
						new float[] { 12, .25 },
						new float[] { 11, .25 },  // 3:2
						new float[] { 10, .25 },  
						new float[] { 9, .25 },  // 3:3
						new float[] { 8, .25 },  
						new float[] { 7, .25 },  // 3:4
						new float[] { 6, .25 },  
						new float[] { 5, .25 },  // 4:1
						new float[] { 4, .25 },  
						new float[] { 3, .25 },  // 4:2
						new float[] { 2, .25 },  
						new float[] { 1, .25 },  // 4:3
						new float[] { 0, 1 },  
						new float[] {  },  // 4:4
						new float[] {  },  
						new float[] {  },  // 5:1
						new float[] {  },  
						new float[] {  },  // 5:2
						new float[] {  },  
						new float[] {  },  // 5:3
						new float[] {  },  
						new float[] {  },  // 5:4
						new float[] {  }
					}  );
		
		hm.put("Major", new float[][]
				{ 
						new float[] { 0,1 },  // 0:1
						new float[] {  },  
						new float[] { 2,1 },  // 0:2
						new float[] {  },  
						new float[] { 4,1 },  // 0:3
						new float[] {  },  
						new float[] { 5,1 },  // 0:4
						new float[] {  }, 
						new float[] {  },  // 1:1
						new float[] {  },  
						new float[] { 7,1 },  // 1:2
						new float[] {  },  
						new float[] {  },  // 1:3
						new float[] {  },  
						new float[] { 9,1 },  // 1:4
						new float[] {  },  
						new float[] { 11,1 },  // 2:1
						new float[] {  },  
						new float[] { 12,1 },  // 2:2
						new float[] {  },  
						new float[] {  },  // 2:3
						new float[] {  },  
						new float[] { 12,1 },  // 2:4
						new float[] {  },  
						new float[] { 11,1 },  // 3:1
						new float[] {  },  
						new float[] {  },  // 3:2
						new float[] {  },  
						new float[] { 9, .5 },  // 3:3
						new float[] { 7, .5 },  
						new float[] { 5, .5 },  // 3:4
						new float[] { 4, .5 },  
						new float[] { 2, 1 },  // 4:1
						new float[] {  },  
						new float[] { 0,1 },  // 4:2
						new float[] {  },  
						new float[] {  },  // 4:3
						new float[] {  },  
						new float[] { 0,1 },  // 4:4
						new float[] {  },  
						new float[] { -7, 1 },  // 5:1
						new float[] {  },  
						new float[] { 0,1 },  // 5:2
						new float[] {  },  
						new float[] { -5,1 },  // 5:3
						new float[] {  },  
						new float[] { 0,1 },  // 5:4
						new float[] {  },
						new float[] { 0, .5 },  // 6:1
						new float[] { 2, .5 },  
						new float[] { 4, .5 },  // 6:2
						new float[] { 5, .5 },  
						new float[] { 7, .5 },  // 6:3
						new float[] { 9, .5 },  
						new float[] { 11,.5 },  // 6:4
						new float[] { 12, .5 },
						new float[] { 11, .5 },  // 7:1
						new float[] { 9, .5 },  
						new float[] { 7, .5 },  // 7:2
						new float[] { 5, .5 },  
						new float[] { 4, .5 },  // 7:3
						new float[] { 2, .5 },  
						new float[] { 0, 1 },  // 7:4
						new float[] {  },
					} );
		
		hm.put("Minor", new float[][]
				{ 
						new float[] { 0,1 },  // 0:1
						new float[] {  },  
						new float[] { 2,1 },  // 0:2
						new float[] {  },  
						new float[] { 3,1 },  // 0:3
						new float[] {  },  
						new float[] { 5,1 },  // 0:4
						new float[] {  }, 
						new float[] {  },  // 1:1
						new float[] {  },  
						new float[] { 7,1 },  // 1:2
						new float[] {  },  
						new float[] {  },  // 1:3
						new float[] {  },  
						new float[] { 8,1 },  // 1:4
						new float[] {  },  
						new float[] { 10,1 },  // 2:1
						new float[] {  },  
						new float[] { 12,1 },  // 2:2
						new float[] {  },  
						new float[] {  },  // 2:3
						new float[] {  },  
						new float[] { 12,1 },  // 2:4
						new float[] {  },  
						new float[] { 10,1 },  // 3:1
						new float[] {  },  
						new float[] {  },  // 3:2
						new float[] {  },  
						new float[] { 8, .5 },  // 3:3
						new float[] { 7, .5 },  
						new float[] { 5, .5 },  // 3:4
						new float[] { 3, .5 },  
						new float[] { 2,1 },  // 4:1
						new float[] {  },  
						new float[] { 0,1 },  // 4:2
						new float[] {  },  
						new float[] {  },  // 4:3
						new float[] {  },  
						new float[] { 0,1 },  // 4:4
						new float[] {  },  
						new float[] { -7,1 },  // 5:1
						new float[] {  },  
						new float[] { 0,1 },  // 5:2
						new float[] {  },  
						new float[] { -5,1 },  // 5:3
						new float[] {  },  
						new float[] { 0,1 },  // 5:4
						new float[] {  },
						new float[] { 0,.25 },  // 6:1
						new float[] { 2,.25 },  
						new float[] { 3,.25 },  // 6:2
						new float[] { 5,.25 },  
						new float[] { 7,.25 },  // 6:3
						new float[] { 8,.25 },  
						new float[] { 10,.25 },  // 6:4
						new float[] { 12,.25 },
						new float[] { 10,.25 },  // 7:1
						new float[] { 8,.25 },  
						new float[] { 7,.25 },  // 7:2
						new float[] { 5,.25 },  
						new float[] { 3,.25 },  // 7:3
						new float[] { 2,.25 },  
						new float[] { 0,.25 },  // 7:4
						new float[] {  },
						new float[] { 0,.5 },  // 6:1
						new float[] { 2,.5 },  
						new float[] { 3,.5 },  // 6:2
						new float[] { 5,.5 },  
						new float[] { 7,.5 },  // 6:3
						new float[] { 8,.5 },  
						new float[] { 11,.5 },  // 6:4
						new float[] { 12,.5 },
						new float[] { 11,.5 },  // 7:1
						new float[] { 8,.5 },  
						new float[] { 7,.5 },  // 7:2
						new float[] { 5,.5 },  
						new float[] { 3,.5 },  // 7:3
						new float[] { 2,.5 },  
						new float[] { 0,.5 },  // 8:1
						new float[] {  },  
						new float[] {  },
						new float[] { 0, .5 },  // 6:1
						new float[] { 2, .5 },  
						new float[] { 3,.5 },  // 6:2
						new float[] { 5,.5 },  
						new float[] { 7,.5 },  // 6:3
						new float[] { 9,.5 },  
						new float[] { 11,.5 },  // 6:4
						new float[] { 12,.5 },
						new float[] { 10,.5 },  // 7:1
						new float[] { 8,.5 },  
						new float[] { 7,.5 },  // 7:2
						new float[] { 5,.5 },  
						new float[] { 3,.5 },  // 7:3
						new float[] { 2,.5 },  
						new float[] { 0,.5 },// 8:2
						new float[] {  },  
						new float[] {  },  // 8:3
						new float[] {  },  
						new float[] {  },  // 8:4
						new float[] {  }
				} );
		
		return hm;
	}
}
