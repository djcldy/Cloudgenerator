class Viewport3D{

  // class to control viewport3D // pointcloud
  ArrayList<PVector> pc;
  PVector p1, p2;

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

cellUnit.display(true);

  strokeWeight(1);
  stroke(34,155,215);
  strokeWeight(1);

}

}
