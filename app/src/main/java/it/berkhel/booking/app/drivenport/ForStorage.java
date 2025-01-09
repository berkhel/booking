package it.berkhel.booking.app.drivenport;

import it.berkhel.booking.entity.Purchase;

public interface ForStorage {

    public void save(Purchase aReservation);

    public Purchase retrieveById(String reservationId);
    
}