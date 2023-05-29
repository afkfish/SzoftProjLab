package com.ez_mode.gui;

import static com.ez_mode.Main.map;

import com.ez_mode.Main;
import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.objects.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

public class Game {
  public static int gridNum = 10;
  public static boolean nomadTurn = false;
  public static ArrayList<String> plumberNames;
  public static ArrayList<String> nomadNames;
  /** Adding all the images' path that will be used in the game */
  public static String pipeImagePath = "src/main/resources/pipe.png";
  public static String sandImagePath = "src/main/resources/sand.png";
  public static String plumberImagePath = "src/main/resources/transplumber.png";
  public static String nomadImagePath = "src/main/resources/transnomad.png";
  public static String waterspringImagePath = "src/main/resources/waterspring.png";
  public static String waterpumpImagePath = "src/main/resources/waterpump.png";
  public static String waterpipeImagePath = "src/main/resources/waterpipe.png";
  public static String stickypipeImagePath = "src/main/resources/stickypipe.png";
  public static String slipperypipeImagePath = "src/main/resources/slipperypipe.png";
  public static String repairImagePath = "src/main/resources/repair.png";
  public static String emptypumpImagePath = "src/main/resources/emptypump.png";
  public static String cisternImagePath = "src/main/resources/cistern.png";
  public static String brokenpumpImagePath = "src/main/resources/brokenpump.png";
  public static String brokenpipeImagePath = "src/main/resources/brokenpipe.png";
  public static String breakImagePath = "src/main/resources/break.png";
  public static String pickuppipeImagePath = "src/main/resources/pickuppipe.png";
  public static String pickuppumpImagePath = "src/main/resources/pickuppump.png";
  public static String setpumpImagePath = "src/main/resources/setpump.png";
  /** Adding all the images as ImageIcons, using the path given previously */
  public static ImageIcon pipeIcon = new ImageIcon(pipeImagePath);
  public static ImageIcon sandIcon = new ImageIcon(sandImagePath);
  public static ImageIcon waterspringIcon = new ImageIcon(waterspringImagePath);
  public static ImageIcon waterpumpIcon = new ImageIcon(waterpumpImagePath);
  public static ImageIcon waterpipeIcon = new ImageIcon(waterpipeImagePath);
  public static ImageIcon stickypipeIcon = new ImageIcon(stickypipeImagePath);
  public static ImageIcon slipperypipeIcon = new ImageIcon(slipperypipeImagePath);
  public static ImageIcon repairIcon = new ImageIcon(repairImagePath);
  public static ImageIcon emptypumpIcon = new ImageIcon(emptypumpImagePath);
  public static ImageIcon cisternIcon = new ImageIcon(cisternImagePath);
  public static ImageIcon brokenpumpIcon = new ImageIcon(brokenpumpImagePath);
  public static ImageIcon brokenpipeIcon = new ImageIcon(brokenpipeImagePath);
  public static ImageIcon breakIcon = new ImageIcon(breakImagePath);
  public static ImageIcon pickuppipeIcon = new ImageIcon(pickuppipeImagePath);
  public static ImageIcon pickuppumpIcon = new ImageIcon(pickuppumpImagePath);
  public static ImageIcon setpumpIcon = new ImageIcon(setpumpImagePath);
  static int windowWidth = 800 - 100;
  static int fieldSize = windowWidth / gridNum;
  static int actionSize = fieldSize - 10;
  static ArrayList<String> playerNames;
  static int playerIdx = 0;
  /** Java Swing components for the Game class */
  static JFrame frame = new JFrame();
  public String waterImagePath = "src/main/resources/water.png";
  public String moveupImagePath = "src/main/resources/moveup.png";
  public String moveleftImagePath = "src/main/resources/moveleft.png";
  public String movedownImagePath = "src/main/resources/movedown.png";
  public String moverightImagePath = "src/main/resources/moveright.png";
  public ImageIcon plumberIcon = new ImageIcon(plumberImagePath);
  public ImageIcon nomadIcon = new ImageIcon(nomadImagePath);
  public ImageIcon waterIcon = new ImageIcon(waterImagePath);
  public ImageIcon moveupIcon = new ImageIcon(moveupImagePath);
  public ImageIcon moveleftIcon = new ImageIcon(moveleftImagePath);
  public ImageIcon movedownIcon = new ImageIcon(movedownImagePath);
  public ImageIcon moverightIcon = new ImageIcon(moverightImagePath);
  int windowHeight = 800;
  JPanel titlePanel = new JPanel();
  JLabel textField = new JLabel();
  JButton endGameButton = new JButton();
  JPanel mapPanel = new JPanel();
  JButton[] mapButtons = new JButton[gridNum * gridNum + 1];
  JPanel actionPanel = new JPanel();

