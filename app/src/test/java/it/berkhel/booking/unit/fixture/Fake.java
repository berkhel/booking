package it.berkhel.booking.unit.fixture;


import it.berkhel.booking.entity.Event;
import it.berkhel.booking.entity.Ticket;

import java.util.Random;
import java.util.stream.IntStream;

import it.berkhel.booking.entity.Attendee;

public class Fake {

    public static Event event(){
        return new Event("EVFAKE"+new Random().nextInt(),new Random().nextInt(100),new Random().nextInt(100));
    }

    public static Attendee attendee(){
        return new Attendee("ATFAKE"+new Random().nextInt(),"/","/","/","/");
    }

    public static Ticket ticket(){
        Ticket ticket = new Ticket();
        ticket.setEvent(event());
        ticket.setAttendee(attendee());
        return ticket;
    }
    
}
