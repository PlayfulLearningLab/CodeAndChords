package functionSketch_06;

import java.awt.Color;

import processing.core.PApplet;

public class LogoAnimation
{

	private PApplet parent;

	private int startTime;

	private Color ustPurple;
	private Color boltColor;

	private int boxHeight;
	private int boxWidth;

	private int boxIncrament;

	private int boxXPos;
	private int boxYPos;

	private int[][] coordinateBuffer;

	private int randX;
	private int randY;

	private int index = 1;

	private int resetCount = 0;

	private float fillColor = 0;
	
	private float alpha = 255;
	private boolean fade;

	public LogoAnimation(PApplet parent)
	{
		this.parent = parent;

		this.startTime = parent.millis();

		this.ustPurple  = new Color(81, 12, 118);
		this.boltColor = this.ustPurple;

		this.boxHeight = 200;
		this.boxWidth = 100;
		this.boxIncrament = 30;

		this.boxXPos = -this.boxIncrament;
		this.boxYPos = parent.height/2;

		this.coordinateBuffer = new int[(int) Math.ceil((parent.width + 2*this.boxWidth)/this.boxIncrament)][2];

		this.coordinateBuffer[0][0] = -this.boxWidth;
		this.coordinateBuffer[0][1] = this.boxYPos;

		this.coordinateBuffer[this.coordinateBuffer.length - 1][0] = parent.width + this.boxWidth;
		this.coordinateBuffer[this.coordinateBuffer.length - 1][1] = this.boxYPos;

		for(int i = 1; i < coordinateBuffer.length - 1; i++)
		{
			this.randX = (int) (Math.random()*this.boxWidth);
			this.randY = (int) (Math.random()*this.boxHeight) - (this.boxHeight/2);

			this.coordinateBuffer[i][0] = (i*this.boxIncrament) + this.randX;
			this.coordinateBuffer[i][1] = this.boxYPos + this.randY;
		}

		this.fade = false;
		
		this.parent.registerMethod("draw", this);
		System.out.println("animation started");

	}

	public void draw()
	{
		this.parent.fill(0);
		this.parent.background(0);

		this.randX = (int) (Math.random()*boxWidth);
		this.randY = (int) (Math.random()*boxHeight) - (boxHeight/2);

		if(this.index != 0 && this.index != this.coordinateBuffer.length - 1)
		{
			this.coordinateBuffer[this.index][0] = (this.index*boxIncrament) + randX;
			this.coordinateBuffer[this.index][1] = boxYPos + randY;
		}

		
		if(this.parent.millis() - this.startTime > 5000 && this.fade && this.alpha > 0)
		{
			this.alpha -= 5;
		}

		this.parent.strokeWeight(10);

		this.boltColor = this.ustPurple;

		int halfLife = 1;



		for(int i2 = 0; i2 < this.coordinateBuffer.length; i2++)
		{
			this.parent.stroke(boltColor.getRed(), boltColor.getGreen(), boltColor.getBlue(), this.alpha);

			if(index-i2 != 0)
			{
				this.parent.line(this.coordinateBuffer[(index + this.coordinateBuffer.length - i2)%this.coordinateBuffer.length][0], 
						this.coordinateBuffer[(index + this.coordinateBuffer.length - i2)%this.coordinateBuffer.length][1], 
						this.coordinateBuffer[(index + this.coordinateBuffer.length - i2 - 1)%this.coordinateBuffer.length][0], 
						this.coordinateBuffer[(index + this.coordinateBuffer.length - i2 - 1)%this.coordinateBuffer.length][1]);
			}


			if(i2 > halfLife)
			{
				this.boltColor = this.boltColor.darker();
				halfLife = halfLife*2 + 1;
			}
		}

		if(fillColor < 255)
			fillColor += (.5 + fillColor/25);

		this.parent.fill(fillColor, this.alpha);
		this.parent.stroke(fillColor, this.alpha);


		this.parent.textSize(70);
		this.parent.textAlign(this.parent.CENTER, this.parent.CENTER);
		this.parent.text("CODE+CHORDS", this.parent.width/2, this.parent.height/2 - 20);

		this.parent.strokeWeight(4);
		this.parent.line(this.parent.width/2 - 260, this.parent.height/2 + 30 , this.parent.width/2 + 260, this.parent.height/2 + 30 );

		this.parent.textSize(30);
		this.parent.text("Playful Learning Lab", this.parent.width/2 + 110, this.parent.height/2 + 50);

		this.index++;

		if(this.index == this.coordinateBuffer.length - 1)
		{
			this.index = 1;
			this.resetCount++;
		}
		
		

		try {
			Thread.sleep(26);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(this.alpha < 5)
		{
			this.parent.unregisterMethod("draw", this);
		}

	}
	
	public void startFade()
	{
		this.fade = true;
	}

}
