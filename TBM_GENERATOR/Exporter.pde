

class Exporter implements Runnable {


  Thread thread;
  boolean drawNew = false;
  boolean isReady = false;

  PImage imgExport, imgVisual;
  PImage displayLayer;
  PImage dC, aC, mC;
  int pauseTime, dimXY;
  boolean invert;
  float ratio;
  float layerVoxels;


  VoxelLayer (PApplet parent, boolean _invert, PImage depthChannel, PImage alphaChannel, PImage materChannel, int dim) {

    invert = _invert;
    dC = depthChannel;
    aC = alphaChannel;
    mC = materChannel;
    dimXY = dim;

    layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
    int z = int(zz % layerVoxels);
    float ratio = float(z)/ layerVoxels;

  }

  void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop()
  {
    thread = null;
  }


  public void dispose() {
    stop();
  }



  void run() {
    while (!isReady) { //


     for (int z = 0; z < voxZ;  z++){

      boolean invertLayer = true;
      float l = float(z)/float(voxZ);

      if (l > 0.5){ invertLayer = false;}
        imgExport = getLayer(ratio,invert,dC,aC,mC);
        imgExport.save("exports/layer" + nf(l, 3) + ".png");
      }

      try {
        Thread.sleep(200);
        isReady = true;
      }
      catch(InterruptedException e) {
      }
    }
  }




PGraphics getLayer(float ratio, boolean invert, PImage depthChannel, PImage alphaChannel, PImage materChannel){

    int voxXY = int(DimXY/0.040);

    PGraphics temp = createGraphics(voxXY,voxXY);
    temp.beginDraw();
    temp.background(0);

    for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {

        if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

           float val = brightness(depthChannel.get(x, y));
            color c = materChannel.get(x,y);
            c = translatePo(c);
            float pp = val/255;

            if (invert){
              if (pp >= ratio) { temp.set(int(x), int(y), c);} // below halfway
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
