package com.ez_mode.gui;

import static com.ez_mode.Main.map;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.objects.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Game {
  public static int gridNum = 10;
  static int windowWidth = 800 - 100;
  int windowHeight = 800;
  static int fieldSize = windowWidth / gridNum;
  static int actionSize = fieldSize - 10;
  public static boolean nomadTurn = false;
  public static ArrayList<String> plumberNames;
  public static ArrayList<String> nomadNames;
  static ArrayList<String> playerNames;
  static int playerIdx = 0;

  /** Java Swing components for the Game class */
  static JFrame frame = new JFrame();

  JPanel titlePanel = new JPanel();
  static JLabel textField = new JLabel();
  JButton endGameButton = new JButton();
  JPanel mapPanel = new JPanel();
  static JButton[] mapButtons = new JButton[gridNum * gridNum + 1];
  JPanel actionPanel = new JPanel();

  /** Adding all the images' path that will be used in the game */
  public static String outImagePath = "out.png";

  public static String pipeImagePath = "src/main/resources/pipe.png";
  public static String sandImagePath = "src/main/resources/sand.png";
  public static String plumberImagePath = "src/main/resources/transplumber.png";
  public static String nomadImagePath = "src/main/resources/transnomad.png";
  public static String waterspringImagePath = "src/main/resources/waterspring.png";
  public static String waterpumpImagePath = "src/main/resources/waterpump.png";
  public static String waterpipeImagePath = "src/main/resources/waterpipe.png";
  public String waterImagePath = "src/main/resources/water.png";
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
  public String moveupImagePath = "src/main/resources/moveup.png";
  public String moveleftImagePath = "src/main/resources/moveleft.png";
  public String movedownImagePath = "src/main/resources/movedown.png";
  public String moverightImagePath = "src/main/resources/moveright.png";
  public static String setpumpImagePath = "src/main/resources/setpump.png";

  /** Adding all the images as ImageIcons, using the path given previously */
  public static ImageIcon outIcon = new ImageIcon(outImagePath);

  public static ImageIcon pipeIcon = new ImageIcon(pipeImagePath);
  public static ImageIcon sandIcon = new ImageIcon(sandImagePath);
  public ImageIcon plumberIcon = new ImageIcon(plumberImagePath);
  public ImageIcon nomadIcon = new ImageIcon(nomadImagePath);
  public static ImageIcon waterspringIcon = new ImageIcon(waterspringImagePath);
  public static ImageIcon waterpumpIcon = new ImageIcon(waterpumpImagePath);
  public static ImageIcon waterpipeIcon = new ImageIcon(waterpipeImagePath);
  public ImageIcon waterIcon = new ImageIcon(waterImagePath);
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
  public ImageIcon moveupIcon = new ImageIcon(moveupImagePath);
  public ImageIcon moveleftIcon = new ImageIcon(moveleftImagePath);
  public ImageIcon movedownIcon = new ImageIcon(movedownImagePath);
  public ImageIcon moverightIcon = new ImageIcon(moverightImagePath);
  public static ImageIcon setpumpIcon = new ImageIcon(setpumpImagePath);

  static BufferedImage overlay = null;

  public Game() {

    /** Properties of the frame */
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setTitle("Game");
    frame.setResizable(false);

    titlePanel.setLayout(new BorderLayout());

    /** Properties of the text field */
    textField.setBackground(new Color(0, 0, 0));
    textField.setForeground(new Color(230, 230, 230));
    textField.setFont(new Font("Monospace", Font.BOLD, 50));
    textField.setHorizontalAlignment(JLabel.CENTER);
    textField.setText("Plumbers turn");
    textField.setOpaque(true);

    /** Properties of the end game button */
    endGameButton.setBounds(500, 13, 20, 30);
    endGameButton.setFont(new Font("Monospace", Font.BOLD, 20));
    endGameButton.setText("End Game");
    endGameButton.setBackground(new Color(250, 250, 250));
    endGameButton.setForeground(new Color(0, 0, 0));
    endGameButton.setFocusable(false);
    endGameButton.addActionListener(Controller::GameExitAction);

    /** Filling the map */
    mapPanel.setLayout(new GridLayout(gridNum, gridNum + 1));
    mapPanel.setBorder(null);

    int nodeType = 0;
    for (int i = 0; i < gridNum; i++) {
      for (int j = 0; j < gridNum; j++) {
        Node temp = Map.getNode(j, i);
        mapButtons[i * gridNum + j] = new JButton();
        mapPanel.add(mapButtons[i * gridNum + j]);
        mapButtons[i * gridNum + j].setFocusable(false);
        mapButtons[i * gridNum + j].setSize(fieldSize, fieldSize);
        mapButtons[i * gridNum + j].setBorderPainted(false);
        mapButtons[i * gridNum + j].setHorizontalAlignment(JLabel.HORIZONTAL);
        if (temp == null) {
          Image sandImage = sandIcon.getImage();
          Image sandModIcon =
              sandImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[i * gridNum + j].setIcon(new ImageIcon(sandModIcon));
        } else {
          try {
            Cistern c = (Cistern) temp;
            Image cisternImage = cisternIcon.getImage();
            Image cisternModIcon =
                cisternImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
            mapButtons[i * gridNum + j].setIcon(new ImageIcon(cisternModIcon));
            nodeType = 1;
          } catch (Exception e) {
            try {
              Pipe pi = (Pipe) temp;
              Image pipeImage = pipeIcon.getImage();
              Image pipeModIcon =
                  pipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
              mapButtons[i * gridNum + j].setIcon(new ImageIcon(pipeModIcon));
              nodeType = 2;
            } catch (Exception ex) {
              try {
                Pump pu = (Pump) temp;
                Image emptypumpImage = emptypumpIcon.getImage();
                Image emptypumpModIcon =
                    emptypumpImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
                mapButtons[i * gridNum + j].setIcon(new ImageIcon(emptypumpModIcon));

                nodeType = 3;
              } catch (Exception exception) {
                try {
                  WaterSpring ws = (WaterSpring) temp;
                  Image waterspringImage = waterspringIcon.getImage();
                  Image waterspringModIcon =
                      waterspringImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
                  mapButtons[i * gridNum + j].setIcon(new ImageIcon(waterspringModIcon));
                  nodeType = 4;
                } catch (Exception e1) {
                }
              }
            }
          }
        }
        for (int k = 0; k < Map.playerCount(); k++) {
          Character temp2 = Map.getPlayer(k);
          if (temp == null) continue;
          if (temp.getCharacters().contains(temp2)) {
            BufferedImage overlay = null;
            try {
              Nomad n = (Nomad) temp2;
              overlay = ImageIO.read(new File(nomadImagePath));
            } catch (Exception e) {
              try {
                Plumber p = (Plumber) temp2;
                overlay = ImageIO.read(new File(plumberImagePath));
              } catch (Exception ex) {
              }
            }
            try {
              BufferedImage image;
              switch (nodeType) {
                case 1:
                  image = ImageIO.read(new File(cisternImagePath));
                  break;
                case 2:
                  image = ImageIO.read(new File(pipeImagePath));
                  break;
                case 3:
                  image = ImageIO.read(new File(emptypumpImagePath));
                  break;
                case 4:
                  image = ImageIO.read(new File(waterspringImagePath));
                  break;
                default:
                  image = ImageIO.read(new File(sandImagePath));
              }
              if (overlay == null) continue;
              int w = Math.max(image.getWidth(), overlay.getWidth());
              int h = Math.max(image.getHeight(), overlay.getHeight());
              BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

              Graphics g = combined.getGraphics();
              g.drawImage(image, 0, 0, null);
              g.drawImage(overlay, 0, 0, null);
              g.dispose();

              ImageIO.write(combined, "PNG", new File(outImagePath));
            } catch (IOException e) {
            }
            Image outImage = outIcon.getImage();
            Image outModIcon =
                outImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
            mapButtons[i * gridNum + j].setIcon(new ImageIcon(outModIcon));
          }
        }
      }
    }

    /** Icons of the action bar */
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

    /** Adding the components to the frame and setting their layouts */
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
  static void updateAction() {
    Character tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    Node tempNode = tempChar.getStandingOn();
    System.out.println(nomadTurn);
    if (nomadTurn) {
      Image slipperypipeImage = slipperypipeIcon.getImage();
      Image slipperypipeModIcon =
          slipperypipeImage.getScaledInstance(actionSize, actionSize, Image.SCALE_DEFAULT);
      mapButtons[gridNum * gridNum - gridNum + 5].setIcon(new ImageIcon(slipperypipeModIcon));
      mapButtons[gridNum * gridNum - gridNum + 7].setIcon(null);
      mapButtons[gridNum * gridNum - gridNum + 8].setIcon(null);
      mapButtons[gridNum * gridNum - gridNum + 9].setIcon(null);

      textField.setText(nomadNames.get(playerIdx / 2) + " Nomads turn");
      playerIdx++;
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
      playerIdx++;
    }
    playerIdx = playerIdx % (Menu.playerCount * 2);

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

    // Game.UpdateFlow(); TODO: debug UpdateFlow()
  }

  // TODO: needs a LOT of debug :c
  static void UpdateFlow() {
    Character tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    Node tempNode = tempChar.getStandingOn();
    int idx = tempChar.getStandingOn().getX() + (gridNum * tempChar.getStandingOn().getY());
    getPlayerType();
    BufferedImage image;
    // checks the flowrate for every node, if it's >0, sets the water in the node
    for (int i = 0; i < gridNum; i++) {
      for (int j = 0; j < gridNum; j++) {
        Node tempN = Map.getNode(j, i);
        try {
          Pump pu = (Pump) tempN;
          if (pu.getFlowRate() > 0) {
            Image waterpumpImage = waterpumpIcon.getImage();
            Image waterpumpModIcon =
                waterpumpImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
            mapButtons[i * gridNum + j].setIcon(new ImageIcon(waterpumpModIcon));
            if (i * gridNum + j == idx) {
              image = ImageIO.read(new File(Game.waterpumpImagePath));
              createLayeredImage(image);
              Image outImage = Game.outIcon.getImage();
              Image outModIcon =
                  outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
              mapButtons[idx].setIcon(new ImageIcon(outModIcon));
            }
          } else {
            Image emptypumpImage = emptypumpIcon.getImage();
            Image emptypumpModIcon =
                emptypumpImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
            mapButtons[i * gridNum + j].setIcon(new ImageIcon(emptypumpModIcon));
            if (i * gridNum + j == idx) {
              image = ImageIO.read(new File(Game.emptypumpImagePath));
              createLayeredImage(image);
              Image outImage = Game.outIcon.getImage();
              Image outModIcon =
                  outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
              mapButtons[idx].setIcon(new ImageIcon(outModIcon));
            }
          }
        } catch (Exception exception) {
          try {
            Pipe pi = (Pipe) tempNode;
            System.out.println(pi.getFlowRate());
            if (pi.getFlowRate() > 0) {
              Image waterpipeImage = waterpipeIcon.getImage();
              Image waterpipeModIcon =
                  waterpipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
              mapButtons[i * gridNum + j].setIcon(new ImageIcon(waterpipeModIcon));
              if (i * gridNum + j == idx) {
                image = ImageIO.read(new File(Game.waterpipeImagePath));
                createLayeredImage(image);
                Image outImage = Game.outIcon.getImage();
                Image outModIcon =
                    outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
                mapButtons[idx].setIcon(new ImageIcon(outModIcon));
              }
            }
          } catch (Exception e1) {
          }
        }
      }
    }
    map.tick();
  }

  /** The character movement in the gui, with the correct images */
  static void MoveCharacter() {
    // calculate the index of the node that the character is standing on
    int idx = Controller.tempNode.getX() + (gridNum * Controller.tempNode.getY());
    Controller.prevIdx = Controller.prevNode.getX() + (gridNum * Controller.prevNode.getY());

    // get character type
    getPlayerType();

    switch (getNodeType(Controller.prevNode)) {
      case 1:
        Image cisternImage = cisternIcon.getImage();
        Image cisternModIcon =
            cisternImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
        mapButtons[Controller.prevIdx].setIcon(new ImageIcon(cisternModIcon));
        break;
      case 2:
        Pipe tempPipe = (Pipe) Controller.prevNode;
        if (tempPipe.isSticky()) {
          Image stickypipeImage = stickypipeIcon.getImage();
          Image stickypipeModIcon =
              stickypipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[Controller.prevIdx].setIcon(new ImageIcon(stickypipeModIcon));
        }
        if (Controller.prevNode.isBroken()) {
          Image brokenpipeImage = brokenpipeIcon.getImage();
          Image brokenpipeModIcon =
              brokenpipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[Controller.prevIdx].setIcon(new ImageIcon(brokenpipeModIcon));
        } else {
          Image pipeImage = pipeIcon.getImage();
          Image pipeModIcon =
              pipeImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[Controller.prevIdx].setIcon(new ImageIcon(pipeModIcon));
        }
        break;
      case 3:
        if (Controller.prevNode.isBroken()) {
          Image brokenpumpImage = brokenpumpIcon.getImage();
          Image brokenpumpModIcon =
              brokenpumpImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[Controller.prevIdx].setIcon(new ImageIcon(brokenpumpModIcon));
        } else {
          Image emptypumpImage = emptypumpIcon.getImage();
          Image emptypumpModIcon =
              emptypumpImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
          mapButtons[Controller.prevIdx].setIcon(new ImageIcon(emptypumpModIcon));
        }
        break;
      case 4:
        Image waterspringImage = waterspringIcon.getImage();
        Image waterspringModIcon =
            waterspringImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
        mapButtons[Controller.prevIdx].setIcon(new ImageIcon(waterspringModIcon));
        break;
      default:
        Image sandImage = sandIcon.getImage();
        Image sandModIcon = sandImage.getScaledInstance(fieldSize, fieldSize, Image.SCALE_DEFAULT);
        mapButtons[Controller.prevIdx].setIcon(new ImageIcon(sandModIcon));
        break;
    }
    try {
      BufferedImage image;
      switch (getNodeType(Controller.tempNode)) {
        case 1:
          image = ImageIO.read(new File(Game.cisternImagePath));
          break;
        case 2:
          image = ImageIO.read(new File(Game.pipeImagePath));
          break;
        case 3:
          image = ImageIO.read(new File(Game.emptypumpImagePath));
          break;
        case 4:
          image = ImageIO.read(new File(Game.waterspringImagePath));
          break;
        case 5:
          image = ImageIO.read(new File(Game.waterpipeImagePath));
          break;
        case 6:
          image = ImageIO.read(new File(Game.waterpumpImagePath));
          break;
        default:
          image = ImageIO.read(new File(Game.sandImagePath));
          break;
      }
      createLayeredImage(image);
    } catch (IOException ignored) {
    }
    Image outImage = Game.outIcon.getImage();
    Image outModIcon =
        outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
    mapButtons[idx].setIcon(new ImageIcon(outModIcon));
  }

  /** The current character's break action in the gui, with the correct images */
  static void BreakNode() {
    // get character type
    getPlayerType();
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be broken by both characters
    try {
      Pipe ignored = (Pipe) Controller.tempNode;
      BufferedImage image = ImageIO.read(new File(Game.brokenpipeImagePath));
      createLayeredImage(image);
      Image outImage = Game.outIcon.getImage();
      Image outModIcon =
          outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
      mapButtons[idx].setIcon(new ImageIcon(outModIcon));
    } catch (Exception e) {
    }
  }

  static void SetSticky() {
    getPlayerType();
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be made sticky by both characters
    try {
      Pipe ignored = (Pipe) Controller.tempNode;
      BufferedImage image = ImageIO.read(new File(Game.stickypipeImagePath));
      createLayeredImage(image);
      Image outImage = Game.outIcon.getImage();
      Image outModIcon =
          outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
      mapButtons[idx].setIcon(new ImageIcon(outModIcon));
    } catch (Exception e) {
    }
  }

  static void SetSlippery() {
    getPlayerType();
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be made slippery by nomad characters
    try {
      Nomad tempNomad = (Nomad) Controller.tempChar;
      Pipe ignored = (Pipe) Controller.tempNode;
      BufferedImage image = ImageIO.read(new File(Game.slipperypipeImagePath));
      createLayeredImage(image);
      Image outImage = Game.outIcon.getImage();
      Image outModIcon =
          outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
      mapButtons[idx].setIcon(new ImageIcon(outModIcon));
    } catch (Exception e) {
    }
  }

  static void RepairNode() {
    getPlayerType();
    int idx =
        Controller.tempChar.getStandingOn().getX()
            + (gridNum * Controller.tempChar.getStandingOn().getY());
    // only the Pipe nodes can be made slippery by nomad characters
    try {
      Plumber tempPlumber = (Plumber) Controller.tempChar;
      System.out.println("RepairNode character is a plumber");
      Pipe ignored = (Pipe) Controller.tempNode;
      BufferedImage image = ImageIO.read(new File(Game.pipeImagePath));
      createLayeredImage(image);
      Image outImage = Game.outIcon.getImage();
      Image outModIcon =
          outImage.getScaledInstance(Game.fieldSize, Game.fieldSize, Image.SCALE_DEFAULT);
      mapButtons[idx].setIcon(new ImageIcon(outModIcon));
    } catch (Exception e) {
    }
  }

  static void SetPump() {}

  /** The getter for the current character's type */
  private static void getPlayerType() {
    try {
      Nomad ignored = (Nomad) Controller.tempChar;
      overlay = ImageIO.read(new File(Game.nomadImagePath));
    } catch (Exception e) {
      try {
        Plumber ignored = (Plumber) Controller.tempChar;
        overlay = ImageIO.read(new File(Game.plumberImagePath));
      } catch (Exception ignored) {
      }
    }
  }

  /** The getter for the current node's type */
  static int getNodeType(Node node) {
    int nodeType = 0;
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
  static void createLayeredImage(BufferedImage image) {
    try {
      int w = Math.max(image.getWidth(), overlay.getWidth());
      int h = Math.max(image.getHeight(), overlay.getHeight());
      BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

      // adds the two layers of the images
      Graphics g = combined.getGraphics();
      g.drawImage(image, 0, 0, null);
      g.drawImage(overlay, 0, 0, null);
      g.dispose();

      // writes the combined image
      ImageIO.write(combined, "PNG", new File(Game.outImagePath));
    } catch (IOException ignored) {
    }
  }
}
