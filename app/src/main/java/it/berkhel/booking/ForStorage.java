package it.berkhel.booking;

public interface ForStorage {

    public void save(Reservation aReservation);

    public Reservation retrieveById(String reservationId);
    
}