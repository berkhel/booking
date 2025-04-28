package it.berkhel.booking.app.entity;

import java.util.ArrayList;
import java.util.List;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

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
    private List<Purchase> commitments;

    private Account(){}


    public Account(String id, List<Ticket> tickets){
        this(id);
        this.addTickets(tickets);
    }

    public Account(String id){
        this.id = id;
        this.commitments = new ArrayList<Purchase>();
        this.tickets = new ArrayList<Ticket>();
    }

    public void addPurchase(Purchase purchase) {
        commitments.add(purchase);
    }

    private void addTickets(List<Ticket> tickets){
        tickets.stream().forEach(this::addTicket);
    }

    private void addTicket(Ticket ticket){
        this.tickets.add(ticket);
        ticket.setAccount(this);
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



    /**
     * a ticket entry is required to move a ticket from the event (pool) account
     */
    public Ticket moveFirstTicketTo(Account otherAccount, TicketEntry pendingTicketEntry) throws SoldoutException{
        assert pendingTicketEntry != null : "ticket entry is required on moveFirstTicketTo";
        Ticket movingTicket = tickets.removeFirst();
        otherAccount.addTicket(movingTicket);
        movingTicket.assign(pendingTicketEntry);
        return movingTicket;
    }




    public boolean contains(Ticket ticket) {
        return tickets.contains(ticket);
    }

    

}
