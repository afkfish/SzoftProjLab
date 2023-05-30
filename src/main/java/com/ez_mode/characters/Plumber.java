package com.ez_mode.characters;

import com.ez_mode.Main;
import com.ez_mode.Map;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.*;

/**
 * Class representing a Plummer character. The plummer can repair pipes and pumps, place new pipes
 * and pupms and reroute the water to the cisterns.
 */
public class Plumber extends Character {
  private Pump pickedupPump;
  private Pipe draggedpipe;
  private Pipe pickedUpPipe;

  public Plumber(String name) {
    super(name);
  }

  /** Repairs the node the player is standing on. */
  public void repair() {
    try {
      this.standingOn.repairNode(this);
      Main.log("\t" + this.getUuid() + " has repaired " + standingOn.getUuid());
    } catch (InvalidPlayerActionException e) {
      Main.log(e.getMessage());
    }
  }

  /** A plumber can place a pump onto a pipe if it has one in its inventory. */
  public void PlacePump() {
    if (this.pickedupPump != null) {
      try {
        Pipe temp = ((Pipe) standingOn);
        Pipe newPipe = new Pipe();
        this.getEmptyPlace(newPipe);
        try {
          if (temp.getNeighbours().size() == 2) {
            Node a = temp.getNeighbours().get(1);
            temp.disconnect(a);
            newPipe.connect(a);
          }
          temp.connect(pickedupPump);
          newPipe.connect(pickedupPump);
          Main.log("\t" + pickedupPump.getUuid() + " has been placed ");
          if (standingOn.getNeighbours().contains(pickedupPump)) pickedupPump = null;
        } catch (ObjectFullException e) {
          Main.log("Object is full!");
        }
      } catch (ClassCastException e) {
        Main.log(this.getUuid() + " is not standing on a Pipe");
      }
    } else {
      Main.log(this.getUuid() + " doesn't have a pump to place");
    }
  }

  public void getEmptyPlace(Node node) {
    int X = this.standingOn.getX();
    int Y = this.standingOn.getY();
    if ((X + 1) < Map.getMapSize() && Map.getNode(X + 1, Y) == null) pickedupPump.setPos(X + 1, Y);
    else if ((X - 1) >= 0 && Map.getNode(X - 1, Y) == null) pickedupPump.setPos(X - 1, Y);
    else if ((Y + 1) < Map.getMapSize() && Map.getNode(X, Y + 1) == null)
      pickedupPump.setPos(X, Y + 1);
    else if ((Y - 1) >= 0 && Map.getNode(X, Y - 1) == null) pickedupPump.setPos(X, Y - 1);
    X = pickedupPump.getX();
    Y = pickedupPump.getY();
    if ((X + 1) < Map.getMapSize() && Map.getNode(X + 1, Y) == null) node.setPos(X + 1, Y);
    else if ((X - 1) >= 0 && Map.getNode(X - 1, Y) == null) node.setPos(X - 1, Y);
    else if ((Y + 1) < Map.getMapSize() && Map.getNode(X, Y + 1) == null) node.setPos(X, Y + 1);
    else if ((Y - 1) >= 0 && Map.getNode(X, Y - 1) == null) node.setPos(X, Y - 1);
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

  public Pump getPickedupPump() {
    return pickedupPump;
  }

  public void setPickedupPump(Pump p) {
    pickedupPump = p;
  }

  /** A plumber can place a pipe onto an empty field if it has one in its inventory. */
  public void PlacePipe() {
    try {
      if (draggedpipe != null) {
        standingOn.connect(draggedpipe);
        draggedpipe = null;
      } else if (pickedUpPipe != null) {
        standingOn.connect(pickedUpPipe);
        // Map.addNode(pickedUpPipe, standingOn.getX()+5, standingOn.getY()+5);
        pickedUpPipe = null;
      } else Main.log(this.getUuid() + "has no pipe to place");
    } catch (ObjectFullException e) {
      Main.log(this.getUuid() + "tried to place and connect a pipe to a full node");
    }
  }

  /** A plumber can pick up pumps from a cistern if it has one. */
  public void PickupPump() {
    try {
      Pump temp = ((Cistern) standingOn).GivePump();
      if ((temp != null)) {
        pickedupPump = temp;
        Main.log("\t" + temp.getUuid() + " has been picked up by " + this.getUuid());
        return;
      }
      Main.log("There is no pump to pick up");
    } catch (ClassCastException e) {
      Main.log(this.getUuid() + " is not standing on a Cistern");
    }
  }

  /**
   * A plumber can pick up a pipe from its neighbours.
   *
   * @param pipe the pipe to be picked up
   * @throws NotFoundExeption when a pipe is not found then it throws an exceptions
   */
  public void PickupPipe(Pipe pipe) throws NotFoundExeption {
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
    }
  }
}
