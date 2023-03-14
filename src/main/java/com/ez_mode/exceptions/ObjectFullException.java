package com.ez_mode.exceptions;

/**
 * Exception thrown when an object is at full capacity.
 */
public class ObjectFullException extends Exception {
	public ObjectFullException(String message) {
		super(message);
	}
}
