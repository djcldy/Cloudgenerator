class MicroTexture{

  PImage img = null;

  PImage map;
  int voxXY, dim;


  MicroTexture(String path, int _dim){
    map = loadImage(path);
    dim = _dim;
    map.resize(dim,dim);
    map = normalize(map);
    update();
    // PGraphics temp = createGraphics( )
  }



  void update(){

    voxXY = int(DimXY/0.080);
    PGraphics temp = createGraphics(voxXY, voxXY);
    temp.beginDraw();


    for (int x = 0;  x < temp.width; x += dim){
      for (int y = 0; y < temp.height; y+= dim){

        temp.image(map,x,y);

      }

    }

    temp.endDraw();
    img = temp.get();
    img.resize(int(width/6-os-os/2),int(width/6-os-os/2));

  }



  PImage normalize(PImage img) {

    PGraphics temp = createGraphics(img.width, img.height);

    temp.beginDraw();
    temp.image(img, 0, 0);
    temp.loadPixels();

    float min = 255;
    float max = 0;
    float brightness = 0;
    float sum = 0;

    for (int i = 0; i < temp.pixels.length; i++) {
      float val = brightness(temp.pixels[i]);
      brightness +=  val;
      if (val < min) min = val;
      if (val >max) max = val ;
    }

    float constant = 255.0/(max-min);
    float newMin  = 255.0;
    float tempRange = max-min;
    float newMax = 0;

    for (int i = 0; i <    temp.pixels.length; i++) {
      float val = brightness(temp.pixels[i]);
      float newVal = (val-min)*constant;
      temp.pixels[i] =  color(newVal,255);
    }
    temp.updatePixels();
    temp.endDraw();

    img = temp.get(); // normalized image

    return img;
  }


  float get(int x, int y){

  int tempX = x % dim; // gets the remainder
    int tempY = y % dim; // gets the remainder

    float val = brightness(map.get(tempX,tempY));
    return val;
  }


  void display(){

    if (img != null){
        image(img,col8-width/6+os/2,row4+os);
    }

  }

















}
