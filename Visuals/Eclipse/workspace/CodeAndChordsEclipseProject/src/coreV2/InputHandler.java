package coreV2;

import java.awt.Color;
import java.util.ArrayList;

import com.portaudio.PortAudio;

import controlP5.ControlEvent;
import controlP5.ControlP5;
import controlP5.ScrollableList;
import controlP5.Textlabel;
import core.input.Input;
import core.input.MidiStreamInput;
import core.input.MusicalInput;
import core.input.PortAudioAudioIO;
import core.input.MicrophoneInput;
import core.input.RecordedInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;
import processing.core.PFont;

/*
 * Danny's TODO List:
 * 
 *   1: make all setter methods go through one method
 *   2: add a method that sets the scrollable lists to the right setting
 *   3: clean up moduleDriver
 */

public class InputHandler extends MenuTemplate
{
	//Using a the real time input or playable input?
	private boolean					useRealTimeInput;

	//Real Time Inputs
	private	MusicalInput[]			realTimeInputs;

	//Playable Inputs
	private	MusicalInput[]			playableInputs;


	private String[]				polyMidiButtonText;
	private String[]				monoMidiTypeButtonText;

	protected	int	xVals;
	protected	int	yVals;
	protected	int	rectWidths;
	protected	int	rectHeights;


	/**
	 * Constructor
	 * 
	 * @param parent
	 */
	public InputHandler(ModuleDriver driver)
	{

		super("Input Menu", driver, true);
		this.makeControls();

		this.realTimeInputs = new MusicalInput[0];
		this.playableInputs = new MusicalInput[0];

		//Setup for Microphone Input
		AudioContext ac;
		boolean skip4to8 = false;

		//Number of Microphone Channels
		int numInputs = 1;
		
		System.out.println("starting try");

		try{
			PortAudio.getVersion();
			ac = new AudioContext(new PortAudioAudioIO(numInputs), 512, AudioContext.defaultAudioFormat(numInputs, numInputs));
		}
		catch( UnsatisfiedLinkError e ){
			System.err.println("Port Audio could not be found.  Switching to default audio context.\n"
					+ "Multiple Inputs will NOT be enabled.");
			
			ac = new AudioContext();
			numInputs = 1;
		}

		MicrophoneInput mic = new MicrophoneInput(numInputs, ac, skip4to8, this.driver.getParent());
		mic.setInputName("Single Channel");
		this.addMusicalInput(mic);
		
		//Add MIDI Input
		this.addMusicalInput(new MidiStreamInput());

		this.useRealTimeInput = true;
		this.parent.registerMethod("draw", this);

		this.controlP5.get("realTimeInput").setValue(0);


		
		//recorded inputs
		RecordedInput recInput1	= new RecordedInput(driver.getParent(), new String[] {	"6_Part_Scale1.wav", 
				"6_Part_Scale2.wav", 
				"6_Part_Scale3.wav", 
		"6_Part_Scale4.wav"});

		recInput1.setInputName("4 Part Scale");
		this.addMusicalInput(recInput1);



		RecordedInput recInput2	= new RecordedInput(driver.getParent(), new String[] {	"WantingMemories_Melody.wav",
				"WMBass_Later_Quiet.wav",
				"WantingMemories_Alto.wav",
				"WantingMemories_Soprano.wav",
		"WMTenor_Medium.wav"});

		recInput2.setInputName("Wanting Memories");
		this.addMusicalInput(recInput2);

		this.controlP5.get("playableInput").setValue(0);

		this.polyMidiButtonText = new String[] {"Polyphonic", "Monophonic"};
		this.monoMidiTypeButtonText = new String[] {"Last", "Loudest", "First"};

	}

	// Setter for useRealTimeInput boolean

	public void setUseRealTimeInput(boolean useRealTimeInput)
	{		
		this.useRealTimeInput = useRealTimeInput;	
	}

	public void draw()
	{
		if(this.controlP5.getController("Legend").getValue() == 1)
		{
			this.legend();
		}
		if(this.controlP5.getController("Legend").getValue() == 0)
		{

		}
	}


	//Stuff for dealing with input channels.  Needs some changes and different names





	// Get different note values

	private MusicalInput getCurInput()
	{
		MusicalInput curInput = null;

		if(this.useRealTimeInput)
		{
			curInput = this.realTimeInputs[(int) this.controlP5.getController("realTimeInput").getValue()];
		}
		else
		{
			curInput = this.playableInputs[(int) this.controlP5.getController("playableInput").getValue()];
		}

		return curInput;
	}

