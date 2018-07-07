package com.sdsu.edu.main;


import static com.sdsu.edu.main.constant.GUILabelConstants.CHARTING_TOOL_TIP;

import com.esri.mo2.cs.geom.Envelope;
import com.esri.mo2.data.feat.Feature;
import com.esri.mo2.data.feat.SelectionSet;
import com.esri.mo2.map.dpy.FeatureLayer;
import com.esri.mo2.map.dpy.Layerset;
import com.esri.mo2.map.draw.AoLineStyle;
import com.esri.mo2.ui.bean.AcetateLayer;
import com.esri.mo2.ui.bean.Identify;
import com.esri.mo2.ui.bean.Layer;
import com.esri.mo2.ui.bean.Legend;
import com.esri.mo2.ui.bean.Map;
import com.esri.mo2.ui.bean.PickEvent;
import com.esri.mo2.ui.bean.PickListener;
import com.esri.mo2.ui.bean.Toc;
import com.esri.mo2.ui.bean.TocAdapter;
import com.esri.mo2.ui.bean.TocEvent;
import com.esri.mo2.ui.dlg.AboutBox;
import com.esri.mo2.ui.ren.LayerProperties;
import com.esri.mo2.ui.tb.SelectionToolBar;
import com.esri.mo2.ui.tb.ZoomPanToolBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class ChartingTool extends JFrame {
    static Map map = new Map();
    static boolean fullMap = true;
    static boolean helpToolOn;
    Legend legend;
    Legend legend2;
    Layer layer = new Layer();
    Layer layer2 = new Layer();
    Layer layer3 = null;
    static AcetateLayer acetLayer;
    static com.esri.mo2.map.dpy.Layer layer4;
    com.esri.mo2.map.dpy.Layer activeLayer;
    int activeLayerIndex;
    com.esri.mo2.cs.geom.Point initPoint, endPoint;
    double distance;
    JMenuBar mbar = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenu theme = new JMenu("Theme");
    JMenu layercontrol = new JMenu("LayerControl");
    JMenu help = new JMenu("Help");
    JMenuItem attribitem = new JMenuItem("open attribute table", new ImageIcon( "./src/main/resources/data/tableview.gif"));
    JMenuItem createlayeritem = new JMenuItem("create layer from selection", new ImageIcon("./src/main/resources/data/Icon0915b.jpg"));
    static JMenuItem promoteitem = new JMenuItem("promote selected layer",  new ImageIcon("./src/main/resources/data/promote.jpg"));
    JMenuItem demoteitem = new JMenuItem("demote selected layer",   new ImageIcon("./src/main/resources/data/demote.jpg"));
    JMenuItem printitem = new JMenuItem("print", new ImageIcon( "./src/main/resources/data/print.gif"));
    JMenuItem addlyritem = new JMenuItem("add layer", new ImageIcon( "./src/main/resources/data/addtheme.gif"));
    JMenuItem remlyritem = new JMenuItem("remove layer", new ImageIcon( "./src/main/resources/data/delete.gif"));
    JMenuItem propsitem = new JMenuItem("Legend Editor", new ImageIcon( "./src/main/resources/data/properties.gif"));
    JMenu helptopics = new JMenu("Help Topics");
    JMenuItem tocitem = new JMenuItem("Table of Contents", new ImageIcon( "./src/main/resources/data/helptopic.gif"));
    JMenuItem legenditem = new JMenuItem("Legend Editor", new ImageIcon( "./src/main/resources/data/helptopic.gif"));
    JMenuItem layercontrolitem = new JMenuItem("Layer Control", new ImageIcon( "./src/main/resources/data/helptopic.gif"));
    JMenuItem helptoolitem = new JMenuItem("Help Tool", new ImageIcon(  "./src/main/resources/data/help2.gif"));
    JMenuItem contactitem = new JMenuItem("Contact us");
    JMenuItem aboutitem = new JMenuItem("About MOJO...");
    Toc toc = new Toc();
    String USAShp = "./src/main/resources/data/states.shp";

    String datapathname = "";
    String legendname = "";
    ZoomPanToolBar zptb = new ZoomPanToolBar();
    static SelectionToolBar stb = new SelectionToolBar();
    JToolBar jtb = new JToolBar();
    ComponentListener complistener;
    JLabel statusLabel = new JLabel("status bar    LOC");
    static JLabel milesLabel = new JLabel("   DIST:  0 mi    ");
    static JLabel kmLabel = new JLabel("  0 km    ");
    java.text.DecimalFormat df = new java.text.DecimalFormat("0.000");
    JPanel myjp = new JPanel();
    JPanel myjp2 = new JPanel();
    JButton prtjb = new JButton(new ImageIcon("data\\print.gif"));
    JButton addlyrjb = new JButton(new ImageIcon("./src/main/resources/data/addtheme.gif"));
    JButton ptrjb = new JButton(new ImageIcon("./src/main/resources/data/pointer.gif"));
    JButton distjb = new JButton(new ImageIcon("./src/main/resources/data/measure_1.gif"));
    JButton XYjb = new JButton("XY");
    JButton helpjb = new JButton(new ImageIcon("./src/main/resources/data/help2.gif"));
    JButton hotjb = new JButton(new ImageIcon("./src/main/resources/data/hotlink.gif"));
    //add the button to the charting facility
    JButton chartjb = new JButton(new ImageIcon("./src/main/resources/data/chart16.gif"));

    Arrow arrow = new Arrow();
    static HelpTool helpTool = new HelpTool();
    ActionListener lis;
    ActionListener layerlis;
    ActionListener layercontrollis;
    ActionListener helplis;
    TocAdapter mytocadapter;
    Toolkit tk = Toolkit.getDefaultToolkit();
    Image bolt = tk.getImage("./src/main/resources/data/hotlink_32x32-32.gif");
    java.awt.Cursor boltCursor = tk.createCustomCursor(bolt, new java.awt.Point(6, 30), "bolt");
    MyPickAdapter picklis = new MyPickAdapter();
    Identify hotlink = new Identify();

    class MyPickAdapter implements PickListener {
        public void beginPick(PickEvent pe) {
        };

        // this fires even when you click outside the layer
        public void endPick(PickEvent pe) {
        }

        public void foundData(PickEvent pe) {
            com.esri.mo2.data.feat.Cursor c = pe.getCursor();
            Feature f = null;
            // Fields fields = null;
            if (c != null)
                f = (Feature) c.next();
            // fields = f.getFields();

            try {
                HotPick hotpick = new HotPick(f);// opens dialog window
            } catch (Exception e) {
            }
        }
    };

    static Envelope env;

    public ChartingTool() {
        super("Map of USA");
        helpToolOn = false;
        this.setSize(700, 450);
        zptb.setMap(map);
        stb.setMap(map);
        setJMenuBar(mbar);
        ActionListener lisZoom = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fullMap = false;
            }
        };
        ActionListener lisFullExt = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                fullMap = true;
            }
        };

        MouseAdapter mlXY = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(helpText.get(6));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };

        MouseAdapter mlMeasure = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(helpText.get(10));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };

        MouseAdapter mlLisZoom = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(helpText.get(4));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        MouseAdapter mlLisPoint = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(helpText.get(9));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        MouseAdapter mlLisHot = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(helpText.get(8));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        MouseAdapter mlLisZoomActive = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(helpText.get(5));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        MouseAdapter mllisFullExt = new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) && helpToolOn) {
                    try {
                        HelpDialog helpdialog = new HelpDialog(
                                (String) helpText.get(7));
                        helpdialog.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };

        JButton zoomInButton = (JButton) zptb.getActionComponent("ZoomIn");
        JButton zoomFullExtentButton = (JButton) zptb.getActionComponent("ZoomToFullExtent");
        JButton zoomToSelectedLayerButton = (JButton) zptb.getActionComponent("ZoomToSelectedLayer");

        zoomInButton.addActionListener(lisZoom);
        zoomInButton.addMouseListener(mlLisZoom);

        zoomFullExtentButton.addActionListener(lisFullExt);
        zoomFullExtentButton.addMouseListener(mllisFullExt);

        zoomToSelectedLayerButton.addActionListener(lisZoom);
        zoomToSelectedLayerButton.addMouseListener(mlLisZoomActive);

        complistener = new ComponentAdapter() {
            public void componentResized(ComponentEvent ce) {
                if (fullMap) {
                    map.setExtent(env);
                    map.zoom(1.0);
                    map.redraw();
                }
            }
        };
        addComponentListener(complistener);
        lis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source == chartjb) {
                    try {
                      ChartingToolStart.ChartTypeFrameGUI chartTool = new ChartingToolStart().new ChartTypeFrameGUI();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (source == prtjb || source instanceof JMenuItem) {
                    com.esri.mo2.ui.bean.Print mapPrint = new com.esri.mo2.ui.bean.Print();
                    mapPrint.setMap(map);
                    mapPrint.doPrint();// prints the map
                } else if (source == ptrjb) {
                    arrow.arrowChores();
                    map.setSelectedTool(arrow);
                    DistanceTool.resetDist();
                } else if (source == distjb) {
                    DistanceTool distanceTool = new DistanceTool();
                    map.setSelectedTool(distanceTool);
                } else if (source == XYjb) {
                    try {
                        AddXYtheme addXYtheme = new AddXYtheme();
                        addXYtheme.setMap(map);
                        addXYtheme.setVisible(false);
                        map.redraw();
                    } catch (IOException e) {
                    }
                } else if (source == helpjb) {
                    helpToolOn = true;
                    map.setSelectedTool(helpTool);
                } else if (source == hotjb) {
                    hotlink.setCursor(boltCursor);
                    map.setSelectedTool(hotlink);
                } else {
                    try {
                        AddLyrDialog aldlg = new AddLyrDialog();
                        aldlg.setMap(map);
                        aldlg.setVisible(true);
                    } catch (IOException e) {
                    }
                }
            }
        };
        layercontrollis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                String source = ae.getActionCommand();
                System.out.println(activeLayerIndex + " active index");
                if (source == "promote selected layer")
                    map.getLayerset().moveLayer(activeLayerIndex, ++activeLayerIndex);
                else
                    map.getLayerset().moveLayer(activeLayerIndex, --activeLayerIndex);
                enableDisableButtons();
                map.redraw();
            }
        };
        helplis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source instanceof JMenuItem) {
                    String arg = ae.getActionCommand();
                    if (arg == "About MOJO...") {
                        AboutBox aboutbox = new AboutBox();
                        aboutbox.setProductName("MOJO");
                        aboutbox.setProductVersion("2.0");
                        aboutbox.setVisible(true);
                    } else if (arg == "Contact us") {
                        try {
                            String s = "\n\n\n\n              Any enquiries should be addressed to "
                                    + "\n\n\n                         nanditha.murthy@gmail.com";
                            HelpDialog helpdialog = new HelpDialog(s);
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "Table of Contents") {
                        try {
                            HelpDialog helpdialog = new HelpDialog(
                                    helpText.get(0));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "Legend Editor") {
                        try {
                            HelpDialog helpdialog = new HelpDialog(
                                    helpText.get(1));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "Layer Control") {
                        try {
                            HelpDialog helpdialog = new HelpDialog(
                                    helpText.get(2));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "Help Tool") {
                        try {
                            HelpDialog helpdialog = new HelpDialog(
                                    helpText.get(3));
                            helpdialog.setVisible(true);
                        } catch (IOException e) {
                        }
                    }
                }
            }
        };
        layerlis = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Object source = ae.getSource();
                if (source instanceof JMenuItem) {
                    String arg = ae.getActionCommand();
                    if (arg == "add layer") {
                        try {
                            AddLyrDialog aldlg = new AddLyrDialog();
                            aldlg.setMap(map);
                            aldlg.setVisible(true);
                        } catch (IOException e) {
                        }
                    } else if (arg == "remove layer") {
                        try {
                            com.esri.mo2.map.dpy.Layer dpylayer = legend.getLayer();
                            map.getLayerset().removeLayer(dpylayer);
                            map.redraw();
                            remlyritem.setEnabled(false);
                            propsitem.setEnabled(false);
                            attribitem.setEnabled(false);
                            promoteitem.setEnabled(false);
                            demoteitem.setEnabled(false);
                            stb.setSelectedLayer(null);
                            zptb.setSelectedLayer(null);
                        } catch (Exception e) {
                        }
                    } else if (arg == "Legend Editor") {
                        LayerProperties lp = new LayerProperties();
                        lp.setLegend(legend);
                        lp.setSelectedTabIndex(0);
                        lp.setVisible(true);
                    } else if (arg == "open attribute table") {
                        try {
                            layer4 = legend.getLayer();
                            AttrTab attrtab = new AttrTab();
                            attrtab.setVisible(true);
                        } catch (IOException ioe) {
                        }
                    }else if (arg == "create layer from selection") {
                        layer4 = legend.getLayer();
                        FeatureLayer flayer2 = (FeatureLayer) layer4;

                        com.esri.mo2.map.draw.BaseSimpleRenderer sbr = new com.esri.mo2.map.draw.BaseSimpleRenderer(); 
                        com.esri.mo2.map.draw.SimpleFillSymbol sfs = null; // for polygon 
                        com.esri.mo2.map.draw.SimpleMarkerSymbol sms = null; // for points
                        com.esri.mo2.map.draw.SimpleLineSymbol sls = null; // for lines

                        // select, e.g., Montana and then click the
                        // create layer menuitem; next line verifies a selection was made
                        System.out.println("has selected: " + flayer2.hasSelection());
                        //System.out.println("layer info: " + flayer2.getLayerInfo());
                        //System.out.println("ShapeFileType: " + flayer2.getLayerInfo().getType()); // to get ShapeFileType
                        //***
                        // next line creates the 'set' of selections

                        if (flayer2.hasSelection()) {
                            SelectionSet selectset = flayer2.getSelectionSet(); 
                            // next line makes a new feature layer of the selections
                            FeatureLayer selectedlayer = flayer2.createSelectionLayer(selectset);
                            //selectedlayer is the child layer created from selecting flayer2.
                            sbr.setLayer(selectedlayer);


                            int shT = flayer2.getFeatureClass().getFeatureType();


                            if (shT == 2) {
                                sfs = new com.esri.mo2.map.draw.SimpleFillSymbol();// for polygons
                                sfs.setSymbolColor(new Color(255, 255, 0)); // mellow yellow
                                sfs.setType(com.esri.mo2.map.draw.SimpleFillSymbol.FILLTYPE_SOLID);
                                sfs.setBoundary(true);
                                sbr.setSymbol(sfs);
                            } else if (shT == 0) {
                                sms = new com.esri.mo2.map.draw.SimpleMarkerSymbol();// for points
                                sms.setType(com.esri.mo2.map.draw.SimpleMarkerSymbol.CIRCLE_MARKER);
                                sms.setWidth(6);
                                sms.setSymbolColor(Color.red);
                                sbr.setSymbol(sms);
                            } else if (shT == 1) {
                                sls = new com.esri.mo2.map.draw.SimpleLineSymbol();// for lines
                                sls.setLineColor(new Color(0, 255, 0)); // green
                                sls.setStroke(AoLineStyle.getStroke(AoLineStyle.SOLID_LINE, 4));
                                sbr.setSymbol(sls);
                            }

                            selectedlayer.setRenderer(sbr);
                            Layerset layerset = map.getLayerset();

                            // next line places a new visible layer, e.g.
                            // Montana, on the map
                            layerset.addLayer(selectedlayer);

                            // selectedlayer.setVisible(true);
                            try {
                                legend2 = toc.findLegend(selectedlayer);
                            } catch (Exception e) {
                            }

                            CreateShapeDialog csd = new CreateShapeDialog(selectedlayer,shT);
                            csd.setVisible(true);
                            Flash flash = new Flash(legend2);
                            flash.start();
                            map.redraw(); // necessary to see color immediately
                        }
                    }
                }
            }
        };

        toc.setMap(map);
        mytocadapter = new TocAdapter() {
            public void click(TocEvent e) {
                System.out.println(activeLayerIndex + "dex");
                legend = e.getLegend();
                activeLayer = legend.getLayer();
                stb.setSelectedLayer(activeLayer);
                zptb.setSelectedLayer(activeLayer);
                // get acive layer index for promote and demote
                activeLayerIndex = map.getLayerset().indexOf(activeLayer);
                // layer indices are in order added, not toc order.
                System.out.println(activeLayerIndex + "active ndex");
                com.esri.mo2.map.dpy.Layer[] layers = { activeLayer };
                hotlink.setSelectedLayers(layers);// replaces setToc from MOJ10
                remlyritem.setEnabled(true);
                propsitem.setEnabled(true);
                attribitem.setEnabled(true);
                enableDisableButtons();
            }
        };
        map.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent me) {
                com.esri.mo2.cs.geom.Point worldPoint = null;
                if (map.getLayerCount() > 0) {
                    worldPoint = map.transformPixelToWorld(me.getX(), me.getY());
                    String s = "X:" + df.format(worldPoint.getX()) + " " + "Y:" + df.format(worldPoint.getY());
                    statusLabel.setText(s);
                } else
                    statusLabel.setText("X:0.000 Y:0.000");
            }
        });

        toc.addTocListener(mytocadapter);
        remlyritem.setEnabled(false); // assume no layer initially selected
        propsitem.setEnabled(false);
        attribitem.setEnabled(false);
        promoteitem.setEnabled(false);
        demoteitem.setEnabled(false);
        printitem.addActionListener(lis);
        addlyritem.addActionListener(layerlis);
        remlyritem.addActionListener(layerlis);
        propsitem.addActionListener(layerlis);
        attribitem.addActionListener(layerlis);
        createlayeritem.addActionListener(layerlis);
        promoteitem.addActionListener(layercontrollis);
        demoteitem.addActionListener(layercontrollis);
        tocitem.addActionListener(helplis);
        legenditem.addActionListener(helplis);
        layercontrolitem.addActionListener(helplis);
        helptoolitem.addActionListener(helplis);
        contactitem.addActionListener(helplis);
        aboutitem.addActionListener(helplis);
        file.add(addlyritem);
        file.add(printitem);
        file.add(remlyritem);
        file.add(propsitem);
        theme.add(attribitem);
        theme.add(createlayeritem);
        layercontrol.add(promoteitem);
        layercontrol.add(demoteitem);
        help.add(helptopics);
        helptopics.add(tocitem);
        helptopics.add(legenditem);
        helptopics.add(layercontrolitem);
        help.add(helptoolitem);
        help.add(contactitem);
        help.add(aboutitem);
        mbar.add(file);
        mbar.add(theme);
        mbar.add(layercontrol);
        mbar.add(help);
        chartjb.addActionListener(lis);
        chartjb.setToolTipText(CHARTING_TOOL_TIP);
        prtjb.addActionListener(lis);
        prtjb.setToolTipText("print map");
        addlyrjb.addActionListener(lis);
        addlyrjb.setToolTipText("add layer");
        ptrjb.addActionListener(lis);
        distjb.addActionListener(lis);
        distjb.addMouseListener(mlMeasure);
        XYjb.addActionListener(lis);
        XYjb.addMouseListener(mlXY);
        helpjb.addActionListener(lis);
        XYjb.setToolTipText("add a layer of points from a file");
        prtjb.setToolTipText("pointer");
        prtjb.addMouseListener(mlLisPoint);
        distjb.setToolTipText("press-drag-release to measure a distance");
        helpjb.setToolTipText("left click here, then right click on a tool to learn about that tool");
        hotlink.addPickListener(picklis);
        hotlink.setPickWidth(20);// sets tolerance for hotlink clicks
        hotjb.addActionListener(lis);
        hotjb.addMouseListener(mlLisHot);
        hotjb.setToolTipText("hotlink tool--click somthing to get information about it.");
        jtb.add(prtjb);
        jtb.add(addlyrjb);
        jtb.add(ptrjb);
        jtb.add(chartjb);
        jtb.add(distjb);
        jtb.add(XYjb);
        jtb.add(hotjb);
        jtb.add(helpjb);
        myjp.add(jtb);
        myjp.add(zptb);
        myjp.add(stb);
        myjp2.add(statusLabel);
        myjp2.add(milesLabel);
        myjp2.add(kmLabel);
        setuphelpText();
        getContentPane().add(map, BorderLayout.CENTER);
        getContentPane().add(myjp, BorderLayout.NORTH);
        getContentPane().add(myjp2, BorderLayout.SOUTH);
        addShapefileToMap(layer, USAShp);
        // addShapefileToMap(layer2,s2);
        getContentPane().add(toc, BorderLayout.WEST);
    }

    private void addShapefileToMap(Layer layer, String s) {
        String datapath = s;
        layer.setDataset("0;" + datapath);
        map.add(layer);
    }

    private void setuphelpText() {
        String s0 = "    The toc, or table of contents, is to the left of the map. \n"
                + "    Each entry is called a 'legend' and represents a map 'layer' or \n"
                + "    'theme'.  If you click on a legend, that layer is called the \n"
                + "    active layer, or selected layer.  Its display (rendering) properties \n"
                + "    can be controlled using the Legend Editor, and the legends can be \n"
                + "    reordered using Layer Control.  Both Legend Editor and Layer Control \n"
                + "    are separate Help Topics.  This line is e... x... t... e... n... t... e... d"
                + "    to test the scrollpane.";
        helpText.add(s0);
        String s1 = "  The Legend Editor is a menu item found under the File menu. \n"
                + "    Given that a layer is selected by clicking on its legend in the table of \n"
                + "    contents, clicking on Legend Editor will open a window giving you choices \n"
                + "    about how to display that layer.  For example you can control the color \n"
                + "    used to display the layer on the map, or whether to use multiple colors ";
        helpText.add(s1);
        String s2 = "  Layer Control is a Menu on the menu bar.  If you have selected a \n"
                + " layer by clicking on a legend in the toc (table of contents) to the left of \n"
                + " the map, then the promote and demote tools will become usable.  Clicking on \n"
                + " promote will raise the selected legend one position higher in the toc, and \n"
                + " clicking on demote will lower that legend one position in the toc.";
        helpText.add(s2);
        String s3 = "    This tool will allow you to learn about certain other tools. \n"
                + "    You begin with a standard left mouse button click on the Help Tool itself. \n"
                + "    RIGHT click on another tool and a window may give you information about the  \n"
                + "    intended usage of the tool.  Click on the arrow tool to stop using the \n"
                + "    help tool.";
        helpText.add(s3);
        String s4 = "If you click on the Zoom In tool, and then click on the map, you \n"
                + " will see a part of the map in greater detail.  You can zoom in multiple times. \n"
                + " You can also sketch a rectangular part of the map, and zoom to that.  You can \n"
                + " undo a Zoom In with a Zoom Out or with a Zoom to Full Extent";
        helpText.add(s4);
        String s5 = "You must have a selected layer to use the Zoom to Active Layer tool.\n"
                + "    If you then click on Zoom to Active Layer, you will be shown enough of \n"
                + "    the full map to see all of the features in the layer you select.  If you \n"
                + "    select a layer that shows where glaciers are, then you do not need to \n"
                + "    see Hawaii, or any southern states, so you will see Alaska, and northern \n"
                + "    mainland states.";
        helpText.add(s5);
        String s6 = "You must click on XY button\n"
                + "    In the ne wwindow you have to go to the path where your csv file is present \n"
                + "    once csv file is obtained you should open the file \n"
                + "    on selecting your csv file, new points that were in the file will be  \n"
                + "    marked on the map, with a layer named Mypoint, on  \n"
                + "    top of the toc.";
        helpText.add(s6);
        String s7 = "You must click on ZoomtoFull extent button,\n"
                + "    This button is used to zoom to the largest extent of the map service \n"
                + "    Single click on the button will \n"
                + "    allows you to quickly zoom out to the full extent of all the   \n"
                + "    map layers in the current window  \n"
                + "    i.e, quickly zoom the map to its fullest visible extent.";
        helpText.add(s7);
        String s8 = "You must click on Bolt button,\n"
                + "     hotlink tool allows users to view mulitple images  \n"
                + "    associated with single features in a theme. \n"
                + "    When we click on the point on the map, we get the details of that point   \n"
                + "    like image, president , vice president etc  \n"
                + "    related to that Cricket League";
        helpText.add(s8);
        String s9 = "You must click on arrow button,\n"
                + "     Arrow tool, and when we click on it, our code will make it \n"
                + "     the �current� or �selected� tool.   That disenfranchises the \n"
                + "     previous tool, and toolwise, we go back into a neutral gear, \n"
                + "since the Arrow tool has no behavior attached to it.";
        helpText.add(s9);
        String s10 = "You must click on distance measure button,\n"
                + "     If you click on the MOJO MeasureTool Measuring Tool and then.\n"
                + "     you click anywhere on the map, and drag and release. This will give \n"
                + "     the distance between the mouse click and release.";
        helpText.add(s10);
    }

    public static void main(String[] args) {
        ChartingTool usleague = new ChartingTool();
        usleague.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Bye Bye Cricket League");
                System.exit(0);
            }
        });
        usleague.setVisible(true);
        env = map.getExtent();
    }

    private void enableDisableButtons() {
        int layerCount = map.getLayerset().getSize();
        if (layerCount < 2) {
            promoteitem.setEnabled(false);
            demoteitem.setEnabled(false);
        } else if (activeLayerIndex == 0) {
            demoteitem.setEnabled(false);
            promoteitem.setEnabled(true);
        } else if (activeLayerIndex == layerCount - 1) {
            promoteitem.setEnabled(false);
            demoteitem.setEnabled(true);
        } else {
            promoteitem.setEnabled(true);
            demoteitem.setEnabled(true);
        }
    }

    private ArrayList<String> helpText = new ArrayList<String>(3);
}








