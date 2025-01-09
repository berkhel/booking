package it.berkhel.booking;


import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Reservation {

    @Id
    private String id;

    public Reservation(){
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + "]";
    }


}
