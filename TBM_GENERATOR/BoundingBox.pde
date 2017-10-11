class BoundingBox{

  // bounding box

  boolean grow =  false;
  boolean display = true;
  PVector pMin, pMax, p1,p2;
  float boxWidth, boxHeight, boxDepth;


  BoundingBox(float xMin,  float yMin, float zMin, float xMax, float yMax, float zMax, PVector _p1, PVector _p2){

    pMin = new PVector(xMin, yMin, zMin);
    pMax = new PVector(xMax, yMax, zMax);
    p1 =  new PVector( _p1.x+50 , _p1.y+50);
    p2 =  new PVector( _p2.x-50 , _p2.y-50);

    boxWidth = xMax - xMin;
    boxHeight = yMax - yMin;
    boxDepth = zMax - zMin;

  }


  boolean checkExtents(PVector min, PVector max){

    boolean maximize = true;

    float x1 = screenX(pMin.x,pMin.y,pMin.z);
    float y1 = screenY(pMin.x,pMin.y,pMin.z);
    float z1 = screenZ(pMin.x,pMin.y,pMin.z);


    PVector v2=  new PVector(pMax.x, pMin.y, pMax.z);

    float x2 = screenX(v2.x,v2.y,v2.z);
    float y2 = screenY(v2.x,v2.y,v2.z);
    float z2 = screenZ(v2.x,v2.y,v2.z);

v2=  new PVector(pMax.x, pMax.y, pMin.z);

    float x3 = screenX(v2.x,v2.y,v2.z);
    float y3 = screenY(v2.x,v2.y,v2.z);
    float v3 = screenZ(v2.x,v2.y,v2.z);


v2=  new PVector(pMin.x, pMax.y, pMax.z);

    float x4 = screenX(v2.x,v2.y,v2.z);
    float y4 = screenY(v2.x,v2.y,v2.z);
    float v4 = screenZ(v2.x,v2.y,v2.z);

v2=  new PVector(pMin.x, pMin.y, pMax.z);

    float x5 = screenX(v2.x,v2.y,v2.z);
    float y5 = screenY(v2.x,v2.y,v2.z);
    float v5 = screenZ(v2.x,v2.y,v2.z);

v2=  new PVector(pMin.x, pMax.y, pMin.z);

    float x6 = screenX(v2.x,v2.y,v2.z);
    float y6 = screenY(v2.x,v2.y,v2.z);
    float v6 = screenZ(v2.x,v2.y,v2.z);


    float x8 = screenX(pMax.x,pMax.y,pMax.z);
    float y8 = screenY(pMax.x,pMax.y,pMax.z);
    float z8 = screenZ(pMax.x,pMax.y,pMax.z);

    if ((x8 < max.x) && (x1 < max.x) && (x2 < max.x)
     && (x3 < max.x) && (x4 < max.x) && (x5 < max.x) && (x6 < max.x) && (y8 < max.y) && (y1 < max.y)
     && (y2 < max.y) && (y3 < max.y)&& (y4 < max.y)  && (y5 < max.y) && (y6 < max.y) && (y1 > min.y)
     && (y2 > min.y) && (y3 > min.y)&& (y4 > min.y)&& (y5 > min.y)&& (y6 > min.y) && (y8 > min.y)&& (x1 > min.x)
     && (x2 > min.x) && (x3 > min.x)&& (x4 > min.x)&& (x5 > min.x)&& (x6 > min.y) && (x8 > min.x)){
      maximize = true;
    } else {
      maximize = false;
          }

    return maximize;

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

    grow = checkExtents(p1,p2);
  }




}
