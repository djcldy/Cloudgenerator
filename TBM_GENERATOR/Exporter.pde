

class Exporter implements Runnable {


  Thread thread;
  boolean drawNew = false;
  boolean isReady = false;

  PImage imgExport, imgVisual;
  PImage displayLayer;
  PImage dC, aC, mC;
  int pauseTime, dimXY, dimZ;
  boolean invert;
  float ratioX;
  float layerVoxels;
  MicroTexture microTexture;


  Exporter (PApplet parent, MicroTexture _microTexture, float _ratio, boolean _invert, PImage depthChannel, PImage alphaChannel, PImage materChannel, int _dimXY, int _dimZ) {

    println("initialize exporter");

    invert  =   _invert;
    dC      =   depthChannel;
    aC      =   alphaChannel;
    mC      =   materChannel;
    dimXY   =   _dimXY;
    dimZ    =   _dimZ;
    microTexture = _microTexture;

    // ratioX       =   _ratio;
    // int voxZ     =   100;
    // layerVoxels  =   voxZ/LayersZ; // number of vertical voxels per layer
    // int z        =   int(zz % layerVoxels);
    // float ratio  =   float(z)/ layerVoxels;

  }

  void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop(){
    println("exporter stopping...");
    thread = null;
  }

  public void dispose() {
    stop();
  }


  void run() {
    while (!isReady) { //

      println("voxel dimensions = " +  int(DimXY/0.040) + "," + int(DimXY/0.040) +"," + dimZ);
     for (int z = 0; z < dimZ;  z++){

      boolean invertLayer = false;

      float l = float(z)/float(dimZ);

      if (l > 0.5){ invertLayer = true;}

      println("invert layer");

        imgExport = getVoxLayer(microTexture, getRatio(l),invertLayer,dC,aC,mC);

        PImage temp = imgExport.get();
        temp.resize(int(DimXY/0.040),int(DimXY/0.080));
        temp.save("exports/layer_" + z + ".png");
      }

      try {
        Thread.sleep(300);
        isReady = true;
      }
      catch(InterruptedException e) {
      }
    }
  }



  float getRatio(float layer){

      int voxZ = int(DimZ/0.030); //  num voxels in Z
      int zz = int(voxZ*layer); //
      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = int(zz % layerVoxels);
      float ratio = float(z)/ layerVoxels;
      return ratio;

  }



// PGraphics getVoxLayer(float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){

//     int voxXY = depthChannel.width;

//     // println("getVox layer:" + voxXY);

//     PGraphics temp = createGraphics(voxXY,voxXY);
//     float t = 0.05; // percentage for microtexture

//     temp.beginDraw();
//     temp.background(0);

//     for (int x = 0; x < temp.width; x ++){
//       for (int y = 0; y < temp.height; y++){

//         if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

//            float val = brightness(depthChannel.get(x, y));
//             color c = materChannel.get(x,y);
//             // float offset = microTexture.get(x,y); // offset for microtexture
//             float offset = 0;
//             // if (random(0,1)>0.999){println("offset:" + os*t);}
//             float pp = (val-offset*t)/255; //

//             if (pp < 0){ pp = 0;} // if the offset is less than zeo set to 0

//             // ratio is the current layer
//             // pp is the height of the given pixel

//             if (i){ //

//               if (pp > ratio) {

//                 temp.set(int(x), int(y), c);

//               } // below halfway



//             } else {

//               pp = 1 - pp;

//               if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway

//             }
//           }
//         }
//       }

//     temp.endDraw();
//     return temp;
//  }

// PGraphics getVoxLayer(float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){

//     int voxXY = depthChannel.width;

//     PGraphics temp = createGraphics(voxXY,voxXY);
//     temp.beginDraw();
//     temp.background(0);

//     for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {

//         if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

//            float val = brightness(depthChannel.get(x, y));
//             color c = materChannel.get(x,y);
//          // c = translatePo(c);
//             float pp = val/255;



//             if (i){
//               if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway
//             } else {
//               pp = 1 - pp;
//               if (pp > ratio) { temp.set(int(x), int(y), c);} // below halfway
//             }

//             // if (invert){
//             //   if (pp >= ratio) { temp.set(int(x), int(y), c);} // below halfway
//             // } else {
//             //   pp = 1 - pp;
//             //   if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway
//             // }

//           }
//         }
//       }

//     temp.endDraw();
//     return temp;
//     }


// PGraphics getLayer(float ratio, boolean invert, PImage depthChannel, PImage alphaChannel, PImage materChannel){
//     int voxXY = int(DimXY/0.040);
//     PGraphics temp = createGraphics(voxXY,voxXY);
//     temp.beginDraw();
//     temp.background(0);
//     for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {
//         if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white
//            float val = brightness(depthChannel.get(x, y));
//             color c = materChannel.get(x,y);
//             // c = translatePo(c);
//             float pp = val/255;
//             if (invert){
//               if (pp >= ratio) { temp.set(int(x), int(y), c);} // below halfway
//             } else {
//               pp = 1 - pp;
//               if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway
//             }

//           }
//         }
//       }

//     temp.endDraw();
//     return temp;
//     }


  color translatePo(color c){

    color Cyan    = color(0,89,158);
    color Magenta = color(161,35,99);
    color Yellow  = color (213, 178, 0);
    color Black   = color(30,30,30);
    color White   = color(220,222,216);

    float r0 = red(c)/255;
    float g0 = green(c)/255;
    float b0 = blue(c)/255;

    float k = 1 - max(r0,g0,b0); // return maximum?
    float cy = (1 - r0 -k)/(1-k);
    float mg = (1 - g0 -k)/(1-k);
    float ye = (1 - b0 -k)/(1-k);

    float sum = k + cy + mg+ ye;
    float rnd = random(0,sum);

    if ((rnd > 0) && (rnd < k)){
      if (k > 0.5){
        return Black;
      } else {
        return White;
      }
    } else if (( rnd > k ) && (rnd < k + cy)){ return Cyan;
    } else if (( rnd > k + cy ) && (rnd < k + cy + ye)){ return Yellow;
    } else if (( rnd > k + cy + ye)){ return Magenta;}

    return White;

  }


}
