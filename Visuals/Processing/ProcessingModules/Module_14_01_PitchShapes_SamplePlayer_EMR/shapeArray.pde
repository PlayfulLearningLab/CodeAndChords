/** source code derived from
https://forum.processing.org/one/topic/storing-shapes-in-an-array.html
center all shapes 11/20/16
*/

class shapeArray 
{ 
  color colour; 
  float shapeWidth; 
  float shapeHeight; 
  int num;
  
  /** Maybe at a certain point I'll pass the page parameters so that I can automatically place the shapes 
  in the center of the page */
  
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
    if (num % 12 ==0) {
      //this isn't actually centered which I find deeply, fundamentally annoying
      ellipse(279, 144, 200, 200);
      //triangle(213, 200, 318, 70, 426, 200);
      //arc(50, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==1) {
      polygon(279,144,100, 9);
     //rect(250, 50, 200, 200);
        //arc(100, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==2) {
      polygon(279,144,100, 8);
      //quad(138, 31, 186, 20, 269, 63, 230, 76);
      //arc(150, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==3) {
      polygon(279,144,100, 7);
      //ellipse(316, 144, 72, 72);
     //arc(200, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==4) {
     polygon(279,144,100, 6);
      //triangle(130, 75, 58, 20, 286, 75); 
     //arc(250, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==5) {
      polygon(279,144,100, 3);
    // rect(250, 50, 100, 200); 
    // arc(300, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==6) {
      beginShape();
      vertex(225, 200);
      vertex(280, 144);
      vertex(320, 144);
      vertex(375, 200);
      endShape();
      //MAKE THIS A trapezoid
     // star(316, 180, shapeWidth, shapeHeight, 5);
     //arc(350, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
     if (num % 12 ==7) {
      rect(250, 50, 100, 200); 
     // star(316, 180, shapeWidth, shapeHeight, 5);
     //arc(350, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
     if (num % 12 ==8) {
       rect(250, 50, 100, 200); 
     // star(316, 180, shapeWidth, shapeHeight, 5);
     //arc(350, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
    if (num % 12 ==9) {
      polygon(279, 144, 100, 5);
      //pentagon
       //ellipse(316, 244, 72, 72);
      //arc(400, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
     if (num % 12 ==10) {
       polygon(279, 144, 100, 4);
      // ellipse(316, 244, 72, 72);
      //arc(400, 300, shapeWidth, shapeHeight, PI, TWO_PI);
    } 
     if (num % 12 ==11) {
       star(279, 180, 100, 100, 5);
      // ellipse(316, 244, 72, 72);
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

void polygon(float x, float y, float radius, int npoints) {
  float angle = TWO_PI / npoints;
  beginShape();
  for (float a = 0; a < TWO_PI; a += angle) {
    float sx = x + cos(a) * radius;
    float sy = y + sin(a) * radius;
    vertex(sx, sy);
  }
  endShape(CLOSE);
}
  
  
  
  
  
  
  
  
  
  
} 