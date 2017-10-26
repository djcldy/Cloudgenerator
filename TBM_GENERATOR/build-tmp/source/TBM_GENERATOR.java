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

public class TBM_GENERATOR extends PApplet {

// "Texture Generator Code"
// Data-Driven 3D Sampling Project
// Sayjel Vijay Patel (2017), SUTD Digital Manufacturing & Design Centre
// 3DJ Texture Generator for the Stratasys J-1750 Voxel Print Technology
// Create multi-material, procedural micro-structures & textures for 3D printing
// import libraries
// Max file size 2.5 Gb
// 25 CM
// Magik ++









ControlP5 cp5;
ControlP5 cp5View;


// surboard dimension = 170 mm x 170 mm x 9 mm

public Controller c;


// variables for 3D printer
public float layerHeight = .030f; // cm
public float voxW = 0.040f; // cm
public float voxH = 0.040f; // cm

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
float Current = 0.5f;
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
    int Cyan    = color(0,89,158);
    int Magenta = color(161,35,99);
    int Yellow  = color (213, 178, 0);
    int Black   = color(30,30,30);
    int White   = color(220,222,216);
//


public void setup() {

  println("setup started");
  float time01 = PApplet.parseFloat(millis());
  init();
  float time02 = PApplet.parseFloat(millis());
  float elapsedTime = time02 - time01;
  println("setup completed after " + elapsedTime/1000 + " s."); //MIN: 0.548s  MAX: 0.637s

}


public void draw() {
  background(2, 7, 49);
  c.update();
  step ++;
}


public void settings(){
  size(1800,1000,P3D);
  smooth(8);
   // fullScreen(P3D);
}
public void setStyle(){
  // background(0, 51, 102);
  ortho();


}

public void init(){

  setStyle();
  setConst();
  initControl();
  setInterface(); // these are all of the buttons ect.
  isSetup = false;
  initGeo();
  // win = new PWindow();
}



public void initControl(){
  c = new Controller(this); // initialize controller
  c.m.vox.thickness = Thickness;
  c.m.vox.layer = Current;
  c.m.vox.update();
}

public void initGeo(){
  // thread("RESETUNITCELL"); //
  // thread("RESETARRAY"); //
}

public void LayersZ(int value){
  if (!isSetup){
  LayersZ = value;
  thread("RESETUNITCELL");
  }
}


public void DimXY(float value){
  if (!isSetup){
    println("dimXY");
  DimXY = value;
  thread("RESETUNITCELL");
  }
}


public void DimZ(float value){
  if (!isSetup){
  DimZ = value;
  println("dimZ = " + DimZ);
  thread("RESETUNITCELL");
  }
}


public void Intersection(float value){
  if (!isSetup){
  // println("set intersection = " + value);
  Intersection = value;
  thread("RESETUNITCELL");
  }
}

public void R2() { if (c.v.vp3D.mode == "UNIT"){ c.m.currentThumb.setChildren(1);}}
public void L2(){if (c.v.vp3D.mode == "UNIT"){c.m.currentThumb.setChildren(-1);}}


public void togglecell() {
  if (!isSetup){
    c.toggleMode();
    thread("RESETPOINTCLOUD");
  }
}

public void Export() {

  if (!isSetup){
    // exportDepth = c.m.vox.depth.texture.get();
    c.m.vox.exportStack();
  }
}

public void export() {

  int layers = 200;
  PVector dim = new PVector(500, 500);
  PImage img = exportDepth;
  img.resize(PApplet.parseInt(dim.x), PApplet.parseInt(dim.y));


  for (int j = 0; j < 255; j++) {

    // println("export layer: " + l);

    PGraphics pg = createGraphics(PApplet.parseInt(dim.x), PApplet.parseInt(dim.y));
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

public void mousePressed() {
  // Mouse Event
  PVector mouseEvent = new PVector(mouseX, mouseY);
  c.mouseDown(mouseEvent); // mouse pressed

  if (c.zoneC.isSelected(mouseEvent)){
  thread("RESETPOINTCLOUD");
  }

}

public void event(ControlEvent theEvent) {
  println(theEvent.getArrayValue());
}

public void SPIN(){
  c.v.vp3D.toggleSpin();
}

public void Invert(){ if (!isSetup){ thread("INVERTTEXTURE");}}


public void Shell(){

  if (!isSetup){
    c.m.vox.toggleFillMode();
  }
}

public void VIEWMODE(){
  if (!isSetup){c.v.toggleMode();}
}


public void INVERTTEXTURE(){ // inverts the color of the texture
 c.v.vp2D.invertTexture(c.currentRow);
 RESETUNITCELL();
}

public void initButtons(){

  addButton(cp5View, "VIEWMODE", os, 0, PApplet.parseInt(2*os), PApplet.parseInt(os));
  addButton(cp5,"Invert", os,row1+os, PApplet.parseInt(2*os), PApplet.parseInt(os));
  addButton(cp5,"LEFT", col3,row5-os, PApplet.parseInt(os), PApplet.parseInt(os));
  addButton(cp5,"RIGHT", col3+os,row5-os, PApplet.parseInt(os), PApplet.parseInt(os));
  addButton(cp5,"AXO", col3+2*os,row5-os, PApplet.parseInt(os), PApplet.parseInt(os));
  addButton(cp5,"Export", col8,row8, PApplet.parseInt(width/6), PApplet.parseInt(os));


}

public void initToggles(){

  addToggle(cp5,"Shell", col8,row10,PApplet.parseInt(os), PApplet.parseInt(os/4));
  addToggle(cp5,"SpinX", col3+os,row5-2*os,PApplet.parseInt(os), PApplet.parseInt(os/4));
  addToggle(cp5,"SpinY", col3+2*os,row5-2*os,PApplet.parseInt(os), PApplet.parseInt(os/4));
  addToggle(cp5,"SpinZ", col3+4*os,row5-2*os,PApplet.parseInt(os), PApplet.parseInt(os/4));
  addToggle(cp5,"ToggleCell", width/2 + os, yA + os/2,PApplet.parseInt(os), PApplet.parseInt(os/4));

}

public void LEFT(){
  if (!isSetup){
  c.v.vp3D.cellUnit.setLeft();
  c.v.vp3Darray.cellUnit.setLeft();
  }
}


public void RIGHT(){
  // c.v.vp3D.cellUnit.setRight();
  // c.v.vp3Darray.cellUnit.setRight();
}


public void AXO(){
  // c.v.vp3D.cellUnit.setAxo();
  // c.v.vp3Darray.cellUnit.setAxo();
}


public void setInterface(){

  cp5 = new ControlP5(this);
  cp5View = new ControlP5(this);

  initButtons();
  initSlider();
  initToggles();
}

public void LayersX(int value){
    LayersX = value;
    if (!isSetup && (c.v.vp3D.mode != "UNIT")){thread("adjustGrid");}
}

public void LayersY(int value){

    LayersY = value;
    if (!isSetup && (c.v.vp3D.mode != "UNIT")){thread("adjustGrid");}

}


public void adjustGrid(){
    // // println("adjustGrid");
    // c.m.depthArray = c.m.depth.array(LayersX,LayersY);
    // c.m.alphaArray = c.m.alpha.array(LayersX,LayersY);
    // c.m.materArray = c.m.mater.array(LayersX,LayersY);
    // c.m.updateArray();
}
public void initSlider() {

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
    .setRange(0, 0.5f) // mm
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
    .setValue(0.5f)
    // .setNumberOfTickMarks(255)
    .setSliderMode(Slider.FLEXIBLE)
    ;


}

public void Current(float value) {
  if (!isSetup){  //

    Current = value;
    c.m.vox.layer = value;    //l: set vox");
    c.m.vox.update();         //
    c.v.vp3D.setCurrentLayer(c.m.vox.pcLayer);
    // c.v.setCurrent();
  }
}


public void Thickness(float value) {
  if (!isSetup){  //
    c.m.vox.thickness = value;
    c.v.thickness = value;
    c.m.vox.update();
  }
}



public PVector reprameterize(PImage depth, PImage alpha){

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






  public int translate(int c){

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





public void RESETUNITCELL2() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM
  // REFACTOR THIS CODE...
  // note to self, scaling only important  for the voxel exportd

  float time01 = PApplet.parseFloat(millis());

  ArrayList<PVector> vertexCols = new ArrayList<PVector>();


  int voxXY = PApplet.parseInt(DimXY/0.040f);   //  num voxels in X
  int voxX  = PApplet.parseInt(DimXY/0.040f);   //  num voxels in X
  int voxY  = PApplet.parseInt(DimXY/0.040f);   //  num voxels in Y
  int voxZ  = PApplet.parseInt(DimZ/0.027f);    //  num voxels in Z each voxel layer is 0.027mm

  // float proportion = float(voxXY)*float(voxXY)/float(voxZ);
  float proportion = sq(DimXY)/DimZ;
  float volume = 5000000; // total number of voxels...


  float xy =  sqrt(sqrt(volume*proportion));
  float zz = volume/sq(xy);
  println("zz = " + zz + ", xy = " + xy);
  // float zz = 100;




  int res = 1;

  PImage depthChannel = c.m.depth.getMap(PApplet.parseInt(xy)); //
  PImage alphaChannel = c.m.alpha.getMap(PApplet.parseInt(xy)); //
  PImage materChannel = c.m.mater.getMap(PApplet.parseInt(xy)); //

    float rangeLo = 255*Intersection;
    float rangeHi = 255 - rangeLo;

    int levels = LayersZ;

    float layerVoxels = PApplet.parseInt(zz)/levels;
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
           int c = materChannel.get(x,y);

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


  float time02 = PApplet.parseFloat(millis());
  float elapsedTime = time02 - time01;
  println("reset unitcell completed after " + elapsedTime/1000 + " s."); //MIN: 0.004s  MAX: 0.219s

}




public void RESETUNITCELL() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM
  // REFACTOR THIS CODE...
  // note to self, scaling only important  for the voxel exportd

  float time01 = PApplet.parseFloat(millis());

  ArrayList<PVector> vertexCols = new ArrayList<PVector>();


  int voxXY = PApplet.parseInt(DimXY/0.040f);   //  num voxels in X
  int voxX  = PApplet.parseInt(DimXY/0.040f);   //  num voxels in X
  int voxY  = PApplet.parseInt(DimXY/0.040f);   //  num voxels in Y
  int voxZ  = PApplet.parseInt(DimZ/0.027f);    //  num voxels in Z each voxel layer is 0.027mm

  // float proportion = float(voxXY)*float(voxXY)/float(voxZ);
  float proportion = sq(DimXY)/DimZ;
  float volume = 5000000; // total number of voxels...


  float xy =  sqrt(sqrt(volume*proportion));
  float zz = volume/sq(xy);
  println("zz = " + zz + ", xy = " + xy);
  // float zz = 100;




  int res = 1;

  PImage depthChannel = c.m.depth.getMap(PApplet.parseInt(xy)); //
  PImage alphaChannel = c.m.alpha.getMap(PApplet.parseInt(xy)); //
  PImage materChannel = c.m.mater.getMap(PApplet.parseInt(xy)); //

    float rangeLo = 255*Intersection;
    float rangeHi = 255 - rangeLo;

    int levels = LayersZ;

    float layerVoxels = PApplet.parseInt(zz)/levels;
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
           int c = materChannel.get(x,y);

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


  float time02 = PApplet.parseFloat(millis());
  float elapsedTime = time02 - time01;
  println("reset unitcell completed after " + elapsedTime/1000 + " s."); //MIN: 0.004s  MAX: 0.219s

}

 public PShape copyPS(PShape s, ArrayList<PVector> vertexCols){

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





public void RESETUNITCELLARCHIVE() {

  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM
  // REFACTOR THIS CODE...
  // note to self, scaling only important  for the voxel exportd

  float time01 = PApplet.parseFloat(millis());

  ArrayList<PVector> vertexCols = new ArrayList<PVector>();

  int voxX = PApplet.parseInt(DimXY/0.040f); //  num voxels in X
  int voxY = PApplet.parseInt(DimXY/0.040f); //  num voxels in Y
  int voxZ = PApplet.parseInt(DimZ/0.027f); //  num voxels in Z each voxel layer is 0.027mm

  float maxVoxels = 10000;
  float test = LayersZ*(PApplet.parseFloat(voxX)*PApplet.parseFloat(voxY))/maxVoxels;

  int res =  PApplet.parseInt(test);

  if (test<1){res=1;}
  // int res = 1;

  // int res = 1;
  float sumVoxels = PApplet.parseFloat(voxX)*PApplet.parseFloat(voxY)*2*(1/PApplet.parseFloat(res));

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
           int c = materChannel.get(x,y);

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


  float time02 = PApplet.parseFloat(millis());
  float elapsedTime = time02 - time01;
  println("reset unitcell completed after " + elapsedTime/1000 + " s."); //MIN: 0.004s  MAX: 0.219s

}
class BoundingBox{

  // bounding box

  boolean grow =  false;
  boolean display = true;
  PVector pMin, pMax, p1,p2;
  float boxWidth, boxHeight, boxDepth;


  BoundingBox(float xMin,  float yMin, float zMin, float xMax, float yMax, float zMax, PVector _p1, PVector _p2){

    pMin = new PVector(xMin, yMin, zMin);
    pMax = new PVector(xMax, yMax, zMax);
    p1 =  new PVector( _p1.x+50 , _p1.y+50);
    p2 =  new PVector( _p2.x-50 , _p2.y-50);

    boxWidth = xMax - xMin;
    boxHeight = yMax - yMin;
    boxDepth = zMax - zMin;

  }


  public boolean checkExtents(PVector min, PVector max){

    boolean maximize = true;

    float x1 = screenX(pMin.x,pMin.y,pMin.z);
    float y1 = screenY(pMin.x,pMin.y,pMin.z);
    float z1 = screenZ(pMin.x,pMin.y,pMin.z);


    PVector v2=  new PVector(pMax.x, pMin.y, pMax.z);

    float x2 = screenX(v2.x,v2.y,v2.z);
    float y2 = screenY(v2.x,v2.y,v2.z);
    float z2 = screenZ(v2.x,v2.y,v2.z);

v2=  new PVector(pMax.x, pMax.y, pMin.z);

    float x3 = screenX(v2.x,v2.y,v2.z);
    float y3 = screenY(v2.x,v2.y,v2.z);
    float v3 = screenZ(v2.x,v2.y,v2.z);


v2=  new PVector(pMin.x, pMax.y, pMax.z);

    float x4 = screenX(v2.x,v2.y,v2.z);
    float y4 = screenY(v2.x,v2.y,v2.z);
    float v4 = screenZ(v2.x,v2.y,v2.z);

v2=  new PVector(pMin.x, pMin.y, pMax.z);

    float x5 = screenX(v2.x,v2.y,v2.z);
    float y5 = screenY(v2.x,v2.y,v2.z);
    float v5 = screenZ(v2.x,v2.y,v2.z);

v2=  new PVector(pMin.x, pMax.y, pMin.z);

    float x6 = screenX(v2.x,v2.y,v2.z);
    float y6 = screenY(v2.x,v2.y,v2.z);
    float v6 = screenZ(v2.x,v2.y,v2.z);


    float x8 = screenX(pMax.x,pMax.y,pMax.z);
    float y8 = screenY(pMax.x,pMax.y,pMax.z);
    float z8 = screenZ(pMax.x,pMax.y,pMax.z);

    if ((x8 < max.x) && (x1 < max.x) && (x2 < max.x)
     && (x3 < max.x) && (x4 < max.x) && (x5 < max.x) && (x6 < max.x) && (y8 < max.y) && (y1 < max.y)
     && (y2 < max.y) && (y3 < max.y)&& (y4 < max.y)  && (y5 < max.y) && (y6 < max.y) && (y1 > min.y)
     && (y2 > min.y) && (y3 > min.y)&& (y4 > min.y)&& (y5 > min.y)&& (y6 > min.y) && (y8 > min.y)&& (x1 > min.x)
     && (x2 > min.x) && (x3 > min.x)&& (x4 > min.x)&& (x5 > min.x)&& (x6 > min.y) && (x8 > min.x)){
      maximize = true;
    } else {
      maximize = false;
          }
    return maximize;

  }



  public void display(boolean fullScreen){

    if ((display) && !fullScreen) { drawBox(); }
    if (fullScreen){
      grow = checkExtents(new PVector(os,os),new PVector(width-os, height-os));
    } else {
      grow = checkExtents(p1,p2);
    }

  }

  public void drawBox(){
    stroke(34,155,215,150);
    strokeWeight(1);
    line(pMin.x,pMin.y,pMin.z, pMax.x, pMin.y,pMin.z);
    line(pMin.x,pMin.y,pMin.z, pMin.x, pMax.y,pMin.z);
    line(pMin.x,pMin.y,pMin.z, pMin.x, pMin.y,pMax.z);
    line(pMin.x,pMax.y,pMax.z, pMax.x, pMax.y,pMax.z);
    line(pMax.x,pMin.y,pMax.z, pMax.x, pMax.y,pMax.z);
    line(pMax.x,pMax.y,pMin.z, pMax.x, pMax.y,pMax.z);
    line(pMin.x,pMax.y,pMin.z, pMin.x, pMax.y,pMax.z);
    line(pMax.x,pMin.y,pMin.z, pMax.x, pMin.y,pMax.z);
    line(pMin.x,pMax.y,pMin.z, pMax.x, pMax.y,pMin.z);
    line(pMax.x,pMin.y,pMin.z, pMax.x, pMax.y,pMin.z);
    line(pMin.x,pMax.y,pMax.z, pMin.x, pMin.y,pMax.z);
    line(pMax.x,pMin.y,pMax.z, pMin.x, pMin.y,pMax.z);
  }




}
class CellArray  {












}
class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  PApplet app;

  int currentRow = 1;


  // can get rid of this ?
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<UnitCell> unitCells = new ArrayList<UnitCell>();

  Viewport2D vp;

  Zone zoneA, zoneB, zoneC, zoneD, zoneE, zoneF;  // select zones
  ArrayList<Zone> zones = new ArrayList<Zone>();

  Controller(PApplet _app) {

    initSelector();
    init();

    app = _app;
     m = new Model(app);
     m.currentThumb = thumbs.get(1);
     v = new View(m, vp);
     vp.set(m.depth);
     vp.m = m; // clean this up later

  }

  public void initSelector(){

    zoneA = new Zone(os, row3, col2,row4);
    zoneB = new Zone(os, row5, col4, row6);                 // row 2
    zoneC = new Zone(os, row7, col4, row8);                 //
    zoneD = new Zone(width/4, row0, col4, row4);                     //
     zoneE = new Zone(col5, row0, col10, row4);


     PVector dimGlobe = new PVector(width/6-os-os/2, width/6-os-os/2);
    // shaper = new Thumb(app, null,new PVector(width/2+ width/6+os, row5),  dimGlobe, "DEFAULT");

    zoneF = new Zone(width/2+width/6+os,row5,width/2+width/6+os+dimGlobe.x,row5+dimGlobe.y);


    zones.add(zoneA);
    zones.add(zoneB);
    zones.add(zoneC);
    zones.add(zoneD);
     zones.add(zoneE);
     zones.add(zoneF); // shaper tool

  }

  public void init() {
    int bW = PApplet.parseInt((width/4 - width/32));
    int tW = PApplet.parseInt((width/4 - width/32)/3); // width of thumb with row of 3
    float y4 = height/16+width/64+bW;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    dimThumb = new PVector((width/2 - os-os/2)/6,(width/2 - os-os/2)/6);


    Thumb depth = new Thumb(app,"/textures/array/depth/bubble.png",   new PVector(os,y4),   dimThumb, "unit/depth",  "MONO");
    Thumb mater = new Thumb(app,"/textures/array/mater/manta.png", new PVector(os+dimThumb.x,y4),   dimThumb, "unit/mater", "COLOR");
    Thumb alpha = new Thumb(app,"/textures/array/alpha/solid.png",    new PVector(os+2*dimThumb.x,y4),  dimThumb, "unit/alpha", "MONO");

    int voxX = PApplet.parseInt(DimXY/0.040f);

    // depth.getMap(voxX);
    // mater.getMap(voxX);
    // alpha.getMap(voxX);

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    PShape shape = null;

    for (float x = os; x < width/2 - dimThumb.x; x += cellWidth){
      unitCells.add(new UnitCell(new PVector(x,row7), new PVector(cellWidth,cellWidth), shape ));
    }
    unitCells.get(0).isSelected = true;
    mater.isSelected = true;

    initViewport(new PVector(os, yA),  dimThumbView,  mater);

  }

  public void setCurrentUC(PShape _pc){

    for (UnitCell uc : unitCells){
      if (uc.isSelected){
        uc.setCloud(_pc);
      }
    }

  }

  public void updateShape(PShape ps){
    v.vp3D.setCellUnit(ps);
    // v.vp3D.setArrayUnit(ps);
    setCurrentUC(ps);
    m.vox.pointCloud = ps;
    m.vox.update();
  }

  public void initViewport(PVector _loc, PVector _size, Thumb _th){
    vp = new Viewport2D(_loc, _size, _th);
  }


  public void update() {

    v.display();

    // PVector mouse = new PVector(mouseX, mouseY);
    // for  (Zone z: zones){ z.display(mouse);} // not necessary

  }


  public void zoneC(PVector ms){
  Thumb current = m.currentThumb; // current thumb that is selected
  current.checkSelectedChildren();

  }

  public void zoneA(){

  }

  public void toggleMode(){
  // we run this when we switch


   v.vp3D.toggleMode();
   m.vox.toggleMode();
   vp.toggleMode();
   m.resetRows(v.vp3D.mode);
  thread("adjustGrid");

  }

  public void zoneB(PVector ms, ArrayList<Thumb> _thumbs){
  boolean toggle = false;

  for (Thumb th : _thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
        vp.set(m.currentThumb);
        toggle = true;
      }
    }
  }


public boolean checkR1(ArrayList<Thumb> _thumbs,  PVector ms){
  boolean regen = true;   // R1 is the channel level of thumbs

  for (Thumb th : _thumbs) {
     boolean b1 = th.isSelected;
      if (th.checkSelected(ms)) {
        if (!b1){
          m.currentR1 = th;
          vp.set(th);
        } else {
          regen = false;
        }
      }

  }

  if (regen){ currentRow = 1; }
  return regen;
}

public void checkR2(Thumb th,  PVector ms){
        m.currentR2 = th.checkSelectedChildren();
        vp.set(m.currentR2);
        currentRow = 2;

}

public void checkR3(PVector _ms){

  for (UnitCell cell : unitCells){
    if (cell.checkSelected(_ms)) {
      if (cell.pc != null){
        v.vp3D.setCellUnit(cell.pc);
        // v.vp3Darray.setCellArray(cell.pc);
      }
    }
  }

}


public boolean unitSelect(PVector ms){

  boolean regen = true;

    if (zoneA.isSelected(ms)){
        regen = checkR1(m.thumbs, ms);
        m.vox.updateChannel();
    } else if (zoneB.isSelected(ms)){
        if (m.currentR1 != null){checkR2(m.currentR1, ms);}
        m.vox.updateChannel();
    } else if (zoneC.isSelected(ms)) {
       checkR3(ms);
       regen = false;
    }
    return regen;
  }

  public void mouseDown(PVector ms) {

    if (v.vp3D.mode == "UNIT"){
      if (unitSelect(ms)){
        thread("RESETUNITCELL");
      }
    }
  }
}


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


  Exporter (PApplet parent, float _ratio, boolean _invert, PImage depthChannel, PImage alphaChannel, PImage materChannel, int _dimXY, int _dimZ) {

    println("initialize exporter");
    invert  =   _invert;
    dC      =   depthChannel;
    aC      =   alphaChannel;
    mC      =   materChannel;
    dimXY   =   _dimXY;
    dimZ    =   _dimZ;
    // ratioX   =   _ratio;

    // int voxZ = 100;

    // // layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
    // // int z = int(zz % layerVoxels);
    // // float ratio = float(z)/ layerVoxels;

  }

  public void start() {
    thread = new Thread(this);
    thread.start();
  }

  public void stop()
  {
    println("exporter stopping...");
    thread = null;
  }


  public void dispose() {
    stop();
  }



  public void run() {
    while (!isReady) { //

      println("voxel dimensions = " +  PApplet.parseInt(DimXY/0.040f) + "," + PApplet.parseInt(DimXY/0.040f) +"," + dimZ);
     for (int z = 0; z < dimZ;  z++){

      boolean invertLayer = true;
      float l = PApplet.parseFloat(z)/PApplet.parseFloat(dimZ);

      if (l > 0.5f){ invertLayer = false;}

        imgExport = getVoxLayer(getRatio(l),invertLayer,dC,aC,mC);

        PImage temp = imgExport.get();
        temp.resize(PApplet.parseInt(2*DimXY/0.040f),PApplet.parseInt(DimXY/0.040f));
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



  public float getRatio(float layer){

      int voxZ = PApplet.parseInt(DimZ/0.030f); //  num voxels in Z
      int zz = PApplet.parseInt(voxZ*layer); //
      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = PApplet.parseInt(zz % layerVoxels);
      float ratio = PApplet.parseFloat(z)/ layerVoxels;
      return ratio;

  }


public PGraphics getVoxLayer(float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){

    int voxXY = depthChannel.width;

    PGraphics temp = createGraphics(voxXY,voxXY);
    temp.beginDraw();
    temp.background(0);

    for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {

        if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

           float val = brightness(depthChannel.get(x, y));
            int c = materChannel.get(x,y);
         // c = translatePo(c);
            float pp = val/255;



            if (i){
              if (pp < ratio) { temp.set(PApplet.parseInt(x), PApplet.parseInt(y), c);} // below halfway
            } else {
              pp = 1 - pp;
              if (pp > ratio) { temp.set(PApplet.parseInt(x), PApplet.parseInt(y), c);} // below halfway
            }

            // if (invert){
            //   if (pp >= ratio) { temp.set(int(x), int(y), c);} // below halfway
            // } else {
            //   pp = 1 - pp;
            //   if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway
            // }
          }
        }
      }

    temp.endDraw();
    return temp;
    }


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


  public int translatePo(int c){

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


}


class VoxelLayer implements Runnable {


  Thread thread;
  boolean drawNew = false;
  boolean isReady = false;

  PImage imgExport, imgVisual;
  PImage displayLayer;
  PImage dC, aC, mC;
  int pauseTime, dimXY;
  boolean invert;
  float ratio;

  VoxelLayer (PApplet parent, float _ratio, boolean _invert, PImage depthChannel, PImage alphaChannel, PImage materChannel, int dim) {

    ratio = _ratio;
    invert = _invert;
    dC = depthChannel;
    aC = alphaChannel;
    mC = materChannel;
    dimXY = dim;

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

      imgExport = getVoxLayer(ratio,invert,dC,aC,mC);
      imgVisual = imgExport.get();
      imgVisual.resize(dimXY,dimXY);

      // imgVisual = dC.get();
      // imgVisual.resize(dimXY, dimXY);


      try {
        Thread.sleep(200);
        isReady = true;
      }
      catch(InterruptedException e) {
      }
    }
  }


public PGraphics getVoxLayer(float ratio, boolean invert, PImage depthChannel, PImage alphaChannel, PImage materChannel){

    int voxXY = depthChannel.width;

    PGraphics temp = createGraphics(voxXY,voxXY);
    temp.beginDraw();
    temp.background(0);

    for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {

        if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

           float val = brightness(depthChannel.get(x, y));
            int c = materChannel.get(x,y);
         // c = translatePo(c);
            float pp = val/255;

            if (invert){
              if (pp >= ratio) { temp.set(PApplet.parseInt(x), PApplet.parseInt(y), c);} // below halfway
            } else {
              pp = 1 - pp;
              if (pp < ratio) { temp.set(PApplet.parseInt(x), PApplet.parseInt(y), c);} // below halfway
            }
          }
        }
      }

    temp.endDraw();
    return temp;
    }


