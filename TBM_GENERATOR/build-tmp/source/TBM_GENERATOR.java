import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.Collections; 
import java.util.Comparator; 
import java.util.ArrayList; 
import controlP5.*; 
import java.util.*; 

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
// Re-factored (2017-09)








// what is this
ControlP5 cp5;

int myColor = color(0,0,0);
float Amplitude = 0.1f;
float Thickness = 10;
int  Current = 10;
int sliderTicks1 = 100;
int sliderTicks2 = 30;
Slider abc;
//

Controller c;


public void setup(){
 println("starting");
 
 background(0, 51, 102);
 
 c = new Controller();
}


 public void draw(){

  background(2,7,49);
  // c.update();


 }



// void mousePressed(){
//   // Mouse Event
//   PVector mouseEvent = new PVector(mouseX, mouseY);
//   c.mouseDown(mouseEvent); // mouse pressed

// }

// void event(ControlEvent theEvent) {
//   println(theEvent.getArrayValue());
// }


// void initSlider(){
//   cp5 = new ControlP5(this);

//   // add a horizontal sliders, the value of this slider will be linked
//   // to variable 'sliderValue'
//   cp5.addSlider("Amplitude")
//      .setPosition(1300,50)
//      .setWidth(200)
//      .setRange(0.025,0.5)
//      ;

//   cp5.addSlider("Thickness")
//      .setPosition(1300,75)
//      .setWidth(200)
//      .setRange(1,20)
//      ;


//   cp5.addSlider("Current")
//      .setPosition(1300,100)
//      .setWidth(200)
//      .setRange(0,255) // values can range from big to small as well
//      .setValue(100)
//      .setNumberOfTickMarks(255)
//      .setSliderMode(Slider.FLEXIBLE)
//   ;


//   cp5.addSlider("Layers")
//      .setPosition(1300,125)
//      .setWidth(200)
//      .setRange(1,20) // values can range from big to small as well
//      .setValue(4)
//      .setNumberOfTickMarks(10)
//      .setSliderMode(Slider.FLEXIBLE)
//   ;

// //    List l = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
// //    /* add a ScrollableList, by default it behaves like a DropdownList */
// //      cp5.addScrollableList("textures")
// //     .setPosition(1300, 150)
// //     .setSize(200, 100)
// //     .setBarHeight(20)
// //     .setItemHeight(20)
// //     .addItems(l)
// //      .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
// //     ;
// //
// //      cp5.addScrollableList("heightfield")
// //     .setPosition(1300, 300)
// //     .setSize(200, 100)
// //     .setBarHeight(20)
// //     .setItemHeight(20)
// //     .addItems(l)
// //      .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
// //     ;
// //
// //    cp5.addScrollableList("alpha")
// //     .setPosition(1300, 450)
// //     .setSize(200, 100)
// //     .setBarHeight(20)
// //     .setItemHeight(20)
// //     .addItems(l)
// //      .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
// //     ;


//   // create another slider with tick marks, now without
//   // default value, the initial value will be set according to
//   // the value of variable sliderTicks2 then.
//   //cp5.addSlider("sliderTicks1")
//   //   .setPosition(100,140)
//   //   .setSize(20,100)
//   //   .setRange(0,255)
//   //   .setNumberOfTickMarks(5)
//   //   ;


//   // add a vertical slider
//   //cp5.addSlider("slider")
//   //   .setPosition(100,305)
//   //   .setSize(200,20)
//   //   .setRange(0,200)
//   //   .setValue(128)
//   //   ;

//   //// reposition the Label for controller 'slider'
//   //cp5.getController("slider").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
//   //cp5.getController("slider").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);



//   // use Slider.FIX or Slider.FLEXIBLE to change the slider handle
//   // by default it is Slider.FIX

// }


// //void setup() {


// //}

// //void draw() {
// //  background(sliderTicks1);

// //  fill(sliderValue);
// //  rect(0,0,width,100);

// //  fill(myColor);
// //  rect(0,280,width,70);

// //  fill(sliderTicks2);
// //  rect(0,350,width,50);
// //}

// //void slider(float theColor) {
// //  myColor = color(theColor);
// //  println("a slider event. setting background to "+theColor);
// //}

// //
// //void setup() {
// //  size(400, 400);
// //  cp5 = new ControlP5(this);
// //  List l = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
// //  /* add a ScrollableList, by default it behaves like a DropdownList */
// //  cp5.addScrollableList("dropdown")
// //     .setPosition(100, 100)
// //     .setSize(200, 100)
// //     .setBarHeight(20)
// //     .setItemHeight(20)
// //     .addItems(l)
// //     // .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
// //     ;
// //
// //
// //}
// //
// //void draw() {
// //  background(240);
// //}

// void dropdown(int n) {
//   /* request the selected item based on index n */
//   println(n, cp5.get(ScrollableList.class, "dropdown").getItem(n));

//   /* here an item is stored as a Map  with the following key-value pairs:
//    * name, the given name of the item
//    * text, the given text of the item by default the same as name
//    * value, the given value of the item, can be changed by using .getItem(n).put("value", "abc"); a value here is of type Object therefore can be anything
//    * color, the given color of the item, how to change, see below
//    * view, a customizable view, is of type CDrawable
//    */

//    CColor c = new CColor();
//   c.setBackground(color(255,0,0));
//   cp5.get(ScrollableList.class, "dropdown").getItem(n).put("color", c);

// }

