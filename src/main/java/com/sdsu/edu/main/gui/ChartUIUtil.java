package com.sdsu.edu.main.gui;

import java.util.List;
import javax.swing.JOptionPane;

public class ChartUIUtil {

  public static boolean parameterChecks(List<String> xAxisSelectList, List<String> yAxisSelectedList) {
    if(null != xAxisSelectList && xAxisSelectList.size() > 1) {
      JOptionPane.showMessageDialog(null, "Please select only one independent variable");
      return true;
    } else if(null != yAxisSelectedList && yAxisSelectedList.size() > 1) {
      JOptionPane.showMessageDialog(null, "Please select only one dependent variable");
      return true;
    } else if(xAxisSelectList.size() == 0 || yAxisSelectedList.size() == 0) {
      JOptionPane.showMessageDialog(null, "Missing parameter");
      return true;
    } else if(xAxisSelectList.size() == 1 && yAxisSelectedList.size() == 1) {
      if(xAxisSelectList.get(0).equalsIgnoreCase(yAxisSelectedList.get(0))) {
        JOptionPane.showMessageDialog(null, "Please select different dependent and independent variables");
        return true;
      }
    }
    return false;
  }

  public static boolean parameterCheck3d(List<String> selectedField) {
    if(null != selectedField && selectedField.size() > 1) {
      JOptionPane.showMessageDialog(null, "Please select only one parameter");
      return true;
    }
    return false;
  }

}
