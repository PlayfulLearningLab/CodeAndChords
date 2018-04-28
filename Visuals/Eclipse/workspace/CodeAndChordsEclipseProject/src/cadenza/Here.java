package cadenza;

import core.Module;
import core.ModuleMenu;
import core.input.RealTimeInput;
import core.input.RecordedInput;
import filters.Follower;
import net.beadsproject.beads.core.AudioContext;
import processing.core.PApplet;

public class Here extends Module
{
	//index number = mic number - 1;
	private int			beatBoxIndexNumber = 0;

	//index number = mic number - 1;
	private int			soloistIndexNumber = 1;
	
	private	int[]		inputNums	= {
			2, 4, 6, 7, 10, 12, 14
	//		0, 1, 0, 1, 0, 1, 0
	};
	
	private	int	numSmallRects	= 500;
	
	private	int			rectHeight;
	private	int			rectWidth;
	
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

		this.input	= new RealTimeInput(15, false, this);

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
		
		this.menu.getControlP5().hide();

		background(255);
		int[] color;

		color = new int [] {255,255,255};
		this.menu.setCanvasColor(color);
		this.noStroke();

		move = new int [this.inputNums.length][this.numSmallRects][3];
		hold1 = new int [3];
		hold2 = new int [3];
		checkpoint = this.millis();
		
		this.rectHeight	= (this.height / this.inputNums.length) + 1;
		this.rectWidth	= (this.width / (this.move[0].length)) + 1;


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

			for(int j = 0; j < move.length; j++)
			{
				/*
				if(j == this.soloistIndexNumber) 
				{
					soloistPassed = 1;
					j++;
				}
				*/
				
				if(this.menu.getRecInputPlaying())
				{
					scaleDegree	= (round(this.menu.getRecInput().getAdjustedFundAsMidiNote(this.inputNums[j])) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) % 12;
				} else {
					System.out.println("input check");
					scaleDegree	= (round(input.getAdjustedFundAsMidiNote(this.inputNums[j])) - this.menu.getCurKeyEnharmonicOffset() + 3 + 12) %12;
				}

				System.out.println(scaleDegree);

				this.menu.fadeColor(scaleDegree, this.inputNums[j]);

				hold2[0] = this.menu.getCurHue()[this.inputNums[j]][0];
				hold2[1] = this.menu.getCurHue()[this.inputNums[j]][1];
				hold2[2] = this.menu.getCurHue()[this.inputNums[j]][2];

				for(int i = 0; i < move[j].length; i++)
				{	

					hold1[0] = move[j][i][0];
					hold1[1] = move[j][i][1];
					hold1[2] = move[j][i][2];


					move[j][i][0] = hold2[0];
					move[j][i][1] = hold2[1];
					move[j][i][2] = hold2[2];


					this.fill(move[j][i][0], move[j][i][1], move[j][i][2]);
					this.rect((this.rectWidth*(i)), (this.rectHeight * j),this.rectWidth, this.rectHeight);


					hold2[0] = hold1[0];
					hold2[1] = hold1[1];
					hold2[2] = hold1[2];

					/*
					if(j == this.move.length && soloistPassed == 0)
					{
						j++;
					}
					*/
					
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

		//float totalSkew = 0;
/*
		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.skewGenerator[i])
			{
			*/
		// For multiple inputs, would need to be "totalSkew += _____"
				float totalSkew = this.bbFollower.getUnitVal()*this.maxSkew[this.beatBoxIndexNumber];
				/*
			}
		}
		*/

		//float rand = 0;
		/*
		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.skewFlag[i])
			{
			*/
				float rand = (float) (2 * (Math.random() - .5));
	
				this.pointSkew[this.soloistIndexNumber] = (totalSkew * rand) ;
				/*
			}
		}
		*/


		this.stroke(0);
		this.fill(0);
/*
		for(int i = 0; i < this.curNumInputs; i++)
		{
			if(this.drawSineFlag[i])
			{
			*/
				float sineAmp = (float) (this.height * .5 * this.ampFollower.getUnitVal());
				System.out.println("unitVal: " + this.bbFollower.getUnitVal());
				System.out.println("sineAmp: " + sineAmp);
				
				float sineIncrament = (float) (6*Math.PI)/this.numPoints;
				
				for(int i2 = 0; i2 < this.numPoints; i2++)
				{
					this.pointHeight[this.soloistIndexNumber][i2] = (float) (sineAmp*Math.sin(sineIncrament * i2));

					float h = (this.height/2) + (this.pointHeight[this.soloistIndexNumber][i2]);
					/*
					if(this.skewFlag[i])
					{
					*/
						rand = (float)(2*(Math.random() - .5));
						h = (this.height/2) + this.pointHeight[this.soloistIndexNumber][i2] + this.pointSkew[this.soloistIndexNumber]*rand*5;
					/*
					}
					*/

					this.ellipse(this.pointPos[i2], h, this.pointSize, this.pointSize);
				}
				/*
			}
		}
		*/

/*
		if(this.menu.isShowScale())
		{
			//this.legend(scaleDegree, i);
		}
*/


//		this.menu.runMenu();

	} // draw()



	public String[] getLegendText()
	{
		return this.menu.getScale(this.menu.getCurKey(), this.menu.getMajMinChrom());
	} // getLegendText

}
