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
int myColor = color(0, 0, 0);
float Amplitude = 0.1f;
float Thickness = 10;
int  Current = 10;
int sliderTicks1 = 100;
int sliderTicks2 = 30;

//
PImage exportDepth;
float th = 10;
float ll = 50;

// PUBLIC VARIABLES FOR LAYOUT
float os;
float xA,xB,xC,xD,xE,xF,xG;
float yA,yB,yC,yD,yE,yF,yG;
float cA,cB,cC,cD;
float rA,rB,rC,rD,rE;



public void setup() {

  println("starting");
  
  background(0, 51, 102);
  
  initDims();
  c = new Controller();
  c.vox.thickness = th;
  c.vox.layer = ll;
  c.vox.update();
  initSlider();

}



public void draw() {

  background(2, 7, 49);
  c.update();

}



public void initDims(){

  // initialize all of the dimensions

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
 cA = xC/2;
 cB = xC + (xD-xC)/2;
 rC = yC + os*2/3;
 rD = yE + os*2/3;
//
}


public void checkBox(float[] a) {
  println(a); // these are the toggles
  c.m.points = c.m.resetPC(3, a); // we will have to multi thread this bitch
}


public void Export() {
  th = c.vox.thickness;
  ll = c.vox.layer;
  exportDepth = c.vox.depth.texture.get();
  //thread("export");
}

