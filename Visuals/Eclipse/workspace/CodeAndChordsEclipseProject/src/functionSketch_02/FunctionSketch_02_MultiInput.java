package functionSketch_02;

import core.Input;
import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.SampleManager;
import processing.core.PApplet;
import processing.sound.*;

public class FunctionSketch_02_MultiInput extends PApplet 
{
	int[]	xVals;
	int[]	yVals;
	int		numInputs	= 4;
	int		threshold	= 10;

	Input	input;

	float[][] rainbowColors	= new float[][] {
		{ 255, 0, 0 }, 
		{ 255, (float) 127.5, 0 }, 
		{ 255, 255, 0 },
		{ (float) 127.5, 255, 0 }, 
		{ 0, 255, (float) 127.5 }, 
		{ 0, 255, 255 }, 
		{ 0, (float) 127.5, 255 },
		{ 0, 0, 255 }
	};

	public static void main( String[] args )
	{
		PApplet.main("functionSketch_02.FunctionSketch_02_MultiInput");
	}

	public void settings()
	{
		size(925,520);
	} // setup()

	public void setup()
	{
		background(0);

		this.input	= new Input(this.numInputs);

		this.xVals	= new int[] {
				0, this.width/4, this.width/2, (this.width/4) * 3
		};
		this.yVals	= new int[] {
				0, this.height/2
		};

	} // setup

	public void draw()
	{
		for(int i = 0; i < this.numInputs; i++)
		{
			if(this.input.getAmplitude(i + 1) > this.threshold)
			{
				fill(this.rainbowColors[i][0], this.rainbowColors[i][1], this.rainbowColors[i][2]);
			} else {
				fill(0, 0, 0);
			}
				rect(xVals[i % xVals.length], yVals[(i / 4)], (this.width / 4), (this.height / 2));
				
				fill(0);
				text((i + 1), (xVals[i % xVals.length] + (this.width / 8)), (yVals[(i / 4)] + (this.height / 4)));
		}
	} // draw

} // FunctionSketch_02
