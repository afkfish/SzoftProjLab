package com.ez_mode.gui;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Cistern;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class Controller {
  static Game game;
  static Character currentPlayer;
  static Direction direction;
  static Node currentNode;
  static int setChoice;
  static Pump pump = new Pump();

  /**
   * Actions in Menu class
   *
   * @param ignored action event when a button is pressed
   */
  public static void MenuStartAction(ActionEvent ignored) {
    String[] pNames;
    String[] nNames;
    Menu.frame.dispose();
    EndGame.frame.dispose();

    if (Menu.playerCountTextField.getText().isEmpty()) {
      Menu.playerCount = 2;
    } else {
      Menu.playerCount = Integer.parseInt(Menu.playerCountTextField.getText());
    }

    Menu.loadedPath = Menu.loadTextField.getText();

    pNames = Menu.plumberNamesTextField.getText().split(" ");
    Game.plumberNames = new ArrayList<>(List.of(pNames));

    nNames = Menu.nomadNamesTextField.getText().split(" ");
    Game.nomadNames = new ArrayList<>(List.of(nNames));

    if (Menu.loadTextField.getText().isEmpty()) {
      Map.fillMap(Menu.playerCount);
    } else {
      Map.loadMap(Menu.loadedPath);
    }

    Game.players.addAll(Map.getPlayers());
    game = new Game();
  }

  public static void MenuExitAction(ActionEvent ignored) {
    Menu.frame.dispose();
  }

  /**
   * Actions in Game class
   *
   * @param ignored action event when a button is pressed
   */
  public static void MoveUpAction(ActionEvent ignored) {
    direction = Direction.UP;

    Node prevNode = currentPlayer.getStandingOn();
    try {
      currentNode = Map.getNode(prevNode.getX(), prevNode.getY() - 1);
    } catch (ArrayIndexOutOfBoundsException ex) {
      currentNode = prevNode;
      return;
    }

    if (currentNode == null) {
      currentNode = prevNode;
      return;
    }

    try {
      currentPlayer.moveTo(currentNode);
      Game.moveCharacter(prevNode, currentNode);
    } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
    }

    game.updateAction();
  }

  public static void MoveLeftAction(ActionEvent ignored) {
    direction = Direction.LEFT;

    Node prevNode = currentPlayer.getStandingOn();
    try {
      currentNode = Map.getNode(prevNode.getX() - 1, prevNode.getY());
    } catch (ArrayIndexOutOfBoundsException ex) {
      currentNode = prevNode;
      return;
    }

    if (currentNode == null) {
      currentNode = prevNode;
      return;
    }

    try {
      currentPlayer.moveTo(currentNode);
      Game.moveCharacter(prevNode, currentNode);
    } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
    }

    game.updateAction();
  }

  public static void MoveDownAction(ActionEvent ignored) {
    direction = Direction.DOWN;

    Node prevNode = currentPlayer.getStandingOn();
    try {
      currentNode = Map.getNode(prevNode.getX(), prevNode.getY() + 1);
    } catch (ArrayIndexOutOfBoundsException ex) {
      currentNode = prevNode;
      return;
    }

    if (currentNode == null) {
      currentNode = prevNode;
      return;
    }

    try {
      currentPlayer.moveTo(currentNode);
      Game.moveCharacter(prevNode, currentNode);
    } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
    }

    game.updateAction();
  }

  public static void MoveRightAction(ActionEvent ignored) {
    direction = Direction.RIGHT;

    Node prevNode = currentPlayer.getStandingOn();
    try {
      currentNode = Map.getNode(prevNode.getX() + 1, prevNode.getY());
    } catch (ArrayIndexOutOfBoundsException ex) {
      currentNode = prevNode;
      return;
    }

    if (currentNode == null) {
      currentNode = prevNode;
      return;
    }

    try {
      currentPlayer.moveTo(currentNode);
      Game.moveCharacter(prevNode, currentNode);
    } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
    }

    game.updateAction();
  }

  // two actions because one button
  public static void CharacterSpecAction(ActionEvent ignored) {
    if (currentPlayer instanceof Nomad tempNomad) {
      tempNomad.setSlippery();
      Game.setSlippery(currentNode, currentPlayer);
    } else if (currentPlayer instanceof Plumber tempPlumber) {
      tempPlumber.repair();
      Game.repairNode(currentNode, currentPlayer);
    }

    game.updateAction();
  }

  public static void BreakAction(ActionEvent ignored) {
    currentPlayer.breakNode();
    Game.breakNode(currentNode, currentPlayer);

    game.updateAction();
  }

  public static void StickyAction(ActionEvent ignored) {
    currentPlayer.makePipeSticky();
    Game.setSticky(currentNode);

    game.updateAction();
  }

  public static void PickUpPipeAction(ActionEvent ignored) {
    if (currentPlayer instanceof Nomad) {
      return;
    }

    Plumber tempPlumber = (Plumber) currentPlayer;
    try {
      new PopUp(false, tempPlumber);
      if (PopUp.place) {
        Node placed = tempPlumber.PlacePipe(setChoice);
        Game.updateField(placed);
      } else {
        Pipe tempPipe = new Pipe();

        try {
          switch (setChoice) {
            case 0 -> tempPipe = (Pipe) Map.getNode(currentNode.getX(), currentNode.getY() - 1);
            case 1 -> tempPipe = (Pipe) Map.getNode(currentNode.getX(), currentNode.getY() + 1);
            case 2 -> tempPipe = (Pipe) Map.getNode(currentNode.getX() - 1, currentNode.getY());
            case 3 -> tempPipe = (Pipe) Map.getNode(currentNode.getX() + 1, currentNode.getY());
            case 4 -> tempPipe = ((Cistern) currentPlayer.getStandingOn()).MakePipe();
          }
        } catch (ClassCastException ignored1) {
          JOptionPane.showMessageDialog(null, "You are not standing on a cistern!");
        }
        tempPlumber.PickupPipe(tempPipe);
      }
    } catch (NotFoundExeption ignored1) {
    }

    game.updateAction();
  }

  public static void PickUpPumpAction(ActionEvent ignored) {
    if (currentPlayer instanceof Plumber tempPlumber) {
      // if not empty, then place
      if (tempPlumber.getPickedupPump() != null) {
        Pump placed = tempPlumber.getPickedupPump();
        tempPlumber.PlacePump();
        //        tempNode = placed;
        Game.updateField(placed);
        //        if (placed.getNeighbours().size() >= 2) {
        //          tempNode = placed.getNeighbours().get(1);
        //          game.updateField(tempNode);
        //        }
      } else { // if inventory is empty pick up
        tempPlumber.PickupPump();
      }
    }
    game.updateAction();
  }

  public static void SetPumpAction(ActionEvent ignored) {
    if (currentNode instanceof Pump) {
      new PopUp(true, null);
      Pipe inputPipe = null;
      Pipe outputPipe = null;
      try {
        if (setChoice == 0 | setChoice == 1) {
          inputPipe = (Pipe) Map.getNode(currentNode.getX(), currentNode.getY() - 1);
          ;
          outputPipe = (Pipe) Map.getNode(currentNode.getX(), currentNode.getY() + 1);
          ;
        } else {
          inputPipe = (Pipe) Map.getNode(currentNode.getX() - 1, currentNode.getY());
          ;
          outputPipe = (Pipe) Map.getNode(currentNode.getX() + 1, currentNode.getY());
          ;
        }
      } catch (ClassCastException ex) {
        System.out.println(ex.getMessage());
      }
      pump.setActiveInput(inputPipe);
      pump.setActiveOutput(outputPipe);
    }

    game.updateAction();
  }

  public static void GameExitAction(ActionEvent ignored) {
    game.dispose();
    new EndGame();
  }

  /**
   * Actions in EndGame class
   *
   * @param ignored action event when a button is pressed
   */
  public static void EndGameExitAction(ActionEvent ignored) {
    EndGame.frame.dispose();
    EndGame.savedPath = EndGame.saveTextField.getText();
    Map.saveMap(EndGame.savedPath);
  }

  public static void LoadMapAction(ActionEvent ignored) {
    Menu.frame.dispose();
    Menu.loadedPath = Menu.loadTextField.getText();
    Map.loadMap(Menu.loadedPath);
    Menu.setPlayerCount(Map.playerCount() / 2);
    Game.plumberNames = new ArrayList<>();
    Game.nomadNames = new ArrayList<>();
    Game.playerNames = new ArrayList<>();
    for (int i = 0; i < Map.playerCount(); i++) {
      Character tempChar = Map.getPlayer(i);
      if (tempChar instanceof Nomad nomad) {
        Game.nomadNames.add(tempChar.getName());
        Game.players.add(nomad);
      } else if (tempChar instanceof Plumber plumber) {
        Game.plumberNames.add(tempChar.getName());
        Game.players.add(plumber);
      }
    }
    game = new Game();
  }

  public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }
}
