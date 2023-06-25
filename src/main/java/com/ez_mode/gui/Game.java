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
import javax.swing.*;

public class Game {
  public static int gridNum = 10;
  public static boolean nomadTurn = false;
  public static ArrayList<String> plumberNames;
  public static ArrayList<String> nomadNames;
  static int windowWidth = 70 * 10;
  static int fieldSize = windowWidth / gridNum;
  static int actionSize = fieldSize;
  static ArrayList<String> playerNames;
  static int playerIdx = 0;

  /** Java Swing components for the Game class */
  static JFrame frame = new JFrame();

  int windowHeight = 70 * 12 + 10;
  JPanel titlePanel = new JPanel();
  JLabel textField = new JLabel();
  JButton endGameButton = new JButton();
  JPanel mapPanel = new JPanel();
  JButton[] mapButtons = new JButton[gridNum * gridNum];
  JButton[] actionButtons = new JButton[gridNum];
  JPanel actionPanel = new JPanel();

  public Game() {
    playerIdx = 0;
    // Properties of the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Game");
    frame.setResizable(false);

    titlePanel.setLayout(new BorderLayout());

    // Properties of the text field
    textField.setBackground(Color.BLACK);
    textField.setForeground(Color.WHITE);
    textField.setFont(new Font("Monospace", Font.BOLD, 50));
    textField.setHorizontalAlignment(JLabel.CENTER);
    textField.setText("Plumbers turn");
    textField.setOpaque(true);

    // Properties of the end game button
    endGameButton.setBounds(500, 13, 20, 30);
    endGameButton.setFont(new Font("Monospace", Font.BOLD, 20));
    endGameButton.setText("End Game");
    endGameButton.setBackground(Color.WHITE);
    endGameButton.setForeground(Color.BLACK);
    endGameButton.setFocusable(false);
    endGameButton.addActionListener(Controller::GameExitAction);

    // Filling the map
    mapPanel.setLayout(new GridLayout(gridNum, gridNum));
    mapPanel.setBorder(null);

    actionPanel.setLayout(new GridLayout(1, gridNum));
    actionPanel.setBorder(null);

    for (int i = 0; i < gridNum; i++) {
      for (int j = 0; j < gridNum; j++) {
        Node temp = Map.getNode(j, i);
        mapButtons[i * gridNum + j] = new JButton();
        mapPanel.add(mapButtons[i * gridNum + j]);
        mapButtons[i * gridNum + j].setFocusable(false);
        mapButtons[i * gridNum + j].setSize(fieldSize, fieldSize);
        mapButtons[i * gridNum + j].setBorderPainted(false);
        mapButtons[i * gridNum + j].setHorizontalAlignment(JLabel.HORIZONTAL);
        updateNodeImage(temp, i * gridNum + j, null);
      }
    }
    updatePlayerNodes();

    for (int i = 0; i < gridNum; i++) {
      JButton button = actionButtons[i] = new JButton();
      actionPanel.add(button);
      button.setFocusable(false);
      button.setSize(fieldSize, fieldSize);
      button.setBorderPainted(false);
      button.setHorizontalAlignment(JLabel.HORIZONTAL);
    }

    int i = 0;
    Image moveupImage = getImage("moveup", actionSize);
    actionButtons[i].setIcon(new ImageIcon(moveupImage));
    actionButtons[i++].addActionListener(Controller::MoveUpAction);

    Image moveleftImage = getImage("moveleft", actionSize);
    actionButtons[i].setIcon(new ImageIcon(moveleftImage));
    actionButtons[i++].addActionListener(Controller::MoveLeftAction);

    Image movedownImage = getImage("movedown", actionSize);
    actionButtons[i].setIcon(new ImageIcon(movedownImage));
    actionButtons[i++].addActionListener(Controller::MoveDownAction);

    Image moverightImage = getImage("moveright", actionSize);
    actionButtons[i].setIcon(new ImageIcon(moverightImage));
    actionButtons[i++].addActionListener(Controller::MoveRightAction);

    Image breakImage = getImage("break", actionSize);
    actionButtons[i].setIcon(new ImageIcon(breakImage));
    actionButtons[i++].addActionListener(Controller::BreakAction);

    Image repairImage = getImage("repair", actionSize);
    actionButtons[i].setIcon(new ImageIcon(repairImage));
    actionButtons[i++].addActionListener(Controller::CharacterSpecAction);

    Image stickypipeImage = getImage("sticky", actionSize);
    actionButtons[i].setIcon(new ImageIcon(stickypipeImage));
    actionButtons[i++].addActionListener(Controller::StickyAction);

    Image setpumpImage = getImage("setpump", actionSize);
    actionButtons[i].setIcon(new ImageIcon(setpumpImage));
    actionButtons[i++].addActionListener(Controller::SetPumpAction);

    Image pickuppipeImage = getImage("pickuppipe", actionSize);
    actionButtons[i].setIcon(new ImageIcon(pickuppipeImage));
    actionButtons[i++].addActionListener(Controller::PickUpPipeAction);

    Image pickuppumpImage = getImage("pickuppump", actionSize);
    actionButtons[i].setIcon(new ImageIcon(pickuppumpImage));
    actionButtons[i].addActionListener(Controller::PickUpPumpAction);

    // Adding the components to the frame and setting their layouts
    textField.setText(plumberNames.get(playerIdx) + "'s turn (Plumber)");
    playerIdx++;
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

  /**
   * After one team's turn the action bar will be updated to suit for the next team's actions The
   * mutual actions are the movements, making the pipe sticky or breaking a pipe The plumbers' team
   * can repair, set pump, pick up pump and pick up pipe The nomad's team can break, make a pipe
   * slippery
   */
  public void updateAction() {
    playerIdx++;
    playerIdx = playerIdx % (Menu.playerCount * 2);

    Controller.tempChar = Map.getPlayer(Game.playerIdx);
    assert Controller.tempChar != null : "tempChar is null???";
    Controller.tempNode = Controller.tempChar.getStandingOn();

    if (nomadTurn) {
      int i = 5;
      Image slipperyImage = getImage("slippery", actionSize);
      actionButtons[i++].setIcon(new ImageIcon(slipperyImage));
      i++; // skip the sticky button
      i++; // skip the setpump button
      actionButtons[i++].setIcon(getTransparent());
      actionButtons[i].setIcon(getTransparent());

      textField.setText(nomadNames.get(playerIdx / 2) + "'s turn (Nomad)");
    } else {
      int i = 5;
      Image repairImage = getImage("repair", actionSize);
      actionButtons[i++].setIcon(new ImageIcon(repairImage));

      i++; // skip the sticky button

      Image setpumpImage = getImage("setpump", actionSize);
      actionButtons[i++].setIcon(new ImageIcon(setpumpImage));

      Image pickuppipeImage = getImage("pickuppipe", actionSize);
      actionButtons[i++].setIcon(new ImageIcon(pickuppipeImage));

      Image pickuppumpImage = getImage("pickuppump", actionSize);
      actionButtons[i].setIcon(new ImageIcon(pickuppumpImage));

      textField.setText(plumberNames.get(playerIdx / 2) + "'s turn (Plumber)");
    }

    updateFlow();
  }

  public void updateFlow() {
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
  public void moveCharacter() {
    // calculate the index of the node that the character is standing on
    int idx = Controller.tempNode.getX() + (gridNum * Controller.tempNode.getY());
    Controller.prevIdx = Controller.prevNode.getX() + (gridNum * Controller.prevNode.getY());

    updateNodeImage(Controller.tempNode, idx, Controller.tempChar);
    updateNodeImage(Controller.prevNode, Controller.prevIdx, null);
    updatePlayerNodes();
  }

  private void updateNodeImage(Node node, int idx, Character character) {
    Image nodeImage = getImage(getNodeType(node), actionSize);

    // if the player is standing on the node, add the player image to the node image
    Image characterI = getPlayerImage(character);
    if (characterI != null) {
      nodeImage = combine(nodeImage, characterI).scale(actionSize);
    }

    mapButtons[idx].setIcon(new ImageIcon(nodeImage));
  }

  /** The current character's break action in the gui, with the correct images */
  public void breakNode() {
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be broken by both characters
    try {
      Pipe ignored = (Pipe) Controller.tempNode;
      updateNodeImage(Controller.tempNode, idx, Controller.tempChar);
    } catch (Exception ignored) {
      Main.log("Target of break is not a pipe");
    }
  }

  public void setSticky() {
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be made sticky by both characters
    try {
      Pipe ignored = (Pipe) Controller.tempNode;
      updateNodeImage(Controller.tempNode, idx, Controller.tempChar);
    } catch (Exception ignored) {
      Main.log("Target of sticky is not a pipe");
    }
  }

  public void setSlippery() {
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be made slippery by nomad characters
    try {
      Nomad ignored1 = (Nomad) Controller.tempChar;
      Pipe ignored2 = (Pipe) Controller.tempNode;
      updateNodeImage(Controller.tempNode, idx, Controller.tempChar);
    } catch (Exception ignored) {
      Main.log("Target of slippery is not a pipe or character is not a nomad");
    }
  }

  public void repairNode() {
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be made slippery by nomad characters
    try {
      Plumber ignored1 = (Plumber) Controller.tempChar;
      updateNodeImage(Controller.tempNode, idx, Controller.tempChar);
    } catch (Exception ignored) {
      Main.log("Repair failed: character is not a plumber");
    }
  }

  public void UpdateField() {
    int idx = Controller.tempNode.getX() + (gridNum * Controller.tempNode.getY());
    System.out.println(idx);
    updateNodeImage(Controller.tempNode, idx, null);
    updatePlayerNodes();
  }

  /** The getter for the current character's type */
  private Image getPlayerImage(Character character) {
    if (character instanceof Nomad) return getImage("nomad", actionSize);
    else if (character instanceof Plumber) return getImage("plumber", actionSize);
    else return null;
  }

  /** The getter for the current node's type */
  private String getNodeType(Node node) {
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

  private void updatePlayerNodes() {
    try {
      for (Character character : Map.getPlayers()) {
        Node temp = character.getStandingOn();
        int coord = temp.getX() + (gridNum * temp.getY());

        updateNodeImage(temp, coord, character);
      }
    } catch (NullPointerException ignored) {
    }
  }
}
