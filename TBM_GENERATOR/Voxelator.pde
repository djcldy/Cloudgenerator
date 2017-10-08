class Voxelator {

  // PImage layer;
  PVector loc, size;
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> arrays = new ArrayList<Thumb>();
  ArrayList<Thumb> globes = new ArrayList<Thumb>();
  ArrayList<PVector> pcLayer = new ArrayList<PVector>();
  PGraphics pg;
  float layer, thickness;

  float dimX, dimY;

  Thumb depth, alpha, mater;
  Thumb depthArray, alphaArray, materArray;
  Thumb depthGlobe, alphaGlobe, materGlobe;

  boolean loaded = false;

  String mode = "UNIT";

  Voxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

    loc    =  _loc;
    size   =  _size;
    thumbs =  _thumbs;
    arrays = _arrays;
    globes = _globes;

    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    dimX = size.x;
    dimY = size.z;

    // setArrays(arrays);

    depthGlobe = globes.get(0);
    materGlobe = globes.get(1);
    alphaGlobe = globes.get(2);

    layer = Current;
    thickness = Thickness;
    // update();

  }


  // void getCurrentPC(){


  // }
  ArrayList<PVector> getCurrentPC(){  // returns pointcloud of the current list

    ArrayList<PVector> _pc = new ArrayList<PVector>();
    int res = 1;
    PImage img = pg.get();
    img.resize(int(dimX),int(dimY));

    for (int x = 0; x < img.width; x+= res){
      for (int y =  0; y < img.height; y+= res ){
          color c = img.get(x, y);
          if (brightness(c) > 100){
            _pc.add(new PVector(x,y,layer*255*Amplitude));
          }
      }
    }

    return _pc;
  }


  void setArrays(ArrayList<Thumb> _arrays){
    depthArray = _arrays.get(0);
    materArray = _arrays.get(1);
    alphaArray = _arrays.get(2);

    if (alphaGlobe != null ){updateArray(); }
    //
  }

  // ArrayList getPC(){

   void toggleMode(){
    if (mode == "UNIT"){
      mode = "ARRAY";
    } else {
      mode = "UNIT";
    }
    update();
   }

  // }

  void exportStack(){
    export();
  }

   void export(){

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

  void toggle(){



  }

  void update(){

    // println("update Vox");

    if (mode == "ARRAY"){
      updateArray();
    } else {
      updateUnit();
    }

  }



  void updateUnit(){

    if (depth.texture != null){

    PImage img = depth.texture.get();
    img.resize(int(size.x),int(size.y));

    boolean invertLayer;

    int res = 1;
    float range = 255;
    int numLevels = LayersZ;

    float domain = range;
    // float amp = Amplitude/levels; // this is the total height
    float totalSlices = 255;
    float subDomain = totalSlices/numLevels;

    int currentSlice = int(totalSlices*layer); // current slice
    int currentStep = floor(currentSlice/subDomain);
    float currentLayer = (currentSlice%subDomain)/subDomain*255;

    // println("Update Vox, currentLayer = " + currentLayer);

    if (currentStep%2 == 0){
      invertLayer = false;
    } else {
      invertLayer = true;
    }


    pg = createGraphics(int(size.x),int(size.y));
    pg.beginDraw();
    pg.background(0);
    pg.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      float val = brightness(img.pixels[k]);
    if ((val < (currentLayer+thickness)) && (val > (currentLayer-thickness))) { pg.pixels[k] = color(255);}
    }

    pg.updatePixels();
    pg.endDraw();
    pcLayer = getCurrentPC(); // this displays the current cut layer in 3D
  }
  }


  void updateArray(){

    println("get depth texture");
    PImage img = depthArray.parent.get();
    PImage shape = alphaGlobe.parent.get();

    img.resize(int(size.x),int(size.y));
    shape.resize(int(size.x),int(size.y));

    pg = createGraphics(int(size.x),int(size.y));
    pg.beginDraw();
    // pg.background(0);
    pg.image(img,0,0);
    pg.endDraw();

    pg.loadPixels();
    shape.loadPixels();

    for (int k = 0; k <  img.pixels.length; k++) {
      color bright = pg.pixels[k];
      color bright2 = shape.pixels[k];

      float val = brightness(bright);
      float val2 = brightness(bright2);
    if ((val < (layer+thickness)) && (val > (layer-thickness)) && (val2 > 25)) {
        pg.pixels[k] = color(255);
      } else {
      pg.pixels[k] = color(0);

      }
    }

    pg.updatePixels();


  }

  void display(){

    if (loaded){
      image(pg, int(loc.x) , int(loc.y) );

    } else if (depth.texture != null){
      updateUnit();
      loaded = true;
    }
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
