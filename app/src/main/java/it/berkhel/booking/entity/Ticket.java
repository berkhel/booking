package it.berkhel.booking.entity;

import org.springframework.data.annotation.Reference;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Attendee attendee;

    @ManyToOne
    private Event event;

    public String getId() {
        return id;
    }

    public Attendee getAttendee() {
        return attendee;
    }

    public Event getEvent() {
        return event;
    }

    public void setAttendee(Attendee attendee) {
        this.attendee = attendee;
    }

    public void setEvent(Event event) {
        this.event = event;
    }



    
}