  public int translatePo(int c){

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


}


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

    x = PApplet.parseInt(_size.x);
    y = PApplet.parseInt(_size.y);
    filepath = _filepath;

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


  public PImage normalize(PImage img) {

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

    float constant = 255.0f/(max-min);
    float newMin  = 255.0f;
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

  public PImage invert(PImage _img){
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
class Model {

  // this class is all about modelling the 3D Geometry

  PImage slice;
  PGraphics pg;
  PointCloud pointCloud;
  Thumb shaper;
  PApplet app;

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> thumbsArray = new ArrayList<Thumb>();
  ArrayList<Thumb> thumbsGlobal = new ArrayList<Thumb>();

  CellArray cArray; // this object controls the array

  Thumb alpha, mater, depth, currentThumb;
  Thumb alphaArray, materArray, depthArray;
  Thumb alphaGlb, materGlb, depthGlb;

  Thumb currentR1, currentR2, currentR3;

  Voxelator vox;

  //  PGraphics matMap;
  int stepX = 2;
  int stepY = 2;
  int stepZ = 4;

  int step = 1;
  int thickness = 25; // thickness of each layer

  ArrayList<PVector> points = new ArrayList<PVector>();

  Model(PApplet _app) {

    app = _app;

    initChannelThumbs();

    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    // depthArray = depth.array(stepX,stepY);
    // alphaArray = alpha.array(stepX,stepY);
    // materArray = mater.array(stepX,stepY);

    // thumbsArray.add(depthArray);
    // thumbsArray.add(alphaArray);
    // thumbsArray.add(materArray);

    initGlobalThumbs();
    initVoxel(thumbs, thumbsArray, thumbsGlobal);

    // pg = createGraphics(200, 200);

  }

  public void updateArray(){
    thumbsArray = new ArrayList<Thumb>();
    thumbsArray.add(depthArray);
    thumbsArray.add(alphaArray);
    thumbsArray.add(materArray);
    vox.setArrays(thumbsArray);
  }



  public void resetRows(String mode){

    currentR1 = null;
    currentR2 = null;
    currentR3 = null;


    if (mode == "UNIT"){

      for (Thumb th: thumbs){ if (th.isSelected){ currentR1 = th;}}
      if (currentR1 != null){ for (Thumb th: currentR1.children){ if (th.isSelected){ currentR2 = th;}}}

    } else {

     for (Thumb th: thumbsGlobal){ if (th.isSelected){ currentR1 = th;}}
      if (currentR1 != null){ for (Thumb th: currentR1.children){ if (th.isSelected){ currentR2 = th;}}}
    }
  }


  public void initChannelThumbs(){

    int bW = PApplet.parseInt((width/4 - width/32));
    int tW = PApplet.parseInt((width/4 - width/32)/3); // width of thumb with row of 3
    // float y4 = height/16+width/64+bW;

    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);



    // dimThumb = new PVector((width/2 - os-os/2)/6,(width/2 - os-os/2)/6);


    depth = new Thumb(app, "/textures/array/depth/bubble.png",   new PVector(os,row3),   dimThumb, "unit/depth","MONO");
    mater = new Thumb(app, "/textures/array/mater/manta.png", new PVector(os + dimThumb.x,row3),   dimThumb, "unit/mater", "COLOR");
    alpha = new Thumb(app, "/textures/unit/alpha/dots.png",    new PVector(os + dimThumb.x*2,row3),  dimThumb, "unit/alpha","MONO");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    mater.isSelected = true;
    currentR1 = materGlb;

  }

  public void initGlobalThumbs(){

    int bW = PApplet.parseInt((width/4 - width/32));
    int tW = PApplet.parseInt((width/4 - width/32)/3); // width of thumb with row of 3

    float y4 = height/16+width/64+bW+os+tW;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);


     // depthGlb = new Thumb(app,"/textures/global/depth/blank.png",   new PVector(os,y4),   dimThumb, "global/depth");
     // materGlb = new Thumb(app,"/textures/global/mater/blank.png", new PVector(os+tW,y4),   dimThumb, "global/mater");
     // alphaGlb = new Thumb(app,"/textures/global/alpha/blank.png",new PVector(width/2+ width/6+os, row5),  dimGlobe, "global/alpha");
     // shaper = new Shape(app,null,new PVector(width/2+ width/6+os, row5),  dimGlobe, "DEFAULT");


    PVector dimGlobe = new PVector(width/6-os-os/2, width/6-os-os/2);
    shaper = new Thumb(app, null,new PVector(width/2+ width/6+os, row5),  dimGlobe, "DEFAULT");

    // thumbsGlobal.add(depthGlb);
    // thumbsGlobal.add(materGlb);
    // thumbsGlobal.add(alphaGlb);

    // materGlb.isSelected = true;
    // currentR1 = materGlb;

  }

  public void initVoxel(ArrayList<Thumb> _units, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

    //
    PVector dimVox = new PVector(width/6-os-os/2, width/6-os-os/2);


    // location , size
    initVoxelator(new PVector(width/2+ width/3+os/2, row5), dimVox, _units, _arrays, _globes);

  }

  public void initVoxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){
      vox = new Voxelator(app, _loc, _size,  _thumbs, _arrays, _globes);

  }

