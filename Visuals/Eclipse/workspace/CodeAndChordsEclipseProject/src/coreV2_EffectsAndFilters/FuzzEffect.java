package coreV2_EffectsAndFilters;

import processing.core.PApplet;

public class FuzzEffect extends CanvasEffect {
	

	protected FuzzEffect(PApplet parent, Drawable output) {
		super(parent, output);
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
	
	@Override
	public int getType() {
		return Drawable.EFFECT;
	}

}
