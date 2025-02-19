package it.berkhel.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.berkhel.booking.entity.Attendee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketDto {

    @JsonProperty
    private String id;

    @JsonProperty
    @NotNull
    private AttendeeDto attendee;

    @JsonProperty
    @NotBlank
    private String eventId;

    public TicketDto(AttendeeDto attendee, String eventId) {
        this.attendee = attendee;
        this.eventId = eventId;
    }

    public AttendeeDto getAttendee() {
        return attendee;
    }

    public String getEventId() {
        return eventId;
    }

    public void setId(String id){
        this.id = id;
    }

    @Override
    public String toString() {
        return "TicketDto [attendee=" + attendee + ", eventId=" + eventId + "]";
    }
    
    
}
