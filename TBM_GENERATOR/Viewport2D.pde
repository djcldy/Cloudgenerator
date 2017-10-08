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
    if (thumb.texture != null){
      image = thumb.texture.get();
      image.resize(int(size.x), int(size.y));
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
    } else {
      displayCellArray();
    }
  }




}
