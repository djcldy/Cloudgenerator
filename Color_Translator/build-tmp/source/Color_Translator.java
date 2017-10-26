import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Collections; 
import java.util.Comparator; 
import java.util.ArrayList; 
import controlP5.*; 
import java.util.*; 
import java.io.File; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Color_Translator extends PApplet {









boolean isStart = true;
int i = 0;

ArrayList<TranslateThread> threads = new ArrayList<TranslateThread>();

  public void setup(){
    
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


  public void run(){


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




  public void colorize(String path){

  int black = color(0,0,0);

  PImage temp = loadImage(path);
  temp.loadPixels();

   for (int i = 0; i < temp.pixels.length; i++) {
      int c = temp.pixels[i];
      if (c != black){ temp.pixels[i] =  translateColor(c);}

    }

    temp.updatePixels();
    temp.save("_" + path);
  }



  public int translateColor(int c){

    int Cyan    = color(0,89,158);
    int Magenta = color(161,35,99);
    int Yellow  = color (213, 178, 0);
    int Black   = color(30,30,30);
    int White   = color(220,222,216);

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
      if (k > 0.5f){
        return Black;
      } else {
        return White;
      }
    } else if (( rnd > k ) && (rnd < k + cy)){ return Cyan;
    } else if (( rnd > k + cy ) && (rnd < k + cy + ye)){ return Yellow;
    } else if (( rnd > k + cy + ye)){ return Magenta;}

    return White;

  }




public String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}


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

  public void start() {
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
  public void run() {
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

  public void colorize(String path){

  int black = color(0,0,0);

  PImage temp = loadImage(path);
  temp.loadPixels();

   for (int i = 0; i < temp.pixels.length; i++) {
      int c = temp.pixels[i];
      if (c != black){ temp.pixels[i] =  translateColor(c);}

    }
    temp.updatePixels();
    temp.save("colorized" +  path);
  }



  public int translateColor(int c){

    int Cyan    = color(0,89,158);
    int Magenta = color(161,35,99);
    int Yellow  = color (213, 178, 0);
    int Black   = color(30,30,30);
    int White   = color(220,222,216);

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
      if (k > 0.5f){
        return Black;
      } else {
        return White;
      }
    } else if (( rnd > k ) && (rnd < k + cy)){ return Cyan;
    } else if (( rnd > k + cy ) && (rnd < k + cy + ye)){ return Yellow;
    } else if (( rnd > k + cy + ye)){ return Magenta;}

    return White;

  }



public String[] listFileNames(String dir) {
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

  public void settings() {  size(200,200); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#CCCCCC", "Color_Translator" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
