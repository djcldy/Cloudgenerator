// "Texture Generator Code"
// Data-Driven 3D Sampling Project
// Sayjel Vijay Patel (2017), SUTD Digital Manufacturing & Design Centre
// 3DJ Texture Generator for the Stratasys J-1750 Voxel Print Technology
// Create multi-material, procedural micro-structures & textures for 3D printing
// import libraries
// Max file size 2.5 Gb
// 25 CM
// Magik ++


import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import controlP5.*;
import java.util.*;
import java.io.File;

ControlP5 cp5;
ControlP5 cp5View;


// surboard dimension = 170 mm x 170 mm x 9 mm

public Controller c;


// variables for 3D printer
public float layerHeight = .030; // cm
public float voxW = 0.040; // cm
public float voxH = 0.040; // cm

public float DimXY = 20;
// public float DimY = 10;
public float DimZ = 3;
public ArrayList<PVector> vertexColors;
// public ArrayList<color> vertexColors = new ArrayList<color>();

boolean SpinX = true;
boolean SpinY = true;
boolean SpinZ = true;

// stuff for interface
Slider abc;
CheckBox checkbox;

///////////
float Intersection = 0;
/////////////////// these may be obsolete
float Amplitude = 10; // cm
float Thickness = 10;
float Current = 0.5;
////////////////////////////////////////
int LayersY =1;
int LayersX =1;
int LayersZ =1;
///////////////////////////////////////////////
PImage exportDepth;
boolean isSetup = true;
PWindow win;
/////////////////////////////////////////////////
int step = 0;
/////////////////////////////////////////////////
    color Cyan    = color(0,89,158);
    color Magenta = color(161,35,99);
    color Yellow  = color (213, 178, 0);
    color Black   = color(30,30,30);
    color White   = color(220,222,216);
//


void setup() {

  println("setup started");
  float time01 = float(millis());
  init();
  float time02 = float(millis());
  float elapsedTime = time02 - time01;
  println("setup completed after " + elapsedTime/1000 + " s."); //MIN: 0.548s  MAX: 0.637s

}


void draw() {
  background(2, 7, 49);
  c.update();
  step ++;
}


void settings(){
  size(1800,1000,P3D);
  smooth(8);
   // fullScreen(P3D);
}
void setStyle(){
  // background(0, 51, 102);
  ortho();


}

void init(){

  setStyle();
  setConst();
  initControl();
  setInterface(); // these are all of the buttons ect.
  isSetup = false;
  initGeo();
  // win = new PWindow();
}



