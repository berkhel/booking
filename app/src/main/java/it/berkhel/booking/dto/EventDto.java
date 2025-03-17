package it.berkhel.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class EventDto {


    @NotBlank
    String id;
    
    @PositiveOrZero
    @NotNull
    Integer maxSeats;



    public void setId(String id) {
        this.id = id;
    }



    public void setMaxSeats(Integer maxSeats) {
        this.maxSeats = maxSeats;
    }




}
