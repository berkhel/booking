package it.berkhel.booking.app.exception;

public class DuplicateTicketException extends Exception {
    public DuplicateTicketException(String msg){
        super(msg);
    }
    
}
