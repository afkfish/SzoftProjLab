package com.ez_mode.characters;

import com.ez_mode.exceptions.InvalidPlayerActionException;

public class Nomad extends Character {
	public Nomad(String name) {
		super(name);
	}

	public void breakNode() {
		try {
			this.standingOn.breakNode(this);
		} catch (InvalidPlayerActionException e) {
			this.logger.error(e.getMessage());
		}
	}

}
