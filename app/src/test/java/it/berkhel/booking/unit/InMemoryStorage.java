package it.berkhel.booking.unit;

import java.util.HashMap;
import java.util.Map;

import it.berkhel.booking.Reservation;
import it.berkhel.booking.drivenport.ForStorage;

public class InMemoryStorage implements ForStorage {

    private Map<String,Reservation> memory = new HashMap<>();

    @Override
    public void save(Reservation aReservation) {
        memory.put(aReservation.getId(), aReservation);
    }

    @Override
    public Reservation retrieveById(String reservationId) {
        return memory.get(reservationId);
    }

}
