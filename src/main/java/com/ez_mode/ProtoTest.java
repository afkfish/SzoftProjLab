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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import static com.ez_mode.Main.map;

public class ProtoTest {
  private final HashMap<String, Runnable> commands;
  private static ArrayList<String> args = new ArrayList<>();
  private boolean exited = false;
  private Scanner input = new Scanner(System.in);
  private final Logger logger = LogManager.getLogger(Map.class);

  public ProtoTest() {
    commands = new HashMap<>();
    commands.put("print map", () -> MapPrintTest());
    commands.put("add character", () -> CharacterAddTest());
    commands.put("add node pump", () -> AddNewPumpTest());
    commands.put("add node pipe", () -> AddNewPipeTest());
    commands.put("character place pump", () -> PlacePumpTest());
    commands.put("character place pipe", () -> PlacePipeTest());
    commands.put("character move", () -> MoveCharacterTest());
    commands.put("character break", () -> BreakPipeTest());
    commands.put("character set", () -> SetPumpTest());
    commands.put("character repair pump", () -> RepairPumpTest());
    commands.put("character repair pipe", () -> RepairPipeTest());
    commands.put("character slippery", () -> MakePipeSlipperyTest());
    commands.put("character sticky", () -> MakePipeStickyTest());
    commands.put("exit", () -> exit());
    // map.fillMap(2);
    mapInit(map);
  }

  private void mapInit(Map map) {
    map = new Map(10);

    map.addNode(new Cistern(0, 0), 0, 0);
    map.addNode(new Cistern(0, 9), 0, 9);
    map.addNode(new Pump(0, 3), 0, 3);
    map.addNode(new Pump(0, 6), 0, 6);

    map.addNode(new Pipe(0, 1), 0, 1);
    map.addNode(new Pipe(0, 2), 0, 2);
    map.addNode(new Pipe(0, 4), 0, 4);
    map.addNode(new Pipe(0, 5), 0, 5);
    map.addNode(new Pipe(0, 7), 0, 7);
    map.addNode(new Pipe(0, 8), 0, 8);

    for (int i = 1; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        map.addNode(new Pipe(i, j), i, j);
      }
    }

    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        try {
          map.getNode(i, j).connect(map.getNode(i + 1, j));
          map.getNode(i, j).connect(map.getNode(i, j + 1));
        } catch (ObjectFullException e) {
          // Main.log();("Couldn't connect nodes.");
        }
      }
    }

    map.addPlayer(new Nomad("nomad1"), map.getNode(0, 0));
    map.addPlayer(new Nomad("nomad2"), map.getNode(0, 3));
    map.addPlayer(new Nomad("nomad3"), map.getNode(0, 4));

    map.addPlayer(new Plumber("plumber1"), map.getNode(0, 6));
    ((Plumber) (map.getPlayer("plumber1"))).setPickedUpPipe(new Pipe());
    map.addPlayer(new Plumber("plumber2"), map.getNode(0, 7));
    ((Plumber) (map.getPlayer("plumber2"))).setPickedupPump(new Pump());
    map.addPlayer(new Plumber("plumber3"), map.getNode(0, 9));
  }

  public void processCommand() {
    while (!exited) {
      args.clear();
      String cmd = input.nextLine();
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
      else Main.log(cmd + "is an invalid command");
    }
  }

  public void MapPrintTest() {
    Main.log("Printing map: ");
    Map.printPlayers();
    Map.printNodes();
  }

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

  public void AddNewPumpTest() {
    int X = Integer.parseInt(args.get(0));
    int Y = Integer.parseInt(args.get(1));
    Pump pump = new Pump(X, Y);
    Map.addNode(pump, pump.getX(), pump.getX());
    Main.log("Successfully added pump!");
  }

  public void AddNewPipeTest() {
    int X = Integer.parseInt(args.get(0));
    int Y = Integer.parseInt(args.get(1));
    Pipe pipe = new Pipe(X, Y);
    Map.addNode(pipe, pipe.getX(), pipe.getX());
    Main.log("Successfully added pipe!");
  }

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
      return;
    }
    Main.log("PlacePumpTest failed!");
  }

  public void PlacePipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    Plumber p = (Plumber) c;
    if (p.getDraggedpipe() != null && p.getPickedUpPipe() != null) {
      p.PlacePipe();
      return;
    } else Main.log("Plumber has no pipe to place");
    Main.log("BreakPipeTest failed!");
  }

  public void MoveCharacterTest() {
    Character c = Map.getPlayer(args.get(0));
    int Up = Integer.parseInt(args.get(1));
    int Right = Integer.parseInt(args.get(2));
    if (Up > 1 || Right > 1) {
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

  public void BreakPipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      c.breakNode();
      if (((Pipe) (c.getStandingOn())).isBroken()) {
        Main.log("Pipe has been broken successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("The player is not standing on a Pump.");
      return;
    }
    Main.log("BreakPipeTest failed!");
  }

  public void SetPumpTest() {

    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ArrayList<Node> neighbours = c.getStandingOn().getNeighbours();
      ArrayList<Pipe> pipes = new ArrayList<>();
      for (Node n : neighbours) {
        if (n.getUuid().contains("Pipe")) pipes.add((Pipe) n);
      }
      if (pipes.size() < 2) Main.log("There are not enough pipe neighbours!");
      else c.setPump(pipes.get(0), pipes.get(1));

      if (((Pump) c.getStandingOn()).getActiveInput().equals(pipes.get(0))
          && ((Pump) c.getStandingOn()).getActiveInput().equals(pipes.get(1))) {
        Main.log("Pump has been set right successfully!");
        return;
      }

    } catch (ClassCastException e) {
      Main.log("the player is not standing on a Pump");
      return;
    }
    Main.log("SetPumpTest failed!");
  }

  public void RepairPumpTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).repair();
      if (!((Pump) (c.getStandingOn())).isBroken()) {
        Main.log("Pump has been repaired successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a Pump");
      return;
    }
    Main.log("RepairPipeTest failed!");
  }

  public void RepairPipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).repair();
      if (!((Pipe) (c.getStandingOn())).isBroken()) {
        Main.log("Pipe has been repaired successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a pipe");
      return;
    }
    Main.log("RepairPipeTest failed!");
  }

  public void MakePipeSlipperyTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      ((Nomad) c).setSlippery();
      if (((Pipe) (c.getStandingOn())).isSlippery()) {
        Main.log("Pipe has been made slippery successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("the player is not standing on a pipe");
      return;
    }
    Main.log("MakePipeSlipperyTest failed!");
  }

  public void MakePipeStickyTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      Main.log("Character couldn't be found on the map");
      return;
    }
    try {
      (c).makePipeSticky();
      if (((Pipe) (c.getStandingOn())).isSticky()) {
        Main.log("Pipe has been made sticky successfully!");
        return;
      }
    } catch (ClassCastException e) {
      Main.log("\n\tThe player is not standing on a pipe");
      return;
    }
    Main.log("MakePipeStickyTest failed!");
  }

  public void exit() {
    exited = true;
    Map.clearMap();
  }
}
