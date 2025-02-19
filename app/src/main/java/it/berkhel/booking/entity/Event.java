package it.berkhel.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;

@Entity
public class Event {

    @Id
    private String id;

    @Version
    @Column(columnDefinition = "integer DEFAULT 0")
    private Long version;


    @Column(name = "max_seats")
    private Integer maxSeats;

    @Column(name = "remaining_seats")
    private Integer remainingSeats;


    private Event(){} //for JPA

    public Event(String id, Integer maxSeats, Integer remainingSeats){
        this.id = id;
        this.maxSeats = maxSeats;
        this.remainingSeats = remainingSeats;
    }

    public String getId() {
        return id;
    }

    public Long getVersion() {
        return version;
    }

    public Integer getMaxSeats() {
        return maxSeats;
    }

    public Integer getRemainingSeats() {
        return remainingSeats;
    }

    private void decrementAvailableSeats(){
        remainingSeats--;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", maxSeats=" + maxSeats + ", remainingSeats=" + remainingSeats + "]";
    }

    public void registerTicket(Ticket ticket) {
        decrementAvailableSeats();
    }
   
    
}