  public void arrayTexture(){

  }

  public void displayCellUnit() {
    // getLayer(Current, false); // activate voxel map



    for (Thumb th : thumbs) {
      th.display();
    }
    // testSelection();
    // for (Thumb th: thumbsGlobal){
    //   th.display();
    // }
    shaper.display();

  }


  public void displayCellArray() {

    for (Thumb th : thumbsArray) {
      th.display();
    }

    for (Thumb th: thumbsGlobal){
      th.display();
    }

  }


  public void testSelection(){

    // println("test  selection: ");
      if (currentR1 != null){ currentR1.debug();}
      if (currentR2 != null){ currentR2.debug();}
      if (currentR3 != null){ currentR3.debug();}
  }


  public ArrayList<PVector> resetPC(int res, float []a) {

    //Println("reset pointCloud");
    ArrayList<PVector> temp = new ArrayList<PVector>();
    depth.map.loadPixels();

    float range = 255;

    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {

        float val = brightness(depth.map.get(x, y));

        if (a[0] == 1) {
          temp.add(new PVector(x, y, -val/2 + range/2));
          temp.add(new PVector(x, y, val/2 + range/2));
        } else {
          temp.add(new PVector(x, y, val));
        }
      }
    }
    return temp;
  }


}

class PWindow extends PApplet {
  // this class displays 3D Content

