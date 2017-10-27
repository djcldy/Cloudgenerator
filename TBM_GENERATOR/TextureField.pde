class TextureField {

  String path_a, path_b;

  int voxXY = 1;
  int voxZ = 1;

  PImage a,b, tempA, tempB;

  TextureField(String _path_a, String _path_b){

    // path_a = _path_a;
    // path_b = _path_b;

    // tempA = loadImage(path_a);
    // tempB = loadImage(path_b);

    init();
    update();

  }


  void init(){

    // a = tempA.get();
    // b = tempB.get();



  }

  void update(){

    voxXY = int(DimXY/0.080);
    voxZ = int(DimZ/0.027);

    // int xy = int(DimXY/0.080);
    // int z = int(DimZ/0.030);

    // if (voxXY != xy){
    //   voxXY = xy; //  resizeXY
    //   a = tempA.get();
    //   b = tempB.get();
    //   a.resize(dimXY, dimXY);
    //   b.resize(dimXY,dimXY);
    // }

    // voxZ = z //  resizeZ

  }


  color get(int x, int y, int z){

    color c1 = a.get(x,y);
    color c2 = b.get(x,y);
    float t = float(z)/DimZ;

    float r1 = red(c1);
    float r2 = red(c2);
    float g1 = green(c1);
    float g2 = green(c2);
    float b1 = blue(c1);
    float b2 = blue(c2);

    float r = r1*t + r2*(1-t);
    float g = g1*t + g2*(1-t);
    float bl = b1*t + b2*(1-t);

    return color(r,g,bl);

  }












}
