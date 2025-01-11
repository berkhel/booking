package it.berkhel.booking.unit.fixture;

import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Ticket;

public class Fake {

    public static Event event(){
        return new Event("EVFAKE0001",100,100);
    }

    public static Ticket ticket(){
        Ticket ticket = new Ticket();
        ticket.setEvent(event());
        return ticket;
    }
    
}
