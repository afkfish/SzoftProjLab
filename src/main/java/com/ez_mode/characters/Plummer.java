package com.ez_mode.characters;

import com.ez_mode.exceptions.InvalidPlayerActionException;

public class Plummer extends Character {
	public Plummer(String name) {
		super(name);
	}

	public void repair() {
		try {
			this.standingOn.repairNode(this);
		} catch (InvalidPlayerActionException e) {
			this.logger.error(e.getMessage());
		}
	}
}
