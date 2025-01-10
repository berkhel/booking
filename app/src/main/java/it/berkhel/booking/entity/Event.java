package it.berkhel.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Event {

    @Id
    String id;

    @Column(name = "max_seats")
    Integer maxSeats;

    @Column(name = "remaining_seats")
    Integer remainingSeats;
    
}
