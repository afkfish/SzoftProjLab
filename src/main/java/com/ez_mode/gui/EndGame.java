package com.ez_mode.gui;

import java.awt.*;
import javax.swing.*;

/**
 * This class is used for the menu that is seen when the 'End Game' button is pressed. It presents
 * the option to save the map as it is, to start a new game, and to just close the game.
 */
public class EndGame {
  static JFrame frame = new JFrame();
  JTextField title = new JTextField();
  JTextField winnerText = new JTextField();
  JTextField saveText = new JTextField();
  static JTextField saveTextField = new JTextField();
  JButton exitButton = new JButton();
  static String savedPath;
  String winner = "It's a draw!";

  public EndGame() {
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    title.setFont(new Font("Monospace", Font.BOLD, 45));
    title.setBounds(140, 10, 300, 40);
    title.setText("Menu");
    title.setEditable(false);

    winnerText.setBackground(new Color(50, 50, 50));
    winnerText.setForeground(new Color(250, 250, 250));
    winnerText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    winnerText.setFont(new Font("Monospace", Font.BOLD, 20));
    winnerText.setBounds(55, 100, 300, 40);
    winnerText.setText("The winner team is: " + winner);
    winnerText.setEditable(false);

    saveText.setBackground(new Color(50, 50, 50));
    saveText.setForeground(new Color(250, 250, 250));
    saveText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    saveText.setFont(new Font("Monospace", Font.BOLD, 20));
    saveText.setBounds(55, 180, 450, 40);
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
    frame.add(winnerText);
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
