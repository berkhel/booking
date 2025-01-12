package it.berkhel.booking.app.drivenport;

import it.berkhel.booking.entity.Attendee;

public interface ForSendingMessage {

    public void sendMessage(Attendee recipient, String message);
    
}
