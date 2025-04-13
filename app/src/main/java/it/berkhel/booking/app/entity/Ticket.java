package it.berkhel.booking.app.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

/**
 * Invariant: event != null 
 *         && account != null
 *         && seat != null
 *         && event.getTickets().noneMatch(t -> t.seat == seat)
 */
@Entity
public class Ticket {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String seat;


    @ManyToOne
    @JoinColumn(name = "event", nullable = false)
    private Event event;


    @ManyToOne
    @JoinColumn(name = "account", nullable = false)
    private Account account;


    @ManyToOne(cascade = CascadeType.PERSIST)
    private Attendee attendee;

    private Ticket(){} //for JPA

    /**
     * tickets can be created only when an event is created
     */
    public Ticket(Event event, String seat){
        this.seat = seat;
        this.event = event;
        this.account = event.getAccount();
    }

    public Event getEvent() {
        return event;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

}
