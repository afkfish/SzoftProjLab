package com.ez_mode.gui;

import com.ez_mode.characters.Plumber;

import javax.swing.*;

public class PopUp {

  Object[] options = {"Pick up", "Place"};
  Object[] pickUpDirections = {"Up", "Down", "Left", "Right", "From cistern"};
  Object[] placeDirections = {"Up", "Down", "Left", "Right"};

  static boolean place = false;

  public PopUp(boolean set, Plumber tempPlumber) {
    if (!set) {
      int action =
              JOptionPane.showOptionDialog(
                      null,
                      "Pick up or place the node",
                      "Action",
                      JOptionPane.DEFAULT_OPTION,
                      JOptionPane.QUESTION_MESSAGE,
                      null,
                      options,
                      options[0]);

      place = action == 1;
      if (action == JOptionPane.CLOSED_OPTION) {
        System.out.println("User closed the window");
        return;
      } else if (action == 1) {
        if (tempPlumber.getDraggedpipe() == null && tempPlumber.getPickedUpPipe() == null) {
          JOptionPane.showMessageDialog(null, "The plumber does not have any pipies to place");
          return;
        }
      }
    }

    int direction;
    if (place) {
      direction =
              JOptionPane.showOptionDialog(
                      null,
                      "Specify the direction",
                      "Direction",
                      JOptionPane.DEFAULT_OPTION,
                      JOptionPane.QUESTION_MESSAGE,
                      null,
                      placeDirections,
                      placeDirections[0]);
    } else {
      direction =
              JOptionPane.showOptionDialog(
                      null,
                      "Specify the direction",
                      "Direction",
                      JOptionPane.DEFAULT_OPTION,
                      JOptionPane.QUESTION_MESSAGE,
                      null,
                      pickUpDirections,
                      pickUpDirections[0]);
    }


    // Handle the selected choices
    switch (direction) {
      default -> {
        System.out.println("User chose Up");
        Controller.setChoice = 0;
      }
      case 1 -> {
        System.out.println("User chose Down");
        Controller.setChoice = 1;
      }
      case 2 -> {
        System.out.println("User Chose Left");
        Controller.setChoice = 2;
      }
      case 3 -> {
        System.out.println("User Chose Right");
        Controller.setChoice = 3;
      }
      case 4 -> {
        System.out.println("User Chose From Cistern");
        Controller.setChoice = 4;
      }
    }
  }
}
