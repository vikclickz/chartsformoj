package com.sdsu.edu.main;

import com.esri.mo2.data.feat.BaseQueryFilter;
import com.esri.mo2.data.feat.Feature;
import com.esri.mo2.data.feat.FeatureClass;
import com.esri.mo2.data.feat.Fields;
import com.esri.mo2.map.dpy.FeatureLayer;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
  // the required methods to implement are getRowCount,
  // getColumnCount, getValueAt
  com.esri.mo2.map.dpy.Layer layer = ChartingTool.layer4;

  MyTableModel() {
      qfilter.setSubFields(fields);
      com.esri.mo2.data.feat.Cursor cursor = flayer.search(qfilter);
      while (cursor.hasMore()) {
          ArrayList<String> inner = new ArrayList<String>();
          Feature f = (com.esri.mo2.data.feat.Feature) cursor.next();
          inner.add(0, String.valueOf(row));
          for (int j = 1; j < fields.getNumFields(); j++) {
              // System.out.println("Attr Table::" + j);
              // System.out.println("::" + f.getValue(j).toString());
              inner.add(f.getValue(j).toString());
          }
          data.add(inner);
          row++;
      }
  }

  FeatureLayer flayer = (FeatureLayer) layer;
  FeatureClass fclass = flayer.getFeatureClass();
  String columnNames[] = fclass.getFields().getNames();
  ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
  int row = 0;
  int col = 0;
  BaseQueryFilter qfilter = new BaseQueryFilter();
  Fields fields = fclass.getFields();

  public int getColumnCount() {
      return fclass.getFields().getNumFields();
  }

  public int getRowCount() {
      return data.size();
  }

  public String getColumnName(int colIndx) {
      return columnNames[colIndx];
  }

  public Object getValueAt(int row, int col) {
      ArrayList temp = new ArrayList();
      temp = data.get(row);
      return temp.get(col);
  }
}
