class TweenPoint{

float dpmg; // damping factor
PVector start,end;

 TweenPoint(PVector _start, PVector _end, float _dpmg){

    start = _start;
    end = _end;

 }

 void addIncrement(PVector p){

  end.x += p.x;
  end.y += p.y;
  end.z += p.z;

 }

 void set(PVector _end){
  end = _end;

 }
 PVector get(){

    // start.x += 0.5;
    // start.y += 0.5;
    // start.z += 0.5;
  float valX = (end.x-start.x)*0.1;
  float valY = (end.y-start.y)*0.1;

  if (start != end) {
    // println("damping = " + val);
  }


  start.x = start.x + valX;
  start.y = start.y + valY;
  start.z = start.z +(end.z -start.z)*dpmg;

  return start;
 }

}
