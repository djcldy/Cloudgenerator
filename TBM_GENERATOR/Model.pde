class Model {

  // this class is all about modelling the 3D Geometry

  PImage slice;
  PGraphics pg;
  PointCloud pointCloud;
  Shape shaper;
  PApplet app;

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<Thumb> thumbsArray = new ArrayList<Thumb>();
  ArrayList<Thumb> thumbsGlobal = new ArrayList<Thumb>();

  CellArray cArray; // this object controls the array

  Thumb alpha, mater, depth, currentThumb;
  Thumb alphaArray, materArray, depthArray;
  Thumb alphaGlb, materGlb, depthGlb;

  Thumb currentR1, currentR2, currentR3;

  Voxelator vox;

  //  PGraphics matMap;
  int stepX = 2;
  int stepY = 2;
  int stepZ = 4;

  int step = 1;
  int thickness = 25; // thickness of each layer

  ArrayList<PVector> points = new ArrayList<PVector>();

  Model(PApplet _app) {

    app = _app;

    initChannelThumbs();

    depth = thumbs.get(0);
    mater = thumbs.get(1);
    alpha = thumbs.get(2);

    depthArray = depth.array(stepX,stepY);
    alphaArray = alpha.array(stepX,stepY);
    materArray = mater.array(stepX,stepY);

    thumbsArray.add(depthArray);
    thumbsArray.add(alphaArray);
    thumbsArray.add(materArray);

    initGlobalThumbs();
    initVoxel(thumbs, thumbsArray, thumbsGlobal);

    // pg = createGraphics(200, 200);

  }

  void updateArray(){
    thumbsArray = new ArrayList<Thumb>();
    thumbsArray.add(depthArray);
    thumbsArray.add(alphaArray);
    thumbsArray.add(materArray);
    vox.setArrays(thumbsArray);
  }



  void resetRows(String mode){

    currentR1 = null;
    currentR2 = null;
    currentR3 = null;


    if (mode == "UNIT"){

      for (Thumb th: thumbs){ if (th.isSelected){ currentR1 = th;}}
      if (currentR1 != null){ for (Thumb th: currentR1.children){ if (th.isSelected){ currentR2 = th;}}}

    } else {

     for (Thumb th: thumbsGlobal){ if (th.isSelected){ currentR1 = th;}}
      if (currentR1 != null){ for (Thumb th: currentR1.children){ if (th.isSelected){ currentR2 = th;}}}
    }
  }


  void initChannelThumbs(){

    int bW = int((width/4 - width/32));
    int tW = int((width/4 - width/32)/3); // width of thumb with row of 3
    float y4 = height/16+width/64+bW;

    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    depth = new Thumb(app, "/textures/array/depth/bubble.png",   new PVector(os,y4),   dimThumb, "unit/depth");
    mater = new Thumb(app, "/textures/array/mater/manta.png", new PVector(os+tW,y4),   dimThumb, "unit/mater");
    alpha = new Thumb(app, "/textures/array/alpha/solid.png",    new PVector(os+2*tW,y4),  dimThumb, "unit/alpha");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    mater.isSelected = true;
    currentR1 = materGlb;

  }

  void initGlobalThumbs(){

    int bW = int((width/4 - width/32));
    int tW = int((width/4 - width/32)/3); // width of thumb with row of 3

    float y4 = height/16+width/64+bW+os+tW;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    PVector dimGlobe = new PVector(width/6-os-os/2, width/6-os-os/2);

     // depthGlb = new Thumb(app,"/textures/global/depth/blank.png",   new PVector(os,y4),   dimThumb, "global/depth");
     // materGlb = new Thumb(app,"/textures/global/mater/blank.png", new PVector(os+tW,y4),   dimThumb, "global/mater");
     alphaGlb = new Thumb(app,"/textures/global/alpha/blank.png",new PVector(width/2+ width/6+os, row5),  dimGlobe, "global/alpha");
     shaper = new Shape(app,"/textures/global/alpha/blank.png",new PVector(width/2+ width/6+os, row5),  dimGlobe);
    // thumbsGlobal.add(depthGlb);
    // thumbsGlobal.add(materGlb);
    thumbsGlobal.add(alphaGlb);

    // materGlb.isSelected = true;
    // currentR1 = materGlb;

  }

  void initVoxel(ArrayList<Thumb> _units, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){

    //
    PVector dimVox = new PVector(width/6-os-os/2, width/6-os-os/2);


    // location , size
    initVoxelator(new PVector(width/2+ width/3+os/2, row5), dimVox, _units, _arrays, _globes);

  }

  void initVoxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs, ArrayList<Thumb> _arrays, ArrayList<Thumb> _globes){
      vox = new Voxelator(_loc, _size,  _thumbs, _arrays, _globes);

  }

  void arrayTexture(){

  }

  void displayCellUnit() {
    // getLayer(Current, false); // activate voxel map



    for (Thumb th : thumbs) {
      th.display();
    }
    // testSelection();
    // for (Thumb th: thumbsGlobal){
    //   th.display();
    // }
    shaper.display();

  }


  void displayCellArray() {

    for (Thumb th : thumbsArray) {
      th.display();
    }

    for (Thumb th: thumbsGlobal){
      th.display();
    }

  }


  void testSelection(){

    // println("test  selection: ");
      if (currentR1 != null){ currentR1.debug();}
      if (currentR2 != null){ currentR2.debug();}
      if (currentR3 != null){ currentR3.debug();}
  }


  ArrayList<PVector> resetPC(int res, float []a) {

    //Println("reset pointCloud");
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


}
