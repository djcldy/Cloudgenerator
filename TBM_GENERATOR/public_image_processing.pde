//
//public float getImgSD(PImage img) { 
//  // returns the standard deviation of an image 
//  float brightness = 0;  
//  float sum = 0;  
//  float average = 1;  
//  float variation = 0;
//  float avgVar;
//  float SD; 
//  img.loadPixels();
//  for (int i = 0; i < img.pixels.length; i++) {
//    float val = brightness(img.pixels[i]); 
//    brightness +=  val;
//  }
//  average = brightness/img.pixels.length; 
//  for (int i = 0; i < img.pixels.length; i++) {
//    float val = brightness(img.pixels[i]);  
//    variation += (val - average)*(val - average);
//  }  
//  avgVar = variation/img.pixels.length; 
//  SD = sqrt(avgVar);
//  return SD;
//}
//
//PImage getPImage() { 
//  String tempPath = "tempFrame.jpg";  
//  saveFrame("tempFrame.jpg");     
//  PImage temp = loadImage(tempPath);
//return temp;
//} 
//
//
//
//float CalcAvg(PImage p) {
// p.loadPixels(); 
//  float sum =0;
//  for (int i = 0; i < p.pixels.length; i++) {
//    float val = brightness(p.pixels[i]); 
//    sum += val;
//  }
//  float average = sum/p.pixels.length; 
//  return average;
//} 
//
//float CalcAvgPixel(FloatList pixelList) { 
//  float sum =0;
//  for (float p : pixelList) { 
//    float val = brightness(int(p)); 
//    sum += val;
//  }
//  float average = sum/(pixelList.size()); 
//  return average;
//} 
//
//float CalcStdevPixel(float average, FloatList pixelList) { 
//  
//  float variation = 0; 
//  
//  for (float p : pixelList) { 
//    float val = brightness(int(p)); 
//    variation += (val - average)*(val - average);
//  
//  }  
//  
//  float avgVariation = variation/pixelList.size(); 
//  float stdDev = sqrt(avgVariation);  
//  
//  // println("standard deviation = " + stdDev); 
//  
//  return stdDev;
//
//} 
//
//
//
//float CalcStdev(float average) { 
//
//  float variation = 0; 
//  
//  for (int i = 0; i < pixels.length; i++) {
//    float val = brightness(pixels[i]); 
//    variation += (val - average)*(val - average);
//  
//  }  
//  
//  float avgVariation = variation/pixels.length; 
//  float stdDev = sqrt(avgVariation);  
//  
//  return stdDev;
//
//} 
//
//public int getBin(float val, ArrayList<Bin> bins) {
//
//  int bin =0;
//  float step = 255.0/bins.size(); 
//
//  for (int k = 0; k < bins.size (); k ++) {
//    int r = bins.size()-k;
//    float threshold = float(r)*step;
//    if (val > threshold) return r;
//  }
//
//  return bin;
//
//}
