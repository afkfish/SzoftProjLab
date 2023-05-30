package com.ez_mode.objects;

import com.ez_mode.Main;
import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import java.util.Random;

/**
 * A pipe is a node that can be broken and repaired. It can also be connected to other pipes. A pipe
 * can only be connected to 2 other nodes and the maximum player capacity is 1.
 */
public class Pipe extends Node {
  private double capacity;
  private boolean isStikcy;
  private boolean isSlippery;
  private int unbreakableTill;

  public Pipe(int x, int y) {
    super(1, 4, x, y);
    isSlippery = false;
    isStikcy = false;
    unbreakableTill = 0;
  }

  public Pipe() {
    super(1, 4, -1, -1);
    isSlippery = false;
    isStikcy = false;
    unbreakableTill = 0;
  }

  public boolean isSlippery() {
    return isSlippery;
  }

  public boolean isSticky() {
    return isStikcy;
  }

  public double getCapacity() {
    return capacity;
  }

  @Override
  public void repairNode(Character character) throws InvalidPlayerActionException {
    if (this.isBroken) {
      this.isBroken = false;
      Random random = new Random();
      unbreakableTill = ((random.nextInt(10)) + 1);
    } else {
      throw new InvalidPlayerActionException(
          String.format(
              "Player <%s> tried to repair a pipe that was not broken.", character.getName()));
    }
  }

  @Override
  public void addCharacter(Character character)
      throws ObjectFullException, InvalidPlayerMovementException {
    if (this.characters.size() >= maxCharacters)
      throw new ObjectFullException(
          String.format(
              "Player <%s> tried to add a character to a full object.", character.getName()));

    for (Node neighbour : neighbours) {
      if (neighbour.characters.contains(character)) {
        this.characters.add(character);
        if (isSlippery) {
          Random random = new Random();
          if (this.neighbours.size() == 1) this.neighbours.get(0).addCharacter(character);
          else {
            int RNG = random.nextInt(100);
            if (RNG < 50) this.neighbours.get(1).addCharacter(character);
            else this.neighbours.get(0).addCharacter(character);
          }
          try {
            this.removeCharacter(character);
          } catch (NotFoundExeption e) {
            throw new RuntimeException(e);
          }
        }
        if (isStikcy) character.stuck();
        Main.log("\t" + character.getUuid() + " added to " + this.uuid);
        return;
      }
    }
    // If the character is not on a neighbour, then they cannot be added to this object.
    throw new InvalidPlayerMovementException(
        String.format(
            "Player <%s> tried to move to a non-neighbouring object.", character.getName()));
  }

  @Override
  public void breakNode(Character character) throws InvalidPlayerActionException {
    if (!this.isBroken && unbreakableTill == 0) {
      this.isBroken = true;
    } else {
      throw new InvalidPlayerActionException(
          String.format(
              "Player <%s> tried to break a pipe that was already broken.", character.getName()));
    }
  }

  @Override
  public void tick() {
    assert this.neighbours.size() <= this.maxConnections
        : this.getClass().getName() + " has more than max neighbours";

    this.calculateFlowRate();
    if (unbreakableTill > 0) this.unbreakableTill--;
    Main.log("Flow rate is at " + this.flowRate);
  }

  @Override
  public void setSurface(String type, Character c) {
    if (type.equals("sticky")) isStikcy = true;
    else if (type.equals("slippery")) isSlippery = true;
    else {
      isStikcy = false;
      isSlippery = false;
    }
  }

  @Override
  protected void calculateFlowRate() {
    // If the pipe is broken or any of its connectors are not connected, then it loses water
    if (this.isBroken) {
      Main.log("Pipe is broken, losing water.");

      // add the water loss to the nomad points
      Map.waterLost += this.flowRate;
      this.getNeighbours().forEach(node -> node.removeFlowRate(this, this.flowRate));

    } else if (this.getFlowRate() != 0) {
      this.getNeighbours().forEach(node -> node.addFlowRate(this, this.flowRate));
    }
  }
}
