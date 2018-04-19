package filters;

public class Follower 
{
	private float 		val = 0;
	
	
	private String 		type;
	
	private boolean		integersOnly = false;
	
	private boolean		useLimits = false;
	
	private float 		maxVal = 0;
	private float		minVal = 0;

		
	public Follower()
	{
		this.setType("average");
	}
	
	public Follower(String type)
	{
		this.setType(type);
	}

	public void setType(String type)
	{
		boolean typeCheck = false;
		
		if(type == "average") typeCheck = true;
		
		
		if(typeCheck == false)
		{
			throw new IllegalArgumentException("Not a valid follower type");
		}
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
			System.err.println("WARNING: Remember to set your limit values");
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

			this.val = (rawValue + this.val)/2;
			break;
			
			
		}
		
		
		if(this.useLimits && this.maxVal < this.val)
		{
			this. val = this.maxVal;
		}
		
		if(this.useLimits && this.minVal > this.val)
		{
			this.val = this.minVal;
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
	
	public float getScalarVal()
	{
		if(this.useLimits == false)
			throw new IllegalArgumentException("Limits must be set and turned on to use this function");
		
		return this.val/this.maxVal;
	}

}
