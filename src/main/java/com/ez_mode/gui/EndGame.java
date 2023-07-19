package com.ez_mode.gui;

import com.ez_mode.Map;
import java.awt.*;
import javax.swing.*;

/**
 * This class is used for the menu that is seen when the 'End Game' button is pressed. It presents
 * the option to save the map as it is, to start a new game, and to just close the game.
 */
public class EndGame {
  private static final JFrame frame = new JFrame();
  private static final JTextField saveTextField = new JTextField();

  public static void show() {
    String winner = Map.waterArrived > Map.waterLost ? "Plumbers" : "Nomads";
    JLabel title = new JLabel();
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    title.setFont(new Font("Monospace", Font.BOLD, 45));
    title.setBounds(140, 10, 300, 40);
    title.setText("Menu");

    JLabel winnerText = new JLabel();
    winnerText.setBackground(new Color(50, 50, 50));
    winnerText.setForeground(new Color(250, 250, 250));
    winnerText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    winnerText.setFont(new Font("Monospace", Font.BOLD, 20));
    winnerText.setBounds(55, 100, 300, 40);
    winnerText.setText("The winner team is: " + winner);

    JLabel saveText = new JLabel();
    saveText.setBackground(new Color(50, 50, 50));
    saveText.setForeground(new Color(250, 250, 250));
    saveText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    saveText.setFont(new Font("Monospace", Font.BOLD, 20));
    saveText.setBounds(55, 180, 450, 40);
    saveText.setText("Path to save the map: ");

    saveTextField.setBackground(new Color(250, 250, 250));
    saveTextField.setFont(new Font("Monospace", Font.BOLD, 20));
    saveTextField.setBounds(55, 240, 300, 40);
    saveTextField.getText();
    saveTextField.addActionListener(Controller::loadMapAction);

    JButton exitButton = new JButton();
    exitButton.setBounds(55, 380, 300, 40);
    exitButton.setFont(new Font("Monospace", Font.BOLD, 25));
    exitButton.setText("Exit game");
    exitButton.setBackground(new Color(0, 0, 0));
    exitButton.setForeground(new Color(250, 250, 250));
    exitButton.setFocusable(false);
    exitButton.addActionListener(Controller::endGameExitAction);

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

  public static String getSavedPath() {
    return saveTextField.getText();
  }

  public static void dispose() {
    frame.dispose();
  }
}
