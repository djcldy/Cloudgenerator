class PointCloud {

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc, currentLayerPC;
  PVector p1, p2;

  BoundingBox bbox;

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  TweenPoint position, rotation, scale;

  float vWidth, vHeight, currentLayer;
  boolean mode; // mode 0 = max , mode 1 = min


PShape boxCloud;


  PointCloud(float x1, float y1, float x2, float y2, boolean _mode){

    p1 = new PVector(x1,y1);
    p2 = new PVector(x2,y2);

    vWidth = x2 -x1;
    vHeight = y2 - y1;

    mode = _mode;
    initView();

  }

  void setCloud(PShape _pc){
    boxCloud = _pc;
  }
  void setMode2(){ // ZOOM OUT

  xScal = 1;
    xRot = -atan(sin(radians(45)));
    yRot =  radians(45);
    xPos = width/4 + vWidth/8;
    yPos = yB + vHeight/8 - os;

  }

  void setMode1(){ // ZOOM IN
    xScal = 3;
    xRot = -atan(sin(radians(45)));
    yRot =  radians(45);
    xPos = width/4 + vWidth/2;
    yPos = vHeight/2-2*os;

  }

  void initView(){

    PVector start,scl,rot;

    if (mode){
      // setMode1();
      start = new PVector(width/4 + vWidth/8, yB + vHeight/8 - os, 0);
      scl = new PVector(1,1,1);
      rot = new PVector(-atan(sin(radians(45))), radians(45),0);

    }else{
      // setMode2();
      start = new PVector(width/4 + vWidth/2, vHeight/2-2*os);
      scl = new PVector(3,3,3);
      rot = new PVector(-atan(sin(radians(45))), radians(45),0);

    }

    float val = 0.1;

    position = new TweenPoint(start,start,val); // tween for position
    scale = new TweenPoint(scl,scl,val); // tween for position
    rotation = new TweenPoint(rot,rot,val);
  }

void updateView(){
  // println("updateview");
  PVector end,scl,rot;

    if (!mode){
      // setMode1();
      end = new PVector(width/4 + vWidth/8, yB + vHeight/8 - os, 0);
      scl = new PVector(1,1,1);
      rot = new PVector(-atan(sin(radians(45))), radians(45),0);
      rotation.set(rot);

    }else{
      // setMode2();
      end = new PVector(width/4 + vWidth/2, vHeight/2-2*os);
      scl = new PVector(3,3,3);
    }
    position.set(end);
    scale.set(scl);
  }


  void toggle(){
   updateView();

   mode = !mode;

  }

  void set(ArrayList<PVector> _pc){
    // println("Pointcloud list: " + _pc.size());
    pc = _pc;
    getBoundingBox(_pc);
  }

   void updateCamera(){

    if (!mode){
      rotation.addIncrement(new PVector(0.005,0,0.005));
      // yRot += 0.005;
      //  zRot += 0.005;
    }

   }

   boolean isDisplay(float x, float y){
    boolean display = false; // check if point is in viewport
    if ((x > p1.x) && (x < p2.x) && (y > p1.y) && (y< p2.y)) { display = true;}
    return display;
   }

   void displayLayer(){


    if (currentLayerPC != null){
      strokeWeight(1);
      stroke(255);
      for (PVector p: currentLayerPC){
        point(p.x, p.y, p.z);}
      }
   }

  void display(boolean showLayer){

      PVector pos = position.get(); // println(position)
      PVector scl = scale.get();
      PVector rot = rotation.get();

      strokeWeight(1);
      stroke(255);

      pushMatrix();
      // translate(-bbox.boxWidth/2, -bbox.boxHeight/2,-bbox.boxDepth/2);
      translate(pos.x, pos.y); //??

      rotateX(rot.x);
      rotateZ(rot.y);

      scale(scl.x);


      drawPoints();

      if (showLayer){displayLayer();}
      if (bbox != null ){ bbox.display();}


      popMatrix();
      updateCamera();

   }

  void drawCurrent(){


  }

   void drawPoints(){

      strokeWeight(2);
      stroke(255,50);

      // int res = 2;

      // if (mode){
      //   res = 16;
      // }
        if (boxCloud != null){shape(boxCloud);}

      // for (int i = 0; i < pc.size(); i += res) {

      // PVector p = pc.get(i);

      // float x = p.x;
      // float y = p.y;
      // float z1 = p.z*Amplitude + 1;
      // float z2 = p.z*Amplitude - 1;

      // float scrnX1 = screenX(x, y, z1);
      // float scrnX2 = screenX(x, y, z2);
      // float scrnY1 = screenY(x, y, z1); // top layer
      // float scrnY2 = screenY(x, y, z2); // bottom layer






      // if (isDisplay(scrnX1,scrnY1)) { point(x, y, z1); }
      // if (isDisplay(scrnX2,scrnY2)) { point(x, y, z2); }

      // }

    }

  void setCurrent(ArrayList<PVector> _pc){ //
    currentLayerPC = _pc; // as percentage of a layer
   }

   void getBoundingBox(ArrayList<PVector> _pc){

    // println("getBoundingBox:" + _pc.size());

    float xMin = 10;
    float yMin = 10;
    float zMin = 10;
    float xMax = 0;
    float yMax = 0;
    float zMax = 0;


    for (PVector p: _pc) {

      float z1 = p.z*Amplitude+5;
      float z2 = p.z*Amplitude-5;

      if (p.x < xMin){ xMin = p.x; }
      if (p.y < yMin){ yMin = p.y; }
      if (z1 < zMin){ zMin = z1; }
      if (z2 < zMin){ zMin = z2; }

      if (p.x > xMax){ xMax = p.x; }
      if (p.y > yMax){ yMax = p.y; }
      if (z1 > zMax){ zMax = z1; }
      if (z2 > zMax){ zMax = z2; }

    }

    // println("BoundingBox: " + xMin + "," + yMin + "," + zMin+ "," + xMax + "," + yMax + "," + zMax);

    bbox = new BoundingBox(xMin,yMin,zMin,xMax,yMax,zMax);

   }




}
