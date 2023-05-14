package com.ez_mode;

// import com.ez_mode.gui.Menu;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
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
    GRAPHICAL
  }

  private static boolean running = true;
  private static final StringBuilder logs = new StringBuilder();
  private static final HashMap<String, Runnable> commands = new HashMap<>();
  public static final Map map = new Map(10);
  private static Scanner scanner;
  public static Version version = Version.PROTOTYPE;

  private static void init() {
    commands.put(
        "test",
        () -> new ProtoTest().processCommand(scanner));
    commands.put("fill", () -> Map.fillMap(4));
    commands.put(
        "load",
        () -> {
          log("What is the path to the game file?");
          Map.clearMap();
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
              Character character = createCharacter(scanner);
              if (character == null) {
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
                  Node node = createNode(scanner);
                  if (node != null) {
                    Map.addPlayer(character, node);
                  } else {
                    log("Character creation aborted!");
                  }
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
                    temp.PickupPipe(null);
                  } catch (ClassCastException e) {
                    log("Only plumbers can pick up nodes.");
                  } catch (NotFoundExeption e) {
                    try {
                      Plumber temp = (Plumber) character;
                      temp.PickupPump();
                    } catch (ClassCastException e2) {
                      log("Only plumbers can pick up nodes.");
                    }
                  }
                });
            actions.get(action).run();
          }
        });
    commands.put(
        "node",
        () -> {
          Node node;
          if (Map.getNodeCount() > 0) {
            log("Select a node:");
            Map.printNodes();
            int x = Integer.parseInt(scanner.nextLine());
            int y = Integer.parseInt(scanner.nextLine());
            node = Map.getNode(x, y);
          } else {
            log("There are no nodes on the map.");
            log("Do you want to create a new node? (y/n): ");
            String input = scanner.nextLine();
            if (input.contains("y")) {
              node = createNode(scanner);
            } else {
              log("Node creation aborted!");
              return;
            }
          }
          assert node != null : "Anyad";
          HashMap<Integer, Runnable> actions = new HashMap<>();
          log("What do you want to do with the node?");
          log("1 - break");
          log("2 - repair");
          log("3 - setSurface");
          log("4 - connect");
          log("5 - disconnect");
          Nomad character = new Nomad("Debug Jozsi");
          int action = Integer.parseInt(scanner.nextLine());
          actions.put(
              1,
              () -> {
                log("Trying to break the node...");
                try {
                  node.breakNode(character);
                } catch (InvalidPlayerActionException e) {
                  log(e.getMessage());
                }
              });
          actions.put(
              2,
              () -> {
                log("Trying to repair the node...");
                try {
                  node.repairNode(character);
                } catch (InvalidPlayerActionException e) {
                  log(e.getMessage());
                }
              });
          actions.put(
                  3,
                  () -> {
                    log("Select a surface:");
                    log("1 - sticky");
                    log("2 - slippery");
                    log("3 - normal");
                    int surface = Integer.parseInt(scanner.nextLine());
                    try {
                      if (surface == 1) {
                        node.setSurface("sticky", character);
                      } else if (surface == 2) {
                        node.setSurface("slippery", character);
                      } else {
                        node.setSurface(" " , character);
                      }
                    } catch (InvalidPlayerActionException e) {
                      log(e.getMessage());
                    }
                  });
            actions.put(
                    4,
                    () -> {
                      log("Select an neighbour:");
                      Map.printNodes();
                      int x1 = Integer.parseInt(scanner.nextLine());
                      int y1 = Integer.parseInt(scanner.nextLine());
                      Node node1 = Map.getNode(x1, y1);
                      try {
                        node.connect(node1);
                      } catch (ObjectFullException e) {
                        throw new RuntimeException(e);
                      }
                    });
            actions.put(
                    5,
                    () -> {
                      log("Select an neighbour:");
                      node.getNeighbours().forEach(alma -> log(alma.toString()));
                      int x1 = Integer.parseInt(scanner.nextLine());
                      int y1 = Integer.parseInt(scanner.nextLine());
                      Node node1 = Map.getNode(x1, y1);
                      node.disconnect(node1);
                    });
            actions.get(action).run();
        });
    commands.put(
        "add",
        () -> {
          log("What do you want to add?");
          log("1 - Character");
          log("2 - Node");
          int type = Integer.parseInt(scanner.nextLine());
          if (type == 1) {
            if (createCharacter(scanner) == null) {
              log("Character creation aborted!");
            }
          } else if (type == 2) {
            if (createNode(scanner) == null) {
              log("Node creation aborted!");
            }
          }
        });
    commands.put(
        "map",
        () -> {
          log("What do you want to do with the map?");
          log("1 - Print");
          log("2 - Clear");
          log("3 - Generate");
          int action = Integer.parseInt(scanner.nextLine());
          HashMap<Integer, Runnable> actions = new HashMap<>();
          actions.put(
              1,
              () -> {
                  log("Printing the map...");
                  log(map.toString());
              });
          actions.put(
              2,
              () -> {
                  log("Clearing the map...");
                  Map.clearMap();
              });
          actions.put(
              3,
              () -> {
                  log("Generating the map...");
                  log("How many players do you want to have?");
                  int size = Integer.parseInt(scanner.nextLine());
                  Map.fillMap(size);
              });
          actions.get(action).run();
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
    scanner = new Scanner(System.in);
    if (version == Version.PROTOTYPE) {
      proto(scanner);
    } else if (version == Version.GRAPHICAL) {
      // TODO
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

  private static Node createNode(Scanner scanner) {
    log("What is the type of the node?");
    log("1 - Pipe");
    log("2 - Pump");
    log("3 - Cistern");
    log("4 - WaterSpring");
    int type2 = Integer.parseInt(scanner.nextLine());
    if (type2 < 1 || type2 > 4) {
      log("Unknown node type");
      log("Character creation aborted!");
      return null;
    }
    log("Where do you want to place the node?");
    log("Please provide the coordinates of the node like this: x \\n y");
    int x = Integer.parseInt(scanner.nextLine());
    int y = Integer.parseInt(scanner.nextLine());
    Node node;
    HashMap<Integer, Node> nodeType = new HashMap<>();
    nodeType.put(1, new Pipe(x, y));
    nodeType.put(2, new Pump(x, y));
    nodeType.put(3, new Cistern(x, y));
    nodeType.put(4, new WaterSpring(x, y));
    node = nodeType.get(type2);
    if (node == null) {
      log("Unknown node type");
      log("Node creation aborted!");
      return null;
    }
    Map.addNode(node, node.getX(), node.getY());
    log("Node succesfuly created!");
    return node;
  }

  private static Character createCharacter(Scanner scanner) {
    log("What is the name of the character?");
    String name = scanner.nextLine();
    log("What is the type of the character?");
    log("1 - Plumber");
    log("2 - Nomad");

    int type = Integer.parseInt(scanner.nextLine());

    Character character;
    if (type == 1) {
      character = new Plumber(name);
    } else if (type == 2) {
      character = new Nomad(name);
    } else {
      log("Unknown character type!");
      log("Character creation aborted!");
      return null;
    }
    log("Character succesfuly created!");
    return character;
  }

  private static void proto(Scanner scanner) {
    init();
    log("Hello! This is the prototype version of the game.");

    while (running) {
      log("\nWhat do you want to do?");
      log("test - runs the tests");
      log("fill - fills the map with random objects");
      log("load - loads a map from a file");
      log("save - saves the map to a file");
      log("character - shows the character actions");
      log("node - shows the node actions");
      log("add - create new nodes or characters");
      log("map - shows the map in text form");
      log("exit - exits the program");

      try {
        String line = scanner.nextLine();
        Runnable action = commands.get(line);
        action.run();
      } catch (Exception e) {
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
  }
}
