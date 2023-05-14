package com.ez_mode;

// import com.ez_mode.gui.Menu;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
  private enum Version {
    SKELETON,
    PROTOTYPE,
    GRAPGHICAL
  }

  private static boolean running = true;
  private static final StringBuilder logs = new StringBuilder();
  private static final HashMap<String, Runnable> commands = new HashMap<>();
  private static final Map map = new Map(10);
  public static Version version = Version.PROTOTYPE;

  private static void init() {
    Scanner scanner = new Scanner(System.in);
    commands.put("fill", () -> map.fillMap(4));
    commands.put(
        "load",
        () -> {
          log("What is the path to the game file?");
          map.clearMap();
          map.loadMap(scanner.nextLine());
        });
    commands.put(
        "save",
        () -> {
          log("Where do you want to save the game?");
          map.saveMap(scanner.nextLine());
        });
    commands.put(
        "character",
        () -> {
          if (Map.playerCount() == 0) {
            log("There are no characters on the map.");
            log("Do you want to create a new character? (y/n): ");
            String input = scanner.nextLine();
            if (input.contains("y")) {
              log("What is the name of the character?");
              String name = scanner.nextLine();
              log("What is the type of the character?");
              log("1 - Plumber");
              log("2 - Nomad");

              int type = Integer.parseInt(scanner.nextLine());

              Character character = null;
              if (type == 1) {
                character = new Plumber(name);
              } else if (type == 2) {
                character = new Nomad(name);
              } else {
                log("Unknown character type!");
                log("Character creation aborted!");
                return;
              }

              if (Map.getNodeCount() > 0) {
                log("Where do you want to place the character?");
                Map.printNodes();
                log("Please provide the coordinates of the node like this: x \\n y");
                int x = Integer.parseInt(scanner.nextLine());
                int y = Integer.parseInt(scanner.nextLine());
                Node node = Map.getNode(x, y);
                Map.addPlayer(character, node);
              } else {
                log("There are no nodes on the map.");
                log("Do you want to create a new node? (y/n): ");
                String input2 = scanner.nextLine();
                if (input2.contains("y")) {
                  log("What is the type of the node?");
                  log("1 - Pipe");
                  log("2 - Pump");
                  log("3 - Cistern");
                  log("4 - WaterSpring");
                  int type2 = Integer.parseInt(scanner.nextLine());
                  if (type2 < 1 || type2 > 4) {
                    log("Unknown node type");
                    log("Character creation aborted!");
                    return;
                  }
                  log("Where do you want to place the node?");
                  log("Please provide the coordinates of the node like this: x \\n y");
                  int x = Integer.parseInt(scanner.nextLine());
                  int y = Integer.parseInt(scanner.nextLine());
                  Node node = null;
                  HashMap<Integer, Node> nodeType = new HashMap<>();
                  nodeType.put(1, new Pipe(x, y));
                  nodeType.put(2, new Pump(x, y));
                  nodeType.put(3, new Cistern(x, y));
                  nodeType.put(4, new WaterSpring(x, y));
                  node = nodeType.get(type2);
                  if (node == null) {
                    log("Unknown node type");
                    log("Character creation aborted!");
                    return;
                  }
                  Map.addPlayer(character, node);
                  log("Character succesfuly created!");
                } else {
                  log("Character creation aborted!");
                }
              }
            } else {
              log("Character creation aborted!");
            }
          } else {
            log("Select a character:");
            Map.printPlayers();
            int i = Integer.parseInt(scanner.nextLine());
            Character character = Map.getPlayer(i);
            log("What do you want to do with the character?");
            log("1 - Move");
            log("2 - Break");
            log("3 - Repair");
            log("4 - Set");
            log("5 - Pick up");
            log("6 - Place");
            int action = Integer.parseInt(scanner.nextLine());
            HashMap<Integer, Runnable> actions = new HashMap<>();
            actions.put(
                1,
                () -> {
                  if (character.getStandingOn().getNeighbours().isEmpty()) {
                    log("There are no neighbours to move to.");
                    return;
                  }
                  log("Where do you want to move with the character?");
                  ArrayList<Node> neighbours = character.getStandingOn().getNeighbours();
                  int index = printNeighbours(scanner, neighbours);
                  try {
                    character.moveTo(neighbours.get(index));
                  } catch (ObjectFullException | InvalidPlayerMovementException e) {
                    log("The character could not move to the selected node.");
                    log(e.getMessage());
                  }
                });
            actions.put(
                2,
                () -> {
                  if (character.getStandingOn().getNeighbours().isEmpty()) {
                    log("There are no neighbours to break.");
                    return;
                  }
                  log("Trying to break the node...");
                  character.breakNode();
                });
            actions.put(
                3,
                () -> {
                  log("Trying to repair the node...");
                  try {
                    Plumber temp = (Plumber) character;
                    temp.repair();
                  } catch (ClassCastException e) {
                    log("Only plumbers can repair nodes.");
                  }
                });
            actions.put(
                4,
                () -> {
                  ArrayList<Node> neighbours = character.getStandingOn().getNeighbours();
                  if (neighbours.isEmpty()) {
                    log("There are no neighbours to set the node to.");
                  } else if (neighbours.size() == 1) {
                    log("There is only one neighbour to set the node to.");
                    log("Trying to set the node...");
                    try {
                      Pipe temp = (Pipe) neighbours.get(0);
                      character.setPump(temp, null);
                    } catch (ClassCastException e) {
                      log("The neighbour is not a pipe.");
                    }
                  } else {
                    log("Select an input pipe:");
                    int index = printNeighbours(scanner, neighbours);
                    Pipe input;
                    try {
                      input = (Pipe) neighbours.get(index);
                    } catch (ClassCastException e) {
                      log("The selected node is not a pipe.");
                      return;
                    }
                    log("Select an output pipe:");
                    for (int j = 0; j < neighbours.size(); j++) {
                      Node temp = neighbours.get(j);
                      if (temp != input) {
                        log(j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
                      }
                    }
                    index = Integer.parseInt(scanner.nextLine());
                    Pipe output;
                    try {
                      output = (Pipe) neighbours.get(index);
                    } catch (ClassCastException e) {
                      log("The selected node is not a pipe.");
                      return;
                    }
                    log("Trying to set the node...");
                    character.setPump(input, output);
                  }
                });
            actions.put(
                5,
                () -> {
                  log("Trying to pick up the node...");
                  try {
                    Plumber temp = (Plumber) character;
                    // TODO: Pick up node and move it to the inventory
                    // TODO: move away from the node to a neighbour
                    // if there is no neighbour, the character goes to the first cistern on the
                    // map
                    // playerLostHandler()
                  } catch (ClassCastException e) {
                    log("Only plumbers can pick up nodes.");
                  }
                });
            actions.get(action).run();
          }
        });
    commands.put(
        "node",
        () -> {
          // TODO: node actions
        });
    commands.put(
        "add",
        () -> {
          // TODO: character creation or node creation
        });
    commands.put(
        "map",
        () -> {
          // TODO: map actions
        });
    commands.put(
        "exit",
        () -> {
          log("Exiting...");
          running = false;
        });
  }

  private static int printNeighbours(Scanner scanner, ArrayList<Node> neighbours) {
    for (int j = 0; j < neighbours.size(); j++) {
      Node temp = neighbours.get(j);
      log(j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
    }
    return Integer.parseInt(scanner.nextLine());
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
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
              System.out.println("Exiting");
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
      init();
      log("Hello! This is the prototype version of the game.");

      while (running) {
        log("\nWhat do you want to do?");
        log("fill - fills the map with random objects");
        log("load - loads a map from a file");
        log("save - saves the map to a file");
        log("character - shows the character actions");
        log("node - shows the node actions");
        log("add - create new nodes or characters");
        log("map - shows the map in text form");
        log("exit - exits the program");

        Runnable action = commands.get(scanner.nextLine());
        if (action != null) {
          action.run();
        } else {
          log("Unknown command");
        }
      }

      log("Do you want to write out the log to a file? (y/n)");
      String input = scanner.nextLine();
      if (input.equals("y")) {
        log("Specify the file name:");
        input = scanner.nextLine();
        saveLog(input);
      }
    } else if (version == Version.GRAPGHICAL) {
      // new Menu();
    }
  }

  public static void log(String message) {
    logs.append(message).append("\n");
    System.out.println(message);
  }

  private static void saveLog(String fileName) {
    fileName = fileName.endsWith(".txt") ? fileName : fileName + ".txt";
    try (PrintWriter out = new PrintWriter(fileName)) {
      out.println(logs);
      log("Log saved to " + fileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
