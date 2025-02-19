package it.berkhel.booking.unit.fixture;


import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Ticket;

import java.util.Random;
import it.berkhel.booking.entity.Attendee;

public class Fake {

    public static Event event(){
        return new Event("EVFAKE"+new Random().nextInt(),10, 10);
    }

    public static Attendee attendee(){
        return new Attendee("ATFAKE"+new Random().nextInt(),"/","/","/","/");
    }

    public static Ticket ticket(){
        return new Ticket(event(), attendee());
    }
    
}
