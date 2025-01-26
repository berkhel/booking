package it.berkhel.booking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Event {

    @Id
    @NotBlank
    String id;

    @Version
    @Column(columnDefinition = "integer DEFAULT 0")
    private Long version;


    @NotNull
    @Column(name = "max_seats")
    Integer maxSeats;

    @PositiveOrZero
    @NotNull
    @Column(name = "remaining_seats")
    Integer remainingSeats;

    public void setRemainingSeats(Integer remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public Event(){}

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

    public void decrementAvailableSeats(){
        remainingSeats--;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", maxSeats=" + maxSeats + ", remainingSeats=" + remainingSeats + "]";
    }
   
    
}
