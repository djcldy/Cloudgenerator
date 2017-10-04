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








Controller c;
ControlP5 cp5;

// stuff for interface
Slider abc;
CheckBox checkbox;


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
  ortho();
  
  background(0, 51, 102);
  // fullScreen(P3D);
  

  init();

}

public void initControl(){
  c = new Controller(); // initialize controller
  c.m.vox.thickness = Thickness;
  c.m.vox.layer = Current;
  c.m.vox.update();
}

public void initViewport(){
  c.v.vp3D.setCellArray(c.m.points); // ?
}

public void draw() {
  background(2, 7, 49);
  c.update();
}

public void init(){
  setConst();
  initControl();
  setInterface();
  initViewport();
  isSetup = false;
  initGeo();
}

public void initGeo(){
  thread("RESETUNITCELL"); // can we make this more efficient
}

public void LayersZ(int value){
  if (!isSetup){
  LayersZ = value;
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
  // toggle
  //println("toggle cell");
  if (!isSetup){
    c.toggleMode();
    thread("RESETPOINTCLOUD");
  }
}


public void checkBox(float[] a) {
  //println(a); // these are the toggles
  c.m.points = c.m.resetPC(3, a); // we will have to multi thread this bitch
}


public void Export() {

  exportDepth = c.m.vox.depth.texture.get();
  if (!isSetup){export();}
}

public void export() {

  int layers = 200;
  PVector dim = new PVector(500, 500);
  PImage img = exportDepth;
  img.resize(PApplet.parseInt(dim.x), PApplet.parseInt(dim.y));


  for (int j = 0; j < 255; j++) {

    //println("export layer: " + l);

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
  //println(theEvent.getArrayValue());
}



public void initButtons(){

  int bW = PApplet.parseInt((width/4 - width/32)/4);
  int bH = PApplet.parseInt(height/16 - os);

  cp5.addToggle("togglecell")
     .setPosition(xD,yA)
     .setSize(PApplet.parseInt(width/6-os),75)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;

  cp5.addButton("Export")
    .setValue(0)
    .setPosition(xD, height-height/16)
    .setSize(PApplet.parseInt(width/6-os), PApplet.parseInt(height/16-os))
    ;

  cp5.addButton("ADD")
    .setValue(0)
    .setPosition(os, height-height/16)
    .setSize(bW, bH)
    ;

  cp5.addButton("SUBTRACT")
    .setValue(0)
    .setPosition(os+bW, height-height/16)
    .setSize(bW, bH)
    ;

  cp5.addButton("MULTIPLY")
    .setValue(0)
    .setPosition(os+2*bW, height-height/16)
    .setSize(bW, bH)
    ;

  cp5.addButton("DIVIDE")
    .setValue(0)
    .setPosition(os+3*bW, height-height/16)
    .setSize(bW, bH)
    ;



  int y4 = PApplet.parseInt(height/16+width/64+ width/4 - width/32);

  cp5.addButton("R1")
    .setValue(0)
    .setPosition(width/4-os, y4)
    .setSize(PApplet.parseInt(os), PApplet.parseInt(tWidth))
    ;

  cp5.addButton("R2")
    .setValue(0)
    .setPosition(width/4-os, y4 + tWidth + os)
    .setSize(PApplet.parseInt(os), PApplet.parseInt(tWidth))
    ;

  cp5.addButton("R3")
    .setValue(0)
    .setPosition(width/4-os, y4 + 2*(tWidth + os))
    .setSize(PApplet.parseInt(os), PApplet.parseInt(tWidth))
    ;

  cp5.addButton("L1")
    .setValue(0)
    .setPosition(0, y4)
    .setSize(PApplet.parseInt(os), PApplet.parseInt(tWidth))
    ;

  cp5.addButton("L2")
    .setValue(0)
    .setPosition(0, y4 + tWidth + os)
    .setSize(PApplet.parseInt(os), PApplet.parseInt(tWidth))
    ;

  cp5.addButton("L3")
    .setValue(0)
    .setPosition(0, y4 + 2*(tWidth + os))
    .setSize(PApplet.parseInt(os), PApplet.parseInt(tWidth))
    ;

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
    println("adjustGrid");
    c.m.depthArray = c.m.depth.array(LayersX,LayersY);
    c.m.alphaArray = c.m.alpha.array(LayersX,LayersY);
    c.m.materArray = c.m.mater.array(LayersX,LayersY);
    c.m.updateArray();
}
public void initSlider() {

  int len = 200;

  cp5.addSlider("Amplitude")
    .setPosition(xD, yG+ 50)
    .setWidth(len)
    .setValue(Amplitude)
    .setRange(1, 500) // cm
    ;

  cp5.addSlider("Thickness")
    .setPosition(xD, yG+75)
    .setWidth(len)
    .setRange(1, 100)
    .setValue(Thickness)
    ;

  cp5.addSlider("Current")
    .setPosition(xD, yG+100)
    .setWidth(len)
    .setRange(0, 1) // values can range from big to small as well
    .setValue(0.5f)
    // .setNumberOfTickMarks(255)
    .setSliderMode(Slider.FLEXIBLE)
    ;


    cp5.addSlider("LayersX")
     .setPosition(xD,yG + 125)
     .setWidth(len)
     .setRange(1,10) // values can range from big to small as well
     .setValue(2)
     .setNumberOfTickMarks(10)
     .setSliderMode(Slider.FLEXIBLE)
     ;



    cp5.addSlider("LayersY")
     .setPosition(xD,yG + 150)
     .setWidth(len)
     .setRange(1,10) // values can range from big to small as well
     .setValue(2)
     .setNumberOfTickMarks(10)
     .setSliderMode(Slider.FLEXIBLE)
     ;


    cp5.addSlider("LayersZ")
     .setPosition(xD,yG + 175)
     .setWidth(len)
     .setRange(1,10) // values can range from big to small as well
     .setValue(LayersZ)
     .setNumberOfTickMarks(10)
     .setSliderMode(Slider.FLEXIBLE)
     ;

}

public void Current(float value) {
  if (!isSetup){  //

    c.m.vox.layer = value*255;    //l: set vox");
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


  PShape boxCloud = createShape();
  boxCloud.beginShape(POINTS);
  boxCloud.stroke(255);

    ArrayList<PVector> temp = new ArrayList<PVector>();
    Thumb depth,alpha, mater; // place holder variables

    depth = c.m.depth;
    alpha = c.m.alpha;
    mater = c.m.mater;

    depth.map.loadPixels();
    alpha.map.loadPixels();
    mater.map.loadPixels();

    println("depth map size = " + depth.map.width + "," + depth.map.height);

    int res = 1;
    float range = 255;


    int levels = LayersZ;
    float amp = Amplitude/levels; // this is the total height
    boolean invert = false;


  println("resetting unit-cell");
  println("layers = " + levels);
  println("amp = " + amp);
  println("total height = " + levels*amp);


  for (int z = 0; z < levels; z++){
    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {
        float alp = brightness((alpha.map.get(x,y)));

        // if (alp > 10) { // check alpha

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
    c.v.vp3D.setCellUnit(boxCloud);

}







public void RESETARRAY() {


    println("resetting array");


    ArrayList<PVector> temp = new ArrayList<PVector>();
    Thumb depth,alpha, mater, alphaGlobe; // place holder variables

    depth = c.m.depthArray;
    alpha = c.m.alphaArray;
    mater = c.m.materArray;

    alphaGlobe = c.m.alphaGlb;

    depth.map.loadPixels();
    alpha.map.loadPixels();
    mater.map.loadPixels();
    alphaGlobe.map.loadPixels();


    int res = 5;
    float range = 255;


    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {

        float alp = brightness((alpha.map.get(x,y)));
        float alp2 = brightness((alphaGlobe.map.get(x,y)));
        if (random(0,1)>0.99f){println("alph: " + alp2);}

        if ((alp2 > 25)) { // check alpha (both of them)
           float val = brightness(depth.map.get(x, y));
           temp.add(new PVector(x, y, val));
          }
        }
      }

    // c.m.points = temp; // does this mater?
    c.v.vp3D.setCellArray(temp);
}

public void RESETPOINTCLOUD() {

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

    //   //println("generate micro-structures");

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
class BoundingBox{

  // bounding box

  boolean display = true;
  PVector pMin, pMax;
  float boxWidth, boxHeight, boxDepth;

  BoundingBox(float xMin,  float yMin, float zMin, float xMax, float yMax, float zMax){

    pMin = new PVector(xMin, yMin, zMin);
    pMax = new PVector(xMax, yMax, zMax);

    boxWidth = xMax - xMin;
    boxHeight = yMax - yMin;
    boxDepth = zMax - zMin;

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

  }




}
class CellArray  {












}
class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  // can get rid of this ?
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  Viewport2D vp;

  Zone zoneA, zoneB, zoneC;  // select zones
  ArrayList<Zone> zones = new ArrayList<Zone>();

  Controller() {

    //Println("initialize controller");
    initSelector();
    init();

     m = new Model();
     m.currentThumb = thumbs.get(1);
     v = new View(m,vp);
     vp.set(m.depthArray);
     vp.m = m; // clean this up later

  }

  public void initSelector(){

    zoneA = new Zone(xA, yD-os, xB, yE-os);                 // row 1
    zoneB = new Zone(xA, yE, xB, yE+tWidth);                // row 2
    zoneC = new Zone(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row 3

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

    Thumb depth = new Thumb("/textures/array/depth/bubble.png",   new PVector(os,y4),   dimThumb, "unit/depth");
    Thumb mater = new Thumb("/textures/array/mater/manta.png", new PVector(os+tW,y4),   dimThumb, "unit/mater");
    Thumb alpha = new Thumb("/textures/array/alpha/solid.png",    new PVector(os+2*tW,y4),  dimThumb, "unit/alpha");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    mater.isSelected = true;



    initViewport(new PVector(os, yA),  dimThumbView,  mater);

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
  //Println("current thumb = " + m.currentThumb.name);
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

  // toggle zoneB
  boolean toggle = false;

  for (Thumb th : _thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
        vp.set(m.currentThumb);
        toggle = true;
      }
    }

    // if (toggle) {
    //   for (Thumb th : _thumbs) {
    //    if (th != m.currentThumb){
    //     th.isSelected = false;
    //    }

    //   }
    // }
  }


public void checkR1(ArrayList<Thumb> _thumbs,  PVector ms){

  for (Thumb th : _thumbs) {
      if (th.checkSelected(ms)) {
        m.currentR1 = th;
        vp.set(th);
      }
  }
}

public void checkR2(Thumb th,  PVector ms){
        m.currentR2 = th.checkSelectedChildren();
        vp.set(m.currentR2);
}

public void checkR3(ArrayList<Thumb> _thumbs,  PVector ms){


  for (Thumb th : _thumbs) {
      if (th.checkSelected(ms)) {
        m.currentR3 = th;
        vp.set(th);
      }
  }
}


public void unitSelect(PVector ms){
    if (zoneA.isSelected(ms)){
        checkR1(m.thumbs, ms);
      } else if (zoneB.isSelected(ms)){
        if (m.currentR1 != null){
          checkR2(m.currentR1, ms);
        }
    }
}



public void arraySelect(PVector ms){
      println("array select");
      if (zoneB.isSelected(ms)){
        checkR1(m.thumbsGlobal, ms);
      println("zoneB: ");
      } else if (zoneC.isSelected(ms)){
        println("checkZone");
        if (m.currentR1 != null){
          println("zoneC");
          checkR2(m.currentR1, ms);
        } else {
          println("zoneC = null");
        }
      }
}

  public void mouseDown(PVector ms) {

    if (v.vp3D.mode == "UNIT"){
      unitSelect(ms);
      thread("RESETUNITCELL");
    } else {
      arraySelect(ms);
      thread("RESETARRAY");
    }
}
}
class Model {

  // this class is all about modelling the 3D Geometry

  PImage slice;
  PGraphics pg;
  PointCloud pointCloud;

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

  Model() {

    initChannelThumbs();

    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    depthArray = depth.array(stepX,stepY);
    alphaArray = alpha.array(stepX,stepY);
    materArray = mater.array(stepX,stepY);

    thumbsArray.add(depthArray);
    thumbsArray.add(alphaArray);
    thumbsArray.add(materArray);

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
    float y4 = height/16+width/64+bW;

    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    depth = new Thumb("/textures/array/depth/bubble.png",   new PVector(os,y4),   dimThumb, "unit/depth");
    mater = new Thumb("/textures/array/mater/manta.png", new PVector(os+tW,y4),   dimThumb, "unit/mater");
    alpha = new Thumb("/textures/array/alpha/solid.png",    new PVector(os+2*tW,y4),  dimThumb, "unit/alpha");

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

     depthGlb = new Thumb("/textures/global/depth/blank.png",   new PVector(os,y4),   dimThumb, "global/depth");
     materGlb = new Thumb("/textures/global/mater/blank.png", new PVector(os+tW,y4),   dimThumb, "global/mater");
     alphaGlb = new Thumb("/textures/global/alpha/blank.png",    new PVector(os+2*tW,y4),  dimThumb, "global/alpha");

    thumbsGlobal.add(depthGlb);
    thumbsGlobal.add(materGlb);
    thumbsGlobal.add(alphaGlb);

    materGlb.isSelected = true;
    // currentR1 = materGlb;

  }

  public void initVoxel(ArrayList<Thumb> _units, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

    PVector dimVox = new PVector(width/6-os,width/6-os);
    initVoxelator(new PVector(width*5/6, height-height/16-dimVox.y), dimVox, _units, _arrays, _globes);

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
    testSelection();
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

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  TweenPoint position, rotation, scale;

  float vWidth, vHeight, currentLayer;
  boolean mode; // mode 0 = max , mode 1 = min


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

    if (mode){
      // setMode1();
      start = new PVector(width/4 + vWidth/8, yB + vHeight/8 - os, 0);
      scl = new PVector(1,1,1);
      rot = new PVector(-atan(sin(radians(45))), radians(45),0);

    }else{
      // setMode2();
      start = new PVector(width/4 + vWidth/2, vHeight/2-2*os);
      scl = new PVector(3,3,3);
      rot = new PVector(-atan(sin(radians(45))), radians(45),0);

    }

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
    getBoundingBox(_pc);
  }

   public void updateCamera(){

    if (!mode){
      rotation.addIncrement(new PVector(0.005f,0,0.005f));
      // yRot += 0.005;
      //  zRot += 0.005;
    }

   }

   public boolean isDisplay(float x, float y){
    boolean display = false; // check if point is in viewport
    if ((x > p1.x) && (x < p2.x) && (y > p1.y) && (y< p2.y)) { display = true;}
    return display;
   }

   public void displayLayer(){


    if (currentLayerPC != null){
      strokeWeight(1);
      stroke(255);
      for (PVector p: currentLayerPC){
        point(p.x, p.y, p.z);}
      }
   }

  public void display(boolean showLayer){

      PVector pos = position.get(); // println(position)
      PVector scl = scale.get();
      PVector rot = rotation.get();

      strokeWeight(1);
      stroke(255);

      pushMatrix();
      // translate(-bbox.boxWidth/2, -bbox.boxHeight/2,-bbox.boxDepth/2);
      translate(pos.x, pos.y); //??

      rotateX(rot.x);
      rotateZ(rot.y);

      scale(scl.x);


      drawPoints();

      if (showLayer){displayLayer();}
      if (bbox != null ){ bbox.display();}


      popMatrix();
      updateCamera();

   }

  public void drawCurrent(){


  }

   public void drawPoints(){

      strokeWeight(2);
      stroke(255,50);

      // int res = 2;

      // if (mode){
      //   res = 16;
      // }
        if (boxCloud != null){shape(boxCloud);}

      // for (int i = 0; i < pc.size(); i += res) {

      // PVector p = pc.get(i);

      // float x = p.x;
      // float y = p.y;
      // float z1 = p.z*Amplitude + 1;
      // float z2 = p.z*Amplitude - 1;

      // float scrnX1 = screenX(x, y, z1);
      // float scrnX2 = screenX(x, y, z2);
      // float scrnY1 = screenY(x, y, z1); // top layer
      // float scrnY2 = screenY(x, y, z2); // bottom layer






      // if (isDisplay(scrnX1,scrnY1)) { point(x, y, z1); }
      // if (isDisplay(scrnX2,scrnY2)) { point(x, y, z2); }

      // }

    }

  public void setCurrent(ArrayList<PVector> _pc){ //
    currentLayerPC = _pc; // as percentage of a layer
   }

   public void getBoundingBox(ArrayList<PVector> _pc){

    // println("getBoundingBox:" + _pc.size());

    float xMin = 10;
    float yMin = 10;
    float zMin = 10;
    float xMax = 0;
    float yMax = 0;
    float zMax = 0;


    for (PVector p: _pc) {

      float z1 = p.z*Amplitude+5;
      float z2 = p.z*Amplitude-5;

      if (p.x < xMin){ xMin = p.x; }
      if (p.y < yMin){ yMin = p.y; }
      if (z1 < zMin){ zMin = z1; }
      if (z2 < zMin){ zMin = z2; }

      if (p.x > xMax){ xMax = p.x; }
      if (p.y > yMax){ yMax = p.y; }
      if (z1 > zMax){ zMax = z1; }
      if (z2 > zMax){ zMax = z2; }

    }

    // println("BoundingBox: " + xMin + "," + yMin + "," + zMin+ "," + xMax + "," + yMax + "," + zMax);

    bbox = new BoundingBox(xMin,yMin,zMin,xMax,yMax,zMax);

   }




}
class Shape { 

  Shape(String _path) {

    println("load shape"); 

    String path = "/images/shape/grid.csv";
    Table table = loadTable(path); 

    for (TableRow row : table.rows ()) {
      
      String str1 = row.getString(0); 
      str1 = str1.replaceFirst("\\{", ""); 
      println(str1);
      
      float b = PApplet.parseFloat(row.getString(1));

      println(row.getString(0)+ ","+ row.getString(1)+","+row.getString(2));
    }
  }
}
class Thumb {

  PGraphics map;
  PImage texture, parent;
  String path, name;
  PVector loc, size;
  boolean isSelected = false;
  ArrayList<Thumb> children; // chilrdren
  ArrayList<Thumb> selectedChildren = new ArrayList<Thumb>();

  int scroll = 0;

  Thumb(String _path, PVector _loc, PVector _size, String _name) {
    name = _name;
    size = _size;
    reset(_path, _loc);
    setChildren(0); // start on step 1
  }

  Thumb(String _path, PVector _loc, PVector _size) {
    size = _size;
    reset(_path, _loc);
  }

  Thumb(PImage _texture, PVector _loc, PVector _size){
    size = _size;
    resetArray(_texture,_loc);
  }


  public Thumb array(int stepX, int stepY){

    PGraphics arrayTexture = createGraphics(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
    PImage clone = parent.get();
    clone.resize(PApplet.parseInt(size.x/stepX), PApplet.parseInt(size.y/stepY));

    arrayTexture.beginDraw();

    for (int x =0; x < size.x; x += size.x/stepX){
      for (int y = 0; y < size.y; y += size.y/stepY){
        arrayTexture.image(clone,x,y);
      }
    }
    arrayTexture.endDraw();

    PImage result = arrayTexture.get();
    Thumb arrayThumb = new Thumb(result, loc, size);

    return arrayThumb;
  }


  public void resetArray(PImage _tex, PVector _loc) {

    path = null;
    loc = _loc;
    float x = size.x;
    float y = size.y;

    map = createGraphics(PApplet.parseInt(x), PApplet.parseInt(y));
    map.beginDraw();
    map.background(0);
    map.endDraw();
    texture = _tex; // textures 512 px x 512 px
    parent = texture.get();

    updateMap();
  }


  public void reset(String _path, PVector _loc) {

    path = _path;
    loc = _loc;
    float x = size.x;
    float y = size.y;

    map = createGraphics(PApplet.parseInt(x), PApplet.parseInt(y));
    map.beginDraw();
    map.background(0);
    map.endDraw();

    PImage xx = loadImage(path); // textures 512 px x 512 px

    texture = normalize(xx);

    println(" normalize");

    println(" size = " + texture.width + "," + texture.height);
    println(" size = " + size.x + "," + size.y);

    if (texture == null) { println("texture is null");}
    if (texture == null) { println("parent is null");}

    parent = texture.get();

    updateMap();
  }

  public void updateMap() {
    texture.resize(0, PApplet.parseInt(size.x));
    map.beginDraw();
    map.image(texture, 0, 0);
    map.endDraw();
  }




  public void  setChildren(int step) {

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

    if (b2 > items) {
      b2 = items;

    }


    if (filenames !=null) {
      for (int j = 0; j < b2; j++){
        // children.add(new Thumb(localPath + filenames[j], new PVector(xA+size.x*col,yE+size.x*row), size));
        children.add(new Thumb(localPath + filenames[j], new PVector(os+size.x*col,loc.y+size.x+os), size));
        col ++;
        if (col == 3) {
          col = 0;
          row++;
        }
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

    println("normalize image xxxxxxxxx");
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
        reset(child.path, loc);

        if (!selectedChildren.contains(child)){
          selectedChildren.add(child);
        }

      } else {
        child.isSelected = false;
        selectedChildren.remove(child);
      }
    }

    return chi;
  }



  public boolean checkSelected(PVector ms) { // hmm we can  refactor this

    if ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
      isSelected = !isSelected;
    } else {
      isSelected = false;
    }

    return isSelected;
  }

  public void debug(){

      // println("debug");
      strokeWeight(5);
      noFill();
      stroke(163, 149, 41);
      rect(loc.x, loc.y, map.width, map.height); // Left

  }
  public void display() {

    strokeWeight(2);
    noFill();

    image(map, loc.x, loc.y);

    // showChildren();

    //// hover
    if (isSelected) {
      stroke(163, 149, 41);
      rect(loc.x, loc.y, map.width, map.height); // Left
      showChildren();
    } else if  ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
      stroke(255, 100);
      rect(loc.x, loc.y, map.width, map.height); // Left
    }

    // hover
    float x1 = loc.x + size.x/2;
    float y1 = loc.y+size.y+12;
    //textAlign(CENTER);
    //text(name, x1, y1 );
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
class View {

  // this controls the styles of display
  Model model;
  Viewport2D vp2D;
  Viewport3D vp3D;
  Voxelator vox;

  float thickness = 2;

  // camera variables
  float xRot = 3.14f/2;
  float zRot = 3.14f/2;
  float current = 0; //?

  View(Model m, Viewport2D _vp) {
    // println("initialize view class") ;

    model = m;
    vp2D = _vp;
    vp3D = new Viewport3D(xC, yA, xD, yF-height/16);


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
    model.vox.display(); // this one ok


    if (vp3D.mode == "UNIT"){
      vp2D.display();   // this is just a screen
      model.displayCellUnit();  //
    } else {
      vp2D.displayCellArray();
      model.displayCellArray();
    }



  }

  public void display3D() {
    vp3D.display();

  }

  public void drawFrames() {

    // clean this up

    stroke(255, 50);

    float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;
    float y4 = height - y2;
    float y5 = height/2;

    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5f)/24);
    float x5 = (width*(13.75f)/24);
    float y6 = height/16+width/64+width/4 - width/32;

    ////////////////////////////////////////
    // horizontal lines
    strokeWeight(2);
    line(0, yA, width, yA);
    line(0, y4, width, y4);
    line(0, height-os, width, height-os);

    // vertical lines
    line(width/4-os, 0, width/4-os, height);
    line(os, 0, os, height);
    line(width-os, 0, width-os, height);
    line(width/4, 0, width/4, height);
    line(x1, 0, x1, height);
    int y10 = PApplet.parseInt(height/16+width/64+ width/4 - width/32);
    line(0,y10-os, width/4, y10-os);
    line(0,y10, width/4, y10);
    line(0,y10 + tWidth + os, width/4, y10 + tWidth + os);
    line(0,y10 + 2*(tWidth + os), width/4, y10 + 2*(tWidth+os));
    line(0,y10, width/4, y10);
    line(0,y10+ tWidth, width/4-os, y10 + tWidth);
    line(0,y10 + 2*(tWidth)+os, width/4, y10 + 2*(tWidth)+os);


  }

  public void drawLabels() {

    // Small Labels
    // textSize(18);
    // textAlign(CENTER);

    textSize(11);
    textAlign(CENTER);
    String s = "PointCloud Generator - Sayjel Vijay Patel - 2017";
    text(s, width/2, height-os/2);

    // textAlign(CENTER);



    textSize(14);
    s = "Mode: " + vp3D.mode;
    text(s, cA, rA);

    textSize(11);
    // text("CHANNELS", cA, rC);

    //     line(xA, yD-os, xB, yE-os);  // row1
    // line(xA, yE, xB, yE+tWidth); // row2
    // line(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row3

    if (vp3D.mode == "UNIT"){

    text("TEXTURE: CHANNELS", cA, yD-os-os/2); // row1 //
    text("TEXTURES", cA, yE-os/2); // row2 //

    } else {
    text("ARRAYS", cA, yD-os-os/2); // row1 //
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
    strokeWeight(3);
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

    image(image, loc.x, loc.y);

  }

  public void displayCellArray(){

    image(image, loc.x, loc.y);



  }

  public void update(){

    if (thumb.parent != null){
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
      displayCellArray();
    } else {
      displayCellUnit();
    }
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

  public void setCellUnit(PShape _pc){
    // println("set cell unit = " + _pc.size());

    cellUnit.setCloud(_pc);
    // cellUnit.set(_pc);
  }

  public void setCellArray(ArrayList<PVector> _pc){
     println("set cell array = " + _pc.size());
    cellArray.set(_pc);
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
    println("vp3d: setCurrentLayer");
    if (mode == "UNIT"){
    cellUnit.setCurrent(_pc);
    } else {
    cellArray.setCurrent(_pc);
    }
  }

  public void display(){



    if (mode == "UNIT"){
        cellUnit.display(true);
        cellArray.display(false);
      } else {

        cellUnit.display(false);
        cellArray.display(true);
    }

  }




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

    dimX = depth.map.width;
    dimY = depth.map.height;

    setArrays(arrays);

    depthGlobe = globes.get(0);
    materGlobe = globes.get(1);
    alphaGlobe = globes.get(2);

    layer = Current;
    thickness = Thickness;
    update();

  }


  // void getCurrentPC(){


  // }
  public ArrayList<PVector> getCurrentPC(){  // returns pointcloud of the current list

    println("get current pc");

    ArrayList<PVector> _pc = new ArrayList<PVector>();
    int res = 1;
    PImage img = pg.get();
    img.resize(PApplet.parseInt(dimX),PApplet.parseInt(dimY));

    for (int x = 0; x < img.width; x+= res){
      for (int y =  0; y < img.height; y+= res ){
          int c = img.get(x, y);
          if (brightness(c) > 100){
            _pc.add(new PVector(x,y,layer*Amplitude));
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


    // println("toggle Voxelator: " + mode);
    update();
   }

  // }

  public void exportStack(){
    export();
  }

   public void export(){

    // println("exportStack");

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

    println("update Vox");

    if (mode == "ARRAY"){
      updateArray();
    } else {
      updateUnit();
    }

  }

  public void updateUnit(){

    // println("GETTING LAYER : " + t);
    PImage img = depth.texture.get();

    img.resize(PApplet.parseInt(size.x),PApplet.parseInt(size.y));

    pg = createGraphics(PApplet.parseInt(size.x),PApplet.parseInt(size.y));
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

    pcLayer = getCurrentPC();

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
    image(pg, PApplet.parseInt(loc.x) , PApplet.parseInt(loc.y) );
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
    strokeWeight(3);

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

public void setConst(){
os = width/64;

 xA = os;
 xB = width/4 - os;
 xC = width/4;
 xD = width*5/6;

 yA = height/16;
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
}
  public void settings() {  size(1500,800,P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#CCCCCC", "TBM_GENERATOR" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
