package it.berkhel.booking.app;

import java.util.List;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
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
            throw new Exception("Cannot purchase more than 3 tickets");
        }
        Purchase purchase = new Purchase();
        tickets.forEach(ticket -> ticket.setPurchase(purchase));
        purchase.setTickets(tickets);
        storage.save(purchase);
        return purchase;
    }


}