package it.berkhel.booking.app.drivenport;

import java.util.function.Predicate;

import it.berkhel.booking.app.exception.TransactionPostConditionException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;

public interface ForStorage {

    public void save(Purchase purchase, Predicate<Purchase> postCondition) throws TransactionPostConditionException;

    public Purchase retrieveById(String purchaseId);

    public Event save(Event event);

    
}