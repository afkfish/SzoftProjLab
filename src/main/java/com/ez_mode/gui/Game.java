package com.ez_mode.gui;

import static com.ez_mode.Main.map;
import static com.ez_mode.utils.ImageUtil.*;

import com.ez_mode.Main;
import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.objects.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;

public class Game {
  public static int gridNum = 10;
  public static ArrayList<String> plumberNames;
  public static ArrayList<String> nomadNames;
  public static ArrayList<String> playerNames;
  public static LinkedList<Character> players = new LinkedList<>();
  private static int buttonSize;
  private static Game game;

  /** Java Swing components for the Game class */
  private final JFrame frame = new JFrame();

  private final JLabel textField = new JLabel();
  private final JButton[] mapButtons = new JButton[gridNum * gridNum];
  private final JButton[] actionButtons = new JButton[gridNum];

  public Game() {
    game = this;
    loadImageCache();
    // Properties of the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Game");
    frame.setResizable(false);

    int windowWidth = 70 * gridNum;
    int windowHeight = 70 * (gridNum + 2) + 20;
    buttonSize = windowWidth / gridNum;

    JPanel titlePanel = new JPanel();
    titlePanel.setLayout(new BorderLayout());
    titlePanel.setBackground(Color.BLACK);

    // Properties of the text field
    textField.setBackground(Color.BLACK);
    textField.setForeground(Color.WHITE);
    textField.setFont(new Font("Monospace", Font.BOLD, 50));
    textField.setHorizontalAlignment(JLabel.CENTER);
    textField.setText("Plumbers turn");
    textField.setOpaque(true);

    // Properties of the end game button
    JButton endGameButton = new JButton();
    endGameButton.setBounds(500, 13, 20, 30);
    endGameButton.setFont(new Font("Monospace", Font.BOLD, 20));
    endGameButton.setText("End Game");
    // endGameButton.setBackground(Color.WHITE);
    endGameButton.setForeground(Color.RED);
    endGameButton.setFocusable(false);
    endGameButton.setOpaque(false);
    endGameButton.setContentAreaFilled(false);
    endGameButton.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
    endGameButton.addActionListener(Controller::gameExitAction);

    // Filling the map
    JPanel mapPanel = new JPanel();
    mapPanel.setLayout(new GridLayout(gridNum, gridNum));
    mapPanel.setBackground(Color.BLACK);
    mapPanel.setBorder(null);

    JPanel actionPanel = new JPanel();
    actionPanel.setLayout(new GridLayout(1, gridNum));
    actionPanel.setBackground(Color.WHITE);
    actionPanel.setBorder(null);

    for (int i = 0; i < gridNum; i++) {
      for (int j = 0; j < gridNum; j++) {
        Node temp = Map.getNode(j, i);
        JButton button = mapButtons[i * gridNum + j] = new JButton();
        mapPanel.add(button);
        button.setFocusable(false);
        button.setSize(buttonSize, buttonSize);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorder(null);
        button.setHorizontalAlignment(JLabel.HORIZONTAL);
        updateNodeImage(temp, i * gridNum + j, null);
      }
    }
    updatePlayerNodes();

    for (int i = 0; i < gridNum; i++) {
      JButton button = actionButtons[i] = new JButton();
      actionPanel.add(button);
      button.setFocusable(false);
      button.setSize(buttonSize, buttonSize);
      button.setBorderPainted(false);
      button.setOpaque(false);
      button.setContentAreaFilled(false);
      button.setBorder(null);
      button.setHorizontalAlignment(JLabel.HORIZONTAL);
    }

    int i = 0;
    Image moveupImage = getImage("moveup", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(moveupImage));
    actionButtons[i++].addActionListener(Controller::moveUpAction);

    Image moveleftImage = getImage("moveleft", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(moveleftImage));
    actionButtons[i++].addActionListener(Controller::moveLeftAction);

    Image movedownImage = getImage("movedown", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(movedownImage));
    actionButtons[i++].addActionListener(Controller::moveDownAction);

    Image moverightImage = getImage("moveright", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(moverightImage));
    actionButtons[i++].addActionListener(Controller::moveRightAction);

    Image breakImage = getImage("break", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(breakImage));
    actionButtons[i++].addActionListener(Controller::breakAction);

    Image repairImage = getImage("repair", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(repairImage));
    actionButtons[i++].addActionListener(Controller::characterSpecAction);

    Image stickypipeImage = getImage("sticky", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(stickypipeImage));
    actionButtons[i++].addActionListener(Controller::stickyAction);

    Image setpumpImage = getImage("setpump", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(setpumpImage));
    actionButtons[i++].addActionListener(Controller::setPumpAction);

    Image pickuppipeImage = getImage("pickuppipe", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(pickuppipeImage));
    actionButtons[i++].addActionListener(Controller::pickUpPipeAction);

    Image pickuppumpImage = getImage("pickuppump", buttonSize);
    actionButtons[i].setIcon(new ImageIcon(pickuppumpImage));
    actionButtons[i].addActionListener(Controller::pickUpPumpAction);

    Character firstPlayer = players.removeFirst();
    players.addLast(firstPlayer);
    if (firstPlayer instanceof Nomad) {
      firstPlayer = players.removeFirst();
      players.addLast(firstPlayer);
    }
    Controller.currentPlayer = firstPlayer;

    // Adding the components to the frame and setting their layouts
    textField.setText(firstPlayer.getName() + "'s turn (Plumber)");
    // playerIdx++;
    frame.add(titlePanel, BorderLayout.NORTH);
    titlePanel.add(textField);
    titlePanel.add(endGameButton, BorderLayout.EAST);
    frame.add(mapPanel);
    frame.add(actionPanel, BorderLayout.SOUTH);
    frame.pack();
    frame.setSize(windowWidth, windowHeight);
    frame.setLocationRelativeTo(null);
    frame.setVisible(true);
  }

