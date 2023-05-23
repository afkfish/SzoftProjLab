package com.ez_mode.gui;

import com.ez_mode.Map;

import java.awt.event.ActionEvent;

public class Controller {

  /**
   * Actions in Menu class
   *
   * @param e action event when a button is pressed
   */
  public static void MenuStartAction(ActionEvent e) {
    Menu.frame.dispose();
    EndGame.frame.dispose();
    if (Menu.playerCountTextField.getText().isEmpty()) {
      Menu.playerCount = 2;
    } else {
      Menu.playerCount = Integer.parseInt(Menu.playerCountTextField.getText());
    }
    Menu.loadedPath = Menu.loadTextField.getText();
    // TODO: Creat/load the map, add the players
    Map.fillMap(Menu.playerCount);
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
    // TODO
  }

  public static void MoveLeftAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void MoveDownAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void MoveRightAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void RepairAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void BreakAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void StickyAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void SlipperyAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void PickUpPipeAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
    // TODO
  }

  public static void PickUpPumpAction(ActionEvent e) {
    Game.nomadTurn = !Game.nomadTurn;
    Game.updateAction();
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