void initControl(){
  c = new Controller(this); // initialize controller
  c.m.vox.thickness = Thickness;
  c.m.vox.layer = Current;
  c.m.vox.update();
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


void DimXY(float value){
  if (!isSetup){
    println("dimXY");
  DimXY = value;
  thread("RESETUNITCELL");
  }
}


void DimZ(float value){
  if (!isSetup){
  DimZ = value;
  println("dimZ = " + DimZ);
  thread("RESETUNITCELL");
  }
}


void Intersection(float value){
  if (!isSetup){
  // println("set intersection = " + value);
  Intersection = value;
  thread("RESETUNITCELL");
  }
}

void R2() { if (c.v.vp3D.mode == "UNIT"){ c.m.currentThumb.setChildren(1);}}
void L2(){if (c.v.vp3D.mode == "UNIT"){c.m.currentThumb.setChildren(-1);}}


void togglecell() {
  if (!isSetup){
    c.toggleMode();
    thread("RESETPOINTCLOUD");
  }
}

void Export() {

  if (!isSetup){
    // exportDepth = c.m.vox.depth.texture.get();
    c.m.vox.exportStack();
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

void SPIN(){
  c.v.vp3D.toggleSpin();
}

void Invert(){ if (!isSetup){ thread("INVERTTEXTURE");}}


void Shell(){

  if (!isSetup){
    c.m.vox.toggleFillMode();
  }
}

void VIEWMODE(){
  if (!isSetup){c.v.toggleMode();}
}


void INVERTTEXTURE(){ // inverts the color of the texture
 c.v.vp2D.invertTexture(c.currentRow);
 RESETUNITCELL();
}

void initButtons(){

  addButton(cp5View, "VIEWMODE", os, 0, int(2*os), int(os));
  addButton(cp5,"Invert", os,row1+os, int(2*os), int(os));
  addButton(cp5,"LEFT", col3,row5-os, int(os), int(os));
  addButton(cp5,"RIGHT", col3+os,row5-os, int(os), int(os));
  addButton(cp5,"AXO", col3+2*os,row5-os, int(os), int(os));
  addButton(cp5,"Export", col8,row8, int(width/6), int(os));


}

void initToggles(){

  addToggle(cp5,"Shell", col8,row10,int(os), int(os/4));
  addToggle(cp5,"SpinX", col3+os,row5-2*os,int(os), int(os/4));
  addToggle(cp5,"SpinY", col3+2*os,row5-2*os,int(os), int(os/4));
  addToggle(cp5,"SpinZ", col3+4*os,row5-2*os,int(os), int(os/4));
  addToggle(cp5,"ToggleCell", width/2 + os, yA + os/2,int(os), int(os/4));

}

void LEFT(){
  if (!isSetup){
  c.v.vp3D.cellUnit.setLeft();
  c.v.vp3Darray.cellUnit.setLeft();
  }
}


void RIGHT(){
  // c.v.vp3D.cellUnit.setRight();
  // c.v.vp3Darray.cellUnit.setRight();
}


void AXO(){
  // c.v.vp3D.cellUnit.setAxo();
  // c.v.vp3Darray.cellUnit.setAxo();
}


void setInterface(){

  cp5 = new ControlP5(this);
  cp5View = new ControlP5(this);

  initButtons();
  initSlider();
  initToggles();
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
    // // println("adjustGrid");
    // c.m.depthArray = c.m.depth.array(LayersX,LayersY);
    // c.m.alphaArray = c.m.alpha.array(LayersX,LayersY);
    // c.m.materArray = c.m.mater.array(LayersX,LayersY);
    // c.m.updateArray();
}
void initSlider() {

  int len = width/9;


  cp5.addSlider("DimXY")
    .setPosition(xE, row5+ 25)
    .setWidth(len)
    .setValue(95)
    .setRange(1, 200) // mm?
    ;

  cp5.addSlider("DimZ")
    .setPosition(xE, row5+ 75)
    .setWidth(len)
    .setValue(4)
    .setRange(1, 30) // mm
     .setNumberOfTickMarks(250)
     .setSliderMode(Slider.FLEXIBLE)
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
    .setRange(0, 0.5) // mm
    ;


    cp5.addSlider("LayersZ")
     .setPosition(xE,row5 + 175)
     .setWidth(len)
     .setRange(1,6) // values can range from big to small as well
     .setValue(LayersZ)
     .setNumberOfTickMarks(6)
     .setSliderMode(Slider.FLEXIBLE)
     ;

///////////////// SLICER

  cp5.addSlider("Current")
    .setPosition(xE, row5+225)
    .setWidth(len)
    .setRange(0, 1) // values can range from big to small as well
    .setValue(0.5)
    // .setNumberOfTickMarks(255)
    .setSliderMode(Slider.FLEXIBLE)
    ;


}

void Current(float value) {
  if (!isSetup){  //

    Current = value;
    c.m.vox.layer = value;    //l: set vox");
    c.m.vox.update();         //
    c.v.vp3D.setCurrentLayer(c.m.vox.pcLayer);
    // c.v.setCurrent();
  }
}


void Thickness(float value) {
  if (!isSetup){  //
    c.m.vox.thickness = value;
    c.v.thickness = value;
    c.m.vox.update();
  }
}



PVector reprameterize(PImage depth, PImage alpha){

  PVector range = new PVector(255,0);

    for (int x = 0; x     <   depth.width; x += 5) {
      for (int y = 0; y   <   depth.height; y+= 5) {
      float value1 = brightness((alpha.get(x,y)));
      float value2 =  brightness((depth.get(x,y)));
        if (value1 > 0){
          if (value2 > range.y){range.y = value2;}
          else if (value2 < range.x){range.x = value2;}
        }
      }
    }

    return range;
}






  color translate(color c){

    // color col = color(100,100,100);

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





void RESETUNITCELL2() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM
  // REFACTOR THIS CODE...
  // note to self, scaling only important  for the voxel exportd

  float time01 = float(millis());

  ArrayList<PVector> vertexCols = new ArrayList<PVector>();


  int voxXY = int(DimXY/0.080);   //  num voxels in X
  // int voxX  = int(DimXY/0.040);   //  num voxels in X
  int voxY  = int(DimXY/0.040);   //  num voxels in Y
  int voxZ  = int(DimZ/0.027);    //  num voxels in Z each voxel layer is 0.027mm

  // float proportion = float(voxXY)*float(voxXY)/float(voxZ);
  float proportion = sq(DimXY)/DimZ;
  float volume = 5000000; // total number of voxels...


  float xy =  sqrt(sqrt(volume*proportion));
  float zz = volume/sq(xy);
  println("zz = " + zz + ", xy = " + xy);
  // float zz = 100;




  int res = 1;

  PImage depthChannel = c.m.depth.getMap(int(xy)); //
  PImage alphaChannel = c.m.alpha.getMap(int(xy)); //
  PImage materChannel = c.m.mater.getMap(int(xy)); //

    float rangeLo = 255*Intersection;
    float rangeHi = 255 - rangeLo;

    int levels = LayersZ;

    float layerVoxels = int(zz)/levels;
    boolean invert = false;

    PVector range = reprameterize(depthChannel,alphaChannel);  //
    float max = range.y;
    float min = range.x;


    PShape boxCloud = createShape();
    boxCloud.beginShape(POINTS);
    boxCloud.stroke(255,150);
    boxCloud.strokeWeight(2);

    vertexColors = new ArrayList<PVector>();

    for (int z = 0; z < levels; z++){
      for (int x = 0; x < depthChannel.width; x += res) {
      for (int y = 0; y < depthChannel.height; y+= res) {
        float alph = brightness((alphaChannel.get(x,y)));

        if (alph > 0){
           float val = brightness(depthChannel.get(x, y));
           val =  (val-min)*(255/max);
           color c = materChannel.get(x,y);

           // if (random(0,1) > 0.99){ translate(c); }

           if (val > rangeHi )  { val = rangeHi;    }
           if (val < rangeLo) { val = rangeLo;  }
           if (invert){val = 255-val;}

           float voxLevel = (val-rangeLo)/(rangeHi-rangeLo)*layerVoxels; // height of voxel within this level

           // c = translate(c);

           PVector col  = new PVector(red(c),green(c), blue(c));

           vertexColors.add(col);

           // vertexCols.add(col);

           boxCloud.stroke(col.x, col.y, col.z, 150);
           boxCloud.vertex(x-xy/2, y-xy/2, voxLevel + z*layerVoxels - zz/2);

          }
        }
      }

      invert = !invert; // need to do something hear to invert
    }

    boxCloud.endShape();
    c.updateShape(boxCloud);


  float time02 = float(millis());
  float elapsedTime = time02 - time01;
  println("reset unitcell completed after " + elapsedTime/1000 + " s."); //MIN: 0.004s  MAX: 0.219s

}




void RESETUNITCELL() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM
  // REFACTOR THIS CODE...
  // note to self, scaling only important  for the voxel exportd

  float time01 = float(millis());

  ArrayList<PVector> vertexCols = new ArrayList<PVector>();


  int voxXY = int(DimXY/0.080);   //  num voxels in X
  // int voxX  = int(DimXY/0.040);   //  num voxels in X
  int voxY  = int(DimXY/0.040);   //  num voxels in Y
  int voxZ  = int(DimZ/0.027);    //  num voxels in Z each voxel layer is 0.027mm

  // float proportion = float(voxXY)*float(voxXY)/float(voxZ);
  float proportion = sq(DimXY)/DimZ;
  float volume = 5000000; // total number of voxels...


  float xy =  sqrt(sqrt(volume*proportion));
  float zz = volume/sq(xy);
  println("zz = " + zz + ", xy = " + xy);
  // float zz = 100;




  int res = 1;

  PImage depthChannel = c.m.depth.getMap(int(xy)); //
  PImage alphaChannel = c.m.alpha.getMap(int(xy)); //
  PImage materChannel = c.m.mater.getMap(int(xy)); //

    float rangeLo = 255*Intersection;
    float rangeHi = 255 - rangeLo;

    int levels = LayersZ;

    float layerVoxels = int(zz)/levels;
    boolean invert = false;

    PVector range = reprameterize(depthChannel,alphaChannel);  //
    float max = range.y;
    float min = range.x;


    PShape boxCloud = createShape();
    boxCloud.beginShape(POINTS);
    boxCloud.stroke(255,150);
    boxCloud.strokeWeight(2);

    vertexColors = new ArrayList<PVector>();

    for (int z = 0; z < levels; z++){
      for (int x = 0; x < depthChannel.width; x += res) {
      for (int y = 0; y < depthChannel.height; y+= res) {
        float alph = brightness((alphaChannel.get(x,y)));

        if (alph > 0){
           float val = brightness(depthChannel.get(x, y));
           val =  (val-min)*(255/max);
           color c = materChannel.get(x,y);

           // if (random(0,1) > 0.99){ translate(c); }

           if (val > rangeHi )  { val = rangeHi;    }
           if (val < rangeLo) { val = rangeLo;  }
           if (invert){val = 255-val;}

           float voxLevel = (val-rangeLo)/(rangeHi-rangeLo)*layerVoxels; // height of voxel within this level

           // c = translate(c);

           PVector col  = new PVector(red(c),green(c), blue(c));

           vertexColors.add(col);

           // vertexCols.add(col);

           boxCloud.stroke(col.x, col.y, col.z, 150);
           boxCloud.vertex(x-xy/2, y-xy/2, voxLevel + z*layerVoxels - zz/2);

          }
        }
      }

      invert = !invert; // need to do something hear to invert
    }

    boxCloud.endShape();
    c.updateShape(boxCloud);


  float time02 = float(millis());
  float elapsedTime = time02 - time01;
  println("reset unitcell completed after " + elapsedTime/1000 + " s."); //MIN: 0.004s  MAX: 0.219s

}

 PShape copyPS(PShape s, ArrayList<PVector> vertexCols){

    PShape cloud = s;
    cloud = createShape();
    cloud.beginShape(POINTS);
    cloud.strokeWeight(1);
    for(int i = 0; i < s.getVertexCount(); i++){
      float x = s.getVertexX(i);
      float y = s.getVertexY(i);
      float z = s.getVertexZ(i);
      PVector col = vertexCols.get(i);
      cloud.stroke(col.x, col.y, col.z,150);
      cloud.vertex(x,y,z);
    }
    cloud.endShape();
    return cloud;
 }





void RESETUNITCELLARCHIVE() {

  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM
  // REFACTOR THIS CODE...
  // note to self, scaling only important  for the voxel exportd

  float time01 = float(millis());

  ArrayList<PVector> vertexCols = new ArrayList<PVector>();

  int voxX = int(DimXY/0.040); //  num voxels in X
  int voxY = int(DimXY/0.040); //  num voxels in Y
  int voxZ = int(DimZ/0.027); //  num voxels in Z each voxel layer is 0.027mm

  float maxVoxels = 10000;
  float test = LayersZ*(float(voxX)*float(voxY))/maxVoxels;

  int res =  int(test);

  if (test<1){res=1;}
  // int res = 1;

  // int res = 1;
  float sumVoxels = float(voxX)*float(voxY)*2*(1/float(res));

   // depth channel and reset it
  PImage depthChannel = c.m.depth.getMap(voxX); //
  PImage alphaChannel = c.m.alpha.getMap(voxX); //
  PImage materChannel = c.m.mater.getMap(voxX); //

    float rangeLo = 255*Intersection;
    float rangeHi = 255 - rangeLo;

    int levels = LayersZ;

    float layerVoxels = voxZ/levels;
    boolean invert = false;

    PVector range = reprameterize(depthChannel,alphaChannel);  //
    float max = range.y;
    float min = range.x;


    PShape boxCloud = createShape();
    boxCloud.beginShape(POINTS);
    boxCloud.stroke(255,150);
    boxCloud.strokeWeight(1);

    vertexColors = new ArrayList<PVector>();

    for (int z = 0; z < levels; z++){
      for (int x = 0; x < depthChannel.width; x += res) {
      for (int y = 0; y < depthChannel.height; y+= res) {
        float alph = brightness((alphaChannel.get(x,y)));

        if (alph > 0){
           float val = brightness(depthChannel.get(x, y));
           val =  (val-min)*(255/max);
           color c = materChannel.get(x,y);

           // if (random(0,1) > 0.99){ translate(c); }

           if (val > rangeHi )  { val = rangeHi;    }
           if (val < rangeLo) { val = rangeLo;  }
           if (invert){val = 255-val;}

           float voxLevel = (val-rangeLo)/(rangeHi-rangeLo)*layerVoxels; // height of voxel within this level

           c = translate(c);

           PVector col  = new PVector(red(c),green(c), blue(c));

           vertexColors.add(col);

           // vertexCols.add(col);

           boxCloud.stroke(col.x, col.y, col.z, 150);
           boxCloud.vertex(x-voxX/2, y-voxY/2, voxLevel + z*layerVoxels - voxZ/2);

          }
        }
      }

      invert = !invert; // need to do something hear to invert
    }

    boxCloud.endShape();
    c.updateShape(boxCloud);


  float time02 = float(millis());
  float elapsedTime = time02 - time01;
  println("reset unitcell completed after " + elapsedTime/1000 + " s."); //MIN: 0.004s  MAX: 0.219s

}
