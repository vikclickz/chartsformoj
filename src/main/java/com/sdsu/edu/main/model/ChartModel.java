package com.sdsu.edu.main.model;

import java.util.List;
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

  public List<Double> getIndependentList() {
    return independentList;
  }

  public void setIndependentList(List<Double> independentList) {
    this.independentList = independentList;
  }

  public List<Double> getDependentList() {
    return dependentList;
  }

  public void setDependentList(List<Double> dependentList) {
    this.dependentList = dependentList;
  }

  List<Double> independentList;
  List<Double> dependentList;
}
