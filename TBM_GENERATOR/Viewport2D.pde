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

  void update(){
    image = thumb.parent.get();
    image.resize(int(size.x), int(size.y));


  }

  void set(Thumb th){

    thumb = th;
   update();

  }

  void display(){

    image(image, loc.x, loc.y);

  }




}