// //void keyPressed() {
// //  switch(key) {
// //    case('1'):
// //    /* make the ScrollableList behave like a ListBox */
// //    cp5.get(ScrollableList.class, "dropdown").setType(ControlP5.LIST);
// //    break;
// //    case('2'):
// //    /* make the ScrollableList behave like a DropdownList */
// //    cp5.get(ScrollableList.class, "dropdown").setType(ControlP5.DROPDOWN);
// //    break;
// //    case('3'):
// //    /*change content of the ScrollableList */
// //    List l = Arrays.asList("a-1", "b-1", "c-1", "d-1", "e-1", "f-1", "g-1", "h-1", "i-1", "j-1", "k-1");
// //    cp5.get(ScrollableList.class, "dropdown").setItems(l);
// //    break;
// //    case('4'):
// //    /* remove an item from the ScrollableList */
// //    cp5.get(ScrollableList.class, "dropdown").removeItem("k-1");
// //    break;
// //    case('5'):
// //    /* clear the ScrollableList */
// //    cp5.get(ScrollableList.class, "dropdown").clear();
// //    break;
// //  }
// //}

class Controller {

  Model m;
  View v;
  float  ii = 0;


  Thumb alpha, depth, mater, shape; // image channels

  Controller() {

    println("initialize controller");
    init();


    // m = new Model(alpha, depth, mater, shape);

    // v = new View(m);
    println("controller initialized");
  }

  public void init() {
    alpha = new Thumb("/images/alpha/solid.png", new PVector(250, 520),new PVector(200, 200), "alpha");
    mater = new Thumb("/images/material/manta.png", new PVector(40, 750), new PVector(200, 200), "mater");
    depth = new Thumb("/images/depth/bubble.png", new PVector(40, 520), new PVector(200, 200), "depth");
    shape = new Thumb("/images/depth/bubble.png", new PVector(40, 40), new PVector(4, 410), "shape");

    Shape grid = new Shape("/images/depth/bubble.png");


  }

  public void update() {
    v.display();
    m.getLayer(Current, false);
  }

  public void mouseDown(PVector ms) {

    boolean toggle = false;

    for (Thumb th : m.thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
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
}


class Model {

  //  PImage material, depth;
  PImage slice;
  PGraphics pg;
  PGraphics texMap, matMap, alphMap;

  Thumb currentThumb;

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();

  Thumb alpha, mater, depth, shape;

  //  PGraphics matMap;

  int step = 1;
  int thickness = 25; // thickness of each layer
  ArrayList<PVector> points = new ArrayList<PVector>();

  Model(Thumb _alpha, Thumb _depth, Thumb _mater, Thumb _shape) {

    println("initialize model") ;


    alpha = _alpha;
    mater = _mater;
    depth = _depth;
    shape = _shape;


    thumbs.add(alpha);
    thumbs.add(mater);
    thumbs.add(depth);
    thumbs.add(shape);


    pg = createGraphics(200, 200);

    initPC(3);

  }

  public void display() {
    for (Thumb th : thumbs) {
      th.display();
    }
  }

  public void initPC(int res) {

    println("inialize points");


    points = new ArrayList<PVector>();

    depth.map.loadPixels();

    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {

        float val = brightness(depth.map.get(x, y));

        points.add(new PVector(x, y, val));
//        points.add(new PVector(x, y, -val));
//        points.add(new PVector(x, y, 255+val));
//        points.add(new PVector(x, y, -255-val));
      }
    }

    println("points length = " + points.size());

    //    pg.updatePixels();
  }

  public void getLayer(float t, boolean save) {
    //    background(0);
    //     println("getlayer: " + t);

    //    pg.beginDraw();

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

    image(pg,250,750);
    fill(255);
    textAlign(CENTER);
    text(s, 340, 965);
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

  Thumb(String _path, PVector _loc, PVector _size, String _name) {
    println("initialize thumb") ;
    
    name = _name;
    size = _size; 
    
    reset(_path, _loc);
  }
  
  
  

  public void reset(String _path, PVector _loc) {

    path = _path; 
    loc = _loc;
    map = createGraphics(PApplet.parseInt(size.x), PApplet.parseInt(size.y));
    texture = loadImage(path); // textures 512 px x 512 px

    if (name != "alpha") { 
      texture = normalize(texture);
    }
    parent = texture.get();  
    updateMap();

  }

  public void updateMap() { 
    texture.resize(0, map.height);
    map.image(texture, 0, 0);
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

  public boolean checkSelected(PVector ms) { 

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


    // hover
    if (isSelected) { 
      stroke(163, 149, 41);
      rect(loc.x, loc.y, map.width, map.height); // Left
    } else if  ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
      stroke(255, 100);
      rect(loc.x, loc.y, map.width, map.height); // Left
    }

    // hover 

    text(name, loc.x + 100, loc.y+215 );
  }
}

class View { 

  Model model;
  
  // camera variables
  float xRot = 3.14f/2;
  float zRot = 3.14f/2;

  View(Model m) {
    println("initialize view class") ;
    model = m;
  }


  public void display() { 
    display2D();  
    display3D();
  }

  public void display2D() {
    model.display();
  }


  public void display3D() {

    strokeWeight(2); 
    for (PVector p : model.points) { 
      pushMatrix(); 
      translate(width*0.66f, height*.33f);
      rotateX(xRot);
      rotateZ(zRot);  
      scale(1.5f);
      if ((p.z > Current -Thickness) && (p.z < Current+Thickness)) { 
        stroke(255);
      } else { 
        stroke(255, 50);
      }
      point(p.x, p.y, (p.z + Thickness)*Amplitude);
      point(p.x, p.y, (p.z - Thickness)*Amplitude);
      popMatrix();
    }
    xRot += 0.005f;
    zRot += 0.005f;
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
  public void settings() {  size(2000,1200,P3D);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--present", "--window-color=#666666", "--stop-color=#CCCCCC", "TBM_GENERATOR" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
