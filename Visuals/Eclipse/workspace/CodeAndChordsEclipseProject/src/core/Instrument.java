package core;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.Bead;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.data.Pitch;
import net.beadsproject.beads.events.KillTrigger;
import net.beadsproject.beads.events.PauseTrigger;
import net.beadsproject.beads.events.StartTrigger;
import net.beadsproject.beads.ugens.Envelope;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;
import processing.core.PApplet;

/**
 * 06/12/2017
 * Class to play Notes; usually called by a Melody.
 * 
 * @author Emily Meuer
 *
 */
public class Instrument {

	private float	attack;	// millis
	private float	decay; // millis
	private float	sustain;	// number between 0 and 100, signifies the percent of a note's amplitude at which to sustain
	private float	release;	// millis

	private static float[][]		adsrPresets	= new float[][] {
		// 0: even
		new float[] { 100, 50, 90, 100 },
		// 1: long attack
		new float[] { 1500, 50, 80, 50 },
		// 2: long decay + low sustain
		new float[] { 100, 750, 60, 100 },
		// 3: long release
		new float[] { 100, 75, 80, 3000 },
	};
	private PApplet		                 	parent;
	
	private AudioContext				    audioContext;
	
	private Glide		                 	frequencyGlide;
	private WavePlayer	                	wavePlayer;
	
	private	Envelope		                gainEnvelope;
	private	LinkedList<Envelope.Segment>	envelopeSegments;
	private Gain		                 	envolope;
	
	private Glide                           volumeGlide;
	private Gain                            volume;


	/**
	 * Constructor
	 * @param parent
	 */
	public Instrument(PApplet parent)
	{
		this.parent		     	= parent;
		this.audioContext       = new AudioContext();
		
		this.setADSR(0);

		
		this.frequencyGlide     = new Glide(this.audioContext, 440, 20);
		this.wavePlayer		    = new WavePlayer(this.audioContext, this.frequencyGlide, Buffer.SINE);
		
		this.gainEnvelope	    = new Envelope(this.audioContext);
		this.envelopeSegments	= new LinkedList<Envelope.Segment>();
		this.envolope        	= new Gain(this.audioContext, 1, this.gainEnvelope);
		
		this.volumeGlide        = new Glide(this.audioContext, 1, 50);
		this.volume             = new Gain(this.audioContext, 1, this.volumeGlide);
		
		this.envolope.addInput(this.wavePlayer);
		this.volume.addInput(this.envolope);
		this.audioContext.out.addInput(this.volume);

		this.audioContext.start();
		
		this.wavePlayer.pause(true);
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
		if(note == null)
		{
			throw new IllegalArgumentException("Instrument.playNote: Note parameter is null.");
		}
		
		float	sustainAmp;
		float	sustainTime;
		
		sustainAmp	= (note.getAmplitude() * this.sustain / 100);
		sustainTime	= Math.max(note.getDuration() - this.attack - this.decay, 0);
		
		/*
		 * AD envelopes rise from 0.0 to
		 * 1.0 over a length of time called the Attack. Then they fall back to 0.0 over a
		length of time called the Decay. ADSR envelopes rise to 1 during the
		attack, then fall to a sustain value, where they stay until the event ends,
		and the value falls to 0 over a time called the Release." - Sonifying Processing, p. 31
		 */

		if(note.getMidiNum() != -1)
		{
			this.gainEnvelope.clear();
			this.gainEnvelope.setValue(0);
		}

		// Attack:
		this.gainEnvelope.addSegment(note.getAmplitude(), this.attack);
		// Decay:
		this.gainEnvelope.addSegment(sustainAmp, this.decay);

		// Sustain (amplitude doesn't change):
		this.gainEnvelope.addSegment(sustainAmp, sustainTime);

		// Release:
		this.gainEnvelope.addSegment(0, this.release);

		//		this.wavePlayer.setFrequency(Pitch.mtof(note.getMidiNum()));

		// Treat -1 like a rest:
		if(note.getMidiNum() != -1)
		{
			this.wavePlayer.pause(false);
			this.frequencyGlide.setValue(Pitch.mtof(note.getMidiNum()));
//			System.out.println("Instrument.playNote: " + Pitch.mtof(note.getMidiNum()));
		} else {
//			System.out.println("1/8th rest");
		}
		
		/*
		if(note.getMidiNum() == -1)
		{
			float	checkpoint	= parent.millis() + this.release;
			
			if(parent.millis() > checkpoint)
			{
				this.wavePlayer.pause(true);
				System.out.println("Instrument.playNote: paused the WavePlayer");
			}
		} else
		{
			this.wavePlayer.pause(false);
			this.frequencyGlide.setValue(Pitch.mtof(note.getMidiNum()));
		} // if
		*/

	} // playNote
	
	public void stopNote()
	{
		this.volume.clearDependents();
		/*
		this.gainEnvelope.clear();
//		this.wavePlayer.pause(true);
		new KillTrigger(this.wavePlayer);
		new KillTrigger(this.frequencyGlide);
		new KillTrigger(this.gainEnvelope);
		*/
	}
	
	public void pauseNote(boolean pause)
	{
		if(pause)
		{
			new PauseTrigger(this.wavePlayer);
			new PauseTrigger(this.frequencyGlide);
			new PauseTrigger(this.gainEnvelope);
		} else {
			new StartTrigger(this.wavePlayer);
			new StartTrigger(this.frequencyGlide);
			new StartTrigger(this.gainEnvelope);
		}
		
		/*
		this.wavePlayer.pause(pause);
		this.frequencyGlide.pause(pause);
		this.gainEnvelope.pause(pause);
		*/
		
		
		System.out.println("pause = " + pause);
	}
	
	public void setVolume(float vol)
	{
		this.volumeGlide.setValue(vol);
		System.out.println("Volume set to " + vol);
	}


	/**
	 * Sets the adsr with pre-determined values designated by the given preset number.
	 * 
	 * @param presetNum	int designating the preset values to which adsr is to be set
	 */
	public void setADSR(int presetNum)
	{
		if(presetNum < 0 || presetNum > Instrument.adsrPresets.length)
		{
			throw new IllegalArgumentException("Instrument.adsrPresets: int param. " + presetNum + " is out of bounds; must be between 0 and " + Instrument.adsrPresets.length + ".");
		}
		
		this.attack		= Instrument.adsrPresets[presetNum][0];
		this.decay		= Instrument.adsrPresets[presetNum][1];
		this.sustain	= Instrument.adsrPresets[presetNum][2];
		this.release	= Instrument.adsrPresets[presetNum][3];
		
		

	} // adsrPresets
	
	public void setADSR(float a, float d, float s, float r)
	{
		this.attack = a * 10;
		this.decay = d * 10;
		this.sustain = s;
		this.release = r * 10;
		
		System.out.println(a + " / " + d + " / " + s + " / " + r);
	}

} // Instrument