  public Game() {
    playerIdx = 0;
    // Properties of the frame
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Game");
    frame.setResizable(false);

    titlePanel.setLayout(new BorderLayout());

    // Properties of the text field
    textField.setBackground(new Color(0, 0, 0));
    textField.setForeground(new Color(230, 230, 230));
    textField.setFont(new Font("Monospace", Font.BOLD, 50));
    textField.setHorizontalAlignment(JLabel.CENTER);
    textField.setText("Plumbers turn");
    textField.setOpaque(true);

    // Properties of the end game button
    endGameButton.setBounds(500, 13, 20, 30);
    endGameButton.setFont(new Font("Monospace", Font.BOLD, 20));
    endGameButton.setText("End Game");
    endGameButton.setBackground(new Color(250, 250, 250));
    endGameButton.setForeground(new Color(0, 0, 0));
    endGameButton.setFocusable(false);
    endGameButton.addActionListener(Controller::GameExitAction);

    // Filling the map
    mapPanel.setLayout(new GridLayout(gridNum, gridNum + 1));
    mapPanel.setBorder(null);

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

    // Icons of the action bar
    for (int i = (gridNum * gridNum - gridNum); i < (gridNum * gridNum); i++) {
      mapButtons[i].setBackground(new Color(255, 255, 255));
    }

    Image moveupImage = moveupIcon.getImage();
    Image moveupModIcon =
        moveupImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum].setIcon(new ImageIcon(moveupModIcon));
    mapButtons[gridNum * gridNum - gridNum].addActionListener(Controller::MoveUpAction);

