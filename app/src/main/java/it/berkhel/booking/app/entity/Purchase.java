package it.berkhel.booking.app.entity;

import java.util.HashSet;
import java.util.Set;

import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

/**
 * Purchase is the transaction that contains the list of entries that records movements from the event account
 */
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "purchase")
    private Set<TicketEntry> ticketEntries;

    @ManyToOne
    private Account account;

    private Purchase(){} // for JPA
    
    public Purchase(Account account, Set<TicketEntry> ticketEntries) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException{
        validateSize(ticketEntries);
        for(var entry : ticketEntries){
            entry.setPurchase(this);
            entry.register();
        };
        this.ticketEntries = ticketEntries;
        this.account = account;
    }

    private void validateSize(Set<TicketEntry> ticketEntries) throws BadPurchaseRequestException {
        if(ticketEntries.size() < 1){
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(ticketEntries.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }
    }

    public String getId() {
        return id;
    }

    public Set<TicketEntry> getTicketEntries(){
        return ticketEntries;
    }


    @Override
    public String toString() {
        return "Purchase [id=" + id + "]";
    }

    public Account getAccount() {
        return account;
    }



}
