class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;
  float  ii = 0; // can get rid of this ?
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  Viewport2D vp;
  Voxelator vox;

  Controller() {

    println("initialize controller");
    init();
     m = new Model(thumbs);
     v = new View(m,vp,vox);

    println("controller initialized");
  }

  void init() {

    float o = 10; // offset variable
    float x1 = (width/14);
    float x2 = (width/8);
    float y1 = (height/14)+3*o;
    float x3 = x1*3+o*2;

    PVector dimThumb = new PVector(x1,x1);
    PVector dimVox = new PVector(width/6,width/6);
    PVector dimThumbView = new PVector(x3,x3);

    Thumb depth = new Thumb("/images/depth/bubble.png",   new PVector(x2-x1*1.5-o,  y1),   dimThumb, "depth");
    Thumb mater = new Thumb("/images/material/manta.png", new PVector(x2-x1*0.5,   y1),   dimThumb, "mater");
    Thumb alpha = new Thumb("/images/alpha/solid.png",    new PVector(x2+0.5*x1+o,  y1),  dimThumb, "alpha");


    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    mater.isSelected = true;

    initViewport(new PVector(x2-x3/2, y1+dimThumb.y + 20),  dimThumbView,  mater);
    initVoxelator(new PVector(width*11/12-dimVox.x/2, height-height/16-dimVox.y), dimVox, thumbs);

  }

  void initViewport(PVector _loc, PVector _size, Thumb _th){
    vp = new Viewport2D(_loc, _size, _th);
  }

  void initVoxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs){
      vox = new Voxelator(_loc, _size,  _thumbs);

  }

  void update() {
    v.display();
  }

  void mouseDown(PVector ms) {

    boolean toggle = false;

    for (Thumb th : m.thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
        vp.set(m.currentThumb);
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
