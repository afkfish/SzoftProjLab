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

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Controller {

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
    Game.plumberNames = new ArrayList<>(Arrays.asList(pNames.split(" ")));

    nNames = Menu.nomadNamesTextField.getText();
    Game.nomadNames = new ArrayList<>(Arrays.asList(nNames.split(" ")));

    for (int i = 0; i < (Game.nomadNames.size() + Game.plumberNames.size()); i++) {
      if (i % 2 == 0) Game.playerNames.add(Game.plumberNames.get(p++));
      else Game.playerNames.add(Game.nomadNames.get(n++));
    }

    Map.fillMap(Menu.playerCount * 2);
    new Game();
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
    Game.updateAction();
    Character tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    try {
      assert tempChar != null;
      Node tempNode =
          Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
      try {

        assert tempNode != null;
        tempChar.moveTo(tempNode);
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("Legfelso sor");
    }
  }

  public static void MoveLeftAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    Character tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    try {
      assert tempChar != null;
      Node tempNode =
              Map.getNode(tempChar.getStandingOn().getX()-1, tempChar.getStandingOn().getY() );
      try {

        assert tempNode != null;
        tempChar.moveTo(tempNode);
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("balszelso sor");
    }
  }


  public static void MoveDownAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    Character tempChar = Map.getPlayer(Game.playerNames.get(Game.playerIdx));
    try {
      assert tempChar != null;
      Node tempNode =
              Map.getNode(tempChar.getStandingOn().getX(), tempChar.getStandingOn().getY() - 1);
      try {

        assert tempNode != null;
        tempChar.moveTo(tempNode);
      } catch (ObjectFullException | InvalidPlayerMovementException ignored) {
      }
    } catch (ArrayIndexOutOfBoundsException ex) {
      System.out.println("legalso sor");
    }
  }


  public static void MoveRightAction(ActionEvent e){
        Game.nomadTurn=!Game.nomadTurn;
        Game.updateAction();
        Character tempChar=Map.getPlayer(Game.playerNames.get(Game.playerIdx));
        try{
        assert tempChar!=null;
        Node tempNode=
        Map.getNode(tempChar.getStandingOn().getX()+1,tempChar.getStandingOn().getY());
        try{

        assert tempNode!=null;
        tempChar.moveTo(tempNode);
        }catch(ObjectFullException|InvalidPlayerMovementException ignored){
        }
        }catch(ArrayIndexOutOfBoundsException ex){
        System.out.println("jobb szelso sor");
        }
        }



  public static void RepairAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      tempChar.repair();
    }
    catch(ClassCastException ignored){}
    // TODO
  }

  public static void BreakAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
      Character tempChar =  Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      tempChar.breakNode();

  }

  public static void StickyAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
    try {
      Character tempChar =  Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      tempChar.makePipeSticky();
    }
    catch(ClassCastException ignored){}
  }

  public static void SlipperyAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
    try {
      Nomad tempChar = (Nomad) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      tempChar.setSlippery();
    }
    catch(ClassCastException ignored){}
  }


  public static void PickUpPipeAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      try {
        tempChar.PickupPipe((Pipe) tempChar.getStandingOn());
      }catch ( NotFoundExeption NOTignored){
        ///Todo
      }
    }
    catch(ClassCastException ignored){}
  }

  public static void PickUpPumpAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    try {
      Plumber tempChar = (Plumber) Map.getPlayer(Game.playerNames.get(Game.playerIdx));
      assert tempChar != null;
      tempChar.PickupPump();
    }
    catch(ClassCastException ignored){}
    // TODO
  }

  public static void SetPumpAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  // TODO: other actions for every possible action in the action bar

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
}
