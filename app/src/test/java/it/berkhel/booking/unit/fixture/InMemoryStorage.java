package it.berkhel.booking.unit.fixture;

import java.util.HashMap;
import java.util.Map;

import it.berkhel.booking.app.drivenport.ForStorage;
import it.berkhel.booking.entity.Purchase;

public class InMemoryStorage implements ForStorage {

    private Map<String,Purchase> memory = new HashMap<>();

    @Override
    public void save(Purchase aReservation) {
        memory.put(aReservation.getId(), aReservation);
    }

    @Override
    public Purchase retrieveById(String reservationId) {
        return memory.get(reservationId);
    }

}
