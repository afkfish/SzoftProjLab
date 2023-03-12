package com.ez_mode.objects;

import com.ez_mode.characters.Character;
import com.ez_mode.exceptions.InvalidPlayerActionException;

public class WaterSpring extends Node {
	public WaterSpring() {
		super(Integer.MAX_VALUE);
		this.flowRate = 1;
	}

	@Override
	public void addCharacter(Character character) {
		this.charactersOn.add(character);
	}

	@Override
	public void repairNode(Character character) throws InvalidPlayerActionException {
		throw new InvalidPlayerActionException(String.format("Player <%s> tried to repair a cistern.", character.getName()));
	}

	@Override
	public void breakNode(Character character) throws InvalidPlayerActionException {
		throw new InvalidPlayerActionException(String.format("Player <%s> tried to break a cistern.", character.getName()));
	}

	@Override
	public void tick() {
		this.neighbours.forEach(node -> node.addFlowRate(this, 1));

		this.logger.debug(String.format("Flow rate is at %f", this.flowRate));
	}
}
