package it.berkhel.booking.app;

import java.util.List;
import java.util.Optional;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.app.exception.TransactionPostConditionException;
import it.berkhel.booking.entity.Event;
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
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(tickets.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }


        Purchase purchase = new Purchase();
        for(var ticket : tickets){
            ticket.setPurchase(purchase);
            ticket.getEvent().decrementAvailableSeats();
        }

        checkSoldoutEvents(tickets);

        purchase.setTickets(tickets);
        try {
            storage.save(purchase, prch -> eventWithSoldout(prch.getTickets()).isEmpty());
        }catch(TransactionPostConditionException ex){
            checkSoldoutEvents(tickets);
            throw ex;
        }
        return purchase;
    }

    private Optional<Event> eventWithSoldout(List<Ticket> tickets) {
        for (var ticket : tickets) {
            var event = ticket.getEvent();
            if (event.getRemainingSeats() < 0) {
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    private void checkSoldoutEvents(List<Ticket> tickets) throws SoldoutException {
        Optional<Event> eventWithSoldoutTickets = eventWithSoldout(tickets);
        if (eventWithSoldoutTickets.isPresent()) {
            throw new SoldoutException("Sorry, no remaining seats for event " + eventWithSoldoutTickets.get());
        }
    }


}