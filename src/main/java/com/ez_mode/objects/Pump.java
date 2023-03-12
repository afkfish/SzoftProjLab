package com.ez_mode.objects;

import com.ez_mode.Map;
import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

public class Pump extends Node {
	// TODO: Make that this is used when calculating the flow rate
	private double internalBufferLevel = 0;
	public Pump() {
		super(5);
	}

	@Override
	public void repairNode(Character character) throws InvalidPlayerActionException {
		if (this.isBroken) {
			this.isBroken = false;
		} else {
			throw new InvalidPlayerActionException(String.format("Player <%s> tried to repair a pump that was not broken.", character.getName()));
		}
	}

	@Override
	public void breakNode(Character character) throws InvalidPlayerActionException {
		if (!this.isBroken) {
			this.isBroken = true;
		} else {
			throw new InvalidPlayerActionException(String.format("Player <%s> tried to break a pump that was already broken.", character.getName()));
		}
	}

	@Override
	public void tick() {
		if (this.isBroken) {
			if (this.sources.size() > 0) {
				Map.waterLost += this.flowRate;
			}
			this.absorbers.forEach(node -> node.removeFlowRate(this, this.flowRate));
		} else {
			this.absorbers.forEach(node -> node.addFlowRate(this, this.flowRate));
		}

		this.logger.debug(String.format("Flow rate is at %f", this.flowRate));
	}
}
