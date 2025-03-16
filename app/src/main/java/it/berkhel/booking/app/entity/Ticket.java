package it.berkhel.booking.app.entity;

import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Ticket is the resource that can be exchanged for a seat the day of the event
 * It belongs to a ticket account and should belong to it
 */
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "attendee_id", nullable = false)
    private Attendee attendee;

    @ManyToOne
    @JoinColumn(name = "purchase_id", nullable = false)
    private Purchase purchase;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    private Ticket(){} // for JPA

    public Ticket(Event event, Attendee attendee) throws EventNotFoundException {
        if(event == null){
            throw new EventNotFoundException("Event not found");
        }
        this.event = event;
        this.attendee = attendee;
    }

    public void register() throws SoldoutException, DuplicateTicketException{
        event.registerTicket(this);
    }

    public String getId() {
        return id;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public Event getEvent() {
        return event;
    }

    void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    @Override
    public String toString() {
        return "Ticket [id=" + id + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 29;
        int result = 1;
        result = prime * result + ((attendee == null) ? 0 : attendee.hashCode());
        result = prime * result + ((event == null) ? 0 : event.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Ticket other = (Ticket) obj;
        if (attendee == null) {
            if (other.attendee != null)
                return false;
        } else if (!attendee.equals(other.attendee))
            return false;
        if (event == null) {
            if (other.event != null)
                return false;
        } else if (!event.equals(other.event))
            return false;
        return true;
    }
    
}
