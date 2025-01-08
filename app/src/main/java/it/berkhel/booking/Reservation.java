package it.berkhel.booking;

import java.util.UUID;

public class Reservation {

    private String id = UUID.randomUUID().toString();

    public String getId() {
        return id;
    }

}
