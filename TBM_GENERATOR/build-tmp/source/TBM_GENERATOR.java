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








ControlP5 cp5;

public Controller c;


// variables for 3D printer
public float layerHeight = .030f; // cm
public float voxW = 0.040f; // cm
public float voxH = 0.040f; // cm

public float DimXY = 10;
// public float DimY = 10;
public float DimZ = 3;

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


int LayersY =1;
int LayersX =1;
int LayersZ =1;


//
PImage exportDepth;

// PUBLIC VARIABLES FOR LAYOUT
boolean isSetup = true;



public void setup() {

  println("setup started");
  float time01 = PApplet.parseFloat(millis());


  init();

  float time02 = PApplet.parseFloat(millis());
  float elapsedTime = time02 - time01;
  println("setup completed after " + elapsedTime/1000 + " s.");

}


public void draw() {
  background(2, 7, 49);
  c.update(
    );
}


public void settings(){
  size(1800,1000,P3D);

   // fullScreen(P3D);
}
public void setStyle(){
  smooth();
  // background(0, 51, 102);
  ortho();


}

public void init(){

  setStyle();
  setConst();
  initControl();
  setInterface(); // these are all of the buttons ect.
  initViewport();
  isSetup = false;
  initGeo();
}



public void initControl(){
  c = new Controller(this); // initialize controller
  c.m.vox.thickness = Thickness;
  c.m.vox.layer = Current;
  c.m.vox.update();
}

public void initViewport(){
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
  DimXY = value;
  thread("RESETUNITCELL");
  }
}


// void DimY(float value){
//   if (!isSetup){
//   DimY = value;
//   thread("RESETUNITCELL");
//   }
// }


