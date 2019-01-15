package functionSketch_07_musicality;

import processing.core.PApplet;

public class Sampling extends PApplet
{

	private int 	sampleMode;

	private int 	barWidth;

	private int 	lastSampleTime;
	private int 	samplePeriod;

	private int		sampleValue;

	private int[] 	barValues;

	private float	vel;

	//PID Controller stuff
	private int		lastLoopStartTime;
	private float	lastError;
	private int		iPID;

	public static void main(String[] args)
	{
		PApplet.main("functionSketch_07_musicality.Sampling");
	}

	public void settings()
	{
		size(925, 520);
	}

	public void setup()
	{
		this.lastSampleTime = 0;
		this.samplePeriod = 3000;
		this.sampleValue = this.mouseY;
		this.setSampleMode(1);

		this.barValues = new int[] {this.sampleValue,this.sampleValue,this.sampleValue};

		this.lastLoopStartTime = this.millis();
		this.iPID = 0;
		this.lastError = 0;
		this.vel = 0;
	}

	public void draw()
	{
		float drawPeriod = (this.millis()-this.lastLoopStartTime);
		this.lastLoopStartTime = this.millis();

		//update bar heights as needed
		if(this.millis() >= this.lastSampleTime + this.samplePeriod)
		{
			//reset the lastSampleTime variable
			this.lastSampleTime += this.samplePeriod;

			//save the current sample value
			this.sampleValue = this.mouseY;

			//update bar 1
			this.barValues[0] = sampleValue;
		}

		//update bar 2
		if(this.sampleValue > this.barValues[1]+2)
		{
			this.barValues[1] += 3;
		}
		else if(this.sampleValue < this.barValues[1]-2)
		{
			this.barValues[1] -= 3;
		}
		else
		{
			this.barValues[1] = this.sampleValue;
		}


		//this.velocity[i] += (distance[i]/this.morphTime)*(this.accelerationIndex/20) - (this.velocity[i]/this.decay);
		//this.currentShape[i] += this.velocity[i];

		float distance = this.sampleValue - this.barValues[2];
		float morphTime = 25;
		float accelerationIndex = 4f;
		float decay = 30;

		this.vel += (distance/morphTime)*(accelerationIndex/20) - (this.vel/decay);
		this.barValues[2] += this.vel;



		/*
		//update bar 3 (PID Controller)
		float Kp = .02f;
		float Kd = 0f;
		float Ki = .005f;

		float error = (this.sampleValue - this.barValues[2]);

		//P
		float P = error;

		//D
		float D = (error - this.lastError)/drawPeriod;

		//I
		this.iPID += (drawPeriod * (error - this.lastError))/2;


		this.lastError = error;

		float sum = Kp*P + Kd*D + Ki*this.iPID;

		System.out.println(sum);
		this.barValues[2] += sum;

		 */

		//clear canvas
		this.background(0);

		//draw bars
		for(int i = 1; i <= this.sampleMode; i++)
		{
			this.noStroke();
			this.fill(this.getColor(i));

			this.rect(this.barWidth*(i-1), this.height, this.barWidth, this.convertHeight(this.barValues[i-1]));
		}

		//Draw dividing lines
		this.strokeWeight(10);
		this.stroke(150);

		for(int i = 1; i < this.sampleMode; i++)
		{
			this.line(i*this.barWidth, 0, i*this.barWidth, this.height);
		}

		//draw the bar height
		this.strokeWeight(10);
		this.stroke(70, 5, 200);
		this.line(0, this.sampleValue, this.width, this.sampleValue);

		//draw the bar height
		this.strokeWeight(6);
		this.stroke(170, 5, 100);
		this.line(0, this.mouseY, this.width, this.mouseY);


	}

	public void keyPressed()
	{
		if(key == '1'){
			this.setSampleMode(1);
		}
		else if (key == '2'){
			this.setSampleMode(2);
		}
		else if (key == '3'){
			this.setSampleMode(3);
		}
	}

	private void setSampleMode(int mode)
	{
		this.sampleMode = mode;
		this.barWidth = this.width/this.sampleMode;
	}

	private int convertHeight(int input)
	{
		return 0 - (this.height - input);
	}

	private int getColor(int barNum)
	{
		int color = 60*barNum;


		return color;
	}

}
