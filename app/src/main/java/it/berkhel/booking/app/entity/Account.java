package it.berkhel.booking.app.entity;

import java.util.ArrayList;
import java.util.List;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

/**
 * Responsibility: Keep the tickets balance, Keeps the transaction history
 * Invariant: tickets != null ; history != null 
 */
@Entity
public class Account {
    

    @Id
    private String id;

    public String getId() {
        return id;
    }


    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<Ticket> tickets;

    @OneToMany(mappedBy = "account")
    private List<Purchase> history;

    private Account(){}


    public Account(String id){
        this.id = id;
        this.history = new ArrayList<Purchase>();
        this.tickets = new ArrayList<Ticket>();
    }

    public void addPurchase(Purchase purchase) {
        history.add(purchase);
    }

    public void addTickets(List<Ticket> tickets){
        this.tickets.addAll(tickets);
    }

    public void addTicket(Ticket ticket){
        this.tickets.add(ticket);
    }


    public Integer ticketsCount() {
        return tickets.size();
    }


    public void claim(TicketEntry ticketEntry) throws SoldoutException, DuplicateTicketException{
        Event event = ticketEntry.getEvent();
        event.ensureNoPreviousEntriesForSameAttendee(ticketEntry.getAttendee());
        event.ensureTicketAvailability();
        event.getAccount().moveFirstTicketTo(this, ticketEntry);
    }



    public Ticket moveFirstTicketTo(Account otherAccount, TicketEntry pendingTicketEntry) throws SoldoutException{
        Ticket movingTicket = tickets.removeFirst();
        otherAccount.addTicket(movingTicket);
        movingTicket.setAccount(otherAccount);
        movingTicket.fulfill(pendingTicketEntry);
        return movingTicket;
    }




    public boolean contains(Ticket ticket) {
        return tickets.contains(ticket);
    }

    

}
