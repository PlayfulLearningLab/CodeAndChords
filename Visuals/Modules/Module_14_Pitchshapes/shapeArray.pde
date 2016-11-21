/** source code derived from
https://forum.processing.org/one/topic/storing-shapes-in-an-array.html
*/

class shapeArray 
{ 
  color colour; 
  float shapeWidth; 
  float shapeHeight; 
  int num;
  
  shapeArray(color colour, float shapeWidth, float shapeHeight, int num) 
  { 
    this.colour = colour; 
    this.shapeWidth = shapeWidth; 
    this.shapeHeight = shapeHeight + 200; 
    this.num = num;
  } 
  
  void display() 
  { 
    //smooth(); 
    fill(colour); 
    stroke(127); 
    //ellipseMode(CORNER); 
    //ellipse(50, 50, shapeWidth, shapeHeight); 
    if (num % 8 ==0) {
      //this isn't actually centered which I find deeply, fundamentally annoying
      triangle(280, 200, 360, 70, 440, 200);
      //arc(50, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==1) {
     rect(250, 50, 200, 200);
    //arc(100, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==2) {
      quad(138, 31, 186, 20, 269, 63, 230, 76);
      //arc(150, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==3) {
      ellipse(316, 144, 72, 72);
     //arc(200, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==4) {
      triangle(130, 75, 58, 20, 286, 75); 
     //arc(250, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==5) {
     rect(250, 50, 100, 200); 
    // arc(300, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==6) {
      star(316, 180, shapeWidth, shapeHeight, 5);
     //arc(350, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 8 ==7) {
       ellipse(316, 244, 72, 72);
      //arc(400, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    
    /** 
      * add the modules
      * to create different shapes
      * depending on input
      */
   // star(316, 180, shapeWidth, shapeHeight, 5);
  } 
  
  
  void star(float x, float y, float radius1, float radius2, int npoints) {
  float angle = TWO_PI / npoints;
  float halfAngle = angle/2.0;
  beginShape();
  for (float a = 0; a < TWO_PI; a += angle) {
    float sx = x + cos(a) * radius2;
    float sy = y + sin(a) * radius2;
    vertex(sx, sy);
    sx = x + cos(a+halfAngle) * radius1;
    sy = y + sin(a+halfAngle) * radius1;
    vertex(sx, sy);
  }
  endShape(CLOSE);
}
  
  
  
  
  
  
  
  
  
  
} 