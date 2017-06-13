package core;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;
import processing.core.PApplet;

public class Instrument {
	
	// ADSR defined in milliseconds:
	float			attack;
	float			decay;
	float			sustainAmp;
	float			sustainTime;
	float			release;
	
	AudioContext	audioContext;
	Envelope		gainEnvelope;
	Gain			gain;
	Glide			frequencyGlide;
	PApplet			parent;
	WavePlayer		wavePlayer;
	
	public Instrument(PApplet parent)
	{
		this.parent			= parent;
		
		this.attack		= 500;
		this.decay		= 500;
		// TODO: let sustainAmp be set by user (or at least a preset?):
		this.sustainAmp		= 40;
		this.sustainTime	= 100;
		this.release		= 500;
		
		this.audioContext	= new AudioContext();
		
		this.gainEnvelope	= new Envelope(this.audioContext);
		this.frequencyGlide = new Glide(this.audioContext, 440, 50);
		
		this.gain			= new Gain(this.audioContext, 1, this.gainEnvelope);
		this.wavePlayer		= new WavePlayer(this.audioContext, this.frequencyGlide, Buffer.SINE);

		this.gain.addInput(this.wavePlayer);
		this.audioContext.out.addInput(this.gain);
		
		this.audioContext.start();
	}
	
	/**
	 * Sets the attack, decay, sustain, and release envelopes,
	 * then either pauses the WavePlayer (for rests) or sets it to the 
	 * frequency of the given Note.
	 * 
	 * @param note	Note whose midiNote is turned into a frequency and used to play it.
	 */
	public void playNote(Note note)
	{
		// TODO: deal with release going longer than the note's duration?
		
		System.out.println("Playing note " + note.getMidiNum() + "; frequency = " + Pitch.mtof(note.getMidiNum()));

		/*
		 * AD envelopes rise from 0.0 to
		 * 1.0 over a length of time called the Attack. Then they fall back to 0.0 over a
		length of time called the Decay. ADSR envelopes rise to 1 during the
		attack, then fall to a sustain value, where they stay until the event ends,
		and the value falls to 0 over a time called the Release." - Sonifying Processing, p. 31
		 */
		
		this.gainEnvelope.clear();
		
		// Attack:
		this.gainEnvelope.addSegment(note.getAmplitude(), this.attack);
		
		// Decay:
		this.gainEnvelope.addSegment(this.sustainAmp, this.decay);
		
		// Sustain (remains stable):
		this.gainEnvelope.addSegment(this.sustainAmp, this.sustainTime);
		
		// Release:
		this.gainEnvelope.addSegment(0, this.release);
		
		
		System.out.println("note.getAmplitude() = " + note.getAmplitude() + "; note.getDuration() = " + note.getDuration());
		
//		this.wavePlayer.setFrequency(Pitch.mtof(note.getMidiNum()));
		
		// Treat -1 like a rest:
		if(note.getMidiNum() == -1)
		{
			this.wavePlayer.pause(true);
		} else
		{
			this.wavePlayer.pause(false);
			this.frequencyGlide.setValue(Pitch.mtof(note.getMidiNum()));
		} // if
	} // playNote

} // Instrument
