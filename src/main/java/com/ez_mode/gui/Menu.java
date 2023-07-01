package com.ez_mode.gui;

import java.awt.*;
import javax.swing.*;

public class Menu {
  /** Java Swing components for the Menu class */
  private static final JFrame frame = new JFrame();
  public static JTextField loadTextField = new JTextField();
  public static JTextField plumberNamesTextField = new JTextField();
  public static JTextField nomadNamesTextField = new JTextField();
  public static JTextField playerCountTextField = new JTextField();
  public static int playerCount;
  public static String loadedPath;

  public Menu() {
    JLabel title = new JLabel();
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    title.setFont(new Font("Monospace", Font.BOLD, 45));
    title.setBounds(140, 10, 300, 40);
    title.setText("Menu");

    JButton startButton = new JButton();
    startButton.setBounds(55, 80, 300, 40);
    startButton.setFont(new Font("Monospace", Font.BOLD, 25));
    startButton.setText("Start game");
    startButton.setBackground(new Color(0, 0, 0));
    startButton.setForeground(new Color(250, 250, 250));
    startButton.setFocusable(false);
    startButton.addActionListener(Controller::MenuStartAction);

    JLabel loadText = new JLabel();
    loadText.setBackground(new Color(50, 50, 50));
    loadText.setForeground(new Color(250, 250, 250));
    loadText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    loadText.setFont(new Font("Monospace", Font.BOLD, 20));
    loadText.setBounds(20, 150, 130, 40);
    loadText.setText("Path of map: ");
    loadTextField.setBackground(new Color(250, 250, 250));
    loadTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    loadTextField.setBounds(150, 150, 230, 40);
    loadTextField.addActionListener(Controller::LoadMapAction);

    JLabel playerCountText = new JLabel();
    playerCountText.setBackground(new Color(50, 50, 50));
    playerCountText.setForeground(new Color(250, 250, 250));
    playerCountText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    playerCountText.setFont(new Font("Monospace", Font.BOLD, 20));
    playerCountText.setBounds(20, 210, 300, 40);
    playerCountText.setText("Number of players in one team: ");
    playerCountTextField.setBackground(new Color(250, 250, 250));
    playerCountTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    playerCountTextField.setBounds(330, 210, 50, 40);

    JLabel plumberNamesText = new JLabel();
    plumberNamesText.setBackground(new Color(50, 50, 50));
    plumberNamesText.setForeground(new Color(250, 250, 250));
    plumberNamesText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    plumberNamesText.setFont(new Font("Monospace", Font.BOLD, 20));
    plumberNamesText.setBounds(20, 270, 240, 40);
    plumberNamesText.setText("Names of the plumbers:");
    plumberNamesTextField.setBackground(new Color(250, 250, 250));
    plumberNamesTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    plumberNamesTextField.setBounds(260, 270, 100, 40);

    JLabel nomadNamesText = new JLabel();
    nomadNamesText.setBackground(new Color(50, 50, 50));
    nomadNamesText.setForeground(new Color(250, 250, 250));
    nomadNamesText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    nomadNamesText.setFont(new Font("Monospace", Font.BOLD, 20));
    nomadNamesText.setBounds(20, 330, 240, 40);
    nomadNamesText.setText("Names of the nomads:");
    nomadNamesTextField.setBackground(new Color(250, 250, 250));
    nomadNamesTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    nomadNamesTextField.setBounds(260, 330, 100, 40);

    JButton exitButton = new JButton();
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

  public static void setPlayerCount(int playerCount) {
    Menu.playerCount = playerCount;
  }

  public static void dispose() {
    frame.dispose();
  }
}
