package functionSketch_02;

import net.beadsproject.beads.analysis.featureextractors.PowerSpectrum;
import net.beadsproject.beads.analysis.segmenters.ShortFrameSegmenter;
import net.beadsproject.beads.core.*;
import net.beadsproject.beads.data.SampleManager;
import processing.core.PApplet;
import processing.sound.*;

public class FunctionSketch_02_MultiInput extends PApplet 
{
	
	private AudioContext            ac;
	private FFT[]                   fft;
	private float[]                 fundamentalArray;
	private float[]                 adjustedFundArray;
	private int                     numInputs;
	private PowerSpectrum[]         psArray;
	private ShortFrameSegmenter[]   sfsArray;
	private SampleManager           sampleManager;
	
	
	public static void main( String[] args )
	{
		PApplet.main("functionSketch_01.FunctionSketch_01_v1_MultiInput");
	}
	
	public void settings()
	{
		size(925,520);
	}
	
	public void setup()
	{
		background(0);
		
	}
	
	public void draw()
	{
		
	}
	
	public FunctionSketch_02_MultiInput(AudioContext audioContext, int numberInputs)
	{
		this.ac = audioContext;
		this.numInputs = numberInputs;
		
		
	}
	
	
	
	
	
	
	
}
