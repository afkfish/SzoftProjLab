package com.ez_mode.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Game implements ActionListener {
  int gridNum = 10;
  int fieldSize = 68;

  JFrame frame = new JFrame();
  JPanel titlePanel = new JPanel();
  JPanel turnPanel = new JPanel();
  JPanel playerPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JLabel textField = new JLabel();
  JLabel turnLabel = new JLabel();
  JButton[] buttons = new JButton[gridNum * gridNum];
  JButton exitButton = new JButton();

  public String pipeImagePath = "src/main/resources/pipe.png";
  public String sandImagePath = "src/main/resources/sand.png";
  public String plumberImagePath = "src/main/resources/plumber.png";
  public String nomadImagePath = "src/main/resources/nomad.png";
  public String waterspringImagePath = "src/main/resources/waterspring.png";
  public String waterpumpImagePath = "src/main/resources/waterpump.png";
  public String waterpipeImagePath = "src/main/resources/waterpipe.png";
  public String waterImagePath = "src/main/resources/water.png";
  public String stickypipeImagePath = "src/main/resources/stickypipe.png";
  public String slipperypipeImagePath = "src/main/resources/slipperypipe.png";
  public String repairImagePath = "src/main/resources/repair.png";
  public String emptypumpImagePath = "src/main/resources/emptypump.png";
  public String cisternImagePath = "src/main/resources/cistern.png";
  public String brokenpumpImagePath = "src/main/resources/brokenpump.png";
  public String brokenpipeImagePath = "src/main/resources/brokenpipe.png";
  public String breakImagePath = "src/main/resources/break.png";

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
  public ImageIcon repairIcon = new ImageIcon(repairImagePath);
  public ImageIcon emptypumpIcon = new ImageIcon(emptypumpImagePath);
  public ImageIcon cisternIcon = new ImageIcon(cisternImagePath);
  public ImageIcon brokenpumpIcon = new ImageIcon(brokenpumpImagePath);
  public ImageIcon brokenpipeIcon = new ImageIcon(brokenpipeImagePath);
  public ImageIcon breakIcon = new ImageIcon(breakImagePath);

  public Game() {
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Game");
    frame.setResizable(false);
    frame.getContentPane().setBackground(new Color(50, 50, 50));

    titlePanel.setLayout(new BorderLayout());
    turnPanel.setLayout(new BorderLayout());

    textField.setBackground(new Color(0, 0, 0));
    textField.setForeground(new Color(230, 230, 230));
    textField.setFont(new Font("Monospace", Font.BOLD, 50));
    textField.setHorizontalAlignment(JLabel.CENTER);
    textField.setText("GAME");
    textField.setOpaque(true);

    turnLabel.setBackground(new Color(50, 50, 50));
    turnLabel.setForeground(new Color(230, 230, 230));
    turnLabel.setFont(new Font("Monospace", Font.BOLD, 30));
    turnLabel.setHorizontalAlignment(JLabel.CENTER);
    /*if (turnNum == 1) {
        turnLabel.setText( + " turn");
    }
    else {
        turnLabel.setText( + " turn");
    }
    turnLabel.setOpaque(true);*/

    playerPanel.setLayout(new BorderLayout());
    buttonPanel.setLayout(new GridLayout(gridNum, gridNum));
    buttonPanel.setBackground(new Color(50, 50, 50));

    for (int i = 0; i < (gridNum * gridNum); i++) {
      buttons[i] = new JButton();
      buttonPanel.add(buttons[i]);
      buttons[i].setFocusable(false);
      buttons[i].setSize(20, 20);
      buttons[i].setMaximumSize(buttons[i].getPreferredSize());
      buttons[i].setBackground(new Color(244, 228, 156));
      buttons[i].setBorderPainted(false);
      buttons[i].setHorizontalAlignment(JLabel.HORIZONTAL);
      sandIcon = new ImageIcon(String.valueOf(sandImagePath));
      Image sandImage = sandIcon.getImage();
      Image modIcon2 = sandImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
      buttons[i].setIcon(new ImageIcon(modIcon2));
      buttons[i].addActionListener(this);
    }

    Image cisternImage = cisternIcon.getImage();
    Image cisternModIcon = cisternImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    buttons[0].setIcon(new ImageIcon(cisternModIcon));
    buttons[1].setIcon(new ImageIcon(cisternModIcon));
    buttons[2].setIcon(new ImageIcon(cisternModIcon));

    Image waterspringImage = waterspringIcon.getImage();
    Image waterspringModIcon = waterspringImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    buttons[gridNum*gridNum-1].setIcon(new ImageIcon(waterspringModIcon));
    buttons[gridNum*gridNum-2].setIcon(new ImageIcon(waterspringModIcon));
    buttons[gridNum*gridNum-3].setIcon(new ImageIcon(waterspringModIcon));

    buttons[10].setBackground(new Color(180, 180, 180));
    Image plumberImage = plumberIcon.getImage();
    Image plumberModIcon = plumberImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    buttons[10].setIcon(new ImageIcon(plumberModIcon));

    buttons[24].setBackground(new Color(180, 180, 180));
    Image nomadImage = nomadIcon.getImage();
    Image nomadModIcon = nomadImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    buttons[24].setIcon(new ImageIcon(nomadModIcon));

    exitButton.setBounds(500, 13, 150, 40);
    exitButton.setFont(new Font("Monospace", Font.BOLD, 20));
    exitButton.setText("End Game");
    exitButton.setBackground(new Color(250, 250, 250));
    exitButton.setForeground(new Color(0, 0, 0));
    exitButton.setFocusable(false);
    exitButton.addActionListener(this);

    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel textPanel = new JPanel(new GridLayout(2, 1));
    textPanel.add(textField);
    textPanel.add(turnLabel);
    frame.add(exitButton);
    mainPanel.add(textPanel, BorderLayout.NORTH);
    mainPanel.add(buttonPanel, BorderLayout.CENTER);
    frame.add(mainPanel);
    frame.pack();
    frame.setSize(700, 800);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    for (int i = 0; i < gridNum * gridNum; i++) {
      if (e.getSource() == buttons[i]) {
        if (buttons[i].getText().equals("")) {
          Image pipeImage = pipeIcon.getImage();
          Image pipeModIcon = pipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          buttons[i].setBackground(new Color(180, 180, 180));
          buttons[i].setIcon(new ImageIcon(pipeModIcon));
        }
      }
    }
  }
}
