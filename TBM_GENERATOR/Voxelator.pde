class Voxelator {


  // PImage layer;

    color Cyan    = color(0,89,158);
    color Magenta = color(161,35,99);
    color Yellow  = color (213, 178, 0);
    color Black   = color(30,30,30);
    color White   = color(220,222,216);


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
  TextureField texField;
  MicroTexture microTexture;

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

    // initTextureField();
    initMicroTexture();
  }

  void initTextureField(){


    // String path_a = "/textures/blend/texture_a.png";
    // String path_b = "/textures/blend/texture_b.png";

    // textureField = new TextureField(path_a, path_b);

  }


  void initMicroTexture(){

    String path  =  "/textures/micro/texture_a.png";
    microTexture = new MicroTexture(path, 200);



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


  void update(){
    updateUnit();
  }


  void updateUnit(){

    if (pointCloud != null){
      // println("update voxelator: layer = " + layer);

      int voxXY = int(DimXY/0.080); //  num voxels in X
      int voxZ = int(DimZ/0.027); //  num voxels in Z
      int zz = int(voxZ*layer); //

      // is there a way not to do this each time?

      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer


      int z = int(zz % layerVoxels);




      boolean i = false;
      if (layer > 0.5){ i = true;}

      float ratio = float(z)/ layerVoxels;
      getLayer(ratio, i, depthChannel, alphaChannel, materChannel, int(size.x));
    }

  }


void getLayer(float ratio, boolean invert, PImage dC, PImage aC, PImage mC, int dim){

      microTexture.update();

      PImage imgExport = getVoxLayer(null, ratio,invert,dC,aC,mC);

      PImage imgVisual = imgExport.get();

      imgVisual.resize(dim,dim);

      currentLayer = imgVisual;


  // loaded = false;
  // vL = new VoxelLayer(pApp, ratio,invert,depthChannel,alphaChannel,materChannel, dim);
  // vL.start();

}












// void up


  void exportStack(){


    if (pointCloud != null){
      // println("exporting stack...");
      int voxXY = int(DimXY/0.080); //  num voxels in X

      // println("dimZ = " + DimZ);

      int voxZ = int(DimZ/0.027); //  num voxels in Z
      updateChannel();
      boolean invert = true;
      exportVoxels = new Exporter(pApp,microTexture, getRatio(),invert,depthChannel,alphaChannel,materChannel, voxXY, voxZ);
      exportVoxels.start();
    }

  }

  float getRatio(){

      int voxZ = int(DimZ/0.027); //  num voxels in Z
      int zz = int(voxZ*layer); //
      float layerVoxels = voxZ/LayersZ; // number of vertical voxels per layer
      int z = int(zz % layerVoxels);
      float ratio = float(z)/ layerVoxels;
      println("ratio = " + ratio);
      return ratio;

  }

  void updateSolid(){

    if (pointCloud != null){

      println("update voxelator: layer = " + layer);
      int voxXY = int(DimXY/0.080); //  num voxels in X
      int voxZ = int(DimZ/0.027); //  num voxels in Z
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



    int voxXY = int(DimXY/0.080); //

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

    // if (vL != null){
    if (currentLayer == null ){
      // if (vL.isReady){
      //   currentLayer = vL.imgVisual;
      //   vL.stop();
      //   loaded = true;
      // }
    } else {
      image(currentLayer, int(loc.x) , int(loc.y) );

      microTexture.display();
    }
  // }

  if (exportVoxels != null){
    if (exportVoxels.isReady){
      exportVoxels.stop();
      exportVoxels = null;
    }

  }

}


// PGraphics getVoxLayer(float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){

//     int voxXY = depthChannel.width;

//     // println("getVox layer:" + voxXY);

//     PGraphics temp = createGraphics(voxXY,voxXY);
//     float t = 0.5; // percentage for microtexture

//     temp.beginDraw();
//     temp.background(0);

//     for (int x = 0; x < temp.width; x ++){
//       for (int y = 0; y < temp.height; y++){

//         if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

//            float val = brightness(depthChannel.get(x, y));
//             color c = materChannel.get(x,y);
//             // float offset = microTexture.get(x,y); // offset for microtexture
//             float offset = 0;
//             // if (random(0,1)>0.999){println("offset:" + os*t);}
//             float pp = (val-offset*t)/255; //

//             if (pp < 0){ pp = 0;} // if the offset is less than zeo set to 0

//             // ratio is the current layer
//             // pp is the height of the given pixel

//             if (i){ //

//               if (pp > ratio) {

//                 temp.set(int(x), int(y), c);

//               } // below halfway



//             } else {

//               pp = 1 - pp;

//               if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway

//             }
//           }
//         }
//       }

//     temp.endDraw();
//     return temp;
//  }



// PGraphics getVoxLayer(float ratio, boolean i, PImage depthChannel, PImage alphaChannel, PImage materChannel){



//     int voxXY = depthChannel.width;

//     PGraphics temp = createGraphics(voxXY,voxXY);
//     temp.beginDraw();
//     temp.background(0);

//     for (int x = 0; x < temp.width; x ++) { for (int y = 0; y < temp.height; y++) {

//         if (brightness((alphaChannel.get(x,y))) > 0){ // is it black or white

//            float val = brightness(depthChannel.get(x, y));
//             color c = materChannel.get(x,y);
//             float pp = val/255;

//             if (i){
//               if (pp < ratio) { temp.set(int(x), int(y), c);} // below halfway
//             } else {
//               pp = 1 - pp;
//               if (pp > ratio) { temp.set(int(x), int(y), c);} // below halfway
//             }
//           }
//         }
//       }

//     temp.endDraw();
//     return temp;
//     }


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



}

