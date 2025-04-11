package it.berkhel.booking.app.drivenport;

import java.util.Optional;

import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;

public interface ForStorage {

    public Purchase save(Purchase purchase) throws ConcurrentPurchaseException;

    public Event save(Event event);
    public Account saveAccount(Account account);

    public Optional<Event> getEventById(String any);

    public Optional<Account> getAccountById(String any);

    // public Optional<Ticket> getTicketBy(String eventId, String attendeeId);
    
}