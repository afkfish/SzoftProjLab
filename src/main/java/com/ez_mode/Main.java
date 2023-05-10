package com.ez_mode;

// import com.ez_mode.gui.Menu;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
  private enum Version {
    SKELETON,
    PROTOTYPE,
    GRAPGHICAL
  }

  private static final HashMap<String, Runnable> commands = new HashMap<>();
  private static final Map map = new Map(10);

  public static Version version = Version.PROTOTYPE;

  private static void init() {
    Scanner scanner = new Scanner(System.in);
    commands.put("fill", () -> map.fillMap(4));
    commands.put("load", () -> {
      System.out.println("What is the path to the game file?");
      map.clearMap();
      map.loadMap(scanner.nextLine());
    });
    commands.put("save", () -> {
      System.out.println("Where do you want to save the game?");
      map.saveMap(scanner.nextLine());
    });
    commands.put("character", () -> {
      if (Map.playerCount() == 0) {
        System.out.println("There are no characters on the map.");
        System.out.println("Do you want to create a new character? (y/n): ");
        String input = scanner.nextLine();
        if (input.contains("y")) {
          System.out.println("What is the name of the character?");
          String name = scanner.nextLine();
          System.out.println("What is the type of the character?");
          System.out.println("1 - Plumber");
          System.out.println("2 - Nomad");

          int type = Integer.parseInt(scanner.nextLine());

          Character character = null;
          if (type == 1) {
            character = new Plumber(name);
          } else if (type == 2) {
            character = new Nomad(name);
          } else {
            System.out.println("Unknown character type!");
            System.out.println("Character creation aborted!");
            return;
          }

          if (Map.getNodeCount() > 0) {
            System.out.println("Where do you want to place the character?");
            Map.printNodes();
            System.out.println(
                    "Please provide the coordinates of the node like this: x \\n y");
            int x = Integer.parseInt(scanner.nextLine());
            int y = Integer.parseInt(scanner.nextLine());
            Node node = Map.getNode(x, y);
            Map.addPlayer(character, node);
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
              int type2 = Integer.parseInt(scanner.nextLine());
              if (type2 < 1 || type2 > 4) {
                System.out.println("Unknown node type");
                System.out.println("Character creation aborted!");
                return;
              }
              System.out.println("Where do you want to place the node?");
              System.out.println(
                      "Please provide the coordinates of the node like this: x \\n y");
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
                System.out.println("Unknown node type");
                System.out.println("Character creation aborted!");
                return;
              }
              Map.addPlayer(character, node);
              System.out.println("Character succesfuly created!");
            } else {
              System.out.println("Character creation aborted!");
            }
          }
        } else {
          System.out.println("Character creation aborted!");
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
        HashMap<Integer, Runnable> actions = new HashMap<>();
        actions.put(1, () -> {
          if (character.getStandingOn().getNeighbours().isEmpty()) {
            System.out.println("There are no neighbours to move to.");
            return;
          }
          System.out.println("Where do you want to move with the character?");
          ArrayList<Node> neighbours = character.getStandingOn().getNeighbours();
          int index = printNeighbours(scanner, neighbours);
          try {
            character.moveTo(neighbours.get(index));
          } catch (ObjectFullException | InvalidPlayerMovementException e) {
            System.out.println("The character could not move to the selected node.");
            System.out.println(e.getMessage());
          }
        });
        actions.put(2, () -> {
          if (character.getStandingOn().getNeighbours().isEmpty()) {
            System.out.println("There are no neighbours to break.");
            return;
          }
          System.out.println("Trying to break the node...");
          character.breakNode();
        });
        actions.put(3, () -> {
          System.out.println("Trying to repair the node...");
          try {
            Plumber temp = (Plumber) character;
            temp.repair();
          } catch (ClassCastException e) {
            System.out.println("Only plumbers can repair nodes.");
          }
        });
        actions.put(4, () -> {
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
            int index = printNeighbours(scanner, neighbours);
            Pipe input;
            try {
              input = (Pipe) neighbours.get(index);
            } catch (ClassCastException e) {
              System.out.println("The selected node is not a pipe.");
              return;
            }
            System.out.println("Select an output pipe:");
            for (int j = 0; j < neighbours.size(); j++) {
              Node temp = neighbours.get(j);
              if (temp != input) {
                System.out.println(
                        j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
              }
            }
            index = Integer.parseInt(scanner.nextLine());
            Pipe output;
            try {
              output = (Pipe) neighbours.get(index);
            } catch (ClassCastException e) {
              System.out.println("The selected node is not a pipe.");
              return;
            }
            System.out.println("Trying to set the node...");
            character.setPump(input, output);
          }
        });
        actions.put(5, () -> {
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
        });
        actions.get(action).run();
      }
    });
    commands.put("node", () -> {
      // TODO: node actions
    });
    commands.put("add", () -> {
      // TODO: character creation or node creation
    });
    commands.put("map", () -> {
      // TODO: map actions
    });
    commands.put("exit", () -> {
      System.out.println("Exiting...");
      System.exit(0);
    });
  }

  private static int printNeighbours(Scanner scanner, ArrayList<Node> neighbours) {
    for (int j = 0; j < neighbours.size(); j++) {
      Node temp = neighbours.get(j);
      System.out.println(
              j + " - " + temp + " - [" + temp.getX() + ", " + temp.getY() + "]");
    }
    return Integer.parseInt(scanner.nextLine());
  }

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
      init();
      System.out.println("Hello! This is the prototype version of the game.");
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

        Runnable action = commands.get(scanner.nextLine());
        if (action != null) {
          action.run();
        } else {
          System.out.println("Unknown command");
        }
      }
    } else if (version == Version.GRAPGHICAL) {
      // new Menu();
    }
  }
}
