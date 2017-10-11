class Shape {

  ImageThread it;
  boolean loaded = false;
  PImage parent, texture;
  PGraphics map;
  PVector loc,size;
  PApplet app;
  String path, mode;
  boolean isSelected = false;

  Shape(PApplet _app, String _path, PVector _loc, PVector _size, String _mode) {
    app   =   _app;
    // name  =   _name;
    mode = _mode;
    size  =  _size;
    loc = _loc;
    path = _path;
    // println("thumb path = " + _path);
    reset(_path, _loc);
  }


  void reset(String _path, PVector _loc){
    println("reset: " + _path + "," + size);

    if (mode == "DEFAULT"){

      PGraphics temp = createGraphics(1024,1024);
      temp.beginDraw();
      temp.background(255);
      temp.endDraw();

      parent = temp.get();
      texture = parent.get();
      texture.resize(int(size.x), int(size.y));
      map = null;
      loaded = true;

    } else {
      it = new ImageThread(app, _path,size);
      it.start();
    }
  }


  void display(){



    if (loaded == false){
      if (it.isReady){
        // println("it is ready");
        parent = it.parent;//
        texture = it.texture;//
        map = it.map;
        it.stop();
        loaded = true;
      }
    }

    pushMatrix();
    translate(loc.x,loc.y);


    fill(34,155,215);
    if (loaded){
      image(texture,0,0);
    }else{
    rect(0,0,rA,rA);
    }
    popMatrix();


    noFill();
    if (isSelected) {

      stroke(163, 149, 41);
      rect(loc.x, loc.y, size.x, size.y); // Left
    } else if  ((mouseX > loc.x) && (mouseX < (loc.x + size.x)) && (mouseY > loc.y) && (mouseY < (loc.y + size.y))) {
      stroke(255, 100);
      rect(loc.x, loc.y, size.x, size.y); // Left
    }





  }


  // Shape(String _path) {

  //   println("load shape");

  //   String path = "/images/shape/grid.csv";
  //   Table table = loadTable(path);

  //   for (TableRow row : table.rows ()) {

  //     String str1 = row.getString(0);
  //     str1 = str1.replaceFirst("\\{", "");
  //     println(str1);

  //     float b = float(row.getString(1));

  //     println(row.getString(0)+ ","+ row.getString(1)+","+row.getString(2));
  //   }
  // }
}
