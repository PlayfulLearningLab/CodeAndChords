package addons;

import processing.core.PApplet;

/**
 *  from https://processing.org/examples/scrollbar.html
 */

public class HScrollbar {
	int swidth, sheight;    // width and height of bar
	float xpos, ypos;       // x and y position of bar
	float spos, newspos;    // x position of slider
	private float sposMin; // max and min values of slider
	private float sposMax;
	int loose;              // how loose/heavy
	boolean over;           // is the mouse over the slider?
	boolean locked;
	float ratio;

	PApplet	parent;

	public HScrollbar (PApplet parent, float xp, float yp, int sw, int sh, int l) {
		this.parent = parent;
		swidth = sw;
		sheight = sh;
		int widthtoheight = sw - sh;
		ratio = (float)sw / (float)widthtoheight;
		xpos = xp;
		ypos = yp-sheight/2;
		spos = xpos + swidth/2 - sheight/2;
		newspos = spos;
		setSposMin(xpos);
		setSposMax(xpos + swidth - sheight);
		loose = l;
	}

	public void update() {
		if (overEvent()) {
			over = true;
		} else {
			over = false;
		}
		if (parent.mousePressed && over) {
			locked = true;
		}
		if (!parent.mousePressed) {
			locked = false;
		}
		if (locked) {
			newspos = parent.constrain(parent.mouseX-sheight/2, getSposMin(), getSposMax());
		}
		if (parent.abs(newspos - spos) > 1) {
			spos = spos + (newspos-spos)/loose;
		}
	}


	/*
 // currently calling constrain() from PApplet -- it doesn't like when we try to override.
  float constrain(float val, float minv, float maxv) {
    return min(max(val, minv), maxv);
  }
	 */

	private boolean overEvent() {
		if (parent.mouseX > xpos && parent.mouseX < xpos+swidth &&
				parent.mouseY > ypos && parent.mouseY < ypos+sheight) {
			return true;
		} else {
			return false;
		}
	}

	public void display() {
		parent.noStroke();
		parent.fill(204);
		parent.rect(xpos, ypos, swidth, sheight);
		if (over || locked) {
			parent.fill(0, 0, 0);
		} else {
			parent.fill(102, 102, 102);
		}
		parent.rect(spos, ypos, sheight, sheight);
	}

	public float getPos() {
		// Convert spos to be values between
		// 0 and the total width of the scrollbar
		return spos * ratio;
	} // getPos

	public float getSposMin() {
		return sposMin;
	}

	public void setSposMin(float sposMin) {
		this.sposMin = sposMin;
	}

	public float getSposMax() {
		return sposMax;
	}

	public void setSposMax(float sposMax) {
		this.sposMax = sposMax;
	}
} // class
