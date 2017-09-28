// "Texture Generator Code"
// Data-Driven 3D Sampling Project
// Sayjel Vijay Patel (2017), SUTD Digital Manufacturing & Design Centre
// 3DJ Texture Generator for the Stratasys J-1750 Voxel Print Technology
// Create multi-material, procedural micro-structures & textures for 3D printing


// import libraries

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import controlP5.*;
import java.util.*;
import java.io.File;

ControlP5 cp5;

// stuff for interface
Slider abc;
CheckBox checkbox;
int myColor = color(0, 0, 0);
float Amplitude = 0.1;
float Thickness = 10;
int  Current = 10;
int sliderTicks1 = 100;
int sliderTicks2 = 30;

//
PImage exportDepth;

Controller c;

float th = 10;
float ll = 50;

// boolean enableOutput = false;


void setup() {
  println("starting");
  smooth();
  background(0, 51, 102);
  size(300, 300, P3D);
  fullScreen(P3D);

  c = new Controller();
  c.vox.thickness = th;
  c.vox.layer = ll;
  c.vox.update();
  // initSlider();
  // enableOutput=true;
  // initFiles();

}


// void draw() {
//   background(2, 7, 49);
//    c.update();
// }

void checkBox(float[] a) {
  println(a); // these are the toggles
  c.m.points = c.m.resetPC(3,a); // we will have to multi thread this bitch
}

void initFiles(){


  // Using just the path of this sketch to demonstrate,
  // but you can list any directory you like.
  String path = sketchPath();
  println("Listing all filenames in a directory: ");
  String[] filenames = listFileNames(path + "/images/alpha/");
  printArray(filenames);
}

// This function returns all the files in a directory as an array of Strings
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

void Export(){
  // if (enableOutput){
  // th = c.vox.thickness;
  // ll = c.vox.layer;
  // exportDepth = c.vox.depth.texture.get();
  // thread("export");
  // }
}

void export(){

    int layers = 200;
    PVector dim = new PVector(2000,2000);
    PImage img = exportDepth;
    img.resize(int(dim.x),int(dim.y));


    for (int l = 0; l < 200; l++){

      println("export layer: " + l);

    PGraphics pg = createGraphics(int(dim.x),int(dim.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {    //   color bright = img.pixels[k];
      float val = brightness(img.pixels[k]);
    if ((val < (ll+th)) && (val > (ll-th))) {
        pg.pixels[k] = color(255);
      }
    }
      pg.updatePixels();
      pg.endDraw();
      pg.save("exports/layer" + nf(l, 3) + ".png");
    }

}

void mousePressed() {
  // Mouse Event
  PVector mouseEvent = new PVector(mouseX, mouseY);
  c.mouseDown(mouseEvent); // mouse pressed
}

void event(ControlEvent theEvent) {
  println(theEvent.getArrayValue());
}


void initSlider() {
  float x1 = (width*5)/6;
  float y1 = height/16;
  float o = 25;

  cp5 = new ControlP5(this);


  checkbox = cp5.addCheckBox("checkBox")
                .setPosition(width*5/6+o, height/4)
                .setSize(40, 40)
                .setItemsPerRow(3)
                .setSpacingColumn(40)
                .setSpacingRow(20)
                .addItem("xy", 0)
                .addItem("yz", 0)
                .addItem("xz", 0)
                ;

  cp5.addButton("Export")
     .setValue(0)
     .setPosition(width*5/6,height-height/16)
     .setSize(width/6,height/16)
     ;

  // add a horizontal sliders, the value of this slider will be linked
  // to variable 'sliderValue'
  cp5.addSlider("Amplitude")
    .setPosition(o+x1, y1+ 50)
    .setWidth(200)
    .setRange(0.025, 0.5)
    ;

  cp5.addSlider("Thickness")
    .setPosition(o+x1, y1+75)
    .setWidth(200)
    .setRange(1, 100)
    .setValue(th)
    ;

  // cp5.addSlider("Layers")
  //   .setPosition(o+x1, y1+100)
  //   .setWidth(200)
  //   .setRange(1, 20) // values can range from big to small as well
  //   .setValue(4)
  //   .setNumberOfTickMarks(10)
  //   .setSliderMode(Slider.FLEXIBLE)
  //   ;

  cp5.addSlider("Current")
    .setPosition(o+x1, y1+100)
    .setWidth(200)
    .setRange(0, 255) // values can range from big to small as well
    .setValue(ll)
    // .setNumberOfTickMarks(255)
    .setSliderMode(Slider.FLEXIBLE)
    ;



}

void Current(float value){

  c.vox.layer = value;
  c.vox.update();
  c.v.setCurrent(value);

}


void Thickness(float value){

  c.vox.thickness = value;
  c.vox.update();

}

//void setup() {


//}

//void draw() {
//  background(sliderTicks1);

//  fill(sliderValue);
//  rect(0,0,width,100);

//  fill(myColor);
//  rect(0,280,width,70);

//  fill(sliderTicks2);
//  rect(0,350,width,50);
//}

//void slider(float theColor) {
//  myColor = color(theColor);
//  println("a slider event. setting background to "+theColor);
//}

//
//void setup() {
//  size(400, 400);
//  cp5 = new ControlP5(this);
//  List l = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
//  /* add a ScrollableList, by default it behaves like a DropdownList */
//  cp5.addScrollableList("dropdown")
//     .setPosition(100, 100)
//     .setSize(200, 100)
//     .setBarHeight(20)
//     .setItemHeight(20)
//     .addItems(l)
//     // .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
//     ;
//
//
//}
//
//void draw() {
//  background(240);
//}

void dropdown(int n) {
  /* request the selected item based on index n */
  println(n, cp5.get(ScrollableList.class, "dropdown").getItem(n));

  /* here an item is stored as a Map  with the following key-value pairs:
   * name, the given name of the item
   * text, the given text of the item by default the same as name
   * value, the given value of the item, can be changed by using .getItem(n).put("value", "abc"); a value here is of type Object therefore can be anything
   * color, the given color of the item, how to change, see below
   * view, a customizable view, is of type CDrawable
   */

  CColor c = new CColor();
  c.setBackground(color(255, 0, 0));
  cp5.get(ScrollableList.class, "dropdown").getItem(n).put("color", c);
}

//void keyPressed() {
//  switch(key) {
//    case('1'):
//    /* make the ScrollableList behave like a ListBox */
//    cp5.get(ScrollableList.class, "dropdown").setType(ControlP5.LIST);
//    break;
//    case('2'):
//    /* make the ScrollableList behave like a DropdownList */
//    cp5.get(ScrollableList.class, "dropdown").setType(ControlP5.DROPDOWN);
//    break;
//    case('3'):
//    /*change content of the ScrollableList */
//    List l = Arrays.asList("a-1", "b-1", "c-1", "d-1", "e-1", "f-1", "g-1", "h-1", "i-1", "j-1", "k-1");
//    cp5.get(ScrollableList.class, "dropdown").setItems(l);
//    break;
//    case('4'):
//    /* remove an item from the ScrollableList */
//    cp5.get(ScrollableList.class, "dropdown").removeItem("k-1");
//    break;
//    case('5'):
//    /* clear the ScrollableList */
//    cp5.get(ScrollableList.class, "dropdown").clear();
//    break;
//  }
// //}
