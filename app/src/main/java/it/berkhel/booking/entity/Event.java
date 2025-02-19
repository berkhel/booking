package it.berkhel.booking.entity;

import java.util.HashSet;
import java.util.Set;

import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;

@Entity
public class Event {

    @Id
    private String id;

    @Version
    @Column(columnDefinition = "integer DEFAULT 0")
    private Long version; // for optimistic locking

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private Set<Ticket> tickets = new HashSet<>();

    @Column(name = "max_seats")
    private Integer maxSeats;

    @Column(name = "remaining_seats")
    private Integer remainingSeats;


    private Event(){} //for JPA

    public Event(String id, Integer maxSeats, Integer remainingSeats){
        this.id = id;
        this.maxSeats = maxSeats;
        this.remainingSeats = remainingSeats;
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    private void decrementAvailableSeats() throws SoldoutException{
        if(remainingSeats == 0){
            throw new SoldoutException("Sorry, no enough seats in event " + id + " for current request");
        }
        remainingSeats--;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", maxSeats=" + maxSeats + ", remainingSeats=" + remainingSeats + "]";
    }

    public void registerTicket(Ticket ticket) throws SoldoutException, DuplicateTicketException {
        if(tickets.stream().anyMatch(tkt -> tkt.getAttendee().getId().equals(ticket.getAttendee().getId()))){
            throw new DuplicateTicketException("Ticket was already purchased in a previous session for attendee " + ticket.getAttendee().getId() + " and event " + id);
        }
        decrementAvailableSeats();
    }
   
    
}
