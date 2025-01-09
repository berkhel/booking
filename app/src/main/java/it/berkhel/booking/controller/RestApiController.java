package it.berkhel.booking.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import it.berkhel.booking.actionport.ForBooking;
import it.berkhel.booking.entity.Reservation;

@RestController
public class RestApiController {

    private final ForBooking bookingManager;


    public RestApiController(ForBooking bookingManager){
        this.bookingManager = bookingManager;
    }

    @PostMapping(value = "/booking", produces = "application/json")
    public Reservation book() {
       return bookingManager.book();
    }

    
}
