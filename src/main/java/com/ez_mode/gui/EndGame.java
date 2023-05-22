package com.ez_mode.gui;

import java.awt.*;
import javax.swing.*;

public class EndGame {
  static JFrame frame = new JFrame();
  JButton startButton = new JButton();
  JTextField title = new JTextField();
  JTextField saveText = new JTextField();
  static JTextField saveTextField = new JTextField();
  JButton exitButton = new JButton();
  static String savedPath;

  public EndGame() {
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    title.setFont(new Font("Monospace", Font.BOLD, 45));
    title.setBounds(140, 10, 300, 40);
    title.setText("Menu");
    title.setEditable(false);

    startButton.setBounds(55, 80, 300, 40);
    startButton.setFont(new Font("Monospace", Font.BOLD, 25));
    startButton.setText("New game");
    startButton.setBackground(new Color(0, 0, 0));
    startButton.setForeground(new Color(250, 250, 250));
    startButton.setFocusable(false);
    startButton.addActionListener(Controller::OpenMenuAction);

    saveText.setBackground(new Color(50, 50, 50));
    saveText.setForeground(new Color(250, 250, 250));
    saveText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    saveText.setFont(new Font("Monospace", Font.BOLD, 20));
    saveText.setBounds(55, 180, 300, 40);
    saveText.setText("Path to save the map: ");
    saveText.setEditable(false);

    saveTextField.setBackground(new Color(250, 250, 250));
    saveTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    saveTextField.setBounds(55, 240, 300, 40);
    saveTextField.getText();
    saveTextField.addActionListener(Controller::LoadMapAction);

    exitButton.setBounds(55, 380, 300, 40);
    exitButton.setFont(new Font("Monospace", Font.BOLD, 25));
    exitButton.setText("Exit game");
    exitButton.setBackground(new Color(0, 0, 0));
    exitButton.setForeground(new Color(250, 250, 250));
    exitButton.setFocusable(false);
    exitButton.addActionListener(Controller::EndGameExitAction);

    frame.add(title);
    frame.add(startButton);
    frame.add(saveText);
    frame.add(saveTextField);
    frame.add(exitButton);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(420, 500);
    frame.getContentPane().setBackground(new Color(50, 50, 50));
    frame.setTitle("End Game");
    frame.setResizable(false);
    frame.setLayout(null);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);

  }
}
