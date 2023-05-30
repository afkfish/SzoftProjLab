package com.ez_mode.characters;

import com.ez_mode.Main;
import com.ez_mode.Tickable;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;

/**
 * This class is responsible for the characters in the game. This is tha abstract class for all
 * characters. It contains a name and a reference to the StandableObject it is standing on.
 */
public abstract class Character implements Tickable {
  /** The name of the player. */
  private final String name;

  /*
   * The uuid of the character.
   */
  private final String uuid;

  /** The StandableObject the player is standing on. */
  protected Node standingOn;

  /** When a Character is stuck on a sticky pipe. */
  protected int stuckOnPipe;

  public Character(String name) {
    this.name = name;
    this.uuid = this.name + (int) (Math.random() * 100);
    this.stuckOnPipe = 0;
  }

  public String getUuid() {
    return uuid;
  }

  public String getName() {
    return name;
  }

  public Node getStandingOn() {
    return standingOn;
  }

  /**
   * This method moves the character to the given Node.
   *
   * @param node The destination Node.
   */
  public void moveTo(Node node) throws ObjectFullException, InvalidPlayerMovementException {
    try {
      if (node == null) {
        Main.log("Homokra akartunk lepni");
        return;
      }
      if (this.stuckOnPipe > 0)
        throw new InvalidPlayerMovementException(
            this.uuid + "Player tried to move, but cant because it stucked in a pipe");
      node.addCharacter(this);
      standingOn.removeCharacter(this);
      Main.log("Moved " + this.uuid + " to " + node.getUuid() + " from " + standingOn.getUuid());
      this.standingOn = node;
      Main.log("\t" + this.uuid + " moved to " + node.getUuid());
    } catch (NotFoundExeption e) {
      Main.log(e.getMessage());    }
  }

  /**
   * This method places the character on the given Node. Useful when a character has to respawn, or
   * be placed at the game start.
   *
   * @param node The destination Node.
   */
  public void placeTo(Node node) {
    Main.log("\tPlaced " + this.uuid + " on " + node.getUuid());
    this.standingOn = node;
    node.placeCharacter(this);
  }

  /** Breaks the node the player is standing on. */
  public void breakNode() {
    try {
      this.standingOn.breakNode(this);
      Main.log("\t" + this.getUuid() + " has broken " + standingOn.getUuid());
    } catch (InvalidPlayerActionException | ClassCastException e) {
      Main.log(e.getMessage());
    }
  }

  /**
   * A character can set the input and output of a pump.
   *
   * @param in the input pipe
   * @param out the output pipe
   */
  public void setPump(Pipe in, Pipe out) {
    try {
      Pump pump = (Pump) this.standingOn;
      assert in != out : "Input and output pipes must be different.";
      if (pump.getNeighbours().contains(in)) pump.setActiveInput(in);
      else {
        Main.log("\t" + in.getUuid() + " in Pipe not connected to the pump.");
        return;
      }
      if (pump.getNeighbours().contains(out)) pump.setActiveOutput(out);
      else {
        Main.log("\t" + in.getUuid() + " out Pipe not connected to the pump.");
        return;
      }
      Main.log("\t" + this.getUuid() + " is setting the pump.");
    } catch (ClassCastException e) {
      Main.log("Player " + this.getUuid() + " tried to set a pump on a non-pump object.");
    }
  }

  /** Sets a Pipe sticky. */
  public void makePipeSticky() {
    try {
      standingOn.setSurface("sticky", this);
    } catch (InvalidPlayerActionException e) {
      Main.log(e.getMessage());
    }
  }

  /** Reduces the sticky timer. */
  public void tick() {
    if (stuckOnPipe > 0) {
      stuckOnPipe--;
    }
  }

  /** When a pipe is sticky the Character is not able to move so the stuck flag is true. */
  public void stuck() {
    stuckOnPipe = ((int) (Math.random() * 100)) + 1;
  }

  @Override
  public String toString() {
    String sb =
        "\t"
            + this.uuid
            + " ("
            + this.name
            + ") "
            + "\n"
            + "\t"
            + "Standing on: "
            + this.standingOn.getUuid();
    return sb;
  }
}
