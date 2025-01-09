package it.berkhel.booking.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Event {

    @Id
    String id;

    Integer maxTickets;

    Integer remainingTickets;
    
}
