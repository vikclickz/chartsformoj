package com.sdsu.edu.main.gui;

import com.sdsu.edu.main.constant.ChartType;
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

public class ThreeDimensionalPanelGUI extends JPanel {

  private String[] characterNameTypes;
  private List<String> attributeNames;
  final DefaultListModel<String> attributeList;
  final JList<String> attributeSelectList;
  public List<String> selectedFields;
  private JComboBox<String> charNamejcb;
  private JComboBox<String> chartTypeComboBox;
  private JButton selectbtn;
  String characterNameSType;
  String chartTypeName = ChartType.BAR.getChartName();
  private String[] chartType = {"Bar Chart", "Pie Chart"};

  public ThreeDimensionalPanelGUI(List<String> numericNameList, List<String> charNameList) {
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
    charNamejcb = new JComboBox<String>(characterNameTypes);
    charNamejcb.setAutoscrolls(getVerifyInputWhenFocusTarget());
    add(charNamejcb);

    // things to do upon selecting the type of chart that is needed
    charNamejcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox<String> characterNameType = (JComboBox<String>) e.getSource();
        characterNameSType = (String) characterNameType.getSelectedItem();
      }
    });
    // set the list for numeric attributes available to select
    JScrollPane scrollPane = new JScrollPane();
    attributeSelectList = new JList<String>(attributeList);
    attributeSelectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    attributeSelectList.setSelectedIndex(0);
    attributeSelectList.setVisibleRowCount(5);
    attributeSelectList.getAutoscrolls();
    attributeSelectList.setAutoscrolls(getVerifyInputWhenFocusTarget());
    scrollPane.setViewportView(attributeSelectList);
    add(scrollPane);

    chartTypeComboBox = new JComboBox<String>(chartType);
    chartTypeComboBox.setAutoscrolls(getVerifyInputWhenFocusTarget());
    add(chartTypeComboBox);

    // things to do upon selecting the type of chart that is needed
    chartTypeComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox<String> characterNameType = (JComboBox<String>) e.getSource();
        chartTypeName = (String) characterNameType.getSelectedItem();
      }
    });

    // add a button to show the list of attributes selected
    selectbtn = new JButton(GUILabelConstants.SUBMIT_BTN_LBL);
    add(selectbtn);
    selectbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        selectedFields = new ArrayList<String>();
        String data = "";
        if (attributeSelectList.getSelectedIndex() != -1) {
          data += "attribute selected: ";
          for (Object obj : attributeSelectList.getSelectedValues()) {
            data += obj + ", ";
            selectedFields.add((String) obj);
          }
          if (characterNameSType == null) {
            characterNameSType = (String) charNamejcb.getSelectedItem();
          }
          if (chartTypeName.contains(ChartType.BAR.getChartName())) {
            MapObjectChartController mapObjectChartController = new MapObjectChartController();
            mapObjectChartController.create3dBarChart(selectedFields, characterNameSType);
          } else {
            MapObjectChartController mapObjectChartController = new MapObjectChartController();
            mapObjectChartController.create3dPieChart(selectedFields, characterNameSType);
          }
        }
      }
    });
  } // constructor
}
