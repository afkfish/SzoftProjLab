package com.ez_mode.objects;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

public class Cistern extends Node {
  public  Cistern() {
    super(Integer.MAX_VALUE, 4);
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
  public void tick() {
    super.tick();

    if (sources.size() < maxConnections) sources.add(MakePipe());

    // this.sources.forEach(node -> Map.waterLost += node.flowRate);
    for (Node nodi : this.sources) {
      Map.waterArrived += nodi.flowRate;
    }
  }

  public Pipe MakePipe() {
    // Stakeholder
    return (new Pipe());
  }
}
