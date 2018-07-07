package com.sdsu.edu.main;

import com.esri.mo2.ui.bean.Tool;

class Arrow extends Tool {
  public void arrowChores() { // undo measure tool residue
      ChartingMain.milesLabel.setText("DIST   0 mi   ");
      ChartingMain.kmLabel.setText("   0 km    ");
      if (ChartingMain.acetLayer != null)
        ChartingMain.map.remove(ChartingMain.acetLayer);
      ChartingMain.acetLayer = null;
      ChartingMain.map.repaint();
      ChartingMain.helpToolOn = false;
  }
}
