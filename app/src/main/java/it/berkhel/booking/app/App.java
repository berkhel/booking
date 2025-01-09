package it.berkhel.booking.app;

import it.berkhel.booking.actionport.ForBooking;
import it.berkhel.booking.drivenport.ForStorage;
import it.berkhel.booking.entity.Reservation;

public class App implements ForBooking {

    public static App init(ForStorage storage){
        return new App(storage);
    }
    
    private ForStorage storage;

    private App(ForStorage storage) {
        this.storage = storage;
    }

    @Override
    public Reservation book(){
        Reservation reservation = new Reservation();
        storage.save(reservation);
        return reservation;
    }


}