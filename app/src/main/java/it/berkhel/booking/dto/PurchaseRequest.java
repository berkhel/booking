package it.berkhel.booking.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PurchaseRequest {

    @JsonProperty
    @NotBlank
    public String accountId;

    @JsonProperty
    @NotNull
    public Set<TicketDto> tickets;

    
}
