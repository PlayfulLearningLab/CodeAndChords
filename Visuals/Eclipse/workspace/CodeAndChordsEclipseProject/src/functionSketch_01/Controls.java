package functionSketch_01;

import java.awt.Color;
import java.util.Map;

import controlP5.*;
import processing.core.PApplet;
import processing.core.PFont;
import core.Melody;
import core.Instrument;

public class Controls extends PApplet 
{
	/*
    6/9/2017
    Danny Mahota

    This Class controls the user window.


	 */

	private ControlP5         cp5;
	private Melody            melody;
	private Instrument	      instrument;
	
	private int               defaultForeground;
	private int               defaultBackground;
	private int               defaultActive;


	public static void main(String[] args)
	{
		PApplet.main("functionSketch_01.Controls");
	}// main

	public void settings()
	{
		size(700, 450);
	}

	public void setup()
	{
		//Create ControlP5 object to build controls on
		this.cp5 = new ControlP5(this);
		//Create a group to put all of the controls in
		new Group(cp5, "gtGroup");
		
		// Initialize melody before calling setUpControls, b/c the latter uses melody.getRangeList():
		this.melody	= new Melody(this); 
		this.instrument	= new Instrument(this);
		//Sets up all of the ControlP5 controls
		this.setUpControls();

		//melody.playMelody("E", 20, "minor", 4, instrument);

	}

