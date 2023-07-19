package com.ez_mode;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.gui.Game;
import com.ez_mode.notJson.NotJSONArray;
import com.ez_mode.notJson.NotJSONObject;
import com.ez_mode.notJson.NotJSONTokener;
import com.ez_mode.objects.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * This class is responsible for the map of the game. It contains a HashMap of StandableObjects and
 * the Characters standing on them. It also contains a method to handle the case when a player is
 * lost somehow.
 */
public class Map implements Tickable {

  /** The list of all players. */
  private static final ArrayList<Character> players = new ArrayList<>();

  /** The amount of water, lost by the nomads sabotaging the nodes. */
  public static double waterLost = 0;

  /** The amount of water, arrived to the cisterns. */
  public static double waterArrived = 0;

  /** The ArrayList representation of the game. This map contains every object. */
  private static Node[][] gameMap = null;

  public Map(int size) {
    gameMap = new Node[size][size];
  }

  public static int getMapSize() {
    return gameMap[0].length;
  }

  /**
   * This method fills the map with the objects and places the characters to their startiing
   * positions.
   */
  public static void fillMap(int playerCount) {
    Main.log("Filling map with random objects...");

    gameMap = new Node[10][10];
    // creates the players
    // each team has playersCount players
    for (int i = 0; i < playerCount; i++) {
      players.add(new Plumber(Game.plumberNames.remove(0)));
      players.add(new Nomad(Game.nomadNames.remove(0)));
    }

    // lists for the different generated nodes
    ArrayList<Node> nodes = new ArrayList<>();
    ArrayList<Pipe> pipes = new ArrayList<>();
    LinkedList<Node> startPPositions = new LinkedList<>();
    LinkedList<Node> startNPositions = new LinkedList<>();

    Random rand = new Random();
    // generates the different node types
    for (int i = 0; i < Game.gridNum; i++) {
      for (int j = 0; j < Game.gridNum; j++) {
        int randomInt = rand.nextInt(100);
        if (j == 0) {
          if (i % 2 == 0) {
            Cistern c = new Cistern(i, j);
            nodes.add(c);
            startPPositions.add(c);
          }
        } else if (j == 9) {
          if ((i + 2) % 2 == 0) {
            WaterSpring w = new WaterSpring(i, j);
            nodes.add(w);
            startNPositions.add(w);
          }
        } else if (j == 1 || j == 7) {
          if (i % 2 == 0) {
            // gameMap[i][j] = new Pipe(i, j);
            pipes.add(new Pipe(i, j));
          }

        } else { // leaves the place empty
          if (20 <= randomInt && randomInt <= 80) {
            // gameMap[i][j] = new Pipe(i, j);
            pipes.add(new Pipe(i, j));
          } else if (80 <= randomInt) {
            nodes.add(new Pump(i, j));
          }
        }
      }
    }
    for (Pipe pipe : pipes) {
      for (Node node : nodes) {
        // connect to a not full neighbour
        connectIfNeighbouring(pipe, node);
        try {
          // if the  node is a pump, then try to connect it to its neighbours
          Pump p = (Pump) node;
          if (p.getNeighbours().size() == 1) {
            p.setActiveOutput((Pipe) p.getNeighbours().get(0));
          } else if (p.getNeighbours().size() > 1) {
            p.setActiveInput((Pipe) p.getNeighbours().get(p.getNeighbours().size() - 1));
            p.setActiveOutput((Pipe) p.getNeighbours().get(p.getNeighbours().size() - 2));
          }
        } catch (ClassCastException ignored) {
        }
      }
      // connect the pipes to other pipes if possibles
      for (Pipe otherPipe : pipes) {
        connectIfNeighbouring(otherPipe, pipe);
      }
      // add the pipe to the map
      nodes.add(pipe);
    }
    // set the nodes on the map
    for (Node node : nodes) {
      gameMap[node.getX()][node.getY()] = node;
    }

    // place the characters
    for (Character player : players) {
      try {
        // try to cast the player to a nomad
        Nomad ignored = (Nomad) player;
        player.placeTo(startNPositions.removeFirst());
      } catch (ClassCastException ignored) {
        // if it fails, then it is a plumber
        player.placeTo(startPPositions.removeFirst());
      }
    }

    Main.log("Map filled!");
  }

