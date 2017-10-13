class Voxelator {

  // PImage layer;
  PVector loc, size;
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> arrays = new ArrayList<Thumb>();
  ArrayList<Thumb> globes = new ArrayList<Thumb>();
  ArrayList<PVector> pcLayer = new ArrayList<PVector>();
  PGraphics pg;
  PImage currentLayer;

  float layer, thickness;


  PShape pointCloud;


  float dimX, dimY;

  Thumb depth, alpha, mater;
  Thumb depthArray, alphaArray, materArray;
  Thumb depthGlobe, alphaGlobe, materGlobe;

  boolean loaded = false;

  String mode = "UNIT";
  String fillMode = "SHELL";

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

    // depthGlobe = globes.get(0);
    // materGlobe = globes.get(1);
    // alphaGlobe = globes.get(2);

    layer = Current;
    thickness = Thickness;
    // update();

  }


  void toggleMode(){

    // do nothing //

  }


  void toggleFillMode(){

    println("toggleFillMode");
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
    updateUnit();
  }



  void updateUnit(){


    println("updateUnit");
    if (fillMode == "SHELL"){
      println("fill shell");
      updateShell();
    } else {
      println("fill solid");
      updateSolid();
    }
  }


void updateSolid(){


    if (pointCloud != null){

      int voxXY = int(DimXY/0.040); //  num voxels in X
      int voxZ = int(DimZ/0.030); //  num voxels in Z
      float layer = 0.33;
      int z = int(voxZ*layer);

      PGraphics temp = createGraphics(voxXY,voxXY);

      color black = color(255);

      temp.beginDraw();
      temp.background(0);

      for (int i = 0; i < s.getVertexCount(); i++) {
        PVector v = s.getVertex(i);
        if (v.z == z){
          temp.set(v.x,v.y, white);
        }
      }
      temp.endDraw();
      currentLayer = temp.resize(size.x,size.y);
    }

  // updateShell();
    // println("update solid");
    // if (depth.texture != null){

    // PImage img = depth.texture.get();
    // img.resize(int(size.x),int(size.y));

    // boolean invertLayer;

    // int res = 1;
    // float range = 255;
    // int numLevels = LayersZ;

    // float domain = range;
    // // float amp = Amplitude/levels; // this is the total height
    // float totalSlices = 255;
    // float subDomain = totalSlices/numLevels;

    // int currentSlice = int(totalSlices*layer); // current slice
    // int currentStep = floor(currentSlice/subDomain);
    // float currentLayer = (currentSlice%subDomain)/subDomain*255;

    // // println("Update Vox, currentLayer = " + currentLayer);

    // if (currentStep%2 == 0){
    //   invertLayer = false;
    // } else {
    //   invertLayer = true;
    // }


    // pg = createGraphics(int(size.x),int(size.y));
    // pg.beginDraw();
    // pg.background(0);
    // pg.loadPixels();

    // for (int k = 0; k <  img.pixels.length; k++) {
    //   float val = brightness(img.pixels[k]);
    //   if (val < currentLayer) { pg.pixels[k] = color(255);}
    // }
    //   pg.updatePixels();
    //   pg.endDraw();
    //   pcLayer = getCurrentPC(); // this displays the current cut layer in 3D


    // }
  }

  void updateShell(){



    if (pointCloud != null){

      int voxXY = int(DimXY/0.040); //  num voxels in X
      int voxZ = int(DimZ/0.030); //  num voxels in Z
      float layer = 0.33;
      int z = int(voxZ*layer);

      PGraphics temp = createGraphics(voxXY,voxXY);

      color black = color(255);

      temp.beginDraw();
      temp.background(0);

      for (int i = 0; i < s.getVertexCount(); i++) {
        PVector v = s.getVertex(i);
        if (v.z == z){
          temp.set(v.x,v.y, white);
        }
      }
      temp.endDraw();
      currentLayer = temp.resize(size.x,size.y);
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
      if (currentLayer != null){ image(currentLayer, int(loc.x) , int(loc.y) );}
    } else if (depth.texture != null){
      updateUnit();
      loaded = true;
    }
  }



