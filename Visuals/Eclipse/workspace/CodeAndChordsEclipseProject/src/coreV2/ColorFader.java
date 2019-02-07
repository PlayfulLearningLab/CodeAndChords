package coreV2;

import processing.core.PApplet;
import processing.core.PConstants;

public class ColorFader implements PConstants
{
	private PApplet		parent;

	//RGB values for current, last, and target colors
	private int[]		RGB;
	private int[]		targetRGB;
	private int[]		lastRGB;

	//Transition duration and start time (transition refers to the transition between two colors)
	private int			transitionDuration;
	private int			transitionStartTime;


	//Alpha (how transparent the color is)
	//255 is opaque //  0 is completely see through (not visible)
	private int			alpha;
	private int			targetAlpha;
	private int			lastAlpha;

	private int			attackDuration;
	private int			releaseDuration;
	private int			alphaStartTime;





	public ColorFader(int r, int g, int b, int alpha, PApplet parent)
	{
		this.parent = parent;

		this.RGB = new int[] {0, 0, 0};
		this.lastRGB = new int[] {0, 0, 0};
		this.targetRGB = new int[] {r, g, b};

		this.alpha 	= 	alpha;

		this.transitionDuration = 1000;

		this.parent.registerMethod("pre", this);
	}

	public ColorFader(int r, int g, int b, PApplet parent)
	{
		this(r, g, b, 255, parent);
	}

	public ColorFader(int[] RGB, PApplet parent)
	{
		this(RGB[0], RGB[1], RGB[2], 255, parent);
	}

	public ColorFader(PApplet parent)
	{
		this(0, 0, 0, 255, parent);
	}


	public void pre()
	{
		//Color - RGB
		if(this.parent.millis() < this.transitionStartTime + this.transitionDuration)
		{
			int curTransitionTime = (this.parent.millis() - this.transitionStartTime);

			int transitionDistance;

			for(int i = 0; i < 3; i++)
			{
				transitionDistance = this.targetRGB[i] - this.lastRGB[i];
				this.RGB[i] = this.lastRGB[i] + (int)(transitionDistance * (float)((float)curTransitionTime / (float)this.transitionDuration));

				this.RGB[i] = Math.max(0, this.RGB[i]);
				this.RGB[i] = Math.min(255, this.RGB[i]);
			}
		}
		else
		{
			this.RGB = this.targetRGB.clone();
		}

		//Alpha

		if(this.alpha == this.targetAlpha)
		{
			//nothing to do here
		}
		else if(this.targetAlpha < this.alpha) //release
		{
			int startTimeIndex = (int) (this.releaseDuration * (float)(255 - this.lastAlpha)/255f);
			int curTransitionTime = this.parent.millis() - this.alphaStartTime + startTimeIndex;

			this.alpha = 255 - 255 * curTransitionTime/this.releaseDuration;
			this.alpha = Math.max(0, this.alpha);
			
			if(this.alpha == 0) this.setCurrentColor(0, 0, 0);

		}
		else if(this.targetAlpha > this.alpha) //attack
		{
			int startTimeIndex = (int) (this.attackDuration * (float)(this.lastAlpha)/255f);
			int curTransitionTime = this.parent.millis() - this.alphaStartTime + startTimeIndex;

			this.alpha = 255 * curTransitionTime/this.attackDuration;
			this.alpha = Math.min(255, this.alpha);
		}

	}//pre()


	//Color methods - RGB

	public void setCurrentColor(int r, int g, int b)
	{
		this.RGB = new int[] {r, g, b};
		this.targetRGB = this.RGB.clone();
		this.lastRGB = this.RGB.clone();
	}

	public void setCurrentColor(int[] RGB)
	{
		this.setCurrentColor(RGB[0], RGB[1], RGB[2]);
	}

	public void setTargetColor(int r, int g, int b)
	{
		this.setTargetColor(new int[] {r, g, b});
	}

	public void setTargetColor(int[] targetColorRGB)
	{
		if(targetColorRGB.length == 4)
		{
			this.setTargetAlpha(targetColorRGB[3]);
		}
		else if(targetColorRGB.length != 3) throw new IllegalArgumentException("must be array of length 3 - RGB values");

		if(!this.sameRGB(targetColorRGB, this.targetRGB))
		{
			this.lastRGB = this.RGB.clone();
			this.targetRGB = targetColorRGB.clone();
			this.transitionStartTime = this.parent.millis();
		}

	}

	public int[] getColor()
	{
		return new int[] {this.RGB[0], this.RGB[1], this.RGB[2], this.alpha};
	}

	public int[] getTargetColor()
	{
		return this.targetRGB.clone();
	}

	public void setTransitionDuration(int time)
	{
		this.transitionDuration = time;
	}


	//Alpha Methods

	public void setCurrentAlpha(int alpha)
	{
		this.targetAlpha = alpha;
		this.alpha = alpha;
		this.lastAlpha = alpha;
	}

	public void setTargetAlpha(int alpha)
	{
		if(this.targetAlpha != alpha)
		{
			this.targetAlpha = alpha;
			this.lastAlpha = this.alpha;
			this.alphaStartTime = this.parent.millis();
		}

	}

	public void setAttackDuration(int attackDuration)
	{
		this.attackDuration = attackDuration;
	}

	public void setReleaseDuration(int releaseDuration)
	{
		this.releaseDuration = releaseDuration;
	}

	public int getCurrentAlpha()
	{
		return this.alpha;
	}



	//Convenience Methods

	private boolean sameRGB(int[] color1, int[] color2)
	{
		boolean val = true;
		//if(color1.length != 3 || color2.length != 3) throw new IllegalArgumentException("sameRGB(int[], int[]) inputs must be RGB values - length 3");

		for(int i = 0; i < 3; i++)
		{
			if(color1[i] != color2[i])
			{
				val = false;
			}
		}

		return val;
	}


}
