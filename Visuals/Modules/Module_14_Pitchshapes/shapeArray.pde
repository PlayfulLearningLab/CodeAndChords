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
    this.shapeHeight = shapeHeight; 
    this.num = num;
  } 
  
  void display() 
  { 
    //smooth(); 
    fill(colour); 
    stroke(127); 
    //ellipseMode(CORNER); 
    //ellipse(50, 50, shapeWidth, shapeHeight); 
    if (num % 7 ==0) {
      triangle(80, shapeWidth, 108, 70, 136, shapeWidth);
    } 
    if (num % 7 ==1) {
      rect(250, 250, 200, 200);
    } 
    if (num % 7 ==2) {
      quad(189, 18, shapeWidth, 18, 216, 360, 144, 360);
    } 
    if (num % 7 ==3) {
      ellipse(316, 144, 72, 72);
    } 
    if (num % 7 ==4) {
      triangle(30, 75, 58, 20, 86, 75); 
    } 
    if (num % 7 ==5) {
      arc(479, 300, shapeWidth, 280, PI, TWO_PI);
    } 
    if (num % 7 ==6) {
      star(316, 180, shapeWidth, shapeHeight, 5);
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