	public void draw()
	{
		//Keep refreshing the black background
		background(0);
		
		if(!  cp5.get("bpmText").getStringValue().equals(((Textfield) cp5.get("bpmText")).getText())  )
		{
			cp5.get("bpmText").setColorForeground(Color.RED.getRGB());
			cp5.get("bpmText").setColorActive(Color.RED.getRGB());
		}else
		{
			cp5.get("bpmText").setColorForeground(this.defaultForeground);
			cp5.get("bpmText").setColorActive(this.defaultActive);
		}
		
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
		String[] names = new String[] { "range", "key", "scale" } ;
		
		PFont pfont = createFont("Arial",20,true); // use true/false for smooth/no-smooth
		ControlFont largerStandard = new ControlFont(pfont,13);
		//ControlFont    largerStandard    = new ControlFont(ControlP5.BitFontStandard58, 13);

		//Volume Slider:
		cp5.addLabel("volumeLabel")
		.setFont(largerStandard)
		.setText("volume")
		.setPosition(30, 250);
		
		cp5.addSlider("volumeSlider", 0f, 1f, .8f, 110, 250, 170, 22)
		.getCaptionLabel()
		.setVisible(false);
		
		
		//Position of first box
		int  x = 30;
		int  y = 200;
		int  spacing = 50;

		//set up bpm box
		cp5.addTextlabel("bpmLabel")
		.setFont(largerStandard)
		.setPosition(x, y)
		.setText("bpm");

		cp5.addTextfield("bpmText")
		//.setColorActive(Color.RED.getRGB())
		.setPosition(x+80, y)
		.setSize(170, 22)
		.setStringValue("120")
		.setText("120")
		.setAutoClear(false)
		.getCaptionLabel()
		.setVisible(false);

		//set up play, pause and stop buttons
		cp5.addButton("playButton")
		.setLabel("Play")
		.setPosition(150, 320)
		.setSize(100, 100);

		cp5.addButton("pauseButton")
		.setLabel("Pause")
		.setPosition(300, 320)
		.setSize(100, 100)
		.setSwitch(false);

		cp5.addButton("stopButton")
		.setLabel("Stop")
		.setPosition(450, 320)
		.setSize(100, 100);

		//Initializes a drop box and label for each element in names String[]
		//lists and dropboxes are named after what they store, followed by "List" or "Label".  Example: keyList or rangeLabel.
		for(int i = 0; i < names.length; i++)
		{
			//move the y coordinate down for the next box
			y = y - spacing;

			//Make Label
			cp5.addTextlabel(names[i] + "Label")
			.setFont(largerStandard)
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
		
		cp5.addGroup("adsr");
		
		cp5.addLabel("attackLabel")
		.setFont(largerStandard)
		.setText("attack")
		.setPosition(350, 100);
		
		cp5.addSlider("attackSlider", 0.01f, 1000f, 100f, 430, 100, 170, 22)
		.setGroup("adsr")
		.getCaptionLabel()
		.setVisible(false);
		
		cp5.addLabel("decayLabel")
		.setFont(largerStandard)
		.setText("decay")
		.setPosition(350, 150);
		
		cp5.addSlider("decaySlider", 0.01f, 1000f, 500f, 430, 150, 170, 22)
		.setGroup("adsr")
		.getCaptionLabel()
		.setVisible(false);
		
		cp5.addLabel("sustainLabel")
		.setFont(largerStandard)
		.setText("sustain")
		.setPosition(350, 200);
		
		cp5.addSlider("sustainSlider", 0f, 100f, 50f, 430, 200, 170, 22)
		.setGroup("adsr")
		.getCaptionLabel()
		.setVisible(false);
		
		cp5.addLabel("releaseLabel")
		.setFont(largerStandard)
		.setText("release")
		.setPosition(350, 250);
		
		cp5.addSlider("releaseSlider", 0f, 1000f, 100f, 430, 250, 170, 22)
		.setGroup("adsr")
		.getCaptionLabel()
		.setVisible(false);
		
		this.defaultForeground = cp5.get("releaseSlider").getColor().getForeground();
		this.defaultBackground = cp5.get("releaseSlider").getColor().getBackground();
		this.defaultActive = cp5.get("releaseSlider").getColor().getActive();
		
		cp5.addLabel("adsrLabel")
		.setFont(largerStandard)
		.setText("adsr")
		.setPosition(350,50);
		
		cp5.addScrollableList("adsrList")
		.setPosition(430,50)
		.bringToFront()
		.setOpen(false);

		this.customizeList( (ScrollableList) cp5.get("adsrList"));
		this.fillList( "adsrList" );


	}//setUpControls()

	private void customizeList(ScrollableList sr)
	{
		//sr.setBackgroundColor(color(190));
		sr.setWidth(170);
		sr.setItemHeight(22);
		sr.setBarHeight(22);
		//sr.setColorBackground(color(60));
		//sr.setColorActive(255);
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
			sr.addItems( new String[] {  "C", "C# / Db","D", "D# / Eb","E","F","F# / Gb","G","G# / Ab","A","A# / Bb", "B" } )
			.setValue(0f);
			break;

		case "rangeList":
			this.melody.setRangeList();
			sr.addItems( this.melody.getRangeList() )
			.setValue(0f);
			break;

		case "adsrList":
			sr.addItems(new String[] {
					"Custom",
					"Even",
					"Long Attack",
					"Long Decay",
					"High Sustain",
					"Low Sustain",
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


	public void controlEvent(ControlEvent theEvent) throws InterruptedException 
	{	
		String name = theEvent.getName();

		switch(name)
		{
		case "scaleList":
			// get scale:
			int val = (int) cp5.get("scaleList").getValue();
			Map<String, Object>map = (Map<String, Object>) cp5.get(ScrollableList.class, "scaleList").getItem(val);
			String scale = (String) map.get("name");
			this.melody.setScale(scale);
			this.fillRangeList();

			break;

		case "keyList":
			// get key:
			val = (int) cp5.get("keyList").getValue();
			map = (Map<String, Object>) cp5.get(ScrollableList.class, "keyList").getItem(val);
			String	key	= (String) map.get("name");
			this.melody.setKey(key);
			this.fillRangeList();

			break;

		case "rangeList":

			break;

		case "adsrList":
			
			if(0 == (int) cp5.get("adsrList").getValue())
			{
				cp5.getGroup("adsr").setColorForeground(this.defaultForeground);
				cp5.getGroup("adsr").setColorBackground(this.defaultBackground);
				cp5.getGroup("adsr").setColorActive(this.defaultActive);
				
				((Slider) cp5.get("attackSlider")).unlock();
				((Slider) cp5.get("decaySlider")).unlock();
				((Slider) cp5.get("sustainSlider")).unlock();
				((Slider) cp5.get("releaseSlider")).unlock();

			}else 
			{
				cp5.getGroup("adsr").setColorBackground(Color.DARK_GRAY.getRGB());
				cp5.getGroup("adsr").setColorForeground(Color.GRAY.getRGB());
				cp5.getGroup("adsr").setColorActive(Color.GRAY.getRGB());
				
				this.instrument.setADSR( (int) cp5.get("adsrList").getValue() - 1);
				float[] adsr = this.instrument.getADSR();
				
				((Slider) cp5.get("attackSlider")).setValue(adsr[0]).lock();
				((Slider) cp5.get("decaySlider")).setValue(adsr[1]).lock();
				((Slider) cp5.get("sustainSlider")).setValue(adsr[2]).lock();
				((Slider) cp5.get("releaseSlider")).setValue(adsr[3]).lock();
				
			}
			
			break;
			
		case "volumeSlider":
			this.instrument.setVolume( PApplet.map(cp5.getValue("volumeSlider"), 0, 1, 0, .2f) );
			break;

		case "bpmText":
			// 
			String bpm = cp5.get("bpmText").getStringValue();
			System.out.println("bpm is " + bpm);
			int    bpmInt = 120;
			try
			{
				bpmInt = Integer.parseInt(bpm);
			}
			catch (NumberFormatException e) { ((Textfield) cp5.get("bpmText")).setStringValue("120"); ((Textfield) cp5.get("bpmText")).setValue("120"); }
			catch (NullPointerException e) { ((Textfield) cp5.get("bpmText")).setStringValue("120"); ((Textfield) cp5.get("bpmText")).setValue("120"); }

			if (bpmInt < 40) { ((Textfield) cp5.get("bpmText")).setStringValue("40"); ((Textfield) cp5.get("bpmText")).setValue("40"); }
			if (bpmInt > 200) { ((Textfield) cp5.get("bpmText")).setStringValue("200"); ((Textfield) cp5.get("bpmText")).setValue("200"); }

			break;

		case "playButton":
			
			if(this.melody.isRunning()) 
			{ 
				this.melody.stop(); 
				Thread.sleep(10); 
				System.out.println("stopped");
			}
			
			if(((Button) cp5.get("pauseButton")).isSwitch()) 
			{
				this.melody.pause(false); 
				((Button) cp5.get("pauseButton")).setSwitch(false); 
				((Button) cp5.get("pauseButton")).setLabel("Pause");
				System.out.println("resume"); 
			}
			
			// get key:
			val = (int) cp5.get("keyList").getValue();
			map = (Map<String, Object>) cp5.get(ScrollableList.class, "keyList").getItem(val);
			key	= (String) map.get("name");

			// get scale:
			val = (int) cp5.get("scaleList").getValue();
			map = (Map<String, Object>) cp5.get(ScrollableList.class, "scaleList").getItem(val);
			scale = (String) map.get("name");

			// get range octave:
			int	rangeOctave = (int) ( cp5.get("rangeList").getValue() + 3);

			// get adsr presets:
			val = (int) cp5.get("adsrList").getValue();
			if(val == 0)
			{
				this.instrument.setADSR(cp5.getValue("attackSlider"), cp5.getValue("decaySlider"), cp5.getValue("sustainSlider"), cp5.getValue("releaseSlider"));
			}
			else
			{
				this.instrument.setADSR(val - 1);
			}
				

			// get bpm:
			try{ bpmInt = Integer.parseInt(((Textfield) cp5.get("bpmText")).getStringValue()); } 
			catch(NumberFormatException e) {bpmInt = 120;}
			
			System.out.println("playing this bpm: "  + bpmInt);

			this.melody.playMelody(key, bpmInt, scale, rangeOctave, this.instrument);

			break;

		case "pauseButton":
			
			if(!((Button) cp5.get("pauseButton")).isSwitch()) 
			{ 
				this.melody.pause(true); 
				((Button) cp5.get("pauseButton")).setSwitch(true);
				((Button) cp5.get("pauseButton")).setLabel("Resume");
				System.out.println("paused"); 
			}
			else 
			{ 
				this.melody.pause(false); 
				((Button) cp5.get("pauseButton")).setSwitch(false); 
				((Button) cp5.get("pauseButton")).setLabel("Pause");
				System.out.println("resumed"); 
			}
			
			break;

		case "stopButton":
			float vol = PApplet.map(cp5.getValue("volumeSlider"), 0, 1, 0, .2f);
			this.instrument.setVolume(0);
			
			this.melody.pause(false);
			Thread.sleep(100);
			this.melody.stop();
			((Button) cp5.get("pauseButton")).setLabel("Pause");
			((Button) cp5.get("pauseButton")).setSwitch(false);
			
			this.instrument.setVolume(vol);
			
			

			break;

		default:
			this.instrument.setADSR(cp5.getValue("attackSlider"), cp5.getValue("decaySlider"), cp5.getValue("sustainSlider"), cp5.getValue("releaseSlider"));
			System.out.println("error with switch: controlEvent() ");
			break;
		}
	}//controlEvent()

	/**
	 * Convenience method called in controlEvent when either the key or scale is updated.
	 */
	private void fillRangeList()
	{
		this.melody.setRangeList();
		System.out.println("this.melody.scale = " + this.melody.scale);
		try
		{
			((ScrollableList)this.cp5.getController("rangeList"))
			.setItems(this.melody.getRangeList())
			.setValue(0f);
		} catch(ClassCastException cce) {
			throw new IllegalArgumentException("Controls.fillRangeList(): error casting rangeList Controller to ScrollableList.");
		} // catch
	} // fillRangeList





}//Controls
