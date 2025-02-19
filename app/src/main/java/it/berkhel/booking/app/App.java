package it.berkhel.booking.app;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.actionport.ForEvents;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public class App implements ForBooking, ForEvents {

    public static App init(ForStorage storage, ForSendingMessage messageBroker){
        return new App(storage, messageBroker);
    }
    
    private final ForStorage storage;
    private final ForSendingMessage messageBroker;

    private App(ForStorage storage, ForSendingMessage messageBroker) {
        this.storage = storage;
        this.messageBroker = messageBroker;
    }

    @Override
    public Purchase purchase(Set<Ticket> tickets) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException, ConcurrentPurchaseException {

        Purchase purchase = new Purchase(tickets);

        //checkDuplicateInStorage(tickets);

        storage.save(purchase);

        for (var ticket : tickets) {
            messageBroker.sendMessage(ticket.getAttendee(), "Here's your ticket:" + ticket.getId());
        }
        
        return purchase;
    }

    private void checkDuplicateInStorage(Set<Ticket> tickets) throws DuplicateTicketException {
        for (var ticket : tickets) {
            var event = ticket.getEvent();
            var eventId = event.getId();
            var attendeeId = ticket.getAttendee().getId();

            if(storage.getTicketBy(event.getId(), attendeeId).isPresent()){
                throw new DuplicateTicketException("Ticket was already purchased in a previous session for attendee " + attendeeId + " and event " + eventId);
            }

        }
    }



    @Override
    public Event createEvent(Event event) throws EventAlreadyExistsException {
        if (storage.getEventById(event.getId()).isPresent()) {
            throw new EventAlreadyExistsException("Event already exists");
        }
        return storage.save(event);
    }


}