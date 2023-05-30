package com.ez_mode.objects;

import com.ez_mode.Main;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * WaterSpring is a special type of node that can be used to refill a cistern. It generates 1 unit
 * of water per tick. It is not possible to break or repair a water spring. It has no maximum
 * capacity and unlimited water.
 */
public class WaterSpring extends Node {
  public WaterSpring(int x, int y) {
    super(Integer.MAX_VALUE, 4, x, y);
    this.flowRate = 1;
  }

  /**
   * This can accept a character indefinitely
   *
   * @param character the character to be placed
   */
  @Override
  public void addCharacter(Character character) {
    this.characters.add(character);
  }

  /**
   * Repairs the node.
   *
   * @param character the actor
   * @throws InvalidPlayerActionException this action is always invalid
   */
  @Override
  public void repairNode(Character character) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to repair a waterSpring.", character.getName()));
  }

  /**
   * Breaks the node.
   *
   * @param character the actor
   * @throws InvalidPlayerActionException this action is always invalid
   */
  @Override
  public void breakNode(Character character) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to break a waterSpring.", character.getName()));
  }

  /**
   * Sets the surface of the node.
   *
   * @param type the surface type
   * @param c the actor
   * @throws InvalidPlayerActionException this action is always invalid
   */
  @Override
  public void setSurface(String type, Character c) throws InvalidPlayerActionException {
    throw new InvalidPlayerActionException(
        String.format("Player <%s> tried to make a waterSpring sticky/slippery.", c.getName()));
  }

  /** Controls the water flow. */
  @Override
  public void tick() {
    flowRate = 100;
    this.neighbours.stream()
        .filter(node -> !node.isBroken)
        .forEach(node -> node.addFlowRate(this, 1));

    Main.log("Flow rate is at " + this.flowRate);
  }
}
