package com.ez_mode.characters;

import com.ez_mode.Map;
import com.ez_mode.Tickable;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Node;
import com.ez_mode.objects.Pipe;
import com.ez_mode.objects.Pump;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for the characters in the game. This is tha abstract class for all
 * characters. It contains a name and a reference to the StandableObject it is standing on.
 */
public abstract class Character implements Tickable {
  /*
   * The logger for this class.
   */
  protected final Logger logger;

  /** The name of the player. */
  private final String name;

  /*
   * The uuid of the character.
   */
  private final String uuid;

  /** The StandableObject the player is standing on. */
  protected Node standingOn;

  protected int stuckedInPipe;

  public Character(String name) {
    this.logger = LogManager.getLogger(this.getClass());
    this.name = name;
    this.uuid = this.name + (int) (Math.random() * 100);
    this.stuckedInPipe = 0;
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
      if(this.stuckedInPipe > 0)
        throw new InvalidPlayerMovementException(this.uuid + "Player tried to move, but cant because it stucked in a pipe");
      node.addCharacter(this);
      standingOn.removeCharacter(this);
      this.logger.debug("Moved {} to {} from {}", this.uuid, node.getUuid(), standingOn.getUuid());
      this.standingOn = node;
      System.out.println("\t" + this.uuid + " moved to " + node.getUuid());
    } catch (NotFoundExeption e) {
      this.logger.error(e.getMessage());
      Map.playerLostHandler(this);
    }
  }

  /**
   * This method places the character on the given Node. Useful when a character has to respawn, or
   * placen at the game start.
   *
   * @param node The destination Node.
   */
  public void placeTo(Node node) {
    System.out.println("\tPlaced " + this.uuid + " on " + node.getUuid());
    this.standingOn = node;
    node.placeCharacter(this);
  }

  /** Breaks the node the player is standing on. */
  public void breakNode() {
    try {
      this.standingOn.breakNode(this);
      System.out.println("\t" + this.getUuid() + " has broken " + standingOn.getUuid());
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }

  // TODO: Override in children
  public void setPump(Pipe in, Pipe out) {
    try {
      Pump pump = (Pump) this.standingOn;
      assert in != out : "Input and output pipes must be different.";
      // TODO: Do the connecting logic this is just shit.
      pump.setActiveInput(in);
      pump.setActiveOutput(out);
      System.out.println("\t" + this.uuid + " is setting the pump.");
    } catch (ClassCastException e) {
      System.out.println("Player " + this.uuid + " tried to set a pump on a non-pump object.");
    }
  }

  public void makePipeSticky() {
    try {
      standingOn.setSurface("sticky", this);
    } catch (InvalidPlayerActionException e) {
      this.logger.error(e.getMessage());
    }
  }

  public void tick() {
    if (stuckedInPipe > 0) {
      stuckedInPipe--;
    }
  }

  public void stucked() {
    stuckedInPipe = ((int) (Math.random() * 100)) + 1;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\t");
    sb.append(this.uuid);
    sb.append(" (");
    sb.append(this.name);
    sb.append(") ");
    sb.append("\n");
    sb.append("\t");
    sb.append("Standing on: ");
    sb.append(this.standingOn.getUuid());
    return sb.toString();
  }
}
