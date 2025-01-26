package it.berkhel.booking.app.drivenport;

import java.util.Optional;

import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public interface ForStorage {

    public Purchase save(Purchase purchase) throws ConcurrentPurchaseException;

    public Event save(Event event);

    public Optional<Event> getEventById(String any);

    public Optional<Ticket> getTicketBy(String eventId, String attendeeId);
    
}