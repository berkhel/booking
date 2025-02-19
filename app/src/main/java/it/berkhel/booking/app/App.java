package it.berkhel.booking.app;

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

        storage.save(purchase);

        tickets.forEach(ticket ->
            messageBroker.sendMessage(ticket.getAttendee(), "Here's your ticket:" + ticket.getId())
        );
        
        return purchase;
    }



    @Override
    public Event createEvent(Event event) throws EventAlreadyExistsException {
        if (storage.getEventById(event.getId()).isPresent()) {
            throw new EventAlreadyExistsException("Event already exists");
        }
        return storage.save(event);
    }


}