package com.ez_mode.characters;

import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.objects.*;

/**
 * Class representing a Plummer character. The plummer can repair pipes and pumps, place new pipes
 * and pupms and reroute the water to the cisterns.
 */
public class Plumber extends Character {
  public Plumber(String name) {
    super(name);
  }

  private Pump pickedup;
  private Pipe draggedpipe;

  @Override
  public void SetPump() {
    System.out.println("\t" + this.getUuid() + " has set " + standingOn.getUuid());
  }

  /** Repairs the node the player is standing on. */
  public void repair() {
    try {
      this.standingOn.repairNode(this);
      System.out.println("\t" + this.getUuid() + " has repaired " + standingOn.getUuid());
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }

  public void PlacePump() {
    // Stakeholder
    if (this.pickedup != null) {
      System.out.println("\t" + pickedup.getUuid() + " has been placed ");
    } else {
      System.out.println(this.getUuid() + " doesn't have a pump to place");
    }
  }

  public void PlacePipe() {
    /// Placeholder
    /// TODO
  }

  public void PickupPump() {
    try {
      Pump temp = ((Cistern) standingOn).GivePump();
      if ((temp != null)) {
        pickedup = temp;
        System.out.println("\t" + temp.getUuid() + " has been picked up by " + this.getUuid());
      }
    } catch (IncompatibleClassChangeError e) {
      System.out.println(this.getUuid() + " is not standing on a Cistern");
    }
  }

  public void PickupPipe() {
    try {
      for (Node nodi : standingOn.getNeighbours()) {
        if (nodi.getNeighbours().size() > 2) {
          draggedpipe = (Pipe) nodi;
          break;
        }
      }
      if ((draggedpipe != null)) {
        System.out.println("\t" + draggedpipe + " has been picked up by " + this.getUuid());
      }
    } catch (IncompatibleClassChangeError e) {
      System.out.println(this.getUuid() + " is not standing on a Cistern");
    }
  }
}
