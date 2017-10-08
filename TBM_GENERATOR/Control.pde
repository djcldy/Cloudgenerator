class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  PApplet app;

  // can get rid of this ?
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<UnitCell> unitCells = new ArrayList<UnitCell>();

  Viewport2D vp;

  Zone zoneA, zoneB, zoneC;  // select zones
  ArrayList<Zone> zones = new ArrayList<Zone>();

  Controller(PApplet _app) {

    initSelector();
    init();

    app = _app;
     m = new Model(app);
     m.currentThumb = thumbs.get(1);
     v = new View(m, vp);
     vp.set(m.depth);
     vp.m = m; // clean this up later

  }

  void initSelector(){

    zoneA = new Zone(xA, yD-os, xB, yE-os);                 // row 1
    zoneB = new Zone(xA, yE, xB, yE+tWidth);                // row 2
    zoneC = new Zone(xA, yE+tWidth+os, xB, yE+2*tWidth+os); // row 3

    zones.add(zoneA);
    zones.add(zoneB);
    zones.add(zoneC);

  }

  void init() {
    int bW = int((width/4 - width/32));
    int tW = int((width/4 - width/32)/3); // width of thumb with row of 3
    float y4 = height/16+width/64+bW;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    Thumb depth = new Thumb(app,"/textures/array/depth/bubble.png",   new PVector(os,y4),   dimThumb, "unit/depth");
    Thumb mater = new Thumb(app,"/textures/array/mater/manta.png", new PVector(os+tW,y4),   dimThumb, "unit/mater");
    Thumb alpha = new Thumb(app,"/textures/array/alpha/solid.png",    new PVector(os+2*tW,y4),  dimThumb, "unit/alpha");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);



    PShape shape = null;

    float cellWidth = (width/2 - os-os/2)/6;

    for (float x = os; x < width/2 - dimThumb.x; x += cellWidth){
      unitCells.add(new UnitCell(new PVector(x,row8), new PVector(cellWidth,cellWidth), shape ));
    }

    mater.isSelected = true;



    initViewport(new PVector(os, yA),  dimThumbView,  mater);

  }

  void initViewport(PVector _loc, PVector _size, Thumb _th){
    vp = new Viewport2D(_loc, _size, _th);
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

  void toggleMode(){
  // we run this when we switch


   v.vp3D.toggleMode();
   m.vox.toggleMode();
   vp.toggleMode();
   m.resetRows(v.vp3D.mode);
  thread("adjustGrid");

  }

  void zoneB(PVector ms, ArrayList<Thumb> _thumbs){
  boolean toggle = false;

  for (Thumb th : _thumbs) {
      if (th.checkSelected(ms)) {
        m.currentThumb = th;
        vp.set(m.currentThumb);
        toggle = true;
      }
    }
  }


boolean checkR1(ArrayList<Thumb> _thumbs,  PVector ms){

  boolean regen = true;   // R1 is the channel level of thumbs

  for (Thumb th : _thumbs) {

     boolean b1 = th.isSelected;

      if (th.checkSelected(ms)) {
        if (!b1){
          m.currentR1 = th;
          vp.set(th);
        } else {
          regen = false;
        }
      }

  }

  return regen;
}

void checkR2(Thumb th,  PVector ms){
        m.currentR2 = th.checkSelectedChildren();
        vp.set(m.currentR2);
}

void checkR3(ArrayList<Thumb> _thumbs,  PVector ms){


  for (Thumb th : _thumbs) {
      if (th.checkSelected(ms)) {
        m.currentR3 = th;
        vp.set(th);
      }
  }
}


boolean unitSelect(PVector ms){

  boolean regen = true;

    if (zoneA.isSelected(ms)){
        regen = checkR1(m.thumbs, ms);
      } else if (zoneB.isSelected(ms)){
        if (m.currentR1 != null){
          checkR2(m.currentR1, ms);
        }
    }


    return regen;
}



void arraySelect(PVector ms){
      // println("array select");
      if (zoneB.isSelected(ms)){
        checkR1(m.thumbsGlobal, ms);
      // println("zoneB: ");
      } else if (zoneC.isSelected(ms)){
        // println("checkZone");
        if (m.currentR1 != null){
          // println("zoneC");
          checkR2(m.currentR1, ms);
        } else {
          // println("zoneC = null");
        }
      }
}

  void mouseDown(PVector ms) {

    if (v.vp3D.mode == "UNIT"){
      if (unitSelect(ms)){
        thread("RESETUNITCELL");
      }
      // thread("RESETUNITCELL");
    } else {
      // arraySelect(ms);
      // thread("RESETARRAY");
    }
}
}
