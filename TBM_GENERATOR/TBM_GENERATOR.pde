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

public Controller c;


// variables for 3D printer
public float layerHeight = .030; // cm
public float voxW = 0.040; // cm
public float voxH = 0.040; // cm



// stuff for interface
Slider abc;
CheckBox checkbox;


float Amplitude = 10; // cm
float Thickness = 10;
float Current = 0.5;


int LayersY =1;
int LayersX =1;
int LayersZ =1;


//
PImage exportDepth;

// PUBLIC VARIABLES FOR LAYOUT
boolean isSetup = true;



void setup() {

  println("setup started");
  float time01 = float(millis());


   init();

  float time02 = float(millis());
  float elapsedTime = time02 - time01;
  println("setup completed after " + elapsedTime/1000 + " s.");

}


void draw() {
  background(2, 7, 49);
  c.update(
    );
}


void settings(){
  // size(1600,900,P3D);

   fullScreen(P3D);
}
void setStyle(){
  smooth();
  // background(0, 51, 102);
  // ortho();


}

void init(){

  setStyle();
  setConst();
  initControl();
  setInterface(); // these are all of the buttons ect.
  initViewport();
  isSetup = false;
  initGeo();
}



void initControl(){
  c = new Controller(this); // initialize controller
  c.m.vox.thickness = Thickness;
  c.m.vox.layer = Current;
  c.m.vox.update();
}

void initViewport(){
}


void initGeo(){
  // thread("RESETUNITCELL"); //
  // thread("RESETARRAY"); //
}

void LayersZ(int value){
  if (!isSetup){
  LayersZ = value;
  thread("RESETUNITCELL");
  }
}

void R2() {
 if (c.v.vp3D.mode == "UNIT"){ c.m.currentThumb.setChildren(1);}
}

void L2(){
if (c.v.vp3D.mode == "UNIT"){c.m.currentThumb.setChildren(-1);}
}


void togglecell() {
  if (!isSetup){
    c.toggleMode();
    thread("RESETPOINTCLOUD");
  }
}


void checkBox(float[] a) {
  println(a); // these are the toggles
  c.m.points = c.m.resetPC(3, a); // we will have to multi thread this bitch
}


void Export() {

  if (!isSetup){
    exportDepth = c.m.vox.depth.texture.get();
    export();
  }
}

