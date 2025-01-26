package it.berkhel.booking.app.exception;

public class ConcurrentPurchaseException extends Exception {

    public ConcurrentPurchaseException(String message){
        super(message);
    }

    
}
