package it.berkhel.booking.app.actionport;

import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;

public interface ForEvents {

    public Event createEvent(Event event) throws EventAlreadyExistsException;
    
}
