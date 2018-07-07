package com.sdsu.edu.main;

import static com.sdsu.edu.main.constant.GUILabelConstants.BAR_CHART_TOOL_TIP;
import static com.sdsu.edu.main.constant.GUILabelConstants.CHARTING_LABEL;
import static com.sdsu.edu.main.constant.GUILabelConstants.LINEAR_REGRESSION_TOOL_TIP;
import static com.sdsu.edu.main.constant.GUILabelConstants.PIE_CHART_TOOL_TIP;
import static com.sdsu.edu.main.constant.GUILabelConstants.POLYNOMIAL_REGRESSION_TOOL_TIP;
import static com.sdsu.edu.main.constant.GUILabelConstants.POWER_REGRESSION_TOOL_TIP;
import static com.sdsu.edu.main.constant.GUILabelConstants.THREE_DIM_TOOL_TIP;

import com.sdsu.edu.main.controller.db.DbfReadController;
import com.sdsu.edu.main.gui.BarPanelGUI;
import com.sdsu.edu.main.gui.LinearRegressionGUI;
import com.sdsu.edu.main.gui.PiePanelGUI;
import com.sdsu.edu.main.gui.PolynomialPanelGUI;
import com.sdsu.edu.main.gui.PowerRegressionGUI;
import com.sdsu.edu.main.gui.ThreeDimensionalPanelGUI;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * Charting Tool Facility: Thesis
 */
class ChartingToolStart {

  /*
   * The Chart GUI for selecting Pie or Bar, and respective choices to chart
   */
  public class ChartTypeFrameGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JFileChooser jfc;
    JPanel firstPanel = new JPanel();
    JPanel secondPanel = new JPanel();
    JPanel borderPanel2 = new JPanel(false);

    JButton piebtn = new JButton("PIE", new ImageIcon("./src/main/resources/data/piechartbtn.jpg"));
    JButton barbtn = new JButton("BAR", new ImageIcon("./src/main/resources/data/barchartbtn.jpg"));

    JButton scatterbtn = new JButton("SCATTER", new ImageIcon("./src/main/resources/data/linear_reg.png"));
    JButton nonLinearBtn = new JButton("NLNR", new ImageIcon("./src/main/resources/data/nonlinear.png"));
    JButton polyBtn = new JButton("POLY", new ImageIcon("./src/main/resources/data/poly_reg.png"));
    JButton threeDBtn = new JButton("3D", new ImageIcon("./src/main/resources/data/three_dim.png"));
    List<String> numericList;
    List<String> charList;
    LinearRegressionGUI linearRegressionGUI;
    BarPanelGUI barPanelGUI;
    PiePanelGUI piePanelGUI;
    PowerRegressionGUI powerRegressionGUI;
    PolynomialPanelGUI polynomialPanelGUI;
    ThreeDimensionalPanelGUI threeDimensionalPanelGUI;
    private List<JPanel> panelList = new ArrayList<>();

