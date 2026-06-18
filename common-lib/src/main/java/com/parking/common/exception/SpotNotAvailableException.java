package com.parking.common.exception;

/**
 * Thrown when no parking spot is available for the requested vehicle type.
 */
public class SpotNotAvailableException extends RuntimeException {

    public SpotNotAvailableException(String message) {
        super(message);
    }

    public SpotNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
