package it.berkhel.booking.app.entity;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;

/**
 * Event is an account of seats created by the event organizer 
 */
@Entity
public class Event {

    @Id
    private String id;

    @Version
    @Column(columnDefinition = "integer DEFAULT 0")
    private Long version; // for optimistic locking

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<TicketEntry> tickets = new HashSet<>();



    @Column(name = "max_seats")
    private Integer maxSeats;


    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> ticketSeats;


    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    private Event(){} //for JPA


    public Event(String id, Integer maxSeats){
        this.id = id;
        this.maxSeats = maxSeats;
        this.account = new Account(id);
        this.ticketSeats = createTickets(maxSeats);
        this.account.addTickets(ticketSeats);
    }

    public String getId() {
        return id;
    }

    public Integer getRemainingSeats() {
        return ticketSeats.size();
    }

    private void decrementAvailableSeats() throws SoldoutException{
        if(ticketSeats.size() == 0){
            throw new SoldoutException("Sorry, no enough seats in event " + id + " for current request");
        }
        ticketSeats.removeFirst();
    }


    public void registerTicket(TicketEntry ticket) throws SoldoutException, DuplicateTicketException {
        if (tickets.contains(ticket)) {
            throw new DuplicateTicketException("Ticket was already purchased in a previous session for attendee "
                    + ticket.getAttendee().getId() + " and event " + id);
        }
        tickets.add(ticket);
        decrementAvailableSeats();
    }

    @Override
    public int hashCode() {
        final int prime = 17;
        return prime + ((id == null) ? 0 : id.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", maxSeats=" + maxSeats + ", remainingSeats=" + getRemainingSeats() + "]";
    }

    private List<Ticket> createTickets(Integer ticketQty) {

        Character lastTicketLetter = 'A';
        Integer lastTicketNumber = 0;

        List<Ticket> tickets = new LinkedList<>();

        while(ticketQty-- > 0){
            if ((lastTicketLetter + lastTicketNumber) % 2 == 0) {
                lastTicketNumber++;
                if (lastTicketLetter > 'A') {
                    lastTicketLetter = lastTicketLetter--;
                }
            } else {
                lastTicketLetter++; 
                if (lastTicketNumber > 0) {
                    lastTicketNumber--;
                }
            }

            tickets.add(new Ticket(this,lastTicketLetter + "" + lastTicketNumber));

        }

        return tickets;

    }


    public Account getAccount() {
        return account;
    }
   
    
}