package it.berkhel.booking.app.entity;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.app.exception.BadPurchaseRequestException;
import it.berkhel.booking.app.exception.DuplicateTicketException;
import it.berkhel.booking.app.exception.EventNotFoundException;
import it.berkhel.booking.app.exception.SoldoutException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Transient;

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

    @Transient
    private String accountId;

    private Purchase(){} // for JPA
    
    public Purchase(String accountId, Set<TicketEntry> ticketEntries) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException{
        validateSize(ticketEntries);
        for(var entry : ticketEntries){
            entry.setPurchase(this);
        };
        this.ticketEntries = ticketEntries;
        this.accountId = accountId;
    }

    public void commit(ForStorage storage) throws SoldoutException, DuplicateTicketException, EventNotFoundException{
         Optional<Account> accountOpt = storage.getAccountById(accountId);
    
         // If account doesn't exist, create and save it
         if (accountOpt.isEmpty()) {
             Account newAccount = new Account(accountId);
             this.account = storage.saveAccount(newAccount); // You'll need to add this method to ForStorage
         } else {
             this.account = accountOpt.get();
         }
        for(var entry : ticketEntries){
            Optional<Event> event = storage.getEventById(entry.getEventId());
            if(event.isEmpty()){
                throw new EventNotFoundException("Event not found");
            }
            entry.register(event.get());
        };

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

    public void setAccount(Account account) {
        this.account = account;
    }



}
