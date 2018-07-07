package com.sdsu.edu.main.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

public abstract class ChartViewController extends JFrame implements ActionListener {

  JLabel chartTitleLabel; // JLabel chartTitle;
  JTextField chartTitleField; // Field to write the title
  JTextField xAxisLabelField; // Field to write the xAxis label
  JTextField yAxisLabelField; // Field to write the yAxis label
  JButton titleChangeBtn; // Button to change the title of the chart
  JButton xAxisLabelChangeBtn; // Button to change the xAxis label
  JButton yAxisLabelChangeBtn; // Button to change the yAxis label
  JButton printBtn; // Button to print the chart
  JFreeChart chart;

  public void displayChart(ChartPanel contentPane, String title) {
    JPanel mainPanel = new JPanel();
    setBounds(200, 200, 700, 400);
    mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    mainPanel.setLayout(new BorderLayout(0, 0));
    this.chart = contentPane.getChart();

    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);

    xAxisLabelChangeBtn = new JButton("Change X Axis");
    xAxisLabelField = new JTextField(10);

    yAxisLabelChangeBtn = new JButton("Change Y Axis");
    yAxisLabelField = new JTextField(10);

    chartTitleLabel = new JLabel(title);

    JPanel tmpPanel1 = addAdditionalButtons();

    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    //topPanel.add(tmpPanel2);

    JPanel bottomPanel = new JPanel();
    bottomPanel.setLayout(new GridLayout(1, 1));
    bottomPanel.add(contentPane);
    bottomPanel.setAutoscrolls(true);
    // Add TopPane to the frame
    mainPanel.add(topPanel, BorderLayout.PAGE_START);
    mainPanel.add(bottomPanel, BorderLayout.CENTER);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);

    xAxisLabelChangeBtn.setActionCommand("changeXAxisLabel");
    xAxisLabelChangeBtn.addActionListener(this);

    yAxisLabelChangeBtn.setActionCommand("changeYAxisLabel");
    yAxisLabelChangeBtn.addActionListener(this);

    setContentPane(mainPanel);
    setTitle("THE CHARTING TOOL");
    setVisible(true);
    setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
  }

  public abstract JPanel addAdditionalButtons();
}

