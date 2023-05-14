package com.ez_mode.objects;

import com.ez_mode.Main;
import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.ObjectFullException;
import java.util.ArrayList;
import java.util.Random;

public class Cistern extends Node {
  private final ArrayList<Pump> producedPumps = new ArrayList<>();

  public Cistern(int x, int y) {
    super(Integer.MAX_VALUE, 4, x, y);
  }

  @Override
  public void repairNode(Character character) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to repair a cistern.", character.getName()));
  }

  @Override
  public void breakNode(Character character) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to break a cistern.", character.getName()));
  }

  @Override
  public void setSurface(String type, Character c) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to make a cistern sticky/slippery.", c.getName()));
  }

  @Override
  public void tick() {
    super.tick();
    Random random = new Random();
    if (random.nextInt(100) < 40) {
      MakePump();
      MakePipe();
    }
    if (sources.size() < maxConnections) sources.add(MakePipe());

    // this.sources.forEach(node -> Map.waterLost += node.flowRate);
    for (Node node : this.sources) {
      Map.waterArrived += node.flowRate;
    }
  }

  public Pump GivePump() {
    if (producedPumps.size() >= 1) {
      return producedPumps.remove(producedPumps.size() - 1);
    }
    return null;
  }

  public Pipe MakePipe() {
    // Stakeholder
    Main.log("\t" + this.uuid + " made a pipe");
    Pipe temp = new Pipe();
    try {
      temp.connect(this);
    } catch (ObjectFullException e) {
      throw new RuntimeException(e);
    }
    return temp;
  }

  public void MakePump() {
    // Stakeholder
    Main.log("\t" + this.uuid + " made a pump");
    Pump temp = new Pump();
    producedPumps.add(temp);
  }
}
