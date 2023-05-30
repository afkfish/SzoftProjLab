package com.ez_mode.gui;

import javax.swing.*;

import java.util.HashSet;
import java.util.Set;

import static com.ez_mode.gui.Controller.game;

public class PopUp {

  Object[] options = {"Pick up", "Place", "Up", "Down", "Left", "Right"};

  Set<Integer> choices = new HashSet<>();

  public PopUp() {
    int maxSelections = 2;

    while (choices.size() < maxSelections) {
      int choice = JOptionPane.showOptionDialog(
              null,
              "Pick up or place the node and its direction: (Select " + (maxSelections - choices.size()) + " more)",
              "Direction",
              JOptionPane.DEFAULT_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              options,
              options[0]);

      if (choice >= 0 && choice < options.length) {
        choices.add(choice);
        System.out.println("User chose: " + options[choice]);
      } else {
        break; // Dialog was closed or an invalid option was selected
      }
    }

    // Handle the selected choices
    for (int choice : choices) {
      if (choice == 0) {
        System.out.println("User chose Pick Up");
        Controller.setChoice = 0;
      } else if (choice == 1) {
        System.out.println("User chose Place");
        Controller.setChoice = 1;
      } else if (choice == 2) {
        System.out.println("User Chose Up");
      } else if (choice == 3) {
        System.out.println("User Chose Down");
      } else if (choice == 4) {
        System.out.println("User Chose Left");
      }else if (choice == 3) {
        System.out.println("User Chose Right");
      }

    }
  }
}