  private static void updateFlow() {
    // checks the flowrate for every node, if it's > 0, sets the node to watery
    for (int i = 0; i < gridNum - 1; i++) {
      for (int j = 0; j < gridNum; j++) {
        Node tempNode = Map.getNode(j, i);
        updateNodeImage(tempNode, i * gridNum + j, null);
      }
    }
    updatePlayerNodes();
    map.tick();
  }

  /** The character movement in the gui, with the correct images */
  public static void moveCharacter(Node from, Node to) {
    // calculate the index of the node that the character is standing on
    int idx = to.getX() + (gridNum * to.getY());
    int prevIdx = from.getX() + (gridNum * from.getY());

    updateNodeImage(from, prevIdx, null);
    updateNodeImage(to, idx, Controller.currentPlayer);
    // updatePlayerNodes();
  }

  private static void updateNodeImage(Node node, int idx, Character character) {
    Image nodeImage;
    String nodeType = getNodeType(node);

    nodeImage = combine(nodeType, getPlayerType(character)).scale(buttonSize);

    if (nodeType.equals("pipe") || nodeType.equals("waterpipe")) {
      Pipe pipe = (Pipe) node;

      if (pipe.isBroken()) {
        nodeImage = combine("brokenpipe", getPlayerType(character)).scale(buttonSize);

      } else if (pipe.isSticky()) {
        Image stickyPipe = combine(nodeType, "sticky").scale(buttonSize);
        nodeImage = combine(stickyPipe, getPlayerType(character)).scale(buttonSize);

      } else if (pipe.isSlippery()) {
        Image slipperyPipe = combine(nodeType, "slippery").scale(buttonSize);
        nodeImage = combine(slipperyPipe, getPlayerType(character)).scale(buttonSize);
      }
    }

    game.mapButtons[idx].setIcon(new ImageIcon(nodeImage));
  }

  /** The current character's break action in the gui, with the correct images */
  public static void breakNode(Node node, Character character) {
    int idx = node.getX() + (gridNum * node.getY());

    // only the Pipe nodes can be broken by both characters
    if (node instanceof Pipe) {
      updateNodeImage(node, idx, character);
    }

    Main.log("Target of break is not a pipe");
  }

