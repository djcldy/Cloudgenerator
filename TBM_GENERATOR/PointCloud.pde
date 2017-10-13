class PointCloud {

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc, currentLayerPC;
  PVector p1, p2, defaultPosition;

  BoundingBox bbox;
  ArrayList<BoundingBox> cellArray;

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  TweenPoint position, rotation, scale;

  float vWidth, vHeight, currentLayer;
  boolean mode; // mode 0 = max , mode 1 = min

  boolean spin;

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
    getBoundingBox(boxCloud);
  }


  void setCloudArray(PShape _pc){


    PVector min = new PVector(-200,-200,-200);

    boxCloud = _pc;
    getBoundingBox(boxCloud);

    float numZ = 3;
    float numY = 3;
    float numX = 3;

    float stepX = (bbox.pMax.x - bbox.pMin.x) / numX;
    float stepY = (bbox.pMax.y - bbox.pMin.y) / numY;
    float stepZ = (bbox.pMax.z - bbox.pMin.z) / numZ;


    cellArray = new ArrayList<BoundingBox>();

 //  for (float x = bbox.pMin.x; x < bbox.pMax.x; x += stepX){
 //   for (float y = bbox.pMin.y; y < bbox.pMax.y; y += stepY){
 //    for (float z = bbox.pMin.z; z < bbox.pMax.z; z += stepZ){
 //     cellArray.add(new BoundingBox(x,y,z, x + stepX, y + stepY, z + stepZ, p1, p2));
 //    // println("cellarrays = " + cellArray.size());

 //    }
 //   }
 // }

      }




  void setMode2(){ // ZOOM OUT

  // xScal = 1;
  //   xRot = -atan(sin(radians(45)));
  //   yRot =  radians(45);
  //   xPos = width/4 + vWidth/8;
  //   yPos = yB + vHeight/8 - os;

  }

  void setMode1(){ // ZOOM IN
    // xScal = 3;
    // xRot = -atan(sin(radians(45)));
    // yRot =  radians(45);
    // xPos = width/4 + vWidth/2;
    // yPos = vHeight/2-2*os;

  }

  void initView(){

    PVector start,scl,rot;
      float w = 3*width/8;
      scl = new PVector(0.75,0.75,0.75);
      rot = new PVector(-atan(sin(radians(-30))), radians(-30),0);
      start = new PVector((p2.x-p1.x)/2+p1.x, (p2.y-p1.y)/2 + p1.y, -10);
      defaultPosition = start;
    float val = 0.1;
    position = new TweenPoint(start,start,val); // tween for position
    scale = new TweenPoint(scl,scl,val); // tween for position
    rotation = new TweenPoint(rot,rot,val);
    setLeft();
  }

  void setLeft(){

    // println("set left");
    PVector rot = new PVector((radians(-90)), 0, 0);
    rotation.set(rot);

  }

  void setAxo(){
    // println("set axo");
    PVector rot = new PVector((radians(-45)), radians(-45), 0);
    rotation.set(rot);

  }


  void setRight(){
    println("set right");
    PVector rot = new PVector((radians(90)), 0, 0);
    rotation.set(rot);

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
    // getBoundingBox(_pc);
  }

   void updateCamera(){


    if (!mode){
      if (SpinX){   rotation.addIncrement(new PVector(0.005,0,0));  }
      if (SpinY){   rotation.addIncrement(new PVector(0,0.005,0));  }
      if (SpinZ){   rotation.addIncrement(new PVector(0,0,0.005));  }
    }

    if (bbox != null){
    if (bbox.grow){
      // println("GROW!!!");

      // scale.addIncrement(new PVector(0.01,0.01,0.01));
      scale.addIncrement(new PVector(0.0005,0.0005,0.0005));
    } else {
      scale.addIncrement(new PVector(-0.0005,-0.0005,-0.0005));
    }
    }

   }

   boolean isDisplay(float x, float y){
    boolean display = false; // check if point is in viewport
    if ((x > p1.x) && (x < p2.x) && (y > p1.y) && (y< p2.y)) { display = true;}
    return display;
   }

   void displayLayer(){


    if (currentLayerPC != null){
      // strokeWeight(1);
      stroke(255);
      for (PVector p: currentLayerPC){
        point(p.x, p.y, p.z);}
      }
   }


void displayFS(){
    // println("DISPLAY FULLSCREEN");
      pushMatrix();
      setView();

      stroke(255,100);
      fill(255,100);

      if (boxCloud != null){ shape(boxCloud);}
      if (bbox != null ){ bbox.display(true);}
      // // strokeWeight(1);
      // if (cellArray != null){
      //   for (BoundingBox cell: cellArray){ cell.display();}
      // }
      // if (showLayer){displayLayer();}

      popMatrix();
      updateCamera();

   }


  void display(boolean showLayer){

      pushMatrix();
      setView();

      stroke(255,100);
      fill(255,100);

      if (boxCloud != null){ shape(boxCloud);}
      if (bbox != null ){ bbox.display(false);}
      if (cellArray != null){ for (BoundingBox cell: cellArray){ cell.display(false);}}
      popMatrix();
      updateCamera();

   }

   void setView(){

      PVector pos = position.get(); // println(position)
      PVector scl = scale.get();
      PVector rot = rotation.get();

      // strokeWeight(1);
      stroke(255,100);
      translate(pos.x, pos.y, 0); //
      rotateX(rot.x);
      rotateY(rot.y);
      rotateZ(rot.z);
      scale(scl.x);


   }




  void setCurrent(ArrayList<PVector> _pc){ //
    currentLayerPC = _pc; // as percentage of a layer
   }

   void getBoundingBox(PShape _pc){


    // println("getBoundingBox:" + _pc.size());

    float xMin = 10;
    float yMin = 10;
    float zMin = 10;
    float xMax = 0;
    float yMax = 0;
    float zMax = 0;


    for (int i= 0; i < _pc.getVertexCount(); i++) {
      PVector p = _pc.getVertex(i);

      if (p.x < xMin){ xMin = p.x; }
      if (p.y < yMin){ yMin = p.y; }
      if (p.z < zMin){ zMin =  p.z; }

      if (p.x > xMax){ xMax = p.x; }
      if (p.y > yMax){ yMax = p.y; }
      if (p.z > zMax){ zMax = p.z; }

    }

    // println("BoundingBox: " + xMin + "," + yMin + "," + zMin+ "," + xMax + "," + yMax + "," + zMax);

    bbox = new BoundingBox(xMin,yMin,zMin,xMax,yMax,zMax,p1,p2);

   }




}
