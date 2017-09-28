// "Texture Generator Code"
// Data-Driven 3D Sampling Project
// Sayjel Vijay Patel (2017), SUTD Digital Manufacturing & Design Centre
// Re-factored (2017-09)

import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;
import controlP5.*;
import java.util.*;


// what is this
ControlP5 cp5;

int myColor = color(0, 0, 0);
float Amplitude = 0.1;
float Thickness = 10;
int  Current = 10;
int sliderTicks1 = 100;
int sliderTicks2 = 30;
Slider abc;
//

Controller c;


void setup() {
  println("starting");
  smooth();
  background(0, 51, 102);
  //size(1000, 600, P3D);
  fullScreen(P3D);
  c = new Controller();
}


void draw() {
  background(2, 7, 49);
   c.update();
}



void mousePressed() {
  // Mouse Event
  PVector mouseEvent = new PVector(mouseX, mouseY);
  c.mouseDown(mouseEvent); // mouse pressed
}

void event(ControlEvent theEvent) {
  println(theEvent.getArrayValue());
}


void initSlider() {
  cp5 = new ControlP5(this);

  // add a horizontal sliders, the value of this slider will be linked
  // to variable 'sliderValue'
  cp5.addSlider("Amplitude")
    .setPosition(1300, 50)
    .setWidth(200)
    .setRange(0.025, 0.5)
    ;

  cp5.addSlider("Thickness")
    .setPosition(1300, 75)
    .setWidth(200)
    .setRange(1, 20)
    ;


  cp5.addSlider("Current")
    .setPosition(1300, 100)
    .setWidth(200)
    .setRange(0, 255) // values can range from big to small as well
    .setValue(100)
    .setNumberOfTickMarks(255)
    .setSliderMode(Slider.FLEXIBLE)
    ;


  cp5.addSlider("Layers")
    .setPosition(1300, 125)
    .setWidth(200)
    .setRange(1, 20) // values can range from big to small as well
    .setValue(4)
    .setNumberOfTickMarks(10)
    .setSliderMode(Slider.FLEXIBLE)
    ;

  //    List l = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
  //    /* add a ScrollableList, by default it behaves like a DropdownList */
  //      cp5.addScrollableList("textures")
  //     .setPosition(1300, 150)
  //     .setSize(200, 100)
  //     .setBarHeight(20)
  //     .setItemHeight(20)
  //     .addItems(l)
  //      .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
  //     ;
  //
  //      cp5.addScrollableList("heightfield")
  //     .setPosition(1300, 300)
  //     .setSize(200, 100)
  //     .setBarHeight(20)
  //     .setItemHeight(20)
  //     .addItems(l)
  //      .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
  //     ;
  //
  //    cp5.addScrollableList("alpha")
  //     .setPosition(1300, 450)
  //     .setSize(200, 100)
  //     .setBarHeight(20)
  //     .setItemHeight(20)
  //     .addItems(l)
  //      .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
  //     ;


  // create another slider with tick marks, now without
  // default value, the initial value will be set according to
  // the value of variable sliderTicks2 then.
  //cp5.addSlider("sliderTicks1")
  //   .setPosition(100,140)
  //   .setSize(20,100)
  //   .setRange(0,255)
  //   .setNumberOfTickMarks(5)
  //   ;


  // add a vertical slider
  //cp5.addSlider("slider")
  //   .setPosition(100,305)
  //   .setSize(200,20)
  //   .setRange(0,200)
  //   .setValue(128)
  //   ;

  //// reposition the Label for controller 'slider'
  //cp5.getController("slider").getValueLabel().align(ControlP5.LEFT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);
  //cp5.getController("slider").getCaptionLabel().align(ControlP5.RIGHT, ControlP5.BOTTOM_OUTSIDE).setPaddingX(0);



  // use Slider.FIX or Slider.FLEXIBLE to change the slider handle
  // by default it is Slider.FIX
}


//void setup() {


//}

//void draw() {
//  background(sliderTicks1);

//  fill(sliderValue);
//  rect(0,0,width,100);

//  fill(myColor);
//  rect(0,280,width,70);

//  fill(sliderTicks2);
//  rect(0,350,width,50);
//}

//void slider(float theColor) {
//  myColor = color(theColor);
//  println("a slider event. setting background to "+theColor);
//}

//
//void setup() {
//  size(400, 400);
//  cp5 = new ControlP5(this);
//  List l = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
//  /* add a ScrollableList, by default it behaves like a DropdownList */
//  cp5.addScrollableList("dropdown")
//     .setPosition(100, 100)
//     .setSize(200, 100)
//     .setBarHeight(20)
//     .setItemHeight(20)
//     .addItems(l)
//     // .setType(ScrollableList.LIST) // currently supported DROPDOWN and LIST
//     ;
//
//
//}
//
//void draw() {
//  background(240);
//}

void dropdown(int n) {
  /* request the selected item based on index n */
  println(n, cp5.get(ScrollableList.class, "dropdown").getItem(n));

  /* here an item is stored as a Map  with the following key-value pairs:
   * name, the given name of the item
   * text, the given text of the item by default the same as name
   * value, the given value of the item, can be changed by using .getItem(n).put("value", "abc"); a value here is of type Object therefore can be anything
   * color, the given color of the item, how to change, see below
   * view, a customizable view, is of type CDrawable
   */

  CColor c = new CColor();
  c.setBackground(color(255, 0, 0));
  cp5.get(ScrollableList.class, "dropdown").getItem(n).put("color", c);
}

//void keyPressed() {
//  switch(key) {
//    case('1'):
//    /* make the ScrollableList behave like a ListBox */
//    cp5.get(ScrollableList.class, "dropdown").setType(ControlP5.LIST);
//    break;
//    case('2'):
//    /* make the ScrollableList behave like a DropdownList */
//    cp5.get(ScrollableList.class, "dropdown").setType(ControlP5.DROPDOWN);
//    break;
//    case('3'):
//    /*change content of the ScrollableList */
//    List l = Arrays.asList("a-1", "b-1", "c-1", "d-1", "e-1", "f-1", "g-1", "h-1", "i-1", "j-1", "k-1");
//    cp5.get(ScrollableList.class, "dropdown").setItems(l);
//    break;
//    case('4'):
//    /* remove an item from the ScrollableList */
//    cp5.get(ScrollableList.class, "dropdown").removeItem("k-1");
//    break;
//    case('5'):
//    /* clear the ScrollableList */
//    cp5.get(ScrollableList.class, "dropdown").clear();
//    break;
//  }
// //}