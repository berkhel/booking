package it.berkhel.booking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {

    @Id
    @JsonProperty
    private String id;

    @ManyToOne
    @JsonProperty
    private Attendee attendee;

    @ManyToOne
    @JsonProperty
    private Event event;
    
}
