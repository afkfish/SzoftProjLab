package com.ez_mode.objects;

import com.ez_mode.Map;
import com.ez_mode.Tickable;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/** The Node class is the base class for all objects that can be placed on the map. */
public abstract class Node implements Tickable {
    /*
     * The logger for this class.
     */
    protected final Logger logger;
    /** The unique identifier for this object. */
    private final String uuid = this.getClass().getSimpleName() + (int) (Math.random() * 1000000);
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
    protected boolean isBroken = false;
    /** The amount of water flowing through this object. */
    protected double flowRate = 0;

    protected Node(int maxCharacters, int maxConnections) {
        this.logger = LogManager.getLogger(this.getClass());
        this.maxCharacters = maxCharacters;
        this.maxConnections = maxConnections;
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
                            "Player <%s> tried to add a character to a full object.",
                            character.getName()));

        for (Node neighbour : neighbours) {
            if (neighbour.characters.contains(character)) {
                this.characters.add(character);
                return;
            }
        }
        // If the character is not on a neighbour, then they cannot be added to this object.
        throw new InvalidPlayerMovementException(
                String.format(
                        "Player <%s> tried to move to a non-neighbouring object.",
                        character.getName()));
    }

    public void removeCharacter(Character character) throws NotFoundExeption {
        if (!this.characters.contains(character))
            throw new NotFoundExeption(
                    String.format(
                            "Player <%s> tried to remove a character from an object they are not"
                                    + " on.",
                            character.getName()));
        characters.remove(character);
    }

    public abstract void repairNode(Character character) throws InvalidPlayerActionException;

    public abstract void breakNode(Character character) throws InvalidPlayerActionException;

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
        if (this.neighbours.size() >= this.maxConnections)
            throw new ObjectFullException("Tried to connect to a full object.");

        this.neighbours.add(node);
    }
}
