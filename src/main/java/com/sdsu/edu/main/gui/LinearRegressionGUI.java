package com.sdsu.edu.main.gui;

import static com.sdsu.edu.main.constant.GUILabelConstants.DEPENDENT_VARIABLE;
import static com.sdsu.edu.main.constant.GUILabelConstants.INDEPENDENT_VARIABLE;

import com.sdsu.edu.main.constant.GUILabelConstants;
import com.sdsu.edu.main.controller.MapObjectChartController;
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

public class LinearRegressionGUI extends JPanel {
  private String[] characterNameTypes;
  private List<String> attributeNames;
  final DefaultListModel<String> attributeList;
  final JList<String> attributeSelectListXaxis;
  final JList<String> attributeSelectListYaxis;
  public List<String> xAxisSelectedList;
  public List<String> yAxisSelectedList;
  private JButton selectbtn;

  public LinearRegressionGUI(List<String> numericNameList, List<String> charNameList) {
    characterNameTypes = charNameList.toArray(new String[charNameList.size()]);
    attributeNames = numericNameList;
    setSize(500, 500);
    attributeList = new DefaultListModel<String>();
    for (int i = 0; i < attributeNames.size(); i++) {
      attributeList.addElement(attributeNames.get(i));
    }
    // set layout
    setLayout(new GridLayout(1, 5));
    // set combobox for char Name type
/*    charNamejcb = new JComboBox<String>(characterNameTypes);
    charNamejcb.setAutoscrolls(getVerifyInputWhenFocusTarget());
    add(charNamejcb);
    // things to do upon selecting the type of chart that is needed
    charNamejcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox<String> characterNameType = (JComboBox<String>) e.getSource();
        characterNameSType = (String) characterNameType.getSelectedItem();
      }
    });*/

    // set the list for numeric attributes available to select
    JScrollPane scrollPane = new JScrollPane();
    attributeSelectListYaxis = new JList<String>(attributeList);
    attributeSelectListYaxis.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    attributeSelectListYaxis.setSelectedIndex(0);
    attributeSelectListYaxis.setVisibleRowCount(5);
    attributeSelectListYaxis.getAutoscrolls();
    attributeSelectListYaxis.setAutoscrolls(getVerifyInputWhenFocusTarget());
    attributeSelectListYaxis.setToolTipText(DEPENDENT_VARIABLE);
    attributeSelectListYaxis.setFocusable(true);
    scrollPane.setViewportView(attributeSelectListYaxis);
    add(scrollPane);

    JScrollPane scrollPane2 = new JScrollPane();
    attributeSelectListXaxis = new JList<String>(attributeList);
    attributeSelectListXaxis.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    attributeSelectListXaxis.setSelectedIndex(0);
    attributeSelectListXaxis.setVisibleRowCount(5);
    attributeSelectListXaxis.getAutoscrolls();
    attributeSelectListXaxis.setAutoscrolls(getVerifyInputWhenFocusTarget());
    attributeSelectListXaxis.setToolTipText(INDEPENDENT_VARIABLE);
    attributeSelectListXaxis.setFocusable(true);
    scrollPane2.setViewportView(attributeSelectListXaxis);
    add(scrollPane2);

    // add a button to show the list of attributes selected
    selectbtn = new JButton(GUILabelConstants.SUBMIT_BTN_LBL);
    add(selectbtn);
    selectbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        xAxisSelectedList = new ArrayList<String>();
        yAxisSelectedList = new ArrayList<String>();
        if (attributeSelectListXaxis.getSelectedIndex() != -1 && attributeSelectListYaxis
            .getSelectedIndex() != -1) {
          for (Object obj : attributeSelectListXaxis.getSelectedValues()) {
            xAxisSelectedList.add((String) obj);
          }

          for (Object obj : attributeSelectListYaxis.getSelectedValues()) {
            yAxisSelectedList.add((String) obj);
          }

          if(ChartUIUtil.parameterChecks(xAxisSelectedList, yAxisSelectedList)) {
            return;
          }

          /*if(characterNameSType == null) {
            characterNameSType = (String) charNamejcb.getSelectedItem();
          }*/
          MapObjectChartController mapObjectChartController = MapObjectChartController.getInstance();
          mapObjectChartController.createLinearRegressionChart(xAxisSelectedList, yAxisSelectedList);
        }
      }
    });
  } // constructor
}
