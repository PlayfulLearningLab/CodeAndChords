package core.input;

public interface MusicalInput 
{
	//Instrument Info
	public String 	getInputName();
		
	public String 	getInputType();
	
	public boolean 	isRealTimeInput();
	
	public boolean 	isPolyphonic();
	
	public int		getTotalNumInputs();
		
	
	//Instrument Input
	public int 		getMidiNote();
	
	public float 	getAmplitude();
	
	

}
