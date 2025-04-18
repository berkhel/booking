package it.berkhel.booking.app.entity;

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
import jakarta.persistence.PostLoad;
import jakarta.persistence.Transient;

/**
 * Purchase is the transaction that contains the list of entries that records movements from the event account
 * Invariant: ticketEntries.size() > 0
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

    @PostLoad
    public void initializeAccountId(){
        this.accountId = this.account.getId();
    }

    private Purchase(){} // for JPA
    
    public Purchase(String accountId, Set<TicketEntry> ticketEntries) throws BadPurchaseRequestException {
        validateSize(ticketEntries);
        for(var entry : ticketEntries){
            entry.setPurchase(this);
        };
        this.ticketEntries = ticketEntries;
        this.accountId = accountId;
    }



    private void validateSize(Set<TicketEntry> ticketEntries) throws BadPurchaseRequestException {
        if(ticketEntries.size() < 1){
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(ticketEntries.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }
    }

    public void process() throws SoldoutException, DuplicateTicketException {
        assert account != null;
        for(TicketEntry entry : ticketEntries){
            account.claim(entry);
        };
    }


    public Set<TicketEntry> getTicketEntries(){
        return ticketEntries;
    }

    public String getId() {
        return id;
    }

    public String getAccountId() {
        if(accountId == null){
            this.accountId = account.getId();
        }
        return accountId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Purchase [id=" + id + "]";
    }


}
