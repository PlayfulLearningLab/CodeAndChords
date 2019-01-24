package functionSketch_07_musicality;

import processing.core.PApplet;

public class MovementHelper {

	private PApplet 	parent;

	private int 		mode;
	private String[] 	modeName;

	private int			val;

	private int			input;
	private int			lastInput;

	private int 		lastSampleTime;
	private int			deltaT;
	
	//for mode 2
	private int			vel;
	
	//for mode 3
	private float		I;
	private float		lastError;

	public MovementHelper(PApplet parent, int movementMode)
	{
		this.parent = parent;
		
		this.modeName = new String[] {"Constant Velocity", "Acceleration and Decay"};
		this.mode = movementMode;
		
		this.I = 0;
		this.vel = 0;
		this.lastError = 0;
		this.lastSampleTime = 0;
		this.deltaT = 0;
		
		this.input = 0;
		this.lastInput = 0;
		
		
		parent.registerMethod("pre", this);
	}

	public void input(int inputVal)
	{
		this.deltaT = this.parent.millis() - this.lastSampleTime;
		this.lastInput = this.input;
		this.input = inputVal;

		this.lastSampleTime = this.parent.millis();
	}
	
	public int getVal()
	{
		return this.val;
	}
	
	public int getLastInput()
	{
		return this.input;
	}

	public void pre()
	{
		if(this.mode == 0) // constant velocity
		{
			if(this.val < this.input - 2)
			{
				this.val += 3;
			}
			else if(this.val > this.input + 2)
			{
				this.val -= 3;
			}
			else
			{
				this.val = this.input;
			}
		}


		if(this.mode == 2) // velocity - acceleration - damping
		{
			float distance = this.input - this.val;
			float morphTime = 25;
			float accelerationIndex = 4f;
			float decay = 30;

			this.vel += (distance/morphTime)*(accelerationIndex/20) - (this.vel/decay);
			this.val += this.vel;
		}

		if(this.mode == 3) //PID Controller
		{
			//update bar 3 (PID Controller)
			float Kp = .02f;
			float Kd = 0f;
			float Ki = .005f;

			float error = (this.input - this.val);

			//P
			float P = error;

			//D
			float D = (error - this.lastError)/this.deltaT;

			//I
			this.I += this.deltaT * ((error - this.lastError)/2);


			this.lastError = error;

			float sum = Kp*P + Kd*D + Ki*this.I;

			this.val += (int) sum;
		}


	}


}