public void DimZ(float value){
  if (!isSetup){
  DimZ = value;
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

public void R2() {
 if (c.v.vp3D.mode == "UNIT"){ c.m.currentThumb.setChildren(1);}
}

public void L2(){
if (c.v.vp3D.mode == "UNIT"){c.m.currentThumb.setChildren(-1);}
}


public void togglecell() {
  if (!isSetup){
    c.toggleMode();
    thread("RESETPOINTCLOUD");
  }
}


public void checkBox(float[] a) {
  println(a); // these are the toggles
  c.m.points = c.m.resetPC(3, a); // we will have to multi thread this bitch
}


public void Export() {

  if (!isSetup){
    exportDepth = c.m.vox.depth.texture.get();
    export();
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


public void initButtons(){

  int bW = PApplet.parseInt((width/4 - width/32)/4);
  int bH = PApplet.parseInt(height/16 - os);


  cp5.addToggle("Invert")
     .setPosition(2*os, row1 + os)
     .setSize(PApplet.parseInt(os), PApplet.parseInt(os/4))
     .setValue(true)
     // .setMode(ControlP5.SWITCH)
     ;


  cp5.addToggle("SpinX")
     .setPosition(width/4+os, row5-2*os)
     .setSize(PApplet.parseInt(os), PApplet.parseInt(os/4))
     .setValue(true)
     // .setMode(ControlP5.SWITCH)
     ;

  cp5.addToggle("SpinY")
     .setPosition(width/4 + 3*os, row5-2*os)
     .setSize(PApplet.parseInt(os), PApplet.parseInt(os/4))
     .setValue(true)
     // .setMode(ControlP5.SWITCH)
     ;

  cp5.addToggle("SpinZ")
     .setPosition(width/4 + 5*os, row5-2*os)
     .setSize(PApplet.parseInt(os), PApplet.parseInt(os/4))
     .setValue(true)
     // .setMode(ControlP5.SWITCH)
     ;


       cp5.addToggle("toggleCell")
     .setPosition(width/2 + os, yA+os/2)
     .setSize(PApplet.parseInt(os), PApplet.parseInt(os/4))
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
  // cp5.addButton("TOP")
  //   .setValue(0)



  int y4 = PApplet.parseInt(height/16+width/64+ width/4 - width/32);

  // cp5.addButton("R1")
  //   .setValue(0)
  //   .setPosition(width/4-os, y4)
  //   .setSize(int(os), int(tWidth))
  //   ;

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

  // cp5.addButton("L1")
  //   .setValue(0)
  //   .setPosition(0, y4)
  //   .setSize(int(os), int(tWidth))
  //   ;

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


public void setInterface(){

  cp5 = new ControlP5(this);

  initButtons();
  initSlider();

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
    .setValue(10)
    .setRange(1, 50) // mm?
    ;

  // cp5.addSlider("DimY")
  //   .setPosition(xE, row5+ 50)
  //   .setWidth(len)
  //   .setValue(10)
  //   .setRange(1, 50) // mm
  //   ;

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

    c.m.vox.layer = value;    //l: set vox");
    c.m.vox.update();         //
    c.v.vp3D.setCurrentLayer(c.m.vox.pcLayer);
    // c.v.setCurrent();
  }
}


public void Thickness(float value) {

  c.m.vox.thickness = value;
  c.v.thickness = value;
  c.m.vox.update();
}


public void dropdown(int n) {

  CColor c = new CColor();
  c.setBackground(color(255, 0, 0));
  cp5.get(ScrollableList.class, "dropdown").getItem(n).put("color", c);
}



public void RESETUNITCELL() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM


  int voxX = PApplet.parseInt(DimXY/0.040f); //  num voxels in X
  int voxY = PApplet.parseInt(DimXY/0.040f); //  num voxels in Y
  int voxZ = PApplet.parseInt(DimZ/0.030f); //  num voxels in Z

  // int voxX = int(10/0.040); //  num voxels in X
  // int voxY = int(10/0.040); //  num voxels in Y
  // int voxZ = int(1/0.030); //  num voxels in Z


  float inter = Intersection;



  println("reset unit cell" + voxX + "," + voxY + "," + voxZ);
  println("intersection = " + inter);

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
  boxCloud.strokeWeight(1);

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

    float rangeLo = 255*inter;
    float rangeHi = 255 - rangeLo;

    int levels = LayersZ;

    float layerVoxels = voxZ/levels;
    boolean invert = false;


  for (int z = 0; z < levels; z++){
    for (int x = 0; x < depthChannel.width; x += res) {
      for (int y = 0; y < depthChannel.height; y+= res) {
        float alph = brightness((alphaChannel.get(x,y)));

        if (alph > 10){
           float val = brightness(depthChannel.get(x, y));
           int c = materChannel.get(x,y);

           if (val > rangeHi )  { val = rangeHi;    }
           if (val < rangeLo) { val = rangeLo;  }

           if (invert){val = 255-val;}

           float voxLevel = (val-rangeLo)/(rangeHi-rangeLo)*layerVoxels; // height of voxel within this level

          boxCloud.stroke(red(c), green(c), blue(c),200);
           boxCloud.vertex(x-voxX/2, y-voxY/2, voxLevel + z*layerVoxels - voxZ/2);
          }
        }
      }

      invert = !invert; // need to do something hear to invert
    }


  boxCloud.endShape();
  c.v.vp3D.setCellUnit(boxCloud);




  // PShape rdmCloud = createShape();
  // rdmCloud.beginShape(POINTS);
  // rdmCloud.stroke(255,150);
  // for (int i = 0; i < 1000; i++) {rdmCloud.vertex(random(-200,200),random(-200,200),random(-200,200));}
  // rdmCloud.endShape();
  c.v.vp3Darray.setCellArray(boxCloud);
  c.setCurrentUC(boxCloud);

}

public void RESETARRAY() {


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
  public void display(){

    if (display){
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

    grow = checkExtents(p1,p2);
  }




}
class CellArray  {












}
class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  PApplet app;

  // can get rid of this ?
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<UnitCell> unitCells = new ArrayList<UnitCell>();

  Viewport2D vp;

  Zone zoneA, zoneB, zoneC;  // select zones
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

    zoneA = new Zone(xA, yD-os, xB, yE-os);                 // row 1


    // zoneB = new Zone(xA, yE, xB, yE+tWidth);                // row 2

    zoneB = new Zone(xA, row5, col4, yE+tWidth);                // row 2
    zoneC = new Zone(xA, yE+tWidth+os, col4, yE+2*tWidth+os); // row 3

    zones.add(zoneA);
    zones.add(zoneB);
    zones.add(zoneC);

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

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    PShape shape = null;
    float cellWidth = (width/2 - os-os/2)/6;

    for (float x = os; x < width/2 - dimThumb.x; x += cellWidth){
      unitCells.add(new UnitCell(new PVector(x,row8), new PVector(cellWidth,cellWidth), shape ));
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


  public void initViewport(PVector _loc, PVector _size, Thumb _th){
    vp = new Viewport2D(_loc, _size, _th);
  }


  public void update() {
    PVector mouse = new PVector(mouseX, mouseY);
    v.display();
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

  return regen;
}

public void checkR2(Thumb th,  PVector ms){
        m.currentR2 = th.checkSelectedChildren();
        vp.set(m.currentR2);
}

public void checkR3(PVector _ms){

  for (UnitCell cell : unitCells){
    if (cell.checkSelected(_ms)){
      thread("RESETUNITCELL");
    }
  }

}


public boolean unitSelect(PVector ms){

  boolean regen = true;

    if (zoneA.isSelected(ms)){
        regen = checkR1(m.thumbs, ms);
    } else if (zoneB.isSelected(ms)){
        if (m.currentR1 != null){checkR2(m.currentR1, ms);}
    } else if (zoneC.isSelected(ms)) {
       checkR3(ms);
    }
    return regen;
  }

  public void mouseDown(PVector ms) {

    if (v.vp3D.mode == "UNIT"){
      if (unitSelect(ms)){
        thread("RESETUNITCELL");
      }
      // thread("RESETUNITCELL");
    }
  }
}


class ImageThread implements Runnable {


  Thread thread;
  boolean drawNew = false;
  boolean isReady = false;
  // Specimen specimenNew;
  int pauseTime;
  String filepath;
  PImage parent, texture;
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
      // parent = normalize(parent);
      texture = parent.get();
       texture.resize(x,y);
      map = createGraphics(x,y);
      map.beginDraw();
      map.image(texture, 0, 0);
      map.endDraw();


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


}
class Model {

  // this class is all about modelling the 3D Geometry

  PImage slice;
  PGraphics pg;
  PointCloud pointCloud;
  Shape shaper;
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
    alpha = new Thumb(app, "/textures/array/alpha/solid.png",    new PVector(os + dimThumb.x*2,row3),  dimThumb, "unit/alpha","MONO");

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

    PVector dimGlobe = new PVector(width/6-os-os/2, width/6-os-os/2);

     // depthGlb = new Thumb(app,"/textures/global/depth/blank.png",   new PVector(os,y4),   dimThumb, "global/depth");
     // materGlb = new Thumb(app,"/textures/global/mater/blank.png", new PVector(os+tW,y4),   dimThumb, "global/mater");
     // alphaGlb = new Thumb(app,"/textures/global/alpha/blank.png",new PVector(width/2+ width/6+os, row5),  dimGlobe, "global/alpha");
     shaper = new Shape(app,"/textures/global/alpha/blank.png",new PVector(width/2+ width/6+os, row5),  dimGlobe);
    // thumbsGlobal.add(depthGlb);
    // thumbsGlobal.add(materGlb);
    thumbsGlobal.add(alphaGlb);

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
      vox = new Voxelator(_loc, _size,  _thumbs, _arrays, _globes);

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
class PointCloud {

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc, currentLayerPC;
  PVector p1, p2;

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

  xScal = 1;
    xRot = -atan(sin(radians(45)));
    yRot =  radians(45);
    xPos = width/4 + vWidth/8;
    yPos = yB + vHeight/8 - os;

  }

  public void setMode1(){ // ZOOM IN
    xScal = 3;
    xRot = -atan(sin(radians(45)));
    yRot =  radians(45);
    xPos = width/4 + vWidth/2;
    yPos = vHeight/2-2*os;

  }

  public void initView(){

    PVector start,scl,rot;
      float w = 3*width/8;
      scl = new PVector(0.75f,0.75f,0.75f);
      rot = new PVector(-atan(sin(radians(-30))), radians(-30),0);
      start = new PVector((p2.x-p1.x)/2+p1.x, (p2.y-p1.y)/2 + p1.y, -10);
    float val = 0.1f;
    position = new TweenPoint(start,start,val); // tween for position
    scale = new TweenPoint(scl,scl,val); // tween for position
    rotation = new TweenPoint(rot,rot,val);
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
      // rotation.addIncrement(new PVector(0,-0.005,0));
      // yRot += 0.005;
      //  zRot += 0.005;
    }

    if (bbox != null){
    if (bbox.grow){
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

  public void display(boolean showLayer){

      pushMatrix();
      setView();

      stroke(255,100);
      fill(255,100);

      if (boxCloud != null){ shape(boxCloud);}
      if (bbox != null ){ bbox.display();}

      // strokeWeight(1);
      if (cellArray != null){
        for (BoundingBox cell: cellArray){ cell.display();}
      }

      // if (showLayer){displayLayer();}

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
class Shape {

  ImageThread it;
  boolean loaded = false;
  PImage parent, texture;
  PGraphics map;
  PVector loc,size;
  PApplet app;
  String path;
  boolean isSelected = false;

  Shape(PApplet _app, String _path, PVector _loc, PVector _size) {
    app   =   _app;
    // name  =   _name;
    size  =  _size;
    loc = _loc;
    path = _path;
    // println("thumb path = " + _path);
    reset(_path, _loc);
  }


  public void reset(String _path, PVector _loc){
    println("reset: " + _path + "," + size);
    it = new ImageThread(app, _path,size);
    it.start();
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
  PImage texture, parent;
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

  public void reset(String _path, PVector _loc){
 // /("reset: " + _path + "," + size);
    it = new ImageThread(app, _path,size);
    it.start();

  }


  public void resetChild(Thumb _child){

    parent = _child.parent.get();
    texture = parent.get();
    texture.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));

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
    int col = 0;
    int b1 = scroll;
    int b2 = 3+scroll;
    b2 = 6;

    if (b2 > items) {
      b2 = items;

    }

    // PVector childSize = new PVector((width/2 - os-os/2)/6,(width/2 - os-os/2)/6);

    PVector childSize = new PVector(size.x/2,size.x/2);


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


  public Thumb checkSelectedChildren() {

   Thumb chi = null;

   for (Thumb child: children){
      if ((mouseX > child.loc.x) && (mouseX < (child.loc.x + map.width)) && (mouseY > child.loc.y) && (mouseY < (child.loc.y + map.height))) {
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
      rect(loc.x, loc.y, map.width, map.height); // Left

  }
  public void display() {



        // println("isnt ready:" + isMater + " " + name);
        // println("fsaddfdasgsad = " + name + " " + fsaddfdasgsad);

    if (loaded == false){
      if (it.isReady){
        // println("it is ready");

        if (mode == "COLOR"){
          println("multi-material");
          parent = it.parent;    //
          texture = it.texture;  // We will make another function for multi material
          map = it.map;

        }else{
          println("normalize");
          parent = it.parent;//
          texture = normalize(it.texture);//
          map = it.map;
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
  PShape pc;
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

    // vp3Darray = new Viewport3D(width/2+os/2,xA,width/6 +os,yF);

 // vp3Darray = new Viewport3D(width/4,height/4,width/2,height/2);

  }

  public void setCurrent() {
    println("set current");
    vox.getCurrentPC();
  }

  public void display() {

    String mode = vp3D.mode;
    display2D();
    display3D();

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


    // ROW 1-2

    line(0, yA, width, yA);

    line(0, row1, col3, row1);
    line(0, row2, col3, row2);
    line(0, row3, col3, row3);

    // ROW 3-4
    line(0,row4, width, row4);
    line(0,row5, width, row5);

    // ROW 5-6
    // line(0,y10+ tWidth, width/2, y10 + tWidth);
    // line(0,y10+tWidth, width, y10+tWidth);
    // line(0,y10+ tWidth+os, width, y10 + tWidth+os);

    // ROW 7-8
    line(0,row7 , width/2-os/2,row7 );
    // line(0,y10 + 2*(tWidth)+2*os, width/2, y10 + 2*(tWidth)+2*os);


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
    if (thumb.texture != null){
      image = thumb.parent.get();
      image.resize(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
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

  BoundingBox bbox;

  float currentLayer = 0.5f;

  PointCloud cellUnit, cellArray;

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  float vWidth, vHeight;

  String mode = "UNIT"; // set as "UNIT" or "GLOBAL"

  Viewport3D(float x1, float y1, float x2, float y2){
    cellUnit = new PointCloud(x1,y1,x2,y2, false);
    cellArray = new PointCloud(x1,y1,x2,y2, true);

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

cellUnit.display(true);

  strokeWeight(1);
  stroke(34,155,215);
  // point(cellUnit.p1.x, cellUnit.p1.y);
  // point(cellUnit.p2.x, cellUnit.p2.y);
  strokeWeight(1);

}
  //   if (mode == "UNIT"){
  //       cellUnit.display(true);
  //       // cellArray.display(false);
  //     } else {

  //       cellUnit.display(false);
  //       // cellArray.display(true);
  //   }

  // }




}
class Voxelator {

  // PImage layer;
  PVector loc, size;
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> arrays = new ArrayList<Thumb>();
  ArrayList<Thumb> globes = new ArrayList<Thumb>();
  ArrayList<PVector> pcLayer = new ArrayList<PVector>();
  PGraphics pg;
  float layer, thickness;

  float dimX, dimY;

  Thumb depth, alpha, mater;
  Thumb depthArray, alphaArray, materArray;
  Thumb depthGlobe, alphaGlobe, materGlobe;

  boolean loaded = false;

  String mode = "UNIT";

  Voxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

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

    // setArrays(arrays);

    // depthGlobe = globes.get(0);
    // materGlobe = globes.get(1);
    // alphaGlobe = globes.get(2);

    layer = Current;
    thickness = Thickness;
    // update();

  }


  // void getCurrentPC(){


  // }
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

  // ArrayList getPC(){

   public void toggleMode(){
    if (mode == "UNIT"){
      mode = "ARRAY";
    } else {
      mode = "UNIT";
    }
    update();
   }

  // }

  public void exportStack(){
    export();
  }

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

    // println("update Vox");

    if (mode == "ARRAY"){
      updateArray();
    } else {
      updateUnit();
    }

  }



  public void updateUnit(){

    if (depth.texture != null){

    PImage img = depth.texture.get();
    img.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));

    boolean invertLayer;

    int res = 1;
    float range = 255;
    int numLevels = LayersZ;

    float domain = range;
    // float amp = Amplitude/levels; // this is the total height
    float totalSlices = 255;
    float subDomain = totalSlices/numLevels;

    int currentSlice = PApplet.parseInt(totalSlices*layer); // current slice
    int currentStep = floor(currentSlice/subDomain);
    float currentLayer = (currentSlice%subDomain)/subDomain*255;

    // println("Update Vox, currentLayer = " + currentLayer);

    if (currentStep%2 == 0){
      invertLayer = false;
    } else {
      invertLayer = true;
    }


    pg = createGraphics(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      float val = brightness(img.pixels[k]);
    if ((val < (currentLayer+thickness)) && (val > (currentLayer-thickness))) { pg.pixels[k] = color(255);}
    }

    pg.updatePixels();
    pg.endDraw();
    pcLayer = getCurrentPC(); // this displays the current cut layer in 3D
  }
  }


  public void updateArray(){

    println("get depth texture");
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

    if (loaded){
      image(pg, PApplet.parseInt(loc.x) , PApplet.parseInt(loc.y) );

    } else if (depth.texture != null){
      updateUnit();
      loaded = true;
    }
  }


}
//   void getLayer(float layer){ // express layer as a percentage of total




//     String s = "Image Stack: " + t;


//     // textAlign(CENTER);
//     // text(s, 340, 965);
//   }






//   }





//   void export(String path, PVector dim){



//  }

// }
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

    stroke(255);


    if (isSelected(pt)){
      rect(a.x,a.y,dim.x,dim.y);
    }

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
//
//public float getImgSD(PImage img) { 
//  // returns the standard deviation of an image 
//  float brightness = 0;  
//  float sum = 0;  
//  float average = 1;  
//  float variation = 0;
//  float avgVar;
//  float SD; 
//  img.loadPixels();
//  for (int i = 0; i < img.pixels.length; i++) {
//    float val = brightness(img.pixels[i]); 
//    brightness +=  val;
//  }
//  average = brightness/img.pixels.length; 
//  for (int i = 0; i < img.pixels.length; i++) {
//    float val = brightness(img.pixels[i]);  
//    variation += (val - average)*(val - average);
//  }  
//  avgVar = variation/img.pixels.length; 
//  SD = sqrt(avgVar);
//  return SD;
//}
//
//PImage getPImage() { 
//  String tempPath = "tempFrame.jpg";  
//  saveFrame("tempFrame.jpg");     
//  PImage temp = loadImage(tempPath);
//return temp;
//} 
//
//
//
//float CalcAvg(PImage p) {
// p.loadPixels(); 
//  float sum =0;
//  for (int i = 0; i < p.pixels.length; i++) {
//    float val = brightness(p.pixels[i]); 
//    sum += val;
//  }
//  float average = sum/p.pixels.length; 
//  return average;
//} 
//
//float CalcAvgPixel(FloatList pixelList) { 
//  float sum =0;
//  for (float p : pixelList) { 
//    float val = brightness(int(p)); 
//    sum += val;
//  }
//  float average = sum/(pixelList.size()); 
//  return average;
//} 
//
//float CalcStdevPixel(float average, FloatList pixelList) { 
//  
//  float variation = 0; 
//  
//  for (float p : pixelList) { 
//    float val = brightness(int(p)); 
//    variation += (val - average)*(val - average);
//  
//  }  
//  
//  float avgVariation = variation/pixelList.size(); 
//  float stdDev = sqrt(avgVariation);  
//  
//  // println("standard deviation = " + stdDev); 
//  
//  return stdDev;
//
//} 
//
//
//
//float CalcStdev(float average) { 
//
//  float variation = 0; 
//  
//  for (int i = 0; i < pixels.length; i++) {
//    float val = brightness(pixels[i]); 
//    variation += (val - average)*(val - average);
//  
//  }  
//  
//  float avgVariation = variation/pixels.length; 
//  float stdDev = sqrt(avgVariation);  
//  
//  return stdDev;
//
//} 
//
//public int getBin(float val, ArrayList<Bin> bins) {
//
//  int bin =0;
//  float step = 255.0/bins.size(); 
//
//  for (int k = 0; k < bins.size (); k ++) {
//    int r = bins.size()-k;
//    float threshold = float(r)*step;
//    if (val > threshold) return r;
//  }
//
//  return bin;
//
//}
// initialize all of the dimensions

float os;
float xA,xB,xC,xD,xE,xF,xG;
float yA,yB,yC,yD,yE,yF,yG;
float cA,cB,cC,cD;
float rA,rB,rC,rD,rE;
int tWidth;
int y10;
float row1,row2,row3,row4,row5, row6, row7, row8;
float col1,col2,col3,col4;

public void setConst(){
os = width/64;

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

 row1 = os + width/4 - width/32;
 row2 = height/32+ width/4 - width/64 + os ;
 row3 = row2 + os;
 row4 = row3 + tWidth;
 row5 = row4 + os;
 row6 = row5;
 row7 = height/16+width/64+ width/4 - width/32 + 2*(tWidth);
 row8 = row7 + os;

 col2 = width/4 -os;
 col3 = col2 +os;
 col4 = (width - os)/2;

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
