package it.berkhel.booking.app.actionport;

import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.entity.Event;

public interface ForEvents {

    public Event createEvent(Event event) throws EventAlreadyExistsException;
    
}