  PShape cloud = null;

  PWindow() {
    super();
    PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);
  }


  float rotX = 0.05f;
  float rotY =0.05f;

  public void settings() {
    size(1000, 1000, P3D);
      // fullScreen(P3D);
  }


  public void setup() {
    background(150);
  }

 public void setPointCloud(PShape s){

  cloud = s;
 }


   public void draw() {
    background(0);
    fill(255);
    stroke(255);
    strokeWeight(10);
    pushMatrix();
    translate(width/2, height/2, 0);
    rotateX(rotY);
    rotateY(rotY);
    rotateZ(rotY);
    if (cloud != null) {
      shape(cloud);

    } else {
      point(0,0,0); }
    popMatrix();
    rotY +=0.025f;
  }

  public void mousePressed() {
    println("mousePressed in secondary window");
  }
}

class PointCloud {

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc, currentLayerPC;
  PVector p1, p2, defaultPosition;

  BoundingBox bbox;
  ArrayList<BoundingBox> cellArray;

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  TweenPoint position, rotation, scale;

  float vWidth, vHeight, currentLayer;
  boolean mode; // mode 0 = max , mode 1 = min

  boolean spin;

PShape boxCloud;


  PointCloud(float x1, float y1, float x2, float y2, boolean _mode){

    p1 = new PVector(x1,y1);
    p2 = new PVector(x2,y2);
    vWidth = x2 -x1;
    vHeight = y2 - y1;
    mode = _mode;

    initView();

  }

  public void setCloud(PShape _pc){
    boxCloud = _pc;
    getBoundingBox(boxCloud);
  }


  public void setCloudArray(PShape _pc){


    PVector min = new PVector(-200,-200,-200);

    boxCloud = _pc;
    getBoundingBox(boxCloud);

    float numZ = 3;
    float numY = 3;
    float numX = 3;

    float stepX = (bbox.pMax.x - bbox.pMin.x) / numX;
    float stepY = (bbox.pMax.y - bbox.pMin.y) / numY;
    float stepZ = (bbox.pMax.z - bbox.pMin.z) / numZ;


    cellArray = new ArrayList<BoundingBox>();

 //  for (float x = bbox.pMin.x; x < bbox.pMax.x; x += stepX){
 //   for (float y = bbox.pMin.y; y < bbox.pMax.y; y += stepY){
 //    for (float z = bbox.pMin.z; z < bbox.pMax.z; z += stepZ){
 //     cellArray.add(new BoundingBox(x,y,z, x + stepX, y + stepY, z + stepZ, p1, p2));
 //    // println("cellarrays = " + cellArray.size());

 //    }
 //   }
 // }

      }




  public void setMode2(){ // ZOOM OUT

  // xScal = 1;
  //   xRot = -atan(sin(radians(45)));
  //   yRot =  radians(45);
  //   xPos = width/4 + vWidth/8;
  //   yPos = yB + vHeight/8 - os;

  }

  public void setMode1(){ // ZOOM IN
    // xScal = 3;
    // xRot = -atan(sin(radians(45)));
    // yRot =  radians(45);
    // xPos = width/4 + vWidth/2;
    // yPos = vHeight/2-2*os;

  }

  public void initView(){

    PVector start,scl,rot;
      float w = 3*width/8;
      scl = new PVector(0.75f,0.75f,0.75f);
      rot = new PVector(-atan(sin(radians(-30))), radians(-30),0);
      start = new PVector((p2.x-p1.x)/2+p1.x, (p2.y-p1.y)/2 + p1.y, -10);
      defaultPosition = start;
    float val = 0.1f;
    position = new TweenPoint(start,start,val); // tween for position
    scale = new TweenPoint(scl,scl,val); // tween for position
    rotation = new TweenPoint(rot,rot,val);
    setLeft();
  }

  public void setLeft(){

    // println("set left");
    PVector rot = new PVector((radians(-90)), 0, 0);
    rotation.set(rot);

  }

  public void setAxo(){
    // println("set axo");
    PVector rot = new PVector((radians(-45)), radians(-45), 0);
    rotation.set(rot);

  }


  public void setRight(){
    println("set right");
    PVector rot = new PVector((radians(90)), 0, 0);
    rotation.set(rot);

  }

public void updateView(){
  // println("updateview");
  PVector end,scl,rot;

    if (!mode){
      // setMode1();
      end = new PVector(width/4 + vWidth/8, yB + vHeight/8 - os, 0);
      scl = new PVector(1,1,1);
      rot = new PVector(-atan(sin(radians(45))), radians(45),0);
      rotation.set(rot);

    }else{
      // setMode2();
      end = new PVector(width/4 + vWidth/2, vHeight/2-2*os);
      scl = new PVector(3,3,3);
    }
    position.set(end);
    scale.set(scl);
  }


  public void toggle(){
   updateView();

   mode = !mode;

  }

  public void set(ArrayList<PVector> _pc){
    // println("Pointcloud list: " + _pc.size());
    pc = _pc;
    // getBoundingBox(_pc);
  }

   public void updateCamera(){


    if (!mode){
      if (SpinX){   rotation.addIncrement(new PVector(0.005f,0,0));  }
      if (SpinY){   rotation.addIncrement(new PVector(0,0.005f,0));  }
      if (SpinZ){   rotation.addIncrement(new PVector(0,0,0.005f));  }
    }

    if (bbox != null){
    if (bbox.grow){
      // println("GROW!!!");

      // scale.addIncrement(new PVector(0.01,0.01,0.01));
      scale.addIncrement(new PVector(0.0005f,0.0005f,0.0005f));
    } else {
      scale.addIncrement(new PVector(-0.0005f,-0.0005f,-0.0005f));
    }
    }

   }

