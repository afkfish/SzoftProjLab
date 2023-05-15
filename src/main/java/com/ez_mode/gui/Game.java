package com.ez_mode.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Game implements ActionListener {
  String imagePath1 = "src/main/resources/waterpipe.png";
  String imagePath2 = "src/main/resources/pixil-frame-1.png";
  String imagePath3 = "src/main/resources/plumber.png";
  String imagePath4 = "src/main/resources/nomad.png";
  int gridNum = 10;
  int fieldSize = 60;

  JFrame frame = new JFrame();
  JPanel titlePanel = new JPanel();
  JPanel turnPanel = new JPanel();
  JPanel playerPanel = new JPanel();
  JPanel buttonPanel = new JPanel();
  JLabel textField = new JLabel();
  JLabel turnLabel = new JLabel();
  JButton[] buttons = new JButton[gridNum * gridNum];
  JButton exitButton = new JButton();

  ImageIcon icon1 = new ImageIcon(imagePath1);
  ImageIcon icon2 = new ImageIcon(imagePath2);
  ImageIcon icon3 = new ImageIcon(imagePath3);
  ImageIcon icon4 = new ImageIcon(imagePath4);


  public String getImagePath1() {
    return imagePath1;
  }
  public String getImagePath2() {
    return imagePath2;
  }
  public String getImagePath3() {
    return imagePath3;
  }
  public String getImagePath4() {
    return imagePath4;
  }

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
      buttons[i].setBackground(new Color(244, 228, 156));
      buttons[i].setBorderPainted(false);
      buttons[i].setHorizontalAlignment(JLabel.HORIZONTAL);
      icon2 = new ImageIcon(String.valueOf(getImagePath2()));
      Image i2 = icon2.getImage();
      Image modIcon2 = i2.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
      buttons[i].setIcon(new ImageIcon(modIcon2));
      buttons[i].addActionListener(this);
    }


    buttons[10].setBackground(new Color(180, 180, 180));
    icon3 = new ImageIcon(String.valueOf(getImagePath3()));
    Image i3 = icon3.getImage();
    Image modIcon3 = i3.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    buttons[10].setIcon(new ImageIcon(modIcon3));

    buttons[24].setBackground(new Color(180, 180, 180));
    icon4 = new ImageIcon(String.valueOf(getImagePath4()));
    Image i4 = icon4.getImage();
    Image modIcon4 = i4.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    buttons[24].setIcon(new ImageIcon(modIcon4));


    exitButton.setBounds(600, 13, 150, 40);
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
    frame.setSize(800, 800);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  public void actionPerformed(ActionEvent e) {
    for (int i = 0; i < gridNum * gridNum; i++) {
      if (e.getSource() == buttons[i]) {
        if (buttons[i].getText().equals("")) {
          icon1 = new ImageIcon(String.valueOf(getImagePath1()));
          Image i1 = icon1.getImage();
          Image modIcon1 = i1.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          buttons[i].setBackground(new Color(180, 180, 180));
          buttons[i].setIcon(new ImageIcon(modIcon1));
        }
      }
    }
  }
}
