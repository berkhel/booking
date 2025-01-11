package it.berkhel.booking.entity;


import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Purchase {

    @Id
    @JsonProperty
    private String id = UUID.randomUUID().toString();

    @JsonProperty
    @OneToMany(mappedBy = "purchase")
    private List<Ticket> tickets;


    public String getId() {
        return id;
    }

    public List<Ticket> getTickets(){
        return tickets;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + ", tickets=" + tickets + "]";
    }



}
