package com.sdsu.edu.main.view;

import com.sdsu.edu.main.print.PrintUtility;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class GenericChartViewController extends ChartViewController {

  @Override
  public JPanel addAdditionalButtons() {
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);

    tmpPanel1.add(xAxisLabelChangeBtn);
    tmpPanel1.add(xAxisLabelField);

    tmpPanel1.add(yAxisLabelChangeBtn);
    tmpPanel1.add(yAxisLabelField);
    return tmpPanel1;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("printChart")) {
      new PrintUtility(this.chart).createChartPrintJob();
    } else if (e.getActionCommand().equals("changeTitle")) {
      chartTitleLabel.setText(chartTitleField.getText());
      chart.setTitle(chartTitleField.getText());
    } else if (e.getActionCommand().equals("changeXAxisLabel")) {
      chart.getXYPlot().getDomainAxis().setLabel(xAxisLabelField.getText());
    } else if (e.getActionCommand().equals("changeYAxisLabel")) {
      chart.getXYPlot().getRangeAxis().setLabel(yAxisLabelField.getText());
    }
  }
}
