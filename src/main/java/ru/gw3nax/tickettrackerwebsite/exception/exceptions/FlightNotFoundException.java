package ru.gw3nax.tickettrackerwebsite.exception.exceptions;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String message) {
        super(message);
    }
}
