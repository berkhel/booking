package it.berkhel.booking.app.drivenport;

import java.util.Optional;
import java.util.function.Predicate;

import it.berkhel.booking.app.exception.TransactionPostConditionException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public interface ForStorage {

    public void save(Purchase purchase, Predicate<Purchase> postCondition) throws TransactionPostConditionException;

    public Event save(Event event);

    public Optional<Event> getEventById(String any);

    public Optional<Ticket> getTicketBy(String eventId, String attendeeId);
    
}