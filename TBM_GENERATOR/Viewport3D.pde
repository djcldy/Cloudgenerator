class Viewport3D{

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc;
  PVector p1, p2;

  boolean fullScreen = false;

  BoundingBox bbox;

  float currentLayer = 0.5;

  PointCloud cellUnit, cellArray;

  float xRot, yRot, zRot;  // rotate camera
  float xScal, yScale, zScale; // scale camera
  float xPos, yPos, zPos;
  float vWidth, vHeight;

  boolean pauseView = false;

  String mode = "UNIT"; // set as "UNIT" or "GLOBAL"



  Viewport3D(float x1, float y1, float x2, float y2){
    cellUnit = new PointCloud(x1,y1,x2,y2, false);
    cellArray = new PointCloud(x1,y1,x2,y2, true);

  }


  void setView(int viewMode){

    if (viewMode == 2){
      // println("set position 2");
      PVector p1 = cellUnit.p1;
      PVector p2 = cellUnit.p2;
      PVector start = new PVector((p2.x-p1.x)/2+p1.x, (p2.y-p1.y)/2 + p1.y, -10);
      cellUnit.position.set(start);
      cellUnit.scale.set(new PVector(1,1,1));

      fullScreen = false;

    } else {

      cellUnit.position.set(new PVector(width/2,height/2));
      cellUnit.scale.set(new PVector(3,3,3));
      fullScreen = true;
    }

  }

  void toggleSpin(){
    cellUnit.spin = !cellUnit.spin;

  }

  void setCellUnit(PShape _pc){
    cellUnit.setCloud(_pc);
  }

  void setCellArray(PShape _pc){
    cellUnit.setCloudArray(_pc);
  }

  void toggleMode(){

    cellUnit.toggle();
    cellArray.toggle();

    if (mode == "UNIT"){
      mode = "ARRAY";
    } else {
      mode = "UNIT";
    }

  }

  void setCurrentLayer(ArrayList<PVector> _pc){
    if (mode == "UNIT"){
    cellUnit.setCurrent(_pc);
    } else {
    cellArray.setCurrent(_pc);
    }
  }

  void display(){

    if (!fullScreen){
      cellUnit.display(true);
    } else {
      cellUnit.displayFS();
    }

  strokeWeight(1);
  stroke(34,155,215);
  strokeWeight(1);

  }

}
