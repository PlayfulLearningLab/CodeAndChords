package core.input;

public interface MusicalInput 
{
	//Instrument Info
	public String 	getInputName();
	
	public void		setInputName(String inputName);
		
	public String 	getInputType();
	
	public boolean 	isRealTimeInput();
	
	public boolean 	isPolyphonic();
	
	public int		getTotalNumChannels();
		
	
	//Instrument Input
	public int 		getMidiNote();
	
	public float 	getAmplitude();
	
	public int 		getMidiNote(int channel);
	
	public float 	getAmplitude(int channel);
	
	

}
