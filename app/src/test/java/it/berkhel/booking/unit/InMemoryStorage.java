package it.berkhel.booking.unit;

import java.util.HashMap;
import java.util.Map;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Reservation;

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
