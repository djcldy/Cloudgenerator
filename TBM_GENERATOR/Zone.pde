class Zone {

  PVector a,b,dim; // width and height of zone

  Zone(float x1, float y1, float x2, float y2){

     a = new PVector(x1,y1);
     b = new PVector(x2,y2);
     dim = new PVector(x2-x1,y2-y1);

  }

  boolean isSelected(PVector pt){
    // check if a point lies within the zone
    boolean select = false;

    if ((pt.x > a.x) && (pt.y > a.y) && (pt.x < b.x) && (pt.y < b.y)){
      select = true;
    }

    return select;

  }

  void display(PVector pt){

    stroke(255,25);
    strokeWeight(3);

    // noFill();

    if (isSelected(pt)){      rect(a.x,a.y,dim.x,dim.y);}

  }












}
