package com.ez_mode.characters;

import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * Class representing a Plummer character. The plummer can repair pipes and pumps, place new pipes
 * and pupms and reroute the water to the cisterns.
 */
public class Plumber extends Character {
  public Plumber(String name) {
    super(name);
  }

  @Override
  public void SetPump() {}

  /** Repairs the node the player is standing on. */
  public void repair() {
    try {
      this.standingOn.repairNode(this);
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }

  public void PlacePump() {
    // Stakeholder
  }

  public void MovePipe() {
    // Stakeholder
  }

  public void PickupPump() {}

  public void PickupPipe() {}
}
