package com.sdsu.edu.main.model;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.data.xy.XYDataset;

public class ChartModel {

  XYDataset xyDataset;

  public XYDataset getXyDataset() {
    return xyDataset;
  }

  public void setXyDataset(XYDataset xyDataset) {
    this.xyDataset = xyDataset;
  }

  public ValueAxis getDomainAxis() {
    return domainAxis;
  }

  public void setDomainAxis(ValueAxis domainAxis) {
    this.domainAxis = domainAxis;
  }

  public ValueAxis getRangeAxis() {
    return rangeAxis;
  }

  public void setRangeAxis(ValueAxis rangeAxis) {
    this.rangeAxis = rangeAxis;
  }

  ValueAxis domainAxis;
  ValueAxis rangeAxis;
}
