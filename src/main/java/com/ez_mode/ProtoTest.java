package com.ez_mode;

import static com.ez_mode.Main.map;

import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.characters.Plumber;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class ProtoTest {
  private final HashMap<String, Runnable> commands;
  private static ArrayList<String> args = new ArrayList<>();
  private boolean exited = false;
  private Scanner input = new Scanner(System.in);

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
    map.fillMap(2);
  }

  public void processCommand() {
    while (!exited) {
      args.clear();
      String cmd = input.nextLine();
      String[] tmp = cmd.split(" ");
      ArrayList<String> parts = new ArrayList<String>(Arrays.asList(tmp));
      for (String str : parts) {
        if (str.endsWith(">") && str.startsWith("<")) {
          args.add(str.substring(1, str.length()-1));
          parts.remove(str);
        }
      }
      cmd = String.join(" ", parts);
      if (commands.containsKey(cmd)) commands.get(cmd).run();
      else System.out.println(cmd + "is an invalid command");
    }
  }

  public void MapPrintTest() {
    System.out.println("Printing map: ");
    Map.printPlayers();
    Map.printNodes();
  }

  public void CharacterAddTest() {
    String characterName = args.get(0);
    int X = Integer.parseInt(args.get(1));
    int Y = Integer.parseInt(args.get(2));
    Character character = new Plumber(characterName);
    Map.addPlayer(character, Map.getNode(X, Y));
  }

  public void AddNewPumpTest() {
    int X = Integer.parseInt(args.get(0));
    int Y = Integer.parseInt(args.get(1));
    Pump pump = new Pump(X, Y);
    Map.addNode(pump, pump.getX(), pump.getX());
  }

  public void AddNewPipeTest() {
    int X = Integer.parseInt(args.get(0));
    int Y = Integer.parseInt(args.get(1));
    Pipe pipe = new Pipe(X, Y);
    Map.addNode(pipe, pipe.getX(), pipe.getX());
  }

  public void PlacePumpTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).PlacePump();
    } catch (ClassCastException e) {
      System.out.println("the player is not standing on a Pipe");
      return;
    }
    System.err.println("BreakPipeTest failed!");
  }

  public void PlacePipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    Plumber p = (Plumber) c;
    if (p.getDraggedpipe() != null && p.getPickedUpPipe() != null) {
      p.PlacePipe();
      return;
    } else System.out.println("Plumber has no pipe to place");
    System.err.println("BreakPipeTest failed!");
  }

  public void MoveCharacterTest() {
    Character c = Map.getPlayer(args.get(0));
    int Up = Integer.parseInt(args.get(1));
    int Right = Integer.parseInt(args.get(2));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      Node move = new Pump(c.getStandingOn().getX() + Up, c.getStandingOn().getY() + Right);
      c.moveTo(move);
      return;
    } catch (InvalidPlayerMovementException | ObjectFullException e) {
      System.out.println("Player can't move because it stucked or the given Node is full");
    }
    System.err.println("MoveCharacterTest failed");
  }

  public void BreakPipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      c.breakNode();
      if (((Pipe) (c.getStandingOn())).isBroken()) {
        System.out.println("Pipe has been broken successfully!");
        return;
      }
    } catch (ClassCastException e) {
      System.out.println("the player is not standing on a Pump");
      return;
    }
    System.err.println("BreakPipeTest failed!");
  }

  public void SetPumpTest() {

    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      c.setPump((Pipe) Map.getNode(args.get(1)), (Pipe) Map.getNode(args.get(2)));
      if (((Pump) (c.getStandingOn())).getActiveInput().getUuid().equals(args.get(1))
          && ((Pump) (c.getStandingOn())).getActiveOutput().getUuid().equals(args.get(2))) {
        System.out.println("Pump has been set right successfully!");
        return;
      }
    } catch (ClassCastException e) {
      System.out.println("the player is not standing on a Pump");
      return;
    }
    System.err.println("SetPumpTest failed!");
  }

  public void RepairPumpTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).repair();
      if (!((Pump) (c.getStandingOn())).isBroken()) {
        System.out.println("Pump has been repaired successfully!");
        return;
      }
    } catch (ClassCastException e) {
      System.out.println("the player is not standing on a Pump");
      return;
    }
    System.err.println("RepairPipeTest failed!");
  }

  public void RepairPipeTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      ((Plumber) c).repair();
      if (!((Pipe) (c.getStandingOn())).isBroken()) {
        System.out.println("Pipe has been repaired successfully!");
        return;
      }
    } catch (ClassCastException e) {
      System.out.println("the player is not standing on a pipe");
      return;
    }
    System.err.println("RepairPipeTest failed!");
  }

  public void MakePipeSlipperyTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      ((Nomad) c).setSlippery();
      if (((Pipe) (c.getStandingOn())).isSlippery()) {
        System.out.println("Pipe has been made slippery successfully!");
        return;
      }
    } catch (ClassCastException e) {
      System.out.println("the player is not standing on a pipe");
      return;
    }
    System.err.println("MakePipeSlipperyTest failed!");
  }

  public void MakePipeStickyTest() {
    Character c = Map.getPlayer(args.get(0));
    if (c == null) {
      System.out.println("Character couldn't be found on the map");
      return;
    }
    try {
      (c).makePipeSticky();
      if (((Pipe) (c.getStandingOn())).isSticky()) {
        System.out.println("Pipe has been made sticky successfully!");
        return;
      }
    } catch (ClassCastException e) {
      System.out.println("\n\tThe player is not standing on a pipe");
      return;
    }
    System.err.println("MakePipeStickyTest failed!");
  }

  public void exit() {
    exited = true;
    Map.clearMap();
  }
}
