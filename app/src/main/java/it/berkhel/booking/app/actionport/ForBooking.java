package it.berkhel.booking.app.actionport;

import java.util.List;

import it.berkhel.booking.entity.Purchase;
import it.berkhel.booking.entity.Ticket;

public interface ForBooking {
    
    public Purchase purchase(List<Ticket> tickets) throws Exception;

}
