package functionSketch_06;

import java.awt.Color;

import controlP5.ControlEvent;
import core.input.MidiStreamInput;
import coreV2.Canvas;
import coreV2.ColorScheme;
import coreV2.ModuleDriver;
import processing.core.PApplet;
import processing.event.KeyEvent;

public class TestModule extends PApplet
{	
	private ModuleDriver driver;

	private Canvas canvas;
	
	private ModuleDriver moduleDriver;




	int startTime = this.millis();

	Color ogColor = new Color(81, 12, 118);
	Color boltColor = this.ogColor;

	int boxHeight = 200;
	int boxWidth = 100;

	int boxIncrament = 30;

	int boxXPos = 0;
	int boxYPos = this.height/2;

	int[][] coordinateBuffer;

	int randX;
	int randY;

	int index = 1;
	
	int resetCount = 0;
	
	float fillColor = 0;



	public static void main(String[] args)
	{
		PApplet.main("coreV2.TestModule");
	}

	public void settings()
	{
		this.size(925, 520);
	}

	public void setup()
	{
		this.driver = new ModuleDriver(this);
		this.canvas = this.driver.getCanvas();
		
		this.coordinateBuffer = new int[(int) Math.ceil(this.width/this.boxIncrament)][2];

		this.boxYPos = height/2;
		
		this.strokeWeight(10);
		this.textSize(70);
		
		coordinateBuffer[0][0] = 0;
		coordinateBuffer[0][1] = boxYPos;
		
		coordinateBuffer[coordinateBuffer.length - 1][0] = 0;
		coordinateBuffer[coordinateBuffer.length - 1][1] = boxYPos;
		
		for(int i = 0; i < coordinateBuffer.length; i++)
		{
			randX = (int) (Math.random()*boxWidth);
			randY = (int) (Math.random()*boxHeight) - (boxHeight/2);

			coordinateBuffer[i][0] = (i*boxIncrament) + randX;
			coordinateBuffer[i][1] = boxYPos + randY;
		}


	}

	public void draw()
	{
		this.randX = (int) (Math.random()*boxWidth);
		this.randY = (int) (Math.random()*boxHeight) - (boxHeight/2);

		this.coordinateBuffer[this.index][0] = (this.index*boxIncrament) + randX;
		this.coordinateBuffer[this.index][1] = boxYPos + randY;
		
		
		background(0);
		
		this.strokeWeight(10);

		boltColor = ogColor;
		
		int halfLife = 1;

		for(int i2 = 0; i2 < this.coordinateBuffer.length; i2++)
		{
			this.stroke(boltColor.getRed(), boltColor.getGreen(), boltColor.getBlue());

			if(index-i2 != 0)
			{
				canvas.line(this.coordinateBuffer[(index + this.coordinateBuffer.length - i2)%this.coordinateBuffer.length][0], 
						this.coordinateBuffer[(index + this.coordinateBuffer.length - i2)%this.coordinateBuffer.length][1], 
						this.coordinateBuffer[(index + this.coordinateBuffer.length - i2 - 1)%this.coordinateBuffer.length][0], 
						this.coordinateBuffer[(index + this.coordinateBuffer.length - i2 - 1)%this.coordinateBuffer.length][1]);
			}

			
			if(i2 > halfLife)
			{
				boltColor = boltColor.darker();
				halfLife = 2*halfLife + 1;
			}
		}
		
		if(fillColor < 255)
			fillColor += (.25 + fillColor/25);
		
		fill(fillColor);
		stroke(fillColor);
		
		
		this.textSize(70);
		this.text("CODE+CHORDS", 200, height/2 + 30);
		
		this.strokeWeight(4);
		this.line(210, height/2 + 40, 730, height/2 + 40);
		
		this.textSize(30);
		this.text("Playful Learning Lab", 400, height/2 + 80);
		
		this.index++;
		
		if(this.index == this.coordinateBuffer.length - 1)
		{
			this.index = 1;
			this.resetCount++;
		}
		
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void drawLoadingScreen()
	{

		for(int i = 0; i < coordinateBuffer.length; i++)
		{
			randX = (int) (Math.random()*boxWidth);
			randY = (int) (Math.random()*boxHeight) - (boxHeight/2);

			coordinateBuffer[i][0] = (i*boxIncrament) + randX;
			coordinateBuffer[i][1] = boxYPos + randY;

			this.fill(0);
			this.canvas.background();

			boltColor = ogColor;

			for(int i2 = i; i2 > 1; i2--)
			{
				this.stroke(boltColor.getRGB());

				this.canvas.line(coordinateBuffer[i2][0], coordinateBuffer[i2][1], coordinateBuffer[i2 - 1][0], coordinateBuffer[i2 - 1][1]);
				boltColor = boltColor.darker();
			}


			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@SuppressWarnings("unused")
	public void fake()
	{
		int midiNoteFromInputHandler = 0;
		
		// make an int array to store a single RGB value 
		int[] pitchColor; 

		// get the current color scheme. (the object that stores the color associated with each pitch) 
		ColorScheme colors = this.moduleDriver.getColorMenu().getColorSchemes()[0]; 

		// get the midi note number of the current note being played 
		int curMidiNote = midiNoteFromInputHandler; 

		// use the note number to get the associated color and store the color 
		pitchColor = colors.getPitchColor(curMidiNote); 
	}


}
