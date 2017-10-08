class UnitCell {

  // Thumb depth, alpha, mater;
  PShape pc;
  PVector loc, size;

  UnitCell(PVector _loc, PVector _size, PShape _pc){
    loc = _loc;
    size = _size;
    pc = _pc;

  }


  void display(){

    strokeWeight(1);
    stroke(34,155,215);
    noFill();

    pushMatrix();
    translate(loc.x,loc.y);
    rect(0,0,size.x,size.y);
    popMatrix();

  }




















}
