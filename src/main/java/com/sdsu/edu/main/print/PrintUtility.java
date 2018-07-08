package com.sdsu.edu.main.print;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public class PrintUtility extends ChartPanel {

  private static final long serialVersionUID = 1L;
  private String[] additionalStr = {};

  public PrintUtility(JFreeChart chart) {
    super(chart);
  }

  public void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);
    drawAdditionalStuff(g, getScreenDataArea());
  }

  public void setAdditional(String[] stuff) {
    additionalStr = stuff;
    updateUI();
  }

  /**
   * Prints the chart on a single page.
   *
   * @param g the graphics context.
   * @param pf the page format to use.
   * @param pageIndex the index of the page. If not <code>0</code>, nothing gets print.
   * @return The result of printing.
   */
  public int print(Graphics g, PageFormat pf, int pageIndex) {
    if (pageIndex != 0) {
      return NO_SUCH_PAGE;
    }
    Graphics2D g2 = (Graphics2D) g;
    double x = pf.getImageableX();
    double y = pf.getImageableY();
    double w = pf.getImageableWidth();
    double h = pf.getImageableHeight();
    Dimension beforeSize = this.getSize();
    this.setSize((int) w, (int) h);
    g2.translate(x, y);
    paintComponent(g2);
    this.setSize(beforeSize);
    return PAGE_EXISTS;
  }

  public void createChartPrintJob() {
    PrinterJob job = PrinterJob.getPrinterJob();
    PageFormat pf = new PageFormat();
    pf.setOrientation(PageFormat.LANDSCAPE);
    job.setPrintable(this, pf);
    if (job.printDialog()) {
      try {
        job.print();
      } catch (PrinterException e) {
        JOptionPane.showMessageDialog(this, e);
      }
    }
  }

  private void drawAdditionalStuff(Graphics g, Rectangle2D plotArea) {
    if (additionalStr.length > 0) {
      FontMetrics met = g.getFontMetrics();
      int border = 10, margin = 5;
      int x = (int) plotArea.getX(), y = (int) plotArea.getY();
      int w = (int) plotArea.getWidth(), h = (int) plotArea.getHeight();
      x += w * 3 / 4 - border;
      y += border;
      w = w / 4;
      h = 2 * margin + (additionalStr.length - 1) * (met.getHeight() + 3) + met.getAscent() + 3;
      g.setColor(new Color(0.3f, 0.5f, 0.7f, 0.3f));
      g.fillRect(x, y, w, h);
      g.setColor(new Color(0f, 0f, 0f, 0.6f));
      g.drawRect(x, y, w, h);
      g.setColor(new Color(0f, 0f, 0f, 0.8f));
      int asc = met.getAscent();
      int height = met.getHeight();
      int xS = x + margin, yS = y + margin + asc, tJ = 2 * margin, hJ = height + 3;
      for (int i = 0; i < additionalStr.length; i++) {
        g.drawString(additionalStr[i], xS + tJ, yS + i * hJ);
      }
    }
  }
}
