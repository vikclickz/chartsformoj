package com.sdsu.edu.main.gui;

import static com.sdsu.edu.main.constant.GUILabelConstants.DEPENDENT_VARIABLE;
import static com.sdsu.edu.main.constant.GUILabelConstants.INDEPENDENT_VARIABLE;

import com.sdsu.edu.main.controller.MapObjectChartController;
import com.sdsu.edu.main.controller.db.DbfReadController;
import com.sdsu.edu.main.constant.GUILabelConstants;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class PolynomialPanelGUI extends JPanel {
  private String[] characterNameTypes;
  private List<String> attributeNames;
  final DefaultListModel<String> attributeList;
  final JList<String> attributeXaxisSelectList;
  final JList<String> attributeYaxisSelectList;

  public List<String> xAxisSelectedList;
  public List<String> yAxisSelectedList;

  private JComboBox<Integer> chartOrderJcb;
  private JButton selectbtn;
  Integer nonLinearRegressionOrder = 2;
  private Integer[] regressionOrder = {2,3};

  public PolynomialPanelGUI(List<String> numericNameList, List<String> charNameList) {
    characterNameTypes = charNameList.toArray(new String[charNameList.size()]);
    attributeNames = numericNameList;
    setSize(500, 500);
    attributeList = new DefaultListModel<String>();
    for (int i = 0; i < attributeNames.size(); i++) {
      attributeList.addElement(attributeNames.get(i));
    }
    // set layout
    setLayout(new GridLayout(1, 5));

    // set the list for numeric attributes available to select
    JScrollPane scrollPane = new JScrollPane();
    attributeYaxisSelectList = new JList<String>(attributeList);
    attributeYaxisSelectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    attributeYaxisSelectList.setSelectedIndex(0);
    attributeYaxisSelectList.setVisibleRowCount(5);
    attributeYaxisSelectList.getAutoscrolls();
    attributeYaxisSelectList.setAutoscrolls(getVerifyInputWhenFocusTarget());
    attributeYaxisSelectList.setToolTipText(DEPENDENT_VARIABLE);
    attributeYaxisSelectList.setFocusable(true);
    scrollPane.setViewportView(attributeYaxisSelectList);
    add(scrollPane);

    JScrollPane scrollPane2 = new JScrollPane();
    attributeXaxisSelectList = new JList<String>(attributeList);
    attributeXaxisSelectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    attributeXaxisSelectList.setSelectedIndex(0);
    attributeXaxisSelectList.setVisibleRowCount(5);
    attributeXaxisSelectList.getAutoscrolls();
    attributeXaxisSelectList.setAutoscrolls(getVerifyInputWhenFocusTarget());
    attributeXaxisSelectList.setToolTipText(INDEPENDENT_VARIABLE);
    attributeXaxisSelectList.setFocusable(true);
    scrollPane2.setViewportView(attributeXaxisSelectList);
    add(scrollPane2);

    chartOrderJcb = new JComboBox<Integer>(regressionOrder);
    chartOrderJcb.setAutoscrolls(getVerifyInputWhenFocusTarget());
    chartOrderJcb.setToolTipText("Polynomial Function Order");
    add(chartOrderJcb);

    // things to do upon selecting the type of chart that is needed
    chartOrderJcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox<String> characterNameType = (JComboBox<String>) e.getSource();
        nonLinearRegressionOrder = (Integer) characterNameType.getSelectedItem();
      }
    });

    // add a button to show the list of attributes selected
    selectbtn = new JButton(GUILabelConstants.SUBMIT_BTN_LBL);
    add(selectbtn);
    selectbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        xAxisSelectedList = new ArrayList<String>();
        yAxisSelectedList = new ArrayList<String>();
        String data = "";
        if (attributeYaxisSelectList.getSelectedIndex() != -1 &&
            attributeXaxisSelectList.getSelectedIndex() != -1) {
          data += "attribute selected: ";
          for (Object obj : attributeXaxisSelectList.getSelectedValues()) {
            data += obj + ", ";
            xAxisSelectedList.add((String) obj);
          }

          for (Object obj : attributeYaxisSelectList.getSelectedValues()) {
            data += obj + ", ";
            yAxisSelectedList.add((String) obj);
          }

          if(ChartUIUtil.parameterChecks(xAxisSelectedList, yAxisSelectedList)) {
            return;
          }

          MapObjectChartController mapObjectChartController = MapObjectChartController.getInstance();
          mapObjectChartController.createPolynomialRegressionChart(xAxisSelectedList,
              yAxisSelectedList, nonLinearRegressionOrder);
        }
      }
    });
  } // constructor
}
