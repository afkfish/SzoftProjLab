package com.ez_mode.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game implements ActionListener {
  static int gridNum = 10;
  static int windowWidth = 800 - 100;
  int windowHeight = 800;
  static int fieldSize = windowWidth / gridNum;
  static int actionSize = fieldSize - 10;
  public boolean nomadTurn = false;

  static JFrame frame = new JFrame();
  JPanel titlePanel = new JPanel();
  JLabel textField = new JLabel();
  JButton exitButton = new JButton();
  JPanel mapPanel = new JPanel();
  static JButton[] mapButtons = new JButton[gridNum * gridNum + 1];
  JPanel actionPanel = new JPanel();

  public String outImagePath = "out.png";
  public String pipeImagePath = "src/main/resources/pipe.png";
  public String sandImagePath = "src/main/resources/sand.png";
  public String plumberImagePath = "src/main/resources/transplumber.png";
  public String nomadImagePath = "src/main/resources/nomad.png";
  public String waterspringImagePath = "src/main/resources/waterspring.png";
  public String waterpumpImagePath = "src/main/resources/waterpump.png";
  public String waterpipeImagePath = "src/main/resources/waterpipe.png";
  public String waterImagePath = "src/main/resources/water.png";
  public String stickypipeImagePath = "src/main/resources/stickypipe.png";
  public String slipperypipeImagePath = "src/main/resources/slipperypipe.png";
  public static String repairImagePath = "src/main/resources/repair.png";
  public String emptypumpImagePath = "src/main/resources/emptypump.png";
  public String cisternImagePath = "src/main/resources/cistern.png";
  public String brokenpumpImagePath = "src/main/resources/brokenpump.png";
  public String brokenpipeImagePath = "src/main/resources/brokenpipe.png";
  public static String breakImagePath = "src/main/resources/break.png";
  public String pickuppipeImagePath = "src/main/resources/pickuppipe.png";
  public String pickuppumpImagePath = "src/main/resources/pickuppump.png";
  public String moveupImagePath = "src/main/resources/moveup.png";
  public String moveleftImagePath = "src/main/resources/moveleft.png";
  public String movedownImagePath = "src/main/resources/movedown.png";
  public String moverightImagePath = "src/main/resources/moveright.png";

  public ImageIcon outIcon = new ImageIcon(outImagePath);
  public ImageIcon pipeIcon = new ImageIcon(pipeImagePath);
  public ImageIcon sandIcon = new ImageIcon(sandImagePath);
  public ImageIcon plumberIcon = new ImageIcon(plumberImagePath);
  public ImageIcon nomadIcon = new ImageIcon(nomadImagePath);
  public ImageIcon waterspringIcon = new ImageIcon(waterspringImagePath);
  public ImageIcon waterpumpIcon = new ImageIcon(waterpumpImagePath);
  public ImageIcon waterpipeIcon = new ImageIcon(waterpipeImagePath);
  public ImageIcon waterIcon = new ImageIcon(waterImagePath);
  public ImageIcon stickypipeIcon = new ImageIcon(stickypipeImagePath);
  public ImageIcon slipperypipeIcon = new ImageIcon(slipperypipeImagePath);
  public static ImageIcon repairIcon = new ImageIcon(repairImagePath);
  public ImageIcon emptypumpIcon = new ImageIcon(emptypumpImagePath);
  public ImageIcon cisternIcon = new ImageIcon(cisternImagePath);
  public ImageIcon brokenpumpIcon = new ImageIcon(brokenpumpImagePath);
  public ImageIcon brokenpipeIcon = new ImageIcon(brokenpipeImagePath);
  public static ImageIcon breakIcon = new ImageIcon(breakImagePath);
  public ImageIcon pickuppipeIcon = new ImageIcon(pickuppipeImagePath);
  public ImageIcon pickuppumpIcon = new ImageIcon(pickuppumpImagePath);
  public ImageIcon moveupIcon = new ImageIcon(moveupImagePath);
  public ImageIcon moveleftIcon = new ImageIcon(moveleftImagePath);
  public ImageIcon movedownIcon = new ImageIcon(movedownImagePath);
  public ImageIcon moverightIcon = new ImageIcon(moverightImagePath);

  public Game() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Game");
    frame.setResizable(false);

    titlePanel.setLayout(new BorderLayout());

    textField.setBackground(new Color(0, 0, 0));
    textField.setForeground(new Color(230, 230, 230));
    textField.setFont(new Font("Monospace", Font.BOLD, 50));
    textField.setHorizontalAlignment(JLabel.CENTER);
    textField.setText("GAME");
    textField.setOpaque(true);

    mapPanel.setLayout(new GridLayout(gridNum, gridNum + 1));
    mapPanel.setBorder(null);

    for (int i = 0; i < (gridNum * gridNum); i++) {
      mapButtons[i] = new JButton();
      mapPanel.add(mapButtons[i]);
      mapButtons[i].setFocusable(false);
      mapButtons[i].setSize(fieldSize, fieldSize);
      mapButtons[i].setBorderPainted(false);
      mapButtons[i].setHorizontalAlignment(JLabel.HORIZONTAL);
      mapButtons[i].addActionListener(this);
    }
    for (int i = 0; i < (gridNum * gridNum - gridNum); i++) {
      // buttons[i].setMaximumSize(buttons[i].getPreferredSize());
      // buttons[i].setBackground(new Color(244, 228, 156));
      mapButtons[i].setBackground(new Color(180, 180, 180));
      sandIcon = new ImageIcon(String.valueOf(sandImagePath));
      Image sandImage = sandIcon.getImage();
      Image sandModIcon = sandImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
      mapButtons[i].setIcon(new ImageIcon(sandModIcon));
    }

    Image cisternImage = cisternIcon.getImage();
    Image cisternModIcon =
        cisternImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    mapButtons[0].setIcon(new ImageIcon(cisternModIcon));
    mapButtons[1].setIcon(new ImageIcon(cisternModIcon));
    mapButtons[2].setIcon(new ImageIcon(cisternModIcon));

    Image waterspringImage = waterspringIcon.getImage();
    Image waterspringModIcon =
        waterspringImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    mapButtons[(gridNum * gridNum - gridNum) - 1].setIcon(new ImageIcon(waterspringModIcon));
    mapButtons[(gridNum * gridNum - gridNum) - 2].setIcon(new ImageIcon(waterspringModIcon));
    mapButtons[(gridNum * gridNum - gridNum) - 3].setIcon(new ImageIcon(waterspringModIcon));

    try {
      BufferedImage image = ImageIO.read(new File(pipeImagePath));
      BufferedImage overlay = ImageIO.read(new File(plumberImagePath));

      int w = Math.max(image.getWidth(), overlay.getWidth());
      int h = Math.max(image.getHeight(), overlay.getHeight());
      BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

      // paint both images, preserving the alpha channels
      Graphics g = combined.getGraphics();
      g.drawImage(image, 0, 0, null);
      g.drawImage(overlay, 0, 0, null);

      g.dispose();

      // Save as new image
      ImageIO.write(combined, "PNG", new File(outImagePath));
    } catch (IOException e) {

    }

    Image plumberImage = outIcon.getImage();
    Image plumberModIcon =
        plumberImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    mapButtons[10].setIcon(new ImageIcon(plumberModIcon));

    Image nomadImage = nomadIcon.getImage();
    Image nomadModIcon = nomadImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    mapButtons[15].setIcon(new ImageIcon(nomadModIcon));

    for (int i = (gridNum * gridNum - gridNum); i < (gridNum * gridNum); i++) {
      mapButtons[i].setBackground(new Color(255, 255, 255));
    }

    Image moveupImage = moveupIcon.getImage();
    Image moveupModIcon =
        moveupImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum].setIcon(new ImageIcon(moveupModIcon));

    Image moveleftImage = moveleftIcon.getImage();
    Image moveleftModIcon =
        moveleftImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 1].setIcon(new ImageIcon(moveleftModIcon));

    Image movedownImage = movedownIcon.getImage();
    Image movedownModIcon =
        movedownImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 2].setIcon(new ImageIcon(movedownModIcon));

    Image moverightImage = moverightIcon.getImage();
    Image moverightModIcon =
        moverightImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 3].setIcon(new ImageIcon(moverightModIcon));

    Image repairImage = repairIcon.getImage();
    Image repairModIcon =
        repairImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 4].setIcon(new ImageIcon(repairModIcon));

    mapButtons[gridNum * gridNum - gridNum + 4].addActionListener(Controller::GameExitAction);

    Image stickypipeImage = stickypipeIcon.getImage();
    Image stickypipeModIcon =
        stickypipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 5].setIcon(new ImageIcon(stickypipeModIcon));

    Image slipperypipeImage = slipperypipeIcon.getImage();
    Image slipperypipeModIcon =
        slipperypipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 6].setIcon(new ImageIcon(slipperypipeModIcon));

    exitButton.setBounds(500, 13, 150, 40);
    exitButton.setFont(new Font("Monospace", Font.BOLD, 20));
    exitButton.setText("End Game");
    exitButton.setBackground(new Color(250, 250, 250));
    exitButton.setForeground(new Color(0, 0, 0));
    exitButton.setFocusable(false);
    exitButton.addActionListener(Controller::GameExitAction);

    frame.add(titlePanel, BorderLayout.NORTH);
    titlePanel.add(textField);
    frame.add(mapPanel);
    frame.add(actionPanel, BorderLayout.SOUTH);
    frame.pack();
    frame.setSize(windowWidth, windowHeight);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    for (int i = 0; i < gridNum * gridNum; i++) {
      /*if (e.getSource() == mapButtons[i]) {
        if (mapButtons[i].getText().equals("")) {
          mapButtons[i].setBorderPainted(false);
          Image pipeImage = pipeIcon.getImage();
          Image pipeModIcon = pipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[i].setIcon(new ImageIcon(pipeModIcon));
        }
      }*/

    }
    if (e.getSource() == mapButtons[gridNum * gridNum - gridNum + 4]) {
      nomadTurn = !nomadTurn;
      updateAction();
    }
  }

  private void updateAction() {
    System.out.println(nomadTurn);
    if (nomadTurn) {
      Image breakImage = breakIcon.getImage();
      Image breakModIcon =
          breakImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 4].setIcon(new ImageIcon(breakModIcon));
    } else {
      Image repairImage = repairIcon.getImage();
      Image repairModIcon =
          repairImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 4].setIcon(new ImageIcon(repairModIcon));
    }
  }
}
