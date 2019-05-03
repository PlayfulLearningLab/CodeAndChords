package coreV2_CanvasEffects;

import coreV2.Canvas;
import processing.core.PApplet;

public class RGBEffect extends CanvasEffect {

	private float 	effectAmount;
	private float	maxEffect;
	
	private int		lastTrigger;
	private int		duration;
	
	public RGBEffect(PApplet parent, Canvas canvas) {
		super(parent, canvas);
		
		this.effectAmount = 0;
		this.maxEffect = 1;
		
		this.lastTrigger = 0;
		this.duration = 1;
	}
	
	private void updateValues()
	{
		
	}

	@Override
	public void triggerEvent(int eventType) {

	}

	@Override
	protected void drawRect(int xPos, int yPos, int width, int height) {

	}

	@Override
	protected void drawEllipse(int xPos, int yPos, int width, int height) {

	}

	@Override
	protected void drawLine(int x1, int y1, int x2, int y2) {

	}

	@Override
	protected void drawBackground() {

	}

}
