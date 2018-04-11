package cadenza;

import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class Here extends Module
{
	private float		maxAmplitude = 10;

	private float 		pointSize;
	private int 		numPoints;
	private float 		pointIncrament;

	private boolean[]	drawSineFlag;
	private float[] 	pointPos;
	private float[][] 	pointHeight;

	private boolean[]	skewGenerator;
	private float[] 	maxSkew;

	private boolean[]	skewFlag;
	private float[] 	pointSkew;

	public static void main(String[] args)
	{
		PApplet.main("cadenza.Here");
	} // main

	public void setup() 
	{
		this.input	= new RealTimeInput(16, new AudioContext(), true, this);
		//this.input          = RealTimeInput() Elena give this the audiocontext in branch
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		this.curNumInputs	= 2;

		this.menu	= new ModuleMenu(this, this, this.input, 12);

		this.setSquareValues();

		// call add methods:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();

		this.pointSize = 10;
		this.numPoints = (int) Math.floor((this.width / (this.pointSize * 2)) - 1);
		System.out.println("numPoints: " + this.numPoints);
		this.pointIncrament = this.width / this.numPoints;

		this.pointSkew = new float[this.totalNumInputs];
		this.maxSkew = new float[this.totalNumInputs];
		
		this.pointPos = new float[this.numPoints];
		
		this.pointHeight = new float[this.totalNumInputs][this.numPoints];
		
		this.drawSineFlag = new boolean[this.totalNumInputs];
		this.skewFlag = new boolean[this.totalNumInputs];
		this.skewGenerator = new boolean[this.totalNumInputs];
		
		float pos = this.pointIncrament/2;
		
		for(int i = 0; i < this.totalNumInputs; i++)
		{	
			for(int i2 = 0; i2 < this.numPoints; i2++)
			{
				this.pointHeight[i][i2] = 0;
			}
			
			this.pointSkew[i] = 0;
			this.skewFlag[i] = false;
			this.skewGenerator[i] = false;
			this.maxSkew[i] = 30;
			this.drawSineFlag[i] = false;
			i++;
		}

		for(int i = 0; i < this.numPoints; i++)
		{
			this.pointPos[i] = pos;
			pos += this.pointIncrament;
		}
		
		this.skewFlag[0] = true;
		this.drawSineFlag[0] = true;
		this.skewGenerator[0] = true;
		this.skewGenerator[1] = true;

	} // setup()


	public void draw()
	{
		int	scaleDegree;
		
		this.background(0);

		//scaleDegree	= (round(input.getAdjustedFundAsMidiNote(i + 1)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
		//this.menu.universalFade(scaleDegree);

		for(int i = 0; i < this.totalNumInputs; i++)
		{
			this.menu.updateAmplitudeFollower(i, 3);
			if(this.menu.getAmplitudeFollower(i) > this.maxAmplitude)
			{
				this.maxAmplitude = this.menu.getAmplitudeFollower(i);
			}
		}

		float totalSkew = 0;

		for(int i = 0; i < this.totalNumInputs; i++)
		{
			if(this.skewGenerator[i])
			{
				float rand = (float) (Math.random() - .5);
				totalSkew += this.maxSkew[i] * (Math.abs(rand)/rand) * (this.menu.getAmplitudeFollower(i)/this.maxAmplitude);
			}
		}

		for(int i = 0; i < this.totalNumInputs; i++)
		{
			if(this.skewFlag[i])
			{
				this.pointSkew[i] = (float) (this.menu.getAmplitudeFollower(i)/this.maxAmplitude);
			}
		}

		
		this.color(255);
		this.fill(255);
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.drawSineFlag[i])
			{
				float sineAmp = this.menu.getAmplitudeFollower(i);
				for(int i2 = 0; i2 < this.numPoints; i2++)
				{
					float sineIncrament = (float) (6*Math.PI)/this.numPoints;
					
					System.out.println("filled cell: " + i + ", " + i2 );
					
					this.pointHeight[i][i2] = (float) (sineAmp*Math.sin(sineIncrament * i2));
					
					float h = (this.height/2) + (this.pointHeight[i][i2]);
					if(this.skewFlag[i])
					{
						float rand = (float)(2*(Math.random() - .5));
						h += totalSkew * rand;
					}
					
					this.ellipse(this.pointPos[i2], h, this.pointSize, this.pointSize);
				}
			}
		}


		if(this.menu.isShowScale())
		{
			//this.legend(scaleDegree, i);
		}



		this.menu.runMenu();

	} // draw()



	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

}
