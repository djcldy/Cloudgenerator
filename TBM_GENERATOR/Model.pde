class Model {

  //  PImage material, depth;
  PImage slice;
  PGraphics pg;
  PGraphics texMap, matMap, alphMap;

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();

  Thumb alpha, mater, depth, shape, currentThumb;

  //  PGraphics matMap;

  int step = 1;
  int thickness = 25; // thickness of each layer
  ArrayList<PVector> points = new ArrayList<PVector>();

  Model(Thumb _alpha, Thumb _depth, Thumb _mater, Thumb _shape) {

    println("initialize model") ;

    alpha = _alpha;
    mater = _mater;
    depth = _depth;
    shape = _shape;

    

    thumbs.add(alpha);
    thumbs.add(mater);
    thumbs.add(depth);
    thumbs.add(shape);


    pg = createGraphics(200, 200);

    initPC(3);

  }

  void display() {
    for (Thumb th : thumbs) {
      th.display();
    }
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

    image(pg,250,750);
    fill(255);
    textAlign(CENTER);
    text(s, 340, 965);
  }
}