	public int getMidiNote()
	{
		MusicalInput curInput = this.getCurInput();		
		if(curInput == null) throw new IllegalArgumentException("Current input is null");

		return curInput.getMidiNote();
	}

	public float getAmplitude()
	{
		MusicalInput curInput = this.getCurInput();		
		if(curInput == null) throw new IllegalArgumentException("Current input is null");
		
		float answer = curInput.getAmplitude();
		
		if(this.controlP5.getController("piano").getValue() > answer)
		{
			answer = 0;
		}
		return answer;
	}

	public int[][] getPolyMidiNotes()
	{
		MusicalInput curInput = this.getCurInput();
		if(curInput == null) throw new IllegalArgumentException("Current input is null");

		int[][] midiNotes;

		if(curInput.isPolyphonic())
		{
			midiNotes = ((MidiStreamInput) curInput).getAllNotesAndAmps();
		}
		else if(this.getMidiNote() != -1)
		{
			midiNotes = new int[1][2];
			midiNotes[0][0] = this.getMidiNote();
			midiNotes[0][1] = (int) this.getAmplitude();
		}
		else
		{
			midiNotes = new int[0][0];
		}

		return midiNotes;
	}
	
	
	



	@Override
	public void controlEvent(ControlEvent theEvent)
	{
		super.controlEvent(theEvent);

		if(theEvent.getName() == "play")
		{
			if(theEvent.getValue() == 1)
			{
				this.setUseRealTimeInput(false);
				((Input) this.getCurInput()).pause(false);
			}
			else
			{
				this.setUseRealTimeInput(true);

				for(int i = 0; i < this.playableInputs.length; i++)
				{
					if(this.playableInputs[i].getInputType() == "Recorded Input")
					{
						((Input) this.playableInputs[i]).pause(true);
					}
				}//for loop

			}//else

		}

		if(theEvent.getName() == "pause"  && !this.useRealTimeInput)
		{
			if(theEvent.getValue() == 1)
			{
				((Input) this.getCurInput()).pause(true);
			}
			else
			{
				((Input) this.getCurInput()).pause(false);
			}
		}

		if(theEvent.getName() == "playableInput")
		{
			theEvent.getController().bringToFront();

			if(this.controlP5.getController("play").getValue() == 1)
			{				
				this.controlP5.getController("play").setValue(0);
			}
			MusicalInput curPlayableInput = this.playableInputs[(int) this.controlP5.getController("playableInput").getValue()];
			if(!curPlayableInput.equals(null))
				((Textlabel) this.controlP5.get("playableInfo")).setText(this.makeInfoString(curPlayableInput));
		}

		if(theEvent.getName() == "realTimeInput")
		{
			theEvent.getController().bringToFront();

			MusicalInput curRealTimeInput = this.realTimeInputs[(int) this.controlP5.getController("realTimeInput").getValue()];
			if(!curRealTimeInput.equals(null))
				((Textlabel) this.controlP5.get("realTimeInfo")).setText(this.makeInfoString(curRealTimeInput));

			//If MIDI is selected
			if(theEvent.getValue() == 1)
			{
				((Textlabel) this.controlP5.get("realTimeInfo")).hide();
				this.controlP5.getController("polyMidiButton").show();
				this.controlP5.getController("monoMidiTypeButton").show();
			}
			else
			{
				((Textlabel) this.controlP5.get("realTimeInfo")).show();
				this.controlP5.getController("polyMidiButton").hide();
				this.controlP5.getController("monoMidiTypeButton").hide();
			}
		}

		if(theEvent.getName() == "polyMidiButton")
		{
			if(theEvent.getLabel() == "Polyphonic")
			{
				theEvent.getController().setLabel("Monophonic");
				this.controlP5.getController("monoMidiTypeButton").setColorBackground(theEvent.getController().getColor().getBackground());
				this.controlP5.getController("monoMidiTypeButton").setColorForeground(theEvent.getController().getColor().getForeground());
				this.controlP5.getController("monoMidiTypeButton").setColorActive(theEvent.getController().getColor().getActive());

				((MidiStreamInput) this.realTimeInputs[1]).setIsPolyphonic(false);
			}
			else
			{
				theEvent.getController().setLabel("Polyphonic");
				this.controlP5.getController("monoMidiTypeButton").setColorBackground(Color.DARK_GRAY.getRGB());
				this.controlP5.getController("monoMidiTypeButton").setColorForeground(Color.GRAY.getRGB());
				this.controlP5.getController("monoMidiTypeButton").setColorActive(Color.LIGHT_GRAY.getRGB());

				((MidiStreamInput) this.realTimeInputs[1]).setIsPolyphonic(true);
			}


		}

		if(theEvent.getName() == "monoMidiTypeButton")
		{
			int index = 0;
			while(index < this.monoMidiTypeButtonText.length && this.monoMidiTypeButtonText[index] != theEvent.getController().getLabel())
			{
				index++;
			}

			if(index == this.monoMidiTypeButtonText.length) throw new IllegalArgumentException("error with monoMidiTypeButton");

			index = (index + 1) % (this.monoMidiTypeButtonText.length);
			theEvent.getController().setLabel(this.monoMidiTypeButtonText[index]);
			((MidiStreamInput)this.realTimeInputs[1]).setMonophonicType(index);
		}

		if(theEvent.getName() == "Key Change" || theEvent.getName() == "Keys")
		{
			//System.out.println("FRONT");
			theEvent.getController().bringToFront();
		}
		
	}//ControlEvent

