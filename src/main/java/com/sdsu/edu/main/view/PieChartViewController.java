package com.sdsu.edu.main.view;

import com.sdsu.edu.main.print.PrintUtility;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;

public class PieChartViewController extends ChartViewController {

  @Override
  public JPanel addAdditionalButtons() {
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    return tmpPanel1;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("printChart")) {
      new PrintUtility(this.chart).createChartPrintJob();
    } else if (e.getActionCommand().equals("changeTitle")) {
      chartTitleLabel.setText(chartTitleField.getText());
      chart.setTitle(chartTitleField.getText());
    }
  }
}
