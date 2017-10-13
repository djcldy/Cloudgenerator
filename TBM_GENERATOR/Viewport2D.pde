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

  void invertTexture(int currentRow){

    // thumb.invertTexture(); // this is the selected thumb here we should update the channel & child..

    if (m.currentR1 != null ){ m.currentR1.invertTexture();}
    if (m.currentR2 != null ){ m.currentR2.invertTexture();}

    image = null;
    // update();
    // set(thumb);
  }

  void displayCellUnit(){
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

  void displayCellArray(){
    if (image != null ){ image(image, loc.x, loc.y);}
  }

  void update(){
    // println("update 2d: " + thumb.name + "," + thumb.parent);
    if (thumb.parent != null){
      PGraphics temp  = createGraphics(int(size.x), int(size.y));
      temp.beginDraw();
      temp.image(thumb.parent, 0,0,int(size.x),int(size.y));
      temp.endDraw();
      image = temp.get();

    }
  }

  void toggleMode(){

    if (mode == "UNIT"){
      mode = "ARRAY";
    } else {
      mode = "UNIT";
    }

  }

  void set(Thumb th){

    thumb = th;
   update();

  }

  void display(){

    if (mode == "UNIT"){
      displayCellUnit();
      displayTool();
    } else {
      displayCellArray();
    }
  }

  void displayTool(){

    noFill();
    strokeWeight(1);
    stroke(255,0,255);
    pushMatrix();
    translate(loc.x+size.x/3, loc.y+size.y/2);
    rect(0,0,150,150);
    popMatrix();

  }




}
