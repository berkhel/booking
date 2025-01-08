package it.berkhel.booking;

public class InMemoryStorage implements ForStorage {

    private Reservation aReservation;

    @Override
    public void save(Reservation aReservation) {
        this.aReservation = aReservation;
    }

    @Override
    public Reservation retrieveById(String reservationId) {
        return aReservation;
    }

}
