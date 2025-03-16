package it.berkhel.booking.app.actionport;

import java.util.Set;

import it.berkhel.booking.app.entity.Purchase;
import it.berkhel.booking.app.entity.TicketEntry;

public interface ForBooking {
    
    public Purchase purchase(Set<TicketEntry> tickets) throws Exception;

}
