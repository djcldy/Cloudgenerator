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

    image(image, loc.x, loc.y);

  }

  void displayCellArray(){

    image(image, loc.x, loc.y);



  }

  void update(){

    if (thumb.parent != null){
    image = thumb.parent.get();
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
      displayCellArray();
    } else {
      displayCellUnit();
    }
  }




}
