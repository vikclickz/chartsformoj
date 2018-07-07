package com.sdsu.edu.main.gui;

import com.sdsu.edu.main.controller.db.DbfReadController;
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

public class BarPanelGUI extends JPanel {

  private String[] barchartTypes = {"Horizontal", "Vertical"};
  private String[] chartColorTypes = {"Normal", "Pastel", "Rainbow"};
  private String[] characterNameTypes;
  private List<String> attributeNames;
  final DefaultListModel<String> attributeList;
  final JList<String> attributeSelectList;
  public List<String> selectedFields;
  private JComboBox<String> chartjcb;
  private JComboBox<String> chartcolorjcb;
  private JComboBox<String> charNamejcb;
  private JButton selectbtn;
  String barchartSType;
  String chartColorSType;
  String characterNameSType;

  public BarPanelGUI(List<String> numericNameList, List<String> charNameList) {
    characterNameTypes = charNameList.toArray(new String[charNameList.size()]);
    attributeNames = numericNameList;
    setSize(500, 500);
    attributeList = new DefaultListModel<String>();
    for (int i = 0; i < attributeNames.size(); i++) {
      attributeList.addElement(attributeNames.get(i));
    }
    // set layout
    setLayout(new GridLayout(1, 4));
    // set combobox for chart type
    chartjcb = new JComboBox<String>(barchartTypes);
    chartjcb.setAutoscrolls(getVerifyInputWhenFocusTarget());
    add(chartjcb);
    // set combobox for chartcolor type
    chartcolorjcb = new JComboBox<String>(chartColorTypes);
    chartcolorjcb.setAutoscrolls(getVerifyInputWhenFocusTarget());
    add(chartcolorjcb);
    // set combobox for char Name type
    charNamejcb = new JComboBox<String>(characterNameTypes);
    charNamejcb.setAutoscrolls(getVerifyInputWhenFocusTarget());
    add(charNamejcb);
    // things to do upon selecting the type of chart that is needed
    chartjcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox<String> chartType = (JComboBox<String>) e.getSource();
        barchartSType = (String) chartType.getSelectedItem();
      }
    });
    // things to do upon selecting the type of chartcolor that is needed
    chartcolorjcb.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JComboBox<String> chartColorType = (JComboBox) e.getSource();
        chartColorSType = (String) chartColorType.getSelectedItem();
      }
    });
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
    // add a button to show the list of attributes selected
    selectbtn = new JButton("Select Done");
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
          DbfReadController dbfread = DbfReadController.getInstance();
          dbfread.dataHandler(selectedFields, barchartSType, characterNameSType,
              chartColorSType);
        }
      }
    });
  } // constructor
}
