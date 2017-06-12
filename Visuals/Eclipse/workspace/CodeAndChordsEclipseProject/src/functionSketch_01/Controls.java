package functionSketch_01;

import controlP5.*;
import processing.core.PApplet;

public class Controls extends PApplet 
{
	/*
    6/9/2017
    Danny Mahota

    This Class controls the user window.
    
    
	 */
	
	private ControlP5 cp5;
	
	
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
		
		
	}
	
	public void draw()
	{
		//Keep refreshing the black background
		background(0);
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
				sr.addItems( new String[] { "C", "C# / Db","D", "D# / Eb","E","F","F# / Gb","G","G# / Ab","A","A# / Bb","B" } )
				.setValue(0f);
				break;
				
			case "rangeList":
				//implement with Melody class
				break;
				
			case "adsrList":
				//implement with Beads Instrument
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
			this.fillList("rangeList");
			break;
			
		case "keyList":
			this.fillList("rangeList");
			break;
			
		case "rangeList":
			
			break;
			
		case "adsrList":
			
			break;
			
		case "bpmText":
			
			break;
			
		default:
			System.out.println("error with switch: controlEvent() ");
			break;
		}
	}//controlEvent()
	
	
	
	
	
	
	
	
}//Controls
