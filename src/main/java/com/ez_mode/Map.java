package com.ez_mode;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.notJson.NotJSONArray;
import com.ez_mode.notJson.NotJSONObject;
import com.ez_mode.notJson.NotJSONTokener;
import com.ez_mode.objects.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for the map of the game. It contains a HashMap of StandableObjects and
 * the Characters standing on them. It also contains a method to handle the case when a player is
 * lost somehow.
 */
public class Map implements Tickable {

  private final Logger logger = LogManager.getLogger(Map.class);

  /**
   * The ArrayList representation of the game. This map contains every object. TODO: store the
   * objects with their coordinates
   */
  private static Node[][] gameMap = null;

  /** The list of all players. */
  private static final ArrayList<Character> players = new ArrayList<>();

  /** The amount of water, lost by the nomads sabotaging the nodes. */
  public static double waterLost = 0;

  /** The amount of water, arrived to the cisterns. */
  public static double waterArrived = 0;

  public Map(int size) {
    gameMap = new Node[size][size];
  }

  /**
   * This method fills the map with the objects and places the characters to their startiing
   * positions.
   */
  public void fillMap(int playerCount) {
    System.out.println("Filling map with random objects...");

    gameMap = new Node[10][10];

    // evenly distribute the players into two teams
    int plumberCount = 0;
    if (playerCount % 2 == 0) {
      plumberCount = playerCount / 2;
    } else {
      plumberCount = playerCount / 2 + 1;
    }
    for (int i = 0; i < plumberCount; i++) {
      players.add(new Plumber("plumber" + (i + 1)));
    }
    for (int i = 0; i < playerCount - plumberCount; i++) {
      players.add(new Nomad("nomad" + (i + 1)));
    }

    // create one of each node, to make sure we have each type on the map
    gameMap[0][0] = new Cistern(0, 0);
    gameMap[0][1] = new WaterSpring(0, 1);
    gameMap[0][2] = new Pipe(0, 2);
    gameMap[0][3] = new Pump(0, 3);

    // create the rest of the map
    for (int i = 0; i < 10; i++) {
      for (int j = 4; j < 10; j++) {
        Random rand = new Random();
        int randomInt = rand.nextInt(100);
        if (randomInt <= 60) {
          gameMap[i][j] = new Pipe(i, j);
        } else if (randomInt <= 80) {
          gameMap[i][j] = new Pump(i, j);
        } else if (randomInt <= 90) {
          gameMap[i][j] = new Cistern(i, j);
        } else {
          gameMap[i][j] = new WaterSpring(i, j);
        }
      }
    }

    // place the characters
    for (int i = 0; i < playerCount; i++) {
      Random random = new Random();
      boolean success = false;
      while (!success) {
        int row = random.nextInt(10);
        int col = random.nextInt(10);
        if (gameMap[row][col] != null && gameMap[row][col].getCharacters().isEmpty()) {
          players.get(i).placeTo(gameMap[row][col]);
          success = true;
        }
      }
    }
  }

  public void loadMap(String path) {
    System.out.println("Loading map...");
    if (!path.endsWith(".json")) {
      System.out.println("The file must be a .json configuration file!");
      return;
    }
    try (FileInputStream fileInputStream = new FileInputStream("testMap.json")) {
      NotJSONTokener root = new NotJSONTokener(fileInputStream);
      NotJSONObject rootObject = new NotJSONObject(root);

      NotJSONArray nodeList = rootObject.getNotJSONArray("map");
      for (int i = 0; i < nodeList.length(); i++) {
        NotJSONObject node = nodeList.getJSONObject(i);
        Node temp;
        switch (node.getString("type")) {
          case "cistern":
            {
              temp = new Cistern(node.getInt("x"), node.getInt("y"));
              break;
            }
          case "pipe":
            {
              temp = new Pipe(node.getInt("x"), node.getInt("y"));
              break;
            }
          case "pump":
            {
              temp = new Pump(node.getInt("x"), node.getInt("y"));
              break;
            }
          case "waterspring":
            {
              temp = new WaterSpring(node.getInt("x"), node.getInt("y"));
              break;
            }
          default:
            {
              System.out.println("Unknown node type!");
              continue;
            }
        }
        gameMap[node.getInt("x")][node.getInt("y")] = temp;
        System.out.println(node);
      }

      // iterate over the nodes again and set the connections
      for (int i = 0; i < nodeList.length(); i++) {
        NotJSONObject node = nodeList.getJSONObject(i);
        Node temp = gameMap[node.getInt("x")][node.getInt("y")];
        if (node.getString("type").equals("waterspring")
            || node.getString("type").equals("cistern")) {
          continue;
        }
        NotJSONArray connections = node.getNotJSONArray("connections");

        for (int j = 0; j < connections.length(); j++) {
          NotJSONObject connection = connections.getJSONObject(j);
          Node neighbour = gameMap[connection.getInt("x")][connection.getInt("y")];
          if (neighbour == null) {
            System.out.println(
                "There is no node at the given coordinates: "
                    + connection.getInt("x")
                    + ", "
                    + connection.getInt("y")
                    + "!");
            System.out.println("Skipping connection...");
            continue;
          }
          temp.connect(neighbour);
        }
      }

      NotJSONArray playerList = rootObject.getNotJSONArray("players");
      for (int i = 0; i < playerList.length(); i++) {
        NotJSONObject player = playerList.getJSONObject(i);
        Character temp;
        switch (player.getString("type")) {
          case "plumber":
            {
              temp = new Plumber(player.getString("name"));
              break;
            }
          case "nomad":
            {
              temp = new Nomad(player.getString("name"));
              break;
            }
          default:
            {
              System.out.println("Unknown player type!");
              continue;
            }
        }
        System.out.println(player);
        players.add(temp);
        temp.placeTo(gameMap[player.getInt("x")][player.getInt("y")]);
      }
      System.out.println("Map loaded successfully!");
    } catch (SecurityException | IOException e) {
      System.out.println("There was an error loading the map!");
    } catch (ObjectFullException e) {
      System.out.println(
          "Some objects are full and cannot have more connections! The map is invalid!");
    }
  }

