package functionSketch_01;

import java.util.Map;

import controlP5.*;
import processing.core.PApplet;
import core.Melody;
import core.Instrument;

public class Controls extends PApplet 
{
	/*
    6/9/2017
    Danny Mahota

    This Class controls the user window.


	 */

	private ControlP5 cp5;
	Melody            melody;
	Instrument	      instrument;


	public static void main(String[] args)
	{
		PApplet.main("functionSketch_01.Controls");
	}// main

	public void settings()
	{
		size(300, 450);
	}

	public void setup()
	{
		//Create ControlP5 object to build controls on
		this.cp5 = new ControlP5(this);
		//Create a group to put all of the controls in
		new Group(cp5, "gtGroup");
		//Sets up all of the ControlP5 controls
		this.setUpControls();

		this.melody	= new Melody(this); 
		this.instrument	= new Instrument(this);
		//melody.playMelody("E", 20, "minor", 4, instrument);

	}

	public void draw()
	{
		//Keep refreshing the black background
		background(0);
		/*
		instrument.gainGlide.setValue(this.mouseX / (float)this.width);
		 // update the frequency based on the position of the mouse
		 // cursor within the Processing window
		instrument.frequencyGlide.setValue(this.mouseY);
		 */
	}

	private void setUpControls()
	{
		//Name all of the drop box lists to be created
		String[] names = new String[] { "adsr", "range", "key", "scale" } ;


		//Position of first box
		int  x = 30;
		int  y = 300;
		int  spacing = 50;

		//set up bpm box
		cp5.addTextlabel("bpmLabel")
		.setPosition(x, y)
		.setText("bpm");

		cp5.addTextfield("bpmText")
		.setPosition(x+80, y)
		.setSize(170, 22)
		.setText("120")
		.setAutoClear(false)
		.getCaptionLabel()
		.setVisible(false);

		//set up play, pause and stop buttons
		cp5.addButton("playButton")
		.setLabel("Play")
		.setPosition(50, 350)
		.setSize(50, 50);

		cp5.addButton("pauseButton")
		.setLabel("Pause")
		.setPosition(125, 350)
		.setSize(50, 50)
		.setSwitch(false);

		cp5.addButton("stopButton")
		.setLabel("Stop")
		.setPosition(200, 350)
		.setSize(50, 50);

		//Initializes a drop box and label for each element in names String[]
		//lists and dropboxes are named after what they store, followed by "List" or "Label".  Example: keyList or rangeLabel.
		for(int i = 0; i < names.length; i++)
		{
			//move the y coordinate down for the next box
			y = y - spacing;

			//Make Label
			cp5.addTextlabel(names[i] + "Label")
			.setPosition(x,y)
			.setText(names[i]);

			//Make ScrollableList
			cp5.addScrollableList(names[i] + "List")
			.setPosition(x + 80, y)
			.bringToFront()
			.setOpen(false);

			//set the list's style
			this.customizeList( (ScrollableList) cp5.get(names[i] + "List"));

			//Fill the list
			this.fillList( names[i] + "List" );

			//Add label and list to GuideTone Group
			/*
			cp5.get("gtGroup")
				.add(cp5.get(names[i] + "Label"));

			cp5.get("gtGroup")
				.add(cp5.get(names[i] + "List"));
			 */

		}//for()



	}//setUpControls()

	private void customizeList(ScrollableList sr)
	{
		//sr.setBackgroundColor(color(190));
		sr.setWidth(170);
		sr.setItemHeight(22);
		sr.setBarHeight(22);
		sr.setColorBackground(color(60));
		sr.setColorActive(255);
	}

	private void fillList(String listName)
	{
		//This method checks what list was passed to it and then fills the list it was passed with the appropriate items

		ScrollableList sr = (ScrollableList) ( cp5.get(listName));

		System.out.println(listName);
		System.out.println(sr);


		switch(listName){
		case "scaleList":
			sr.addItems(new String[] { "Major", "Minor", "Chromatic" })
			.setValue(0f);

			//sr.setValue(0f);
			break;

		case "keyList":
			sr.addItems( new String[] { "A","A# / Bb", "B", "C", "C# / Db","D", "D# / Eb","E","F","F# / Gb","G","G# / Ab", } )
			.setValue(0f);
			break;

		case "rangeList":
			//implement with Melody class - TODO: is this comment still relevant?
			sr.addItems( new String[] {
					"A3 (110 Hz) - G#3 (207.65 Hz)",
					"A4 (220 Hz) - G#4 (415.3 Hz)",
					"A5 (440 Hz) - G#4 (830.6 Hz)",
					"A6 (880 Hz) - G#4 (1661.2 Hz)",
					"A7 (1760 Hz) - G#4 (3322.4 Hz)",
			} )
			.setValue(0f);
			break;

		case "adsrList":
			//implement with Beads Instrument
			sr.addItems(new String[] {
					"Even",
					"Long Attack",
					"Long Decay/Low Sustain",
					"Long Release"
			})
			.setValue(0f);
			break;

		default:
			System.out.println("error with switch: private void fillList(String listName, ScrollableList sr) ");
			System.out.println("List Name: " + listName);
			System.out.println("Scrollable List: " + sr);
			break;

		}//Switch



	}// fillList()


	public void controlEvent(ControlEvent theEvent) 
	{	
		String name = theEvent.getName();

		switch(name)
		{
		case "scaleList":
//			this.fillList("rangeList");
			break;

		case "keyList":
//			this.fillList("rangeList");
			break;

		case "rangeList":

			break;

		case "adsrList":

			break;

		case "bpmText":
			// 
			String bpm = cp5.get("bpmText").getStringValue();
			int    bpmInt = 120;
			try
			{
				bpmInt = Integer.parseInt(bpm);
			}
			catch (NumberFormatException e) { ((Textfield) cp5.get("bpmText")).setText("120"); }
			catch (NullPointerException e) { ((Textfield) cp5.get("bpmText")).setText("120"); }

			if (bpmInt < 40) { ((Textfield) cp5.get("bpmText")).setText("40"); }
			if (bpmInt > 200) { ((Textfield) cp5.get("bpmText")).setText("200"); }

			break;

		case "playButton":

			// get key:
			int val = (int) cp5.get("keyList").getValue();
			Map<String, Object> map = (Map<String, Object>) cp5.get(ScrollableList.class, "keyList").getItem(val);
			String	key	= (String) map.get("name");

			// get scale:
			val = (int) cp5.get("scaleList").getValue();
			map = (Map<String, Object>) cp5.get(ScrollableList.class, "scaleList").getItem(val);
			String scale = (String) map.get("name");
			
			// get range octave:
			int	rangeOctave = (int) ( cp5.get("rangeList").getValue() + 3);
			
			// get adsr presets:
			val = (int) cp5.get("adsrList").getValue();
			this.instrument.setADSR(val);

			// get bpm:
			bpmInt = Integer.parseInt(cp5.get("bpmText").getStringValue());

			this.melody.playMelody(key, bpmInt, scale, rangeOctave, this.instrument);

			break;

		case "pauseButton":
			if(!((Button) cp5.get("pauseButton")).isSwitch()) { this.melody.pause(true); ((Button) cp5.get("pauseButton")).setSwitch(true); System.out.println("pause"); }
			else { this.melody.pause(false); ((Button) cp5.get("pauseButton")).setSwitch(false); System.out.println("resume"); }
			break;

		case "stopButton":
			this.melody.stop();

			break;

		default:
			System.out.println("error with switch: controlEvent() ");
			break;
		}
	}//controlEvent()








}//Controls
