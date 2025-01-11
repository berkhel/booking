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

    public Event(){}

    public Event(String id, Integer maxSeats, Integer remainingSeats){
        this.id = id;
        this.maxSeats = maxSeats;
        this.remainingSeats = remainingSeats;
    }

    public String getId() {
        return id;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", maxSeats=" + maxSeats + ", remainingSeats=" + remainingSeats + "]";
    }
   
    
}
