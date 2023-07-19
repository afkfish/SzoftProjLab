package com.ez_mode.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class Menu {
  /** Java Swing components for the Menu class */
  private static final JFrame frame = new JFrame();
  public static JTextField loadTextField = new JTextField();
  public static JTextField plumberNamesTextField = new JTextField();
  public static JTextField nomadNamesTextField = new JTextField();
  public static JTextField playerCountTextField = new JTextField();
  public static int playerCount = 2;
  public static String loadedPath;
  
  public static void open() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBackground(new Color(50, 50, 50));

    JMenu fileMenu = new JMenu("File");
    fileMenu.setBackground(new Color(50, 50, 50));
    JMenuItem importMenuItem = new JMenuItem("Import");
    JSeparator separator = new JSeparator();
    JMenuItem exitMenuItem = new JMenuItem("Exit");
    exitMenuItem.addActionListener(actionEvent -> dispose());
    fileMenu.add(importMenuItem);
    fileMenu.add(separator);
    fileMenu.add(exitMenuItem);
    menuBar.add(fileMenu);

    frame.add(menuBar, BorderLayout.NORTH);

    JLabel title = new JLabel();
    title.setBackground(new Color(50, 50, 50));
    title.setForeground(new Color(250, 250, 250));
    title.setBorder(null);
    title.setFont(new Font("Monospace", Font.BOLD, 45));
//    title.setBounds(140, 10, 300, 40);
    title.setText("Menu");
    title.setHorizontalAlignment(SwingConstants.CENTER);

    JButton startButton = new JButton();
//    startButton.setBounds(55, 80, 300, 40);
    startButton.setFont(new Font("Monospace", Font.BOLD, 25));
    startButton.setText("Start game");
    startButton.setUI(new ColoredButtonUI(Color.BLACK));
    startButton.setForeground(new Color(250, 250, 250));
    startButton.setFocusable(false);
    startButton.addActionListener(Controller::menuStartAction);

    JLabel loadText = new JLabel();
    loadText.setBackground(new Color(50, 50, 50));
    loadText.setForeground(new Color(250, 250, 250));
    loadText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    loadText.setFont(new Font("Monospace", Font.BOLD, 20));
    loadText.setBounds(20, 150, 130, 40);
    loadText.setText("Path of map: ");
    loadTextField.setBackground(new Color(250, 250, 250));
    loadTextField.setFont(new Font("Monospace", Font.BOLD, 20));
//    loadTextField.setBounds(frame.getWidth(), 150, frame.getWidth()/3, 20);
    loadTextField.addActionListener(Controller::loadMapAction);

//    loadTextField.setMaximumSize(new Dimension((int) (frame.getWidth() / 3), loadTextField.getHeight()));
//    loadTextField.setSize(frame.getWidth() / 3, loadTextField.getHeight());

    JLabel playerCountText = new JLabel();
    playerCountText.setBackground(new Color(50, 50, 50));
    playerCountText.setForeground(new Color(250, 250, 250));
    playerCountText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    playerCountText.setFont(new Font("Monospace", Font.BOLD, 20));
    playerCountText.setBounds(20, 210, 300, 40);
    playerCountText.setText("Number of players in one team: ");
    playerCountTextField.setBackground(new Color(250, 250, 250));
    playerCountTextField.setFont(new Font("Monospace", Font.BOLD, 20));
//    playerCountTextField.setBounds(330, 210, 50, 40);

    JSlider slider = new JSlider(1, 4, 2);
    slider.setBackground(null);
    slider.setSnapToTicks(true);
    slider.addChangeListener(c -> playerCount = slider.getValue());

    JLabel plumberNamesText = new JLabel();
    plumberNamesText.setBackground(new Color(50, 50, 50));
    plumberNamesText.setForeground(new Color(250, 250, 250));
    plumberNamesText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    plumberNamesText.setFont(new Font("Monospace", Font.BOLD, 20));
    plumberNamesText.setBounds(20, 270, 240, 40);
    plumberNamesText.setText("Names of the plumbers:");
    plumberNamesTextField.setBackground(new Color(250, 250, 250));
    plumberNamesTextField.setFont(new Font("Monospace", Font.BOLD, 20));
//    plumberNamesTextField.setBounds(260, 270, 100, 40);

    JLabel nomadNamesText = new JLabel();
    nomadNamesText.setBackground(new Color(50, 50, 50));
    nomadNamesText.setForeground(new Color(250, 250, 250));
    nomadNamesText.setBorder(javax.swing.BorderFactory.createEmptyBorder());
    nomadNamesText.setFont(new Font("Monospace", Font.BOLD, 20));
    nomadNamesText.setBounds(20, 330, 240, 40);
    nomadNamesText.setText("Names of the nomads:");
    nomadNamesTextField.setBackground(new Color(250, 250, 250));
    nomadNamesTextField.setFont(new Font("Monospace", Font.BOLD, 20));
//    nomadNamesTextField.setBounds(260, 330, 100, 40);

//    JButton exitButton = new JButton();
////    exitButton.setBounds(55, 400, 300, 40);
//    exitButton.setFont(new Font("Monospace", Font.BOLD, 25));
//    exitButton.setText("Exit game");
//    exitButton.setUI(new ColoredButtonUI(Color.BLACK));
//    exitButton.setForeground(new Color(250, 250, 250));
//    exitButton.setFocusable(false);
//    exitButton.addActionListener(Controller::menuExitAction);

    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(420, 500);
    frame.getContentPane().setBackground(new Color(50, 50, 50));
    frame.setTitle("Game Menu");
    frame.setResizable(false);
    frame.setLocationRelativeTo(null);

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(3, 1));
    mainPanel.setBackground(null);

    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BorderLayout());
    titlePanel.add(title, BorderLayout.CENTER);
    titlePanel.setBackground(null);

    mainPanel.add(titlePanel);

    JPanel startButtonPanel = new JPanel();
    startButtonPanel.setLayout(new BorderLayout());
    startButtonPanel.add(startButton, BorderLayout.CENTER);
    startButtonPanel.setBackground(null);

    mainPanel.add(startButtonPanel);

    JPanel mainActionPanel = new JPanel();
    mainActionPanel.setLayout(new GridLayout(4, 2));
    mainActionPanel.add(loadText);
    mainActionPanel.add(loadTextField);
    mainActionPanel.add(playerCountText);
    mainActionPanel.add(slider);
    mainActionPanel.add(plumberNamesText);
    mainActionPanel.add(plumberNamesTextField);
    mainActionPanel.add(nomadNamesText);
    mainActionPanel.add(nomadNamesTextField);
    mainActionPanel.setBackground(null);

    mainPanel.add(mainActionPanel);

//    JPanel exitButtonPanel = new JPanel();
//    exitButtonPanel.setLayout(new BorderLayout());
//    exitButtonPanel.add(exitButton, BorderLayout.CENTER);
//    exitButtonPanel.setBackground(null);

//    mainPanel.add(exitButtonPanel);
    frame.add(mainPanel);

    frame.setVisible(true);
  }

  public static void dispose() {
    frame.dispose();
  }

  private static class ColoredButtonUI extends BasicButtonUI {
    private final Color color;
    public ColoredButtonUI(Color color) {
      super();
      this.color = color;
    }

    @Override
    public void update(Graphics g, JComponent c) {
      super.update(g, c);
      if (c.isOpaque()) {
        c.setBackground(color);
      }
      paint(g, c);
    }
  }
}
