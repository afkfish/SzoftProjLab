package com.ez_mode.objects;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

/**
 * A pipe is a node that can be broken and repaired.
 * It can also be connected to other pipes.
 * A pipe can only be connected to 2 other nodes
 * and the maximum player capacity is 1.
 */
public class Pipe extends Node {
	public Pipe() {
		super(1);
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

	@Override
	public void tick() {
		assert this.neighbours.size() <= 2 : "Pipe has more than 2 neighbours";
		this.isConnected = this.neighbours.size() == 2;

		this.logger.debug(String.format("Pipe is connected: %b", this.isConnected));

		if (this.isBroken | !this.isConnected) {
			Map.waterLost += this.flowRate;
			this.absorbers.forEach(node -> node.removeFlowRate(this, this.flowRate));
		} else {
			this.absorbers.forEach(node -> node.addFlowRate(this, this.flowRate));
		}

		this.logger.debug(String.format("Flow rate is at %f", this.flowRate));
	}
}
