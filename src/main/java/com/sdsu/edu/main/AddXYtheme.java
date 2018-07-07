package com.sdsu.edu.main;

import com.esri.mo2.cs.geom.BasePointsArray;
import com.esri.mo2.ui.bean.Map;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class AddXYtheme extends JDialog {
  Map map;
  Vector<String> s2 = new Vector<String>();
  JFileChooser jfc = new JFileChooser();
  BasePointsArray bpa = new BasePointsArray();

  AddXYtheme() throws IOException {
      setBounds(50, 50, 520, 430);
      jfc.showOpenDialog(this);
      if (jfc.getSelectedFile() != null) {
          try {

              File file = jfc.getSelectedFile();
              FileReader fred = new FileReader(file);
              BufferedReader in = new BufferedReader(fred);
              String s; // = in.readLine();
              double x, y;
              int n = 0;
              while ((s = in.readLine()) != null) {
                  StringTokenizer st = new StringTokenizer(s, ",");
                  // System.out.println("St size::" + st.countTokens());
                  x = Double.parseDouble(st.nextToken());
                  y = Double.parseDouble(st.nextToken());
                  bpa.insertPoint(n++, new com.esri.mo2.cs.geom.Point(x, y));
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  s2.addElement(st.nextToken());
                  // System.out.println("S2 size::"+s2.size());
              }

          } catch (IOException e) {
          }
          XYfeatureLayer xyfl = new XYfeatureLayer(bpa, map, s2);
          xyfl.setVisible(true);
          map = ChartingTool.map;
          map.getLayerset().addLayer(xyfl);
          map.redraw();
      }
  }

  public void setMap(com.esri.mo2.ui.bean.Map map1) {
      map = map1;
  }
}
