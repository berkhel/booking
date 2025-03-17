package it.berkhel.booking.app.entity;

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
    @JoinColumn(name = "account")
    private Account account;

    public void setAccount(Account account) {
        this.account = account;
    }

    private Ticket(){} //for JPA

    /**
     * tickets can be created only when an event is created
     */
    public Ticket(Event event, String seat){
        this.seat = seat;
        this.event = event;
        this.account = event.getAccount();
    }
}
