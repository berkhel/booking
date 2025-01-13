package it.berkhel.booking.app;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;


import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.app.actionport.ForEvents;
import it.berkhel.booking.app.drivenport.ForSendingMessage;
import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventAlreadyExistsException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import it.berkhel.booking.app.exception.TransactionPostConditionException;
import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public class App implements ForBooking, ForEvents {

    public static App init(ForStorage storage, ForSendingMessage messageBroker){
        return new App(storage, messageBroker);
    }
    
    private final ForStorage storage;
    private final ForSendingMessage messageBroker;

    private App(ForStorage storage, ForSendingMessage messageBroker) {
        this.storage = storage;
        this.messageBroker = messageBroker;
    }

    @Override
    public Purchase purchase(Set<Ticket> tickets) throws Exception{
        if(tickets.size() < 1){
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(tickets.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }

        Set<String> checkDuplicateTickets = new HashSet<>();


        for (var ticket : tickets) {
            Event event;
            String eventId;
            try {
                event = ticket.getEvent();
                eventId = event.getId();
            } catch (Exception ex) {
                throw new EventNotFoundException("Event not found");
            }
            String attendeeId = ticket.getAttendee().getId();
            String ticketKey = eventId + "~" + attendeeId;

            if (checkDuplicateTickets.contains(ticketKey)) {
                throw new DuplicateTicketException("Duplicate ticket for attendee " + attendeeId + " and event " + eventId);
            }
            checkDuplicateTickets.add(ticketKey);


            if(storage.getTicketBy(event, ticket.getAttendee()).isPresent()){
                throw new DuplicateTicketException("Ticket was already purchased in a previous session for attendee " + attendeeId + " and event " + eventId);
            }


            event.decrementAvailableSeats();
        }

        checkSoldoutEvents(tickets);

        Purchase purchase = new Purchase();
        tickets.forEach(ticket -> ticket.setPurchase(purchase));
        purchase.setTickets(tickets);

        try {
            storage.save(purchase, prch -> eventWithSoldout(prch.getTickets()).isEmpty());
        }catch(TransactionPostConditionException ex){
            checkSoldoutEvents(tickets);
            throw ex;
        }

        for(var ticket : purchase.getTickets()){
            messageBroker.sendMessage(ticket.getAttendee(), "Here's your ticket:"+ticket.getId());
        }

        
        return purchase;
    }

    private Optional<Event> eventWithSoldout(Set<Ticket> tickets) {
        for (var ticket : tickets) {
            var event = ticket.getEvent();
            if (event.getRemainingSeats() < 0) {
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    private void checkSoldoutEvents(Set<Ticket> tickets) throws SoldoutException {
        Optional<Event> eventWithSoldoutTickets = eventWithSoldout(tickets);
        if (eventWithSoldoutTickets.isPresent()) {
            throw new SoldoutException("Sorry, no enough seats in event " + eventWithSoldoutTickets.get().getId() + " for current request");
        }
    }

    @Override
    public Event createEvent(Event event) throws EventAlreadyExistsException {
        if (storage.getEventById(event.getId()).isPresent()) {
            throw new EventAlreadyExistsException("Event already exists");
        }
        return storage.save(event);
    }


}