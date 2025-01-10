package it.berkhel.booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import it.berkhel.booking.app.actionport.ForBooking;
import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

@RestController
public class RestApiController {

    private final ForBooking bookingManager;


    public RestApiController(ForBooking bookingManager){
        this.bookingManager = bookingManager;
    }

    @PostMapping(value = "/booking" , produces = "application/json")
    public Purchase book(@RequestBody List<Ticket> tickets) throws Exception {
        System.out.println("LOG: Called Controller");
       return bookingManager.purchase(tickets);
    }

    
}
