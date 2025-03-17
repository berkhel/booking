package it.berkhel.booking.app.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Account {

    @Id
    private String id;

    @OneToMany(mappedBy = "account")
    private List<Ticket> tickets;

    private Account(){}


    public Account(String id){
        this.id = id;
        this.tickets = new ArrayList<Ticket>();
    }

    public void addTickets(List<Ticket> tickets){
        this.tickets.addAll(tickets);
    }


    public List<Ticket> getTickets() {
        return tickets;
    }

    

}
