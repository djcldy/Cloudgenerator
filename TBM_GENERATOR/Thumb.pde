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


  Thumb array(int stepX, int stepY){

    PGraphics arrayTexture = createGraphics(int(size.x), int(size.y));
    PImage clone = parent.get();
    clone.resize(int(size.x/stepX), int(size.y/stepY));

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


  void reset(String _path, PVector _loc) {

    path = _path;
    loc = _loc;
    float x = size.x;
    float y = size.y;

    map = createGraphics(int(x), int(y));
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

  void updateMap() {
    texture.resize(0, int(size.x));
    map.beginDraw();
    map.image(texture, 0, 0);
    map.endDraw();
  }




  void  setChildren(int step) {

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

  void showChildren() {
    if (children != null) {
      for (Thumb child : children) {
        child.display();
      }
    }
  }




  PImage normalize(PImage img) {

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



  boolean checkSelected(PVector ms) { // hmm we can  refactor this

    if ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
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
