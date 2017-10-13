class Controller {

  // Controller  - class to control data flows and interface

  Model m;
  View v;

  PApplet app;

  int currentRow = 1;


  // can get rid of this ?
  ArrayList<Thumb> thumbs = new ArrayList<Thumb>();
  ArrayList<UnitCell> unitCells = new ArrayList<UnitCell>();

  Viewport2D vp;

  Zone zoneA, zoneB, zoneC, zoneD, zoneE, zoneF;  // select zones
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

    zoneA = new Zone(os, row3, col2,row4);
    zoneB = new Zone(os, row5, col4, row6);                 // row 2
    zoneC = new Zone(os, row7, col4, row8);                 //
    zoneD = new Zone(width/4, row0, col4, row4);                     //
     zoneE = new Zone(col5, row0, col10, row4);


     PVector dimGlobe = new PVector(width/6-os-os/2, width/6-os-os/2);
    // shaper = new Thumb(app, null,new PVector(width/2+ width/6+os, row5),  dimGlobe, "DEFAULT");

    zoneF = new Zone(width/2+width/6+os,row5,width/2+width/6+os+dimGlobe.x,row5+dimGlobe.y);


    zones.add(zoneA);
    zones.add(zoneB);
    zones.add(zoneC);
    zones.add(zoneD);
     zones.add(zoneE);
     zones.add(zoneF); // shaper tool

  }

  void init() {
    int bW = int((width/4 - width/32));
    int tW = int((width/4 - width/32)/3); // width of thumb with row of 3
    float y4 = height/16+width/64+bW;
    PVector dimThumb = new PVector(tW,tW);
    PVector dimVox = new PVector(width/6-os,width/6-os);
    PVector dimThumbView = new PVector(bW,bW);

    dimThumb = new PVector((width/2 - os-os/2)/6,(width/2 - os-os/2)/6);


    Thumb depth = new Thumb(app,"/textures/array/depth/bubble.png",   new PVector(os,y4),   dimThumb, "unit/depth",  "MONO");
    Thumb mater = new Thumb(app,"/textures/array/mater/manta.png", new PVector(os+dimThumb.x,y4),   dimThumb, "unit/mater", "COLOR");
    Thumb alpha = new Thumb(app,"/textures/array/alpha/solid.png",    new PVector(os+2*dimThumb.x,y4),  dimThumb, "unit/alpha", "MONO");

    thumbs.add(depth);
    thumbs.add(mater);
    thumbs.add(alpha);

    PShape shape = null;

    for (float x = os; x < width/2 - dimThumb.x; x += cellWidth){
      unitCells.add(new UnitCell(new PVector(x,row7), new PVector(cellWidth,cellWidth), shape ));
    }
    unitCells.get(0).isSelected = true;
    mater.isSelected = true;

    initViewport(new PVector(os, yA),  dimThumbView,  mater);

  }

  void setCurrentUC(PShape _pc){

    for (UnitCell uc : unitCells){
      if (uc.isSelected){
        uc.setCloud(_pc);
      }
    }

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

  if (regen){ currentRow = 1; }
  return regen;
}

void checkR2(Thumb th,  PVector ms){
        m.currentR2 = th.checkSelectedChildren();
        vp.set(m.currentR2);
        currentRow = 2;

}

void checkR3(PVector _ms){

  for (UnitCell cell : unitCells){
    if (cell.checkSelected(_ms)) {
      if (cell.pc != null){
        v.vp3D.setCellUnit(cell.pc);
        // v.vp3Darray.setCellArray(cell.pc);
      }
    }
  }

}


boolean unitSelect(PVector ms){

  boolean regen = true;

    if (zoneA.isSelected(ms)){
        regen = checkR1(m.thumbs, ms);
    } else if (zoneB.isSelected(ms)){
        if (m.currentR1 != null){checkR2(m.currentR1, ms);}
    } else if (zoneC.isSelected(ms)) {
       checkR3(ms);
       regen = false;
    }
    return regen;
  }

  void mouseDown(PVector ms) {

    if (v.vp3D.mode == "UNIT"){
      if (unitSelect(ms)){
        thread("RESETUNITCELL");
      }
    }
  }
}
