package functionSketch_01;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;
import processing.core.PApplet;

public class Instrument {
	
	AudioContext	audioContext;
	Gain			gain;
	Glide			gainGlide;
	Glide			frequencyGlide;
	PApplet			parent;
	WavePlayer		wavePlayer;
	
	public Instrument(PApplet parent)
	{
		this.parent			= parent;
		
		this.audioContext	= new AudioContext();
		
		this.gainGlide		= new Glide(this.audioContext, 0, 50);
		this.frequencyGlide = new Glide(this.audioContext, 440, 50);
		
		this.gain			= new Gain(this.audioContext, 1, this.gainGlide);
		this.wavePlayer		= new WavePlayer(this.audioContext, this.frequencyGlide, Buffer.SINE);

		this.gain.addInput(this.wavePlayer);
		this.audioContext.out.addInput(this.gain);
		
		this.audioContext.start();
	}
	
	public void playNote(Note note)
	{
		// This is where playing a Note will happen
		System.out.println("Playing note " + note.getMidiNum() + "; frequency = " + Pitch.mtof(note.getMidiNum()));
		
		this.gainGlide.setValue(note.getAmplitude());
		this.gainGlide.setGlideTime(note.getDuration());
		
		System.out.println("note.getAmplitude() = " + note.getAmplitude() + "; note.getDuration() = " + note.getDuration());
		
		// TODO: ignore -1 midi notes (treat like a rest)
//		this.wavePlayer.setFrequency(Pitch.mtof(note.getMidiNum()));
		this.frequencyGlide.setValue(Pitch.mtof(note.getMidiNum()));
//		this.wavePlayer.start();
	}

}
