package coreV2_CanvasEffects;

public interface Drawable {
	
	public void rect(int xPos, int yPos, int width, int height);
	
	public void ellipse(int xPos, int yPos, int width, int height);
	
	public void line(int x1, int y1, int x2, int y2);
	
	public void background();
}