    public ChartTypeFrameGUI() {
      setTitle(CHARTING_LABEL);
      setSize(800, 600); // default size is 0,0
      setLocation(50, 200); // default is 0,0 (top left corner)
      setLayout(new GridLayout(3, 2));

      // Browse for the DBF File using jFileChooser
      jfc = new JFileChooser();
      FileNameExtensionFilter filter = new FileNameExtensionFilter("*.dbf", "dbf");
      jfc.setFileFilter(filter);
      int openDialog = 0;
      do {
        openDialog = jfc.showOpenDialog(this);
      } while (openDialog == JFileChooser.CANCEL_OPTION || null == jfc.getSelectedFile());

      File file = jfc.getSelectedFile();

      DbfReadController dbfread = DbfReadController.getInstance();
      numericList = dbfread.readnumericdbf(file);
      charList = dbfread.readchardbf(file);

      // Window Listeners
      addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        } // windowClosing
      });
      Container contentPane = getContentPane();
      JPanel borderPanel = new JPanel(false);

      Border paneEdge = BorderFactory.createEmptyBorder(10, 10, 10, 12);
      Border blackline = BorderFactory.createLineBorder(Color.black);
      borderPanel.setBorder(paneEdge);
      borderPanel.setLayout(new BoxLayout(borderPanel,
          BoxLayout.X_AXIS));

      firstPanel.setBorder(blackline);

      borderPanel.add(Box.createRigidArea(new Dimension(0, 10)));
      borderPanel.add(firstPanel);
      borderPanel.setVisible(true);

      contentPane.add(borderPanel);
      piebtn.setPreferredSize(new Dimension(100, 100));
      piebtn.setFocusable(true);
      piebtn.setToolTipText(PIE_CHART_TOOL_TIP);
      firstPanel.add(piebtn);
      piebtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearPanelList();
          addCompForTitledBorder(PIE_CHART_TOOL_TIP);
          piePanelGUI = new PiePanelGUI(numericList, charList);
          panelList.add(piePanelGUI);
          secondPanel.add(piePanelGUI);
          secondPanel.setVisible(true);
        }
      });
      barbtn.setPreferredSize(new Dimension(100, 100));
      barbtn.setToolTipText(BAR_CHART_TOOL_TIP);
      firstPanel.add(barbtn);
      barbtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearPanelList();
          addCompForTitledBorder(BAR_CHART_TOOL_TIP);
          barPanelGUI = new BarPanelGUI(numericList, charList);
          panelList.add(barPanelGUI);
          secondPanel.add(barPanelGUI);
          secondPanel.setVisible(true);
        }
      });

      scatterbtn.setPreferredSize(new Dimension(100, 100));
      scatterbtn.setToolTipText(LINEAR_REGRESSION_TOOL_TIP);
      firstPanel.add(scatterbtn);
      scatterbtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearPanelList();
          addCompForTitledBorder(LINEAR_REGRESSION_TOOL_TIP);
          linearRegressionGUI = new LinearRegressionGUI(numericList, charList);
          panelList.add(linearRegressionGUI);
          secondPanel.add(linearRegressionGUI);
          secondPanel.setVisible(true);
        }
      });

      nonLinearBtn.setPreferredSize(new Dimension(100, 100));
      nonLinearBtn.setToolTipText(POWER_REGRESSION_TOOL_TIP);
      firstPanel.add(nonLinearBtn);
      nonLinearBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearPanelList();
          addCompForTitledBorder(POWER_REGRESSION_TOOL_TIP);
          powerRegressionGUI = new PowerRegressionGUI(numericList, charList);
          panelList.add(powerRegressionGUI);
          secondPanel.add(powerRegressionGUI);
          secondPanel.setVisible(true);
        }
      });

      polyBtn.setPreferredSize(new Dimension(100, 100));
      polyBtn.setToolTipText(POLYNOMIAL_REGRESSION_TOOL_TIP);
      firstPanel.add(polyBtn);
      polyBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearPanelList();
          addCompForTitledBorder(POLYNOMIAL_REGRESSION_TOOL_TIP);
          polynomialPanelGUI = new PolynomialPanelGUI(numericList, charList);
          panelList.add(polynomialPanelGUI);
          secondPanel.add(polynomialPanelGUI);
          secondPanel.setVisible(true);
        }
      });

      threeDBtn.setPreferredSize(new Dimension(100, 100));
      threeDBtn.setToolTipText(THREE_DIM_TOOL_TIP);
      firstPanel.add(threeDBtn);
      threeDBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          clearPanelList();
          addCompForTitledBorder(THREE_DIM_TOOL_TIP);
          threeDimensionalPanelGUI = new ThreeDimensionalPanelGUI(numericList, charList);
          panelList.add(threeDimensionalPanelGUI);
          secondPanel.add(threeDimensionalPanelGUI);
          secondPanel.setVisible(true);
        }
      });
      setVisible(true);
    } // End Constructor

    private void clearPanelList() {
      for (JPanel panel : panelList) {
          secondPanel.remove(panel);
          secondPanel.setVisible(false);
          panel.setVisible(false);
      }
    }

    private void addCompForTitledBorder(String title) {
      Container contentPane = getContentPane();
      Border blackline = BorderFactory.createLineBorder(Color.black);

      borderPanel2.add(Box.createRigidArea(new Dimension(0, 10)));
      borderPanel2.add(secondPanel);
      borderPanel2.setVisible(true);

      Border paneEdge2 = BorderFactory.createEmptyBorder(30, 10, 10, 12);
      TitledBorder titled = BorderFactory.createTitledBorder(
          blackline, title);
      titled.setTitleJustification(TitledBorder.LEFT);
      titled.setTitlePosition(TitledBorder.DEFAULT_POSITION);

      borderPanel2.setBorder(paneEdge2);
      borderPanel2.setLayout(new BoxLayout(borderPanel2,
          BoxLayout.X_AXIS));

      secondPanel.setBorder(titled);
      contentPane.add(borderPanel2);
    }

  }
}
