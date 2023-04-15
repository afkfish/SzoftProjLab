package com.ez_mode.objects;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.ObjectFullException;

import java.util.ArrayList;

public class Cistern extends Node {
  ArrayList<Pump> producedPumps=new ArrayList<>();
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
    Pipe temp=new Pipe();
    try {
      temp.connect(this);
    } catch (ObjectFullException e) {
      throw new RuntimeException(e);
    }
    return temp;
  }
  public void MakePump() {
    // Stakeholder
    Pump temp=new Pump();
    producedPumps.add(temp);
  }
}
