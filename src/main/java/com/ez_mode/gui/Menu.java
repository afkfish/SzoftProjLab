package com.ez_mode.gui;

import java.awt.*;
import javax.swing.*;

public class Menu {
  /** Java Swing components for the Menu class */
  static JFrame frame = new JFrame();

  static JTextField loadTextField = new JTextField();
  static JTextField plumberNamesTextField = new JTextField();
  static JTextField nomadNamesTextField = new JTextField();
  static JTextField playerCountTextField = new JTextField();
  static int playerCount;
  static String loadedPath;
  JButton startButton = new JButton();
  JTextField title = new JTextField();
  JTextField loadText = new JTextField();
  JTextField plumberNamesText = new JTextField();
  JTextField nomadNamesText = new JTextField();
  JTextField playerCountText = new JTextField();
  JButton exitButton = new JButton();

  public Menu() {
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    title.setFont(new Font("Monospace", Font.BOLD, 45));
    title.setBounds(140, 10, 300, 40);
    title.setText("Menu");
    title.setEditable(false);

    startButton.setBounds(55, 80, 300, 40);
    startButton.setFont(new Font("Monospace", Font.BOLD, 25));
    startButton.setText("Start game");
    startButton.setBackground(new Color(0, 0, 0));
    startButton.setForeground(new Color(250, 250, 250));
    startButton.setFocusable(false);
    startButton.addActionListener(Controller::MenuStartAction);

    loadText.setBackground(new Color(50, 50, 50));
    loadText.setForeground(new Color(250, 250, 250));
    loadText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    loadText.setFont(new Font("Monospace", Font.BOLD, 20));
    loadText.setBounds(20, 150, 130, 40);
    loadText.setText("Path of map: ");
    loadText.setEditable(false);
    loadTextField.setBackground(new Color(250, 250, 250));
    loadTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    loadTextField.setBounds(150, 150, 230, 40);
    loadTextField.getText();
    loadTextField.addActionListener(Controller::LoadMapAction);

    playerCountText.setBackground(new Color(50, 50, 50));
    playerCountText.setForeground(new Color(250, 250, 250));
    playerCountText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    playerCountText.setFont(new Font("Monospace", Font.BOLD, 20));
    playerCountText.setBounds(20, 210, 300, 40);
    playerCountText.setText("Number of players in one team: ");
    playerCountText.setEditable(false);
    playerCountTextField.setBackground(new Color(250, 250, 250));
    playerCountTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    playerCountTextField.setBounds(330, 210, 50, 40);
    playerCountTextField.getText();

    plumberNamesText.setBackground(new Color(50, 50, 50));
    plumberNamesText.setForeground(new Color(250, 250, 250));
    plumberNamesText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    plumberNamesText.setFont(new Font("Monospace", Font.BOLD, 20));
    plumberNamesText.setBounds(20, 270, 240, 40);
    plumberNamesText.setText("Names of the plumbers:");
    plumberNamesText.setEditable(false);
    plumberNamesTextField.setBackground(new Color(250, 250, 250));
    plumberNamesTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    plumberNamesTextField.setBounds(260, 270, 100, 40);
    plumberNamesTextField.getText();

    nomadNamesText.setBackground(new Color(50, 50, 50));
    nomadNamesText.setForeground(new Color(250, 250, 250));
    nomadNamesText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    nomadNamesText.setFont(new Font("Monospace", Font.BOLD, 20));
    nomadNamesText.setBounds(20, 330, 240, 40);
    nomadNamesText.setText("Names of the nomads:");
    nomadNamesText.setEditable(false);
    nomadNamesTextField.setBackground(new Color(250, 250, 250));
    nomadNamesTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    nomadNamesTextField.setBounds(260, 330, 100, 40);
    nomadNamesTextField.getText();

    exitButton.setBounds(55, 400, 300, 40);
    exitButton.setFont(new Font("Monospace", Font.BOLD, 25));
    exitButton.setText("Exit game");
    exitButton.setBackground(new Color(0, 0, 0));
    exitButton.setForeground(new Color(250, 250, 250));
    exitButton.setFocusable(false);
    exitButton.addActionListener(Controller::MenuExitAction);

    frame.add(title);
    frame.add(startButton);
    frame.add(loadText);
    frame.add(loadTextField);
    frame.add(plumberNamesText);
    frame.add(plumberNamesTextField);
    frame.add(nomadNamesText);
    frame.add(nomadNamesTextField);
    frame.add(playerCountText);
    frame.add(playerCountTextField);
    frame.add(exitButton);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(420, 500);
    frame.getContentPane().setBackground(new Color(50, 50, 50));
    frame.setTitle("Game Menu");
    frame.setResizable(false);
    frame.setLayout(null);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
