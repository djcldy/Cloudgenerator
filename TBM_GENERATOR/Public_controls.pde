// PUBLIC controls


void addButton(ControlP5 c, String name, float x, float y, int w, int h){

  c.addButton(name)
     .setPosition(x,y)
     .setSize(w,h)
     ;

}

void addToggle(ControlP5 c, String name, float x, float y, int w, int h){
  c.addToggle(name)
     .setPosition(x,y)
     .setSize(w,h)
     .setValue(true)
     .setMode(ControlP5.SWITCH)
     ;
}
