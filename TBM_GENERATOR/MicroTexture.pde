class MicroTexture{


  PImage map;
  int voxXY, dim;


  MicroTexture(String path, int _dim){
    map = loadImage(path);
    dim = _dim;
  }

  void update(){

    voxXY = int(DimXY/0.080);


  }

  float get(int x, int y){

  int tempX = x % dim; // gets the remainder
    int tempY = y % dim; // gets the remainder

    float val = brightness(map.get(tempX,tempY));
    return val;
  }



















}
