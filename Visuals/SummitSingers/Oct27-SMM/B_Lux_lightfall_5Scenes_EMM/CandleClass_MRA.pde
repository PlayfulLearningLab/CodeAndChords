class Candle {
  float xGlow;        // x pos of candle
  float yGlow;        // y pos of candle
  float xSpaced;
  float size;
  float curFreq;
  float speed;
  float gravity;
  float redTint;
  float greenTint;
  float blueTint;
  float sizeAdjust;
  
//Constructors with input variables
 Candle(float inRedTint, float inGreenTint, float inBlueTint, float inXSpaced, float inSizeAdjust){
   redTint = inRedTint;
   greenTint = inGreenTint;
   blueTint = inBlueTint;
   xSpaced = inXSpaced;
   sizeAdjust = inSizeAdjust;
   speed = 0;
   gravity = height/250;
 }

//methods

 void fall() {
    xGlow = xSpaced;
    size = 0.02*width*sizeAdjust;
    yGlow = yGlow + speed;
    speed = speed + gravity;
    if (yGlow>height+(height/50)){
      speed = speed * -0.3;
      yGlow = height+height/50;
    }
  }//void fall

  void resetYDrop(){
    yGlow = -height/10;
  }
  
  void hover(float inFreq, float inAmp) {
    xGlow = xSpaced;
//    yGlow = height - inFreq * 2;
    yGlow = height - inFreq;
//    size = inAmp*width/2*sizeAdjust;
    size  = inAmp;
    constrain(yGlow,0,height);
  }//void hover
  
  void circle(float inAmp, float inFreq, float inXCenter, float inYCenter) {
//    float rad = 100+5000*inAmp;
    float rad = 100 + inAmp;
    float deg = inFreq;
    xGlow = cos(radians(deg))*rad+inXCenter;
    yGlow = sin(radians(deg))*rad+inYCenter;
    constrain(xGlow, 0, width);
    constrain(yGlow, 0, height);
//    size = inAmp*width/2*sizeAdjust;
    size = inAmp;
    if (inAmp < 0.01) {
      deg = random(360);
    }
  }
 
 void freqPoints (float inFreq, float inAmp){
   size = inAmp*width*2*sizeAdjust;
   curFreq = inFreq;
   if ((curFreq > 250) && (curFreq < 300)){
     xGlow = 0.1*width;
     yGlow = 0.35*height;
   }
   if ((curFreq > 300) && (curFreq < 350)){
     xGlow = 0.25*width;
     yGlow = 0.25*height;
   }
   if (curFreq > 350){
     xGlow = 0.6*width;
     yGlow = 0.15*height;
   }
 }//void angels
 
 void highlight(float inAmp, float inXGlow, float inYGlow) {
//   size = inAmp*width*2*sizeAdjust;
   size = inAmp;
   xGlow = inXGlow;
   yGlow = inYGlow;
 }
 
 float getXGlow(){
   return xGlow;
 }
 float getYGlow(){
   return yGlow;
 }
 float getSize(){
 return size;
 }
 float getRedTint(){
    return redTint/100;
 }
 float getGreenTint(){
    return greenTint/100;
 }
 float getBlueTint(){
    return blueTint/100;
 }
}//class Shine