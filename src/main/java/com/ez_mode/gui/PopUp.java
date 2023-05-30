package com.ez_mode.gui;

import javax.swing.*;

public class PopUp {

  Object[] options = {"Up","Down", "Left", "Right"};

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
      System.out.println("User chose Up");
      Controller.setChoice = 0;
    } else if (choice == 1) {
      System.out.println("User chose Down");
      Controller.setChoice = 1;
    } else if(choice == 2){
      System.out.println("User Chose Left");
    }
    else if(choice == 3){
      System.out.println("User Chose Right");
    }
  }
}
