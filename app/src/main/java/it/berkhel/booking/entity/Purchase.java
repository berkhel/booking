package it.berkhel.booking.entity;

import java.util.HashSet;
import java.util.Set;

import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "purchase", fetch = FetchType.LAZY)
    private Set<Ticket> tickets;

    private Purchase(){} // for JPA
    
    public Purchase(Set<Ticket> tickets) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException{
        validateSize(tickets);
        checkEvent(tickets);
        checkDuplicate(tickets);
        for(var ticket : tickets){
            ticket.register();
            ticket.setPurchase(this);
        };
        this.tickets = tickets;
    }

    public String getId() {
        return id;
    }

    public Set<Ticket> getTickets(){
        return tickets;
    }


    @Override
    public String toString() {
        return "Purchase [id=" + id + "]";
    }


    private void checkEvent(Set<Ticket> tickets) throws EventNotFoundException {
        for (var ticket : tickets) {
            if(ticket == null || ticket.getEvent() == null){
                throw new EventNotFoundException("Event not found");
            }
        }
    }

    private void validateSize(Set<Ticket> tickets) throws BadPurchaseRequestException {
        if(tickets.size() < 1){
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(tickets.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }
    }

    private void checkDuplicate(Set<Ticket> tickets) throws DuplicateTicketException{
        Set<String> checkDuplicateTickets = new HashSet<>();

        for (var ticket : tickets) {
            String attendeeId = ticket.getAttendee().getId();
            String eventId = ticket.getEvent().getId();
            String ticketKey = eventId + "~" + attendeeId;

            if (checkDuplicateTickets.contains(ticketKey)) {
                throw new DuplicateTicketException("Duplicate ticket for attendee " + attendeeId + " and event " + eventId);
            }
            checkDuplicateTickets.add(ticketKey);
        }
    }

}
