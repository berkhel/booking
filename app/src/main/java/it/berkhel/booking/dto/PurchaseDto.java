package it.berkhel.booking.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PurchaseDto {
    
    @JsonProperty
    private String id;

    @JsonProperty
    private List<TicketDto> tickets;

    public void setId(String id) {
        this.id = id;
    }

    public void setTickets(List<TicketDto> tickets) {
        this.tickets = tickets;
    }
    
}
