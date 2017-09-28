class View { 

  Model model;
  
  // camera variables
  float xRot = 3.14/2;
  float zRot = 3.14/2;

  View(Model m) {
    println("initialize view class") ;
    model = m;
  }


  void display() { 
    display2D();  
    display3D();
  }

  void display2D() {
    model.display();
  }


  void display3D() {

    strokeWeight(2); 
    for (PVector p : model.points) { 
      pushMatrix(); 
      translate(width*0.66, height*.33);
      rotateX(xRot);
      rotateZ(zRot);  
      scale(1.5);
      if ((p.z > Current -Thickness) && (p.z < Current+Thickness)) { 
        stroke(255);
      } else { 
        stroke(255, 50);
      }
      point(p.x, p.y, (p.z + Thickness)*Amplitude);
      point(p.x, p.y, (p.z - Thickness)*Amplitude);
      popMatrix();
    }
    xRot += 0.005;
    zRot += 0.005;
  }
  
}