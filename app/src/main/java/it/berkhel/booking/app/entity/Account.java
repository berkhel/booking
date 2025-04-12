package it.berkhel.booking.app.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Account {

    @Id
    private String id;

    public String getId() {
        return id;
    }


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    private Account(){}


    public Account(String id){
        this.id = id;
        this.tickets = new ArrayList<Ticket>();
    }

    public void addTickets(List<Ticket> tickets){
        this.tickets.addAll(tickets);
    }

    public void addTicket(Ticket ticket){
        this.tickets.add(ticket);
    }


    public List<Ticket> getTickets() {
        return tickets;
    }

    public void process(Purchase purchase, ForStorage storage) throws SoldoutException, DuplicateTicketException, EventNotFoundException{
        purchase.setAccount(this);
        for(TicketEntry entry : purchase.getTicketEntries()){
            Optional<Event> event = storage.getEventById(entry.getEventId());
            if(event.isEmpty()){
                throw new EventNotFoundException("Event not found");
            }
            claim(entry, event.get());
        };
    }

    public void claim(TicketEntry ticketEntry, Event event) throws SoldoutException, DuplicateTicketException{
        if (tickets.stream().filter(ticket -> ticket.getEvent().equals(event)).anyMatch(ticket -> ticketEntry.getAttendee().equals(ticket.getAttendee()))) {
            throw new DuplicateTicketException("Ticket was already purchased in a previous session for attendee "
                    + ticketEntry.getAttendee().getId() + " and event " + event.getId());
        }
        if(event.getAccount().getTickets().size() == 0){
            throw new SoldoutException("Sorry, no enough seats in event " + event.getId() + " for current request");
        }
        Ticket movedTicket = event.getAccount().moveFirstTicket(this);
        movedTicket.setAttendee(ticketEntry.getAttendee());
        ticketEntry.setTicket(movedTicket);
    }

    public Ticket moveFirstTicket(Account otherAccount) throws SoldoutException{
        Ticket movingTicket = tickets.removeFirst();
        otherAccount.addTicket(movingTicket);
        movingTicket.setAccount(otherAccount);
        return movingTicket;
    }

    

}
