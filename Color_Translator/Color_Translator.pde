
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import controlP5.*;
import java.util.*;
import java.io.File;

boolean isStart = true;
int i = 0;

ArrayList<TranslateThread> threads = new ArrayList<TranslateThread>();

  void setup(){
    size(200,200);
    run();
  }

  // void draw(){

  //   boolean done = true;

  //   if (isStart){
  //     run();
  //     isStart = false;
  //   } else if (threads != null) {
  //   for (TranslateThread t: threads){

  //       if (!t.done){done = false;}

  //       if ((t.isReady) && (!t.done)) {
  //         t.done = true;
  //         t.stop();
  //         i++;
  //       }



  //     }
  //   }

  //   if (threads != null){
  //     if (i == threads.size()){
  //       threads = null;
  //       println("done");
  //     }
  //   }
  // }


  void run(){


   println("initializing");
;

    String localPath = "/exports/";
    String[] filenames = listFileNames(sketchPath() + localPath);


    if (filenames !=null) {
      for (int j = 0; j < filenames.length; j++){


        String path = localPath + filenames[j];

        println("colorize:" + path);

        colorize(path);

        println("complete:" + path);

        // TranslateThread t = new TranslateThread(path);
        // t.start();
        // threads.add(t);

        // println("completed:" + path);
      }

    }
    println("completed, threads size = " + threads.size());
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
    temp.save("_" + path);
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