public void export() {

  int layers = 200;
  PVector dim = new PVector(2000, 2000);
  PImage img = exportDepth;
  img.resize(PApplet.parseInt(dim.x), PApplet.parseInt(dim.y));


  for (int l = 0; l < 200; l++) {

    println("export layer: " + l);

    PGraphics pg = createGraphics(PApplet.parseInt(dim.x), PApplet.parseInt(dim.y));
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


public void initSlider() {
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


  //
  float os = width/64;
  int bW = PApplet.parseInt((width/4 - width/32)/4);
  int bH = PApplet.parseInt(height/16 - os);

  cp5.addButton("Export")
    .setValue(0)
    .setPosition(width*5/6, height-height/16)
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
  // add a horizontal sliders, the value of this slider will be linked
  // to variable 'sliderValue'
  cp5.addSlider("Amplitude")
    .setPosition(o+x1, y1+ 50)
    .setWidth(200)
    .setRange(0.025f, 0.5f)
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

public void Current(float value) {

  c.vox.layer = value;
  c.vox.update();
  c.v.setCurrent(value);
}


public void Thickness(float value) {

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

public void dropdown(int n) {
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


public void RESETPOINTCLOUD() {

    println("reset pointCloud");
    ArrayList<PVector> temp = new ArrayList<PVector>();

    c.m.depth.map.loadPixels();
    c.m.alpha.map.loadPixels();
    c.m.mater.map.loadPixels();

    int res = 1;
    float range = 255;

    //

    for (int x = 0; x < c.m.depth.map.width; x += res) {
      for (int y = 0; y < c.m.depth.map.height; y+= res) {

        float alp = brightness((c.m.alpha.map.get(x,y)));

        if (alp > 10) { // check alpha

           float val = brightness(c.m.depth.map.get(x, y));
           temp.add(new PVector(x, y, val));

          }
        }
      }
      c.m.points = temp;
}
class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  // can get rid of this ?

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  Viewport2D vp;
  Voxelator vox;

  Zone zoneA, zoneB, zoneC;  // select zones
  ArrayList<Zone> zones = new ArrayList<Zone>();

  Controller() {

    println("initialize controller");
    initSelector();
    init();
     m = new Model(thumbs);
     m.currentThumb = thumbs.get(1);
     v = new View(m,vp,vox);
    println("controller initialized");
  }

  public void initSelector(){

     zoneA = new Zone(xA,yB,xB,yC);
     zoneB = new Zone(xA,yD,xB,yE);
     zoneC = new Zone(xA,yE,xB,yF);

    zones.add(zoneA);
    zones.add(zoneB);
    zones.add(zoneC);

  }

  public void init() {
    int bW = PApplet.parseInt((width/4 - width/32));
    int tW = PApplet.parseInt((width/4 - width/32)/3); // width of thumb with row of 3
    float y4 = height/16+width/64+bW+os;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    Thumb depth = new Thumb("/images/depth/bubble.png",   new PVector(os,y4),   dimThumb, "depth");
    Thumb mater = new Thumb("/images/mater/manta.png", new PVector(os+tW,y4),   dimThumb, "mater");
    Thumb alpha = new Thumb("/images/alpha/solid.png",    new PVector(os+2*tW,y4),  dimThumb, "alpha");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    mater.isSelected = true;


    initViewport(new PVector(os, height/16+os),  dimThumbView,  mater);
    initVoxelator(new PVector(width*5/6, height-height/16-dimVox.y), dimVox, thumbs);

  }

  public void initViewport(PVector _loc, PVector _size, Thumb _th){
    vp = new Viewport2D(_loc, _size, _th);
  }

  public void initVoxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs){
      vox = new Voxelator(_loc, _size,  _thumbs);

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


  public void zoneB(PVector ms){

  // toggle zoneB
  boolean toggle = false;

  for (Thumb th : m.thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
        vp.set(m.currentThumb);
        toggle = true;
      }
    }

    if (toggle) {
      for (Thumb th : m.thumbs) {
       if (th != m.currentThumb){
        th.isSelected = false;
       }

      }
    }
  }

  public void mouseDown(PVector ms) {
    if (zoneB.isSelected(ms)){
      zoneB(ms);
    } else if (zoneC.isSelected(ms)){
      zoneC(ms);
    }
  }
}
class Model {

  // this class is all about modelling the 3D Geometry

  PImage slice;
  PGraphics pg, texMap, matMap, alphMap;
  PointCloud pointCloud;

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();

  Thumb alpha, mater, depth, currentThumb;

  //  PGraphics matMap;

  int step = 1;
  int thickness = 25; // thickness of each layer
  ArrayList<PVector> points = new ArrayList<PVector>();

  Model(ArrayList<Thumb> _thumbs) {

    println("initialize model") ;

    thumbs = _thumbs;
    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    pg = createGraphics(200, 200);

    initPC(3);

  }

  public void display() {
    // getLayer(Current, false); // activate voxel map
    for (Thumb th : thumbs) {
      th.display();
    }
  }



  public ArrayList<PVector> resetPC(int res, float []a) {

    println("reset pointCloud");
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



  public void initPC(int res) {

    println("inialize points");


    points = new ArrayList<PVector>();

    depth.map.loadPixels();

    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {
        float val = brightness(depth.map.get(x, y));
        points.add(new PVector(x, y, val));
      }
    }

    println("points length = " + points.size());

    //    pg.updatePixels();
  }

  public void getLayer(float t, boolean save) {
    //    background(0);
    //     println("getlayer: " + t);

    //    pg.beginDraw();

        float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;


    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5f)/24);
    float x5 = (width*(13.75f)/24);

    PImage img = depth.texture;

    depth.map.loadPixels();
    mater.map.loadPixels();

    pg = createGraphics(depth.map.width, depth.map.height);
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();


    for (int k = 0; k <  depth.map.pixels.length; k++) {
      int bright = depth.map.pixels[k];
      int rgb = mater.map.pixels[k];

      float val = brightness(bright);

      if ((val < (t+Thickness)) && (val > (t-Thickness))) {
        pg.pixels[k] = color(255);
      }

    }

    pg.updatePixels();
    pg.endDraw();

    String s = "Image Stack: " + t;
    image(pg,x5,y1);
    fill(255);

  }
}
class PointCloud {

    PointCloud (Thumb depth, Thumb material, Thumb Alpha){



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

  Thumb(String _path, PVector _loc, PVector _size, String _name) {
    name = _name;
    size = _size;
    reset(_path, _loc);
    setChildren();
  }

  Thumb(String _path, PVector _loc, PVector _size) {
    size = _size;
    reset(_path, _loc);
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
    texture = loadImage(path); // textures 512 px x 512 px
    // texture = normalize(texture);check
    parent = texture.get();

    updateMap();
  }

  public void updateMap() {
    texture.resize(0, PApplet.parseInt(size.x));
    map.beginDraw();
    map.image(texture, 0, 0);
    map.endDraw();
  }


  public void  setChildren() {

    children = new ArrayList<Thumb>();
    String localPath = "/images/" + name + "/";                   //
    String[] filenames = listFileNames(sketchPath() + localPath);   //

    float childWidth = PApplet.parseInt((xC - 2*os)/3);

    int items = filenames.length;
    int row = 0;
    int col = 0;

    if (filenames !=null) {
      for (int j = 0; j < items; j++){
        children.add(new Thumb(localPath + filenames[j], new PVector(xA+size.x*col,yE+os+size.x*row), size));
        col ++;
        if (col == 3) {
          col = 0;
          row++;
        }
      }
    }

         //  for (int i = 0; i < filenames.length; i++) {
     //    children.add(new Thumb(localPath + filenames[i],   new PVector(xA,yE+os), size));
     //    children.add(new Thumb(localPath + filenames[i+1],   new PVector(xA+size.x,yE+os), size));
     //    children.add(new Thumb(localPath + filenames[i+2],   new PVector(xA+2*size.x,yE+os), size));
     // }
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

    img = temp;
    return img;
  }


  public void checkSelectedChildren() {
   for (Thumb child: children){
      if ((mouseX > child.loc.x) && (mouseX < (child.loc.x + map.width)) && (mouseY > child.loc.y) && (mouseY < (child.loc.y + map.height))) {
        child.isSelected = true;
        reset(child.path, loc);

        if (!selectedChildren.contains(child)){
          selectedChildren.add(child);
        }

      } else {
        child.isSelected = false;
        selectedChildren.remove(child);
      }
    }
  }



  public boolean checkSelected(PVector ms) { // hmm we can  refactor this

    if ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
      isSelected = !isSelected;
    } else {
      isSelected = false;
    }

    return isSelected;
  }

  public void display() {

    strokeWeight(2);
    noFill();

    image(map, loc.x, loc.y);

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
class View {

  // this controls the styles of display
  Model model;
  Viewport2D vp;
  Voxelator vox;

  // camera variables
  float xRot = 3.14f/2;
  float zRot = 3.14f/2;
  int current = 0; //?

  View(Model m, Viewport2D _vp, Voxelator _vox) {
    println("initialize view class") ;
    model = m;
    vp = _vp;
    vox  = _vox;
  }

  public void setCurrent(float value){
    // set the current layer
    current = PApplet.parseInt(value);

  }

  public void display() {
    display3D();
    noStroke();

    // fill(2, 7, 49);
    // rect(0,0,width,height/16);
    vp.display();
    display2D();
  }

  public void display2D() {
    vox.display();
    displayText();
    model.display();
    // vp.display();
  }

  public void displayText(){

    stroke(255,50);
    float y0 = width/64;
    float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;
    float y4 = height - y2;
    float y5 = height/2;

    float x0 = width/64;
    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5f)/24);
    float x5 = (width*(13.75f)/24);



     float y6 = height/16+width/64+width/4 - width/32;

////////////////////////////////////////
    // horizontal lines
    strokeWeight(1);
    // these guys below the thumb
    line(0,yE,width,yE);
    line(0,yE+os,width,yE+os);
/////////////////////////////////////////////

    line(0,y2,width,y2);
    line(0,y2+y0,width,y2+y0);





    line(0,y4,width,y4);
    line(0,height-y0,width,height-y0);

    // vertical lines
    //strokeWeight(2);
    line(width/4-x0,0,width/4-x0,height);
    line(x0,0,x0,height);
    line(width-x0,0,width-x0,height);

    line(width/4,0,width/4,height);
    line(x1,0,x1,height);


    // lables
    textSize(14);
    textAlign(CENTER);


    String s = "Ouputs";
    text(s,x2, y3);

    s = "Inputs";
    text(s,width/8, y3);


//    s = "Rules";
//    text(s,width/8, height - 300);

    s = "Parameters";
    text(s,x3, y3);

    textSize(11);

    //s = "Texture Primitives";
    //text(s,width/8, y2+18);

    s = "Mirror Plane";
    text(s,width*11/12, height/4 - 20);

    // drawZones();
    drawLabels();

  }

  public void drawLabels(){

    // Small Labels

    textSize(10);
    textAlign(CENTER);

    text("CHANNELS",cA,rC);
    text("PRIMITIVES",cA,rD);
    text("RULE-CHAIN",cB,rD);

  }


  public void drawZones() {

    stroke(34,155,215);
    strokeWeight(3);
    noFill();

    // Zone 1-1
    line(xA,0,xB,yA);

    // Zone 2-1
    line(xA,yB,xB,yC);

    // Zone 3-1
    line(xA,yD,xB,yE);

    // Zone 4-1
    line(xA,yE,xB,yF);

    // rect(xA,yD, xB, yE);

  }



  public void display3D() {

    strokeWeight(1);

    for (PVector p : model.points) {
      pushMatrix();
      translate(width*13/24, height/3);
      rotateX(xRot);
      rotateZ(zRot);
      scale(5);

      if ((screenX(p.x,p.y,p.z) > width/4)){
      // if ((p.z > current - 5) && (p.z < current + 5)) {
      //   stroke(255);
      // } else {
      //   stroke(255, 25);
      // }
        point(p.x, p.y, (p.z)*Amplitude+Thickness);
        point(p.x, p.y, (p.z)*Amplitude+Thickness);
      }
      popMatrix();
    }

    xRot += 0.005f;
    zRot += 0.005f;
  }

}
class Viewport2D {

  PVector size, loc;
  PImage image;
  Thumb thumb;

  Viewport2D(PVector _loc, PVector _size, Thumb _thumb){

    size = _size;
    loc = _loc;
    thumb = _thumb;

    update();

  }

  public void update(){
    image = thumb.parent.get();
    image.resize(PApplet.parseInt(size.x), PApplet.parseInt(size.y));


  }

  public void set(Thumb th){

    thumb = th;
   update();

  }

  public void display(){

    image(image, loc.x, loc.y);

  }




}
class Voxelator {

  // PImage layer;
  PVector loc, size;
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  PGraphics pg;
  float layer, thickness;

  Thumb depth, alpha, mater;

  Voxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs){

    loc    =  _loc;
    size   =  _size;
    thumbs =  _thumbs;

    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    layer = Current;
    thickness = Thickness;
    update();

  }

  // ArrayList getPC(){



  // }

  public void exportStack(){
    export();
  }

   public void export(){

    println("exportStack");

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

  public void update(){

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

  }


  public void display(){
    // getLayer(50);

    image(pg, PApplet.parseInt(loc.x) , PApplet.parseInt(loc.y) );
    // String s = "Image Stack: " + Current;
    // textAlign(CENTER);
    // text(s, 340, 965);

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
  public void settings() {  fullScreen(P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#CCCCCC", "TBM_GENERATOR" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
