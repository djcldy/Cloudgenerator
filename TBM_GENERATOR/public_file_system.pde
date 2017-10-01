// void initFiles(){
//   // Using just the path of this sketch to demonstrate,
//   // but you can list any directory you like.
//   String path = sketchPath();
//   println("Listing all filenames in a directory: ");
//   String[] filenames = listFileNames(path + "/images/alpha/");
//   printArray(filenames);
// }

// This function returns all the files in a directory as an array of Strings
String[] listFileNames(String dir) {
  File file = new File(dir);
  if (file.isDirectory()) {
    String names[] = file.list();
    return names;
  } else {
    // If it's not a directory
    return null;
  }
}
