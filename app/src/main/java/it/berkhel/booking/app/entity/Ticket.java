package it.berkhel.booking.app.entity;

import jakarta.persistence.CascadeType;
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


    public Ticket(Event event){
        this.seat = event.nextSeat(); 
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
        assert account.contains(this);
        this.account = account;
    }

    void assign(TicketEntry entry) {
        if (!account.equals(event.getAccount())) {
            setAttendee(entry.getAttendee());
            entry.setTicket(this);
        }
        assert entry.getState().equals(TicketEntry.State.ASSIGNED) : "Ticket entry not fulfilled!";
    }

}
