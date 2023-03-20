package com.ez_mode.objects;

import static java.lang.Double.min;

import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * A pump is a node that can be audjusted and repaired. It is bound to break after a certain amount
 * of time. The pump can hold 5 players at once.
 */
public class Pump extends Node {
  // TODO: Make that this is used when calculating the flow rate
  private double internalBufferLevel = 0;

  public Pump() {
    super(5, 4);
  }

  private Pipe activeInput;
  private Pipe activeOutput;

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

  public void SetActiveInput(Pipe p) {
    activeInput = p;
  }

  public void SetActiveOutput(Pipe p) {
    activeOutput = p;
  }

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
      } else if (this.internalBufferLevel > 0) {
        this.setFlowRate(min(this.internalBufferLevel, activeOutput.getCapacity()));
        activeOutput.flowRate += flowRate;
      }
    } else {
      this.setFlowRate(0);
      internalBufferLevel += activeInput.flowRate;
    }
  }

  @Override
  public void tick() {
    calculateFlowRate();

    // TODO: Implement the pump breaking randomly and other stuff
  }
}
