package core.input;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Transmitter;

public class MidiStreamInput implements Receiver, MusicalInput
{
	public static void main(String[] args)
	{
		MidiStreamInput midi = new MidiStreamInput();
	}

	private MidiDevice			device;
	private Transmitter			transmitter;

	private Instrument[]		midiInstruments;
	private MidiChannel[]		midiChannels;

	private int					numNotes;
	private int[][]				curNotes;

	private int					monophonicType;
	private boolean				isPolyphonic;

	public static final int		LAST_NOTE = 0;
	public static final int		LOUDEST_NOTE = 1;
	public static final int		FIRST_NOTE = 2;

	public MidiStreamInput()
	{
		this.curNotes = new int[127][2];

		for(int i = 0; i < this.curNotes.length; i++)
		{
			this.curNotes[i][0] = -1;
			this.curNotes[i][1] = 0;
		}

		this.monophonicType = 0;
		this.isPolyphonic = false;

		MidiDevice device;
		MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
		for (int i = 0; i < infos.length; i++) 
		{
			try 
			{
				device = MidiSystem.getMidiDevice(infos[i]);
				//does the device have any transmitters?
				//if it does, add it to the device list
				System.out.println(infos[i]);

				//get all transmitters
				List<Transmitter> transmitters = device.getTransmitters();
				//and for each transmitter

				for(int j = 0; j<transmitters.size();j++) 
				{
					//create a new receiver
					transmitters.get(j).setReceiver(this);
				}

				Transmitter trans = device.getTransmitter();
				trans.setReceiver(this);

				//open each device
				device.open();
				//if code gets this far without throwing an exception
				//print a success message
				System.out.println(device.getDeviceInfo()+" Was Opened");


				//Trying to play sound
				Synthesizer midiSynth = MidiSystem.getSynthesizer(); 
				midiSynth.open();

				//get and load default instrument and channel lists
				this.midiInstruments = midiSynth.getDefaultSoundbank().getInstruments();
				this.midiChannels = midiSynth.getChannels();

				midiSynth.loadInstrument(this.midiInstruments[0]);//load an instrument

			}
			catch (MidiUnavailableException e)
			{
				System.out.println("error");
			}
		}

	}


	@Override
	public void send(MidiMessage message, long timeStamp) 
	{
		if(((ShortMessage) message).getCommand() == 144)
		{
			ShortMessage msg = (ShortMessage) message;

			int note = msg.getData1();
			int amp = msg.getData2();

			boolean noteOn = true;

			for(int i = 0; i < this.numNotes && noteOn; i++)
			{
				if(this.curNotes[i][0] == note) 
				{
					noteOn = false;
				}
			}

			if(noteOn)
			{
				this.curNotes[this.numNotes][0] = note;
				this.curNotes[this.numNotes][1] = amp;
				this.numNotes++;

				this.midiChannels[0].noteOn(note, amp);
			}
			else
			{
				boolean noteFound = false;

				int index = 0;

				while(index < this.numNotes && !noteFound)
				{
					if(this.curNotes[index][0] == note)
					{
						noteFound = true;
						this.curNotes[index][0] = -1;
						this.curNotes[index][1] = 0;

						this.numNotes--;

						this.midiChannels[0].noteOff(note);
					}
					index++;
				}

				while(this.curNotes[index][0] != -1)
				{
					this.curNotes[index - 1][0] = this.curNotes[index][0];
					this.curNotes[index][0] = -1;

					this.curNotes[index - 1][1] = this.curNotes[index][1];
					this.curNotes[index][1] = 0;

					index++;
				}

			}
		}

	}//send()

	public void printCurrentNotes()
	{
		System.out.println("----------------------------------------");
		System.out.print("Current Notes: ");
		for(int i = 0; i < 10; i++)
		{
			System.out.print(this.curNotes[i][0] + ",\t");
		}

		System.out.println("");

		System.out.print("Velocity     : ");
		for(int i = 0; i < 10; i++)
		{
			System.out.print(this.curNotes[i][1] + ",\t");
		}

		System.out.println("\n----------------------------------------");

	}

	@Override
	public void close() 
	{
		this.transmitter.close();
		this.device.close();

	}

	public void setMonophonicType(int type)
	{
		if(type < 0 || type > 2) 
			throw new IllegalArgumentException("MidiStreamInput setMonophonicType() int argument out of bounds");

		this.monophonicType = type;
		System.out.println("MIDI mode - " + this.monophonicType);
	}

	private int getMonoNote()
	{
		int noteIndex = -1;

		switch(this.monophonicType)
		{
		case 0: //last
			noteIndex = this.numNotes -1;
			break;

		case 1: //loudest
			int max = 0;

			for(int i = 0; i < this.numNotes; i++)
			{
				if(this.curNotes[i][1] > max)
				{
					max = this.curNotes[i][1];
					noteIndex = i;
				}
			}
			break;

		case 2: //first
			noteIndex = 0;
			break;

		default: //error

			break;
		}

		return noteIndex;
	}

	@Override
	public int getMidiNote() 
	{
		int val = -1;

		if(this.numNotes != 0)
			val = this.curNotes[this.getMonoNote()][0];

		return val;
	}

	@Override
	public float getAmplitude() 
	{
		float val = 0;

		if(this.numNotes != 0)
			val = this.curNotes[this.getMonoNote()][1];

		return val;
	}

	public int[] getAllMidiNotes()
	{
		int[] subArray;

		if(this.isPolyphonic)
		{
			subArray = new int[this.numNotes];

			for(int i = 0; i < this.numNotes; i++)
			{
				subArray[i] = this.curNotes[i][0];
			}
		}
		else
		{
			subArray = new int[] {this.getMidiNote()};
		}		

		return subArray;
	}

	public int[] getAllAmplitudes()
	{
		int[] subArray;

		if(this.isPolyphonic)
		{
			subArray = new int[this.numNotes];

			for(int i = 0; i < this.numNotes; i++)
			{
				subArray[i] = this.curNotes[i][1];
			}
		}
		else
		{
			subArray = new int[] {(int) this.getAmplitude()};
		}		

		return subArray;
	}

	public int[][] getAllNotesAndAmps()
	{
		int[][] subArray;

		if(this.isPolyphonic)
		{
			subArray = new int[this.numNotes][2];

			for(int i = 0; i < this.numNotes; i++)
			{
				subArray[i][0] = this.curNotes[i][0];
				subArray[i][1] = this.curNotes[i][1];
			}
		}
		else if(this.numNotes > 0)
		{
			subArray = new int[1][2];
			subArray[0][0] = this.getMidiNote();
			subArray[0][1] = (int) this.getAmplitude();
		}
		else
		{
			subArray = new int[0][0];
		}

		return subArray;
	}

	public void setIsPolyphonic(boolean isPolyphonic)
	{
		this.isPolyphonic = isPolyphonic;
	}


	@Override
	public String getInputType() 
	{
		return "Midi Stream Input";
	}


	@Override
	public boolean isRealTimeInput() 
	{
		return true;
	}


	@Override
	public boolean isPolyphonic() 
	{
		return true;
	}


	@Override
	public String getInputName() {
		return "";
	}

	@Override
	public int getTotalNumInputs()
	{
		return 1;
	}




}
