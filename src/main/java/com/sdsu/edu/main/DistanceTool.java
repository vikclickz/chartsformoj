package com.sdsu.edu.main;

import com.esri.mo2.ui.bean.AcetateLayer;
import com.esri.mo2.ui.bean.DragTool;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

class DistanceTool extends DragTool {
  int startx, starty, endx, endy, currx, curry;
  com.esri.mo2.cs.geom.Point initPoint, endPoint, currPoint;
  double distance;

  public static void resetDist() { // undo measure tool residue
      ChartingMain.milesLabel.setText("DIST   0 mi   ");
      ChartingMain.kmLabel.setText("   0 km    ");
      if (ChartingMain.acetLayer != null) {
        ChartingMain.map.remove(ChartingMain.acetLayer);
        ChartingMain.acetLayer = null;
      }
      ChartingMain.map.repaint();
  }

  public void mousePressed(MouseEvent me) {
      startx = me.getX();
      starty = me.getY();
      initPoint = ChartingMain.map.transformPixelToWorld(me.getX(),me.getY());
  }

  public void mouseReleased(MouseEvent me) {
      // now we create an acetatelayer instance and draw a line on it
      endx = me.getX();
      endy = me.getY();
      endPoint = ChartingMain.map.transformPixelToWorld(me.getX(),    me.getY());
      distance = (69.44 / (2 * Math.PI))
              * 360
              * Math.acos(Math.sin(initPoint.y * 2 * Math.PI / 360)
                      * Math.sin(endPoint.y * 2 * Math.PI / 360)
                      + Math.cos(initPoint.y * 2 * Math.PI / 360)
                      * Math.cos(endPoint.y * 2 * Math.PI / 360)
                      * (Math.abs(initPoint.x - endPoint.x) < 180 ? Math
                              .cos((initPoint.x - endPoint.x) * 2 * Math.PI
                                      / 360) : Math.cos((360 - Math
                                              .abs(initPoint.x - endPoint.x))
                                              * 2
                                              * Math.PI
                                              / 360)));
      System.out.println(distance);
      ChartingMain.milesLabel.setText("DIST: "
              + new Float((float) distance).toString() + " mi  ");
      ChartingMain.kmLabel.setText(new Float((float) (distance * 1.6093))
      .toString() + " km");
      if (ChartingMain.acetLayer != null)
        ChartingMain.map.remove(ChartingMain.acetLayer);
      ChartingMain.acetLayer = new AcetateLayer() {
          public void paintComponent(Graphics g) {
              java.awt.Graphics2D g2d = (java.awt.Graphics2D) g;
              Line2D.Double line = new Line2D.Double(startx, starty, endx, endy);
              g2d.setColor(new Color(0, 0, 250));
              g2d.draw(line);
          }
      };
      Graphics g = super.getGraphics();
      ChartingMain.map.add(ChartingMain.acetLayer);
      ChartingMain.map.redraw();
  }

  public void cancel() {
  };
}
