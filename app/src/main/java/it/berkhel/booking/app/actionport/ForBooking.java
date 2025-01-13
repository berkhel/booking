package it.berkhel.booking.app.actionport;

import java.util.List;
import java.util.Set;

import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public interface ForBooking {
    
    public Purchase purchase(Set<Ticket> tickets) throws Exception;

}
