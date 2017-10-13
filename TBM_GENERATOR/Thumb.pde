class Thumb {
  boolean loaded = false;
  PGraphics map;
  PImage texture, parent, textureA, parentA, textureB, parentB;
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

  void invertTexture(){

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


  void reset(String _path, PVector _loc){
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
    texture.resize(int(size.x),int(size.y));
    textureA = texture;
    textureB = texture;

    map = null;
    loaded = true;
   }
  }


  void resetChild(Thumb _child){

    parent = _child.parent.get();

    parentA = _child.parentA.get();
    parentB = _child.parentB.get();

    texture = parent.get();
    texture.resize(int(size.x),int(size.y));

    textureA = parentA.get();
    textureA.resize(int(size.x), int(size.y));

    textureB = texture;


    map = _child.map;
    mode = _child.mode;
  }



  void updateMap() {
    texture.resize(0, int(size.x));
    map.beginDraw();
    map.image(texture, 0, 0);
    map.endDraw();
  }




  void setChildren(int step) {

    scroll += step;
    children = new ArrayList<Thumb>();

    String localPath = "/textures/" + name + "/";                   //
    String[] filenames = listFileNames(sketchPath() + localPath);   //
    float childWidth = int((xC - 2*os)/3);
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

  void showChildren() {

    if (children != null) {
      for (Thumb child : children) {
        child.display();
      }
    }

  }




  PImage normalize(PImage img) {

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


    float constant = 255.0/(max-min);
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


  Thumb checkSelectedChildren() {

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

  PImage invert(PImage _img){
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



  boolean checkSelected(PVector ms) { // hmm we can  refactor this
    isSelected = false;
if ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      isSelected = !isSelected;
    } else {
      isSelected = false;
    }
    return isSelected;
  }

  void debug(){

      // println("debug");

      noFill();
      stroke(163, 149, 41);
      rect(loc.x, loc.y, size.x, size.y); // Left

  }
  void display() {



        // println("isnt ready:" + isMater + " " + name);
        // println("fsaddfdasgsad = " + name + " " + fsaddfdasgsad);

    if (loaded == false){
      if (it.isReady){
        // println("it is ready");

        if (mode == "COLOR"){
          // println("multi-material");
          parent = it.parent;    //
          texture = it.texture;  // We will make another function for multi material
          parentA = it.parentA;
          textureA = it.parentA;
          parentB = parent;
          textureB = texture;

          map = null;

        }else{
          // println("normalize");
          parent = normalize(it.parent);//
          texture = parent.get();
          texture.resize(int(size.x), int(size.y));
          // texture = normalize(it.texture);//
          parentA = it.parentA;
          textureA = parentA.get();
          textureA.resize(int(size.x),int(size.y));
          // textureA = normalize(it.texture)
          parentB = parent;
          textureB = texture;
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
