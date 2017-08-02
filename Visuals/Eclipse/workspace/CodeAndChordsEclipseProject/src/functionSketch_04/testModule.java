package functionSketch_04;

import java.awt.Color;

import core.Shape;
import core.ShapeEditor;
import processing.core.PApplet;

public class testModule extends PApplet implements Runnable {

	private Shape 		shape;
	private ShapeEditor shapeEditor;
	
	private boolean		shapeEditorRunning;
	
	private boolean 	testingStarted = false;
	
	
	public static void main(String[] args)
	{
		PApplet.main("functionSketch_04.testModule");
	}
	
	public void settings()
	{
		size(925, 520);
		
	}
	
	public void setup()
	{
		this.shape = new Shape(this);
		this.shapeEditorRunning = false;
		System.out.println("checkpoint a");
		this.shapeEditor = new ShapeEditor(this, 925, 520);
		System.out.println("checkpoint b");
	}
	
	public void draw()
	{
		//System.out.println("checkpoint c");
		this.background(Color.MAGENTA.getRGB());
		//System.out.println("checkpoint d");
		if(!this.testingStarted)
		{
			this.testingStarted = true;
			new Thread(this).start();
		}
		this.shapeEditor.runSE(this.shapeEditorRunning);
	}
	
	private void testOpenClose()
	{
		if(this.shapeEditorRunning) 		this.shapeEditorRunning = false;
		else 								this.shapeEditorRunning = true;
		
		float startTime = this.millis();
		float refTime   = startTime;
		
		while(this.millis() < startTime + (10 * 1000))
		{
			if(this.millis() - refTime > 2000)
			{
				if(this.shapeEditorRunning) 		this.shapeEditorRunning = false;
				else 								this.shapeEditorRunning = true;
				
				refTime = this.millis();
			}
		}
	}

	@Override
	public void run() 
	{
		System.out.println("testing started");
		
		if(this.shapeEditorRunning) 		this.shapeEditorRunning = false;
		else 								this.shapeEditorRunning = true;
		
		float startTime = this.millis();
		float refTime   = startTime;
		
		while(this.millis() < startTime + (4 * 1000))
		{
			if(this.millis() - refTime > 1000)
			{
				if(this.shapeEditorRunning) 		this.shapeEditorRunning = false;
				else 								this.shapeEditorRunning = true;
				
				refTime = this.millis();
			}
		}
		
		if(!this.shapeEditorRunning) 		this.shapeEditorRunning = true;
		System.out.println("testing ended");
	}
	
}
