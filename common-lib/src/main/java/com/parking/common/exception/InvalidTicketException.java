package com.parking.common.exception;

/**
 * Thrown when a ticket ID is invalid or not found.
 */
public class InvalidTicketException extends RuntimeException {

    public InvalidTicketException(String message) {
        super(message);
    }

    public InvalidTicketException(String message, Throwable cause) {
        super(message, cause);
    }
}
