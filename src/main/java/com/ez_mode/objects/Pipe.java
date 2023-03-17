package com.ez_mode.objects;

import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * A pipe is a node that can be broken and repaired.
 * It can also be connected to other pipes.
 * A pipe can only be connected to 2 other nodes
 * and the maximum player capacity is 1.
 */
public class Pipe extends Node {
	private double capacity;
	public Pipe() {
		super(1, 2);
		this.connectors.add(new Connector(this));
	}
	public double getCapacity(){
		return capacity;
	}
	@Override
	public void repairNode(Character character) throws InvalidPlayerActionException {
		if (this.isBroken) {
			this.isBroken = false;
		} else {
			throw new InvalidPlayerActionException(String.format("Player <%s> tried to repair a pipe that was not broken.", character.getName()));
		}
	}

	@Override
	public void breakNode(Character character) throws InvalidPlayerActionException {
		if (!this.isBroken) {
			this.isBroken = true;
		} else {
			throw new InvalidPlayerActionException(String.format("Player <%s> tried to break a pipe that was already broken.", character.getName()));
		}
	}
}