void export() {

  int layers = 200;
  PVector dim = new PVector(500, 500);
  PImage img = exportDepth;
  img.resize(int(dim.x), int(dim.y));


  for (int j = 0; j < 255; j++) {

    // println("export layer: " + l);

    PGraphics pg = createGraphics(int(dim.x), int(dim.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {    //   color bright = img.pixels[k];
      float val = brightness(img.pixels[k]);
      if ((val < (j+5)) && (val > (j-5))) {
        pg.pixels[k] = color(255);
      }
    }
    pg.updatePixels();
    pg.endDraw();
    pg.save("exports/layer" + nf(j, 3) + ".png");
  }
}

void mousePressed() {
  // Mouse Event
  PVector mouseEvent = new PVector(mouseX, mouseY);
  c.mouseDown(mouseEvent); // mouse pressed

  if (c.zoneC.isSelected(mouseEvent)){
  thread("RESETPOINTCLOUD");
  }

}

void event(ControlEvent theEvent) {
  println(theEvent.getArrayValue());
}



void initButtons(){

  int bW = int((width/4 - width/32)/4);
  int bH = int(height/16 - os);

  cp5.addToggle("togglecell")
     .setPosition(xD,yA)
     .setSize(int(width/6-os),75)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;

  cp5.addButton("Export")
    .setValue(0)
    .setPosition(xG, height-height/16)
    .setSize(int(width/6-os), int(height/16-os))
    ;

  // cp5.addButton("ADD")
  //   .setValue(0)
  //   .setPosition(os, height-height/16)
  //   .setSize(bW, bH)
  //   ;

  // cp5.addButton("SUBTRACT")
  //   .setValue(0)
  //   .setPosition(os+bW, height-height/16)
  //   .setSize(bW, bH)
  //   ;

  // cp5.addButton("MULTIPLY")
  //   .setValue(0)
  //   .setPosition(os+2*bW, height-height/16)
  //   .setSize(bW, bH)
  //   ;

  // cp5.addButton("DIVIDE")
  //   .setValue(0)
  //   .setPosition(os+3*bW, height-height/16)
  //   .setSize(bW, bH)
  //   ;



  int y4 = int(height/16+width/64+ width/4 - width/32);

  cp5.addButton("R1")
    .setValue(0)
    .setPosition(width/4-os, y4)
    .setSize(int(os), int(tWidth))
    ;

  // cp5.addButton("R2")
  //   .setValue(0)
  //   .setPosition(width/4-os, y4 + tWidth + os)
  //   .setSize(int(os), int(tWidth))
  //   ;

  // cp5.addButton("R3")
  //   .setValue(0)
  //   .setPosition(width/4-os, y4 + 2*(tWidth + os))
  //   .setSize(int(os), int(tWidth))
  //   ;

  cp5.addButton("L1")
    .setValue(0)
    .setPosition(0, y4)
    .setSize(int(os), int(tWidth))
    ;

  // cp5.addButton("L2")
  //   .setValue(0)
  //   .setPosition(0, y4 + tWidth + os)
  //   .setSize(int(os), int(tWidth))
  //   ;

  // cp5.addButton("L3")
  //   .setValue(0)
  //   .setPosition(0, y4 + 2*(tWidth + os))
  //   .setSize(int(os), int(tWidth))
  //   ;

}


void setInterface(){

  cp5 = new ControlP5(this);

  initButtons();
  initSlider();

}

void LayersX(int value){
    LayersX = value;
    if (!isSetup && (c.v.vp3D.mode != "UNIT")){thread("adjustGrid");}
}

void LayersY(int value){

    LayersY = value;
    if (!isSetup && (c.v.vp3D.mode != "UNIT")){thread("adjustGrid");}

}


void adjustGrid(){
    // println("adjustGrid");
    c.m.depthArray = c.m.depth.array(LayersX,LayersY);
    c.m.alphaArray = c.m.alpha.array(LayersX,LayersY);
    c.m.materArray = c.m.mater.array(LayersX,LayersY);
    c.m.updateArray();
}
void initSlider() {

  int len = width/9;

  cp5.addSlider("DimX")
    .setPosition(xE, row5+ 25)
    .setWidth(len)
    .setValue(10)
    .setRange(1, 50) // mm
    ;

  cp5.addSlider("DimY")
    .setPosition(xE, row5+ 50)
    .setWidth(len)
    .setValue(10)
    .setRange(1, 50) // mm
    ;

  cp5.addSlider("DimZ")
    .setPosition(xE, row5+ 75)
    .setWidth(len)
    .setValue(10)
    .setRange(1, 50) // mm
    ;


  cp5.addSlider("Thickness")
    .setPosition(xE, row5+125)
    .setWidth(len)
    .setRange(1, 100)
    .setValue(Thickness)
    ;



  cp5.addSlider("Intersection")
    .setPosition(xE, row5+ 150)
    .setWidth(len)
    .setValue(0)
    .setRange(0, 1) // mm
    ;


    cp5.addSlider("LayersZ")
     .setPosition(xE,row5 + 175)
     .setWidth(len)
     .setRange(1,10) // values can range from big to small as well
     .setValue(LayersZ)
     .setNumberOfTickMarks(10)
     .setSliderMode(Slider.FLEXIBLE)
     ;

///////////////// SLICER

  cp5.addSlider("Current")
    .setPosition(xG, row5+50)
    .setWidth(len)
    .setRange(0, 1) // values can range from big to small as well
    .setValue(0.5)
    // .setNumberOfTickMarks(255)
    .setSliderMode(Slider.FLEXIBLE)
    ;


}

void Current(float value) {
  if (!isSetup){  //

    c.m.vox.layer = value;    //l: set vox");
    c.m.vox.update();         //
    c.v.vp3D.setCurrentLayer(c.m.vox.pcLayer);
    // c.v.setCurrent();
  }
}


void Thickness(float value) {

  c.m.vox.thickness = value;
  c.v.thickness = value;
  c.m.vox.update();
}


void dropdown(int n) {

  CColor c = new CColor();
  c.setBackground(color(255, 0, 0));
  cp5.get(ScrollableList.class, "dropdown").getItem(n).put("color", c);
}



void RESETUNITCELL() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM

  int voxX = int(10/0.040); //  num voxels in X
  int voxY = int(10/0.040); //  num voxels in Y
  int voxZ = int(5/0.030); //  num voxels in Z

  println("reset unit cell" + voxX + "," + voxY + "," + voxZ);
  //////////////////////////////////////////////


    PImage depthChannel = c.m.depth.texture.get();
    PImage alphaChannel = c.m.alpha.texture.get();
    PImage materChannel = c.m.mater.texture.get();

    depthChannel.resize(voxX,voxY);
    alphaChannel.resize(voxX,voxY);
    materChannel.resize(voxX,voxY);

  PShape boxCloud = createShape();
  boxCloud.beginShape(POINTS);
  boxCloud.stroke(255,150);

    ArrayList<PVector> temp = new ArrayList<PVector>();
    Thumb depth,alpha, mater; // place holder variables

    depth = c.m.depth;
    alpha = c.m.alpha;
    mater = c.m.mater;


    depth = c.m.depth;
    alpha = c.m.alpha;
    mater = c.m.mater;

    depth.texture.loadPixels();
    alpha.texture.loadPixels();
    mater.texture.loadPixels();

    depthChannel.loadPixels();
    alphaChannel.loadPixels();
    materChannel.loadPixels();


    int res = 1;
    float range = 255;
    int levels = LayersZ;
    float amp = Amplitude/levels; // this is the total height
    float layerVoxels = voxZ/levels;
    boolean invert = false;


  for (int z = 0; z < levels; z++){
    for (int x = 0; x < depthChannel.width; x += res) {
      for (int y = 0; y < depthChannel.height; y+= res) {
        float alp = brightness((alphaChannel.get(x,y)));

           float val = brightness(depthChannel.get(x, y));
           if (invert){val = 255-val;}
           float voxLevel = val/255*layerVoxels; // height of voxel within this level
           boxCloud.vertex(x-voxX/2, y-voxY/2, voxLevel + z*layerVoxels - voxZ/2);
        }
      }

      invert = !invert; // need to do something hear to invert
    }




/*
  for (int z = 0; z < levels; z++){
    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {
        float alp = brightness((alpha.map.get(x,y)));
        // if (alp > 10) { // check alpha ??
           float val = brightness(depth.map.get(x, y));
           if (invert){val = 255-val;}
            // temp.add(new PVector(x, y, val + z*255));
            boxCloud.vertex(x, y, (amp*z + val/255*amp)/voxH);

          // }
        }
      }
      invert = !invert; // need to do something hear to invert
    }
*/


  boxCloud.endShape();
  c.v.vp3D.setCellUnit(boxCloud);

}

void RESETARRAY() {


  PShape boxCloud = createShape();
  boxCloud.beginShape(POINTS);
  boxCloud.stroke(255);

    ArrayList<PVector> temp = new ArrayList<PVector>();
    Thumb depth,alpha, mater; // place holder variables

    depth = c.m.depth;
    alpha = c.m.alpha;
    mater = c.m.mater;


  depth = c.m.depthArray;
    alpha = c.m.alphaArray;
    mater = c.m.materArray;

    depth.map.loadPixels();
    alpha.map.loadPixels();
    mater.map.loadPixels();

    // println("depth map size = " + depth.map.width + "," + depth.map.height);

    int res = 1;
    float range = 255;


    int levels = LayersZ;
    float amp = Amplitude/levels; // this is the total height
    boolean invert = false;


  for (int z = 0; z < levels; z++){
    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {
        float alp = brightness((alpha.map.get(x,y)));

        // if (alp > 10) { // check alpha ??

           float val = brightness(depth.map.get(x, y));
           if (invert){val = 255-val;}
            // temp.add(new PVector(x, y, val + z*255));
            boxCloud.vertex(x, y, amp*z + val/255*amp);

          // }
        }
      }

      invert = !invert; // need to do something hear to invert
    }

    boxCloud.endShape();
    // c.m.points = temp;
    c.v.vp3D.setCellArray(boxCloud);

}





