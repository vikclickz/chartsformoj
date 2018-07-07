package com.sdsu.edu.main;

import static org.junit.Assert.*;

import org.junit.Test;

public class ChartingToolStartTest {

  @Test
  public void testCharting() {
    try {
      ChartingToolStart.ChartTypeFrameGUI chartTool = new ChartingToolStart().new ChartTypeFrameGUI();
      Thread.sleep(300000);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

}