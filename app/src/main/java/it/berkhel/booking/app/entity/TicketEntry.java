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
import jakarta.persistence.OneToOne;

@Entity
public class TicketEntry {

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

    private String state;

    @OneToOne
    private Ticket ticket;

    private TicketEntry(){} // for JPA

    public TicketEntry(Event event, Attendee attendee) throws EventNotFoundException {
        if(event == null){
            throw new EventNotFoundException("Event not found");
        }
        this.event = event;
        this.attendee = attendee;
        this.state = "Pending";
    }

    public void register() throws SoldoutException, DuplicateTicketException{
        event.registerTicket(this);
        this.state = "Fulfilled";
    }

    public String getId() {
        return id;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public Purchase getPurchase() {
        return purchase;
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
        TicketEntry other = (TicketEntry) obj;
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

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getState() {
        return state;
    }
    
}
