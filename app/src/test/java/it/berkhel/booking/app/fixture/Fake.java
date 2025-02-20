package it.berkhel.booking.app.fixture;


import java.util.Random;

import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.Ticket;
import it.berkhel.booking.app.exception.EventNotFoundException;

public class Fake {

    public static Event event(){
        return new Event("EVFAKE"+new Random().nextInt(),10, 10);
    }

    public static Attendee attendee(){
        return new Attendee("ATFAKE"+new Random().nextInt(),"/","/","/","/");
    }

    public static Ticket ticket(){
        Ticket ticket = null;
        try {
            ticket = new Ticket(event(), attendee());
        } catch (EventNotFoundException e) {
            e.printStackTrace();
        }
        return ticket;
    }
    
}
