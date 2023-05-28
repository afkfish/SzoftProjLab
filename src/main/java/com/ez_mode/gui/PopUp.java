package com.ez_mode.gui;

import javax.swing.*;

public class PopUp {

  Object[] options = {"Up-Down", "Left-Right"};

  int choice =
      JOptionPane.showOptionDialog(
          null,
          "Which direction do you prefer?",
          "Direction",
          JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE,
          null,
          options,
          options[0]);

  public PopUp() {
    if (choice == 0) {
      System.out.println("User chose Up-Down");
      Controller.setChoice = 0;
    } else if (choice == 1) {
      System.out.println("User chose Left-Right");
      Controller.setChoice = 1;
    }
  }
}
