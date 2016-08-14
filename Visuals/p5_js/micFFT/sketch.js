/**
 *  Visualize the frequency spectrum of live audio input\
 * 
 * NB - warning from Chrome: Remove support for the MediaStreamTrack.getSources() method. 
 *    This method was removed from the spec in favor of MediaDevices.enumerateDevices().
 */

var mic, fft;

function setup() {
   createCanvas(512,400);
   noStroke();
   fill(0,255,255);

   mic = new p5.AudioIn();
   mic.start();
   fft = new p5.FFT();
   fft.setInput(mic);
}

function draw() {
   background(200);
   var spectrum = fft.analyze();

   beginShape();
   vertex(0, height);
   for (i = 0; i<spectrum.length; i++) {
    vertex(i, map(spectrum[i], 0, 255, height, 0) );
   }
   endShape();

//   findFund(spectrum);
}

function findFund(spectrum)
{
    for(i = 0; i < spectrum.length; i++)
    {
        print("spectrum[" + i + "] = " + spectrum[i]);
    } // for
} // findFund