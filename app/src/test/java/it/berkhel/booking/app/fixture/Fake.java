package it.berkhel.booking.app.fixture;


import java.util.Random;

import it.berkhel.booking.app.entity.Attendee;
import it.berkhel.booking.app.entity.Event;
import it.berkhel.booking.app.entity.TicketEntry;
import it.berkhel.booking.app.exception.EventNotFoundException;

public class Fake {

    public static Event event(){
        return new Event("EVFAKE"+new Random().nextInt(), 10);
    }

    public static Attendee attendee(){
        return new Attendee("ATFAKE"+new Random().nextInt(),"/","/","/","/");
    }

    public static TicketEntry ticket(){
        TicketEntry ticket = null;
        try {
            ticket = new TicketEntry(event(), attendee());
        } catch (EventNotFoundException e) {
            e.printStackTrace();
        }
        return ticket;
    }
    
}