   public boolean isDisplay(float x, float y){
    boolean display = false; // check if point is in viewport
    if ((x > p1.x) && (x < p2.x) && (y > p1.y) && (y< p2.y)) { display = true;}
    return display;
   }

   public void displayLayer(){


    if (currentLayerPC != null){
      // strokeWeight(1);
      stroke(255);
      for (PVector p: currentLayerPC){
        point(p.x, p.y, p.z);}
      }
   }


public void displayFS(){
    // println("DISPLAY FULLSCREEN");
      pushMatrix();
      setView();

      stroke(255,100);
      fill(255,100);

      if (boxCloud != null){ shape(boxCloud);}
      if (bbox != null ){ bbox.display(true);}
      // // strokeWeight(1);
      // if (cellArray != null){
      //   for (BoundingBox cell: cellArray){ cell.display();}
      // }
      // if (showLayer){displayLayer();}

      popMatrix();
      updateCamera();

   }


  public void display(boolean showLayer){

      pushMatrix();
      setView();

      stroke(255,100);
      fill(255,100);

      if (boxCloud != null){ shape(boxCloud);}
      if (bbox != null ){ bbox.display(false);}
      if (cellArray != null){ for (BoundingBox cell: cellArray){ cell.display(false);}}
      popMatrix();
      updateCamera();

   }

   public void setView(){

      PVector pos = position.get(); // println(position)
      PVector scl = scale.get();
      PVector rot = rotation.get();

      // strokeWeight(1);
      stroke(255,100);
      translate(pos.x, pos.y, 0); //
      rotateX(rot.x);
      rotateY(rot.y);
      rotateZ(rot.z);
      scale(scl.x);


   }




  public void setCurrent(ArrayList<PVector> _pc){ //
    currentLayerPC = _pc; // as percentage of a layer
   }

   public void getBoundingBox(PShape _pc){


    // println("getBoundingBox:" + _pc.size());

    float xMin = 10;
    float yMin = 10;
    float zMin = 10;
    float xMax = 0;
    float yMax = 0;
    float zMax = 0;


    for (int i= 0; i < _pc.getVertexCount(); i++) {
      PVector p = _pc.getVertex(i);

      if (p.x < xMin){ xMin = p.x; }
      if (p.y < yMin){ yMin = p.y; }
      if (p.z < zMin){ zMin =  p.z; }

      if (p.x > xMax){ xMax = p.x; }
      if (p.y > yMax){ yMax = p.y; }
      if (p.z > zMax){ zMax = p.z; }

    }

    // println("BoundingBox: " + xMin + "," + yMin + "," + zMin+ "," + xMax + "," + yMax + "," + zMax);

    bbox = new BoundingBox(xMin,yMin,zMin,xMax,yMax,zMax,p1,p2);

   }




}
// PUBLIC controls


public void addButton(ControlP5 c, String name, float x, float y, int w, int h){

  c.addButton(name)
     .setPosition(x,y)
     .setSize(w,h)
     ;

}

public void addToggle(ControlP5 c, String name, float x, float y, int w, int h){
  c.addToggle(name)
     .setPosition(x,y)
     .setSize(w,h)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
}
class Shape {

  ImageThread it;
  boolean loaded = false;
  PImage parent, texture;
  PGraphics map;
  PVector loc,size;
  PApplet app;
  String path, mode;
  boolean isSelected = false;

  Shape(PApplet _app, String _path, PVector _loc, PVector _size, String _mode) {
    app   =   _app;
    // name  =   _name;
    mode = _mode;
    size  =  _size;
    loc = _loc;
    path = _path;
    // println("thumb path = " + _path);
    reset(_path, _loc);
  }


  public void reset(String _path, PVector _loc){
    println("reset: " + _path + "," + size);

    if (mode == "DEFAULT"){

      PGraphics temp = createGraphics(1024,1024);
      temp.beginDraw();
      temp.background(255);
      temp.endDraw();

      parent = temp.get();
      texture = parent.get();
      texture.resize(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
      map = null;
      loaded = true;

    } else {
      it = new ImageThread(app, _path,size);
      it.start();
    }
  }


  public void display(){



    if (loaded == false){
      if (it.isReady){
        // println("it is ready");
        parent = it.parent;//
        texture = it.texture;//
        map = it.map;
        it.stop();
        loaded = true;
      }
    }

    pushMatrix();
    translate(loc.x,loc.y);


    fill(34,155,215);
    if (loaded){
      image(texture,0,0);
    }else{
    rect(0,0,rA,rA);
    }
    popMatrix();


    noFill();
    if (isSelected) {

      stroke(163, 149, 41);
      rect(loc.x, loc.y, size.x, size.y); // Left
    } else if  ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      stroke(255, 100);
      rect(loc.x, loc.y, size.x, size.y); // Left
    }





  }


  // Shape(String _path) {

  //   println("load shape");

  //   String path = "/images/shape/grid.csv";
  //   Table table = loadTable(path);

  //   for (TableRow row : table.rows ()) {

  //     String str1 = row.getString(0);
  //     str1 = str1.replaceFirst("\\{", "");
  //     println(str1);

  //     float b = float(row.getString(1));

  //     println(row.getString(0)+ ","+ row.getString(1)+","+row.getString(2));
  //   }
  // }
}
class Thumb {


  boolean loaded = false;
  PGraphics map;
  PImage texture, parent, textureA, parentA, textureB, parentB, texMap;
  String path, name, mode;
  PVector loc, size;
  boolean isSelected = false;
  boolean isMater = false;
  ArrayList<Thumb> children; // chilrdren
  ArrayList<Thumb> selecteren = new ArrayList<Thumb>();
  ImageThread it;
  PApplet app;
  int scroll = 0;
  boolean fsaddfdasgsad;
  boolean isInverted = false;


  Thumb(PApplet _app, String _path, PVector _loc, PVector _size, String _name, String _mode) {
    mode  = _mode;
    app   =   _app;
    name  =   _name;
    size  =  _size;
    mode = _mode;
    loc = _loc;
    path = _path;

    reset(_path, _loc);
    setChildren(0); // start on step 1
  }


  Thumb(PApplet _app,String _path, PVector _loc, PVector _size, String _mode) {

    mode = _mode;
    path = _path;
    size = _size;
    loc = _loc;
    reset(_path, _loc);

  }

  public void invertTexture(){

    // println("invert texture");

    isInverted = !isInverted;
    if (isInverted){
        parent =  parentA;
        texture = textureA;
    } else {
        parent =  parentB;
        texture = textureB;
    }

  }



  public PImage getMap(int dim){
    PGraphics temp;
    // if (texMap != null){
    //   if (texMap.width != dim){texMap.resize(dim,dim);}
    // } else {
      texMap = parent.get();
      texMap.resize(dim,dim);
    // }
    return texMap;
  }


  public void reset(String _path, PVector _loc){
 // /("reset: " + _path + "," + size);
 if (mode != "DEFAULT"){
    it = new ImageThread(app, _path,size);
    it.start();
  } else {
    PGraphics temp = createGraphics(1024,1024);
    temp.beginDraw();
    temp.background(255);
    temp.endDraw();
    PImage defaultImage = temp.get();
    parent = defaultImage;
    parentA = parent;
    parentB = parent;
    texture = parent.get();
    texture.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
    textureA = texture;
    textureB = texture;

    map = null;
    loaded = true;
   }
  }



  public int translatePo(int c){

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


  public void resetChild(Thumb _child){

    parent = _child.parent.get();
    parentA = _child.parentA.get();
    parentB = _child.parentB.get();
    texture = parent.get();
    texture.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
    textureA = parentA.get();
    textureA.resize(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
    textureB = texture;
    texMap = null;

    map = _child.map;
    mode = _child.mode;
  }



  public void updateMap() {
    texture.resize(0, PApplet.parseInt(size.x));
    map.beginDraw();
    map.image(texture, 0, 0);
    map.endDraw();
  }




  public void setChildren(int step) {

    scroll += step;
    children = new ArrayList<Thumb>();

    String localPath = "/textures/" + name + "/";                   //
    String[] filenames = listFileNames(sketchPath() + localPath);   //
    float childWidth = PApplet.parseInt((xC - 2*os)/3);
    int items = filenames.length;
    int row = 0;
    // int col = 0;
    int b1 = scroll;
    int b2 = 3+scroll;
    b2 = 6;

    if (b2 > items) {
      b2 = items;

    }

    // PVector childSize = new PVector((width/2 - os-os/2)/6,(width/2 - os-os/2)/6);


    int col = 1;



    PVector childSize = new PVector(size.x/2,size.x/2);

    // add default material
    children.add(new Thumb(app, null, new PVector(os,loc.y+size.x+os), childSize, "DEFAULT"));


    if (filenames !=null) {
      for (int j = 0; j < b2; j++){


        children.add(new Thumb(app, localPath + filenames[j], new PVector(os+childSize.x*col,loc.y+size.x+os), childSize, mode));

        col ++;
        // if (col == 3) {
        //   col = 0;
        //   row++;
        // }
      }
    }
  }

  public void showChildren() {

    if (children != null) {
      for (Thumb child : children) {
        child.display();
      }
    }

  }




  public PImage normalize(PImage img) {

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
      if ((val > 25)){
        brightness +=  val;
        if (val < min) min = val;
        if (val >max) max = val ;
      }
    }


    float constant = 255.0f/(max-min);
    float tempRange = max-min;


    for (int i = 0; i <    temp.pixels.length; i++) {
      float val = brightness(temp.pixels[i]);
      float newVal = (val-min)*constant;
      temp.pixels[i] =  color(newVal);
    }

    temp.updatePixels();
    temp.endDraw();

    img = temp.get(); // normalized image

    return img;
  }



  public PImage colorize(PImage temp){

    println("colorize");

  temp.loadPixels();

   for (int i = 0; i < temp.pixels.length; i++) {
      int c = temp.pixels[i];
      temp.pixels[i] =  translatePo(c);
    }

    temp.updatePixels();

    return temp;

  }

  public Thumb checkSelectedChildren() {

   Thumb chi = null;

   for (Thumb child: children){
      if ((mouseX > child.loc.x) && (mouseX < (child.loc.x + size.x)) && (mouseY > child.loc.y) && (mouseY < (child.loc.y + size.y))) {
        child.isSelected = true;
        chi = child;

        resetChild(child); // reset Channel with  currennt child...

      } else {
        child.isSelected = false;
        // selectedChildren.remove(child);
      }
    }

    return chi;
  }

  public PImage invert(PImage _img){
    // println("invert");
    PImage img = _img.get();
    PGraphics temp = createGraphics(img.width,img.height);
    temp.beginDraw();
    temp.image(img, 0, 0);
    temp.filter(INVERT);
    temp.endDraw();

    img = temp.get();
    return img;
  }



  public boolean checkSelected(PVector ms) { // hmm we can  refactor this
    isSelected = false;
if ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      isSelected = !isSelected;
    } else {
      isSelected = false;
    }
    return isSelected;
  }

