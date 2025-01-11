package it.berkhel.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Event {

    @Id
    String id;

    public Event(String id, Integer maxSeats, Integer remainingSeats){
        this.id = id;
        this.maxSeats = maxSeats;
        this.remainingSeats = remainingSeats;
    }

    @Column(name = "max_seats")
    Integer maxSeats;

    @Column(name = "remaining_seats")
    Integer remainingSeats;

    public String getId() {
        return id;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }
   
    
}
