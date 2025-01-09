package it.berkhel.booking.drivenport;

import it.berkhel.booking.Reservation;

public interface ForStorage {

    public void save(Reservation aReservation);

    public Reservation retrieveById(String reservationId);
    
}