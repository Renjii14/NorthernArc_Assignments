package org.northernarc.flight.exception;

public class FlightNotFound extends RuntimeException {
    public FlightNotFound(String message) {
        super(message);
    }
}
