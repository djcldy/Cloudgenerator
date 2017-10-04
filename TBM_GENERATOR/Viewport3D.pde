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

  String mode = "UNIT"; // set as "UNIT" or "GLOBAL"

  Viewport3D(float x1, float y1, float x2, float y2){
    cellUnit = new PointCloud(x1,y1,x2,y2, false);
    cellArray = new PointCloud(x1,y1,x2,y2, true);

  }

  void setCellUnit(PShape _pc){
    // println("set cell unit = " + _pc.size());

    cellUnit.setCloud(_pc);
    // cellUnit.set(_pc);
  }

  void setCellArray(ArrayList<PVector> _pc){
     println("set cell array = " + _pc.size());
    cellArray.set(_pc);
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
    println("vp3d: setCurrentLayer");
    if (mode == "UNIT"){
    cellUnit.setCurrent(_pc);
    } else {
    cellArray.setCurrent(_pc);
    }
  }

  void display(){



    if (mode == "UNIT"){
        cellUnit.display(true);
        cellArray.display(false);
      } else {

        cellUnit.display(false);
        cellArray.display(true);
    }

  }




}
