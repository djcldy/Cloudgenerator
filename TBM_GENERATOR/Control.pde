class Controller {

  Model m;
  View v;
  float  ii = 0;


  Thumb alpha, depth, mater, shape; // image channels

  Controller() {

    println("initialize controller");
    init();
     m = new Model(alpha, depth, mater, shape);
     v = new View(m);
    println("controller initialized");
  }

  void init() {
    println("initialize thumbs"); 
    alpha = new Thumb("/images/alpha/solid.png", new PVector(250, 520),new PVector(200, 200), "alpha");
    mater = new Thumb("/images/material/manta.png", new PVector(40, 750), new PVector(200, 200), "mater");
    depth = new Thumb("/images/depth/bubble.png", new PVector(40, 520), new PVector(200, 200), "depth");
    shape = new Thumb("/images/depth/bubble.png", new PVector(40, 40), new PVector(4, 410), "shape");

    //Shape grid = new Shape("/images/depth/bubble.png");


  }

  void update() {
    v.display();
    m.getLayer(Current, false);
  }

  void mouseDown(PVector ms) {

    boolean toggle = false;

    for (Thumb th : m.thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
        toggle = true;
      }
    }

    if (toggle) {
      for (Thumb th : m.thumbs) {
       if (th != m.currentThumb){
        th.isSelected = false;
       }

      }
    }

  }
}