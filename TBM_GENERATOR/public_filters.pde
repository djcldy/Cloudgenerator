// contains image filtering functions

 
 public float[][] sharpen = {
      {
        0, -1, 0
      }
      , {
        -1, 5, -1
      }
      , {
        0, -1, 0
      }
    }; 
 public color convoBlur(int x, int y, float[] Matrix, PGraphics iGraph){ 

  float sumR = 0.0;
  float sumG = 0.0;
  float sumB = 0.0;
  int MatrixSize = int(sqrt(Matrix.length)); //is our matrix 3x3, 5x5, etc.?
  int countMatrix = 0; //to keep track of where we are in convolution matrix

  for (int i =-MatrixSize/2; i<=MatrixSize/2; i++){
    for (int j=-MatrixSize/2; j<=MatrixSize/2; j++){
      //what pixel are we testing
      int xloc = x+i;
      int yloc = y+j;
      int loc = xloc + iGraph.width*(yloc+j);     
      loc = constrain(loc,0, iGraph.pixels.length-1); //make sure we haven't walked off our image
      sumB += (blue(iGraph.pixels[loc]) * Matrix[countMatrix]);
      sumG += (green(iGraph.pixels[loc]) * Matrix[countMatrix]);
      sumR += (red(iGraph.pixels[loc]) * Matrix[countMatrix]);     //calculate the convolution
      countMatrix++;
    }
  }

  //make sure RGB is withing range
  sumR = constrain(sumR,0,255);
  sumG = constrain(sumG,0,255);
  sumB = constrain(sumB,0,255);

  //return the resulting color
  return color(sumR,sumG,sumB);
}
   
public color convolutionBlur(int x, int y, float[] Matrix)
{

  float sumR = 0.0;
  float sumG = 0.0;
  float sumB = 0.0;

  //is our matrix 3x3, 5x5, etc.?
  int MatrixSize = int(sqrt(Matrix.length));
  //to keep track of where we are in convolution matrix
  int countMatrix = 0;

  for (int i =-MatrixSize/2; i<=MatrixSize/2; i++){
    for (int j=-MatrixSize/2; j<=MatrixSize/2; j++){
      //what pixel are we testing
      int xloc = x+i;
      int yloc = y+j;
      int loc = xloc + width*(yloc+j);
      //make sure we haven't walked off our image
      loc = constrain(loc,0,pixels.length-1);
      //calculate the convolution
      sumB += (blue(pixels[loc]) * Matrix[countMatrix]);
      sumG += (green(pixels[loc]) * Matrix[countMatrix]);
      sumR += (red(pixels[loc]) * Matrix[countMatrix]);
      countMatrix++;
    }
  }

  //make sure RGB is withing range
  sumR = constrain(sumR,0,255);
  sumG = constrain(sumG,0,255);
  sumB = constrain(sumB,0,255);

  //return the resulting color
  return color(sumR,sumG,sumB);
}

public color convolution(int x, int y, float[][] matrix, int matrixsize, PImage img)
{
  float rtotal = 0.0;
  float gtotal = 0.0;
  float btotal = 0.0;
  int offset = matrixsize / 2;
  for (int i = 0; i < matrixsize; i++) {
    for (int j= 0; j < matrixsize; j++) {
      // What pixel are we testing
      int xloc = x+i-offset;
      int yloc = y+j-offset;
      int loc = xloc + img.width*yloc;
      // Make sure we haven't walked off our image, we could do better here
      loc = constrain(loc, 0, img.pixels.length-1);
      // Calculate the convolution
      rtotal += (red(img.pixels[loc]) * matrix[i][j]);
      gtotal += (green(img.pixels[loc]) * matrix[i][j]);
      btotal += (blue(img.pixels[loc]) * matrix[i][j]);
    }
  }
  // Make sure RGB is within range
  rtotal = constrain(rtotal, 0, 255);
  gtotal = constrain(gtotal, 0, 255);
  btotal = constrain(btotal, 0, 255);
  // Return the resulting color
  return color(rtotal, gtotal, btotal);
}
 public float cBlur(int x, int y, float[] Matrix, PGraphics iGraph){ 

  float sum = 0;  
  
  int MatrixSize = int(sqrt(Matrix.length)); //is our matrix 3x3, 5x5, etc.?
  int countMatrix = 0; //to keep track of where we are in convolution matrix

  for (int i =-MatrixSize/2; i<=MatrixSize/2; i++){
    for (int j=-MatrixSize/2; j<=MatrixSize/2; j++){
      //what pixel are we testing
      int xloc = x+i;
      int yloc = y+j;
      int loc = xloc + iGraph.width*(yloc+j);     
      loc = constrain(loc,0, iGraph.pixels.length-1); //make sure we haven't walked off our image
      sum += (brightness(iGraph.pixels[loc]) * Matrix[countMatrix]);
      countMatrix++;
    }
  }
  
  //return the resulting color
  return sum;
}
