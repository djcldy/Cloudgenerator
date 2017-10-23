class Voxelator {

  // PImage layer;

  PApplet pApp;
  PVector loc, size;
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> arrays = new ArrayList<Thumb>();
  ArrayList<Thumb> globes = new ArrayList<Thumb>();
  ArrayList<PVector> pcLayer = new ArrayList<PVector>();
  PGraphics pg;
  PImage currentLayer;

  float layer, thickness;
  float dimX, dimY;

  PShape pointCloud;

  Thumb depth, alpha, mater;
  Thumb depthArray, alphaArray, materArray;
  Thumb depthGlobe, alphaGlobe, materGlobe;

  PImage depthChannel, alphaChannel, materChannel;

  boolean loaded = false;

  String mode = "UNIT";
  String fillMode = "SHELL";

  VoxelLayer vL;


  Exporter exportVoxels;


  Voxelator(PApplet _pApp, PVector _loc, PVector _size, ArrayList<Thumb> _thumbs, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

    pApp  = _pApp;
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

    layer = Current;
    thickness = Thickness;

  }


  void toggleMode(){

    // do nothing //

  }


  void toggleFillMode(){

    if (fillMode == "SHELL"){
      fillMode = "SOLID";
    } else {
      fillMode = "SHELL";
    }
    update();
  }

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

  // void exportStack(){
  //   export();
  // }

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
    updateUnit();
  }


  color translatePo(color c){

    color Cyan    = color(0,89,158);
    color Magenta = color(161,35,99);
    color Yellow  = color (213, 178, 0);
    color Black   = color(30,30,30);
    color White   = color(220,222,216);

    float r0 = red(c)/255;
    float g0 = green(c)/255;
    float b0 = blue(c)/255;

    float k = 1 - max(r0,g0,b0); // return maximum?
    float cy = (1 - r0 -k)/(1-k);
    float mg = (1 - g0 -k)/(1-k);
    float ye = (1 - b0 -k)/(1-k);

    float sum = k + cy + mg+ ye;
    float rnd = random(0,sum);

    if ((rnd > 0) && (rnd < k)){
      if (k > 0.5){
        return Black;
      } else {
        return White;
      }
    } else if (( rnd > k ) && (rnd < k + cy)){ return Cyan;
    } else if (( rnd > k + cy ) && (rnd < k + cy + ye)){ return Yellow;
    } else if (( rnd > k + cy + ye)){ return Magenta;}

    return White;

  }


  void updateUnit(){


    updateSolid();

    // println("updateUnit");
    // if (fillMode == "SHELL"){
    //   // println("fill shell");
    //   updateShell();
    // } else {
    //   // println("fill solid");
    //   updateSolid();
    // }
  }


void getLayer(float ratio, boolean invert, PImage depthChannel, PImage alphaChannel, PImage materChannel, int dim){

  loaded = false;
  vL = new VoxelLayer(pApp, ratio,invert,depthChannel,alphaChannel,materChannel, dim);
  vL.start();

}


void exportStack(){

    exportVoxels = new Exporter(pApp, ratio,invert,depthChannel,alphaChannel,materChannel, dim);
    exportVoxels.start();

    if (pointCloud != null){
      int voxXY = int(DimXY/0.040); //  num voxels in X
      int voxZ = int(DimZ/0.030); //  num voxels in Z

      color white = color(255,255);

    // updateChannels();

    println("total layers: " + voxZ);
    for (int z = 0; z < voxZ;  z++){

      boolean invertLayer = true;

      float l = float(z)/float(voxZ);

      println("layers = " + z + " , " + l);

        if (l > 0.5){ invertLayer = false;}

       // PGraphics temp = getLayer(z, invertLayer, depthChannel,alphaChannel,materChannel);
       // temp.save("exports/layer_" + z + ".png");


      }

    }

  }

  void updateSolid(){

    if (pointCloud != null){
      println("update voxelator: layer = " + layer);

      int voxXY = int(DimXY/0.040); //  num voxels in X
      int voxZ = int(DimZ/0.030); //  num voxels in Z
      int zz = int(voxZ*layer); //

      // is there a way not to do this each time?

      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = int(zz % layerVoxels);
      boolean invert = false;
      if (layer > 0.5){ invert = true;}

      float ratio = float(z)/ layerVoxels;
      getLayer(ratio, invert, depthChannel, alphaChannel, materChannel, int(size.x));
    }
  }

  void updateShell(){

    updateSolid();

  }

  void updateChannel(){



    int voxXY = int(DimXY/0.040); //

    depthChannel = depth.getMap(voxXY);
    alphaChannel = alpha.getMap(voxXY);
    materChannel = mater.getMap(voxXY);

  }


  void updateArray(){

    // println("get depth texture");
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

    if (vL != null){
    if (loaded == false){
      if (vL.isReady){
        currentLayer = vL.imgVisual;
        vL.stop();
        loaded = true;
      }
    } else {
      image(currentLayer, int(loc.x) , int(loc.y) );
    }
}


}

}

