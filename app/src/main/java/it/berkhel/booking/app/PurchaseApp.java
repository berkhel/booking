package it.berkhel.booking.app;

import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.ConcurrentPurchaseException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;

class PurchaseApp {

    private ForStorage storage;
    private ForSendingMessage messageSender;

    PurchaseApp(ForStorage storage, ForSendingMessage messageSender){
        this.storage = storage;
        this.messageSender = messageSender;
    }

    public Purchase process(Purchase purchase) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException, ConcurrentPurchaseException {
        prepareBeforeProcess(purchase);
        doProcess(purchase);
        return purchase;
    }

    void doProcess(Purchase purchase) throws SoldoutException, DuplicateTicketException, EventNotFoundException, ConcurrentPurchaseException{
        purchase.process();
        storage.save(purchase);
        sendMessageAbout(purchase);
    }

    private void prepareBeforeProcess(Purchase purchase) throws EventNotFoundException {
        linkWithEntitiesFromStorage(purchase);
    }

    private void linkWithEntitiesFromStorage(Purchase purchase) throws EventNotFoundException {
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
        }
    }

    public void sendMessageAbout(Purchase purchase) {
        purchase.getTicketEntries().stream().filter(entry -> entry.getState().equals(TicketEntry.State.ASSIGNED)).forEach(entry ->
            messageSender.sendMessage(entry.getAttendee(), "Here's your ticket: " + entry.getId())
        );
    }

    
}
