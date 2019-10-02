package javaAudioTest;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;

public class JavaAudioTest 
{

	public static void main(String[] args)
	{
		
		Mixer.Info[] mi = AudioSystem.getMixerInfo();
		
		for (Mixer.Info info : mi) 
		{
			System.out.println("info: " + info);
			Mixer m = AudioSystem.getMixer(info);
			
			System.out.println("mixer " + m);
			Line.Info[] sl = m.getSourceLineInfo();
			
			for (Line.Info info2 : sl) {
				System.out.println("    info: " + info2);
				Line line = null;
				
				try {
					line = AudioSystem.getLine(info2);
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (line instanceof SourceDataLine) {
					SourceDataLine source = (SourceDataLine) line;

					DataLine.Info i = (DataLine.Info) source.getLineInfo();
					for (AudioFormat format : i.getFormats()) {
						System.out.println("    format: " + format);
					}
				}
			}//for source line info
			
			System.out.println("\n\n\n");
		}
	}
	
	
}