  public void saveMap(String path) {
    System.out.println("Saving map...");
    assert path.endsWith(".json") : "The file must be a .json configuration file!";
    try (FileOutputStream fileOutputStream = new FileOutputStream(path)) {
      // create the root object
      NotJSONObject root = new NotJSONObject();
      root.put("size", gameMap.length);
      NotJSONArray playerList = new NotJSONArray();
      // iterate over the players and add them to the list
      for (Character player : players) {
        NotJSONObject playerObject = new NotJSONObject();
        playerObject.put("name", player.getName());
        playerObject.put("type", player.getClass().getSimpleName().toLowerCase());
        playerObject.put("x", player.getStandingOn().getX());
        playerObject.put("y", player.getStandingOn().getY());
        playerObject.put("inventory", new NotJSONObject());
        playerList.put(playerObject);
      }
      NotJSONArray nodeList = new NotJSONArray();
      // iterate over the nodes and add them to the list
      for (int i = 0; i < gameMap.length; i++) {
        for (int j = 0; j < gameMap[i].length; j++) {
          if (gameMap[i][j] == null) {
            continue;
          }
          NotJSONObject node = new NotJSONObject();
          node.put("type", gameMap[i][j].getClass().getSimpleName().toLowerCase());
          node.put("x", i);
          node.put("y", j);
          if (node.getString("type").equals("waterspring")
              || node.getString("type").equals("cistern")) {
            nodeList.put(node);
            continue;
          }
          NotJSONArray connections = new NotJSONArray();
          for (Node neighbour : gameMap[i][j].getNeighbours()) {
            NotJSONObject connection = new NotJSONObject();
            connection.put("x", neighbour.getX());
            connection.put("y", neighbour.getY());
            connections.put(connection);
          }
          node.put("connections", connections);
          nodeList.put(node);
        }
      }

      root.put("players", playerList);
      root.put("map", nodeList);

      fileOutputStream.write(root.toString(2).getBytes());
      System.out.println("Map saved successfully!");
    } catch (IOException e) {
      System.out.println("There was an error saving the map!");
    }
  }

  /**
   * This method adds a node to the map.
   *
   * @param node the node to be added
   */
  public static void addNode(Node node, int x, int y) {
    gameMap[x][y] = node;
  }

  public static int playerCount() {
    return players.size();
  }

  public static void addPlayer(Character player, Node node) {
    players.add(player);
    player.placeTo(node);
  }

  public static void removeNode(Node node) { // TODO implement remove logic
  }

  public static Character getPlayer(int index) {
    return players.get(index);
  }

  public static Character getPlayer(String name) {
    for (Character c : players) {
      if (c.getName().equals(name)) {
        return c;
      } else {
        return null;
      }
    }
    return null;
  }

  public static Node getNode(String name) {
    for (Node[] asd : gameMap) {
      for (Node nodi : asd) {
        if (nodi.getUuid().equals(name)) {
          return nodi;
        }
      }
    }
    return null;
  }

  public static void printPlayers() {
    for (int i = 0; i < players.size(); i++) {
      Character player = players.get(i);
      System.out.println(i + " - " + player.getName() + " - " + player.getStandingOn().getUuid());
    }
  }

  public static int getNodeCount() {
    int count = 0;
    for (Node[] columns : gameMap) {
      for (Node node : columns) {
        if (node != null) {
          count++;
        }
      }
    }
    return count;
  }

  public static void printNodes() {
    for (Node[] nodes : gameMap) {
      for (Node node : nodes) {
        if (node != null) {
          System.out.print(node.getX() + ", " + node.getY() + " - " + node.getUuid());
        }
      }
    }
  }

  public static Node getNode(int x, int y) {
    return gameMap[x][y];
  }

  /**
   * If a player character lost somehow, this method will move it to the position it is supposed to
   * be, or to the start position.
   *
   * @param character the player who is lost
   */
  public static void playerLostHandler(Character character) {
    //    Node playerTruePos =
    //        gameMap.stream()
    //            .flatMap(ArrayList::stream)
    //            .filter(node -> node.getCharacters().contains(character))
    //            .findFirst()
    //            .orElse(null);

    // TODO: move to start if null
    //    assert playerTruePos != null;
    //    character.placeTo(playerTruePos);
  }

  public static void clearMap() {
    gameMap = new Node[gameMap.length][gameMap[0].length];
  }

  @Override
  public void tick() {
    for (Node[] nodes : gameMap) {
      for (Node node : nodes) {
        node.tick();
      }
    }
    this.logger.debug("Current water loss: {}", Map.waterLost);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    //    for (Node node : gameMap) {
    //      sb.append(node.toString());
    //      sb.append("\n ");
    //    }
    return sb.toString();
  }
}
