package it.berkhel.booking.app.exception;

public class TooManyTicketsException extends Exception {

    public TooManyTicketsException(String message) {
        super(message);
    }
    
}
