package com.sdsu.edu.main.constant;

public enum ChartType {
  BAR("Bar"),
  PIE("Pie");

  private String chartName;

  ChartType(String name) {
    this.chartName = name;
  }

  public String getChartName() {
    return chartName;
  }
}
