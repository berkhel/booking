package it.berkhel.booking.app.entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

/**
 * Responsibility: create the tickets  
 * Invariant: account != null
 *         && tickets.size() > 0
 *            
 */
@Entity
public class Event {

    @Id
    private String id;

    // @Version
    // @Column(columnDefinition = "integer DEFAULT 0")
    // private Long version; // for optimistic locking



    @Column(name = "max_seats")
    private Integer maxSeats;


    public Integer getMaxSeats() {
        return maxSeats;
    }


    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ticket> tickets;


    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }


    @OneToOne(cascade = CascadeType.ALL)
    private Account account;

    @Transient
    Supplier<String> seatGenerator;

    private Event(){} //for JPA


    public Event(String id, Integer maxSeats, Supplier<String> seatGenerator){
        this.id = id;
        this.maxSeats = maxSeats;
        this.account = new Account(id);
        this.seatGenerator = seatGenerator;
        this.tickets = new LinkedList<>();
        createTickets(maxSeats);
        this.account.addTickets(tickets);
    }


    public void ensureNoPreviousEntriesForSameAttendee(Attendee attendee) throws DuplicateTicketException {
        if (tickets.stream()
        .anyMatch(ticket -> attendee.equals(ticket.getAttendee()))) {
            throw new DuplicateTicketException("Ticket was already purchased in a previous session for attendee "
                    + attendee.getId() + " and event " + id);
        }
    }

    public void ensureTicketAvailability() throws SoldoutException {
        if(account.ticketsCount() == 0){
            throw new SoldoutException("Sorry, no enough seats in event " + id + " for current request");
        }
    }

    public Account getAccount() {
        return account;
    }

   
    public String getId() {
        return id;
    }

    public Integer getRemainingSeats() {
        return account.ticketsCount();
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


    private void createTickets(Integer ticketQty) {


        while(ticketQty-- > 0){
            tickets.add(new Ticket(this));
        }

    }



    public String nextSeat() {
        assert maxSeats - tickets.size() > 0 : "Maximum number of tickets exceeded!";

        return seatGenerator.get();
    }







}