package com.sdsu.edu.main;

import com.sdsu.edu.main.controller.ChartController.DataSet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

public class VerticalBarLegendPanel extends JPanel {
  private DataSet[] dataset;
  public VerticalBarLegendPanel(DataSet[] data) {
    this.dataset = data;
    setAutoscrolls(true);
  }
  @Override
  protected void paintComponent(Graphics g) {
    int width = 10;
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
    RenderingHints.VALUE_ANTIALIAS_ON);
    int i = 0;
    for (DataSet eachData : dataset) {
      i++;
      g2d.setColor(eachData.getVcolor());
      g2d.setColor(Color.RED);
      g2d.drawString(i + " " + eachData.getName(), 10, width);
        width += 10;
    }
  } // end paintComponent
}
