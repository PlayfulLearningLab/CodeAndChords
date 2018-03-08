package mutlipleLinesTests;

import processing.core.PApplet;
import processing.sound.Amplitude;
import processing.sound.AudioIn;

public class Test1 {
	
	PApplet	parent;
	AudioIn	input1;
	
	public Test1(PApplet parent)
	{
		this.parent	= parent;
		this.input1	= new AudioIn(this.parent, 1);
		
		input1.play();
	} // constructor
	
	public void ampEllipse()
	{
		Amplitude	amp	= new Amplitude(this.parent);
		this.parent.ellipse(this.parent.width, (amp.analyze() * 100), 50, 50);
	}

}
