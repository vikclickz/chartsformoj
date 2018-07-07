package com.sdsu.edu.main.controller;

import com.sdsu.edu.main.gui.ChartViewFrameGUI;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.ToolTipManager;

/*
 * The Rendering of charts both pie and bar-vertical and horizontal
 * Finally display in a GUI with print, change title, x-axis and y-axis label
 */
public class ChartController {
  private static ChartController chartController = new ChartController();
  /* Static 'instance' method */
  public static ChartController getInstance() {
    return chartController;
  }
  /*
   * A dataset Object which contains the String name and its respective quantity
  */
  public static class DataSet {
    private String name;
    private Double quantity;
    private Color vcolor;
    public DataSet(String vname, double vquantity) {
      this.setName(vname);
      this.setQuantity(new Double(vquantity));
    }
    public DataSet(double value, Color color, String name) {
      this.quantity = value;
      this.vcolor = color;
      this.name = name;
    }
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public Double getQuantity() {
      return quantity;
    }
    public void setQuantity(double quantity) {
      this.quantity = quantity;
    }
    public Color getVcolor() {
      return vcolor;
    }
    public void setVcolor(Color vcolor) {
      this.vcolor = vcolor;
    }

  }  //end DataSet
  //ChartingToolStart starthere;
  double maxValueOfTwoFields;
  String chartColorSType;
  String fieldName;
  String[] stateName;
  List<Double> stateAreaList;
  List<String> fieldNameList;
  List<List<Double>> fieldValueList;
  String xAxisLabel;
  String yAxisLabel;

