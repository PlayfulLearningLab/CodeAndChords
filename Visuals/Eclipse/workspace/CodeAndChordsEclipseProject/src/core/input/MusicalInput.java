package core.input;

public interface MusicalInput 
{
	public String getInputName();
		
	public String getInputType();
	
	public boolean isRealTimeInput();
	
	public boolean isPolyphonic();
	
	public int getMidiNote();
	
	public float getAmplitude();
	
	

}
