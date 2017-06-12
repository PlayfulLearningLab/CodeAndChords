package functionSketch_01;

import java.util.*;

public class Melody {
	
	private static HashMap<String, LinkedList<Note>>    melodyLib  = Melody.initializeLib();
//	private LinkedList<Note>                            mel;
	private int                                         highRange;
	private int                                         lowRange;

	
	public Melody()
	{
//		this.mel = Melody.melodyLib.get(type);
		
		for(int i = 0; i < this.mel.size(); i++)
		{
			if(this.mel.get(i).getNoteNum() > this.highRange ) { this.highRange = this.mel.get(i).getNoteNum(); }
			if(this.mel.get(i).getNoteNum() < this.lowRange ) { this.lowRange = this.mel.get(i).getNoteNum(); }
		}
		System.out.println(this.highRange + "    " + this.lowRange  );
	}
	
	public void playMelody(String key, int bpm, String scale)
	{
		float startTime = 0;
		int x;
		float nextNoteStart;
		float quarterNoteTime = (float) (Math.pow(bpm, -1) * 60);
		
//		SineInstrument.pauseNotes();
		
		for(int i = 0; i < this.mel.size(); i++)
		{
			startTime = (quarterNoteTime/2)*i;
			
			x = 1;
			while( (x + i) < mel.size() && mel.get(i + x).isNote() == false )
			{
				x = x + 1;
			}
			nextNoteStart = (quarterNoteTime/2)*x;
			
			mel.get(i).playNote(rootNoteNum, startTime, quarterNoteTime, nextNoteStart);
		}
		
//		SineInstrument.resumeNotes();
	}
		
	public int getHighRange()
	{
		return this.highRange;
	}
	
	public int getLowRange()
	{
		return this.lowRange;
	}
	
