package demo_01;

public class ArrayFillTest {
	
	public void main(String[] args)
	{
		int[]	array0	= { 0, 1, 2 };
		int[]	array1	= array0;
		
		for(int i = 0; i < array1.length; i++)
		{
			System.out.println("array1[" + i + "] = " + array1[i]);
		} // for
	} // main
} // ArrayFillTest
