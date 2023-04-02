package com.ez_mode;

import com.ez_mode.characters.*;
import com.ez_mode.gui.Menu;

import com.ez_mode.objects.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

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
            while (true) {
                System.out.println("\033]31mHello! This is the skeleton version of the game.\033[0m\n");

                System.out.println("What do you want to do?");
                System.out.println("add - adds an object or a character to the map");
                System.out.println("map - shows the map");
                System.out.println("help - shows this message");
                System.out.println("exit - exits the program");

                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();
                switch (input) {
                    case "add" -> {
                        System.out.println("What do you want to add?");
                        System.out.println("""
                                - plumber
                                - nomad
                                - pipe
                                - pump
                                - cistern
                                - water source
                                """
                        );
                        switch (scanner.nextLine()) {
                            case "plumber" -> {
                                if (Map.getNodeCount() == 0) {
                                    Map.addNode(new Cistern());
                                }
                                Map.addPlayer(new Plumber("Plumber"), Map.getNode(0));
                                logger.info("Added character");
                            }
                            case "nomad" -> {
                                if (Map.getNodeCount() == 0) {
                                    Map.addNode(new Cistern());
                                }
                                Map.addPlayer(new Nomad("Nomad"), Map.getNode(0));
                                logger.info("Added character");
                            }
                            case "pipe" -> {
                                Map.addNode(new Pipe());
                                logger.info("Added pipe");
                            }
                            case "pump" -> {
                                Map.addNode(new Pump());
                                logger.info("Added pump");
                            }
                            case "cistern" -> {
                                Map.addNode(new Cistern());
                                logger.info("Added cistern");
                            }
                            case "water source" -> {
                                Map.addNode(new WaterSpring());
                                logger.info("Added water source");
                            }
                            default -> System.out.println("Unknown command");
                        }
                    }
                    case "edit" -> {
                        switch (scanner.nextLine()) {
                            case "remove", "connect" -> {
                                // TODO: implement
                            }
                            default -> System.out.println("Unknown command");
                        }
                    }
                    case "character" -> {
                        switch (scanner.nextLine()) {
                            case "place", "move", "break", "repair", "set" -> {
                                // TODO: list the characters then the nodes and let the user choose
                            }
                            default -> System.out.println("Unknown command");
                        }
                    }
                    case "map" -> System.out.println(map);
                    case "help" -> {
                        System.out.println("add - adds an object or a character to the map");
                        System.out.println("map - shows the map");
                        System.out.println("help - shows this message");
                        System.out.println("exit - exits the program");
                    }
                    case "exit" -> {
                        logger.info("Exiting");
                        System.exit(0);
                    }
                    default -> System.out.println("Unknown command");
                }
            }
        } else if (version == Version.GRAPGHICAL) {
            new Menu();
        }
    }
  }
}
