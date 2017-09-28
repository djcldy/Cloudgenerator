class View {

  // this controls the styles of display
  Model model;
  Viewport2D vp;
  Voxelator vox;

  // camera variables
  float xRot = 3.14/2;
  float zRot = 3.14/2;
  int current = 0; //?

  View(Model m, Viewport2D _vp, Voxelator _vox) {
    println("initialize view class") ;
    model = m;
    vp = _vp;
    vox  = _vox;
  }

  void setCurrent(float value){
    // set the current layer
    current = int(value);

  }

  void display() {
    vp.display();
    display2D();
    display3D();
  }

  void display2D() {
    vp.display();
    vox.display();
    displayText();
    model.display();
    // vp.display();
  }

  void displayText(){

    stroke(255,50);
    strokeWeight(1);

    float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;
    float y4 = height - y2;



    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5)/24);
    float x5 = (width*(13.75)/24);



    // horizontal lines
    line(0,y1,width/4,y1);
    line(0,y2,width,y2);
    line(0,y4,width,y4);

    // vertical lines
    line(width/4,0,width/4,height);
    line(x1,0,x1,height);
    line(x1,0,x1,height);
    // line(x4,y1,x4,height);
    // line(x2,y1,x2,height);

    // lables
    textSize(14);
    textAlign(CENTER);


    String s = "Ouputs";
    text(s,x2, y3);

    s = "Inputs";
    text(s,width/8, y3);

    s = "Parameters";
    text(s,x3, y3);

    textSize(11);

    s = "Texture Primitives";
    text(s,width/8, y2+18);

    s = "Mirror Plane";
    text(s,width*11/12, height/4 - 20);


  }

  void display3D() {

    strokeWeight(2);

    for (PVector p : model.points) {
      pushMatrix();
      translate(width*13/24, height/2);
      rotateX(xRot);
      rotateZ(zRot);
      scale(2);
      if ((p.z > current - 25) && (p.z < current + 25)) {
        stroke(255);
      } else {
        stroke(255, 25);
      }
      point(p.x, p.y, (p.z + Thickness)*Amplitude);
      point(p.x, p.y, (p.z - Thickness)*Amplitude);
      popMatrix();
    }

    xRot += 0.001;
    zRot += 0.001;
  }

}