	private static HashMap<String, LinkedList<Note>> initializeLib()
	{
		HashMap<String, LinkedList<Note>> hm = new HashMap<String, LinkedList<Note>>();
		
		hm.put("Chromatic", Note.asList( new Note[]
				{ 
						new Note(0,1),  // 0:1
						new Note(null),  
						new Note(1,1),  // 0:2
						new Note(null),  
						new Note(2,1),  // 0:3
						new Note(null),  
						new Note(3,1),  // 0:4
						new Note(null), 
						new Note(4,1),  // 1:1
						new Note(null),  
						new Note(5,1),  // 1:2
						new Note(null),  
						new Note(6,1),  // 1:3
						new Note(null),  
						new Note(7,1),  // 1:4
						new Note(null),  
						new Note(8,1),  // 2:1
						new Note(null),  
						new Note(9,1),  // 2:2
						new Note(null),  
						new Note(10,1),  // 2:3
						new Note(null),  
						new Note(11,1),  // 2:4
						new Note(null),  
						new Note(12,2),  // 3:1
						new Note(null),
						new Note(null),
						new Note(null),
						new Note(12, .25),
						new Note(11, .25),  // 3:2
						new Note(10, .25),  
						new Note(9, .25),  // 3:3
						new Note(8, .25),  
						new Note(7, .25),  // 3:4
						new Note(6, .25),  
						new Note(5, .25),  // 4:1
						new Note(4, .25),  
						new Note(3, .25),  // 4:2
						new Note(2, .25),  
						new Note(1, .25),  // 4:3
						new Note(0, 1),  
						new Note(null),  // 4:4
						new Note(null),  
						new Note(null),  // 5:1
						new Note(null),  
						new Note(null),  // 5:2
						new Note(null),  
						new Note(null),  // 5:3
						new Note(null),  
						new Note(null),  // 5:4
						new Note(null)
					} ));
		
		hm.put("Major", Note.asList( new Note[]
				{ 
						new Note(0,1),  // 0:1
						new Note(null),  
						new Note(2,1),  // 0:2
						new Note(null),  
						new Note(4,1),  // 0:3
						new Note(null),  
						new Note(5,1),  // 0:4
						new Note(null), 
						new Note(null),  // 1:1
						new Note(null),  
						new Note(7,1),  // 1:2
						new Note(null),  
						new Note(null),  // 1:3
						new Note(null),  
						new Note(9,1),  // 1:4
						new Note(null),  
						new Note(11,1),  // 2:1
						new Note(null),  
						new Note(12,1),  // 2:2
						new Note(null),  
						new Note(null),  // 2:3
						new Note(null),  
						new Note(12,1),  // 2:4
						new Note(null),  
						new Note(11,1),  // 3:1
						new Note(null),  
						new Note(null),  // 3:2
						new Note(null),  
						new Note(9, .5),  // 3:3
						new Note(7, .5),  
						new Note(5, .5),  // 3:4
						new Note(4, .5),  
						new Note(2, 1),  // 4:1
						new Note(null),  
						new Note(0,1),  // 4:2
						new Note(null),  
						new Note(null),  // 4:3
						new Note(null),  
						new Note(0,1),  // 4:4
						new Note(null),  
						new Note(-7, 1),  // 5:1
						new Note(null),  
						new Note(0,1),  // 5:2
						new Note(null),  
						new Note(-5,1),  // 5:3
						new Note(null),  
						new Note(0,1),  // 5:4
						new Note(null),
						new Note(0, .5),  // 6:1
						new Note(2, .5),  
						new Note(4, .5),  // 6:2
						new Note(5, .5),  
						new Note(7, .5),  // 6:3
						new Note(9, .5),  
						new Note(11,.5),  // 6:4
						new Note(12, .5),
						new Note(11, .5),  // 7:1
						new Note(9, .5),  
						new Note(7, .5),  // 7:2
						new Note(5, .5),  
						new Note(4, .5),  // 7:3
						new Note(2, .5),  
						new Note(0, 1),  // 7:4
						new Note(null),
					} ));
		
		hm.put("Minor", Note.asList( new Note[]
				{ 
						new Note(0,1),  // 0:1
						new Note(null),  
						new Note(2,1),  // 0:2
						new Note(null),  
						new Note(3,1),  // 0:3
						new Note(null),  
						new Note(5,1),  // 0:4
						new Note(null), 
						new Note(null),  // 1:1
						new Note(null),  
						new Note(7,1),  // 1:2
						new Note(null),  
						new Note(null),  // 1:3
						new Note(null),  
						new Note(8,1),  // 1:4
						new Note(null),  
						new Note(10,1),  // 2:1
						new Note(null),  
						new Note(12,1),  // 2:2
						new Note(null),  
						new Note(null),  // 2:3
						new Note(null),  
						new Note(12,1),  // 2:4
						new Note(null),  
						new Note(10,1),  // 3:1
						new Note(null),  
						new Note(null),  // 3:2
						new Note(null),  
						new Note(8, .5),  // 3:3
						new Note(7, .5),  
						new Note(5, .5),  // 3:4
						new Note(3, .5),  
						new Note(2,1),  // 4:1
						new Note(null),  
						new Note(0,1),  // 4:2
						new Note(null),  
						new Note(null),  // 4:3
						new Note(null),  
						new Note(0,1),  // 4:4
						new Note(null),  
						new Note(-7,1),  // 5:1
						new Note(null),  
						new Note(0,1),  // 5:2
						new Note(null),  
						new Note(-5,1),  // 5:3
						new Note(null),  
						new Note(0,1),  // 5:4
						new Note(null),
						new Note(0,.25),  // 6:1
						new Note(2,.25),  
						new Note(3,.25),  // 6:2
						new Note(5,.25),  
						new Note(7,.25),  // 6:3
						new Note(8,.25),  
						new Note(10,.25),  // 6:4
						new Note(12,.25),
						new Note(10,.25),  // 7:1
						new Note(8,.25),  
						new Note(7,.25),  // 7:2
						new Note(5,.25),  
						new Note(3,.25),  // 7:3
						new Note(2,.25),  
						new Note(0,.25),  // 7:4
						new Note(null),
						new Note(0,.5),  // 6:1
						new Note(2,.5),  
						new Note(3,.5),  // 6:2
						new Note(5,.5),  
						new Note(7,.5),  // 6:3
						new Note(8,.5),  
						new Note(11,.5),  // 6:4
						new Note(12,.5),
						new Note(11,.5),  // 7:1
						new Note(8,.5),  
						new Note(7,.5),  // 7:2
						new Note(5,.5),  
						new Note(3,.5),  // 7:3
						new Note(2,.5),  
						new Note(0,.5),  // 8:1
						new Note(null),  
						new Note(null),
						new Note(0,.5),  // 6:1
						new Note(2,.5),  
						new Note(3,.5),  // 6:2
						new Note(5,.5),  
						new Note(7,.5),  // 6:3
						new Note(9,.5),  
						new Note(11,.5),  // 6:4
						new Note(12,.5),
						new Note(10,.5),  // 7:1
						new Note(8,.5),  
						new Note(7,.5),  // 7:2
						new Note(5,.5),  
						new Note(3,.5),  // 7:3
						new Note(2,.5),  
						new Note(0,.5),// 8:2
						new Note(null),  
						new Note(null),  // 8:3
						new Note(null),  
						new Note(null),  // 8:4
						new Note(null)
					} ));
		
		return hm;
	}
}