	@Override
	public void sliderEvent(int id, float val) {
		// TODO Auto-generated method stub

	}



	@Override
	public void buttonEvent(int id) {
		// TODO Auto-generated method stub

	}



	@Override
	public void colorWheelEvent(int id, Color color) {
		// TODO Auto-generated method stub

	}

	public String getCurRealTimeInput()
	{
		ScrollableList controller = (ScrollableList) this.controlP5.getController("realTimeInput");
		int val = (int) controller.getValue();

		String itemText = controller.getItem(val).toString();

		int textStart = itemText.indexOf("text=") + 5;
		int textEnd = itemText.indexOf(',', textStart);

		String listText = itemText.substring(textStart, textEnd);

		return listText;
	}

	public String getCurPlayableInput()
	{
		ScrollableList controller = (ScrollableList) this.controlP5.getController("playableInput");
		int val = (int) controller.getValue();

		String itemText = controller.getItem(val).toString();

		int textStart = itemText.indexOf("text=") + 5;
		int textEnd = itemText.indexOf(',', textStart);

		String listText = itemText.substring(textStart, textEnd);

		return listText;
	}

	public void addMusicalInput(MusicalInput musicalInput)
	{
		ScrollableList inputList = ((ScrollableList) this.controlP5.get("playableInput"));

		if(musicalInput.isRealTimeInput())
		{
			inputList = ((ScrollableList) this.controlP5.get("realTimeInput"));
		}

		inputList.addItem(  "" + musicalInput.getInputName() + "   ( " + musicalInput.getInputType() + " )", inputList.getItems().size());



		MusicalInput[] oldList = this.playableInputs;
		MusicalInput[] newList;

		if(musicalInput.isRealTimeInput())
		{
			oldList = this.realTimeInputs;
		}

		newList = new MusicalInput[oldList.length + 1];

		for(int i = 0; i < oldList.length; i++)
		{
			newList[i] = oldList[i];
		}

		newList[oldList.length] = musicalInput;

		if(musicalInput.isRealTimeInput())
		{
			this.realTimeInputs = newList;
		}
		else
		{
			this.playableInputs = newList;
		}

	}

	private void makeControls()
	{
		this.controlP5.addLabel("Real Time Inputs", 30, this.parent.height/3)
		.setTab(this.getMenuTitle());

		this.controlP5.addLabel("realTimeInfo")
		.setTab(this.getMenuTitle())
		.setMultiline(true)
		.setPosition(40, this.parent.height/3 + 60)
		.setSize(this.parent.width/3 - 50, this.parent.height/3 - 40)
		.setText("info here");

		this.controlP5.addScrollableList("realTimeInput")
		.setPosition(30, this.parent.height/3 + 15)
		.setWidth(250)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());

		this.controlP5.addLabel("Playable Inputs (Play Button)", 30, this.parent.height * 2/3)
		.setTab(this.getMenuTitle());

		this.controlP5.addLabel("playableInfo")
		.setTab(this.getMenuTitle())
		.setMultiline(true)
		.setPosition(40, this.parent.height*2/3 + 60)
		.setSize(this.parent.width/3 - 50, this.parent.height/3 - 40)
		.setText("info here");

