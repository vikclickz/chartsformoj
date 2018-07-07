package com.sdsu.edu.main.gui;

import com.sdsu.edu.main.VerticalBarLegendPanel;
import com.sdsu.edu.main.controller.ChartController;
import com.sdsu.edu.main.controller.ChartController.Axis;
import com.sdsu.edu.main.controller.ChartController.Bar;
import com.sdsu.edu.main.controller.ChartController.BarChartVPanel;
import com.sdsu.edu.main.controller.ChartController.DataSet;
import com.sdsu.edu.main.controller.ChartController.MultiBar;
import com.sdsu.edu.main.controller.ChartController.MultiBarChartHPanel;
import com.sdsu.edu.main.controller.ChartController.MultiBarChartVPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/*
 * Finally present in a GUi where user can sort and re render, print,
 * change the title, x-axis and y-axis label
*/
public class ChartViewFrameGUI extends JFrame implements ActionListener {
  private JPanel contentPane;
  boolean sortflag = false;
  public ChartController.DataSet[] data1;
  public ChartController.DataSet[] sortedData1;
  private JLabel chartTitleLabel; // JLabel chartTitle;
  private JLabel xAxisLabel; // JLabel xAxis label;
  private JLabel yAxisLabel; // JLabel yAxis label;
  private JTextField chartTitleField; // Field to write the title
  private JTextField xAxisLabelField; // Field to write the xAxis label
  private JTextField yAxisLabelField; // Field to write the yAxis label
  private JButton sortBtn; // Button to sort the data
  private JButton titleChangeBtn; // Button to change the title of the chart
  private JButton xAxisLabelChangeBtn; // Button to change the xAxis label
  private JButton yAxisLabelChangeBtn; // Button to change the yAxis label
  private JButton printBtn; // Button to print the chart
  private JSplitPane splitPane;
  PrinterJob printJob;
  BufferedImage image = null;

