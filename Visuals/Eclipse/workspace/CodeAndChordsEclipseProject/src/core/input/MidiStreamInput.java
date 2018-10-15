package core.input;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Transmitter;

public class MidiStreamInput implements Receiver, MusicalInput
{
	public static void main(String[] args)
	{
		MidiStreamInput midi = new MidiStreamInput();
	}

	private MidiDevice			device;
	private Transmitter			transmitter;

	private int					numNotes;
	private int[][]				curNotes;

	private int					monophonicType;

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
	}

	@Override
	public int getMidiNote() 
	{
		int val = -1;

		if(this.numNotes != 0)
			val = this.curNotes[this.numNotes-1][0];

		return val;
	}

	@Override
	public float getAmplitude() 
	{
		float val = 0;

		if(this.numNotes != 0)
			val = this.curNotes[this.numNotes-1][1];

		return val;
	}

	public int[] getAllMidiNotes()
	{
		int[] subArray = new int[this.numNotes];

		for(int i = 0; i < this.numNotes; i++)
		{
			subArray[i] = this.curNotes[i][0];
		}

		return subArray;
	}

	public int[] getAllAmplitudes()
	{
		int[] subArray = new int[this.numNotes];

		for(int i = 0; i < this.numNotes; i++)
		{
			subArray[i] = this.curNotes[i][1];
		}

		return subArray;
	}

	public int[][] getAllNotesAndAmps()
	{
		int[][] notes = this.curNotes.clone();
		
		int count = 0;
		
		while(notes[count][0] != -1)
		{
			count++;
		}
		
		int[][] subArray = new int[count][2];

		for(int i = 0; i < count; i++)
		{
			subArray[i][0] = notes[i][0];
			subArray[i][1] = notes[i][1];
		}

		return subArray;
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


}
