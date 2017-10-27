
PGraphics getVoxLayer(MicroTexture texture, float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){

    println("getVoxLayer...ratio: " + ratio);

    int voxXY = depthChannel.width;

    PGraphics temp = createGraphics(voxXY,voxXY);
    float t = 0.05; // percentage for microtexture

    temp.beginDraw();
    temp.background(0);

    for (int x = 0; x < temp.width; x ++){
      for (int y = 0; y < temp.height; y++){

        if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

           float val = brightness(depthChannel.get(x, y));
            color c = materChannel.get(x,y);

            float offset =0;
            if (texture != null){
                offset = texture.get(x,y); // offset for microtextur
            }
            // float offset = 0;
            // if (random(0,1)>0.999){println("offset:" + os*t);}
            float pp = (val-offset*t)/255; //

            if (pp < 0){ pp = 0;} // if the offset is less than zeo set to 0

            // ratio is the current layer
            // pp is the height of the given pixel

            if (i){ //

              if (pp > ratio) {
                temp.set(int(x), int(y), c);
              } // below halfway



            } else {

              pp = 1 - pp;

              if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway

            }
          }
        }
      }

    temp.endDraw();
    return temp;
 }



// public PImage removeArtifacts(PImage img){



//   for (int x = 0 ; x < img.width; x++){
//     for (int y = 0; y < img.height; y++){

//       if (brightness(img.get(x,y) > 100)){

//         if (x1 > 0) {

//         }

//         if (x2< img.width){

//         }


//         if (y1 > 0) {


//         }

//         if (y2< img.width){


//         }
//        int x1 = x - 1;
//        int x2 = x + 1;
//        int y1 = y - 1;
//        int y2 = y + 1;

//       }
//     }
//   }




// }



// //public float getImgSD(PImage img) {
// //  // returns the standard deviation of an image
// //  float brightness = 0;
// //  float sum = 0;
// //  float average = 1;
// //  float variation = 0;
// //  float avgVar;
// //  float SD;
// //  img.loadPixels();
// //  for (int i = 0; i < img.pixels.length; i++) {
// //    float val = brightness(img.pixels[i]);
// //    brightness +=  val;
// //  }
// //  average = brightness/img.pixels.length;
// //  for (int i = 0; i < img.pixels.length; i++) {
// //    float val = brightness(img.pixels[i]);
// //    variation += (val - average)*(val - average);
// //  }
// //  avgVar = variation/img.pixels.length;
// //  SD = sqrt(avgVar);
// //  return SD;
// //}
// //
// //PImage getPImage() {
// //  String tempPath = "tempFrame.jpg";
// //  saveFrame("tempFrame.jpg");
// //  PImage temp = loadImage(tempPath);
// //return temp;
// //}
// //
// //
// //
// //float CalcAvg(PImage p) {
// // p.loadPixels();
// //  float sum =0;
// //  for (int i = 0; i < p.pixels.length; i++) {
// //    float val = brightness(p.pixels[i]);
// //    sum += val;
// //  }
// //  float average = sum/p.pixels.length;
// //  return average;
// //}
// //
// //float CalcAvgPixel(FloatList pixelList) {
// //  float sum =0;
// //  for (float p : pixelList) {
// //    float val = brightness(int(p));
// //    sum += val;
// //  }
// //  float average = sum/(pixelList.size());
// //  return average;
// //}
// //
// //float CalcStdevPixel(float average, FloatList pixelList) {
// //
// //  float variation = 0;
// //
// //  for (float p : pixelList) {
// //    float val = brightness(int(p));
// //    variation += (val - average)*(val - average);
// //
// //  }
// //
// //  float avgVariation = variation/pixelList.size();
// //  float stdDev = sqrt(avgVariation);
// //
// //  // println("standard deviation = " + stdDev);
// //
// //  return stdDev;
// //
// //}
// //
// //
// //
// //float CalcStdev(float average) {
// //
// //  float variation = 0;
// //
// //  for (int i = 0; i < pixels.length; i++) {
// //    float val = brightness(pixels[i]);
// //    variation += (val - average)*(val - average);
// //
// //  }
// //
// //  float avgVariation = variation/pixels.length;
// //  float stdDev = sqrt(avgVariation);
// //
// //  return stdDev;
// //
// //}
// //
// //public int getBin(float val, ArrayList<Bin> bins) {
// //
// //  int bin =0;
// //  float step = 255.0/bins.size();
// //
// //  for (int k = 0; k < bins.size (); k ++) {
// //    int r = bins.size()-k;
// //    float threshold = float(r)*step;
// //    if (val > threshold) return r;
// //  }
// //
// //  return bin;
// //
// //}
