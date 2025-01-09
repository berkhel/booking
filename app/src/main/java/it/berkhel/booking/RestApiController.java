package it.berkhel.booking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    private final ForBooking bookingManager;


    public RestApiController(ForBooking bookingManager){
        this.bookingManager = bookingManager;
    }

    @GetMapping(value = "/booking", produces = "application/json")
    public Reservation hello() {
       return bookingManager.book();
    }

    
}
