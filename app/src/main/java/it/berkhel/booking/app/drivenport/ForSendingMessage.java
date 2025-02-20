package it.berkhel.booking.app.drivenport;

import it.berkhel.booking.app.entity.Attendee;

public interface ForSendingMessage {

    public void sendMessage(Attendee recipient, String message);
    
}
