package com.ez_mode.characters;

import com.ez_mode.Main;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.NotFoundExeption;
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
  private Pipe pickedUpPipe;

  /** Repairs the node the player is standing on. */
  public void repair() {
    try {
      this.standingOn.repairNode(this);
      Main.log("\t" + this.getUuid() + " has repaired " + standingOn.getUuid());
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }

  public void PlacePump() {
    // Stakeholder
    if (this.pickedupPump != null) {
      try {
        Pipe temp = ((Pipe) standingOn);
        Pipe newPipe = new Pipe();
        // Map.addNode(new Pipe(), temp.getX()+5, temp.getY() +5);
        try {
          temp.connect(pickedupPump);
          newPipe.connect(pickedupPump);
          Main.log("\t" + pickedupPump.getUuid() + " has been placed ");
        } catch (ObjectFullException e) {
          System.err.println("Object is full!");
          return;
        }
      } catch (ClassCastException e) {
        Main.log(this.getUuid() + " is not standing on a Pipe");
      }
    } else {
      Main.log(this.getUuid() + " doesn't have a pump to place");
    }
  }

  public Pipe getDraggedpipe() {
    return draggedpipe;
  }

  public Pipe getPickedUpPipe() {
    return pickedUpPipe;
  }

  public void setPickedUpPipe(Pipe p) {
    pickedUpPipe = p;
  }

  public void setPickedupPump(Pump p) {
    pickedupPump = p;
  }

  public void PlacePipe() {
    try {
      if (draggedpipe != null) {
        standingOn.connect(draggedpipe);
        draggedpipe = null;
        return;
      } else if (pickedUpPipe != null) {
        standingOn.connect(pickedUpPipe);
        // Map.addNode(pickedUpPipe, standingOn.getX()+5, standingOn.getY()+5);
        pickedUpPipe = null;
        return;
      }
    } catch (ObjectFullException e) {
      Main.log(this.getUuid() + "tried to place and connect a pipe to a full node");
    }
  }

  public void PickupPump() {
    try {
      Pump temp = ((Cistern) standingOn).GivePump();
      if ((temp != null)) {
        pickedupPump = temp;
        Main.log("\t" + temp.getUuid() + " has been picked up by " + this.getUuid());
      }
    } catch (ClassCastException e) {
      Main.log(this.getUuid() + " is not standing on a Cistern");
    }
  }

  public void PickupPipe(Pipe pipe) {
    try {
      if (pipe == null) {
        for (Node nodi : standingOn.getNeighbours()) {
          if (nodi != null && nodi.getNeighbours().size() > 2) {
            draggedpipe = (Pipe) nodi;
            draggedpipe.disconnect(this.standingOn);
            break;
          }
        }
      } else if (this.standingOn.getNeighbours().contains(pipe)) {
        draggedpipe = pipe;
      }

      if (draggedpipe != null && !draggedpipe.getNeighbours().isEmpty()) {
        Main.log("\t" + draggedpipe + " has been picked up by " + this.getUuid());
      } else if (draggedpipe != null && draggedpipe.getNeighbours().isEmpty()) {
        pickedUpPipe = draggedpipe;
        draggedpipe = null;
        // Map.removeNode(pickedUpPipe);
        Main.log("\t" + pickedUpPipe + " has been picked up, and stored by " + this.getUuid());
      } else if (draggedpipe == null) {
        throw new NotFoundExeption("Pipe Not Found!");
      }
    } catch (ClassCastException cce) {
      Main.log(this.getUuid() + " is not standing on a Cistern");
    } catch (NotFoundExeption nfe) {
      Main.log(nfe.getMessage());
    }
  }
}