		this.controlP5.addScrollableList("playableInput")
		.setPosition(30, this.parent.height * 2/3 + 15)
		.setWidth(250)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());

		this.controlP5.addScrollableList("Key Change")
		.setPosition(550, 10)
		.setWidth(100)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"})
		.setValue(0)
		.close()
		.setTab(this.getMenuTitle());


		this.controlP5.addScrollableList("Keys")
		.setPosition(670, 10)

		.setWidth(100)
		.setBarHeight(30)
		.setItemHeight(30)
		.setItems(new String[] {"Major", "Minor", "Harmonic Minor", "Melodic Minor", "Major Pentatonic", "Minor Pentatonic", "Chromatic"})
		.setValue(6)
		.close()
		.setTab(this.getMenuTitle());

		this.controlP5.addButton("polyMidiButton")
		.setPosition(30, 230)
		.setSize(100, 30)
		.setTab(this.menuTitle)
		.setLabel("Monophonic");

		this.controlP5.addButton("monoMidiTypeButton")
		.setPosition(30, 270)
		.setSize(100, 30)
		.setTab(this.menuTitle)
		.setLabel("Last");

		this.controlP5.addToggle("Legend")
		.setPosition(450, 15)
		.setSize(50, 25)
		.setValue(1)
		.setCaptionLabel("Legend")
		.setTab(this.getMenuTitle())
		.getCaptionLabel().align(ControlP5.CENTER, ControlP5.CENTER);
		
		
		this.controlP5.addSlider("forte")
		.setMin(20)
		.setMax(5000)
		.setPosition(10, 70)
		.setSize(140, 20)
		.setValue(1000)
		.setDecimalPrecision(0)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().setVisible(false);
		
		this.controlP5.addLabel("Forte Threshold")
		.setPosition(10, 60)
		.setColor(255)
		.setTab(this.getMenuTitle());
		
		
		this.controlP5.addSlider("piano")
		.setMin(0)
		.setMax(100)
		.setPosition(10, 120)
		.setSize(140, 20)
		.setValue(1)
		.setDecimalPrecision(0)
		.setTab(this.getMenuTitle())
		.getCaptionLabel().setVisible(false);
		
		this.controlP5.addLabel("Piano Threshold")
		.setPosition(10, 110)
		.setColor(255)
		.setTab(this.getMenuTitle());
	}
	
	public int[] getScale()
	{
		int[] currentScale;
		ScrollableList controller1 = (ScrollableList) this.controlP5.getController("Key Change");
		int key = (int) controller1.getValue();

		ScrollableList controller2 = (ScrollableList) this.controlP5.getController("Keys");
		int scale = (int) controller2.getValue();

		if(scale == 0)//major
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = (currentScale[0] + 2)% 8;
			currentScale[2] = (currentScale[1] + 2)% 8;
			currentScale[3] = (currentScale[2] + 1)% 8;
			currentScale[4] = (currentScale[3] + 2)% 8;
			currentScale[5] = (currentScale[4] + 2)% 8;
			currentScale[6] = (currentScale[5] + 2)% 8;
			currentScale[7] = (currentScale[6] + 1)% 8;
		}
		else if(scale == 1)//minor
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = (key + 2)% 8;
			currentScale[2] = (key + 1)% 8;
			currentScale[3] = (key + 2)% 8;
			currentScale[4] = (key + 2)% 8;
			currentScale[5] = (key + 1)% 8;
			currentScale[6] = (key + 2)% 8;
			currentScale[7] = (key + 2)% 8;
		}
		else if(scale == 2)//harmonic minor
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = (key + 2)% 8;
			currentScale[2] = (key + 1)% 8;
			currentScale[3] = (key + 2)% 8;
			currentScale[4] = (key + 2)% 8;
			currentScale[5] = (key + 1)% 8;
			currentScale[6] = (key + 2)% 8;
			currentScale[7] = (key + 1)% 8;
		}
		else if(scale == 3)//melodic minor
		{
			currentScale = new int[8];
			currentScale[0] = key;
			currentScale[1] = (currentScale[0] + 2) % 8;
			currentScale[2] = (currentScale[1] + 1) % 8;
			currentScale[3] = (currentScale[2] + 2) % 8;
			currentScale[4] = (currentScale[3] + 2) % 8;
			currentScale[5] = (currentScale[4] + 2) % 8;
			currentScale[6] = (currentScale[5] + 2) % 8;
			currentScale[7] = (currentScale[6] + 1) % 8;
		}
		else if(scale == 4)//major pentatonic
		{
			currentScale = new int[6];
			currentScale[0] = key;
			currentScale[1] = (currentScale[0] + 2) % 6;
			currentScale[2] = (currentScale[1] + 2) % 6;
			currentScale[3] = (currentScale[2] + 3) % 6;
			currentScale[4] = (currentScale[3] + 2) % 6;
			currentScale[5] = (currentScale[4] + 3) % 6;
		}
		else if(scale == 5)//minor pentatonic
		{
			currentScale = new int[6];
			currentScale[0] = key;
			currentScale[1] = (currentScale[0] + 3) % 6;
			currentScale[2] = (currentScale[1] + 2) % 6;
			currentScale[3] = (currentScale[2] + 2) % 6;
			currentScale[4] = (currentScale[3] + 3) % 6;
			currentScale[5] = (currentScale[4] + 2) % 6;
		}
		else//chromatic
		{
			currentScale = new int[12];
			currentScale[0] = key;
			currentScale[1] = (currentScale[0] + 1)% 12;
			currentScale[2] = (currentScale[1] + 1)% 12;
			currentScale[3] = (currentScale[2] + 1)% 12;
			currentScale[4] = (currentScale[3] + 1)% 12;
			currentScale[5] = (currentScale[4] + 1)% 12;
			currentScale[6] = (currentScale[5] + 1)% 12;
			currentScale[7] = (currentScale[6] + 1)% 12;
			currentScale[8] = (currentScale[7] + 1)% 12;
			currentScale[9] = (currentScale[8] + 1)% 12;
			currentScale[10] = (currentScale[9] + 1)% 12;
			currentScale[11] = (currentScale[10] + 1)% 12;
		}
		return currentScale;
	}

	public void legend()
	{ 
		int inputNum;
		String[] legendText;
		int[] scale;
		int rectWidths;
		int rectHeights;
		int xVals;
		int yVals;
		int note;

		scale = this.getScale();
		inputNum = scale.length;
		legendText = this.scaleLetters(scale);
		rectHeights =  50;
		rectWidths = parent.width / inputNum;
		xVals = 0;
		yVals = parent.height - rectHeights;
		Canvas canvas = this.driver.getCanvas();
		note = this.getMidiNote();

		//Why the +1 here?
		//Removed it because I don't think it was doing anything
		for(int i = 0; i < inputNum; i++)
		{ 
			ColorScheme[] schemes = this.driver.getColorMenu().getColorSchemes();
			int[] colors = schemes[0].getPitchColor(i);
			this.parent.fill(colors[0], colors[1], colors[2]);
			this.parent.noStroke();
			if((note%12) == i && this.getAmplitude() > this.controlP5.getController("piano").getValue())
			{
				
				canvas.rect(xVals, (yVals - 30), rectWidths, (rectHeights+ 30));
			}
			else
			{
				canvas.rect(xVals, yVals, rectWidths, rectHeights);
			}
			xVals = xVals + rectWidths;
		}
		
		
		
		
		/*    FONT STUFF
		
		String[] fontList = PFont.list();
		
		for(int i = 0; i < fontList.length; i++)
		{
			System.out.println(fontList[i]);
		}
		*/
		
		this.parent.fill(0,0,0);
		this.parent.noStroke();
		this.parent.textAlign(CENTER, CENTER);
		
		xVals = rectWidths/2;
		for(int i = 0; i < inputNum; i++)
		{ 
			
			this.driver.getCanvas().text(14, legendText[i], xVals, parent.height - (rectHeights/2));
			xVals = xVals + rectWidths;
		}

	} // legend

	private String[] scaleLetters(int[] scale)
	{
		String[] newScale;
		String[] letters;
		int number;

		letters = new String[] {"C", "C#/Db","D", "D#/Eb", "E", "F","F#/Gb", "G", "G#/Ab", "A", "A#/Bb", "B"};
		newScale = new String[scale.length];
		number = 0; 
		for(int i = 0; i < scale.length; i++)
		{
			number = scale[i];
			newScale[i] = letters[number];

		}
		return newScale;
	}
	private void calculateLegendValues(int numinputs)
	{

	}

	private String makeInfoString(MusicalInput input)
	{
		String info = "";

		info += "Number of Inputs:   " + input.getTotalNumInputs() +"\n\n";
		info += "Polyphonic?         " + input.isPolyphonic() +"\n\n";

		return info;
	}

}
