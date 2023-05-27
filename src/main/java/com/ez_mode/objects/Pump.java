package com.ez_mode.objects;

import static java.lang.Double.min;

import com.ez_mode.Main;
import com.ez_mode.characters.Character;
import com.ez_mode.characters.Nomad;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.NotFoundExeption;

import java.util.Random;

/**
 * A pump is a node that can be adjusted and repaired. It is bound to break after a certain amount
 * of time. The pump can hold 5 players at once.
 */
public class Pump extends Node {
  // TODO: Make that this is used when calculating the flow rate
  private double internalBufferLevel = 0;

  public Pump(int x, int y) {
    super(5, 4, x, y);
  }

  public Pump() {
    super(5, 4, -1, -1);
  }

  private Pipe activeInput;
  private Pipe activeOutput;

  public Pipe getActiveOutput() {
    return activeOutput;
  }

  public Pipe getActiveInput() {
    return activeInput;
  }

  /**
   * Repairs the node if it is broken and the actor is a plumber.
   *
   * @param character the actor
   * @throws InvalidPlayerActionException if the node is not broken this is thrown
   */
  @Override
  public void repairNode(Character character) throws InvalidPlayerActionException {
    if (this.isBroken) {
      this.isBroken = false;
    } else {
      throw new InvalidPlayerActionException(
          String.format(
              "Player <%s> tried to repair a pump that was not broken.", character.getName()));
    }
  }

  /**
   * Breaks the node
   *
   * @param character the actor
   * @throws InvalidPlayerActionException if the node is broken already this is thrown
   */
  @Override
  public void breakNode(Character character) throws InvalidPlayerActionException {
    if (!this.isBroken) {
      this.isBroken = true;
    } else {
      throw new InvalidPlayerActionException(
          String.format(
              "Player <%s> tried to break a pump that was already broken.", character.getName()));
    }
  }

  /**
   * Sets a surface type to the node.
   *
   * @param type Surface type
   * @param c the actor
   * @throws InvalidPlayerActionException this action is always invalid since a pump can't be set to
   *     a given surface type
   */
  @Override
  public void setSurface(String type, Character c) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to make a pump sticky/slippery.", c.getName()));
  }

  public void setActiveInput(Pipe p) {
    activeInput = p;
  }

  public void setActiveOutput(Pipe p) {
    activeOutput = p;
  }

  /** Calculates the water flow rate */
  @Override
  public void calculateFlowRate() {
    if (!this.isBroken) {
      if (sources.contains(activeInput)) {
        if (absorbers.contains(activeOutput)) {
          this.setFlowRate(activeInput.flowRate);
          activeOutput.flowRate += this.flowRate;
        } else {
          internalBufferLevel += activeInput.flowRate;
        }
      } else if (this.internalBufferLevel > 0 && sources.contains(activeOutput)) {
        this.setFlowRate(min(this.internalBufferLevel, activeOutput.getCapacity()));
        activeOutput.flowRate += flowRate;
      } else {
        return;
      }
    } else {
      this.setFlowRate(0);
      if (activeInput != null) {
        internalBufferLevel += activeInput.flowRate;
      }
    }
  }

  /** Randomly breaks. */
  @Override
  public void tick() {
    calculateFlowRate();
    Random random = new Random();
    if (random.nextInt(100) < 80) {
      Nomad temp = new Nomad("temp");
      temp.placeTo(this);
      try {
        this.breakNode(temp);
      } catch (InvalidPlayerActionException ignored) {
        Main.log("Pump tried to break itself when it was already broken.");
      }
      try {
        this.removeCharacter(temp);
      } catch (NotFoundExeption e) {
        Main.log(e.getMessage());
      }
    }
  }
}
