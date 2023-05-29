package com.ez_mode.gui;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class Controller {
  static Game game;
  static Character tempChar;
  static Direction direction;
  static Node tempNode;
  static Node prevNode;
  static int prevIdx;
  static int setChoice;
  static Pump pump = new Pump();

  /**
   * Actions in Menu class
   *
   * @param e action event when a button is pressed
   */
  public static void MenuStartAction(ActionEvent e) {
    String pNames;
    String nNames;
    Menu.frame.dispose();
    EndGame.frame.dispose();
    int p = 0;
    int n = 0;
    Game.playerNames = new ArrayList<>();

    if (Menu.playerCountTextField.getText().isEmpty()) {
      Menu.playerCount = 2;
    } else {
      Menu.playerCount = Integer.parseInt(Menu.playerCountTextField.getText());
    }

    Menu.loadedPath = Menu.loadTextField.getText();

    pNames = Menu.plumberNamesTextField.getText();
    Game.plumberNames = new ArrayList<>(List.of(pNames.split(" ")));
    System.out.println(Game.plumberNames);

    nNames = Menu.nomadNamesTextField.getText();
    Game.nomadNames = new ArrayList<>(List.of(nNames.split(" ")));

    for (int i = 0; i < (Game.nomadNames.size() + Game.plumberNames.size()); i++) {
      if (i % 2 == 0) Game.playerNames.add(Game.plumberNames.get(p++));
      else Game.playerNames.add(Game.nomadNames.get(n++));
    }

    Map.fillMap(Menu.playerCount);
    game = new Game();
  }

  public static void MenuExitAction(ActionEvent e) {
    Menu.frame.dispose();
  }

  /**
   * Actions in Game class
   *
   * @param e action event when a button is pressed
   */
  public static void MoveUpAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    direction = Direction.UP;
    try {
      assert tempChar != null;
      prevNode = tempChar.getStandingOn();
      tempNode = Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
      try {
        assert tempNode != null;
        tempChar.moveTo(tempNode);
        tempNode = tempChar.getStandingOn();
        game.moveCharacter();
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("Legfelso sor");
    }
  }

  public static void MoveLeftAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    direction = Direction.LEFT;
    try {
      assert tempChar != null;
      prevNode = tempChar.getStandingOn();
      tempNode = Map.getNode(tempChar.getStandingOn().getX() - 1, tempChar.getStandingOn().getY());
      try {
        assert tempNode != null;
        tempChar.moveTo(tempNode);
        tempNode = tempChar.getStandingOn();
        game.moveCharacter();
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("balszelso sor");
    }
  }

  public static void MoveDownAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    direction = Direction.DOWN;
    try {
      assert tempChar != null;
      prevNode = tempChar.getStandingOn();
      tempNode = Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() + 1);
      try {
        assert tempNode != null;
        tempChar.moveTo(tempNode);
        tempNode = tempChar.getStandingOn();
        game.moveCharacter();
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("legalso sor");
    }
  }

  public static void MoveRightAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    direction = Direction.RIGHT;
    try {
      assert tempChar != null;
      prevNode = tempChar.getStandingOn();
      tempNode = Map.getNode(tempChar.getStandingOn().getX() + 1, tempChar.getStandingOn().getY());
      try {
        assert tempNode != null;
        tempChar.moveTo(tempNode);
        tempNode = tempChar.getStandingOn();
        game.moveCharacter();
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("jobb szelso sor");
    }
  }

  // two actions because one button
  public static void CharacterSpecAction(ActionEvent e) {
    if (Game.nomadTurn) {
      Game.nomadTurn = !Game.nomadTurn;
      game.updateAction();
      tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      try {
        Nomad tempNomad = (Nomad) tempChar;
        assert tempChar != null;
        tempNomad.setSlippery();
        tempNode = tempNomad.getStandingOn();
        game.setSlippery();
      } catch (ClassCastException ignored) {
      }
    } else {
      Game.nomadTurn = !Game.nomadTurn;
      game.updateAction();
      tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      try {
        Plumber tempPlumber = (Plumber) tempChar;
        assert tempChar != null;
        tempPlumber.repair();
        tempNode = tempPlumber.getStandingOn();
        game.repairNode();
      } catch (ClassCastException ignored) {
      }
    }
  }

  public static void BreakAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    tempNode = Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY());
    assert tempChar != null;
    tempChar.breakNode();
    game.breakNode();
  }

  public static void StickyAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      tempChar.makePipeSticky();
      game.setSticky();
    } catch (ClassCastException ignored) {
    }
  }

  // TODO: check if its working, for that we need the cistern ticking (currently it is not)
  public static void PickUpPipeAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      try {
        // if inventory is empty pick up
        if (tempChar.getDraggedpipe() != null || tempChar.getPickedUpPipe() != null)
          tempChar.PlacePipe();
        // if not empty than place
        else tempChar.PickupPipe((Pipe) tempChar.getStandingOn());
      } catch (NotFoundExeption NOTignored) {
      }
    } catch (ClassCastException ignored) {
    }
    /*Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    assert tempChar != null;
    tempChar.PlacePipe();*/
  }

  // TODO: check if its working, for that we need the cistern ticking (currently it is not)
  public static void PickUpPumpAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      // if inventory is empty pick up
      if (tempChar.getPickedupPump() != null) tempChar.PlacePump();
      // if not empty than place
      else tempChar.PickupPump();
    } catch (ClassCastException ignored) {
    }

    /*Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    assert tempChar != null;
    tempChar.PlacePump();*/
  }

  public static void SetPumpAction(ActionEvent e) {

    game.updateAction();
    Game.nomadTurn = !Game.nomadTurn;
    tempNode = Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY());
    int curIdx = tempChar.getStandingOn().getX() + (Game.gridNum * tempChar.getStandingOn().getY());
    Node upNeighbour =
        Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
    Node downNeighbour =
        Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() + 1);
    Node leftNeighbour =
        Map.getNode(tempChar.getStandingOn().getX() - 1, tempChar.getStandingOn().getY());
    Node rightNeighbour =
        Map.getNode(tempChar.getStandingOn().getX() + 1, tempChar.getStandingOn().getY());
    try {
      tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      try {
        Pump tempPump = (Pump) tempNode;
        new PopUp();
        if (setChoice == 0) {
          Pipe inputPipe = (Pipe) upNeighbour;
          Pipe outputPipe = (Pipe) downNeighbour;
          pump.setActiveInput(inputPipe);
          pump.setActiveOutput(outputPipe);
        } else {
          Pipe inputPipe = (Pipe) leftNeighbour;
          Pipe outputPipe = (Pipe) rightNeighbour;
          pump.setActiveInput(inputPipe);
          pump.setActiveOutput(outputPipe);
        }
        game.setPump();
      } catch (Exception ex) {
        System.out.println(ex.getMessage());
      }
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
    }
  }

  public static void GameExitAction(ActionEvent e) {
    Game.frame.dispose();
    new EndGame();
  }

  /**
   * Actions in EndGame class
   *
   * @param e action event when a button is pressed
   */
  public static void EndGameExitAction(ActionEvent e) {
    EndGame.frame.dispose();
    EndGame.savedPath = EndGame.saveTextField.getText();
    // TODO : save the map to the given path
  }

  public static void OpenMenuAction(ActionEvent e) {
    EndGame.frame.dispose();
    new Menu();
  }

  public static void LoadMapAction(ActionEvent e) {
    // TODO : load/create the map
  }

  public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }
}
