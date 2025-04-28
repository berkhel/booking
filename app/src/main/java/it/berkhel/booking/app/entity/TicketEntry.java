package it.berkhel.booking.app.entity;

import it.berkhel.booking.app.exception.EventNotFoundException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;


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

    @Transient
    private String eventId;

    @PostLoad
    public void initializeEventId(){
        this.eventId = this.event.getId();
    }


    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event;


    public static enum State { PENDING, FULFILLED }
    private State state;

    @ManyToOne
    private Ticket ticket;


    private TicketEntry(){} // for JPA

    public TicketEntry(String eventId, Attendee attendee) throws EventNotFoundException {
        if(eventId == null){
            throw new EventNotFoundException("Event not found");
        }
        this.eventId = eventId;
        this.attendee = attendee;
        this.state = State.PENDING;
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

    public Ticket getTicket() {
        return ticket;
    }

    void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public State getState() {
        return state;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEvent(Event event) {
        assert event.getId().equals(eventId) : "EventId "+eventId + " != " + event.getId();
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }

    public void setTicket(Ticket ticket) {
        assert state == State.PENDING;
        this.ticket = ticket;
        this.state = State.FULFILLED;
    }


    @Override
    public String toString() {
        return "Ticket [id=" + id + "]";
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
        if (eventId == null) {
            if (other.eventId != null)
                return false;
        } else if (!eventId.equals(other.eventId))
            return false;
        return true;
    }



    
}
