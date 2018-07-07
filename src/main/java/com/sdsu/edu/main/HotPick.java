package com.sdsu.edu.main;

import com.esri.mo2.data.feat.Feature;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HotPick extends JDialog {

  JPanel namePanel = new JPanel();
  JPanel infoPanel = new JPanel();
  // JPanel rightPanel = new JPanel();
  JPanel photoPanel = new JPanel();
  JLabel image = new JLabel();

  ActionListener btnlis;
  
  HotPick(Feature f) throws IOException {
      if (f.getDataID().getSource().trim().equalsIgnoreCase("MyPoints")) {
          setTitle("Cricket League:" + f.getValue(1).toString());
          final String imageName = f.getValue(8).toString();
          final ImageIcon myImage = new ImageIcon(imageName);

          final JButton nameBtn = new JButton((String) f.getValue(1));
          final JButton addressBtn = new JButton("President : \n" + (String) f.getValue(2));
          final JButton phoneBtn = new JButton("Vice President : \n"  + (String) f.getValue(3));
          final JButton estdYrBtn = new JButton("Contact : \n" + (String) f.getValue(4));
          final JButton ownerBtn = new JButton("Write To : \n" + (String) f.getValue(5));
          final JButton currStatusBtn = new JButton("No. of Teams \n" + (String) f.getValue(6));

          final JButton websiteBtn = new JButton("Know more... Click here");
          final JButton videoBtn = new JButton("Video");

          final String command = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "    + (String) f.getValue(7);
          final String command2 = "C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE "   + (String) f.getValue(9);

          infoPanel.setLayout(new GridLayout(4, 2));

          // rightPanel.setLayout(new BorderLayout());
          // rightPanel.setSize(100, 800);
          namePanel.setLayout(new GridLayout(1, 1));

          image.setIcon(myImage);

          btnlis = new ActionListener() {
              public void actionPerformed(ActionEvent ae) {
                  Object source = ae.getSource();
                  try {
                      if (source == websiteBtn) {
                          try {
                              // String command = googlePath[i];
                              Process link = Runtime.getRuntime().exec(command);
                          } catch (Exception ex) {
                              System.out.println("cannot execute command. " + ex);
                          }
                      } else if (source == videoBtn) {
                          try {
                              // String command2 = googlePath[i];
                              Process link = Runtime.getRuntime().exec(command2);
                          } catch (Exception ex) {
                              System.out.println("cannot execute command. " + ex);
                          }
                      }
                  } catch (Exception e) {
                      System.out.println("Error:::");
                      e.printStackTrace();
                  }
              }
          };
          videoBtn.addActionListener(btnlis);

          websiteBtn.addActionListener(btnlis);
          // nameBtn.setEnabled(false);
          addressBtn.setEnabled(false);
          phoneBtn.setEnabled(false);
          estdYrBtn.setEnabled(false);
          ownerBtn.setEnabled(false);
          currStatusBtn.setEnabled(false);
          namePanel.add(nameBtn);

          infoPanel.add(websiteBtn);
          infoPanel.add(videoBtn);
          infoPanel.add(addressBtn);
          infoPanel.add(phoneBtn);
          infoPanel.add(estdYrBtn);
          infoPanel.add(ownerBtn);
          infoPanel.add(currStatusBtn);

          photoPanel.add(image);

          this.getContentPane().add(namePanel, BorderLayout.NORTH);
          this.getContentPane().add(photoPanel, BorderLayout.CENTER);
          this.getContentPane().add(infoPanel, BorderLayout.SOUTH);

          setVisible(true);
          this.setSize(750, 500);

          // setVisible(true);
      }

  }

}
