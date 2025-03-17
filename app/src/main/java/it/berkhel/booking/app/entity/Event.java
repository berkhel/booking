package it.berkhel.booking.app.entity;

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

    @Column(name = "remaining_seats")
    private Integer remainingSeats;


    private Event(){} //for JPA

    public Event(String id, Integer maxSeats){
        this.id = id;
        this.maxSeats = maxSeats;
        this.remainingSeats = maxSeats;
    }

    public String getId() {
        return id;
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
        return "Event [id=" + id + ", maxSeats=" + maxSeats + ", remainingSeats=" + remainingSeats + "]";
    }
   
    
}
