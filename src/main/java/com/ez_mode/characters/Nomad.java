package com.ez_mode.characters;

import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * Class representing a Nomad character. The nomad can break pipes and adjust pumps in order to
 * reroute the water into the desert.
 */
public class Nomad extends Character {
  public Nomad(String name) {
    super(name);
  }

  @Override
  public void SetPump() {
    System.out.println(this.getUuid() + " has set " + standingOn.getUuid());
  }

  /** Breaks the node the player is standing on. */
  public void breakNode() {
    try {
      this.standingOn.breakNode(this);
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }
}
