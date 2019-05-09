package coreV2;

public class Seeker 
{
	private float 		val = 0;

	private String 		type;
	
	private float 		maxIncrament = 0;

	private boolean		integersOnly = false;

	private boolean		useLimits = false;

	private float 		maxVal = 0;
	private float		minVal = 0;
	
	private float		scalar = 1;


	public Seeker()
	{
		this.setType("average");
	}

	public Seeker(String type)
	{
		this.setType(type);
	}

	public void setType(String type)
	{
		boolean typeCheck = false;

		if(type == "average") typeCheck = true;
		if(type == "maxScalarJump") typeCheck = true;
		if(type == "beatbox") typeCheck = true;


		if(typeCheck == false)
		{
			throw new IllegalArgumentException("Not a valid follower type");
		}
		else
		{
			this.type = type;
		}
	}
	
	public void setMaxIncrament(float maxIncrament)
	{
		this.maxIncrament = maxIncrament;
	}

	public void setIntOnly(boolean flag)
	{
		this.integersOnly = flag;
	}

	public boolean getIntOnly()
	{
		return this.integersOnly;
	}

	public void setUseLimits(boolean flag)
	{
		if(flag == true && (this.maxVal == 0 && this.minVal == 0))
		{
			System.err.println("WARNING (Follower.setUseLimits): Remember to set your limit values");
		}

		this.useLimits = flag;
	}

	public boolean getUseLimits()
	{
		return this.useLimits;
	}

	public void setMaxVal(float maxVal)
	{
		if(maxVal < this.minVal)
			throw new IllegalArgumentException("Max val cannot be set lower than the min val"
					+ "\n Current Values are..."
					+ "\n Min Val = " + this.minVal
					+ "\n Max Val = " + this.maxVal);

		this.maxVal = maxVal;
	}

	public void setMinVal(float minVal)
	{
		if(minVal > this.maxVal) 
			throw new IllegalArgumentException("Min val cannot be set higher than the min val"
					+ "\n Current Values are..."
					+ "\n Min Val = " + this.minVal
					+ "\n Max Val = " + this.maxVal);

		this.minVal = minVal;
	}

	public float getMaxVal()
	{
		return this.maxVal;
	}

	public float getMinVal()
	{
		return this.minVal;
	}

	public void update(float rawValue)
	{
		switch(this.type){

		case "average":

			this.val += (rawValue - this.val)/4;
			break;
			
		case "maxScalarJump":
			
			float incrament = (rawValue - this.val) * (this.scalar/this.maxVal);
			
			if(incrament >= 0) 
				incrament = Math.min(incrament, this.maxIncrament);
			else 
				incrament = Math.max(incrament, -this.maxIncrament);
			
			this.val += (incrament)* (this.maxIncrament/this.scalar);
			
			break;
			
		case "beatbox":
				
			if(rawValue > this.val)
			{
				this.val = rawValue;
			}
			else
			{
				this.val = (float) (this.val * .7);
			}
			
			break;
			
		default:
			
			throw new IllegalArgumentException("Not a valid follower type");

		}


		if(this.useLimits)
		{
			this.val = Math.min(this.maxVal, this.val);
			this.val = Math.max(this.minVal, this.val);
		}


	}

	public float getVal()
	{
		float returnVal = this.val;

		if(this.integersOnly)
		{
			returnVal = Math.round(returnVal);
		}
		return returnVal;
	}

	public void setScalar(float scalar)
	{
		this.scalar = scalar;
	}
	
	public float getScalarVal()
	{
		if(this.useLimits == false)
			throw new IllegalArgumentException("Limits must be set and turned on to use this function");

		float val = this.scalar * (this.val/this.maxVal);
		
		if(this.integersOnly) val = Math.round(val);
		
		return val;
	}

	public float getUnitVal()
	{
		if(this.useLimits == false)
			throw new IllegalArgumentException("Limits must be set and turned on to use this function");

		return this.val/this.maxVal;
	}

}
