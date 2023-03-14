package com.ez_mode.exceptions;

/**
 * Exception thrown when a player is not found
 * on neighbouring nodes when trying to move.
 */
public class NotFoundExeption extends Exception {
	public NotFoundExeption(String message) {
		super(message);
	}
}
