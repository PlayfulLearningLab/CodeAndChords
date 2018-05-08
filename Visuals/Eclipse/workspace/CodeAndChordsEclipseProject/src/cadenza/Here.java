package cadenza;

import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import coreV2.Follower;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class Here extends Module
{
	//index number = mic number - 1;
	private int			beatBoxIndexNumber = 0;

	//index number = mic number - 1;
	private int			soloistIndexNumber = 1;
	
	private	int[]		inputNums	= {
			2, 3, 4, 5, 6, 7, 8
	};
	
	private Follower	ampFollower;
	private Follower	bbFollower;

	private float		maxAmplitude = 500;

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


	int[][][] move;
	int[] hold1;
	int[] hold2;
	int checkpoint;

	public static void main(String[] args)
	{
		PApplet.main("cadenza.Here");
	} // main

	public void settings()
	{
		fullScreen();
	}

	public void setup() 
	{

		this.input	= new RealTimeInput(28, new AudioContext(), this);
		this.totalNumInputs	= this.input.getAdjustedNumInputs();
		//this.curNumInputs	= 7;
		this.curNumInputs	= this.totalNumInputs;

		this.menu	= new ModuleMenu(this, this, this.input, 12);

		this.menu.setUseRecInput(true);

		this.setSquareValues();


		// call add methods:
		this.menu.addLandingMenu();
		this.menu.addSensitivityMenu(true);
		this.menu.addColorMenu();

		this.menu.setCurKey("C", 2);
		this.menu.getControlP5().getController("trichrom").update();

		this.menu.setColor(0, new int[] { 0, 153, 51 }, true);
		this.menu.setColor(8, new int[] { 255, 255, 102 }, true);
		this.menu.setColor(4, new int[] { 0, 153, 204 }, true);

		this.menu.getControlP5().getController("trichrom").update();

		this.menu.getControlP5().getController("keyDropdown").bringToFront();

		background(255);
		int[] color;

		color = new int [] {255,255,255};
		this.menu.setCanvasColor(color);
		this.noStroke();

		move = new int [this.curNumInputs - 1][183][3];
		hold1 = new int [3];
		hold2 = new int [3];
		checkpoint = this.millis(); 


		//		this.pointSize = 25;
		this.pointSize	= 10;
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

		this.skewFlag[this.soloistIndexNumber] = true;
		this.drawSineFlag[this.soloistIndexNumber] = true;
		this.skewGenerator[this.beatBoxIndexNumber] = true;
		
		this.ampFollower = new Follower();
		
		this.ampFollower.setMaxVal(500);
		this.ampFollower.setMinVal(0);
		this.ampFollower.setUseLimits(true);
		
		this.bbFollower = new Follower("beatbox");
		
		this.bbFollower.setMaxVal(300);
		this.bbFollower.setMinVal(0);
		this.bbFollower.setUseLimits(true);

	} // setup()


	public void draw()
	{
		int	scaleDegree;

		this.noStroke();

		if(checkpoint < this.millis())
		{
			int soloistPassed = 0;

			for(int j = 0; j < move.length + 1; j++)
			{
				if(j == this.soloistIndexNumber) 
				{
					soloistPassed = 1;
					j++;
				}

				if(this.menu.getRecInputPlaying())
				{
					scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(j - soloistPassed)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
				} else {
					System.out.println("input check");
					scaleDegree	= (round(input.getAdjustedFundAsMidiNote(j - soloistPassed)) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
				}

				System.out.println(scaleDegree);

				this.menu.fadeColor(scaleDegree, j - soloistPassed);




				hold2[0] = this.menu.getCurHue()[j - soloistPassed][0];
				hold2[1] = this.menu.getCurHue()[j - soloistPassed][1];
				hold2[2] = this.menu.getCurHue()[j - soloistPassed][2];

				for(int i = 0; i < move[j - soloistPassed].length; i++)
				{	

					hold1[0] = move[j - soloistPassed][i][0];
					hold1[1] = move[j - soloistPassed][i][1];
					hold1[2] = move[j - soloistPassed][i][2];


					move[j - soloistPassed][i][0] = hold2[0];
					move[j - soloistPassed][i][1] = hold2[1];
					move[j - soloistPassed][i][2] = hold2[2];


					this.fill(move[j - soloistPassed][i][0], move[j - soloistPassed][i][1], move[j - soloistPassed][i][2]);
					this.rect(((this.width/move[j - soloistPassed].length)*(i)),(this.height/(this.curNumInputs - 1))*(j - soloistPassed),(this.width/move.length), this.height/(this.curNumInputs - 1));


					hold2[0] = hold1[0];
					hold2[1] = hold1[1];
					hold2[2] = hold1[2];

					if(j == this.move.length && soloistPassed == 0)
					{
						j++;
					}
					
				}
				//checkpoint = this.millis() + 100;
			}
		}

		if(this.menu.getRecInputPlaying())
		{
			this.ampFollower.update(this.menu.getRecInput().getAmplitude());
		}
		else
		{
			this.ampFollower.update(this.input.getAmplitude(this.soloistIndexNumber));
		}
		
		this.bbFollower.update(this.input.getAmplitude(this.beatBoxIndexNumber));

		for(int i = 0; i < this.curNumInputs; i++)
		{
			this.menu.updateAmplitudeFollower(i, 3);
			
		}

		float totalSkew = 0;

		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.skewGenerator[i])
			{
				totalSkew += this.bbFollower.getUnitVal()*this.maxSkew[i];
			}
		}

		float rand = 0;
		
		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.skewFlag[i])
			{
				rand = (float) (2 * (Math.random() - .5));
	
				this.pointSkew[i] = (totalSkew * rand) ;
			}
		}


		this.stroke(0);
		this.fill(0);

		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.drawSineFlag[i])
			{
				float sineAmp = (float) (this.height * .5 * this.ampFollower.getUnitVal());
				System.out.println("unitVal: " + this.bbFollower.getUnitVal());
				System.out.println("sineAmp: " + sineAmp);
				
				float sineIncrament = (float) (6*Math.PI)/this.numPoints;
				
				for(int i2 = 0; i2 < this.numPoints; i2++)
				{
					this.pointHeight[i][i2] = (float) (sineAmp*Math.sin(sineIncrament * i2));

					float h = (this.height/2) + (this.pointHeight[i][i2]);
					if(this.skewFlag[i])
					{
						rand = (float)(2*(Math.random() - .5));
						h = (this.height/2) + this.pointHeight[i][i2] + this.pointSkew[i]*rand*5;
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
