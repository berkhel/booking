package it.berkhel.booking.app.actionport;

import it.berkhel.booking.app.entity.Purchase;

public interface ForBooking {
    
    public Purchase process(Purchase purchase) throws Exception;

}
