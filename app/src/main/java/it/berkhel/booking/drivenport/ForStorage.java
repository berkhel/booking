package it.berkhel.booking.drivenport;

import it.berkhel.booking.entity.Reservation;

public interface ForStorage {

    public void save(Reservation aReservation);

    public Reservation retrieveById(String reservationId);
    
}