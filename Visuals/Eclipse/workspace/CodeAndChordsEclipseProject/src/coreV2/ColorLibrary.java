package coreV2;
/*
 * Rachel Farah
 * 08/21/2018
 */
public class ColorLibrary 
{
	private static final ColorLibrary instance = new ColorLibrary();
	
	public static ColorLibrary getInstance()
	{
		return ColorLibrary.instance;
	}
	
	public static ColorLibrary newInstance()
	{
		return ColorLibrary.getInstance();
	}
	
	public static void main(String[] args)
	{
		ColorLibrary rain;
		rain=ColorLibrary.getInstance();
		rain.getPreset(0);
	}
	public int[][] getPreset(int preset)
	{
		int[][] result=new int[12][3];
		if(preset !=0 && preset !=1 && preset !=2)throw new IllegalArgumentException(this.getClass().getName()+"Preset is not 0, 1, or 2.");
		if(preset==0)
		{
			int[][] color=  {{255, 0, 0},
							{255, 127, 0},
							{255, 255, 0},
							{127, 255, 0},
							{0, 255, 0},
							{0, 255, 127},
							{0, 255, 255},
							{0, 127, 255},
							{0, 0, 255},
							{127, 0, 255},
							{255, 0, 255},
							{255, 0, 127}};

			for(int i=0; i<12; i++)
			{
					for(int j=0; j<3; j++)
					{
						result[i][j]=color[i][j];
					}
			}	
		}
		if(preset==1)
		{
			
		}
		if(preset==2)
		{
			
		}
		return result;
	}
}
