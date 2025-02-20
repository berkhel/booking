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
import jakarta.persistence.OneToMany;

@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(mappedBy = "purchase")
    private Set<Ticket> tickets;

    private Purchase(){} // for JPA
    
    public Purchase(Set<Ticket> tickets) throws BadPurchaseRequestException, EventNotFoundException, DuplicateTicketException, SoldoutException{
        validateSize(tickets);
        for(var ticket : tickets){
            ticket.register();
            ticket.setPurchase(this);
        };
        this.tickets = tickets;
    }

    private void validateSize(Set<Ticket> tickets) throws BadPurchaseRequestException {
        if(tickets.size() < 1){
            throw new BadPurchaseRequestException("At least one ticket must be included in the request");
        }
        if(tickets.size() > 3){
            throw new BadPurchaseRequestException("Cannot purchase more than 3 tickets");
        }
    }

    public String getId() {
        return id;
    }

    public Set<Ticket> getTickets(){
        return tickets;
    }


    @Override
    public String toString() {
        return "Purchase [id=" + id + "]";
    }



}
