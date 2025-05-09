package it.berkhel.booking.app;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;

class EventApp {

    private ForStorage storage;

    EventApp(ForStorage storage){
        this.storage = storage;
    }
    
    public Event createEvent(Event event) throws EventAlreadyExistsException {
        if (storage.getEventById(event.getId()).isPresent()) {
            throw new EventAlreadyExistsException("Event already exists");
        }
        return storage.save(event);
    }
}
