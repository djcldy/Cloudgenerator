
class PWindow extends PApplet {
  // this class displays 3D Content

  PShape cloud = null;

  PWindow() {
    super();
    PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);
  }


  float rotX = 0.05;
  float rotY =0.05;

  void settings() {
    size(1000, 1000, P3D);
      // fullScreen(P3D);
  }


  void setup() {
    background(150);
  }

 void setPointCloud(PShape s){

  cloud = s;
 }


   void draw() {
    background(0);
    fill(255);
    stroke(255);
    strokeWeight(10);
    pushMatrix();
    translate(width/2, height/2, 0);
    rotateX(rotY);
    rotateY(rotY);
    rotateZ(rotY);
    if (cloud != null) {
      shape(cloud);

    } else {
      point(0,0,0); }
    popMatrix();
    rotY +=0.025;
  }

  void mousePressed() {
    println("mousePressed in secondary window");
  }
}

