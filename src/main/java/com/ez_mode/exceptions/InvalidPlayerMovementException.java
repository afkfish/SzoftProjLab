package com.ez_mode.exceptions;

/** Exception thrown when a player attempts to move to an invalid position. */
public class InvalidPlayerMovementException extends Exception {
    public InvalidPlayerMovementException(String message) {
        super(message);
    }
}