  public void debug(){

      // println("debug");

      noFill();
      stroke(163, 149, 41);
      rect(loc.x, loc.y, size.x, size.y); // Left

  }
  public void display() {

    if (loaded == false){
      if (it.isReady){
        // println("it is ready");

        if (mode == "COLOR"){
          // println("multi-material");
          // parent = colorize(it.parent);    //
          parent = it.parent;    //
          texture = it.texture;  // We will make another function for multi material
          parentA = it.parentA;
          textureA = it.parentA;
          parentB = parent;
          textureB = texture;

          // parentA    = null;
          // textureA  = null;
          // parentB   = null;
          // textureB  = null;

          map = null;

        }else{
          // println("normalize");
          parent = normalize(it.parent);//
          texture = parent.get();
          texture.resize(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
          // texture = normalize(it.texture);//

          parentA = it.parentA;
          textureA = parentA.get();
          textureA.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
          parentB = parent;
          textureB = texture;

          // parentA    = null;
          // textureA  = null;
          // parentB   = null;
          // textureB  = null;

          // textureA = normalize(it.texture)
          map = null;
        }
        it.stop();
        loaded = true;
      }
    }

    pushMatrix();
    translate(loc.x,loc.y);


    fill(34,155,215);
    if (loaded){
      image(texture,0,0);
    }else{
    rect(0,0,rA,rA);
    }
    popMatrix();


    noFill();
    if (isSelected) {

      stroke(163, 149, 41);
      rect(loc.x, loc.y, size.x, size.y); // Left
      showChildren();
    } else if  ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      stroke(255, 100);
      rect(loc.x, loc.y, size.x, size.y); // Left
    }
  }
}
class TweenPoint{

float dpmg; // damping factor
PVector start,end;

 TweenPoint(PVector _start, PVector _end, float _dpmg){

    start = _start;
    end = _end;

 }

 public void addIncrement(PVector p){

  end.x += p.x;
  end.y += p.y;
  end.z += p.z;

 }

 public void set(PVector _end){
  end = _end;

 }
 public PVector get(){

    // start.x += 0.5;
    // start.y += 0.5;
    // start.z += 0.5;
  float valX = (end.x-start.x)*0.1f;
  float valY = (end.y-start.y)*0.1f;

  if (start != end) {
    // println("damping = " + val);
  }


  start.x = start.x + valX;
  start.y = start.y + valY;
  start.z = start.z +(end.z -start.z)*dpmg;

  return start;
 }

}
class UnitCell {

  // Thumb depth, alpha, mater;
  PShape pc = null;
  PVector loc, size;

  boolean isSelected = false;

  UnitCell(PVector _loc, PVector _size, PShape _pc){
    loc = _loc;
    size = _size;
    pc = _pc;
  }



  public void setCloud(PShape _pc){
    pc = _pc;

  }

  public boolean checkSelected(PVector ms) { // hmm we can  refactor this
    isSelected = false;
    if ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      isSelected = true;
    } else {
      isSelected = false;
    }
    return isSelected;
  }



  public void display(){

    strokeWeight(1);
    stroke(34,155,215);
    noFill();

    if (isSelected){ strokeWeight(4);}

    pushMatrix();
    translate(loc.x,loc.y);
    rect(0,0,size.x,size.y);

    if (pc != null ){
      pushMatrix();
      translate(size.x/2, size.y/2);
      rotateX(PI/4);
      rotateZ(-PI/4);
      scale(.3f);
      shape(pc);
      popMatrix();
    }
    popMatrix();




  }




















}
class View {

  // this controls the styles of display
  int viewMode = 2;
  Model model;
  Viewport2D vp2D;
  Viewport3D vp3D, vp3Darray;
  Voxelator vox;
  // Control control;

  float thickness = 2;

  // camera variables
  float xRot = 3.14f/2;
  float zRot = 3.14f/2;
  float current = 0; //?

  View(Model _m, Viewport2D _vp) {
    // println("initialize view class") ;
    model = _m;
    // control = _C;
    vp2D = _vp;

    vp3D      = new Viewport3D(xC, yA, width/2 - os/2, y10+tWidth);
    vp3Darray = new Viewport3D(width/2 + os/2 , yA, width-os, y10+tWidth);

  }

  public void display() {

    if (viewMode == 2){
      String mode = vp3D.mode;
      display2D();
      display3D();

    } else {

      // background(255);
      display3D();
      // saveFrame("output2/line-######.jpg");

    }

  }

  public void display2D() {

    // drawZones();
    drawFrames();
    drawLabels();

    for (UnitCell cell : c.unitCells){
      cell.display();
    }


    if (vp3D.mode == "UNIT"){
      vp2D.display();   // this is just a screen
      model.displayCellUnit();  //
    } else {
      // vp2D.displayCellArray();
      // model.displayCellArray();
    }

    model.vox.display(); // this one ok

  }

  public void display3D() {
    vp3D.display();

    vp3Darray.display();

  }

  public void drawFrames() {


    stroke(255, 50);


    drawColumns();
    drawRows();



  }

  public void drawRows(){
    // ROWS


    // ROW 0

    line(0, yA, width, yA);

    // ROW 1-2

    line(0, row1, col3, row1);
    line(0, row2, col3, row2);

    // ROW 3-4

    line(0, row3, col3, row3);
    line(0,row4, width, row4);

    // ROW 5-6
    line(0,row5, width, row5);
    line(0,row6 , col4,row6 );

    // ROW 7-8
    line(0,row7 , width/2-os/2,row7 );


  }
  public void drawColumns(){

    //  COLUMNS

  // colum 1-2
    line(os, 0, os, height);

    // column 3-4
    line(width/4-os, 0, width/4-os, row5);
    line(width/4, 0, width/4, row5);

    // column 5-6
    line(width/2 - os/2,0, width/2 - os/2, height);
    line(width/2+os/2,0, width/2+os/2, height);

    // column 7-8
    line(width/2+  width/6 +os,row5,width/2 +  width/6+os, height);
    line(width/2+  width/6,row5,width/2 +  width/6, height);

    // column 9-10
    line(width/2+  width/3+os/2,row5,width/2 +  width/3+os/2, height);
    line(width/2+  width/3-os/2,row5,width/2 +  width/3-os/2, height);

    //
    // colum 11-12
    line(width-os, 0, width-os, height);


  }

  public void toggleMode(){

      println("toggleMode");


    if (viewMode == 2){

      viewMode = 1;
      vp3D.setView(1);
      vp3D.fullScreen = true;
      cp5.hide();

    } else {

      viewMode = 2;
      vp3D.setView(2);
      vp3D.fullScreen = false;
      cp5.show();


    }

  }


  public void drawLabels() {

    // Small Labels
    // textSize(18);
    // textAlign(CENTER);

    fill(255);

    textSize(11);
    textAlign(CENTER);
    // String s = "PointCloud Generator - Sayjel Vijay Patel - 2017";
    // text(s, width/2, height-os/2);

    // textAlign(CENTER);

    textSize(14);
    // String s = "Mode: " + vp3D.mode;
    // text(s, cA, rA);

    textSize(11);
    // text("CHANNELS", cA, rC);

    //     line(xA, yD-os, xB, yE-os);  // row1
    // line(xA, yE, xB, yE+tWidth); // row2
    // line(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row3

    if (vp3D.mode == "UNIT"){

    // text("TEXTURE: CHANNELS", cA, yD-os-os/2); // row1 //
    // text("TEXTURES", cA, yE-os/2); // row2 //

    } else {
    // text("UNITCELLS", cA, yD-os-os/2); // row1 //
    text("SHAPES: CHANNELS", cA, yE-os/2); // row2 //
    text("SHAPES", cA, yE+tWidth+os-os/2); // row2 //

    }
    // text("RULE-CHAIN", cB, rD);


    // String s = "Ouputs";
    // text(s, x2, y3);


    // s = "Parameters";
    // text(s, x3, y3);

    // textSize(11);

    // s = "Mirror Plane";
    // text(s, width*11/12, height/4 - 20);
  }


  public void drawZones() {

    stroke(34, 155, 215);
    strokeWeight(1);
    noFill();

    // Zone Row 1
    // line(xA, 0, xB, yA);

    // Zone Row 1?
    // line(xA, yB, xB, yC);

    // Zone 3-1
    line(xA, yD-os, xB, yE-os);  // row1
    line(xA, yE, xB, yE+tWidth); // row2
    line(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row3
    // rect(xA,yD, xB, yE);


  }




}
class Viewport2D {

  PVector size, loc;
  PImage image;
  Thumb thumb;
  Model m;

  String mode = "UNIT";

  Viewport2D(PVector _loc, PVector _size, Thumb _thumb){

    size = _size;
    loc = _loc;
    thumb = _thumb;

    update();

  }

  public void invertTexture(int currentRow){

    // thumb.invertTexture(); // this is the selected thumb here we should update the channel & child..

    if (m.currentR1 != null ){ m.currentR1.invertTexture();}
    if (m.currentR2 != null ){ m.currentR2.invertTexture();}

    image = null;
    // update();
    // set(thumb);
  }

  public void displayCellUnit(){
    // println("displayCell");
    pushMatrix();
    fill(34,155,215);
    translate(loc.x,loc.y);
    if (image != null){
      image(image, 0, 0);
    } else {
      update();
      rect(0,0,size.x,size.y);
    }

    popMatrix();
  }

