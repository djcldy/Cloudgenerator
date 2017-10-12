

class ImageThread implements Runnable {


  Thread thread;
  boolean drawNew = false;
  boolean isReady = false;
  // Specimen specimenNew;
  int pauseTime;
  String filepath;
  PImage parent, texture, parentA, textureA;
  PGraphics map;
  PVector size;
  int x, y;

  // Databaser dbsr;
  // Generator gnrtr;

  ImageThread (PApplet parent, String _filepath, PVector _size) {

    x = int(_size.x);
    y = int(_size.y);
    filepath = _filepath;

  }

  void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop()
  {
    thread = null;
  }

  // this will magically be called by the parent once the user hits stop
  // this functionality hasn't been tested heavily so if it doesn't work, file a bug
  public void dispose() {
    stop();
  }
  void run() {
    while (!isReady) { //
      // println("filepath:" + filepath);

      parent = loadImage(filepath);
      parentA = invert(parent);
      texture = parent.get();
      texture.resize(x,y);
      textureA = invert(texture);
      map = null;


      try {
        Thread.sleep(200);
        isReady = true;
      }
      catch(InterruptedException e) {
      }
    }
  }


  PImage normalize(PImage img) {

    PGraphics temp = createGraphics(img.width, img.height);

    temp.beginDraw();
    temp.image(img, 0, 0);
    temp.loadPixels();

    float min = 255;
    float max = 0;
    float brightness = 0;
    float sum = 0;

    for (int i = 0; i < temp.pixels.length; i++) {
      float val = brightness(temp.pixels[i]);
      brightness +=  val;
      if (val < min) min = val;
      if (val >max) max = val ;
    }

    float constant = 255.0/(max-min);
    float newMin  = 255.0;
    float tempRange = max-min;
    float newMax = 0;

    for (int i = 0; i <    temp.pixels.length; i++) {
      float val = brightness(temp.pixels[i]);
      float newVal = (val-min)*constant;
      temp.pixels[i] =  color(newVal,255);
    }
    temp.updatePixels();
    temp.endDraw();

    img = temp.get(); // normalized image

    return img;
  }

  PImage invert(PImage _img){
    PImage img = _img.get();
    PGraphics temp = createGraphics(img.width,img.height);
    temp.beginDraw();
    temp.image(img, 0, 0);
    temp.filter(INVERT);
    temp.endDraw();

    img = temp.get();
    return img;
  }

}