void RESETCELL(){


}




void RESETARRAYOLD() {


}

void RESETPOINTCLOUD() {

    //pritnln("reset pointCloud: " + c.v.vp3D.mode);

    // ArrayList<PVector> temp = new ArrayList<PVector>();
    // Thumb depth,alpha, mater; // place holder variables

    // int stepZ;

    // if (c.v.vp3D.mode == "UNIT") {

    //   depth = c.m.depth;
    //   alpha = c.m.alpha;
    //   mater = c.m.mater;
    //   stepZ = 1;

    // } else {

      //println("generate micro-structures");

    //   depth = c.m.depthArray;
    //   alpha = c.m.alpha;
    //   mater = c.m.mater;
    //   stepZ = 2;

    // }

    // depth.map.loadPixels();
    // alpha.map.loadPixels();
    // mater.map.loadPixels();

    // int res = 1;
    // float range = 255;

    // //

    // for (int x = 0; x < depth.map.width; x += res) {
    //   for (int y = 0; y < depth.map.height; y+= res) {

    //     float alp = brightness((alpha.map.get(x,y)));

    //     if (alp > 10) { // check alpha

    //        float val = brightness(depth.map.get(x, y));
    //        temp.add(new PVector(x, y, val));

    //       }
    //     }
    //   }
    // c.m.points = temp;

    // if (c.v.vp3D.mode == "UNIT"){
    //   c.v.vp3D.setCellUnit(c.m.points);
    //   } else {
    //   c.v.vp3D.setCellArray(c.m.points);
    // }
}
