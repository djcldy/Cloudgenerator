class Thumb {
  boolean loaded = false;
  PGraphics map;
  PImage texture, parent;
  String path, name;
  PVector loc, size;
  boolean isSelected = false;
  boolean isMater = false;
  ArrayList<Thumb> children; // chilrdren
  ArrayList<Thumb> selecteren = new ArrayList<Thumb>();
  ImageThread it;
  PApplet app;
  int scroll = 0;




  Thumb(PApplet _app, String _path, PVector _loc, PVector _size, String _name) {
    // isMater = false;
    app   =   _app;
    name  =   _name;
    size  =  _size;
    loc = _loc;
    path = _path;
    reset(_path, _loc);
    setChildren(0); // start on step 1
  }



  Thumb(PApplet _app, String _path, PVector _loc, PVector _size, String _name, boolean _isMultiMaterial) {
    app   =   _app;
    name  =   _name;
    size  =  _size;
    isMater = _isMultiMaterial;
    println("name = " + name + " " + isMater);
    loc = _loc;
    path = _path;
    reset(_path, _loc);
    setChildren(0); // start on step 1
  }


  Thumb(PApplet _app,String _path, PVector _loc, PVector _size, boolean _isMultiMaterial) {
    isMater = _isMultiMaterial;
    println("child multi-material = " + name + " " +  isMater);

    path = _path;
    size = _size;
    loc = _loc;
    reset(_path, _loc);

  }

  Thumb(PImage _texture, PVector _loc, PVector _size){
    size = _size;
       loc = _loc;
     resetArray(_texture,_loc);
  }


  Thumb array(int stepX, int stepY){

    Thumb copy = new Thumb(app,path,loc,size,name);


  // Thumb(PApplet _app, String _path, PVector _loc, PVector _size, String _name) {

    // PGraphics arrayTexture = createGraphics(int(size.x), int(size.y));
    // PImage clone = parent.get();
    // clone.resize(int(size.x/stepX), int(size.y/stepY));

    // arrayTexture.beginDraw();

    // for (int x =0; x < size.x; x += size.x/stepX){
    //   for (int y = 0; y < size.y; y += size.y/stepY){
    //     arrayTexture.image(clone,x,y);
    //   }
    // }
    // arrayTexture.endDraw();

    // PImage result = arrayTexture.get();
    // Thumb arrayThumb = new Thumb(result, loc, size);

   return copy;
  }


  void resetArray(PImage _tex, PVector _loc) {

    path = null;
    loc = _loc;
    float x = size.x;
    float y = size.y;

    map = createGraphics(int(x), int(y));
    map.beginDraw();
    map.background(0);
    map.endDraw();
    texture = _tex; // textures 512 px x 512 px
    parent = texture.get();

    updateMap();
  }



  void reset(String _path, PVector _loc){
    // /("reset: " + _path + "," + size);
    it = new ImageThread(app, _path,size);
    it.start();
  }


  void resetChild(Thumb _child){

    parent = _child.parent.get();
    texture = _child.texture.get();
    map = _child.map;
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
    int col = 0;

    int b1 = scroll;
    int b2 = 3+scroll;

    if (b2 > items) {
      b2 = items;

    }

//     for  (int j = 0 ; j< items; j++){
//     println("children filenames = " + filenames[j]);
// }
    if (filenames !=null) {
      for (int j = 0; j < b2; j++){

        children.add(new Thumb(app, localPath + filenames[j], new PVector(os+size.x*col,loc.y+size.x+os), size, isMater));

        col ++;
        if (col == 3) {
          col = 0;
          row++;
        }
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
      brightness +=  val;
      if (val < min) min = val;
      if (val >max) max = val ;
    }

    float constant = 255.0/(max-min);
    float newMin  = 255.0;
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


  Thumb checkSelectedChildren() {

   Thumb chi = null;

   for (Thumb child: children){
      if ((mouseX > child.loc.x) && (mouseX < (child.loc.x + map.width)) && (mouseY > child.loc.y) && (mouseY < (child.loc.y + map.height))) {
        child.isSelected = true;
        chi = child;

        resetChild(child); // reset Channel with  currennt child...

        // if (selectedChildren.contains(child)){
        //   selectedChildren.add(child);
        // }

      } else {
        child.isSelected = false;
        // selectedChildren.remove(child);
      }
    }

    return chi;
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
      strokeWeight(5);
      noFill();
      stroke(163, 149, 41);
      rect(loc.x, loc.y, map.width, map.height); // Left

  }
  void display() {


        println("isnt ready:" + isMater + " " + name);
    if (loaded == false){
      if (it.isReady){
        // println("it is ready");
        println("isready:" + isMater + " " + name);
        if (isMater == true){
          println("multi-material");
          parent = it.parent;    //
          texture = it.texture;  // We will make another function for multi material
          map = it.map;

        }else{
          println("normalize");
          parent = normalize(it.parent);//
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


    // showChildren();

    //// hover
    // if (isSelected) {
    //   stroke(163, 149, 41);
    //   rect(loc.x, loc.y, map.width, map.height); // Left
    //   showChildren();
    // } else if  ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
    //   stroke(255, 100);
    //   rect(loc.x, loc.y, map.width, map.height); // Left
    // }

    // // hover
    // float x1 = loc.x + size.x/2;
    // float y1 = loc.y+size.y+12;
    //textAlign(CENTER);
    //text(name, x1, y1 );
  }
}
