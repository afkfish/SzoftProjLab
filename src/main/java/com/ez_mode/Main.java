package com.ez_mode;

// import com.ez_mode.gui.Menu;
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
    } else if (version == Version.GRAPGHICAL) {
      // new Menu();
    }
  }
}
