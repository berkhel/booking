package it.berkhel.booking.app;

import java.util.List;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.app.exception.TooManyTicketsException;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;

    private App(ForStorage storage) {
        this.storage = storage;
    }

    @Override
    public Purchase purchase(List<Ticket> tickets) throws Exception{
        if(tickets.size() < 1){
            throw new Exception("At least one ticket must be included in the request");
        }
        if(tickets.size() > 3){
            throw new TooManyTicketsException("Cannot purchase more than 3 tickets");
        }
        Purchase purchase = new Purchase();
        for(var ticket : tickets){
            var event = ticket.getEvent();
            if(event.getRemainingSeats() < 1){
                throw new SoldoutException("There are no remaining seats for event "+event);
            }
            ticket.setPurchase(purchase);
        }
        purchase.setTickets(tickets);
        storage.save(purchase);
        return purchase;
    }


}