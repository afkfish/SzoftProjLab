package com.ez_mode;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Cistern;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;
import java.util.*;

public class ProtoTest {
  private static final ArrayList<String> args = new ArrayList<>();
  private final HashMap<String, Runnable> commands;
  private boolean exited = false;

  /**
   * Constructor for the Prototoest, it fills the command list with the tests, and initializes the
   * map for the tests
   */
  public ProtoTest() {
    commands = new HashMap<>();
    commands.put("print map", this::MapPrintTest);
    commands.put("add character", this::CharacterAddTest);
    commands.put("add node pump", this::AddNewPumpTest);
    commands.put("add node pipe", this::AddNewPipeTest);
    commands.put("character place pump", this::PlacePumpTest);
    commands.put("character place pipe", this::PlacePipeTest);
    commands.put("character move", this::MoveCharacterTest);
    commands.put("character break", this::BreakPipeTest);
    commands.put("character set", this::SetPumpTest);
    commands.put("character repair pump", this::RepairPumpTest);
    commands.put("character repair pipe", this::RepairPipeTest);
    commands.put("character slippery", this::MakePipeSlipperyTest);
    commands.put("character sticky", this::MakePipeStickyTest);
    commands.put("exit", this::exit);
    // map.fillMap(2);
    mapInit();
  }

