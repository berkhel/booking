package it.berkhel.booking.app;

import java.util.Optional;
import java.util.Set;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.actionport.ForEvents;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;

public class App implements ForBooking, ForEvents {

    static App instance;

    public static App init(ForStorage storage, ForSendingMessage messageSender){
        if(instance != null){
            throw new RuntimeException("Cannot instanciate the app twice");
        }
        instance = new App(storage, messageSender);
        return instance;
    }
    
    private final ForStorage storage;
    private final ForSendingMessage messageSender;

    private App(ForStorage storage, ForSendingMessage messageSender) {
        this.storage = storage;
        this.messageSender = messageSender;
    }

    @Override
    public Purchase callPurchase(Purchase purchase) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException, ConcurrentPurchaseException {
        linkPersistentEntities(purchase);
        return purchase(purchase);
    }

    Purchase purchase(Purchase purchase) throws SoldoutException, DuplicateTicketException, EventNotFoundException, ConcurrentPurchaseException{
        purchase.process();
        storage.save(purchase);
        return purchase;
    }

    private void linkPersistentEntities(Purchase purchase) throws EventNotFoundException {

        Account account = storage.getAccountById(purchase.getAccountId()).orElseGet(() -> {
            Account newAccount = new Account(purchase.getAccountId());
            storage.saveAccount(newAccount);
            return newAccount;
        });

        purchase.setAccount(account);
        account.addPurchase(purchase);

        for (TicketEntry entry : purchase.getTicketEntries()) {
            Event event = storage.getEventById(entry.getEventId())
                    .orElseThrow(() -> new EventNotFoundException("Event not found"));
            entry.setEvent(event);
            event.addTicketEntry(entry);
        }

    }

    public void sendMessageAbout(Purchase purchase) {
        purchase.getTicketEntries().stream().filter(entry -> entry.getState().equals("Fulfilled")).forEach(entry ->
            messageSender.sendMessage(entry.getAttendee(), "Here's your ticket: " + entry.getId())
        );
    }

    





    @Override
    public Event createEvent(Event event) throws EventAlreadyExistsException {
        if (storage.getEventById(event.getId()).isPresent()) {
            throw new EventAlreadyExistsException("Event already exists");
        }
        return storage.save(event);
    }


}