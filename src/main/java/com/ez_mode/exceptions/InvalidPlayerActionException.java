package com.ez_mode.exceptions;

/** Exception thrown when a player attempts to perform an invalid action. */
public class InvalidPlayerActionException extends Exception {
    public InvalidPlayerActionException(String message) {
        super(message);
    }
}
