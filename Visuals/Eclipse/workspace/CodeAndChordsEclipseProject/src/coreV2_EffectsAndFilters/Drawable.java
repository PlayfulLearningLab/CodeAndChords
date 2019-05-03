package coreV2_EffectsAndFilters;

public interface Drawable {
	
	public static final int CANVAS = 0;
	public static final int EFFECT = 1;
	public static final int FILTER = 2;
	
	public void rect(int xPos, int yPos, int width, int height);
	
	public void ellipse(int xPos, int yPos, int width, int height);
	
	public void line(int x1, int y1, int x2, int y2);
	
	public void background();
	
	public void setOutput(Drawable output);
	
	public int getType();
}