  public void pieChart(String chartColorSType, String fieldName,
                 String[] stateName, List<Double> stateAreaList) {
    final DataSet[] data1 = new DataSet[stateName.length];
    final DataSet[] sortedData1 = new DataSet[stateName.length];
    Double[] stateArea = (Double[]) stateAreaList
                   .toArray(new Double[stateAreaList.size()]);
    List<String> sortedStateName = new ArrayList<String>();
    for (int i = 0; i < stateName.length; i++) {
      data1[i] = new DataSet(stateName[i], stateArea[i]);
    }
    // sort the array stateArea into another sorted array: sortedStateArea here
    ArrayList<Double> nstore = new ArrayList<Double>(stateAreaList);
    Collections.sort(stateAreaList);
    /*
     * http://stackoverflow.com/questions/15789979/initial-indexes-of-sorted-
     * elements-of-arraylist
    */
    int[] indexes = new int[stateAreaList.size()];
    for (int n = 0; n < stateAreaList.size(); n++) {
      indexes[n] = nstore.indexOf(stateAreaList.get(n));
      sortedStateName.add(stateName[indexes[n]]);
    }
    Double[] sortedStateArea = (Double[]) stateAreaList.toArray(new Double[stateAreaList.size()]);
    for (int i = 0; i < stateName.length; i++) {
      sortedData1[i] = new DataSet(sortedStateName.get(i),
      sortedStateArea[i]);
    }
    /*
     * Create the chart and legend for the given data
    */
    final PieChartPanel chart1 = new PieChartPanel(data1, chartColorSType);
    final PieLegendPanel legend1 = new PieLegendPanel(data1);
    final PieChartPanel sortChart1 = new PieChartPanel(sortedData1,chartColorSType);
    final PieLegendPanel sortLegend1 = new PieLegendPanel(sortedData1);
    /*
     * Add the chart and legend to the view
    */
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          ChartViewFrameGUI frame = new ChartViewFrameGUI(data1,
          sortedData1, chart1, legend1, sortChart1,sortLegend1);
        } catch (Exception e) {
            e.printStackTrace();
        }
      }
    });
  }  // end pieChart

  public void horizontalSingleBarChart(String chartColorSType,
         String fieldName, String[] stateName, List<Double> stateAreaList) {
    final DataSet[] data1 = new DataSet[stateName.length];
    final DataSet[] sortedData1 = new DataSet[stateName.length];
    Random random = new Random();
    Double[] fieldvalue1 = (Double[]) stateAreaList.toArray(new Double[stateAreaList.size()]);
    List<String> sortedStateName = new ArrayList<String>();
    for (int i = 0; i < stateName.length; i++) {
      data1[i] = new DataSet(stateName[i], fieldvalue1[i]);
    }
    // sort the array stateArea into another sorted array: sortedStateArea here
    ArrayList<Double> nstore = new ArrayList<Double>(stateAreaList);
    Collections.sort(stateAreaList);
    /*
     * http://stackoverflow.com/questions/15789979/initial-indexes-of-sorted-
      * elements-of-arraylist
    */
    int[] indexes = new int[stateAreaList.size()];
    for (int n = 0; n < stateAreaList.size(); n++) {
      indexes[n] = nstore.indexOf(stateAreaList.get(n));
      sortedStateName.add(stateName[indexes[n]]);
    }
    Double[] sortedStateArea = (Double[]) stateAreaList.toArray(new Double[stateAreaList.size()]);
    for (int i = 0; i < stateName.length; i++) {
      sortedData1[i] = new DataSet(sortedStateName.get(i),
      sortedStateArea[i]);
    }
    /*
     * Create bar values : "Normal", "Pastel", "Rainbow"
    */
    ArrayList<Bar> values1 = new ArrayList<Bar>();
    ArrayList<Bar> sortedValues1 = new ArrayList<Bar>();
    if (chartColorSType.equalsIgnoreCase("Normal")) {
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color.RED,stateName[i]));
        sortedValues1.add(new Bar((int) (100 * sortedStateArea[i]),Color.RED, sortedStateName.get(i)));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Rainbow")) {
      final float hue = random.nextFloat();
      final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
      final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
                 .getHSBColor(hue, saturation, luminance), stateName[i]));
        sortedValues1.add(new Bar((int) (100 * sortedStateArea[i]),
                 Color.getHSBColor(hue, saturation, luminance),sortedStateName.get(i)));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Pastel")) {
      final float hue = random.nextFloat();
      // Saturation between 0.1 and 0.3
      final float saturation = (random.nextInt(2000) + 1000) / 10000f;
      final float luminance = 0.9f;
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
             .getHSBColor(hue, saturation, luminance), stateName[i]));
        sortedValues1.add(new Bar((int) (100 * sortedStateArea[i]),
            Color.getHSBColor(hue, saturation, luminance),sortedStateName.get(i)));
      }
    }
    /*
     * Create the chart for the given data
    */
    HorizontalBarController testhorizonalbar = new HorizontalBarController();
    testhorizonalbar.horizontalSingleBarMain(values1, fieldName,sortedValues1, sortedData1);
  } // end horizontalSingleBarChart

  public void verticalSingleBarChart(String chartColorSType,
           String fieldName, String[] stateName, List<Double> stateAreaList) {
    final DataSet[] data1 = new DataSet[stateName.length];
    final DataSet[] sortedData1 = new DataSet[stateName.length];
    Random random = new Random();
    Double[] fieldvalue1 = (Double[]) stateAreaList.toArray(new Double[stateAreaList.size()]);
    List<String> sortedStateName = new ArrayList<String>();
    for (int i = 0; i < stateName.length; i++) {
      data1[i] = new DataSet(stateName[i], fieldvalue1[i]);
    }
    // sort the array stateArea into another sorted array: sortedStateArea here
    ArrayList<Double> nstore = new ArrayList<Double>(stateAreaList);
    Collections.sort(stateAreaList);
    /*
     * http://stackoverflow.com/questions/15789979/initial-indexes-of-sorted-
     * elements-of-arraylist
    */
    int[] indexes = new int[stateAreaList.size()];
    for (int n = 0; n < stateAreaList.size(); n++) {
      indexes[n] = nstore.indexOf(stateAreaList.get(n));
      sortedStateName.add(stateName[indexes[n]]);
    }
    Double[] sortedStateArea = (Double[]) stateAreaList.toArray(new Double[stateAreaList.size()]);
    for (int i = 0; i < stateName.length; i++) {
      sortedData1[i] = new DataSet(sortedStateName.get(i),
      sortedStateArea[i]);
    }
    /*
     * Create bar values : "Normal", "Pastel", "Rainbow"
    */
    ArrayList<Bar> values1 = new ArrayList<Bar>();
    ArrayList<Bar> sortedValues1 = new ArrayList<Bar>();
    if (chartColorSType.equalsIgnoreCase("Normal")) {
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color.RED,stateName[i]));
        sortedValues1.add(new Bar((int) (100 * sortedStateArea[i]),
                                      Color.RED, sortedStateName.get(i)));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Rainbow")) {
      final float hue = random.nextFloat();
      final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
      final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
                 .getHSBColor(hue, saturation, luminance), stateName[i]));
        sortedValues1.add(new Bar((int) (100 * sortedStateArea[i]),
            Color.getHSBColor(hue, saturation, luminance),sortedStateName.get(i)));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Pastel")) {
      final float hue = random.nextFloat();
      // Saturation between 0.1 and 0.3
      final float saturation = (random.nextInt(2000) + 1000) / 10000f;
      final float luminance = 0.9f;
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
              .getHSBColor(hue, saturation, luminance), stateName[i]));
        sortedValues1.add(new Bar((int) (100 * sortedStateArea[i]),
              Color.getHSBColor(hue, saturation, luminance),sortedStateName.get(i)));
      }
    }
    /*
     * Create the chart for the given data
    */
    ChartViewFrameGUI frame = new ChartViewFrameGUI(data1, sortedData1, values1, sortedValues1, fieldName);
  } // end verticalSingleBarChart

  public void horizontalMultiBarChart(String chartColorSType,
        List<String> fieldNameList, String[] stateName,List<List<Double>> fieldvalueList) {
    Random random = new Random();
    Double[] fieldvalue1;
    Double[] fieldvalue2;
    fieldvalue1 = (Double[]) fieldvalueList.get(0).toArray(
                     new Double[fieldvalueList.get(0).size()]);
    fieldvalue2 = (Double[]) fieldvalueList.get(1).toArray(
                     new Double[fieldvalueList.get(1).size()]);
    Collections.sort(fieldvalueList.get(0));
    Double[] sortedFieldvalue1 = (Double[]) fieldvalueList.get(0)
              .toArray(new Double[fieldvalueList.get(0).size()]);
    Collections.sort(fieldvalueList.get(1));
    Double[] sortedFieldvalue2 = (Double[]) fieldvalueList.get(1)
             .toArray(new Double[fieldvalueList.get(1).size()]);
    maxValueOfTwoFields = Math.max(sortedFieldvalue1[sortedFieldvalue1.length-1], sortedFieldvalue2[sortedFieldvalue1.length-1]);
    /*
     * Create bar values : "Normal", "Pastel", "Rainbow"
    */
    ArrayList<Bar> values1 = new ArrayList<Bar>();
    ArrayList<Bar> values2 = new ArrayList<Bar>();
    if (chartColorSType.equalsIgnoreCase("Normal")) {
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color.RED,stateName[i]));
        values2.add(new Bar((int) (100 * fieldvalue2[i]), Color.BLUE,stateName[i]));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Rainbow")) {
      final float hue = random.nextFloat();
      final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
      final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
                .getHSBColor(hue, saturation, luminance), stateName[i]));
        values2.add(new Bar((int) (100 * fieldvalue2[i]),
            Color.getHSBColor(hue * 10, saturation * 10,luminance * 10), stateName[i]));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Pastel")) {
      final float hue = random.nextFloat();
      // Saturation between 0.1 and 0.3
      final float saturation = (random.nextInt(2000) + 1000) / 10000f;
      final float luminance = 0.9f;
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
              .getHSBColor(hue, saturation, luminance), stateName[i]));
        values2.add(new Bar((int) (100 * fieldvalue2[i]),
            Color.getHSBColor(hue * 10, saturation * 10,luminance * 10), stateName[i]));
      }
    }
    /*
     * Create the chart for the given data
    */
    HorizontalBarController testhorizonalbar = new HorizontalBarController();
    testhorizonalbar.horizontalTwoBarMain(values1, values2, fieldNameList, maxValueOfTwoFields);
  }  // end horizontalMultiBarChart

  public void verticalMultiBarChart(String chartColorSType,
            List<String> fieldNameList, String[] stateName,
            List<List<Double>> fieldvalueList) {
    final DataSet[] data1 = new DataSet[stateName.length];
    Random random = new Random();
    Double[] fieldvalue1;
    Double[] fieldvalue2;
    fieldvalue1 = (Double[]) fieldvalueList.get(0).toArray(
          new Double[fieldvalueList.get(0).size()]);
    fieldvalue2 = (Double[]) fieldvalueList.get(1).toArray(
          new Double[fieldvalueList.get(1).size()]);
    for (int i = 0; i < stateName.length; i++) {
      data1[i] = new DataSet(stateName[i], fieldvalue1[i]);
    }
    Collections.sort(fieldvalueList.get(0));
    Double[] sortedFieldvalue1 = (Double[]) fieldvalueList.get(0)
                .toArray(new Double[fieldvalueList.get(0).size()]);
    Collections.sort(fieldvalueList.get(1));
    Double[] sortedFieldvalue2 = (Double[]) fieldvalueList.get(1)
                    .toArray(new Double[fieldvalueList.get(1).size()]);
    maxValueOfTwoFields = Math.max(sortedFieldvalue1[sortedFieldvalue1.length-1],
           sortedFieldvalue2[sortedFieldvalue1.length-1]);
    /*
     * Create bar values : "Normal", "Pastel", "Rainbow"
    */
    ArrayList<Bar> values1 = new ArrayList<Bar>();
    ArrayList<Bar> values2 = new ArrayList<Bar>();
    if (chartColorSType.equalsIgnoreCase("Normal")) {
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color.RED,stateName[i]));
        values2.add(new Bar((int) (100 * fieldvalue2[i]), Color.BLUE,stateName[i]));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Rainbow")) {
      final float hue = random.nextFloat();
      final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
      final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
               .getHSBColor(hue, saturation, luminance), stateName[i]));
        values2.add(new Bar((int) (100 * fieldvalue2[i]),
            Color.getHSBColor(hue * 10, saturation * 10,luminance * 10), stateName[i]));
      }
    }
    if (chartColorSType.equalsIgnoreCase("Pastel")) {
      final float hue = random.nextFloat();
      // Saturation between 0.1 and 0.3
      final float saturation = (random.nextInt(2000) + 1000) / 10000f;
      final float luminance = 0.9f;
      for (int i = 0; i < stateName.length; i++) {
        values1.add(new Bar((int) (100 * fieldvalue1[i]), Color
            .getHSBColor(hue, saturation, luminance), stateName[i]));
        values2.add(new Bar((int) (100 * fieldvalue2[i]),
            Color.getHSBColor(hue * 10, saturation * 10,luminance * 10), stateName[i]));
      }
    }
    /*
     * Create the chart for the given data
    */
    ChartViewFrameGUI frame = new ChartViewFrameGUI( values1,
               values2, fieldNameList, data1, maxValueOfTwoFields);
  }  // end VerticalMultiBarChart

  /*
   * Pie Chart Framework: Legend and chart rendering
  */
  public class PieChartPanel extends JPanel {
    private DataSet[] dataset;
    private double totalQuant;
    private String chartColor;
    Random random = new Random();
    double sumStateArea = 0.0;
    public PieChartPanel(DataSet[] data, String chartColorSType) {
      this.dataset = data;
      this.chartColor = chartColorSType;
    }
    @Override
    protected void paintComponent(Graphics g) {
      int width = getSize().height;
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                              RenderingHints.VALUE_ANTIALIAS_ON);
      totalQuant = 0;
      for (DataSet eachData : dataset) {
        totalQuant += eachData.getQuantity();
        if (chartColor.equalsIgnoreCase("Normal")) {
          eachData.setVcolor(new Color((int) (Math.random() * 0x1000000)));
        } else if (chartColor.equalsIgnoreCase("Rainbow")) {
          final float hue = random.nextFloat();
          final float saturation = 0.9f;// 1.0 for brilliant, 0.0 for dull
          final float luminance = 1.0f; // 1.0 for brighter, 0.0 for black
          eachData.setVcolor(Color.getHSBColor(hue, saturation, luminance));
        } else if (chartColor.equalsIgnoreCase("Pastel")) {
          final float hue = random.nextFloat();
          // Saturation between 0.1 and 0.3
          final float saturation = (random.nextInt(2000) + 1000) / 10000f;
          final float luminance = 0.9f;
          eachData.setVcolor(Color.getHSBColor(hue, saturation, luminance));
        }
      }
      double endPoint = 0.0D;
      int arcStart = 0;
      for (final DataSet eachData : dataset) {
        arcStart = (int) (endPoint * 360 / totalQuant);
        int radius = (int) (eachData.getQuantity() * 360 / totalQuant);
        final Arc2D.Double arc = new Arc2D.Double(0, 0, width, width, (int) arcStart, radius,Arc2D.PIE);
        g2d.setColor(eachData.getVcolor());
        g2d.fill(arc);
        //g2d.fillArc(0, 0, width, width, (int) arcStart, radius);
        endPoint += eachData.getQuantity();
        // optional to get the circle boundary
        g2d.setColor(Color.BLACK);
        g2d.drawArc(0, 0, width, width, arcStart, radius);
      }
    }
  } // end PieChartPanel

  public class PieLegendPanel extends JPanel {
    private DataSet[] dataset;
    private double totalQuantity;
    public PieLegendPanel(DataSet[] data) {
      this.dataset = data;
      for(DataSet eachdata: data){
        totalQuantity+= eachdata.getQuantity();
      }
      setAutoscrolls(true);
    }
    @Override
    protected void paintComponent(Graphics g) {
      int width = 10;
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                           RenderingHints.VALUE_ANTIALIAS_ON);
      for (DataSet eachData : dataset) {
        final double eachFinalData = eachData.getQuantity();
        final double eachFinalTuncateData =  truncateDouble((eachData.getQuantity()*100/totalQuantity));
        g2d.setColor(eachData.getVcolor());
        final Shape rect = (Shape) new Rectangle2D.Double(8, width, 10, 10);
        // g2d.fillRect(8, width, 10, 10);
        g2d.fill(rect);
        g2d.drawString("     " + eachData.getName(), 10, width);
        width += 12;
        addMouseMotionListener(new MouseMotionListener() {
          public void mouseMoved(MouseEvent e) {
            if (rect.contains(e.getPoint())){
              setToolTipText(" " + eachFinalData + " : " + eachFinalTuncateData+"%");
            }
            ToolTipManager.sharedInstance().mouseMoved(e);
          }
          public void mouseDragged(MouseEvent e) {
          }
        });
      }
    } // end paintComponent
    private double truncateDouble(double d) {
      DecimalFormat threeDForm = new DecimalFormat("#.000");
      return Double.valueOf(threeDForm.format(d));
    }
  }  // end PieLegendPanel

  /*
   * Classes supporting Bar Chart Rendering
  */
  public static class Axis {
    public int primaryIncrements = 0;
    public int secondaryIncrements = 0;
    public int tertiaryIncrements = 0;
    public int maxValue = 950000;
    public int minValue = 0;
    public String xLabel;
    public String yLabel;
    Axis(String xname, String yname) {
      this(100, 0, 50, 10, 5, xname, yname);
    }
    Axis(int primaryIncrements, int secondaryIncrements,
           int tertiaryIncrements, String xname, String yname) {
      this(100, 0, primaryIncrements, secondaryIncrements,tertiaryIncrements, xname, yname);
    }
    public Axis(Integer maxValue, Integer minValue, int primaryIncrements,
        int secondaryIncrements, int tertiaryIncrements, String xname,String yname) {
      this.maxValue = maxValue;
      this.minValue = minValue;
      this.xLabel = xname;
      this.yLabel = yname;
      if (primaryIncrements != 0)
        this.primaryIncrements = primaryIncrements;
      if (secondaryIncrements != 0)
        this.secondaryIncrements = secondaryIncrements;
      if (tertiaryIncrements != 0)
        this.tertiaryIncrements = tertiaryIncrements;
    }
  }

  public class Bar {
    public double value;
    public Color color;
    public String name;
    public Bar(int value, Color color, String name) {
      this.value = value;
      this.color = color;
      this.name = name;
    }
  }
  public static class MultiBar {
    public ArrayList<Bar> bar;
    public MultiBar(ArrayList<Bar> bar) {
      this.bar = bar;
    }
  }
  /*
   * Horizontal Bar Chart Rendering
  */
  public class HorizontalBarController {
    public void horizontalTwoBarMain(ArrayList<Bar> values1,
        ArrayList<Bar> values2, List<String> fieldNameList, double maxValueOfTwoFields) {
        ArrayList<MultiBar> ml = new ArrayList<MultiBar>();
      MultiBar mb1 = new MultiBar(values1);
      ml.add(mb1);
      MultiBar mb2 = new MultiBar(values2);
      ml.add(mb2);
      int primaryIncrements = (int) (maxValueOfTwoFields)/2;
      int secondaryIncrements = (int) (maxValueOfTwoFields/4);
      int tertiaryIncrements = 10;
      Axis xAxis = new Axis((int) (maxValueOfTwoFields*1), 0, primaryIncrements, secondaryIncrements,
          tertiaryIncrements, fieldNameList.get(0) + " & " + fieldNameList.get(1), " ");
      final MultiBarChartHPanel multibarChart = new MultiBarChartHPanel(ml, xAxis,fieldNameList);
      multibarChart.height = 680;
      if (ml.size() > 2) {
        multibarChart.barHeight = 2;
      } else {
        multibarChart.barHeight = 4;
        multibarChart.yCatFont = new Font("Arial", Font.PLAIN, 12);
      }
      /*
       * Add the chart to the view
      */
      EventQueue.invokeLater(new Runnable() {
        public void run() {
          try {
            ChartViewFrameGUI frame = new ChartViewFrameGUI(multibarChart);
          } catch (Exception e) {
              e.printStackTrace();
          }
        }
      });
    }
    public void horizontalSingleBarMain(ArrayList<Bar> values,
         String fieldName, ArrayList<Bar> sortedValues,DataSet[] sortedData1) {
      final DataSet[] sortedData = sortedData1;
      ArrayList<MultiBar> ml = new ArrayList<MultiBar>();
      MultiBar mb1 = new MultiBar(values);
      ml.add(mb1);
      ArrayList<MultiBar> m2 = new ArrayList<MultiBar>();
      MultiBar mb2 = new MultiBar(sortedValues);
      m2.add(mb2);
      int primaryIncrements = (int) (sortedData[sortedData.length-1].getQuantity()/2);
      int secondaryIncrements = (int) (sortedData[sortedData.length-1].getQuantity()/4);
      int tertiaryIncrements = 10;
      Axis xAxis = new Axis((int) (sortedData[sortedData.length-1].getQuantity()*1), 0,
            primaryIncrements, secondaryIncrements,tertiaryIncrements, fieldName, "");
      final MultiBarChartHPanel multibarChart = new MultiBarChartHPanel(ml, xAxis);
      final MultiBarChartHPanel sortedMultibarChart = new MultiBarChartHPanel(m2, xAxis);
      multibarChart.height = 680;
      //sortedMultibarChart.height = 680;
      if ((ml.size() > 2) || (m2.size() > 2)) {
        multibarChart.barHeight = 2;
        sortedMultibarChart.barHeight = 2;
      } else {
        multibarChart.barHeight = 5;
        //sortedMultibarChart.barHeight = 5;
        multibarChart.yCatFont = new Font("Arial", Font.PLAIN, 12);
        sortedMultibarChart.yCatFont = new Font("Arial", Font.PLAIN, 12);
      }
      /*
       * Add the chart to the view
      */
      EventQueue.invokeLater(new Runnable() {
        public void run() {
          try {
            ChartViewFrameGUI frame = new ChartViewFrameGUI(multibarChart, sortedData, sortedMultibarChart);
          } catch (Exception e) {
              e.printStackTrace();
          }
        }
      });
    }
  }  // end HorizontalBarController

  public class MultiBarChartHPanel extends JPanel {
    ArrayList<MultiBar> multibars;
    Axis xAxis;
    String xAxisStr = "X Axis";
    String yAxisStr = "Y Axis";
    String title = " ";
    List<String> fieldNameList;
    MultiBarChartHPanel(ArrayList<MultiBar> multibars, Axis axis,
                                    List<String> fieldNameList) {
      setSize(5000, 5000);
      this.multibars = multibars;
      this.xAxis = axis;
      this.xAxisStr = axis.xLabel;
      this.yAxisStr = axis.yLabel;
      this.fieldNameList = fieldNameList;
    }
    MultiBarChartHPanel(ArrayList<MultiBar> multibars, Axis axis) {
      setSize(5000, 5000);
      this.multibars = multibars;
      this.xAxis = axis;
      this.xAxisStr = axis.xLabel;
      this.yAxisStr = axis.yLabel;
    }
    // offsets (padding of actual chart to its border)
    int leftOffset = 180;
    int topOffset = 30;
    int bottomOffset = 140;
    int rightOffset = 50;
    // height of X labels (must be significantly smaller than bottomOffset)
    int xLabelOffset = 5;
    // width of Y labels (must be significantly smaller than leftOffset)
    int yLabelOffset = 40;
    // tick heights
    int majorTickHeight = 10;
    int secTickHeight = 5;
    int minorTickHeight = 2;
    //int width = 500; // total width of the component
    int height = 730; // total height of the component
    Color textColor = Color.BLACK;
    Color backgroundColor = Color.WHITE;
    Font textFont = new Font("Arial", Font.BOLD, 20);
    Font yFont = new Font("Arial", Font.BOLD, 12);
    Font xFont = new Font("Arial", Font.BOLD, 12);
    Font titleFont = new Font("Arial", Font.BOLD, 18);
    Font yCatFont = new Font("Arial", Font.PLAIN, 5);
    Font xCatFont = new Font("Arial", Font.PLAIN, 12);
    int barHeight = 6;
    @Override
    protected void paintComponent(Graphics g) {
      int height = getSize().height;
      int width = getSize().width;
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                           RenderingHints.VALUE_ANTIALIAS_ON);
      g.drawRect(0, 0, width, height);
      g2d.setColor(backgroundColor);
      g.fillRect(0, 0, width, height);
      g2d.setColor(Color.BLACK);
      int heightChart = height - (topOffset + bottomOffset);
      int widthChart = width - (leftOffset + rightOffset);
      // left
      g.drawLine(leftOffset, topOffset, leftOffset, heightChart + topOffset);
      // bottom
      g.drawLine(leftOffset, heightChart + topOffset,leftOffset + widthChart, heightChart + topOffset);
      if (this.xAxis.primaryIncrements != 0)
        drawTick(widthChart, heightChart, this.xAxis.primaryIncrements, g,
                                       Color.BLACK, majorTickHeight);
      if (this.xAxis.secondaryIncrements != 0)
        drawTick(widthChart, heightChart, this.xAxis.secondaryIncrements,
                             g, Color.BLACK, secTickHeight);
      if (this.xAxis.tertiaryIncrements != 0)
        drawTick(widthChart, heightChart, this.xAxis.tertiaryIncrements, g,
                                     Color.BLACK, minorTickHeight);
      drawXLabels(widthChart, heightChart, this.xAxis.primaryIncrements, g,Color.BLACK);
      drawLabels(heightChart, widthChart, g, fieldNameList);
      drawMultiBars(heightChart, widthChart, g);
    }  // end paintComponent

    private void drawTick(int widthChart, int heightChart, int increment,
                                      Graphics g, Color c, int tickHeight) {
      int incrementNo = xAxis.maxValue / increment;
      double factor = ((double) widthChart / (double) xAxis.maxValue);
      double incrementInPixel = (double) (increment * factor);
      g.setColor(c);
      for (int i = 0; i < incrementNo; i++) {
        int fromLeft = widthChart + rightOffset   - (int) (i * incrementInPixel);
         g.drawLine(((leftOffset - rightOffset) + fromLeft), heightChart
              + topOffset, ((leftOffset - rightOffset) + fromLeft),
              heightChart + topOffset + tickHeight);
      }
    }

    private void drawXLabels(int widthChart, int heightChart, int increment,Graphics g, Color c) {
      int incrementNo = xAxis.maxValue / increment;
      double factor = ((double) widthChart / (double) xAxis.maxValue);
      int incrementInPixel = (int) (increment * factor);
      g.setColor(c);
      FontMetrics fm = getFontMetrics(xCatFont);
      for (int i = 0; i < incrementNo; i++) {
        int fromLeft = widthChart + rightOffset - (i * incrementInPixel);
        String xLabel = "" + ((incrementNo * increment) - (i * increment));
        int widthStr = fm.stringWidth(xLabel);
        int heightStr = fm.getHeight();
        g.setFont(xCatFont);
        g.drawString(xLabel, (leftOffset - rightOffset) - widthStr / 2
                          + fromLeft, topOffset + heightChart + (heightStr));
      }
    }

    private void drawLabels(int heightChart, int widthChart, Graphics g,List<String> fieldNameList) {
      Graphics2D g2d = (Graphics2D) g;
      AffineTransform oldTransform = g2d.getTransform();
      FontMetrics fmY = getFontMetrics(yFont);
      int yAxisStringWidth = fmY.stringWidth(yAxisStr);
      int yAxisStringHeight = fmY.getHeight();
      FontMetrics fmX = getFontMetrics(xFont);
      int xAxisStringWidth = fmX.stringWidth(xAxisStr);
      FontMetrics fmT = getFontMetrics(titleFont);
      int titleStringWidth = fmT.stringWidth(title);
      int titleStringHeight = fmT.getHeight();
      g2d.setColor(Color.BLACK);
      // draw tick
      g2d.rotate(Math.toRadians(270)); // rotates to above out of screen.
      int translateDown = -leftOffset - (topOffset + heightChart / 2 + yAxisStringWidth / 2);
      // starts off being "topOffset" off, so subtract that first
      int translateLeft = -topOffset + (leftOffset - yLabelOffset) / 2 + yAxisStringHeight / 2;
      // pull down, which is basically the left offset, topOffset, then middle
      // it by usin chart height and using text height.
      g2d.translate(translateDown, translateLeft);
      g2d.setFont(yFont);
      g2d.drawString(yAxisStr, leftOffset, topOffset);
      // reset
      g2d.setTransform(oldTransform);
      int xAxesLabelHeight = bottomOffset - xLabelOffset;
      // x label
      List<Color> colorTemp = new ArrayList<Color>();
      if (multibars.size() > 1) {
        for (MultiBar multi : multibars) {
          for (Bar bar : multi.bar) {
             colorTemp.add(bar.color);
          }
        }
        g2d.setFont(xFont);
        g2d.setColor(colorTemp.get(0));
        g2d.drawString(fieldNameList.get(0), widthChart / 2 + 2 + 0
                * leftOffset - xAxisStringWidth / 2, topOffset
                + heightChart + xLabelOffset + xAxesLabelHeight / 2);
        g2d.setColor(colorTemp.get(colorTemp.size() - 1));
        g2d.drawString(fieldNameList.get(1), widthChart / 2 + leftOffset
                  - xAxisStringWidth / 2, topOffset + heightChart
                  + xLabelOffset + xAxesLabelHeight / 2);
      } else {
        g2d.setColor(Color.BLACK);
        g2d.setFont(xFont);
        g2d.drawString(xAxisStr, widthChart / 2 + leftOffset
                      - xAxisStringWidth / 2, topOffset + heightChart
                      + xLabelOffset + xAxesLabelHeight / 2);
      }
      g2d.setColor(Color.BLACK);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                        RenderingHints.VALUE_ANTIALIAS_ON);
      // title
      g2d.setFont(titleFont);
      int titleX = (leftOffset + rightOffset + widthChart) / 2 - titleStringWidth / 2;
      int titleY = topOffset / 2 + titleStringHeight / 2;
      g2d.drawString(title, titleX, titleY);
    }

    private void drawMultiBars(int heightChart, int widthChart, Graphics g) {
      int i = 0;
      int barSet = multibars.size();
      for (MultiBar multibar : multibars) {
        drawBars(multibar.bar, heightChart, widthChart, g, barSet, i);
        drawBarsName(multibar.bar, heightChart, widthChart, g);
        i++;
      }
    }
    private void drawBars(ArrayList<Bar> bars, int heightChart, int widthChart,
                            Graphics g, int barSet, int index) {
      int i = 0;
      int barNumber = bars.size();
      int pointDistance = (int) (heightChart / (barNumber + 1));
      for (Bar bar : bars) {
        i++;
        final double eachFinalData = bar.value/100;
        int scaledBarWidth = (int) ((widthChart*(bar.value/100)*1) / xAxis.maxValue);
        g.setColor(bar.color);
        final Shape rect = (Shape) new Rectangle2D.Double(leftOffset, topOffset + (i * pointDistance) + barSet
                 * barHeight + index * barHeight - (barHeight / 2),
        scaledBarWidth, barHeight);
        ((Graphics2D) g).fill(rect);
        g.setFont(yCatFont);
        g.setColor(Color.BLACK);
        addMouseMotionListener(new MouseMotionListener() {

          public void mouseMoved(MouseEvent e) {
            if (rect.contains(e.getPoint())){
              setToolTipText(" " + eachFinalData);
            }
            ToolTipManager.sharedInstance().mouseMoved(e);
            ToolTipManager.sharedInstance().getReshowDelay();
            ToolTipManager.sharedInstance().setReshowDelay(10);
          }
          public void mouseDragged(MouseEvent e) {
          }
        });
      }
    }

    private void drawBarsName(ArrayList<Bar> bars, int heightChart,
                                     int widthChart, Graphics g) {
      int i = 0;
      int barNumber = bars.size();
      int pointDistance = (int) (heightChart / (barNumber + 1));
      for (Bar bar : bars) {
        i++;
        g.setColor(bar.color);
        FontMetrics fm = getFontMetrics(yCatFont);
        int widthStr = fm.stringWidth(bar.name);
        int heightStr = fm.getHeight();
        g.setFont(yCatFont);
        g.setColor(Color.BLACK);
        int xPosition = leftOffset - (widthStr);
        int yPosition = topOffset + (i * pointDistance) + barHeight
            + barHeight * 2 - (barHeight / 2) + yLabelOffset / 6
            - heightStr / 6;
        // draw tick
        g.drawString(bar.name, xPosition, yPosition);
      }
    } // end drawBarsName
  }  //end multiBarChartHPanel

  /*
   * Vertical Bar Chart Rendering
  */
  public static class BarChartVPanel extends JPanel {
    ArrayList<Bar> bars;
    Axis yAxis;
    String xAxisStr = "X Axis";
    String yAxisStr = "Y Axis";
    String title = " ";
    public BarChartVPanel(ArrayList<Bar> bars, ChartController.Axis axis) {
      this.bars = bars;
      this.yAxis = axis;
      this.xAxisStr = axis.xLabel;
      this.yAxisStr = axis.yLabel;
    }
    // offsets (padding of actual chart to its border)
    int leftOffset = 90;
    int topOffset = 120;
    int bottomOffset = 100;
    int rightOffset = 15;
    int heightFactor = 0;
    // height of X labels (must be significantly smaller than bottomOffset)
    int xLabelOffset = 40;
    // width of Y labels (must be significantly smaller than leftOffset)
    int yLabelOffset = 40;
    // tick widths
    int majorTickWidth = 10;
    int secTickWidth = 5;
    int minorTickWidth = 2;
    int width = 900; // total width of the component
    // int height = 430; //total height of the component
    Color textColor = Color.BLACK;
    Color backgroundColor = Color.WHITE;
    Font textFont = new Font("Arial", Font.BOLD, 20);
    Font yFont = new Font("Arial", Font.PLAIN, 12);
    Font xFont = new Font("Arial", Font.BOLD, 12);
    Font titleFont = new Font("Arial", Font.BOLD, 18);
    Font yCatFont = new Font("Arial", Font.BOLD, 12);
    Font xCatFont = new Font("Arial", Font.BOLD, 12);
    public int barWidth = 10;
    @Override
    protected void paintComponent(Graphics g) {
      int height = getSize().height;
      int width = getSize().width;
      heightFactor = getSize().height;
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                        RenderingHints.VALUE_ANTIALIAS_ON);
      g.drawRect(0, 0, width, height);
      g2d.setColor(backgroundColor);
      g.fillRect(0, 0, width, height);
      g2d.setColor(Color.BLACK);
      int heightChart = height - (topOffset + bottomOffset);
      int widthChart = width - (leftOffset + rightOffset);
      // left
      g.drawLine(leftOffset, topOffset, leftOffset, heightChart + topOffset);
      // bottom
      g.drawLine(leftOffset, heightChart + topOffset,leftOffset + widthChart, heightChart + topOffset);
      if (this.yAxis.primaryIncrements != 0)
                  drawTick(heightChart, this.yAxis.primaryIncrements, g,Color.BLACK,majorTickWidth);
      if (this.yAxis.secondaryIncrements != 0)
                  drawTick(heightChart, this.yAxis.secondaryIncrements, g,Color.BLACK, secTickWidth);
      if (this.yAxis.tertiaryIncrements != 0)
                  drawTick(heightChart, this.yAxis.tertiaryIncrements, g,Color.BLACK, minorTickWidth);
      drawYLabels(heightChart, this.yAxis.primaryIncrements, g, Color.BLACK);
      drawBars(heightChart, widthChart, g);
      drawLabels(heightChart, widthChart, g);
    } // end paintComponent

    private void drawTick(int heightChart, int increment, Graphics g, Color c,int tickWidth) {
      int incrementNo = yAxis.maxValue / increment;
      double factor = ((double) heightChart / (double) yAxis.maxValue);
      double incrementInPixel = (double) (increment * factor);
      g.setColor(c);
      for (int i = 0; i < incrementNo; i++) {
        int fromTop = heightChart + topOffset - (int) (i * incrementInPixel);
        g.drawLine(leftOffset, fromTop, leftOffset + tickWidth, fromTop);
      }
    }

    private void drawYLabels(int heightChart, int increment, Graphics g, Color c) {
      int incrementNo = yAxis.maxValue / increment;
      double factor = ((double) heightChart / (double) yAxis.maxValue);
      int incrementInPixel = (int) (increment * factor);
      g.setColor(c);
      FontMetrics fm = getFontMetrics(yCatFont);
      for (int i = 0; i < incrementNo; i++) {
        int fromTop = heightChart + topOffset - (i * incrementInPixel);
        String yLabel = "" + (i * increment);
        int widthStr = fm.stringWidth(yLabel);
        int heightStr = fm.getHeight();
        g.setFont(yCatFont);
        g.drawString(yLabel, (leftOffset - yLabelOffset)
              + (yLabelOffset / 2 - widthStr / 2), fromTop + (heightStr / 2));
      }
    }

    private void drawBars(int heightChart, int widthChart, Graphics g) {
      int i = 0;
      int barNumber = bars.size();
      int pointDistance = (int) (widthChart / (barNumber + 1));
      for (Bar bar : bars) {
        i++;
        final double eachFinalData = bar.value/100;
        int scaledBarHeight = (int) ((heightChart*(bar.value/100)*1) / yAxis.maxValue);
        int j = topOffset + heightChart - scaledBarHeight;
        g.setColor(bar.color);
        final Shape rect = (Shape) new Rectangle2D.Double(leftOffset + (i * pointDistance) - (barWidth / 2), j,
                      barWidth, scaledBarHeight);
        ((Graphics2D) g).fill(rect);
        // draw tick
        g.drawLine(leftOffset + (i * pointDistance), topOffset
            + heightChart, leftOffset + (i * pointDistance), topOffset + heightChart + 2);
        FontMetrics fm = getFontMetrics(xCatFont);
        int heightStr = fm.getHeight();
        g.setFont(xCatFont);
        g.setColor(Color.BLACK);
        int yPosition = topOffset + heightChart + xLabelOffset - heightStr / 2;
        // draw tick
        g.drawString(i + "", leftOffset + (i * pointDistance), yPosition);
        addMouseMotionListener(new MouseMotionListener() {
          public void mouseMoved(MouseEvent e) {
            if (rect.contains(e.getPoint())){
              setToolTipText(" " + eachFinalData);
            }
            ToolTipManager.sharedInstance().mouseMoved(e);
            ToolTipManager.sharedInstance().getReshowDelay();
            ToolTipManager.sharedInstance().setReshowDelay(10);
          }
          public void mouseDragged(MouseEvent e) {
          }
        });
      }
    } // end drawBars

    private void drawLabels(int heightChart, int widthChart, Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      AffineTransform oldTransform = g2d.getTransform();
      FontMetrics fmY = getFontMetrics(yFont);
      int yAxisStringWidth = fmY.stringWidth(yAxisStr);
      int yAxisStringHeight = fmY.getHeight();
      FontMetrics fmX = getFontMetrics(xFont);
      int xAxisStringWidth = fmX.stringWidth(yAxisStr);
      FontMetrics fmT = getFontMetrics(titleFont);
      int titleStringWidth = fmT.stringWidth(title);
      int titleStringHeight = fmT.getHeight();
      g2d.setColor(Color.BLACK);
      // draw tick
      g2d.rotate(Math.toRadians(270)); // rotates to above out of screen.
      int translateDown = -leftOffset - (topOffset + heightChart / 2 + yAxisStringWidth / 2);
      // starts off being "topOffset" off, so subtract that first
      int translateLeft = -topOffset + (leftOffset - yLabelOffset) / 2
               + yAxisStringHeight / 2;
      // pull down, which is basically the left offset, topOffset, then middle
      // it by usin chart height and using text height.
      g2d.translate(translateDown, translateLeft);
      g2d.setFont(yFont);
      g2d.drawString(yAxisStr, leftOffset, topOffset);
      // reset
      g2d.setTransform(oldTransform);
      int xAxesLabelHeight = bottomOffset - xLabelOffset;
      // x label
      g2d.setFont(xFont);
      g2d.drawString(xAxisStr, widthChart / 2 + leftOffset - xAxisStringWidth
               / 2, topOffset + heightChart + xLabelOffset + xAxesLabelHeight / 2);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                             RenderingHints.VALUE_ANTIALIAS_ON);
      // title
      g2d.setFont(titleFont);
      int titleX = (leftOffset + rightOffset + widthChart) / 2 - titleStringWidth / 2;
      int titleY = topOffset / 2 + titleStringHeight / 2;
      g2d.drawString(title, titleX, titleY);
    } // end drawLabels
  }  // end BarChartVPanel

  public static class MultiBarChartVPanel extends JPanel {
    ArrayList<MultiBar> multibars;
    Axis yAxis;
    String xAxisStr = "X Axis";
    String yAxisStr = "Y Axis";
    String title = " ";
    List<String> fieldNameList;
    public MultiBarChartVPanel(ArrayList<MultiBar> multibars, Axis axis,
                                     List<String> fieldNameList) {
      this.multibars = multibars;
      this.yAxis = axis;
      this.xAxisStr = axis.xLabel;
      this.yAxisStr = axis.yLabel;
      this.fieldNameList = fieldNameList;
    }
    MultiBarChartVPanel(ArrayList<MultiBar> multibars, Axis axis) {
      this.multibars = multibars;
      this.yAxis = axis;
      this.xAxisStr = axis.xLabel;
      this.yAxisStr = axis.yLabel;
    }
    // offsets (padding of actual chart to its border)
    int leftOffset = 80;
    int topOffset = 100;
    int bottomOffset = 100;
    int rightOffset = 150;
    // height of X labels (must be significantly smaller than bottomOffset)
    int xLabelOffset = 40;
    // width of Y labels (must be significantly smaller than leftOffset)
    int yLabelOffset = 40;
    // tick widths
    int majorTickWidth = 10;
    int secTickWidth = 5;
    int minorTickWidth = 2;
    public int width = 400; // total width of the component
    // int height = 430; // total height of the component
    Color textColor = Color.BLACK;
    Color backgroundColor = Color.WHITE;
    Font textFont = new Font("Arial", Font.BOLD, 20);
    Font yFont = new Font("Arial", Font.PLAIN, 12);
    Font xFont = new Font("Arial", Font.BOLD, 12);
    Font titleFont = new Font("Arial", Font.BOLD, 18);
    Font yCatFont = new Font("Arial", Font.BOLD, 12);
    public Font xCatFont = new Font("Arial", Font.BOLD, 12);
    public int barWidth = 5;
    @Override
    protected void paintComponent(Graphics g) {
      int height = getSize().height;
      int width = getSize().width;
      Graphics2D g2d = (Graphics2D) g;
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                    RenderingHints.VALUE_ANTIALIAS_ON);
      g.drawRect(0, 0, width, height);
      g2d.setColor(backgroundColor);
      g.fillRect(0, 0, width, height);
      g2d.setColor(Color.BLACK);
      int heightChart = height - (topOffset + bottomOffset);
      int widthChart = width - (leftOffset + rightOffset);
      // left
      g.drawLine(leftOffset, topOffset, leftOffset, heightChart + topOffset);
      // bottom
      g.drawLine(leftOffset, heightChart + topOffset,
                 leftOffset + widthChart, heightChart + topOffset);
      if (this.yAxis.primaryIncrements != 0)
        drawTick(heightChart, this.yAxis.primaryIncrements, g, Color.BLACK,majorTickWidth);
      if (this.yAxis.secondaryIncrements != 0)
        drawTick(heightChart, this.yAxis.secondaryIncrements, g,Color.BLACK, secTickWidth);
      if (this.yAxis.tertiaryIncrements != 0)
        drawTick(heightChart, this.yAxis.tertiaryIncrements, g,Color.BLACK, minorTickWidth);
      drawYLabels(heightChart, this.yAxis.primaryIncrements, g, Color.BLACK);
      drawLabels(heightChart, widthChart, g, fieldNameList);
      drawMultiBars(heightChart, widthChart, g);
    }  //end paintComponent

    private void drawTick(int heightChart, int increment, Graphics g, Color c,int tickWidth) {
      int incrementNo = yAxis.maxValue / increment;
      double factor = ((double) heightChart / (double) yAxis.maxValue);
      double incrementInPixel = (double) (increment * factor);
      g.setColor(c);
      for (int i = 0; i < incrementNo; i++) {
         int fromTop = heightChart + topOffset - (int) (i * incrementInPixel);
         g.drawLine(leftOffset, fromTop, leftOffset + tickWidth, fromTop);
      }
    }

    private void drawYLabels(int heightChart, int increment, Graphics g, Color c) {
      int incrementNo = yAxis.maxValue / increment;
      double factor = ((double) heightChart / (double) yAxis.maxValue);
      int incrementInPixel = (int) (increment * factor);
      g.setColor(c);
      FontMetrics fm = getFontMetrics(yCatFont);
      for (int i = 0; i < incrementNo; i++) {
        int fromTop = heightChart + topOffset - (i * incrementInPixel);
        String yLabel = "" + (i * increment);
        int widthStr = fm.stringWidth(yLabel);
        int heightStr = fm.getHeight();
        g.setFont(yCatFont);
        g.drawString(yLabel, (leftOffset - yLabelOffset)
            + (yLabelOffset / 2 - widthStr / 2), fromTop + (heightStr / 2));
      }
    }

    private void drawLabels(int heightChart, int widthChart, Graphics g,
                                            List<String> fieldNameList) {
      Graphics2D g2d = (Graphics2D) g;
      AffineTransform oldTransform = g2d.getTransform();
      FontMetrics fmY = getFontMetrics(yFont);
      int yAxisStringWidth = fmY.stringWidth(yAxisStr);
      int yAxisStringHeight = fmY.getHeight();
      FontMetrics fmX = getFontMetrics(xFont);
      int xAxisStringWidth = fmX.stringWidth(yAxisStr);
      FontMetrics fmT = getFontMetrics(titleFont);
      int titleStringWidth = fmT.stringWidth(title);
      int titleStringHeight = fmT.getHeight();
      g2d.setColor(Color.BLACK);
      // draw tick
      g2d.rotate(Math.toRadians(270)); // rotates to above out of screen.
      int translateDown = -leftOffset - (topOffset + heightChart / 2 + yAxisStringWidth / 2);
      // starts off being "topOffset" off, so subtract that first
      int translateLeft = -topOffset + (leftOffset - yLabelOffset) / 2 + yAxisStringHeight / 2;
      // pull down, which is basically the left offset, topOffset, then middle
      // it by using chart height and using text height.
      g2d.translate(translateDown, translateLeft);
      g2d.setFont(yFont);
      g2d.drawString(yAxisStr, leftOffset, topOffset);
      // reset
      g2d.setTransform(oldTransform);
      int xAxesLabelHeight = bottomOffset - xLabelOffset;
      // x label
      List<Color> colorTemp = new ArrayList<Color>();
      if (multibars.size() > 1) {
        for (MultiBar multi : multibars) {
          for (Bar bar : multi.bar) {
            colorTemp.add(bar.color);
          }
        }
        g2d.setFont(xFont);
        g2d.setColor(colorTemp.get(0));
        g2d.drawString(fieldNameList.get(0), widthChart / 2 + 2 + 0
                 * leftOffset - xAxisStringWidth / 2, topOffset
                 + heightChart + xLabelOffset + xAxesLabelHeight / 2);
        g2d.setColor(colorTemp.get(colorTemp.size() - 1));
        g2d.drawString(fieldNameList.get(1), widthChart / 2 + leftOffset
                 - xAxisStringWidth / 2, topOffset + heightChart
                 + xLabelOffset + xAxesLabelHeight / 2);
      } else {
        g2d.setColor(Color.BLACK);
        g2d.setFont(xFont);
        g2d.drawString(xAxisStr, widthChart / 2 + leftOffset
                 - xAxisStringWidth / 2, topOffset + heightChart
                 + xLabelOffset + xAxesLabelHeight / 2);
      }
      g2d.setColor(Color.BLACK);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, // Anti-alias!
                                RenderingHints.VALUE_ANTIALIAS_ON);
      // title
      g2d.setFont(titleFont);
      int titleX = (leftOffset + rightOffset + widthChart) / 2
              - titleStringWidth / 2;
      int titleY = topOffset / 2 + titleStringHeight / 2;
      g2d.drawString(title, titleX, titleY);
    }

    private void drawMultiBars(int heightChart, int widthChart, Graphics g) {
      int i = 0;
      int barSet = multibars.size();
      for (MultiBar multibar : multibars) {
        drawBars(multibar.bar, heightChart, widthChart, g, barSet, i);
        drawBarsName(multibar.bar, heightChart, widthChart, g);
        i++;
      }
    }

    private void drawBars(ArrayList<Bar> bars, int heightChart, int widthChart,
                                         Graphics g, int barSet, int index) {
      int i = 0;
      int barNumber = bars.size();
      int pointDistance = (int) (widthChart / (barNumber + 1));
      for (Bar bar : bars) {
        i++;
        final double eachFinalData = bar.value/100;
        int scaledBarHeight = (int) ((heightChart*(bar.value/100)*1) / yAxis.maxValue);
        int j = topOffset + heightChart - scaledBarHeight;
        g.setColor(bar.color);
        final Shape rect = (Shape) new Rectangle2D.Double(leftOffset + (i * pointDistance) + barSet / 4 * barWidth
                                   + index * barWidth, j, barWidth, scaledBarHeight);
        ((Graphics2D) g).fill(rect);
        // draw tick
        g.drawLine(leftOffset + (i * pointDistance), topOffset
              + heightChart, leftOffset + (i * pointDistance), topOffset + heightChart + 2);
        g.setFont(xCatFont);
        g.setColor(Color.BLACK);
        addMouseMotionListener(new MouseMotionListener() {
          public void mouseMoved(MouseEvent e) {
            if (rect.contains(e.getPoint())){
              setToolTipText(" " + eachFinalData);
            }
            ToolTipManager.sharedInstance().mouseMoved(e);
            ToolTipManager.sharedInstance().getReshowDelay();
            ToolTipManager.sharedInstance().setReshowDelay(10);
          }
          public void mouseDragged(MouseEvent e) {
          }
        });
      }
    }

    private void drawBarsName(ArrayList<Bar> bars, int heightChart,
      int widthChart, Graphics g) {
      int i = 0;
      int barNumber = bars.size();
      int pointDistance = (int) (widthChart / (barNumber + 1));
      for (Bar bar : bars) {
        i++;
        g.setColor(bar.color);
        FontMetrics fm = getFontMetrics(xCatFont);
        int heightStr = fm.getHeight();
        g.setFont(xCatFont);
        g.setColor(Color.BLACK);
        int yPosition = topOffset + heightChart + xLabelOffset - heightStr / 2;
        // draw tick
        g.drawString(i + "", leftOffset + (i * pointDistance), yPosition);
      }
    }
  }  // end multiBarChartVPanel

  
}