  public static void setSticky(Node node) {
    int idx = node.getX() + (gridNum * node.getY());

    // only the Pipe nodes can be made sticky by both characters
    if (node instanceof Pipe) {
      updateNodeImage(node, idx, Controller.currentPlayer);
      return;
    }

    Main.log("Target of sticky is not a pipe");
  }

  public static void setSlippery(Node node, Character character) {
    int idx = node.getX() + (gridNum * node.getY());

    // only the Pipe nodes can be made slippery by nomad characters
    if (character instanceof Nomad && node instanceof Pipe) {
      updateNodeImage(node, idx, character);
      return;
    }

    Main.log("Target of slippery is not a pipe or character is not a nomad");
  }

  public static void repairNode(Node node, Character character) {
    int idx = node.getX() + (gridNum * node.getY());

    // only the Pump and Pipe nodes can be repaired by plumbers
    if (character instanceof Plumber && node instanceof Pump) {
      updateNodeImage(node, idx, character);
      return;
    }

    Main.log("Repair failed: character is not a plumber");
  }

  public static void updateField(Node node) {
    int idx = node.getX() + (gridNum * node.getY());

    updateNodeImage(node, idx, null);
    updatePlayerNodes();
  }

  /** The getter for the current character's type */
  private static String getPlayerType(Character character) {
    if (character instanceof Nomad) return "nomad";
    else if (character instanceof Plumber) return "plumber";
    else return null;
  }

  /** The getter for the current node's type */
  private static String getNodeType(Node node) {
    if (node instanceof Cistern) return "cistern";
    else if (node instanceof Pipe tempPipe) {
      if (tempPipe.getFlowRate() == 0) return "pipe";
      else return "waterpipe";
    } else if (node instanceof Pump tempPump) {
      if (tempPump.getFlowRate() == 0) return "pump";
      else return "waterpump";
    } else if (node instanceof WaterSpring) return "waterspring";
    else return "sand";
  }

  private static void updatePlayerNodes() {
    try {
      for (Character character : Map.getPlayers()) {
        Node temp = character.getStandingOn();
        int coord = temp.getX() + (gridNum * temp.getY());

        updateNodeImage(temp, coord, character);
      }
    } catch (NullPointerException ignored) {
    }
  }

  public void dispose() {
    frame.dispose();
  }

  /**
   * After one team's turn the action bar will be updated to suit for the next team's actions The
   * mutual actions are the movements, making the pipe sticky or breaking a pipe The plumbers' team
   * can repair, set pump, pick up pump and pick up pipe The nomad's team can break, make a pipe
   * slippery
   */
  public void updateAction() {
    Character currentPlayer = players.removeFirst();
    players.addLast(currentPlayer);

    Controller.currentPlayer = currentPlayer;
    Controller.currentNode = currentPlayer.getStandingOn();

    if (currentPlayer instanceof Nomad) {
      int i = 5;
      Image slipperyImage = getImage("slippery", buttonSize);
      actionButtons[i++].setIcon(new ImageIcon(slipperyImage));
      i++; // skip the sticky button
      i++; // skip the setpump button
      ImageIcon transparent = new ImageIcon(getTransparent().scale(buttonSize));
      actionButtons[i].setEnabled(false);
      actionButtons[i++].setIcon(transparent);
      actionButtons[i].setEnabled(false);
      actionButtons[i].setIcon(transparent);

      textField.setText(currentPlayer.getName() + "'s turn (Nomad)");
    } else {
      int i = 5;
      Image repairImage = getImage("repair", buttonSize);
      actionButtons[i++].setIcon(new ImageIcon(repairImage));

      i++; // skip the sticky button

      Image setpumpImage = getImage("setpump", buttonSize);
      actionButtons[i++].setIcon(new ImageIcon(setpumpImage));

      Image pickuppipeImage = getImage("pickuppipe", buttonSize);
      actionButtons[i].setEnabled(true);
      actionButtons[i++].setIcon(new ImageIcon(pickuppipeImage));

      Image pickuppumpImage = getImage("pickuppump", buttonSize);
      actionButtons[i].setEnabled(true);
      actionButtons[i].setIcon(new ImageIcon(pickuppumpImage));

      textField.setText(currentPlayer.getName() + "'s turn (Plumber)");
    }

    updateFlow();
  }
}
