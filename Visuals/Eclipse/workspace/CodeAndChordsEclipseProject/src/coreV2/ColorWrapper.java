package coreV2;

public class ColorWrapper 
{
	private int		red;
	private int		green;
	private int		blue;

	private int		alpha;

	public ColorWrapper(int r, int g, int b)
	{
		this.red 	=	r;
		this.green 	= 	g;
		this.blue 	= 	b;
		this.alpha 	= 	255;
	}
	
	public ColorWrapper(int[] RGB)
	{
		this.red 	=	RGB[0];
		this.green 	= 	RGB[1];
		this.blue 	= 	RGB[2];
		this.alpha 	= 	255;
	}
	
	public ColorWrapper(int r, int g, int b, int alpha)
	{
		this.red 	=	r;
		this.green 	= 	g;
		this.blue 	= 	b;
		this.alpha 	= 	alpha;
	}

	public void setColor(int r, int g, int b, int alpha)
	{
		this.red 	=	r;
		this.green 	= 	g;
		this.blue 	= 	b;
		this.alpha 	= 	alpha;
	}
	
	public void fadeColor(int[] RGB, int transitionSpeed)
	{
		this.fadeColor(RGB[0], RGB[1], RGB[2], transitionSpeed);
	}

	public void fadeColor(int goalRed, int goalGreen, int goalBlue, int transitionSpeed)
	{
		int[] curRGB = new int[] {this.red, this.green, this.blue};
		int[] goalRGB = new int[] {goalRed, goalGreen, goalBlue};

		for(int i = 0; i < curRGB.length; i++)
		{
			if(curRGB[i] == goalRGB[i])
			{
				//Goal value already reached.  No action necessary.
			}
			else if(curRGB[i] < (goalRGB[i] - transitionSpeed))
			{
				curRGB[i] += transitionSpeed;
				curRGB[i] = Math.min(curRGB[i], goalRGB[i]);

			}
			else if(curRGB[i] > (goalRGB[i] + transitionSpeed))
			{
				curRGB[i] -= transitionSpeed;
				curRGB[i] = Math.max(curRGB[i], goalRGB[i]);
			}
			else
			{
				curRGB[i] = goalRGB[i];
			}
		}

		this.red = curRGB[0];
		this.green = curRGB[1];
		this.blue = curRGB[2];

	}

	public void fadeAlpha(int goalAlpha, int transitionSpeed)
	{
		if(this.alpha == goalAlpha)
		{
			//Goal value already reached.  No action necessary.
		}
		else if(this.alpha < (goalAlpha - transitionSpeed))
		{
			this.alpha += transitionSpeed;

			this.alpha = Math.min(this.alpha, goalAlpha);
		}
		else if(this.alpha > (goalAlpha + transitionSpeed))
		{
			this.alpha -= transitionSpeed;
			this.alpha = Math.max(this.alpha, goalAlpha);
		}

	}

	public int[] getColor()
	{
		return new int[] {this.red, this.green, this.blue, this.alpha};
	}


}
