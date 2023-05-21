package com.ez_mode.gui;

import java.awt.event.ActionEvent;

public class Controller {

  public static void MenuStartAction(ActionEvent e) {
    Menu.frame.dispose();
    new Game();
  }

  public static void GameExitAction(ActionEvent e) {
    Game.frame.dispose();
  }
}
