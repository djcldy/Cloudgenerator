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

  void exportStack(){
    export();
  }

   void export(){

    println("exportStack");

    int layers = 0;
    PVector dim = new PVector(400,400);


    for (int l = 0; l < 255; l++){

    PImage img = depth.texture.get();

    img.resize(int(dim.x),int(dim.y));

    pg = createGraphics(int(dim.x),int(dim.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      color bright = img.pixels[k];
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

  void update(){

    // println("GETTING LAYER : " + t);
    PImage img = depth.texture.get();

    img.resize(int(size.x),int(size.y));

    pg = createGraphics(int(size.x),int(size.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      color bright = img.pixels[k];
      float val = brightness(bright);
    if ((val < (layer+thickness)) && (val > (layer-thickness))) {
        pg.pixels[k] = color(255);
      }
    }

    pg.updatePixels();
    pg.endDraw();

  }


  void display(){
    // getLayer(50);

    image(pg, int(loc.x) , int(loc.y) );
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
