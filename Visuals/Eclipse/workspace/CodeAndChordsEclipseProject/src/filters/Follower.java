package filters;

public class Follower 
{
	
	private String 		type;
	
	private float 		val;
	
	public Follower()
	{
		
	}
	
	public void setType(String type)
	{
		
	}
	
	public void update(float input)
	{
		this.val = (input + this.val)/2;
	}
	
	public float getVal()
	{
		return this.val;
	}
	
}
