package it.berkhel.booking.app.fixture;


import java.util.List;
import java.util.Random;
import java.util.Set;

import it.berkhel.booking.app.entity.Account;
import it.berkhel.booking.app.entity.AlphaNumSeatGenerator;
import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.SizePurchaseRule;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;

public class Fake {

    public static Event event(){
        return new Event("EVFAKE"+new Random().nextInt(), 10, new AlphaNumSeatGenerator());
    }

    public static Attendee attendee(){
        return Attendee.createAttendee("ATFAKE"+new Random().nextInt(),"/","/","/","/");
    }

    public static Account account(){
        return new Account("ACFAKE"+new Random().nextInt());
    }

    public static Purchase purchase(Account account, Set<TicketEntry> entries) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException {
        Purchase purchase = new Purchase(account.getId(), entries, List.of(new SizePurchaseRule()));
        purchase.setAccount(account);
        return purchase;
    }

    public static TicketEntry ticket(Event event, Attendee attendee){
        TicketEntry ticket = null;
        try {
            var eventId = event.getId();
            ticket = new TicketEntry(eventId, attendee);
            ticket.setEvent(event);
        } catch (EventNotFoundException e) {
            e.printStackTrace();
        }
        return ticket;
    }
    
}
