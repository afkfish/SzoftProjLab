package com.ez_mode.objects;

import com.ez_mode.Map;
import com.ez_mode.Tickable;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** The Node class is the base class for all objects that can be placed on the map. */
public abstract class Node implements Tickable {
  /*
   * The logger for this class.
   */
  protected final Logger logger;

  /** The unique identifier for this object. */
  protected final String uuid;

  /** The characters currently on this object. */
  protected final ArrayList<Character> characters = new ArrayList<>();

  /** The objects that are neighbours to this object. */
  protected final ArrayList<Node> neighbours = new ArrayList<>();

  /** The objects that are sources of water to this object. */
  protected final ArrayList<Node> sources = new ArrayList<>();

  /** The objects that are absorbers of water from this object. */
  protected final ArrayList<Node> absorbers = new ArrayList<>();

  /** The maximum number of characters that can be on this object. */
  protected final int maxCharacters;

  protected final int maxConnections;
  protected final int x;
  protected final int y;
  protected boolean isBroken = false;

  /** The amount of water flowing through this object. */
  protected double flowRate = 0;

  protected Node(int maxCharacters, int maxConnections, int x, int y) {
    this.uuid = this.getClass().getSimpleName() + (int) (Math.random() * 100);
    this.logger = LogManager.getLogger(this.getClass());
    this.maxCharacters = maxCharacters;
    this.maxConnections = maxConnections;
    this.x = x;
    this.y = y;
  }

  public String getUuid() {
    return uuid;
  }

  public ArrayList<Character> getCharacters() {
    return characters;
  }

  public void placeCharacter(Character character) {
    characters.add(character);
    this.logger.debug("Placed {} on {}", character.getUuid(), this.uuid);
  }

  public void addCharacter(Character character)
      throws ObjectFullException, InvalidPlayerMovementException {
    if (this.characters.size() >= maxCharacters)
      throw new ObjectFullException(
          String.format(
              "Player <%s> tried to add a character to a full object.", character.getName()));

    for (Node neighbour : neighbours) {
      if (neighbour.characters.contains(character)) {
        this.characters.add(character);
        System.out.println("\t" + character.getUuid() + " added to " + this.uuid);
        return;
      }
    }
    // If the character is not on a neighbour, then they cannot be added to this object.
    throw new InvalidPlayerMovementException(
        String.format(
            "Player <%s> tried to move to a non-neighbouring object.", character.getName()));
  }

  public void removeCharacter(Character character) throws NotFoundExeption {
    if (!this.characters.contains(character))
      throw new NotFoundExeption(
          String.format(
              "Player <%s> tried to remove a character from an object they are not" + " on.",
              character.getName()));
    characters.remove(character);
    System.out.println("\t" + character.getUuid() + " removed from " + this.uuid);
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public abstract void repairNode(Character character) throws InvalidPlayerActionException;

  public abstract void breakNode(Character character) throws InvalidPlayerActionException;

  public abstract void setSurface(String type, Character c)
      throws InvalidPlayerActionException; // TODO implement is childs

  public void addFlowRate(Node source, double excededFlow) {
    if (!this.sources.contains(source)) {
      this.flowRate += excededFlow;
      this.sources.add(source);
      this.absorbers.forEach(node -> node.addFlowRate(this, excededFlow));
    }
  }

  public void removeFlowRate(Node source, double flowRate) {
    if (this.sources.contains(source)) {
      this.flowRate -= flowRate;
      this.sources.remove(source);
      this.absorbers.forEach(node -> node.removeFlowRate(this, flowRate));
    }
  }

  public void setFlowRate(double flowRate) {
    this.flowRate = flowRate;
  }

  @Override
  public void tick() {
    assert this.neighbours.size() <= this.maxConnections
        : this.getClass().getName() + " has more than max neighbours";

    this.calculateFlowRate();

    this.logger.debug("Flow rate is at {}", this.flowRate);
  }

  public ArrayList<Node> getNeighbours() {
    return neighbours;
  }

  protected void calculateFlowRate() {
    // If the pipe is broken or any of its connectors are not connected, then it loses water
    if (this.isBroken) {
      this.logger.warn("Pipe is broken, losing water.");

      // add the water loss to the nomad points
      Map.waterLost += this.flowRate;
      this.absorbers.forEach(node -> node.removeFlowRate(this, this.flowRate));

    } else {
      this.absorbers.forEach(node -> node.addFlowRate(this, this.flowRate));
    }
  }

  public void connect(Node node) throws ObjectFullException {
    System.out.println("\t" + this.uuid + ":connect param: " + node.uuid);
    if (this.neighbours.size() >= this.maxConnections)
      throw new ObjectFullException("Tried to connect to a full object.");
    this.neighbours.add(node);
    if (!node.getNeighbours().contains(this)) node.connect(this);
  }

  public void disconnect(Node node) {
    System.out.println("\t" + this.uuid + ":connect param: " + node.uuid);
    this.neighbours.remove(node);
    if (node.getNeighbours().contains(this)) node.disconnect(this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(this.uuid);
    sb.append(" (");
    sb.append(this.getClass().getSimpleName());
    sb.append(") \n");
    sb.append("Characters: ");
    sb.append(this.characters.size());
    sb.append("/");
    sb.append(this.maxCharacters);
    sb.append("\n");
    for (Character character : this.characters) {
      sb.append(character.toString());
    }
    sb.append("\nNeighbours: ");
    sb.append(this.neighbours.size());
    sb.append("/");
    sb.append(this.maxConnections);
    sb.append("\nSources: ");
    sb.append(this.sources.size());
    sb.append("\nAbsorbers: ");
    sb.append(this.absorbers.size());
    sb.append("\nFlow Rate: ");
    sb.append(this.flowRate);
    sb.append("\nBroken: ");
    sb.append(this.isBroken);

    return sb.toString();
  }
}
