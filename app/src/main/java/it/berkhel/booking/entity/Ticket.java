package it.berkhel.booking.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Ticket {

    @Id
    @JsonProperty
    private String id;

    @JsonProperty
    private Attendee attendee;

    @JsonProperty
    private Event event;
    
}
