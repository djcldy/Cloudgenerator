// initialize all of the dimensions

float os;
float xA,xB,xC,xD,xE,xF,xG;
float yA,yB,yC,yD,yE,yF,yG;
float cA,cB,cC,cD;
float rA,rB,rC,rD,rE;
int tWidth;
int y10;
float row0,row1,row2,row3,row4,row5, row6, row7, row8, row9,row10;
float col1,col2,col3,col4,col5,col6,col7,col8,col9,col10;
float cellWidth;
void setConst(){

os = width/64;

  cellWidth = (width/2 - os-os/2)/6;

 xA = os;
 xB = width/4 - os;
 xC = width/4;
 xD = width/2;
 xE = width/2 + os;

 xG = width*5/6;




 y10 = int(height/16+width/64+ width/4 - width/32); // revisit this value .

//
 yA = height/32; // rowA



 yB = yA + os;
 yC = yB + xB-os;
 yD = yC + os;
 yE = yD + (xB-os)/3;
 yF = height;
 yG = height/5;
 cA = xC/2;
 cB = xC + (xD-xC)/2;
 rC = yC + os*2/3;
 rD = yE + os*2/3;
 rA = yA/2;

 tWidth = int((width/4 - width/32)/3); // width of a thumb
 row0 = os;
 row1 = os + width/4 - width/32;
 row2 = height/32+ width/4 - width/64 + os ;
 row3 = row2 + os;
 row4 = row3 + tWidth;
 row5 = row4 + os;
 row6 = row5 + tWidth/2;
 row7 = row6 + os;
 row8 = row7 + cellWidth;
 row9 = row8;
 row10 = height - 2*os;

  col2 = width/4 -os;
  col3 = col2 +os;
  col4 = (width - os)/2;
  col5 = col4+os;
  col6 = col4;
  col7 = col8 - width/6 -os;
  col8 = width*5/6 + os/2;
  col10 = width-os;

}
