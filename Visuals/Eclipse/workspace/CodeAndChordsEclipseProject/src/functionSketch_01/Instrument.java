package functionSketch_01;

public class Instrument {
	
	public void playNote(Note note)
	{
		// This is where playing a Note will happen
		System.out.println("Playing note " + note.getMidiNum());
	}

}
