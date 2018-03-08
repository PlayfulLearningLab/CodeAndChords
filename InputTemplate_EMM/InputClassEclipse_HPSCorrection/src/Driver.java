import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

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
//		writeToFile("/Users/codeandchords/Documents/CodeAndChords/InputTemplate_EMM/log.txt", new int[] { 1, 2, 3 }, "Still testing");
		
	} // setup

	public void draw()
	{
		background(0);
//		testInput();
		
//		background(250, 150, 204);
		stroke(230, 179, 204);
		fill(230, 179, 204);
//		rect(0, 0, width - 200, height);
		
//		drawPSSecondHighest(input);
//		drawHPSSecondHighest(input);
		
//		inputFreqRain.rain(input.getAdjustedFund());
//		println("input.getAdjustedFundAsHz() = " + input.getAdjustedFundAsHz());
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
		ellipse(width / 2, height - input.getAdjustedFund(), 50, 50);
		println("input.getAdjustedFund() = " + input.getAdjustedFund());
		println("input.getAmplitude() = " + input.getAmplitude());

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
	
	public int[][] testInput()
	{
		int[]	resultBuffer	= new int[100];
		for(int i = 0; i < resultBuffer.length; i++)
		{
			resultBuffer[i]	= 0;
		}
		ArrayList<Integer>	tooHigh	= new ArrayList<Integer>();
		ArrayList<Integer>	tooLow	= new ArrayList<Integer>();
		
		Input input	= new Input("Emily_CMajor-2016_09_2-16bit-44.1K Tuned.wav");
		int	endTime	= millis() + 1500;
		int	bin;

		while(millis() < endTime)
		{
			// Getting the value in the 10's place:
			bin	= (int) ((input.getAdjustedFundAsHz() / 10 ) % 10);
			if(bin < 0) {
				tooLow.add(bin);
			} else if(bin < resultBuffer.length) {
				resultBuffer[bin]++;
			} else {
				tooHigh.add(bin);
			}
		} // while
		
		int[] tooLowArray	= new int[tooLow.size()];
		for(int i = 0; i < tooLowArray.length; i++)
		{
			tooLowArray[i]	= tooLow.get(i);
		} // for
		
		int[] tooHighArray	= new int[tooHigh.size()];
		for(int i = 0; i < tooHighArray.length; i++)
		{
			tooHighArray[i]	= tooHigh.get(i);
		} // for
		
		String	logFile	= "/Users/codeandchords/Documents/CodeAndChords/InputTemplate_EMM/InputClassEclipse_HPSCorrection/filter_tests.txt";
		this.writeToFile(logFile, tooLowArray, "tooLowArray - without filters:");
		this.writeToFile(logFile, resultBuffer, "resultBuffer - without filters:");
		this.writeToFile(logFile, tooHighArray, "tooHighArray - without filters:");
		
		return new int[][] { tooLowArray, resultBuffer, tooHighArray };
	} // testInput
	
	public void writeToFile(String filename, int[] array, String header)
	{
		LocalDateTime	timeDate	=	LocalDateTime.now();
		
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) 
		{
			bw.write(timeDate.toString() + "\n" + header + "\n");
			
			for(int i = 0; i < array.length; i++)
			{
				bw.write((i * 10) + ": " + array[i] + "\n");				
			}
			bw.write("\n");

			// no need to close it.
			//bw.close();

			System.out.println("writeToFile: done writing");

		} catch (IOException e) {
			e.printStackTrace();
		} // catch
	} // writeToFile
} // Driver
