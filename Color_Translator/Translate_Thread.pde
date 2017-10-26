

class TranslateThread implements Runnable {


  Thread thread;
  boolean drawNew = false;
  boolean isReady = false;
  boolean done = false;
  // Specimen specimenNew;
  int pauseTime;
  String filepath;
  PImage parent, texture, parentA, textureA;
  PGraphics map;
  PVector size;
  int x, y;

  // Databaser dbsr;
  // Generator gnrtr;

  TranslateThread (String path) {
    filepath = path;
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
     println("colorizing:" + filepath);
    colorize(filepath);
    println("completed:" + filepath);
      try {
        Thread.sleep(200);
        isReady = true;
      }
      catch(InterruptedException e) {
      }
    }
  }

  void colorize(String path){

  color black = color(0,0,0);

  PImage temp = loadImage(path);
  temp.loadPixels();

   for (int i = 0; i < temp.pixels.length; i++) {
      color c = temp.pixels[i];
      if (c != black){ temp.pixels[i] =  translateColor(c);}

    }
    temp.updatePixels();
    temp.save("colorized" +  path);
  }



  color translateColor(color c){

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



String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }

}

}

