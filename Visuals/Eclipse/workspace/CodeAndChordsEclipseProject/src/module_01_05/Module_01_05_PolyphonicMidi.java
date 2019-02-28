package module_01_05;

import coreV2.*;
import processing.core.PApplet;

public class Module_01_05_PolyphonicMidi extends PApplet
{
	private ModuleDriver 	driver;
	private InputHandler 	inputHandler;
	private ColorScheme		cs;
	private Canvas			canvas;

	private boolean[]		active; 
	private boolean[]		turningOn;
	private boolean[]		turningOff;

	private RectFader[]		rects;

	public static void main(String[] args)
	{
		PApplet.main("module_01_05.Module_01_05_PolyphonicMidi");
	}

	public void settings()
	{
		this.size(925, 520);
	}

	public void setup()
	{
		this.driver = new ModuleDriver(this);

		this.canvas = this.driver.getCanvas();
		this.inputHandler = this.driver.getInputHandler();
		this.cs = this.driver.getColorMenu().getColorSchemes()[0];
		this.canvas = this.driver.getCanvas();
		this.rects = new RectFader[12];

		for(int i = 0; i < this.rects.length; i++)
		{
			this.rects[i] = new RectFader(this);
		}

		this.active = new boolean[12];
		this.turningOn = new boolean[12];
		this.turningOff = new boolean[12];

		for(int i = 0; i < this.active.length; i++)
		{
			this.active[i] = false;
			this.turningOn[i] = false;
			this.turningOff[i] = false;

		}

		this.driver.getCP5().getController("realTimeInput").setValue(1);

	}//setup()


	public void draw()
	{		
		int[][] activeMIDI = this.inputHandler.getAllMidiNotes();
		int numNotes = activeMIDI.length;

		int[] notes = new int[numNotes];
		for(int i = 0; i < numNotes; i++)
		{
			notes[i] = activeMIDI[i][0]%12;
		}

		//filling all of the instance variables

		boolean isActive;
		for(int i = 0; i < this.active.length; i++)
		{
			isActive = false;
			for(int i2 = 0; i2 < notes.length; i2++)
			{
				if(i == notes[i2]) isActive = true;
			}

			this.turningOn[i] = false;
			this.turningOff[i] = false;

			if(!this.active[i] && isActive)//turning on
			{
				this.turningOn[i] = true;
			}
			else if (this.active[i] && !isActive)//turning off
			{
				this.turningOff[i] = true;
				this.rects[i].setTargetWidth(0);
			}

			this.active[i] = isActive;
		}

		this.fill(0);
		this.canvas.background();


		//update the rectangle centers and widths
		RectFader  rf;
		int[] color;
		int alpha;

		for(int i = 0; i < numNotes; i++)
		{
			rf = this.rects[notes[i]];
			rf.setTargetCenter((int) (i*(float)(this.width/numNotes) + (float)(this.width/numNotes/2)));
			rf.setTargetWidth(this.width/numNotes);

			if(this.turningOn[notes[i]]) rf.setCurCenter((int) (i*(float)(this.width/numNotes) + (float)(this.width/numNotes/2)));

			color = this.cs.getPitchColor(notes[i]);
			this.noStroke();

			alpha = (int) (255 * (float)(rf.getWidth())/rf.getTargetWidth());
			alpha = Math.max(0, alpha);
			alpha = Math.min(255, alpha);

			System.out.println(alpha);

			this.fill(color[0], color[1], color[2], alpha);
			this.canvas.rect(rf.getCenter() - rf.getWidth()/2, 0, rf.getWidth(), this.height);
		}



	}//draw()

	private int getOriginPoint()
	{
		int origin = this.width/2;

		if(this.numActive() > 0)
		{

		}


		return origin;
	}

	private int numActive()
	{
		int active = 0;

		for(int i = 0; i < this.rects.length; i++)
		{
			if(this.rects[i].isActive())  active++;
		}

		return active;
	}

	public class RectFader
	{
		private PApplet		parent;
		private boolean 	active;

		private int			curCenter;
		private int			targetCenter;

		private int			curWidth;
		private int			targetWidth;

		private int			centerTransitionTime;
		private int			widthAttackTime;
		private int			widthDecayTime;
		
		private int			speed;

		public RectFader(PApplet parent)
		{
			this.parent = parent;
			this.active = false;

			this.curCenter = 0;
			this.targetCenter = 0;

			this.curWidth = 0;
			this.targetWidth = 0;
			
			this.speed = 3;

			parent.registerMethod("pre", this);

		}

		public void pre()
		{

			//Center
			if(this.curCenter != this.targetCenter)
			{
				if		(this.curCenter < this.targetCenter)		this.curCenter += this.speed;
				else if	(this.curCenter > this.targetCenter)		this.curCenter -= this.speed;
				
				if(Math.abs(this.curCenter - this.targetCenter) < this.speed) this.curCenter = this.targetCenter;
							}

			//Width
			if(this.curWidth != this.targetWidth)
			{
				if		(this.curWidth < this.targetWidth)		this.curWidth += this.speed;
				else if	(this.curWidth > this.targetWidth)		this.curWidth -= this.speed;
				
				this.curWidth = Math.max(0, this.curWidth);
			}


		}//pre()

		public void setCurCenter(int xVal)
		{
			this.curCenter = xVal;
		}

		public void setTargetCenter(int xVal)
		{
			this.targetCenter = xVal;
		}

		public void setTargetWidth(int targetWidth)
		{
			this.targetWidth = targetWidth;
		}

		public void setCenterTransitionTime(int time)
		{
			this.centerTransitionTime = time;
		}

		public void setWidthAttackTime(int time)
		{
			this.widthAttackTime = time;
		}

		public void setWidthDecayTime(int time)
		{
			this.widthDecayTime = time;
		}

		public boolean isActive()
		{
			return this.isActive();
		}

		public int getCenter()
		{
			return this.curCenter;
		}

		public int getWidth()
		{
			return this.curWidth;
		}

		public int getTargetWidth()
		{
			return this.targetWidth;
		}

	}

}
