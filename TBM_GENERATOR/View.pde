class View {

  // this controls the styles of display
  int viewMode = 2;
  Model model;
  Viewport2D vp2D;
  Viewport3D vp3D, vp3Darray;
  Voxelator vox;
  // Control control;

  float thickness = 2;

  // camera variables
  float xRot = 3.14/2;
  float zRot = 3.14/2;
  float current = 0; //?

  View(Model _m, Viewport2D _vp) {
    // println("initialize view class") ;
    model = _m;
    // control = _C;
    vp2D = _vp;

    vp3D      = new Viewport3D(xC, yA, width/2 - os/2, y10+tWidth);
    vp3Darray = new Viewport3D(width/2 + os/2 , yA, width-os, y10+tWidth);

  }

  void display() {

    if (viewMode == 2){
      String mode = vp3D.mode;
      display2D();
      display3D();

    } else {

      // background(255);
      display3D();
      // saveFrame("output2/line-######.jpg");

    }

  }

  void display2D() {

    // drawZones();
    drawFrames();
    drawLabels();

    for (UnitCell cell : c.unitCells){
      cell.display();
    }


    if (vp3D.mode == "UNIT"){
      vp2D.display();   // this is just a screen
      model.displayCellUnit();  //
    } else {
      // vp2D.displayCellArray();
      // model.displayCellArray();
    }

    model.vox.display(); // this one ok

  }

  void display3D() {
    vp3D.display();

    vp3Darray.display();

  }

  void drawFrames() {


    stroke(255, 50);


    drawColumns();
    drawRows();



  }

  void drawRows(){
    // ROWS


    // ROW 0

    line(0, yA, width, yA);

    // ROW 1-2

    line(0, row1, col3, row1);
    line(0, row2, col3, row2);

    // ROW 3-4

    line(0, row3, col3, row3);
    line(0,row4, width, row4);

    // ROW 5-6
    line(0,row5, width, row5);
    line(0,row6 , col4,row6 );

    // ROW 7-8
    line(0,row7 , width/2-os/2,row7 );


  }
  void drawColumns(){

    //  COLUMNS

  // colum 1-2
    line(os, 0, os, height);

    // column 3-4
    line(width/4-os, 0, width/4-os, row5);
    line(width/4, 0, width/4, row5);

    // column 5-6
    line(width/2 - os/2,0, width/2 - os/2, height);
    line(width/2+os/2,0, width/2+os/2, height);

    // column 7-8
    line(width/2+  width/6 +os,row5,width/2 +  width/6+os, height);
    line(width/2+  width/6,row5,width/2 +  width/6, height);

    // column 9-10
    line(width/2+  width/3+os/2,row5,width/2 +  width/3+os/2, height);
    line(width/2+  width/3-os/2,row5,width/2 +  width/3-os/2, height);

    //
    // colum 11-12
    line(width-os, 0, width-os, height);


  }

  void toggleMode(){

      println("toggleMode");


    if (viewMode == 2){

      viewMode = 1;
      vp3D.setView(1);
      vp3D.fullScreen = true;
      cp5.hide();

    } else {

      viewMode = 2;
      vp3D.setView(2);
      vp3D.fullScreen = false;
      cp5.show();


    }

  }


  void drawLabels() {

    // Small Labels
    // textSize(18);
    // textAlign(CENTER);

    fill(255);

    textSize(11);
    textAlign(CENTER);
    // String s = "PointCloud Generator - Sayjel Vijay Patel - 2017";
    // text(s, width/2, height-os/2);

    // textAlign(CENTER);

    textSize(14);
    // String s = "Mode: " + vp3D.mode;
    // text(s, cA, rA);

    textSize(11);
    // text("CHANNELS", cA, rC);

    //     line(xA, yD-os, xB, yE-os);  // row1
    // line(xA, yE, xB, yE+tWidth); // row2
    // line(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row3

    if (vp3D.mode == "UNIT"){

    // text("TEXTURE: CHANNELS", cA, yD-os-os/2); // row1 //
    // text("TEXTURES", cA, yE-os/2); // row2 //

    } else {
    // text("UNITCELLS", cA, yD-os-os/2); // row1 //
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
    strokeWeight(1);
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
