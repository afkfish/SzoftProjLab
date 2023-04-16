package com.ez_mode;

import com.ez_mode.characters.*;
import com.ez_mode.gui.Menu;
import com.ez_mode.objects.*;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {
  private enum Version {
    SKELETON,
    PROTOTYPE,
    GRAPGHICAL
  }

  public static Version version = Version.SKELETON;

  public static void main(String[] args) {
    Logger logger = LogManager.getLogger(Main.class);
    logger.info("Starting");

    if (version == Version.SKELETON) {
      Map map = new Map();
      System.out.println("\033]31mHello! This is the skeleton version of the game.\033[0m\n");

      System.out.println("What do you want to do?");
      System.out.println("add - adds an object or a character to the map");
      System.out.println("map - shows the map");
      System.out.println("help - shows this message");
      System.out.println("exit - exits the program");
      while (true) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        switch (input) {
          case "add":
            {
              System.out.println("What do you want to add?");
              System.out.println("- plumber\n- nomad\n- pipe\n- pump\n- cistern\n- water source");
              switch (scanner.nextLine()) {
                case "plumber":
                  {
                    if (Map.getNodeCount() == 0) {
                      Map.addNode(new Cistern());
                    }
                    Map.addPlayer(new Plumber("Plumber"), Map.getNode(0));
                    logger.info("Added character");
                    break;
                  }
                case "nomad":
                  {
                    if (Map.getNodeCount() == 0) {
                      Map.addNode(new Cistern());
                    }
                    Map.addPlayer(new Nomad("Nomad"), Map.getNode(0));
                    logger.info("Added character");
                    break;
                  }
                case "pipe":
                  {
                    Map.addNode(new Pipe());
                    logger.info("Added pipe");
                    break;
                  }
                case "pump":
                  {
                    Map.addNode(new Pump());
                    logger.info("Added pump");
                    break;
                  }
                case "cistern":
                  {
                    Map.addNode(new Cistern());
                    logger.info("Added cistern");
                    break;
                  }
                case "water source":
                  {
                    Map.addNode(new WaterSpring());
                    logger.info("Added water source");
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
          case "edit":
            {
              System.out.println("What do you want to do?");
              System.out.println("- remove\n- connect");
              switch (scanner.nextLine()) {
                case "remove":
                  {
                    // TODO: implement
                    break;
                  }
                case "connect":
                  {
                    // TODO: implement
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
          case "character":
            {
              System.out.println("What do you want to do?");
              System.out.println("- place\n- move\n- break\n- repair\n- set");
              switch (scanner.nextLine()) {
                case "place":
                  {
                    // TODO: list the characters then the nodes and let the user choose
                    break;
                  }
                case "move":
                  {
                    break;
                  }
                case "break":
                  {
                    break;
                  }
                case "repair":
                  {
                    break;
                  }
                case "set":
                  {
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
              System.out.println("add - adds an object or a character to the map");
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
    } else if (version == Version.GRAPGHICAL) {
      new Menu();
    }
  }
}