  public void displayCellArray(){
    if (image != null ){ image(image, loc.x, loc.y);}
  }

  public void update(){
    // println("update 2d: " + thumb.name + "," + thumb.parent);
    if (thumb.parent != null){
      PGraphics temp  = createGraphics(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
      temp.beginDraw();
      temp.image(thumb.parent, 0,0,PApplet.parseInt(size.x),PApplet.parseInt(size.y));
      temp.endDraw();
      image = temp.get();

    }
  }

  public void toggleMode(){

    if (mode == "UNIT"){
      mode = "ARRAY";
    } else {
      mode = "UNIT";
    }

  }

  public void set(Thumb th){

    thumb = th;
   update();

  }

  public void display(){

    if (mode == "UNIT"){
      displayCellUnit();
      displayTool();
    } else {
      displayCellArray();
    }
  }

  public void displayTool(){

    noFill();
    strokeWeight(1);
    stroke(255,0,255);
    pushMatrix();
    translate(loc.x+size.x/3, loc.y+size.y/2);
    rect(0,0,150,150);
    popMatrix();

  }




}
class Viewport3D{

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc;
  PVector p1, p2;

  boolean fullScreen = false;

  BoundingBox bbox;

  float currentLayer = 0.5f;

  PointCloud cellUnit, cellArray;

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  float vWidth, vHeight;

  boolean pauseView = false;

  String mode = "UNIT"; // set as "UNIT" or "GLOBAL"



  Viewport3D(float x1, float y1, float x2, float y2){
    cellUnit = new PointCloud(x1,y1,x2,y2, false);
    cellArray = new PointCloud(x1,y1,x2,y2, true);

  }


  public void setView(int viewMode){

    if (viewMode == 2){
      // println("set position 2");
      PVector p1 = cellUnit.p1;
      PVector p2 = cellUnit.p2;
      PVector start = new PVector((p2.x-p1.x)/2+p1.x, (p2.y-p1.y)/2 + p1.y, -10);
      cellUnit.position.set(start);
      cellUnit.scale.set(new PVector(1,1,1));

      fullScreen = false;

    } else {

      cellUnit.position.set(new PVector(width/2,height/2));
      cellUnit.scale.set(new PVector(3,3,3));
      fullScreen = true;
    }

  }

  public void toggleSpin(){
    cellUnit.spin = !cellUnit.spin;

  }

  public void setCellUnit(PShape _pc){
    cellUnit.setCloud(_pc);
  }

  public void setCellArray(PShape _pc){
    cellUnit.setCloudArray(_pc);
  }

  public void toggleMode(){

    cellUnit.toggle();
    cellArray.toggle();

    if (mode == "UNIT"){
      mode = "ARRAY";
    } else {
      mode = "UNIT";
    }

  }

  public void setCurrentLayer(ArrayList<PVector> _pc){
    if (mode == "UNIT"){
    cellUnit.setCurrent(_pc);
    } else {
    cellArray.setCurrent(_pc);
    }
  }

  public void display(){

    if (!fullScreen){
      cellUnit.display(true);
    } else {
      cellUnit.displayFS();
    }

  strokeWeight(1);
  stroke(34,155,215);
  strokeWeight(1);

  }

}
class Voxelator {


  // PImage layer;

    int Cyan    = color(0,89,158);
    int Magenta = color(161,35,99);
    int Yellow  = color (213, 178, 0);
    int Black   = color(30,30,30);
    int White   = color(220,222,216);


  PApplet pApp;
  PVector loc, size;
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> arrays = new ArrayList<Thumb>();
  ArrayList<Thumb> globes = new ArrayList<Thumb>();
  ArrayList<PVector> pcLayer = new ArrayList<PVector>();
  PGraphics pg;
  PImage currentLayer;

  float layer, thickness;
  float dimX, dimY;

  PShape pointCloud;

  Thumb depth, alpha, mater;
  Thumb depthArray, alphaArray, materArray;
  Thumb depthGlobe, alphaGlobe, materGlobe;

  PImage depthChannel, alphaChannel, materChannel;

  boolean loaded = false;

  String mode = "UNIT";
  String fillMode = "SHELL";

  VoxelLayer vL;


  Exporter exportVoxels;


  Voxelator(PApplet _pApp, PVector _loc, PVector _size, ArrayList<Thumb> _thumbs, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

    pApp  = _pApp;
    loc    =  _loc;
    size   =  _size;
    thumbs =  _thumbs;
    arrays = _arrays;
    globes = _globes;

    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    dimX = size.x;
    dimY = size.z;

    layer = Current;
    thickness = Thickness;

  }


  public void toggleMode(){

    // do nothing //

  }


  public void toggleFillMode(){

    if (fillMode == "SHELL"){
      fillMode = "SOLID";
    } else {
      fillMode = "SHELL";
    }
    update();
  }

  public ArrayList<PVector> getCurrentPC(){  // returns pointcloud of the current list

    ArrayList<PVector> _pc = new ArrayList<PVector>();
    int res = 1;
    PImage img = pg.get();
    img.resize(PApplet.parseInt(dimX),PApplet.parseInt(dimY));

    for (int x = 0; x < img.width; x+= res){
      for (int y =  0; y < img.height; y+= res ){
          int c = img.get(x, y);
          if (brightness(c) > 100){
            _pc.add(new PVector(x,y,layer*255*Amplitude));
          }
      }
    }

    return _pc;
  }


  public void setArrays(ArrayList<Thumb> _arrays){
    depthArray = _arrays.get(0);
    materArray = _arrays.get(1);
    alphaArray = _arrays.get(2);

    if (alphaGlobe != null ){updateArray(); }
    //
  }

  // void exportStack(){
  //   export();
  // }

   public void export(){

    int layers = 0;
    PVector dim = new PVector(400,400);


    for (int l = 0; l < 255; l++){

    PImage img = depth.texture.get();
    img.resize(PApplet.parseInt(dim.x),PApplet.parseInt(dim.y));

    pg = createGraphics(PApplet.parseInt(dim.x),PApplet.parseInt(dim.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      int bright = img.pixels[k];
      float val = brightness(bright);
    if ((val < (layer+thickness)) && (val > (layer-thickness))) {
        pg.pixels[k] = color(255);
      }
    }
      pg.updatePixels();
      pg.endDraw();
      pg.save("exports/layer" + nf(l, 3) + ".png");
    }

  }

  public void toggle(){



  }

  public void update(){
    updateUnit();
  }


  public int translatePoArchive(int c){


    // float r0 = red(c)/255;
    // float g0 = green(c)/255;
    // float b0 = blue(c)/255;

    // float k = 1 - max(r0,g0,b0); // return maximum?
    // float cy = (1 - r0 -k)/(1-k);
    // float mg = (1 - g0 -k)/(1-k);
    // float ye = (1 - b0 -k)/(1-k);

    // float sum = k + cy + mg+ ye;
    // float rnd = random(0,sum);

    // if ((rnd > 0) && (rnd < k)){
    //   if (k > 0.5){
    //     return Black;
    //   } else {
    //     return White;
    //   }
    // } else if (( rnd > k ) && (rnd < k + cy)){ return Cyan;
    // } else if (( rnd > k + cy ) && (rnd < k + cy + ye)){ return Yellow;
    // } else if (( rnd > k + cy + ye)){ return Magenta;}

    return White;

  }


  public void updateUnit(){

    if (pointCloud != null){
      println("update voxelator: layer = " + layer);

      int voxXY = PApplet.parseInt(DimXY/0.040f); //  num voxels in X
      int voxZ = PApplet.parseInt(DimZ/0.030f); //  num voxels in Z
      int zz = PApplet.parseInt(voxZ*layer); //

      // is there a way not to do this each time?

      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = PApplet.parseInt(zz % layerVoxels);
      boolean i = false;
      if (layer > 0.5f){ i = true;}

      float ratio = PApplet.parseFloat(z)/ layerVoxels;
      getLayer(ratio, i, depthChannel, alphaChannel, materChannel, PApplet.parseInt(size.x));
    }

  }


public void getLayer(float ratio, boolean invert, PImage dC, PImage aC, PImage mC, int dim){


      PImage imgExport = getVoxLayer(ratio,invert,dC,aC,mC);
      PImage imgVisual = imgExport.get();
      imgVisual.resize(dim,dim);
      currentLayer = imgVisual;


  // loaded = false;
  // vL = new VoxelLayer(pApp, ratio,invert,depthChannel,alphaChannel,materChannel, dim);
  // vL.start();

}












// void up


  public void exportStack(){


    if (pointCloud != null){
      println("exporting stack...");
      int voxXY = PApplet.parseInt(DimXY/0.040f); //  num voxels in X

      println("dimZ = " + DimZ);

      int voxZ = PApplet.parseInt(DimZ/0.030f); //  num voxels in Z
      updateChannel();
      boolean invert = true;
      exportVoxels = new Exporter(pApp, getRatio(),invert,depthChannel,alphaChannel,materChannel, voxXY, voxZ);
      exportVoxels.start();
    }

  }

  public float getRatio(){

      int voxZ = PApplet.parseInt(DimZ/0.030f); //  num voxels in Z
      int zz = PApplet.parseInt(voxZ*layer); //
      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = PApplet.parseInt(zz % layerVoxels);
      float ratio = PApplet.parseFloat(z)/ layerVoxels;
      println("ratio = " + ratio);
      return ratio;

  }

  public void updateSolid(){

    if (pointCloud != null){

      println("update voxelator: layer = " + layer);
      int voxXY = PApplet.parseInt(DimXY/0.040f); //  num voxels in X
      int voxZ = PApplet.parseInt(DimZ/0.030f); //  num voxels in Z
      int zz = PApplet.parseInt(voxZ*layer); //

      // is there a way not to do this each time?

      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = PApplet.parseInt(zz % layerVoxels);
      boolean invert = false;
      if (layer > 0.5f){ invert = true;}

      float ratio = PApplet.parseFloat(z)/ layerVoxels;
      getLayer(ratio, invert, depthChannel, alphaChannel, materChannel, PApplet.parseInt(size.x));
    }
  }

  public void updateShell(){

    updateSolid();

  }

  public void updateChannel(){



    int voxXY = PApplet.parseInt(DimXY/0.040f); //

    depthChannel = depth.getMap(voxXY);
    alphaChannel = alpha.getMap(voxXY);
    materChannel = mater.getMap(voxXY);

  }


  public void updateArray(){

    // println("get depth texture");
    PImage img = depthArray.parent.get();
    PImage shape = alphaGlobe.parent.get();

    img.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
    shape.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));

