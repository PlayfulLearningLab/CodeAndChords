package coreV2_visuals;

import java.lang.reflect.Array;

import controlP5.ControlEvent;
import coreV2.ColorFader;
import coreV2.ModuleDriver;
import coreV2.Visual;

public class GlitchVisual extends Visual{

	private int[][]			shapes;

	private ColorFader[]	fader;
	private int[][][]		colorHistory;
	private int				colorIndex;

	private int[]			nextUpdateTime;

	public GlitchVisual(ModuleDriver moduleDriver) 
	{
		super(moduleDriver, "Glitch");

		this.shapes = new int[1][4];
		this.shapes[0] = new int[]{(int)this.parent.width/2 - 100, (int)this.parent.height/2 - 100, 200, 200};

		this.fader = new ColorFader[] {new ColorFader(this.parent)};

		this.colorHistory = new int[1][10][3];
		for(int i = 0; i < this.colorHistory.length; i++)
		{
			this.colorHistory[0][i] = new int[] {0,0,0};
		}

		this.colorIndex = 0;
		this.nextUpdateTime = new int[] {0};
	}

	@Override
	public void controlEvent(ControlEvent theEvent) {
		if(theEvent.getName()=="numChannels")
		{
			System.out.println(this.moduleDriver.getInputHandler().getNumChannels());
			this.setNumChannels(this.moduleDriver.getInputHandler().getNumChannels());
		}

	}

	@Override
	public void drawVisual() 
	{
		int[][] midiNotes = this.moduleDriver.getInputHandler().getAllMidiNotes();
		
		for(int channel = 0; channel < this.fader.length; channel++)
		{
			this.updateColorHistory(channel, this.colorIndex);
			
			int[] newColor = this.moduleDriver.getColorMenu().getColorSchemes()[0].getPitchColor(midiNotes[channel][0]);
			this.fader[channel].setTargetColor(newColor);

			if(midiNotes[0][1] == -1){
				this.fader[channel].setTargetAlpha(0);
			}
			else{
				this.fader[channel].setTargetAlpha(255);
			}


			this.drawGlitchedObjects(channel);

			this.drawObject(channel);
			this.glitch();
		}
		
		this.colorIndex++;
		this.colorIndex = this.colorIndex%10;

	}

	private void updateColorHistory(int channelNum, int colorIndex)
	{
		int[] newColor = this.fader[channelNum].getColor();
		for(int i = 0; i < 3; i++)
		{
			this.colorHistory[channelNum][colorIndex][i] = newColor[i];
		}


	}

	private void drawGlitchedObjects(int channelNum)
	{
		int[][] midiNotes = this.moduleDriver.getInputHandler().getAllMidiNotes();

		this.parent.noFill();
		this.parent.strokeWeight(6);

		//get current color
		int curColorIndex;

		for(int i = 0; i < this.colorHistory[channelNum].length && midiNotes[channelNum][1] != -1; i++)
		{
			curColorIndex = (this.colorIndex - i + this.colorHistory[channelNum].length)%this.colorHistory.length;

			int rand1 = (int) ((Math.random() - .5) * (100 + i*i));
			int rand2 = (int) ((Math.random() - .5) * (100 + i*i));

			this.parent.stroke(this.colorHistory[channelNum][curColorIndex][0], this.colorHistory[channelNum][curColorIndex][1], this.colorHistory[channelNum][curColorIndex][2], (int)(150 - 15*i));

			this.moduleDriver.getCanvas().rect(this.shapes[channelNum][0] + rand1, this.shapes[channelNum][1] + rand2, this.shapes[channelNum][2], this.shapes[channelNum][3]);
		}

	}

	private void drawObject(int channelNum)
	{
		this.parent.stroke(255, 255);
		this.parent.strokeWeight(6);
		this.parent.noFill();

		int[][] midiNotes = this.moduleDriver.getInputHandler().getAllMidiNotes();
		int rand1 = 0;
		int rand2 = 0;

		if(midiNotes[channelNum][1] != -1)
		{
			rand1 = (int) ((Math.random() - .5) * 20);
			rand2 = (int) ((Math.random() - .5) * 20);
		}

		this.moduleDriver.getCanvas().rect(this.shapes[channelNum][0] + rand1, this.shapes[channelNum][1] + rand2, this.shapes[channelNum][2], this.shapes[channelNum][3]);
	}

	private void glitch()
	{

	}

	public void setNumChannels(int numChannels)
	{
		this.shapes = new int[numChannels][4];
		this.colorHistory = new int[numChannels][10][3];
		this.fader = new ColorFader[numChannels];
		this.nextUpdateTime = new int[numChannels];
		
		for(int i = 0; i < numChannels; i++)
		{
			int width = this.parent.width/(2*numChannels);
			width = Math.min(width, 200);
			this.shapes[i] = new int[]{(int)(i*this.parent.width/(numChannels+1) + width), (int)this.parent.height/2 - width/2, width, width};
			
			for(int i2 = 0; i2 < 10; i2++)
			{
				this.colorHistory[i][i2] = new int[] {0,0,0};
			}
				
			this.fader[i] = new ColorFader(this.parent);
			this.nextUpdateTime[i] = 0;
			
		}
	}

	@Override
	public int getNumControllers() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getControllerName(int controllerNum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabelName(int controllerNum) {
		// TODO Auto-generated method stub
		return null;
	}

}
