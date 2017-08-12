package core;

import processing.core.PApplet;

public abstract class Module extends PApplet {
	
//	private		DisposeHandler	disposeHandler;
	protected	Input			input;
	protected	Shape			shape;
	protected 	ShapeEditor		shapeEditor;

	public void settings()
	{
		size(925, 520);
	}
	
	public void setup()
	{
//		this.disposeHandler	= new DisposeHandler(this, this);
		
//		System.out.println("Module: hi! disposeHandler.module.input = " + this.disposeHandler.module.input);
	} // setup
	

	public void setShapeEditorRunning(boolean isRunning)
	{
		this.shapeEditor.setIsRunning(isRunning);
	} // setShapeEditorRunning
	
	public Shape getShape() {
		return this.shape;
	} // getShape
	
	public abstract String[] getLegendText();

} // Module
