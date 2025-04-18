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
 * accounts don't have memory of transaction, they just contains Tickets
 * they also have an (implicit) owner who is the user or the event organizer
 * in order to move a ticket in there, the ticket should exist first and
 * should be also go together with a pending ticket entry
 * 
 * ticket of another account + pending ticket entry => ticket + fulfilled ticket entry
 * this mean that only an Account can fulfill a ticket entry, and then a ticket entry need
 * to fulfill need to point to a ticket that has an account different from the event account
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
    private List<Purchase> purchases;

    private Account(){}


    public Account(String id){
        this.id = id;
        this.purchases = new ArrayList<Purchase>();
        this.tickets = new ArrayList<Ticket>();
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


    public void addPurchase(Purchase purchase) {
        purchases.add(purchase);
    }


    public boolean contains(Ticket ticket) {
        return tickets.contains(ticket);
    }

    

}
