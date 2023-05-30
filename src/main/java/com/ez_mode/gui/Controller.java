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
   * @param ignored action event when a button is pressed
   */
  public static void MenuStartAction(ActionEvent ignored) {
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

    if (Menu.loadTextField.getText().isEmpty()) Map.fillMap(Menu.playerCount);
    else Map.loadMap(Menu.loadedPath);

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
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
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
      } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("Legfelso sor");
    }
  }

  public static void MoveLeftAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
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
      } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("balszelso sor");
    }
  }

  public static void MoveDownAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
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
      } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("legalso sor");
    }
  }

  public static void MoveRightAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
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
      } catch (ObjectFullException | InvalidPlayerMovementException ignored1) {
        tempNode = prevNode;
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("jobb szelso sor");
    }
  }

  // two actions because one button
  public static void CharacterSpecAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    System.err.println(Game.nomadTurn);
    if (!Game.nomadTurn) { // because we are changing the turn before
      System.err.println("Slippery pipe");
      try {
        Nomad tempNomad = (Nomad) tempChar;
        assert tempChar != null;
        tempNomad.setSlippery();
        game.setSlippery();
      } catch (ClassCastException ignored1) {
      }
    } else {
      try {
        Plumber tempPlumber = (Plumber) tempChar;
        assert tempChar != null;
        tempPlumber.repair();
        game.repairNode();
      } catch (ClassCastException ignored1) {
      }
    }
  }

  public static void BreakAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    assert tempChar != null;
    tempChar.breakNode();
    game.breakNode();
  }

  public static void StickyAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      assert tempChar != null;
      tempChar.makePipeSticky();
      game.setSticky();
    } catch (ClassCastException ignored1) {
    }
  }

  // TODO: check if its working, for that we need the cistern ticking (currently it is not)
  public static void PickUpPipeAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      Plumber temp = (Plumber) tempChar;
      assert temp != null;
      try {
        // if inventory is empty pick up
        if (temp.getDraggedpipe() != null || temp.getPickedUpPipe() != null) temp.PlacePipe();
        // if not empty, then place
        else temp.PickupPipe((Pipe) tempChar.getStandingOn());
      } catch (NotFoundExeption ignored1) {
      }
    } catch (ClassCastException ignored1) {
    }
    /*Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    assert tempChar != null;
    tempChar.PlacePipe();*/
  }

  // TODO: check if its working, for that we need the cistern ticking (currently it is not)
  public static void PickUpPumpAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      // if not empty, then place
      if (tempChar.getPickedupPump() != null) {
        Pump placed = tempChar.getPickedupPump();
        tempChar.PlacePump();
        tempNode = placed;
        game.UpdateField();
        if (placed.getNeighbours().size() >= 2) {
          tempNode = placed.getNeighbours().get(1);
          game.UpdateField();
        }
      }
      // if inventory is empty pick up
      else tempChar.PickupPump();
    } catch (ClassCastException ignored1) {
    }
  }

  public static void SetPumpAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    Node upNeighbour =
        Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
    Node downNeighbour =
        Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() + 1);
    Node leftNeighbour =
        Map.getNode(tempChar.getStandingOn().getX() - 1, tempChar.getStandingOn().getY());
    Node rightNeighbour =
        Map.getNode(tempChar.getStandingOn().getX() + 1, tempChar.getStandingOn().getY());
    try {
      Plumber ignored1 = (Plumber) tempChar;
      assert ignored1 != null;
      try {
        Pump ignored2 = (Pump) tempNode;
        new PopUp();
        Pipe inputPipe;
        Pipe outputPipe;
        if (setChoice == 0) {
          inputPipe = (Pipe) upNeighbour;
          outputPipe = (Pipe) downNeighbour;
        } else {
          inputPipe = (Pipe) leftNeighbour;
          outputPipe = (Pipe) rightNeighbour;
        }
        pump.setActiveInput(inputPipe);
        pump.setActiveOutput(outputPipe);
        game.setPump();
      } catch (ClassCastException ex) {
        System.out.println(ex.getMessage());
      }
    } catch (ClassCastException ex) {
      System.out.println(ex.getMessage());
    }
  }

  public static void GameExitAction(ActionEvent ignored) {
    Game.frame.dispose();
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

  public static void OpenMenuAction(ActionEvent ignored) {
    EndGame.frame.dispose();
    new Menu();
  }

  public static void LoadMapAction(ActionEvent ignored) {
    Menu.frame.dispose();
    Menu.loadedPath = Menu.loadTextField.getText();
    Map.loadMap(Menu.loadedPath);
  }

  public enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
  }
}
