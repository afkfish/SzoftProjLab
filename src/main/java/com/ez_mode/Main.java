package com.ez_mode;

// import com.ez_mode.gui.Menu;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.*;
import java.util.ArrayList;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
  private enum Version {
    SKELETON,
    PROTOTYPE,
    GRAPGHICAL
  }

  public static Version version = Version.PROTOTYPE;

  public static void main(String[] args) {
    Logger logger = LogManager.getLogger(Main.class);
    logger.info("Starting");

    if (version == Version.SKELETON) {
      Map map = new Map(10);
      SkeletonTest skeletonTest = new SkeletonTest();
      System.out.println("Hello! This is the skeleton version of the game.");

      while (true) {
        System.out.println("\nWhat do you want to do?");
        System.out.println("character - shows the character tests");
        System.out.println("map - shows the map in text form");
        System.out.println("help - shows this message");
        System.out.println("exit - exits the program");

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
          case "character":
            {
              System.out.println("What do you want to do?");
              System.out.println("- place\n- pickup\n- move\n- break\n- repair\n- set\n");
              switch (scanner.nextLine()) {
                case "place":
                  {
                    skeletonTest.PlumberDeploysPump();
                    break;
                  }
                case "pickup":
                  {
                    skeletonTest.PlumberPicksUpPipe();
                    skeletonTest.PlumberPicksUpPump();
                    break;
                  }
                case "move":
                  {
                    skeletonTest.CharacterMovesTest();
                    skeletonTest.CharacterMovesToPumpTest();
                    skeletonTest.PlumberMovesToPipeTest();
                    skeletonTest.PlumberMovesToPumpTest();
                    skeletonTest.PlumberMovesToCistern();
                    skeletonTest.NomadMovesToPipeTest();
                    break;
                  }
                case "break":
                  {
                    skeletonTest.NomadBreaksPipeTest();
                    break;
                  }
                case "repair":
                  {
                    skeletonTest.PlumberRepairsPipeTest();
                    skeletonTest.PlumberRepairsPumpTest();
                    break;
                  }
                case "set":
                  {
                    skeletonTest.SetPumpTest();
                    break;
                  }
                default:
                  {
                    System.out.println("Unknown command");
                    break;
                  }
              }
              break;
            }
          case "map":
            {
              System.out.println(map);
              break;
            }
          case "help":
            {
              System.out.println("map - shows the map");
              System.out.println("help - shows this message");
              System.out.println("exit - exits the program");
              break;
            }
          case "exit":
            {
              logger.info("Exiting");
              System.exit(0);
              break;
            }
          default:
            {
              System.out.println("Unknown command");
              break;
            }
        }
      }
    } else if (version == Version.PROTOTYPE) {
      System.out.println("Hello! This is the prototype version of the game.");

      Map map = new Map(10);
      Scanner scanner = new Scanner(System.in);
      while (true) {
        System.out.println("\nWhat do you want to do?");
        System.out.println("fill - fills the map with random objects");
        System.out.println("load - loads a map from a file");
        System.out.println("save - saves the map to a file");
        System.out.println("character - shows the character actions");
        System.out.println("node - shows the node actions");
        System.out.println("add - create new nodes or characters");
        System.out.println("map - shows the map in text form");
        System.out.println("exit - exits the program");

        switch (scanner.nextLine()) {
          case "fill":
            {
              map.fillMap(4);
              break;
            }
          case "load":
            {
              System.out.println("What is the path to the game file?");
              map.clearMap();
              map.loadMap(scanner.nextLine());
              break;
            }
          case "save":
            {
              System.out.println("Where do you want to save the game?");
              map.saveMap(scanner.nextLine());
              break;
            }
          case "character":
            {
              if (Map.playerCount() == 0) {
                System.out.println("There are no characters on the map.");
                System.out.println("Do you want to create a new character? (y/n): ");
                String input = scanner.nextLine();
                if (input.contains("y")) {
                  Character temp = null;
                  System.out.println("What is the name of the character?");
                  String name = scanner.nextLine();
                  System.out.println("What is the type of the character?");
                  System.out.println("1 - Plumber");
                  System.out.println("2 - Nomad");
                  int type = scanner.nextInt();
                  scanner.nextLine();
                  switch (type) {
                    case 1:
                      {
                        temp = new Plumber(name);
                        break;
                      }
                    case 2:
                      {
                        temp = new Nomad(name);
                        break;
                      }
                    default:
                      {
                        System.out.println("Unknown character type!");
                        break;
                      }
                  }
                  if (temp == null) {
                    System.out.println("Character creation aborted!");
                    break;
                  }
                  if (Map.getNodeCount() > 0) {
                    System.out.println("Where do you want to place the character?");
                    Map.printNodes();
                    System.out.println(
                        "Please provide the coordinates of the node like this: x \\n y");
                    int x = Integer.parseInt(scanner.nextLine());
                    int y = Integer.parseInt(scanner.nextLine());
                    Node node = Map.getNode(x, y);
                    Map.addPlayer(temp, node);
                  } else {
                    System.out.println("There are no nodes on the map.");
                    System.out.println("Do you want to create a new node? (y/n): ");
                    String input2 = scanner.nextLine();
                    if (input2.contains("y")) {
                      System.out.println("What is the type of the node?");
                      System.out.println("1 - Pipe");
                      System.out.println("2 - Pump");
                      System.out.println("3 - Cistern");
                      System.out.println("4 - WaterSpring");
                      int type2 = scanner.nextInt();
                      System.out.println("Where do you want to place the node?");
                      System.out.println(
                          "Please provide the coordinates of the node like this: x \\n y");
                      int x = Integer.parseInt(scanner.nextLine());
                      int y = Integer.parseInt(scanner.nextLine());
                      Node node = null;
                      switch (type2) {
                        case 1:
                          {
                            node = new Pipe(x, y);
                            break;
                          }
                        case 2:
                          {
                            node = new Pump(x, y);
                            break;
                          }
                        case 3:
                          {
                            node = new Cistern(x, y);
                            break;
                          }
                        case 4:
                          {
                            node = new WaterSpring(x, y);
                            break;
                          }
                        default:
                          {
                            System.out.println("Unknown node type");
                            System.out.println("Character creation aborted!");
                            break;
                          }
                      }
                      if (node == null) {
                        break;
                      }
                      Map.addPlayer(temp, node);
                      System.out.println("Character succesfuly created!");
                    } else {
                      System.out.println("Character creation aborted!");
                      break;
                    }
                  }
                } else {
                  System.out.println("Character creation aborted!");
                  break;
                }
              } else {
                System.out.println("Select a character:");
                Map.printPlayers();
                int i = Integer.parseInt(scanner.nextLine());
                Character character = Map.getPlayer(i);
                System.out.println("What do you want to do with the character?");
                System.out.println("1 - Move");
                System.out.println("2 - Break");
                System.out.println("3 - Repair");
                System.out.println("4 - Set");
                System.out.println("5 - Pick up");
                System.out.println("6 - Place");
                int action = Integer.parseInt(scanner.nextLine());
                switch (action) {
                  case 1:
                    {
                      if (character.getStandingOn().getNeighbours().isEmpty()) {
                        System.out.println("There are no neighbours to move to.");
                        break;
                      }
                      System.out.println("Where do you want to move with the character?");
                      ArrayList<Node> neighbours = character.getStandingOn().getNeighbours();
                      for (int j = 0; j < neighbours.size(); j++) {
                        Node temp = neighbours.get(j);
                        System.out.println(
                            j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
                      }
                      int index = scanner.nextInt();
                      try {
                        character.moveTo(neighbours.get(index));
                      } catch (ObjectFullException | InvalidPlayerMovementException e) {
                        System.out.println("The character could not move to the selected node.");
                        System.out.println(e.getMessage());
                      }
                      break;
                    }
                  case 2:
                    {
                      System.out.println("Trying to break the node...");
                      character.breakNode();
                      break;
                    }
                  case 3:
                    {
                      System.out.println("Trying to repair the node...");
                      try {
                        Plumber temp = (Plumber) character;
                        temp.repair();
                      } catch (ClassCastException e) {
                        System.out.println("Only plumbers can repair nodes.");
                      }
                      break;
                    }
                  case 4:
                    {
                      ArrayList<Node> neighbours = character.getStandingOn().getNeighbours();
                      if (neighbours.isEmpty()) {
                        System.out.println("There are no neighbours to set the node to.");
                      } else if (neighbours.size() == 1) {
                        System.out.println("There is only one neighbour to set the node to.");
                        System.out.println("Trying to set the node...");
                        try {
                          Pipe temp = (Pipe) neighbours.get(0);
                          character.setPump(temp, null);
                        } catch (ClassCastException e) {
                          System.out.println("The neighbour is not a pipe.");
                        }
                      } else {
                        System.out.println("Select an input pipe:");
                        for (int j = 0; j < neighbours.size(); j++) {
                          Node temp = neighbours.get(j);
                          System.out.println(
                              j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
                        }
                        int index = scanner.nextInt();
                        Pipe input;
                        try {
                          input = (Pipe) neighbours.get(index);
                        } catch (ClassCastException e) {
                          System.out.println("The selected node is not a pipe.");
                          break;
                        }
                        System.out.println("Select an output pipe:");
                        for (int j = 0; j < neighbours.size(); j++) {
                          Node temp = neighbours.get(j);
                          if (temp != input) {
                            System.out.println(
                                j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
                          }
                        }
                        index = scanner.nextInt();
                        Pipe output;
                        try {
                          output = (Pipe) neighbours.get(index);
                        } catch (ClassCastException e) {
                          System.out.println("The selected node is not a pipe.");
                          break;
                        }
                        System.out.println("Trying to set the node...");
                        character.setPump(input, output);
                      }
                      break;
                    }
                  case 5:
                    {
                      System.out.println("Trying to pick up the node...");
                      try {
                        Plumber temp = (Plumber) character;
                        // TODO: Pick up node and move it to the inventory
                        // TODO: move away from the node to a neighbour
                        // if there is no neighbour, the character goes to the first cistern on the
                        // map
                        // playerLostHandler()
                      } catch (ClassCastException e) {
                        System.out.println("Only plumbers can pick up nodes.");
                      }
                      break;
                    }
                }
              }
              break;
            }
          case "node":
            {
              // TODO: node actions
              break;
            }
          case "add":
            {
              // TODO: character creation or node creation
              break;
            }
          case "map":
            {
              // TODO: map actions
              break;
            }
          case "exit":
            {
              System.out.println("Exiting...");
              System.exit(0);
              break;
            }
          default:
            {
              System.out.println("Unknown command");
              break;
            }
        }
      }
    } else if (version == Version.GRAPGHICAL) {
      // new Menu();
    }
  }
}
