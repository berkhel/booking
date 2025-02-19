package it.berkhel.booking.entity;

import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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

    public Ticket(Event event, Attendee attendee) {
        if(event == null){
            throw new NullPointerException();
        }
        this.event = event;
        this.attendee = attendee;
    }

    public void register() throws SoldoutException{
        event.registerTicket(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Event getEvent() {
        return event;
    }


    @Override
    public String toString() {
        return "Ticket [id=" + id + "]";
    }




    
}
