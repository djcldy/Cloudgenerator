class View {

  // this controls the styles of display
  Model model;
  Viewport2D vp;
  Voxelator vox;

  float thickness = 2;

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

  void setCurrent(float value) {
    // set the current layer
    current = int(value);
  }

  void display() {
    display3D();
    noStroke();

    // fill(2, 7, 49);
    // rect(0,0,width,height/16);
    vp.display();
    display2D();
  }

  void display2D() {
    vox.display();
    displayText();
    model.display();
    // vp.display();
  }

  void displayText() {

    stroke(255, 50);
    float y0 = width/64;
    float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;
    float y4 = height - y2;
    float y5 = height/2;

    float x0 = width/64;
    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5)/24);
    float x5 = (width*(13.75)/24);



    float y6 = height/16+width/64+width/4 - width/32;

    ////////////////////////////////////////
    // horizontal lines
    strokeWeight(1);
    // these guys below the thumb
    line(0, yE, width, yE);
    line(0, yE+os, width, yE+os);
    /////////////////////////////////////////////
    line(0, y2, width, y2);
    line(0, y2+y0, width, y2+y0);


    line(0, y4, width, y4);
    line(0, height-y0, width, height-y0);

    // vertical lines
    //strokeWeight(2);
    line(width/4-x0, 0, width/4-x0, height);
    line(x0, 0, x0, height);
    line(width-x0, 0, width-x0, height);

    line(width/4, 0, width/4, height);
    line(x1, 0, x1, height);


    // lables
    textSize(14);
    textAlign(CENTER);


    String s = "Ouputs";
    text(s, x2, y3);

    s = "Inputs";
    text(s, width/8, y3);


    //    s = "Rules";
    //    text(s,width/8, height - 300);

    s = "Parameters";
    text(s, x3, y3);

    textSize(11);

    //s = "Texture Primitives";
    //text(s,width/8, y2+18);

    s = "Mirror Plane";
    text(s, width*11/12, height/4 - 20);

    // drawZones();
    drawLabels();
  }

  void drawLabels() {

    // Small Labels

    textSize(10);
    textAlign(CENTER);

    text("CHANNELS", cA, rC);
    text("PRIMITIVES", cA, rD);
    text("RULE-CHAIN", cB, rD);
  }


  void drawZones() {

    stroke(34, 155, 215);
    strokeWeight(3);
    noFill();

    // Zone 1-1
    line(xA, 0, xB, yA);

    // Zone 2-1
    line(xA, yB, xB, yC);

    // Zone 3-1
    line(xA, yD, xB, yE);

    // Zone 4-1
    line(xA, yE, xB, yF);

    // rect(xA,yD, xB, yE);
  }



  void display3D() {

    strokeWeight(1);


    for (PVector p : model.points) {
      pushMatrix();
      translate(width*13/24, height/3);
      rotateX(xRot);
      rotateZ(zRot);
      scale(4);

      float x = p.x;
      float y = p.y;
      float z = p.z*Amplitude; 

      float scrnX = screenX(x, y, z);
      float scrnY1 = screenY(x, y, z + thickness/20);
      float scrnY2 = screenY(x, y, z - thickness/20);
      float scrnZ = screenZ(x, y, z);


      if ((scrnX> width/4)&& (scrnX < width*5/6)&&(scrnY1 > yB) && (scrnY1<yE)) {
        if ((z-thickness/20 > current - 2 ) && (z-thickness/20 < current + 2)) {
          stroke(255);
        } else {
          stroke(255, 25);
        }     
        point(x, y, z + thickness/20);
      }

      if ((scrnX> width/4)&& (scrnX < width*5/6)&&(scrnY2 > yB) && (scrnY2<yE)) {
        if ((z+thickness/20 > current - 2 ) && (z+thickness/20 < current + 2)) {
          stroke(255);
        } else {
          stroke(255, 25);
        }     
        point(x, y, z - thickness/20);
      }





      popMatrix();
    }

    xRot += 0.005;
    zRot += 0.005;
  }
}