package it.berkhel.booking.entity;


import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Reservation {

    @Id
    @JsonProperty
    private String id = UUID.randomUUID().toString();


    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Reservation [id=" + id + "]";
    }


}
