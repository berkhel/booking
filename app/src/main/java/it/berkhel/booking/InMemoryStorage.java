package it.berkhel.booking;

import java.util.HashMap;
import java.util.Map;

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
