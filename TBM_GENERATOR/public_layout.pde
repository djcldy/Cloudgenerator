// initialize all of the dimensions

float os;
float xA,xB,xC,xD,xE,xF,xG;
float yA,yB,yC,yD,yE,yF,yG;
float cA,cB,cC,cD;
float rA,rB,rC,rD,rE;
int tWidth;
int y10;
float row5, row6, row7, row8;

void setConst(){
os = width/64;

 xA = os;
 xB = width/4 - os;
 xC = width/4;
 xD = width/2;
 xE = width/2 + os;

 xG = width*5/6;




 y10 = int(height/16+width/64+ width/4 - width/32); // revisit this value .

 yA = height/16;
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
 row5 = y10+tWidth+os;
 row8 = y10 + 2*(tWidth + os);
}
