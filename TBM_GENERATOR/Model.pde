class Model {

  // this class is all about modelling the 3D Geometry

  PImage slice;
  PGraphics pg;
  PGraphics texMap, matMap, alphMap;

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();

  Thumb alpha, mater, depth, currentThumb;

  //  PGraphics matMap;

  int step = 1;
  int thickness = 25; // thickness of each layer
  ArrayList<PVector> points = new ArrayList<PVector>();

  Model(ArrayList<Thumb> _thumbs) {

    println("initialize model") ;

    thumbs = _thumbs;
    depth = thumbs.get(0);
    mater = thumbs.get(1);

    // for (Thumb th : thumbs){
    //   if (th.name == "depth"){
    //     depth = th;
    //   }
    //   if (th.name == "mater"){
    //     mater = th;
    //   }
    // }


    pg = createGraphics(200, 200);

    initPC(3);

  }

  void display() {
    // getLayer(Current, false); // activate voxel map
    for (Thumb th : thumbs) {
      th.display();
    }
  }



  ArrayList<PVector> resetPC(int res, float []a) {

    println("reset pointCloud");
    ArrayList<PVector> temp = new ArrayList<PVector>();
    depth.map.loadPixels();

    float range = 255;



    for (int x = 0; x < depth.map.width; x += res) {
      for (int y = 0; y < depth.map.height; y+= res) {

        float val = brightness(depth.map.get(x, y));

        if (a[0] == 1) {
          temp.add(new PVector(x, y, -val/2 + range/2));
          temp.add(new PVector(x, y, val/2 + range/2));

        } else {

          temp.add(new PVector(x, y, val));
        }
      }
    }
    return temp;
  }



  void initPC(int res) {

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

  void getLayer(float t, boolean save) {
    //    background(0);
    //     println("getlayer: " + t);

    //    pg.beginDraw();

        float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;


    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5)/24);
    float x5 = (width*(13.75)/24);

    PImage img = depth.texture;

    depth.map.loadPixels();
    mater.map.loadPixels();

    pg = createGraphics(depth.map.width, depth.map.height);
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();


    for (int k = 0; k <  depth.map.pixels.length; k++) {
      color bright = depth.map.pixels[k];
      color rgb = mater.map.pixels[k];

      float val = brightness(bright);

      if ((val < (t+Thickness)) && (val > (t-Thickness))) {
        pg.pixels[k] = color(255);
      }

    }

    pg.updatePixels();
    pg.endDraw();

    String s = "Image Stack: " + t;
    image(pg,x5,y1);
    fill(255);

  }
}
