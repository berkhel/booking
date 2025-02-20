package it.berkhel.booking.unit.fixture;


import java.util.Random;

import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Ticket;

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