    Image moveleftImage = moveleftIcon.getImage();
    Image moveleftModIcon =
        moveleftImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 1].setIcon(new ImageIcon(moveleftModIcon));
    mapButtons[gridNum * gridNum - gridNum + 1].addActionListener(Controller::MoveLeftAction);

    Image movedownImage = movedownIcon.getImage();
    Image movedownModIcon =
        movedownImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 2].setIcon(new ImageIcon(movedownModIcon));
    mapButtons[gridNum * gridNum - gridNum + 2].addActionListener(Controller::MoveDownAction);

    Image moverightImage = moverightIcon.getImage();
    Image moverightModIcon =
        moverightImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 3].setIcon(new ImageIcon(moverightModIcon));
    mapButtons[gridNum * gridNum - gridNum + 3].addActionListener(Controller::MoveRightAction);

    Image breakImage = breakIcon.getImage();
    Image breakModIcon = breakImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 4].setIcon(new ImageIcon(breakModIcon));
    mapButtons[gridNum * gridNum - gridNum + 4].addActionListener(Controller::BreakAction);

    Image repairImage = repairIcon.getImage();
    Image repairModIcon =
        repairImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 5].setIcon(new ImageIcon(repairModIcon));
    mapButtons[gridNum * gridNum - gridNum + 5].addActionListener(Controller::CharacterSpecAction);

    Image stickypipeImage = stickypipeIcon.getImage();
    Image stickypipeModIcon =
        stickypipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 6].setIcon(new ImageIcon(stickypipeModIcon));
    mapButtons[gridNum * gridNum - gridNum + 6].addActionListener(Controller::StickyAction);

    Image setpumpImage = setpumpIcon.getImage();
    Image setpumpModIcon =
        setpumpImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 7].setIcon(new ImageIcon(setpumpModIcon));
    mapButtons[gridNum * gridNum - gridNum + 7].addActionListener(Controller::SetPumpAction);

    Image pickuppipeImage = pickuppipeIcon.getImage();
    Image pickuppipeModIcon =
        pickuppipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 8].setIcon(new ImageIcon(pickuppipeModIcon));
    mapButtons[gridNum * gridNum - gridNum + 8].addActionListener(Controller::PickUpPipeAction);

    Image pickuppumpImage = pickuppumpIcon.getImage();
    Image pickuppumpModIcon =
        pickuppumpImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
    mapButtons[gridNum * gridNum - gridNum + 9].setIcon(new ImageIcon(pickuppumpModIcon));
    mapButtons[gridNum * gridNum - gridNum + 9].addActionListener(Controller::PickUpPumpAction);

    // Adding the components to the frame and setting their layouts
    textField.setText(plumberNames.get(playerIdx) + " Plumbers turn");
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

    Controller.tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    assert Controller.tempChar != null : "tempChar is null???";
    Controller.tempNode = Controller.tempChar.getStandingOn();
    if (nomadTurn) {
      Image slipperypipeImage = slipperypipeIcon.getImage();
      Image slipperypipeModIcon =
          slipperypipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 5].setIcon(new ImageIcon(slipperypipeModIcon));
      mapButtons[gridNum * gridNum - gridNum + 7].setIcon(null);
      mapButtons[gridNum * gridNum - gridNum + 8].setIcon(null);
      mapButtons[gridNum * gridNum - gridNum + 9].setIcon(null);

      textField.setText(nomadNames.get(playerIdx / 2) + " Nomads turn");
    } else {
      Image repairImage = repairIcon.getImage();
      Image repairModIcon =
          repairImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 5].setIcon(new ImageIcon(repairModIcon));

      Image setpumpImage = setpumpIcon.getImage();
      Image setpumpModIcon =
          setpumpImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 7].setIcon(new ImageIcon(setpumpModIcon));

      Image pickuppipeImage = pickuppipeIcon.getImage();
      Image pickuppipeModIcon =
          pickuppipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 8].setIcon(new ImageIcon(pickuppipeModIcon));

      Image pickuppumpImage = pickuppumpIcon.getImage();
      Image pickuppumpModIcon =
          pickuppumpImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 9].setIcon(new ImageIcon(pickuppumpModIcon));

      textField.setText(plumberNames.get(playerIdx / 2) + " Plumbers turn");
    }

    /*
        // TODO: if the given node is in inventory: (white the if)
        Cistern tempCis = (Cistern) tempNode;
        Image pickuppipeImage = pickuppipeIcon.getImage();
        Image pickuppipeModIcon =
            pickuppipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
        mapButtons[gridNum * gridNum - gridNum + 8].setIcon(new ImageIcon(pickuppipeModIcon));
        mapButtons[gridNum * gridNum - gridNum + 8].addActionListener(Controller::PickUpPipeAction);

        Image pickuppumpImage = pickuppumpIcon.getImage();
        Image pickuppumpModIcon =
            pickuppumpImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
        mapButtons[gridNum * gridNum - gridNum + 9].setIcon(new ImageIcon(pickuppumpModIcon));

        // TODO: else:
        mapButtons[gridNum * gridNum - gridNum + 8].setIcon(null);
        mapButtons[gridNum * gridNum - gridNum + 9].setIcon(null);
    */
    updateFlow(); // TODO: debug UpdateFlow()
  }

  // TODO: needs a LOT of debug :c
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
    Image nodeImage;
    switch (getNodeType(node)) {
      case 1 -> nodeImage = cisternIcon.getImage();
      case 2 -> nodeImage = getPipeImage(node, pipeIcon);
      case 3 -> {
        if (node.isBroken()) {
          nodeImage = brokenpumpIcon.getImage();
        } else {
          nodeImage = emptypumpIcon.getImage();
        }
      }
      case 4 -> nodeImage = waterspringIcon.getImage();
      case 5 -> nodeImage = getPipeImage(node, waterpipeIcon);
      case 6 -> nodeImage = waterpumpIcon.getImage();
      default -> nodeImage = sandIcon.getImage();
    }

    // if the player is standing on the node, add the player image to the node image
    Image characterI = getPlayerImage(character);
    if (characterI != null) {
      nodeImage = createLayeredImage(nodeImage, characterI);
    }

    nodeImage = nodeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
    mapButtons[idx].setIcon(new ImageIcon(nodeImage));
  }

  private Image getPipeImage(Node pipe, ImageIcon pipeIcon) {
    Pipe tempPipe = (Pipe) pipe;
    Image nodeImage;
    if (tempPipe.isBroken()) {
      nodeImage = brokenpipeIcon.getImage();
    } else if (tempPipe.isSticky()) {
      nodeImage = createLayeredImage(pipeIcon.getImage(), stickypipeIcon.getImage());
    } else if (tempPipe.isSlippery()) {
      nodeImage = createLayeredImage(pipeIcon.getImage(), slipperypipeIcon.getImage());
    } else {
      nodeImage = pipeIcon.getImage();
    }
    return nodeImage;
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
      System.err.println("Slippery pipe\n\n\n");
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

  public void setPump() {} // TODO: implement

  /** The getter for the current character's type */
  private Image getPlayerImage(Character character) {
    if (character == null) return null;
    try {
      Nomad ignored = (Nomad) character;
      return nomadIcon.getImage();
    } catch (Exception e) {
      try {
        Plumber ignored = (Plumber) character;
        return plumberIcon.getImage();
      } catch (Exception ignored) {
      }
    }
    return null;
  }

  /** The getter for the current node's type */
  private int getNodeType(Node node) {
    int nodeType = 0;
    if (node == null) return nodeType;
    try {
      Cistern ignored = (Cistern) node;
      nodeType = 1;
    } catch (Exception e) {
      try {
        Pipe ignored = (Pipe) node;
        if (ignored.getFlowRate() == 0) nodeType = 2;
        else nodeType = 5;
      } catch (Exception ex) {
        try {
          Pump ignored = (Pump) node;
          if (ignored.getFlowRate() == 0) nodeType = 3;
          else nodeType = 6;
        } catch (Exception exception) {
          try {
            WaterSpring ignored = (WaterSpring) node;
            nodeType = 4;
          } catch (Exception ignored) {
          }
        }
      }
    }
    return nodeType;
  }

  /**
   * create the layered image
   *
   * @param image the buffered image
   */
  private Image createLayeredImage(Image image, Image overlay) {
    int w = Math.max(image.getWidth(null), overlay.getWidth(null));
    int h = Math.max(image.getHeight(null), overlay.getHeight(null));
    BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

    // adds the two layers of the images
    Graphics g = combined.getGraphics();
    g.drawImage(image, 0, 0, null);
    g.drawImage(overlay, 0, 0, null);
    g.dispose();

    return combined;
  }

  private void updatePlayerNodes() {
    try {
      for (int i = 0; i < Map.playerCount(); i++) {
        Character tempChar = Map.getPlayer(i);
        Node temp = tempChar.getStandingOn();
        int coord = temp.getX() + (gridNum * temp.getY());

        updateNodeImage(temp, coord, tempChar);
      }
    } catch (NullPointerException ignored) {
    }
  }
}