    pg = createGraphics(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
    pg.beginDraw();
    // pg.background(0);
    pg.image(img,0,0);
    pg.endDraw();

    pg.loadPixels();
    shape.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      int bright = pg.pixels[k];
      int bright2 = shape.pixels[k];

      float val = brightness(bright);
      float val2 = brightness(bright2);
    if ((val < (layer+thickness)) && (val > (layer-thickness)) && (val2 > 25)) {
        pg.pixels[k] = color(255);
      } else {
      pg.pixels[k] = color(0);
      }
    }

    pg.updatePixels();


  }

  public void display(){

    // if (vL != null){
    if (currentLayer == null ){
      // if (vL.isReady){
      //   currentLayer = vL.imgVisual;
      //   vL.stop();
      //   loaded = true;
      // }
    } else {
      image(currentLayer, PApplet.parseInt(loc.x) , PApplet.parseInt(loc.y) );
    }
  // }

  if (exportVoxels != null){
    if (exportVoxels.isReady){
      exportVoxels.stop();
      exportVoxels = null;
    }

  }

}

public PGraphics getVoxLayer(float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){

    /*
    println(".............");
    println("get voxel layer");
    println(".............");
    */


    int voxXY = depthChannel.width;

    PGraphics temp = createGraphics(voxXY,voxXY);
    temp.beginDraw();
    temp.background(0);

    for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {

        if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

           float val = brightness(depthChannel.get(x, y));
            int c = materChannel.get(x,y);
         // c = translatePo(c);
            float pp = val/255;

            if (i){
              if (pp < ratio) { temp.set(PApplet.parseInt(x), PApplet.parseInt(y), c);} // below halfway
            } else {
              pp = 1 - pp;
              if (pp > ratio) { temp.set(PApplet.parseInt(x), PApplet.parseInt(y), c);} // below halfway
            }
          }
        }
      }

    temp.endDraw();
    return temp;
    }


  public int translatePo(int c){

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



}

class Zone {

  PVector a,b,dim; // width and height of zone

  Zone(float x1, float y1, float x2, float y2){

     a = new PVector(x1,y1);
     b = new PVector(x2,y2);
     dim = new PVector(x2-x1,y2-y1);

  }

  public boolean isSelected(PVector pt){
    // check if a point lies within the zone
    boolean select = false;

    if ((pt.x > a.x) && (pt.y > a.y) && (pt.x < b.x) && (pt.y < b.y)){
      select = true;
    }

    return select;

  }

  public void display(PVector pt){

    stroke(34,155,215,200);
    strokeWeight(5);
    noFill();

    if (isSelected(pt)){      rect(a.x,a.y,dim.x,dim.y);}

    strokeWeight(1);
  }












}
// void initFiles(){
//   // Using just the path of this sketch to demonstrate,
//   // but you can list any directory you like.
//   String path = sketchPath();
//   println("Listing all filenames in a directory: ");
//   String[] filenames = listFileNames(path + "/images/alpha/");
//   printArray(filenames);
// }

// This function returns all the files in a directory as an array of Strings
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
// contains image filtering functions

 
 public float[][] sharpen = {
      {
        0, -1, 0
      }
      , {
        -1, 5, -1
      }
      , {
        0, -1, 0
      }
    }; 
 public int convoBlur(int x, int y, float[] Matrix, PGraphics iGraph){ 

  float sumR = 0.0f;
  float sumG = 0.0f;
  float sumB = 0.0f;
  int MatrixSize = PApplet.parseInt(sqrt(Matrix.length)); //is our matrix 3x3, 5x5, etc.?
  int countMatrix = 0; //to keep track of where we are in convolution matrix

  for (int i =-MatrixSize/2; i<=MatrixSize/2; i++){
    for (int j=-MatrixSize/2; j<=MatrixSize/2; j++){
      //what pixel are we testing
      int xloc = x+i;
      int yloc = y+j;
      int loc = xloc + iGraph.width*(yloc+j);     
      loc = constrain(loc,0, iGraph.pixels.length-1); //make sure we haven't walked off our image
      sumB += (blue(iGraph.pixels[loc]) * Matrix[countMatrix]);
      sumG += (green(iGraph.pixels[loc]) * Matrix[countMatrix]);
      sumR += (red(iGraph.pixels[loc]) * Matrix[countMatrix]);     //calculate the convolution
      countMatrix++;
    }
  }

  //make sure RGB is withing range
  sumR = constrain(sumR,0,255);
  sumG = constrain(sumG,0,255);
  sumB = constrain(sumB,0,255);

  //return the resulting color
  return color(sumR,sumG,sumB);
}
   
public int convolutionBlur(int x, int y, float[] Matrix)
{

  float sumR = 0.0f;
  float sumG = 0.0f;
  float sumB = 0.0f;

  //is our matrix 3x3, 5x5, etc.?
  int MatrixSize = PApplet.parseInt(sqrt(Matrix.length));
  //to keep track of where we are in convolution matrix
  int countMatrix = 0;

  for (int i =-MatrixSize/2; i<=MatrixSize/2; i++){
    for (int j=-MatrixSize/2; j<=MatrixSize/2; j++){
      //what pixel are we testing
      int xloc = x+i;
      int yloc = y+j;
      int loc = xloc + width*(yloc+j);
      //make sure we haven't walked off our image
      loc = constrain(loc,0,pixels.length-1);
      //calculate the convolution
      sumB += (blue(pixels[loc]) * Matrix[countMatrix]);
      sumG += (green(pixels[loc]) * Matrix[countMatrix]);
      sumR += (red(pixels[loc]) * Matrix[countMatrix]);
      countMatrix++;
    }
  }

  //make sure RGB is withing range
  sumR = constrain(sumR,0,255);
  sumG = constrain(sumG,0,255);
  sumB = constrain(sumB,0,255);

  //return the resulting color
  return color(sumR,sumG,sumB);
}

public int convolution(int x, int y, float[][] matrix, int matrixsize, PImage img)
{
  float rtotal = 0.0f;
  float gtotal = 0.0f;
  float btotal = 0.0f;
  int offset = matrixsize / 2;
  for (int i = 0; i < matrixsize; i++) {
    for (int j= 0; j < matrixsize; j++) {
      // What pixel are we testing
      int xloc = x+i-offset;
      int yloc = y+j-offset;
      int loc = xloc + img.width*yloc;
      // Make sure we haven't walked off our image, we could do better here
      loc = constrain(loc, 0, img.pixels.length-1);
      // Calculate the convolution
      rtotal += (red(img.pixels[loc]) * matrix[i][j]);
      gtotal += (green(img.pixels[loc]) * matrix[i][j]);
      btotal += (blue(img.pixels[loc]) * matrix[i][j]);
    }
  }
  // Make sure RGB is within range
  rtotal = constrain(rtotal, 0, 255);
  gtotal = constrain(gtotal, 0, 255);
  btotal = constrain(btotal, 0, 255);
  // Return the resulting color
  return color(rtotal, gtotal, btotal);
}
 public float cBlur(int x, int y, float[] Matrix, PGraphics iGraph){ 

  float sum = 0;  
  
  int MatrixSize = PApplet.parseInt(sqrt(Matrix.length)); //is our matrix 3x3, 5x5, etc.?
  int countMatrix = 0; //to keep track of where we are in convolution matrix

  for (int i =-MatrixSize/2; i<=MatrixSize/2; i++){
    for (int j=-MatrixSize/2; j<=MatrixSize/2; j++){
      //what pixel are we testing
      int xloc = x+i;
      int yloc = y+j;
      int loc = xloc + iGraph.width*(yloc+j);     
      loc = constrain(loc,0, iGraph.pixels.length-1); //make sure we haven't walked off our image
      sum += (brightness(iGraph.pixels[loc]) * Matrix[countMatrix]);
      countMatrix++;
    }
  }
  
  //return the resulting color
  return sum;
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
// initialize all of the dimensions

float os;
float xA,xB,xC,xD,xE,xF,xG;
float yA,yB,yC,yD,yE,yF,yG;
float cA,cB,cC,cD;
float rA,rB,rC,rD,rE;
int tWidth;
int y10;
float row0,row1,row2,row3,row4,row5, row6, row7, row8, row9,row10;
float col1,col2,col3,col4,col5,col6,col7,col8,col9,col10;
float cellWidth;
public void setConst(){

os = width/64;

  cellWidth = (width/2 - os-os/2)/6;

 xA = os;
 xB = width/4 - os;
 xC = width/4;
 xD = width/2;
 xE = width/2 + os;

 xG = width*5/6;




 y10 = PApplet.parseInt(height/16+width/64+ width/4 - width/32); // revisit this value .

//
 yA = height/32; // rowA



 yB = yA + os;
 yC = yB + xB-os;
 yD = yC + os;
 yE = yD + (xB-os)/3;
 yF = height;
 yG = height/5;
 cA = xC/2;
 cB = xC + (xD-xC)/2;
 rC = yC + os*2/3;
 rD = yE + os*2/3;
 rA = yA/2;

 tWidth = PApplet.parseInt((width/4 - width/32)/3); // width of a thumb
 row0 = os;
 row1 = os + width/4 - width/32;
 row2 = height/32+ width/4 - width/64 + os ;
 row3 = row2 + os;
 row4 = row3 + tWidth;
 row5 = row4 + os;
 row6 = row5 + tWidth/2;
 row7 = row6 + os;
 row8 = row7 + cellWidth;
 row9 = row8;
 row10 = height - 2*os;

  col2 = width/4 -os;
  col3 = col2 +os;
  col4 = (width - os)/2;
  col5 = col4+os;
  col6 = col4;
  col7 = col4;
  col8 = width*5/6 + os/2;
  col10 = width-os;

}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#CCCCCC", "TBM_GENERATOR" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
