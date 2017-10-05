class View {

  // this controls the styles of display
  Model model;
  Viewport2D vp2D;
  Viewport3D vp3D;
  Voxelator vox;

  float thickness = 2;

  // camera variables
  float xRot = 3.14/2;
  float zRot = 3.14/2;
  float current = 0; //?

  View(Model m, Viewport2D _vp) {
    // println("initialize view class") ;

    model = m;
    vp2D = _vp;
    vp3D = new Viewport3D(xC, yA, xD, yF-height/16);


  }

  void setCurrent() {
    println("set current");
    vox.getCurrentPC();
  }

  void display() {

    String mode = vp3D.mode;
    display2D();
    display3D();

  }

  void display2D() {

    // drawZones();
    drawFrames();
    drawLabels();
    model.vox.display(); // this one ok


    if (vp3D.mode == "UNIT"){
      vp2D.display();   // this is just a screen
      model.displayCellUnit();  //
    } else {
      vp2D.displayCellArray();
      model.displayCellArray();
    }



  }

  void display3D() {
    vp3D.display();

  }

  void drawFrames() {

    // clean this up

    stroke(255, 50);

    float y1 = (height*2)/3;
    float y2 = (height/16);
    float y3 = y2/2;
    float y4 = height - y2;
    float y5 = height/2;

    float x1 = (width*5)/6;
    float x2 = (width*13)/24;
    float x3 = x1 + (width-x1)/2;
    float x4 = (width*(16.5)/24);
    float x5 = (width*(13.75)/24);
    float y6 = height/16+width/64+width/4 - width/32;

    ////////////////////////////////////////
    // horizontal lines
    strokeWeight(2);
    line(0, yA, width, yA);
    line(0, y4, width, y4);
    line(0, height-os, width, height-os);

    // vertical lines
    line(width/4-os, 0, width/4-os, height);
    line(os, 0, os, height);
    line(width-os, 0, width-os, height);
    line(width/4, 0, width/4, height);
    line(x1, 0, x1, height);
    int y10 = int(height/16+width/64+ width/4 - width/32);
    line(0,y10-os, width/4, y10-os);
    line(0,y10, width/4, y10);
    line(0,y10 + tWidth + os, width/4, y10 + tWidth + os);
    line(0,y10 + 2*(tWidth + os), width/4, y10 + 2*(tWidth+os));
    line(0,y10, width/4, y10);
    line(0,y10+ tWidth, width/4-os, y10 + tWidth);
    line(0,y10 + 2*(tWidth)+os, width/4, y10 + 2*(tWidth)+os);


  }

  void drawLabels() {

    // Small Labels
    // textSize(18);
    // textAlign(CENTER);

    textSize(11);
    textAlign(CENTER);
    String s = "PointCloud Generator - Sayjel Vijay Patel - 2017";
    text(s, width/2, height-os/2);

    // textAlign(CENTER);



    textSize(14);
    s = "Mode: " + vp3D.mode;
    text(s, cA, rA);

    textSize(11);
    // text("CHANNELS", cA, rC);

    //     line(xA, yD-os, xB, yE-os);  // row1
    // line(xA, yE, xB, yE+tWidth); // row2
    // line(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row3

    if (vp3D.mode == "UNIT"){

    text("TEXTURE: CHANNELS", cA, yD-os-os/2); // row1 //
    text("TEXTURES", cA, yE-os/2); // row2 //

    } else {
    text("UNITCELLS", cA, yD-os-os/2); // row1 //
    text("SHAPES: CHANNELS", cA, yE-os/2); // row2 //
    text("SHAPES", cA, yE+tWidth+os-os/2); // row2 //

    }
    // text("RULE-CHAIN", cB, rD);


    // String s = "Ouputs";
    // text(s, x2, y3);


    // s = "Parameters";
    // text(s, x3, y3);

    // textSize(11);

    // s = "Mirror Plane";
    // text(s, width*11/12, height/4 - 20);
  }


  void drawZones() {

    stroke(34, 155, 215);
    strokeWeight(3);
    noFill();

    // Zone Row 1
    // line(xA, 0, xB, yA);

    // Zone Row 1?
    // line(xA, yB, xB, yC);

    // Zone 3-1
    line(xA, yD-os, xB, yE-os);  // row1
    line(xA, yE, xB, yE+tWidth); // row2
    line(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row3
    // rect(xA,yD, xB, yE);


  }




}
