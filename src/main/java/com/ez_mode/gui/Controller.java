package com.ez_mode.gui;

import com.ez_mode.Main;
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

    if (Menu.loadTextField.getText().isEmpty()) {
      Map.fillMap(Menu.playerCount);
    } else {
      Map.loadMap(Menu.loadedPath);
    }

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
    if (!Game.nomadTurn) { // because we are changing the turn before
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

  public static void PickUpPipeAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      Plumber tempPlumber = (Plumber) tempChar;
      assert tempPlumber != null;
      try {
        tempNode = tempPlumber.getStandingOn();
        assert tempNode != null;
        Pipe tempPipe = new Pipe();
        new PopUp(false, tempPlumber);
        if (PopUp.place) {
          tempPlumber.PlacePipe(setChoice);
          switch (setChoice) {
            case 0 -> tempNode = Map.getNode(tempNode.getX(), tempNode.getY() - 1);
            case 1 -> tempNode = Map.getNode(tempNode.getX(), tempNode.getY() + 1);
            case 2 -> tempNode = Map.getNode(tempNode.getX() - 1, tempNode.getY());
            case 3 -> tempNode = Map.getNode(tempNode.getX() + 1, tempNode.getY());
          }
          game.UpdateField();
        } else {
          Node upNeighbour, downNeighbour, rightNeighbour, leftNeighbour;
          try {
            upNeighbour =
                Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
          } catch (ArrayIndexOutOfBoundsException e) {
            upNeighbour = null;
          }
          try {
            downNeighbour =
                Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() + 1);
          } catch (ArrayIndexOutOfBoundsException e) {
            downNeighbour = null;
          }
          try {
            leftNeighbour =
                Map.getNode(tempChar.getStandingOn().getX() - 1, tempChar.getStandingOn().getY());
          } catch (ArrayIndexOutOfBoundsException e) {
            leftNeighbour = null;
          }
          try {
            rightNeighbour =
                Map.getNode(tempChar.getStandingOn().getX() + 1, tempChar.getStandingOn().getY());
          } catch (ArrayIndexOutOfBoundsException e) {
            rightNeighbour = null;
          }
          switch (setChoice) {
            case 0 -> tempPipe = (Pipe) upNeighbour;
            case 1 -> tempPipe = (Pipe) downNeighbour;
            case 2 -> tempPipe = (Pipe) leftNeighbour;
            case 3 -> tempPipe = (Pipe) rightNeighbour;
            case 4 -> tempPipe = ((Cistern) tempChar.getStandingOn()).MakePipe();
          }
          tempPlumber.PickupPipe(tempPipe);
          tempNode = tempPipe;
        }

      } catch (NotFoundExeption ignored1) {
      }
    } catch (ClassCastException ignored1) {
      JOptionPane.showMessageDialog(null, "You are not standing on a cistern!");
    }
  }

  public static void PickUpPumpAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      // if not empty, then place
      if (tempChar.getPickedupPump() != null) {
        Pipe a = (Pipe) tempChar.getStandingOn();
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
      else {
        tempChar.PickupPump();
      }
    } catch (ClassCastException ignored1) {
      Main.log(tempChar.getUuid() + " is not standing on a Pipe");
    }
  }

  public static void SetPumpAction(ActionEvent ignored) {
    Game.nomadTurn = !Game.nomadTurn;
    game.updateAction();
    Node upNeighbour, downNeighbour, rightNeighbour, leftNeighbour;
    try {
      upNeighbour =
          Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
    } catch (ArrayIndexOutOfBoundsException e) {
      upNeighbour = null;
    }
    try {
      downNeighbour =
          Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() + 1);
    } catch (ArrayIndexOutOfBoundsException e) {
      downNeighbour = null;
    }
    try {
      leftNeighbour =
          Map.getNode(tempChar.getStandingOn().getX() - 1, tempChar.getStandingOn().getY());
    } catch (ArrayIndexOutOfBoundsException e) {
      leftNeighbour = null;
    }
    try {
      rightNeighbour =
          Map.getNode(tempChar.getStandingOn().getX() + 1, tempChar.getStandingOn().getY());
    } catch (ArrayIndexOutOfBoundsException e) {
      rightNeighbour = null;
    }
    try {
      // Plumber ignored1 = (Plumber) tempChar;
      assert tempChar != null;
      try {
        Pump ignored2 = (Pump) tempNode;
        new PopUp(true, null);
        Pipe inputPipe;
        Pipe outputPipe;
        if (setChoice == 0 | setChoice == 1) {
          inputPipe = (Pipe) upNeighbour;
          outputPipe = (Pipe) downNeighbour;
        } else {
          inputPipe = (Pipe) leftNeighbour;
          outputPipe = (Pipe) rightNeighbour;
        }
        pump.setActiveInput(inputPipe);
        pump.setActiveOutput(outputPipe);
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
      Game.playerNames.add(tempChar.getName());
      try {
        Plumber ignored1 = (Plumber) tempChar;
        Game.plumberNames.add(tempChar.getName());
      } catch (ClassCastException ignored2) {
        Game.nomadNames.add(tempChar.getName());
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
