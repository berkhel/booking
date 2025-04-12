package it.berkhel.booking.app.drivenport;

import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Purchase;

public interface ForSendingMessage {

    public void sendMessage(Attendee recipient, String message);
    
}