// void RESETUNITCELL() {


  // BY DEFAULT THE UNIT CELL IS 1CM X 1CM X 1CM


  // int voxX = int(DimXY/0.040); //  num voxels in X
  // int voxY = int(DimXY/0.040); //  num voxels in Y
  // int voxZ = int(DimZ/0.030); //  num voxels in Z

  // int voxX = int(10/0.040); //  num voxels in X
  // int voxY = int(10/0.040); //  num voxels in Y
  // int voxZ = int(1/0.030); //  num voxels in Z


  // float inter = Intersection;



  // println("reset unit cell" + voxX + "," + voxY + "," + voxZ);
  // println("intersection = " + inter);

  // //////////////////////////////////////////////


  //   PImage depthChannel = c.m.depth.texture.get();
  //   PImage alphaChannel = c.m.alpha.texture.get();
  //   PImage materChannel = c.m.mater.texture.get();

  //   depthChannel.resize(voxX,voxY);
  //   alphaChannel.resize(voxX,voxY);
  //   materChannel.resize(voxX,voxY);

  // PShape boxCloud = createShape();
  // boxCloud.beginShape(POINTS);
  // boxCloud.stroke(255,150);
  // boxCloud.strokeWeight(1);

  //   ArrayList<PVector> temp = new ArrayList<PVector>();
  //   Thumb depth,alpha, mater; // place holder variables

  //   depth = c.m.depth;
  //   alpha = c.m.alpha;
  //   mater = c.m.mater;

  //   depthChannel.loadPixels();
  //   alphaChannel.loadPixels();
  //   materChannel.loadPixels();


  //   int res = 1;

  //   float rangeLo = 255*inter;
  //   float rangeHi = 255 - rangeLo;

  //   int levels = LayersZ;

  //   float layerVoxels = voxZ/levels;
  //   boolean invert = false;

  //   float max = 0;
  //   float min = 255;

  //   // get Pixel range
  //   for (int x = 0; x < depthChannel.width; x += 5) {
  //     for (int y = 0; y < depthChannel.height; y+= 5) {

  //     float value1 = brightness((alphaChannel.get(x,y)));
  //     float value2 =  brightness((depthChannel.get(x,y)));

  //       if (value1 > 0){
  //         if (value2 > max){max = value2;}
  //         else if (value2 < min){min = value2;}

  //       }
  //     }
  //   }



  // for (int z = 0; z < levels; z++){
  //   for (int x = 0; x < depthChannel.width; x += res) {
  //     for (int y = 0; y < depthChannel.height; y+= res) {
  //       float alph = brightness((alphaChannel.get(x,y)));

  //       if (alph > 0){
  //          float val = brightness(depthChannel.get(x, y));

  //          val =  (val-min)*(255/max);

  //          color c = materChannel.get(x,y);

  //          if (val > rangeHi )  { val = rangeHi;    }
  //          if (val < rangeLo) { val = rangeLo;  }

  //          if (invert){val = 255-val;}

  //          float voxLevel = (val-rangeLo)/(rangeHi-rangeLo)*layerVoxels; // height of voxel within this level

  //         boxCloud.stroke(red(c), green(c), blue(c),150);
  //          boxCloud.vertex(x-voxX/2, y-voxY/2, voxLevel + z*layerVoxels - voxZ/2);
  //         }
  //       }
  //     }

  //     invert = !invert; // need to do something hear to invert
  //   }


  // boxCloud.endShape();
  // c.v.vp3D.setCellUnit(boxCloud);
  // // c.v.vp3Darray.setCellArray(boxCloud);
  // c.setCurrentUC(boxCloud);
  //   c.m.vox.update();
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
