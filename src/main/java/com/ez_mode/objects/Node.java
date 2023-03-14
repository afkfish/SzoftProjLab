package com.ez_mode.objects;

import com.ez_mode.Tickable;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;
import com.ez_mode.exceptions.InvalidPlayerMovementException;
import com.ez_mode.exceptions.NotFoundExeption;
import com.ez_mode.exceptions.ObjectFullException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * The Node class is the base class for
 * all objects that can be placed on the map.
 */
public abstract class Node implements Tickable {
	/*
	 * The logger for this class.
	 */
	protected final Logger logger = LogManager.getLogger(Node.class);
	/**
	 * The unique identifier for this object.
	 */
	private final String uuid = this.getClass().getName() + (int) (Math.random() * 1000000);
	/**
	 * The characters currently on this object.
	 */
	protected final ArrayList<Character> charactersOn = new ArrayList<>();
	/**
	 * The objects that are neighbours to this object.
	 */
	protected final ArrayList<Node> neighbours = new ArrayList<>();
	/**
	 * The objects that are sources of water to this object.
	 */
	protected final ArrayList<Node> sources = new ArrayList<>();
	/**
	 * The objects that are absorbers of water from this object.
	 */
	protected final ArrayList<Node> absorbers = new ArrayList<>();
	/**
	 * The maximum number of characters that can be on this object.
	 */
	protected final int maxCharacters;
	protected boolean isBroken = false;
	protected boolean isConnected = false;
	/**
	 * The amount of water flowing through this object.
	 */
	protected double flowRate = 0;

	protected Node(int maxCharacters) {
		this.maxCharacters = maxCharacters;
	}

	public String getUuid() { return uuid; }

	public void addNeighbour(Node neighbour) {
		neighbours.add(neighbour);
	}

	public void addAbsorber(Node absorber) {
		absorbers.add(absorber);
	}

	public void addSource(Node source) {
		sources.add(source);
	}

	public void removeNeighbour(Node neighbour) {
		neighbours.remove(neighbour);
	}

	public ArrayList<Character> getCharactersOn() {
		return charactersOn;
	}

	public void addCharacter(Character character) throws ObjectFullException, InvalidPlayerMovementException {
		if (this.charactersOn.size() >= maxCharacters)
			throw new ObjectFullException(String.format("Player <%s> tried to add a character to a full object.", character.getName()));

		for (Node neighbour : neighbours) {
			if (neighbour.getCharactersOn().contains(character)) {
				this.charactersOn.add(character);
			} else {
				throw new InvalidPlayerMovementException(String.format("Player <%s> tried to move to a non-neighbouring object.", character.getName()));
			}
		}
	}

	public void removeCharacter(Character character) throws NotFoundExeption {
		charactersOn.remove(character);
	}

	public void repairNode(Character character) throws InvalidPlayerActionException {
		isBroken = false;
	}

	public void breakNode(Character character) throws InvalidPlayerActionException {
		isBroken = true;
	}

	public void addFlowRate(Node source, double flowRate) {
		if (!this.sources.contains(source)) {
			this.flowRate += flowRate;
			this.sources.add(source);
			this.absorbers.forEach(standableObject -> standableObject.addFlowRate(this, flowRate));
		}
	}

	public void removeFlowRate(Node source, double flowRate) {
		if (this.sources.contains(source)) {
			this.flowRate -= flowRate;
			this.sources.remove(source);
			this.absorbers.forEach(standableObject -> standableObject.removeFlowRate(this, flowRate));
		}
	}
}