  /**
   * Connects a pipe to a node if they are neighbours. Used in fillMap to connect the generated map.
   *
   * @param pipe the pipe we want to connect to
   * @param node the node we want to connect
   */
  private static void connectIfNeighbouring(Pipe pipe, Node node) {
    if ((((node.getX() == pipe.getX() - 1 || node.getX() == pipe.getX() + 1)
                && (node.getY() == pipe.getY()))
            || ((node.getY() == pipe.getY() - 1 || node.getY() == pipe.getY() + 1)
                && (node.getX() == pipe.getX())
                && !pipe.getNeighbours().contains(node)))
        && !pipe.fullOfConn()) {
      try {
        node.connect(pipe);
      } catch (ObjectFullException e) {
        Main.log(e.getMessage());
      }
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

  public static void removeNode(Node ignored) {}

  public static Character getPlayer(int index) {
    index = index % players.size();
    return players.get(index);
  }

  public static ArrayList<Character> getPlayers() {
    return players;
  }

  /**
   * Gives a player by its name
   *
   * @param name the name of the player
   * @return a player
   */
  public static Character getPlayer(String name) {
    for (Character c : players) {
      if (c.getName().equals(name)) {
        return c;
      }
    }
    return null;
  }

  /**
   * Gives a node by its name
   *
   * @param name name of the node
   * @return a node
   */
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

  /** Prints the players on the map with an index */
  public static void printPlayers() {
    for (int i = 0; i < players.size(); i++) {
      Character player = players.get(i);
      Main.log(i + " - " + player.getName() + " - " + player.getStandingOn().getUuid());
    }
  }

  /**
   * The node count on the map
   *
   * @return node count
   */
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

  /** Prints the nodes with an index */
  public static void printNodes() {
    for (Node[] nodes : gameMap) {
      for (Node node : nodes) {
        if (node != null) {
          Main.log(node.getX() + ", " + node.getY() + " - " + node.getUuid());
        }
      }
    }
  }

  public static Node getNode(int x, int y) {
    return gameMap[x][y];
  }

  /** Clears the map */
  public static void clearMap() {
    gameMap = new Node[gameMap.length][gameMap[0].length];
  }

  /**
   * Loads a map from a file
   *
   * @param path the file's path
   */
  public static void loadMap(String path) {
    Main.log("Loading map...");
    if (!path.endsWith(".json")) {
      Main.log("The file must be a .json configuration file!");
      return;
    }
    try (FileInputStream fileInputStream = new FileInputStream(path)) {
      NotJSONTokener root = new NotJSONTokener(fileInputStream);
      NotJSONObject rootObject = new NotJSONObject(root);

      NotJSONArray nodeList = rootObject.getNotJSONArray("map");
      for (int i = 0; i < nodeList.length(); i++) {
        NotJSONObject node = nodeList.getJSONObject(i);
        Node temp;
        switch (node.getString("type")) {
          case "cistern" -> temp = new Cistern(node.getInt("x"), node.getInt("y"));
          case "pipe" -> temp = new Pipe(node.getInt("x"), node.getInt("y"));
          case "pump" -> temp = new Pump(node.getInt("x"), node.getInt("y"));
          case "waterspring" -> temp = new WaterSpring(node.getInt("x"), node.getInt("y"));
          default -> {
            Main.log("Unknown node type!");
            continue;
          }
        }
        gameMap[node.getInt("x")][node.getInt("y")] = temp;
        Main.log(String.valueOf(node));
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
            Main.log(
                "There is no node at the given coordinates: "
                    + connection.getInt("x")
                    + ", "
                    + connection.getInt("y")
                    + "!");
            Main.log("Skipping connection...");
            continue;
          }
          try {
            temp.connect(neighbour);
          } catch (ObjectFullException e) {
            Main.log("The node is full!");
            Main.log("Skipping connection...");
          }
        }
      }

      NotJSONArray playerList = rootObject.getNotJSONArray("players");
      for (int i = 0; i < playerList.length(); i++) {
        NotJSONObject player = playerList.getJSONObject(i);
        Character temp;
        switch (player.getString("type")) {
          case "plumber" -> temp = new Plumber(player.getString("name"));
          case "nomad" -> temp = new Nomad(player.getString("name"));
          default -> {
            Main.log("Unknown player type!");
            continue;
          }
        }
        Main.log(String.valueOf(player));
        players.add(temp);
        temp.placeTo(gameMap[player.getInt("x")][player.getInt("y")]);
      }
      Main.log("Map loaded successfully!");
    } catch (SecurityException | IOException e) {
      Main.log("There was an error loading the map!");
    }
  }

  /**
   * Saves the map to a file
   *
   * @param path the file's path
   */
  public static void saveMap(String path) {
    Main.log("Saving map...");
    if (path.isEmpty()) {
      Main.log("The path cannot be empty!");
      return;
    }
    if (!path.endsWith(".json")) path += ".json";
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
      Main.log("Map saved successfully!");
    } catch (IOException e) {
      Main.log("There was an error saving the map!");
    }
  }

  /** ticks every node on map */
  @Override
  public void tick() {
    Arrays.stream(gameMap).forEach((Node[] col) -> Arrays.stream(col).forEach(node -> {if(node != null) node.tick();}));
//    for (Node[] nodes : gameMap) {
//      for (Node node : nodes) {
//        if (node == null) continue;
//        node.tick();
//      }
//    }
    Main.log("Current water loss: " + Map.waterLost);
  }

  /**
   * Map printing
   *
   * @return the string version of the map
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < gameMap.length; i++) {
      for (int j = 0; j < gameMap[i].length; j++) {
        Node node;
        if ((node = gameMap[i][j]) == null) {
          continue;
        }
        sb.append(String.format("[%d, %d]:\n%s", i, j, node));
        sb.append("\n\n");
      }
    }
    return sb.toString();
  }
}
