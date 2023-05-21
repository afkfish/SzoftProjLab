package com.ez_mode.gui;

import java.awt.*;
import javax.swing.*;

public class Menu {
  static JFrame frame = new JFrame();
  JButton startButton = new JButton();
  JTextField title = new JTextField();
  Controller controller = new Controller();

  public JFrame getFrame() {
    return frame;
  }

  public JButton getStartButton() {
    return startButton;
  }

  public Menu() {
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    title.setFont(new Font("Monospace", Font.BOLD, 45));
    title.setBounds(140, 30, 300, 40);
    title.setText("Menu");
    title.setEditable(false);

    startButton.setBounds(55, 150, 300, 40);
    startButton.setFont(new Font("Monospace", Font.BOLD, 25));
    startButton.setText("Start game");
    startButton.setBackground(new Color(0, 0, 0));
    startButton.setForeground(new Color(50, 50, 50));
    startButton.setFocusable(false);
    startButton.addActionListener(Controller::MenuStartAction);

    frame.add(title);
    frame.add(startButton);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(420, 420);
    frame.getContentPane().setBackground(new Color(50, 50, 50));
    frame.setTitle("Game Menu");
    frame.setResizable(false);
    frame.setLayout(null);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }
}
