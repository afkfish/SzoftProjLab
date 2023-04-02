package com.ez_mode.characters;

import com.ez_mode.Map;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import com.ez_mode.objects.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is responsible for the characters in the game. This is tha abstract class for all
 * characters. It contains a name and a reference to the StandableObject it is standing on.
 */
public abstract class Character {
  /*
   * The logger for this class.
   */
  protected final Logger logger;

  /*
   * The uuid of the character.
   */
  private final String uuid = this.getClass().getSimpleName() + (int) (Math.random() * 1000000);

  /** The name of the player. */
  private final String name;

  /** The StandableObject the player is standing on. */
  protected Node standingOn;

  public Character(String name) {
    this.logger = LogManager.getLogger(this.getClass());
    this.name = name;
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
      node.addCharacter(this);
      standingOn.removeCharacter(this);
      this.logger.debug("Moved {} to {} from {}", this.uuid, node.getUuid(), standingOn.getUuid());
      this.standingOn = node;
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
    this.standingOn = node;
    node.placeCharacter(this);
  }

  // TODO: Override in children
  public abstract void SetPump();

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
