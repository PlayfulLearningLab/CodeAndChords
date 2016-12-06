import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import processing.core.PApplet;

public class Driver extends PApplet {
	Input	input;
	Input.FrequencyEMM[] freqEMMarray;
	RainingNumbers	inputFreqRain;
	
	public static void main(String[] args)
	{
		PApplet.main("Driver");
	} // main

	public void settings()
	{
		size(1000,600);
	} // settings

	public void setup()
	{
		background(230, 179, 204);
//		input	= new Input("Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav");
		input	= new Input();
		freqEMMarray = input.getFrequencyArray();
		inputFreqRain	= new RainingNumbers(this, width - 200, "input.getAdjustedFund()", 15);
//		inputFreqRain.setBackgroundColor(250, 150, 204);
//		inputFreqRain.setBackgroundColor(255, 204, 255);
		inputFreqRain.setBackgroundColor(230, 179, 204);

//		background(250, 150, 204);
	} // setup

	public void draw()
	{
//		background(250, 150, 204);
		stroke(230, 179, 204);
		fill(230, 179, 204);
		rect(0, 0, width - 200, height);
		
//		drawPSSecondHighest(input);
		drawHPSSecondHighest(input);
		
		inputFreqRain.rain(input.getAdjustedFund());
		println("input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz());
		/*
 // This is now happening in drawHPSSecondHighest()
		stroke(255);
		  for (int i = 0; i < freqEMMarray[0].hps.length; i++)
		  {
		    rect( i*10, height, 1, - freqEMMarray[0].hps[i] );
		  } // for

		  stroke(150, 50, 150);
		  rect( freqEMMarray[0].hps[freqEMMarray[0].maxBin]*10, height, 1, - freqEMMarray[0].hps[freqEMMarray[0].maxBin] );

		  stroke(50, 50, 255);
		  rect(freqEMMarray[0].secondMaxBin * 10, height, 1, -freqEMMarray[0].hps[freqEMMarray[0].secondMaxBin]);
		 */	
		//		ellipse(width / 2, height - input.getAdjustedFund(), 50, 50);

	} // draw

	/**
	 * @param input
	 */
	void drawHPSSecondHighest(Input input)
	{
		int	divideBy	= 8;
//		println("drawHPSSecondHighest()");
		Input.FrequencyEMM freqEMM  = input.frequencyArray[0];

		int	spacer;
		try
		{
			spacer	= width / freqEMM.hps.length;

			// draw rectangles for every frequency:
			stroke(255);
			for (int i = 0; i < freqEMM.hps.length; i++)
			{
				rect( i*spacer, height, 1, - (freqEMM.hps[i] / divideBy) );
				/*
 // I don't want to find my max and secondMax here, b/c the point is to test them from Input.
	    if (freqEMM.hps[i] > freqEMM.hps[maxBin])
	    {
	      maxBin  = i;
	    } else if (freqEMM.hps[i] > freqEMM.hps[secondMaxBin] && freqEMM.hps[i] < freqEMM.hps[maxBin])
	    {
	      secondMaxBin  = i;
	    } // if
				 */
			} // for

			//	  maxBin		= freqEMM.maxBin;
			//	  secondMaxBin	= freqEMM.secondMaxBin;

			stroke(50, 50, 255);
			rect(freqEMM.secondMaxBinBelow * spacer, height, 1, - (freqEMM.hps[freqEMM.secondMaxBinBelow] / divideBy));
			
			stroke(150, 50, 150);
//			rect( freqEMM.maxBin*spacer, height, 1, - (freqEMM.hps[freqEMM.maxBin] / divideBy) );


		} catch(NullPointerException npe)
		{
			println("InputClassEclipse_HPSCorrection.drawHPSSecondHighest: " + npe.getMessage());
		}
	} // drawHPSSecondHighest
	
	void drawPSSecondHighest(Input input)
	{
		PowerSpectrum	powerSpectrum = input.psArray[0];
		float[]	features	= powerSpectrum.getFeatures();
		int maxBin            = 0;
		int secondMaxBinBelow      = 0;

		int	spacer;
		try
		{
			spacer	= width / features.length;

			// draw rectangles for every frequency:
			stroke(255);
			for (int i = 0; i < features.length; i++)
			{
				rect( i*spacer, height/2, 1, - features[i] );
				
 // Finding my max here b/c I don't ever do it in Input (might have a time delay accuracy error):
	    if (features[i] > features[maxBin])
	    {
	      maxBin  = i;
	    } else if (features[i] > features[secondMaxBinBelow] && features[i] < features[maxBin] && i < maxBin)
	    {
	      secondMaxBinBelow  = i;
	    } // if
				 
			} // for

			//	  maxBin		= freqEMM.maxBin;
			//	  secondMaxBinBelow	= freqEMM.secondMaxBinBelow;

			stroke(150, 50, 150);
			rect(maxBin*spacer, height/2, 1,  - features[maxBin] );


			stroke(50, 50, 255);
			rect(secondMaxBinBelow * 10, height/2, 1, - features[secondMaxBinBelow]);
		} catch(NullPointerException npe)
		{
			println("InputClassEclipse_HPSCorrection.drawHPSSecondHighest: " + npe.getMessage());
		}
	} // drawPSSecondHighest
} // Driver
