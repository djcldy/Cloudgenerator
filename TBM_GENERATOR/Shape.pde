class Shape { 

  Shape(String _path) {

    println("load shape"); 

    String path = "/images/shape/grid.csv";
    Table table = loadTable(path); 

    for (TableRow row : table.rows ()) {
      
      String str1 = row.getString(0); 
      str1 = str1.replaceFirst("\\{", ""); 
      println(str1);
      
      float b = float(row.getString(1));

      println(row.getString(0)+ ","+ row.getString(1)+","+row.getString(2));
    }
  }
}