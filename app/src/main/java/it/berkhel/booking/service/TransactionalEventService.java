package it.berkhel.booking.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.berkhel.booking.app.actionport.ForEvents;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;

@Transactional
@Service
public class TransactionalEventService {

    private final ForEvents eventManager;

    public TransactionalEventService(ForEvents eventManager){
        this.eventManager = eventManager;
    }

    public Event create(Event event) throws EventAlreadyExistsException{
        return eventManager.createEvent(event);
    }
    
}
