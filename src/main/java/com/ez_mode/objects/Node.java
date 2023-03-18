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
import java.util.Optional;

/**
 * The Node class is the base class for
 * all objects that can be placed on the map.
 */
public abstract class Node implements Tickable {
	/*
	 * The logger for this class.
	 */
	protected final Logger logger;
	/**
	 * The unique identifier for this object.
	 */
	private final String uuid = this.getClass().getName() + (int) (Math.random() * 1000000);
	/**
	 * The characters currently on this object.
	 */
	protected final ArrayList<Character> charactersOn = new ArrayList<>();
	protected final ArrayList<Connector> connectors = new ArrayList<>();
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
	protected final int maxConnections;
	protected boolean isBroken = false;
	/**
	 * The amount of water flowing through this object.
	 */
	protected double flowRate = 0;

	protected Node(int maxCharacters, int maxConnections) {
		this.logger = LogManager.getLogger(this.getClass());
		this.maxCharacters = maxCharacters;
		this.maxConnections = maxConnections;
		this.connectors.add(new Connector(this));
	}

	public String getUuid() { return uuid; }

	public void addAbsorber(Node absorber) {
		absorbers.add(absorber);
	}

	public void removeAbsorber(Node absorber) {
		absorbers.remove(absorber);
	}

	public void addSource(Node source) {
		sources.add(source);
	}

	public void removeSource(Node source) {
		sources.remove(source);
	}

	public void addConnector(Connector connector) {
		connectors.add(connector);
	}

	public void removeConnector(Connector connector) {
		connectors.remove(connector);
	}

	public ArrayList<Character> getCharactersOn() {
		return charactersOn;
	}

	public void placeCharacter(Character character) {
		charactersOn.add(character);
		this.logger.debug("Placed {} on {}", character.getUuid(), this.uuid);
	}

	public void addCharacter(Character character) throws ObjectFullException, InvalidPlayerMovementException {
		if (this.charactersOn.size() >= maxCharacters)
			throw new ObjectFullException(String.format("Player <%s> tried to add a character to a full object.", character.getName()));

		for (Connector connector : connectors) {
			if (connector.getReachableCharacters(this).contains(character)) {
				this.charactersOn.add(character);
				return;
			} else {
				throw new InvalidPlayerMovementException(String.format("Player <%s> tried to move to a non-neighbouring object.", character.getName()));
			}
		}
	}

	public void removeCharacter(Character character) throws NotFoundExeption {
		if (!this.charactersOn.contains(character))
			throw new NotFoundExeption(String.format("Player <%s> tried to remove a character from an object they are not on.", character.getName()));
		charactersOn.remove(character);
	}

	public void repairNode(Character character) throws InvalidPlayerActionException {
		isBroken = false;
	}

	public void breakNode(Character character) throws InvalidPlayerActionException {
		isBroken = true;
	}

	public void addFlowRate(Node source, double excededFlow) {
		if (!this.sources.contains(source)) {
			this.flowRate += excededFlow;
			this.sources.add(source);
			this.absorbers.forEach(standableObject -> standableObject.addFlowRate(this, excededFlow));
		}
	}

	public void removeFlowRate(Node source, double flowRate) {
		if (this.sources.contains(source)) {
			this.flowRate -= flowRate;
			this.sources.remove(source);
			this.absorbers.forEach(standableObject -> standableObject.removeFlowRate(this, flowRate));
		}
	}

	public void setFlowRate(double flowRate) {
		this.flowRate = flowRate;
	}

	@Override
	public void tick() {
		assert this.connectors.size() <= this.maxConnections : this.getClass().getName() + " has more than max neighbours";

		this.calculateFlowRate();

		this.logger.debug("Flow rate is at {}", this.flowRate);
	}

	protected void calculateFlowRate() {
		this.logger.debug(this.connectors.stream().allMatch(Connector::isConnected));

		// If the pipe is broken or any of its connectors are not connected, then it loses water
		if (this.isBroken || !this.connectors.stream().allMatch(Connector::isConnected)) {

			this.logger.warn("Pipe is broken or has a loose connection, losing water.");

			// get how many connectors are not connected
			double looseConnectors = (double) this.connectors.stream().filter(connector -> !connector.isConnected()).count();

			// calculate the water loss per connection
			double waterLossPerConnection = this.flowRate * (looseConnectors / (this.connectors.size() - this.sources.size()));

			// add the water loss to the nomad points
			Map.waterLost += waterLossPerConnection;
			this.absorbers.forEach(node -> node.removeFlowRate(this, waterLossPerConnection));

		} else
		{
			this.absorbers.forEach(node -> node.addFlowRate(this, this.flowRate));
		}
	}

	public void connect(Node node) throws ObjectFullException {
		Optional<Connector> freeConnector = this.connectors.stream().filter(connector -> !connector.isConnected()).findFirst();
		if (freeConnector.isPresent()) {
			freeConnector.get().connect(node);
		} else if (this.maxConnections < this.connectors.size()) {
			this.connectors.add(new Connector(this, node));
		} else {
			throw new ObjectFullException("Tried to connect to a full object.");
		}
	}
}
