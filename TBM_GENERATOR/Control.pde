class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  // can get rid of this ?

  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  Viewport2D vp;
  Voxelator vox;

  Zone zoneA, zoneB, zoneC;  // select zones
  ArrayList<Zone> zones = new ArrayList<Zone>();

  Controller() {

    println("initialize controller");
    initSelector();
    init();
     m = new Model(thumbs);
     m.currentThumb = thumbs.get(1);
     v = new View(m,vp,vox);
    println("controller initialized");
  }

  void initSelector(){

     zoneA = new Zone(xA,yB,xB,yC);
     zoneB = new Zone(xA,yD,xB,yE);
     zoneC = new Zone(xA,yE,xB,yF);

    zones.add(zoneA);
    zones.add(zoneB);
    zones.add(zoneC);

  }

  void init() {
    int bW = int((width/4 - width/32));
    int tW = int((width/4 - width/32)/3); // width of thumb with row of 3
    float y4 = height/16+width/64+bW+os;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    Thumb depth = new Thumb("/images/depth/bubble.png",   new PVector(os,y4),   dimThumb, "depth");
    Thumb mater = new Thumb("/images/mater/manta.png", new PVector(os+tW,y4),   dimThumb, "mater");
    Thumb alpha = new Thumb("/images/alpha/solid.png",    new PVector(os+2*tW,y4),  dimThumb, "alpha");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    mater.isSelected = true;


    initViewport(new PVector(os, height/16+os),  dimThumbView,  mater);
    initVoxelator(new PVector(width*5/6, height-height/16-dimVox.y), dimVox, thumbs);

  }

  void initViewport(PVector _loc, PVector _size, Thumb _th){
    vp = new Viewport2D(_loc, _size, _th);
  }

  void initVoxelator(PVector _loc, PVector _size, ArrayList<Thumb> _thumbs){
      vox = new Voxelator(_loc, _size,  _thumbs);

  }

  void update() {
    PVector mouse = new PVector(mouseX, mouseY);
    v.display();
    // for  (Zone z: zones){ z.display(mouse);} // not necessary
  }


  void zoneC(PVector ms){

  Thumb current = m.currentThumb; // current thumb that is selected
  current.checkSelectedChildren();

  }

  void zoneA(){

  }


  void zoneB(PVector ms){

  // toggle zoneB
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

  void mouseDown(PVector ms) {
    if (zoneB.isSelected(ms)){
      zoneB(ms);
    } else if (zoneC.isSelected(ms)){
      zoneC(ms);
    }
  }
}
