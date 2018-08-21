package coreV2;

import processing.core.PApplet;
import processing.core.PConstants;

public class ColorWrapper implements PConstants
{
	private PApplet		parent;

	private int[]		RGB;
	private int			alpha;

	private int[]		lastRGB;
	private int[]		targetRGB;
	private int			targetAlpha;

	private int			checkpoint;

	private int			transitionStartTime;
	private int			transitionDuration;
	private int			transitionIndex;

	private int[]		distance;

	
	public void pre()
	{
		if(this.parent.millis() < this.transitionStartTime + this.transitionDuration)
		{
			this.transitionIndex = (this.parent.millis() - this.transitionStartTime);

			for(int i = 0; i < 3; i++)
			{
				this.RGB[i] = (this.lastRGB[i]) + (int)(this.distance[i] * (float)((float)this.transitionIndex/(float)this.transitionDuration));
				this.RGB[i] = Math.max(0, this.RGB[i]);
				this.RGB[i] = Math.min(255, this.RGB[i]);
			}
		}
		else if(this.RGB != this.targetRGB)
		{
			this.RGB = this.targetRGB;
		}
		
	}//pre()

	public ColorWrapper(int r, int g, int b, int alpha, PApplet parent)
	{
		this.parent = parent;

		this.RGB = new int[] {0, 0, 0};
		
		this.lastRGB = new int[] {0, 0, 0};
		
		this.targetRGB = new int[] {0, 0, 0};
		
		this.distance = new int[] {0,0,0};

		this.RGB[0] 	=	r;
		this.RGB[1] 	= 	g;
		this.RGB[2] 	= 	b;
		this.alpha 	= 	alpha;

		this.transitionDuration = 1000;

		this.checkpoint = this.parent.millis();
		this.parent.registerMethod("pre", this);

	}

	public ColorWrapper(int r, int g, int b, PApplet parent)
	{
		this(r, g, b, 255, parent);
	}

	public ColorWrapper(int[] RGB, PApplet parent)
	{
		this(RGB[0], RGB[1], RGB[2], 255, parent);
	}

	public void setCurrentColor(int r, int g, int b)
	{
		this.targetRGB = RGB;

		this.RGB[0] 	=	r;
		this.RGB[1] 	= 	g;
		this.RGB[2] 	= 	b;
	}

	public void setCurrentColor(int[] RGB)
	{
		this.setCurrentColor(RGB[0], RGB[1], RGB[2]);
	}

	public void setCurrentAlpha(int alpha)
	{
		this.alpha = alpha;
	}

	public void setTargetColor(int r, int g, int b)
	{
		this.setTargetColor(new int[] {r, g, b});
	}

	public void setTargetColor(int[] targetColorRGB)
	{
		for(int i = 0; i < 3; i++)
		{
			this.lastRGB[i] = this.RGB[i];
			this.targetRGB[i] = targetColorRGB[i];
			this.distance[i] = this.targetRGB[i] - this.lastRGB[i];
		}
		
		this.transitionStartTime = this.parent.millis();
	}

	public void setTargetAlpha()
	{

	}


	public int[] getColor()
	{
		return new int[] {this.RGB[0], this.RGB[1], this.RGB[2], this.alpha};
	}

	public int[] getTargetColor()
	{
		return this.targetRGB;
	}


}
