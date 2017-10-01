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


  void reset(String _path, PVector _loc) {

    path = _path;
    loc = _loc;
    float x = size.x;
    float y = size.y;

    map = createGraphics(int(x), int(y));
    map.beginDraw();
    map.background(0);
    map.endDraw();
    texture = loadImage(path); // textures 512 px x 512 px
    // texture = normalize(texture);check
    parent = texture.get();

    updateMap();
  }

  void updateMap() {
    texture.resize(0, int(size.x));
    map.beginDraw();
    map.image(texture, 0, 0);
    map.endDraw();
  }


  void  setChildren() {

    children = new ArrayList<Thumb>();
    String localPath = "/images/" + name + "/";                   //
    String[] filenames = listFileNames(sketchPath() + localPath);   //

    float childWidth = int((xC - 2*os)/3);

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

    img = temp;
    return img;
  }


  void checkSelectedChildren() {
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



  boolean checkSelected(PVector ms) { // hmm we can  refactor this

    if ((mouseX > loc.x) && (mouseX < (loc.x + map.width)) && (mouseY > loc.y) && (mouseY < (loc.y + map.height))) {
      isSelected = !isSelected;
    } else {
      isSelected = false;
    }

    return isSelected;
  }

  void display() {

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
