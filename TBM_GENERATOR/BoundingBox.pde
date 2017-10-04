class BoundingBox{

  // bounding box

  boolean display = true;
  PVector pMin, pMax;
  float boxWidth, boxHeight, boxDepth;

  BoundingBox(float xMin,  float yMin, float zMin, float xMax, float yMax, float zMax){

    pMin = new PVector(xMin, yMin, zMin);
    pMax = new PVector(xMax, yMax, zMax);

    boxWidth = xMax - xMin;
    boxHeight = yMax - yMin;
    boxDepth = zMax - zMin;

  }


  void display(){

    if (display){
    stroke(34,155,215,150);
    strokeWeight(1);

    line(pMin.x,pMin.y,pMin.z, pMax.x, pMin.y,pMin.z);
    line(pMin.x,pMin.y,pMin.z, pMin.x, pMax.y,pMin.z);
    line(pMin.x,pMin.y,pMin.z, pMin.x, pMin.y,pMax.z);
    line(pMin.x,pMax.y,pMax.z, pMax.x, pMax.y,pMax.z);
    line(pMax.x,pMin.y,pMax.z, pMax.x, pMax.y,pMax.z);
    line(pMax.x,pMax.y,pMin.z, pMax.x, pMax.y,pMax.z);

    line(pMin.x,pMax.y,pMin.z, pMin.x, pMax.y,pMax.z);
    line(pMax.x,pMin.y,pMin.z, pMax.x, pMin.y,pMax.z);

    line(pMin.x,pMax.y,pMin.z, pMax.x, pMax.y,pMin.z);
    line(pMax.x,pMin.y,pMin.z, pMax.x, pMax.y,pMin.z);

    line(pMin.x,pMax.y,pMax.z, pMin.x, pMin.y,pMax.z);
    line(pMax.x,pMin.y,pMax.z, pMin.x, pMin.y,pMax.z);

    }

  }




}