  /**
   * Initializes the map for the tests, with an appropriate map, it's not random because this way
   * the test are deterministic by nature
   */
  private void mapInit() {
    Map.addNode(new Cistern(0, 0), 0, 0);
    Map.addNode(new Cistern(0, 9), 0, 9);
    Map.addNode(new Pump(0, 3), 0, 3);
    Map.addNode(new Pump(0, 6), 0, 6);

    Map.addNode(new Pipe(0, 1), 0, 1);
    Map.addNode(new Pipe(0, 2), 0, 2);
    Map.addNode(new Pipe(0, 4), 0, 4);
    Map.addNode(new Pipe(0, 5), 0, 5);
    Map.addNode(new Pipe(0, 7), 0, 7);
    Map.addNode(new Pipe(0, 8), 0, 8);

    for (int i = 1; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        Map.addNode(new Pipe(i, j), i, j);
      }
    }

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        try {
          Map.getNode(i, j).connect(Map.getNode(i + 1, j));
          Map.getNode(i, j).connect(Map.getNode(i, j + 1));
        } catch (ObjectFullException e) {
          // Main.log();("Couldn't connect nodes.");
        }
      }
    }

    Map.addPlayer(new Nomad("nomad1"), Map.getNode(0, 0));
    Map.addPlayer(new Nomad("nomad2"), Map.getNode(0, 3));
    Map.addPlayer(new Nomad("nomad3"), Map.getNode(0, 4));

    Map.addPlayer(new Plumber("plumber1"), Map.getNode(0, 6));
    ((Plumber) (Objects.requireNonNull(Map.getPlayer("plumber1")))).setPickedUpPipe(new Pipe());
    Map.addPlayer(new Plumber("plumber2"), Map.getNode(0, 7));
    ((Plumber) (Objects.requireNonNull(Map.getPlayer("plumber2")))).setPickedupPump(new Pump());
    Map.addPlayer(new Plumber("plumber3"), Map.getNode(0, 9));
  }

  /**
   * This function processes the command with inputs from the scanner, and runs them.
   *
   * @param scanner the scanner
   */
  public void processCommand(Scanner scanner) {
    while (!exited) {
      args.clear();
      String cmd = scanner.nextLine();
      String[] tmp = cmd.split(" ");
      ArrayList<String> parts = new ArrayList<>(Arrays.asList(tmp));
      for (String str : parts) {
        if (str.endsWith(">") && str.startsWith("<")) {
          args.add(str.substring(1, str.length() - 1));
          // parts.remove(str);
        }
      }
      for (int i = 0; i < parts.size(); i++) {
        if (args.contains(parts.get(i).substring(1, parts.get(i).length() - 1))) {
          parts.remove(parts.get(i));
          i--;
        }
      }
      cmd = String.join(" ", parts);
      if (commands.containsKey(cmd)) commands.get(cmd).run();
      else Main.log(cmd + " is an invalid command");
    }
  }

  /** This tests printing the map. */
  public void MapPrintTest() {
    Main.log("Printing map: ");
    Map.printPlayers();
    Map.printNodes();
  }

  /** This function tests if we can add a player to the map at given coordinates. */
  public void CharacterAddTest() {
    String characterName = args.get(0);
    int X = Integer.parseInt(args.get(1));
    int Y = Integer.parseInt(args.get(2));
    Character character;
    if (characterName.contains("p") || characterName.contains("P"))
      character = new Plumber(characterName);
    else character = new Nomad(characterName);

    try {
      Map.addPlayer(character, Map.getNode(X, Y));
    } catch (NullPointerException e) {
      Main.log("Tried to place character on an unavailable node!");
    }
  }

  /** This function tests if we can add a new pump to the map at given coordinates. */
  public void AddNewPumpTest() {
    int X = Integer.parseInt(args.get(0));
    int Y = Integer.parseInt(args.get(1));
    Pump pump = new Pump(X, Y);
    Map.addNode(pump, pump.getX(), pump.getX());
    Main.log("Successfully added pump!");
  }

  /** This function tests if we can add a new pipe to the map at given coordinates. */
  public void AddNewPipeTest() {
    int X = Integer.parseInt(args.get(0));
    int Y = Integer.parseInt(args.get(1));
    Pipe pipe = new Pipe(X, Y);
    Map.addNode(pipe, pipe.getX(), pipe.getX());
    Main.log("Successfully added pipe!");
  }

  /** This tests if the given character can place a pump where it stands. */
  public void PlacePumpTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).PlacePump();
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a Pipe");
      Main.log("PlacePumpTest failed!");
    }
  }

  /** This tests if the given character can place a pipe where it stands. */
  public void PlacePipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    Plumber p = (Plumber) c;
    if (p.getDraggedpipe() != null || p.getPickedUpPipe() != null) {
      p.PlacePipe();
      if (p.getDraggedpipe() != null || p.getPickedUpPipe() != null) {
        Main.log("Plumber still has a pipe to place");
        Main.log("PlacePipeTest failed!");
      } else Main.log("PlacePipeTest ran successfully");
    } else {
      Main.log("Plumber has no pipe to place");
    }
  }

  /** This tests if a player character can move a given amount. */
  public void MoveCharacterTest() {
    Character c = Map.getPlayer(args.get(0));
    int Up = Integer.parseInt(args.get(1));
    int Right = Integer.parseInt(args.get(2));
    if (Math.abs(Up) > 1 || Math.abs(Right) > 1) {
      Main.log("Too much movement.");
      return;
    }
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      Node move = Map.getNode(c.getStandingOn().getX() + Right, c.getStandingOn().getY() + Up);
      if (move == null) {
        Main.log("Cannot move there.");
        return;
      }
      c.moveTo(move);
      return;
    } catch (InvalidPlayerMovementException | ObjectFullException e) {
      Main.log("Player can't move because it is stuck or the given Node is full");
    }
    Main.log("MoveCharacterTest failed");
  }

  /** This function tests if given character can break the pipe it stands on. */
  public void BreakPipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      c.breakNode();
      if (c.getStandingOn().isBroken()) {
        Main.log("BreakPipeTest ran successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("The player is not standing on a Pump.");
      return;
    }
    Main.log("BreakPipeTest failed!");
  }

  /**
   * This tests if given character can set the active input and active output to the pump it stands
   * on.
   */
  public void SetPumpTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    Pump node = (Pump) c.getStandingOn();
    try {
      ArrayList<Node> neighbours = node.getNeighbours();
      ArrayList<Pipe> pipes = new ArrayList<>();
      for (Node n : neighbours) {
        if (n.getUuid().contains("Pipe")) pipes.add((Pipe) n);
      }
      if (pipes.size() < 2) Main.log("There are not enough pipe neighbours!");
      else c.setPump(pipes.get(0), pipes.get(1));

      if (node.getActiveInput().equals(pipes.get(0))
          && node.getActiveOutput().equals(pipes.get(1))) {
        Main.log("SetPumpTest ran successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a Pump");
      return;
    }
    Main.log("SetPumpTest failed!");
  }

  /** This function tests if the given character can repair the pump it stands on. */
  public void RepairPumpTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).repair();
      if (!c.getStandingOn().isBroken()) {
        Main.log("RepairTest ran successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a Pump");
      return;
    }
    Main.log("RepairPipeTest failed!");
  }

  /** This function tests if the given character can repair the pipe it stands on. */
  public void RepairPipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).repair();
      if (!c.getStandingOn().isBroken()) {
        Main.log("RepairPipeTest successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a pipe");
      return;
    }
    Main.log("RepairPipeTest failed!");
  }

  /** This function tests if given character can make the pipe it stands on slippery. */
  public void MakePipeSlipperyTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Nomad) c).setSlippery();
      if (((Pipe) (c.getStandingOn())).isSlippery()) {
        Main.log("MakePipeSlipperyTest ran successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a pipe");
      return;
    }
    Main.log("MakePipeSlipperyTest failed!");
  }

  /** This function tests if given character can make the pipe it stands on sticky. */
  public void MakePipeStickyTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      (c).makePipeSticky();
      if (((Pipe) (c.getStandingOn())).isSticky()) {
        Main.log("MakePipeStickyTest ran successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("\n\tThe player is not standing on a pipe");
      return;
    }
    Main.log("MakePipeStickyTest failed!");
  }

  /** Exists the testprogram. */
  public void exit() {
    exited = true;
    Map.clearMap();
  }
}
