class UnitCell {

  // Thumb depth, alpha, mater;
  PShape pc = null;
  PVector loc, size;

  boolean isSelected = false;

  UnitCell(PVector _loc, PVector _size, PShape _pc){
    loc = _loc;
    size = _size;
    pc = _pc;
  }



  void setCloud(PShape _pc){
    pc = _pc;

  }

  boolean checkSelected(PVector ms) { // hmm we can  refactor this
    isSelected = false;
    if ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      isSelected = true;
    } else {
      isSelected = false;
    }
    return isSelected;
  }



  void display(){

    strokeWeight(1);
    stroke(34,155,215);
    noFill();

    if (isSelected){ strokeWeight(4);}

    pushMatrix();
    translate(loc.x,loc.y);
    rect(0,0,size.x,size.y);

    if (pc != null ){
      pushMatrix();
      translate(size.x/2, size.y/2);
      rotateX(PI/4);
      rotateZ(-PI/4);
      scale(.3);
      shape(pc);
      popMatrix();
    }
    popMatrix();




  }




















}