  /**
   * Create the frame. PIE CHART
   */
  public ChartViewFrameGUI(ChartController.DataSet[] data1, final ChartController.DataSet[] sortedData1, ChartController.PieChartPanel chart1,
      ChartController.PieLegendPanel legend1, final ChartController.PieChartPanel sortedChart1, final ChartController.PieLegendPanel sortedLegend1) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * center include chart and legend for display
     */
    // put chart and legend to JScrollPane so text can be scrolled when too long
    JScrollPane scrollPanelLeft = new JScrollPane(legend1);
    JScrollPane scrollPanelRight = new JScrollPane(chart1);
    // put two JScrollPane into SplitPane
    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
    splitPane.setOneTouchExpandable(true);
    contentPane.add(splitPane, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     *
     */
    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // create new Label with "Pie Chart" as default chart title.
    chartTitleLabel = new JLabel("Pie Chart");
    // put field and button in one panel and label into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frame
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the button
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the button
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    /**
     * Bottom panel Include sortbutton
     */
    // create sort button
    sortBtn = new JButton("Sort by increasing order");
    // add action for this button
    sortBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chartViewFrameSorted(sortedData1, sortedChart1, sortedLegend1);
      }
    });
    // add all to bottomPanel
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.add(sortBtn);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
    splitPane.setDividerLocation(getWidth() / 2);
  } // end chartViewFrameGui for Pie Chart

  public void chartViewFrameSorted(ChartController.DataSet[] sortedData1, final ChartController.PieChartPanel sortedChart1,
      final ChartController.PieLegendPanel sortedLegend1) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * center include chart and legend for display
     */
    // put chart and legend to JScrollPane so text can be scrolled when too long
    JScrollPane scrollPanelLeft = new JScrollPane(sortedLegend1);
    JScrollPane scrollPanelRight = new JScrollPane(sortedChart1);
    // put two JScrollPane into SplitPane
    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
    splitPane.setOneTouchExpandable(true);
    contentPane.add(splitPane, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     * */
    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // create new Label with "Pie Chart" as default chart title.
    chartTitleLabel = new JLabel("Pie Chart");
    // put field and button in one panel and label into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frames
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the button
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the button
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    /**
     * Bottom panel Include sortbutton
     */
    // create sort button
    sortBtn = new JButton("Sort by increasing order");
    // add all to bottomPanel
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.add(sortBtn);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
    splitPane.setDividerLocation(getWidth() / 2);
  } // end chartViewFrameSorted

  /**
   * Create the frame. VERTICAL SINGLE BAR CHART
   */
  public ChartViewFrameGUI(final ChartController.DataSet[] data, final ChartController.DataSet[] sortedData,
      final ArrayList<Bar> values, final ArrayList<Bar> sortedValues, final String fieldName) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * Create Chart and Legend
     */
    int primaryIncrements = (int) (sortedData[sortedData.length - 1].getQuantity() / 2);
    int secondaryIncrements = (int) (sortedData[sortedData.length - 1].getQuantity() / 4);
    int tertiaryIncrements = 10;
    final String xAxisLabel = "WRT wat field name";
    final String yAxisLabel = fieldName;
    ChartController.Axis yAxisV =
        new ChartController.Axis((int) (sortedData[sortedData.length - 1].getQuantity() * 2), 0, primaryIncrements,
            secondaryIncrements, tertiaryIncrements, xAxisLabel, yAxisLabel);
    final BarChartVPanel barChart = new BarChartVPanel(values, yAxisV);
    final VerticalBarLegendPanel legend = new VerticalBarLegendPanel(data);
    final BarChartVPanel sortedBarChart = new BarChartVPanel(sortedValues, yAxisV);
    final VerticalBarLegendPanel sortedLegend = new VerticalBarLegendPanel(sortedData);
    barChart.barWidth = 5;
    /**
     * center include chart and legend for display
     */
    // put chart and legend to JScrollPane so text can be scrolled when too long
    JScrollPane scrollPanelLeft = new JScrollPane(legend);
    JScrollPane scrollPanelRight = new JScrollPane(barChart);
    // put two JScrollPane into SplitPane
    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
    splitPane.setOneTouchExpandable(true);
    contentPane.add(splitPane, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title, xAxis label and yAxis label
     * 
     */
    // create a button print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // create a button to change the xAxis label
    xAxisLabelChangeBtn = new JButton("Change xAxis label");
    // create a button to change the yAxis label
    yAxisLabelChangeBtn = new JButton("Change yAxis label");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // Create a field to enter new xAxisLabel
    xAxisLabelField = new JTextField(10);
    // Create a field to enter new xAxisLabel
    yAxisLabelField = new JTextField(10);
    // create new Label with "Vertical Bar Chart" as default chart title.
    chartTitleLabel = new JLabel("Vertical Bar Chart");
    // put fields and buttons in one panel and labels into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.setLayout(new GridLayout(1, 3));
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    tmpPanel1.add(xAxisLabelChangeBtn);
    tmpPanel1.add(xAxisLabelField);
    tmpPanel1.add(yAxisLabelChangeBtn);
    tmpPanel1.add(yAxisLabelField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frames
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    xAxisLabelChangeBtn.setActionCommand("changeXAxisLabel");
    // add action for this button
    xAxisLabelChangeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ChartViewFrameVerticalXAxisGUI(data, sortedData, values, sortedValues, fieldName,
            xAxisLabelField.getText());
        xAxisLabelField.hide();
      }

      private void ChartViewFrameVerticalXAxisGUI(final DataSet[] data, final DataSet[] sortedData,
          final ArrayList<Bar> values, final ArrayList<Bar> sortedValues, final String fieldName,
          String text) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 200, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        /**
         * Create Chart and Legend
         */
        int primaryIncrements = (int) (sortedData[sortedData.length - 1].getQuantity() / 2);
        int secondaryIncrements = (int) (sortedData[sortedData.length - 1].getQuantity() / 4);
        int tertiaryIncrements = 10;
        final String xAxisLabel = text;
        // final String yAxisLabel = fieldName;
        Axis yAxisV =
            new Axis((int) (sortedData[sortedData.length - 1].getQuantity() * 2), 0,
                primaryIncrements, secondaryIncrements, tertiaryIncrements, xAxisLabel, yAxisLabel);
        final BarChartVPanel barChart = new BarChartVPanel(values, yAxisV);
        final VerticalBarLegendPanel legend = new VerticalBarLegendPanel(data);
        final BarChartVPanel sortedBarChart = new BarChartVPanel(sortedValues, yAxisV);
        final VerticalBarLegendPanel sortedLegend = new VerticalBarLegendPanel(sortedData);
        barChart.barWidth = 5;
        /**
         * center include chart and legend for display
         */
        // put chart and legend to JScrollPane so text can be scrolled when too long
        JScrollPane scrollPanelLeft = new JScrollPane(legend);
        JScrollPane scrollPanelRight = new JScrollPane(barChart);
        // put two JScrollPane into SplitPane
        JSplitPane splitPane =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
        splitPane.setOneTouchExpandable(true);
        contentPane.add(splitPane, BorderLayout.CENTER);
        /**
         * Top Include label, button and a field to change the Title, xAxis label and yAxis label
         * 
         */
        // create a button to print the chart
        printBtn = new JButton("Print Chart");
        // create a button to change the title of the chart
        titleChangeBtn = new JButton("Change Title");
        // create a button to change the xAxis label
        xAxisLabelChangeBtn = new JButton("Change xAxis label");
        // create a button to change the yAxis label
        yAxisLabelChangeBtn = new JButton("Change yAxis label");
        // Create a field to enter new Title
        chartTitleField = new JTextField(10);
        // Create a field to enter new xAxisLabel
        xAxisLabelField = new JTextField(10);
        // Create a field to enter new xAxisLabel
        yAxisLabelField = new JTextField(10);
        // create new Label with "Vertical Bar Chart" as default chart title.
        chartTitleLabel = new JLabel("Vertical Bar Chart");
        // put fields and buttons in one panel and labels into a separate panel
        JPanel tmpPanel1 = new JPanel();
        tmpPanel1.setLayout(new GridLayout(1, 2));
        tmpPanel1.add(printBtn);
        tmpPanel1.add(titleChangeBtn);
        tmpPanel1.add(chartTitleField);
        tmpPanel1.add(xAxisLabelChangeBtn);
        tmpPanel1.add(xAxisLabelField);
        tmpPanel1.add(yAxisLabelChangeBtn);
        tmpPanel1.add(yAxisLabelField);
        JPanel tmpPanel2 = new JPanel();
        tmpPanel2.add(chartTitleLabel);
        // finally. put all those into TopPane
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(tmpPanel1);
        topPanel.add(tmpPanel2);
        // Add TopPane to the frames
        contentPane.add(topPanel, BorderLayout.NORTH);
        // Set actions for the buttons
        printBtn.setActionCommand("printChart");
        // add action for this button
        printBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("printChart")) {
              System.out.println("Do Print");
            }
          }
        });
        // Set actions for the buttons
        titleChangeBtn.setActionCommand("changeTitle");
        // add action for this button
        titleChangeBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("changeTitle")) {
              chartTitleLabel.setText(chartTitleField.getText());
              chartTitleField.hide();
            }
          }
        });
        yAxisLabelChangeBtn.setActionCommand("changeYAxisLabel");
        yAxisLabelChangeBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            ChartViewFrameVerticalYAxisGUI(data, sortedData, values, sortedValues, fieldName,
                yAxisLabelField.getText());
            yAxisLabelField.hide();
          }

          private void ChartViewFrameVerticalYAxisGUI(DataSet[] data, DataSet[] sortedData,
              ArrayList<Bar> values, ArrayList<Bar> sortedValues, String fieldName, String text) {
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setBounds(200, 200, 700, 400);
            contentPane = new JPanel();
            contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
            contentPane.setLayout(new BorderLayout(0, 0));
            /**
             * Create Chart and Legend
             */
            int primaryIncrements = (int) (sortedData[sortedData.length - 1].getQuantity() / 2);
            int secondaryIncrements = (int) (sortedData[sortedData.length - 1].getQuantity() / 4);
            int tertiaryIncrements = 10;
            // final String xAxisLabel = text;
            final String yAxisLabel = text;
            Axis yAxisV =
                new Axis((int) (sortedData[sortedData.length - 1].getQuantity() * 2), 0,
                    primaryIncrements, secondaryIncrements, tertiaryIncrements, xAxisLabel,
                    yAxisLabel);
            final BarChartVPanel barChart = new BarChartVPanel(values, yAxisV);
            final VerticalBarLegendPanel legend = new VerticalBarLegendPanel(data);
            final BarChartVPanel sortedBarChart = new BarChartVPanel(sortedValues, yAxisV);
            final VerticalBarLegendPanel sortedLegend = new VerticalBarLegendPanel(sortedData);
            barChart.barWidth = 5;
            /**
             * center include chart and legend for display
             */
            // put chart and legend to JScrollPane so text can be scrolled when too long
            JScrollPane scrollPanelLeft = new JScrollPane(legend);
            JScrollPane scrollPanelRight = new JScrollPane(barChart);
            // put two JScrollPane into SplitPane
            JSplitPane splitPane =
                new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
            splitPane.setOneTouchExpandable(true);
            contentPane.add(splitPane, BorderLayout.CENTER);
            /**
             * Top Include label, button and a field to change the Title, xAxis label and yAxis
             * label
             * 
             */
            // create a button to print the chart
            printBtn = new JButton("Print Chart");
            // create a button to change the title of the chart
            titleChangeBtn = new JButton("Change Title");
            // create a button to change the xAxis label
            xAxisLabelChangeBtn = new JButton("Change xAxis label");
            // create a button to change the yAxis label
            yAxisLabelChangeBtn = new JButton("Change yAxis label");
            // Create a field to enter new Title
            chartTitleField = new JTextField(10);
            // Create a field to enter new xAxisLabel
            xAxisLabelField = new JTextField(10);
            // Create a field to enter new xAxisLabel
            yAxisLabelField = new JTextField(10);
            // create new Label with "Vertical Bar Chart" as default
            // chart title.
            chartTitleLabel = new JLabel("Vertical Bar Chart");
            // put fields and buttons in one panel and labels into a
            // separate panel
            JPanel tmpPanel1 = new JPanel();
            tmpPanel1.setLayout(new GridLayout(1, 2));
            tmpPanel1.add(printBtn);
            tmpPanel1.add(titleChangeBtn);
            tmpPanel1.add(chartTitleField);
            tmpPanel1.add(xAxisLabelChangeBtn);
            tmpPanel1.add(yAxisLabelChangeBtn);
            tmpPanel1.add(yAxisLabelField);
            JPanel tmpPanel2 = new JPanel();
            tmpPanel2.add(chartTitleLabel);
            // finally. put all those into TopPane
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new GridLayout(1, 2));
            topPanel.add(tmpPanel1);
            topPanel.add(tmpPanel2);
            // Add TopPane to the frames
            contentPane.add(topPanel, BorderLayout.NORTH);
            // Set actions for the buttons
            printBtn.setActionCommand("printChart");
            // add action for this button
            printBtn.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("printChart")) {
                  System.out.println("Do Print");
                }
              }
            });
            // Set actions for the buttons
            titleChangeBtn.setActionCommand("changeTitle");
            // add action for this button
            titleChangeBtn.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("changeTitle")) {
                  chartTitleLabel.setText(chartTitleField.getText());
                  chartTitleField.hide();
                }
              }
            });
            /**
             * Bottom panel Include sortbutton
             */
            // create sort button
            sortBtn = new JButton("Sort by Increasing order");
            // add action for this button
            sortBtn.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent e) {
                chartBarViewFrameSorted(sortedData1, sortedBarChart, sortedLegend);
              }
            });
            // add all to bottomPanel
            JPanel bottomPanel = new JPanel(new FlowLayout());
            bottomPanel.add(sortBtn);
            contentPane.add(bottomPanel, BorderLayout.SOUTH);
            setContentPane(contentPane);
            setTitle("THE CHARTING TOOL");
            // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
            setVisible(true);
            splitPane.setDividerLocation(getWidth() / 2);
          }
        });
        /**
         * Bottom panel Include sortbutton
         */
        // create sort button
        sortBtn = new JButton("Sort by Increasing order");
        // add action for this button
        sortBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            chartBarViewFrameSorted(sortedData1, sortedBarChart, sortedLegend);
          }
        });
        // add all to bottomPanel
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(sortBtn);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(contentPane);
        setTitle("THE CHARTING TOOL");
        setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setVisible(true);
        splitPane.setDividerLocation(getWidth() / 2);
      }
    });
    yAxisLabelChangeBtn.setActionCommand("changeYAxisLabel");
    // yAxisLabelChangeBtn.addActionListener(this);
    /**
     * Bottom panel Include sortbutton
     */
    // create sort button
    sortBtn = new JButton("Sort by Increasing order");
    // add action for this button
    sortBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chartBarViewFrameSorted(sortedData1, sortedBarChart, sortedLegend);
      }
    });
    // add all to bottomPanel
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.add(sortBtn);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
    splitPane.setDividerLocation(getWidth() / 2);
  } // end fn ChartViewFrameGUI for Vert Single BarChart

  private void chartBarViewFrameSorted(DataSet[] sortedData1, BarChartVPanel sortedChart1,
      VerticalBarLegendPanel sortedLegend1) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 300);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * center include chart and legend for display
     */
    // put chart and legend to JScrollPane so text can be scrolled when too long
    JScrollPane scrollPanelLeft = new JScrollPane(sortedLegend1);
    JScrollPane scrollPanelRight = new JScrollPane(sortedChart1);
    // put two JScrollPane into SplitPane
    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
    splitPane.setOneTouchExpandable(true);
    contentPane.add(splitPane, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     */
    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // create new Label with "Vertical Bar Chart" as default chart title.
    chartTitleLabel = new JLabel("Vertical Bar Chart");
    // put fields and buttons in one panel and labels into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frame
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    /**
     * Bottom panel Include sortbutton
     */
    // create sort button
    sortBtn = new JButton("Sort by Increasing order");
    // add all to bottomPanel
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.add(sortBtn);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
    splitPane.setDividerLocation(getWidth() / 2);
  }// ends fn ChartBarViewFrameSorted

  /**
   * Create the frame. VERTICAL MULTIBAR CHART
   */
  public ChartViewFrameGUI(final ArrayList<Bar> values1, final ArrayList<Bar> values2,
      final List<String> fieldNameList, final DataSet[] data, final double maxValueOfTwoFields) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * Create the chart and legend
     */
    final String yAxisLabel = "WRT wat field name";
    ArrayList<MultiBar> ml = new ArrayList<MultiBar>();
    MultiBar mb1 = new MultiBar(values1);
    ml.add(mb1);
    MultiBar mb2 = new MultiBar(values2);
    ml.add(mb2);
    int primaryIncrements = (int) (maxValueOfTwoFields / 2);
    int secondaryIncrements = (int) (maxValueOfTwoFields / 4);
    int tertiaryIncrements = 10;
    Axis yAxis =
        new Axis((int) (maxValueOfTwoFields * 2), 0, primaryIncrements, secondaryIncrements,
            tertiaryIncrements, fieldNameList.get(0) + " & " + fieldNameList.get(1), yAxisLabel);
    final VerticalBarLegendPanel legend = new VerticalBarLegendPanel(data);
    final MultiBarChartVPanel multibarChart = new MultiBarChartVPanel(ml, yAxis, fieldNameList);
    multibarChart.width = 1300;
    if (ml.size() > 2) {
      multibarChart.barWidth = 2;
    } else {
      multibarChart.barWidth = 5;
      multibarChart.xCatFont = new Font("Arial", Font.PLAIN, 12);
    }
    /**
     * center include chart and legend for display
     */
    // put chart and legend to JScrollPane so text can be scrolled when too long
    JScrollPane scrollPanelLeft = new JScrollPane(legend);
    JScrollPane scrollPanelRight = new JScrollPane(multibarChart);
    // put two JScrollPane into SplitPane
    JSplitPane splitPane =
        new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
    splitPane.setOneTouchExpandable(true);
    contentPane.add(splitPane, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     * 
     */
    // create a button to change the title of the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // create a button to change the yAxis label
    yAxisLabelChangeBtn = new JButton("Change yAxis label");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // Create a field to enter new yAxisLabel
    yAxisLabelField = new JTextField(10);
    // create new Label with "Vertical Bar Chart" as default chart title.
    chartTitleLabel = new JLabel("Vertical Bar Chart");
    // put fields and buttons in one panel and labels into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.setLayout(new GridLayout(1, 2));
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    tmpPanel1.add(yAxisLabelChangeBtn);
    tmpPanel1.add(yAxisLabelField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frame
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    yAxisLabelChangeBtn.setActionCommand("changeYAxisLabel");
    // yAxisLabelChangeBtn.addActionListener(this);
    yAxisLabelChangeBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ChartViewFrameMVerticalYAxisGUI(values1, values2, fieldNameList, data, maxValueOfTwoFields,
            yAxisLabelField.getText());
        yAxisLabelField.hide();
      }

      private void ChartViewFrameMVerticalYAxisGUI(final ArrayList<Bar> values1,
          final ArrayList<Bar> values2, final List<String> fieldNameList, final DataSet[] data,
          final double maxValueOfTwoFields, String text) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(200, 200, 700, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        /**
         * Create the chart and legend
         */
        final String yAxisLabel = text;
        ArrayList<MultiBar> ml = new ArrayList<MultiBar>();
        MultiBar mb1 = new MultiBar(values1);
        ml.add(mb1);
        MultiBar mb2 = new MultiBar(values2);
        ml.add(mb2);
        int primaryIncrements = (int) (maxValueOfTwoFields / 2);
        int secondaryIncrements = (int) (maxValueOfTwoFields / 4);
        int tertiaryIncrements = 10;
        Axis yAxis =
            new Axis((int) (maxValueOfTwoFields * 2), 0, primaryIncrements, secondaryIncrements,
                tertiaryIncrements, fieldNameList.get(0) + " & " + fieldNameList.get(1), yAxisLabel);
        final VerticalBarLegendPanel legend = new VerticalBarLegendPanel(data);
        final MultiBarChartVPanel multibarChart = new MultiBarChartVPanel(ml, yAxis, fieldNameList);
        multibarChart.width = 1300;
        if (ml.size() > 2) {
          multibarChart.barWidth = 2;
        } else {
          multibarChart.barWidth = 5;
          multibarChart.xCatFont = new Font("Arial", Font.PLAIN, 12);
        }
        /**
         * center include chart and legend for display
         */
        // put chart and legend to JScrollPane so text can be scrolled when too long
        JScrollPane scrollPanelLeft = new JScrollPane(legend);
        JScrollPane scrollPanelRight = new JScrollPane(multibarChart);
        // put two JScrollPane into SplitPane
        JSplitPane splitPane =
            new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanelLeft, scrollPanelRight);
        splitPane.setOneTouchExpandable(true);
        contentPane.add(splitPane, BorderLayout.CENTER);
        /**
         * Top Include label, button and a field to change the Title
         * 
         */
        // create a button to print the chart
        printBtn = new JButton("Print Chart");
        // create a button to change the title of the chart
        titleChangeBtn = new JButton("Change Title");
        // create a button to change the yAxis label
        JButton yAxisLabelChangeBtn = new JButton("Change yAxis label");
        // Create a field to enter new Title
        chartTitleField = new JTextField(10);
        // Create a field to enter new yAxisLabel
        yAxisLabelField = new JTextField(10);
        // create new Label with "Vertical Bar Chart" as default chart title.
        chartTitleLabel = new JLabel("Vertical Bar Chart");
        // put fields and buttons in one panel and labels into a separate panel
        JPanel tmpPanel1 = new JPanel();
        tmpPanel1.setLayout(new GridLayout(1, 2));
        tmpPanel1.add(printBtn);
        tmpPanel1.add(titleChangeBtn);
        tmpPanel1.add(chartTitleField);
        tmpPanel1.add(yAxisLabelChangeBtn);
        JPanel tmpPanel2 = new JPanel();
        tmpPanel2.add(chartTitleLabel);
        // finally. put all those into TopPane
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 2));
        topPanel.add(tmpPanel1);
        topPanel.add(tmpPanel2);
        // Add TopPane to the frame
        contentPane.add(topPanel, BorderLayout.NORTH);
        // Set actions for the buttons
        printBtn.setActionCommand("printChart");
        // add action for this button
        printBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("printChart")) {
              System.out.println("Do Print");
            }
          }
        });
        // Set actions for the buttons
        titleChangeBtn.setActionCommand("changeTitle");
        // add action for this button
        titleChangeBtn.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("changeTitle")) {
              chartTitleLabel.setText(chartTitleField.getText());
              chartTitleField.hide();
            }
          }
        });
        setContentPane(contentPane);
        setTitle("THE CHARTING TOOL");
        // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setVisible(true);
        splitPane.setDividerLocation(getWidth() / 2);
      }
    });
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
    splitPane.setDividerLocation(getWidth() / 2);
  } // end ChartViewFrameVUI for Vertical Multibar Chart

  /*
   * Create the frame. HORIZONTAL MULTIBAR CHART
   */
  public ChartViewFrameGUI(MultiBarChartHPanel chart1) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * center include chart for display
     */
    contentPane.add(chart1, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     * 
     */
    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // create new Label with "Horizontal Bar Chart" as default chart title.
    chartTitleLabel = new JLabel("Horizontal Bar Chart");
    // put fields and buttons in one panel and labels into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frame
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
  } // end ChartViewFrameGUI for Horiz Multi Bar Chart

  /**
   * Create the frame. HORIZONTAL SINGLE BAR CHART
   */
  public ChartViewFrameGUI(MultiBarChartHPanel chart1, final DataSet[] sortedData1,
      final MultiBarChartHPanel sortedChart1) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * center include chart for display
     */
    contentPane.add(chart1, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     * 
     */
    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // create new Label with "Horizontal Bar Chart" as default chart title.
    chartTitleLabel = new JLabel("Horizontal Bar Chart");
    // put fields and buttons in one panel and labels into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // / finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frame
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    /**
     * Bottom panel Include sortbutton
     */
    // create sort button
    sortBtn = new JButton("Sort by Increasing order");
    // add action for this button
    sortBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        chartBarViewFrameSorted(sortedData1, sortedChart1);
      }
    });
    // add all to bottomPanel
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.add(sortBtn);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
  } // end ChartViewFrameGUI(M.....

  private void chartBarViewFrameSorted(DataSet[] sortedData1, MultiBarChartHPanel sortedChart1) {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(200, 200, 700, 400);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    /**
     * center include chart for display
     */
    contentPane.add(sortedChart1, BorderLayout.CENTER);
    /**
     * Top Include label, button and a field to change the Title
     * 
     */
    // create a button to print the chart
    printBtn = new JButton("Print Chart");
    // create a button to change the title of the chart
    titleChangeBtn = new JButton("Change Title");
    // Create a field to enter new Title
    chartTitleField = new JTextField(10);
    // create new Label with "Horizontal Bar Chart" as default chart title.
    chartTitleLabel = new JLabel("Horizontal Bar Chart");
    // put fields and buttons in one panel and labels into a separate panel
    JPanel tmpPanel1 = new JPanel();
    tmpPanel1.add(printBtn);
    tmpPanel1.add(titleChangeBtn);
    tmpPanel1.add(chartTitleField);
    JPanel tmpPanel2 = new JPanel();
    tmpPanel2.add(chartTitleLabel);
    // finally. put all those into TopPane
    JPanel topPanel = new JPanel();
    topPanel.setLayout(new GridLayout(1, 2));
    topPanel.add(tmpPanel1);
    topPanel.add(tmpPanel2);
    // Add TopPane to the frame
    contentPane.add(topPanel, BorderLayout.NORTH);
    // Set actions for the buttons
    printBtn.setActionCommand("printChart");
    printBtn.addActionListener(this);
    // Set actions for the buttons
    titleChangeBtn.setActionCommand("changeTitle");
    titleChangeBtn.addActionListener(this);
    /**
     * Bottom panel Include sortbutton
     */
    // create sort button
    sortBtn = new JButton("Sort by Increasing order");
    // add all to bottomPanel
    JPanel bottomPanel = new JPanel(new FlowLayout());
    bottomPanel.add(sortBtn);
    contentPane.add(bottomPanel, BorderLayout.SOUTH);
    setContentPane(contentPane);
    setTitle("THE CHARTING TOOL");
    // setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
    setVisible(true);
  } // end chartBarViewFrameSorted

  public void actionPerformed(ActionEvent e) {
    if (e.getActionCommand().equals("printChart")) {
      try {
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension d = tool.getScreenSize();
        Rectangle rect = new Rectangle(d);
        Robot robot = new Robot();
        Thread.sleep(2000);
        File f = new File("screenshot.jpg");
        BufferedImage img = robot.createScreenCapture(rect);
        ImageIO.write(img, "jpeg", f);
        tool.beep();
        JOptionPane.showMessageDialog(null, "Set printer settings");
      } catch (Exception et) {
        et.printStackTrace();
      }
      try {
        image = ImageIO.read(new File("screenshot.jpg"));
      } catch (IOException e2) {
        e2.printStackTrace();
      }
      printJob = PrinterJob.getPrinterJob();
      PageFormat preformat = printJob.defaultPage();
      PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();

      preformat = printJob.pageDialog(aset);
      printJob.setPrintable(new Printable() {

        public int print(Graphics g, PageFormat preformat, int pageIndex) throws PrinterException {

          if (pageIndex != 0) {
            return NO_SUCH_PAGE;
          } else {
            int pWidth = 0;
            int pHeight = 0;
            pWidth = (int) Math.min(preformat.getImageableWidth(), (double) image.getWidth());
            pHeight = pWidth * image.getHeight() / image.getWidth();
            g.drawImage(image, (int) preformat.getImageableX(), (int) preformat.getImageableY(),
                pWidth, pHeight, null);
            return PAGE_EXISTS;
          }
        }
      }, preformat);
      try {
        printJob.print();
      } catch (PrinterException e1) {
        e1.printStackTrace();
      }
    } else if (e.getActionCommand().equals("changeTitle")) {
      chartTitleLabel.setText(chartTitleField.getText());
      chartTitleField.hide();
    } else if (e.getActionCommand().equals("changeXAxisLabel")) {
      xAxisLabelField.hide();
    } else if (e.getActionCommand().equals("changeYAxisLabel")) {
      yAxisLabelField.hide();
    }
  } // end actionPerformed
}
