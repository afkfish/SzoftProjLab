package com.ez_mode.characters;

import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.*;

/**
 * Class representing a Plummer character. The plummer can repair pipes and pumps, place new pipes
 * and pupms and reroute the water to the cisterns.
 */
public class Plumber extends Character {
  public Plumber(String name) {
    super(name);
  }

  private Pump pickedupPump;
  private Pipe draggedpipe;

  @Override
  public void setPump(Pipe in, Pipe out) {
    try {
      Pump pump = (Pump) this.standingOn;
      assert in != out : "Input and output pipes must be different.";
      // TODO: Do the connecting logic this is just shit.
      if (pump.getNeighbours().contains(in)) pump.setActiveInput(in);
      else {
        System.out.println("\t" + in.getUuid() + " in Pipe not connected to the pump.");
        return;
      }
      if (pump.getNeighbours().contains(out)) pump.setActiveOutput(out);
      else {
        System.out.println("\t" + in.getUuid() + " out Pipe not connected to the pump.");
        return;
      }
      System.out.println("\t" + this.getUuid() + " is setting the pump.");
    } catch (ClassCastException e) {
      System.out.println("Player " + this.getUuid() + " tried to set a pump on a non-pump object.");
    }
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

  public void PlacePump() { // TODO implemet the placing
    // Stakeholder
    if (this.pickedupPump != null) {
      try {
        Pipe temp = ((Pipe) standingOn);
        Pipe newPipe = new Pipe();
        // Map.addNode(new Pipe(), temp.getX()+5, temp.getY() +5);
        try {
          temp.connect(pickedupPump);
          newPipe.connect(pickedupPump);
        } catch (ObjectFullException e) {
          throw new RuntimeException(e);
        }
      } catch (ClassCastException e) {
        System.out.println(this.getUuid() + " is not standing on a Pipe");
        System.out.println("\t" + pickedupPump.getUuid() + " has been placed ");
      }
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
        pickedupPump = temp;
        System.out.println("\t" + temp.getUuid() + " has been picked up by " + this.getUuid());
      }
    } catch (ClassCastException e) {
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
    } catch (ClassCastException e) {
      System.out.println(this.getUuid() + " is not standing on a Cistern");
    }
  }
}
